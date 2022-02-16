/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bucket.scanner;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.bucket.api.IResourceEntryProcessor;
import com.ibm.fhir.bucket.api.ResourceEntry;
import com.ibm.fhir.bucket.cos.COSClient;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.generator.exception.FHIRGeneratorException;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.model.resource.Bundle.Entry.Request;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.Url;
import com.ibm.fhir.model.type.code.BundleType;
import com.ibm.fhir.model.type.code.HTTPVerb;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.model.util.ReferenceMappingVisitor;


/**
 * Breaks a large Bundle into smaller Bundles, translating local references to external references to
 * maintain the relationships between the resources when they are loaded into the FHIR server. This
 * is experimental, and primarily used for analyzing the performance of different bundle sizes.
 */
public class BundleBreakerResourceProcessor implements IResourceEntryProcessor {
    private static final Logger logger = Logger.getLogger(BundleBreakerResourceProcessor.class.getName());
    private static final String LOCAL_REF_URN_PREFIX = "urn:";
    private static final String LOCAL_REF_RESOURCE_PREFIX = "resource:";

    // to write the processed bundles back to COS
    private final COSClient cosClient;

    // The maximum number of resources we allow into a single bundle
    private final int maxBundleSize;

    // The COS bucket we want to use to store the resource bundle fragments
    private final String targetBucket;

    // The COS key prefix where we want to store everything
    private final String targetPrefix;

    /**
     * Public constructor
     * @param cosClient the client for writing new bundles to COS
     * @param maxBundleSize max number of resources we want in a bundle
     * @param targetPrefix target location (COS key prefix)
     */
    public BundleBreakerResourceProcessor(COSClient cosClient, int maxBundleSize, String targetBucket, String targetPrefix) {
        this.cosClient = cosClient;
        this.maxBundleSize = maxBundleSize;
        this.targetBucket = targetBucket;
        this.targetPrefix = targetPrefix == null ? "" : targetPrefix;
    }

    @Override
    public void process(ResourceEntry re) {
        boolean success = false;
        try {
            Resource r = re.getResource();
            if (r.is(Bundle.class)) {
                process(re.getJob().getObjectKey(), r.as(Bundle.class));
                success = true;
            } else {
                logger.info("Skipping non-bundle resource: " + r.getClass().getSimpleName());
            }
        } finally {
            // Signal the processing is complete for this entry
            re.getJob().operationComplete(success);
        }
    }

    /**
     * See fhir-persistence-jdbc TimestampPrefixedUUID.
     * TODO refactor to use a common class without having to
     * include fhir-persistence-jdbc as a dependency here.
     * @return
     */
    private String createNewIdentityValue() {
        // It's OK to use milli-time here. It doesn't matter too much if the time changes
        // because we're not using the timestamp to determine uniqueness in any way. The
        // timestamp prefix is purely to help push index writes to the right hand side
        // of the btree, minimizing the number of physical reads likely required
        // during ingestion when an index is too large to be fully cached.
        long millis = System.currentTimeMillis();

        // String encoding. Needs to collate correctly, so don't use any
        // byte-based encoding which would be sensitive to endian issues. For simplicity,
        // hex is sufficient, although a custom encoding using the full character set
        // supported by FHIR identifiers would be a little more compact (== smaller indexes).
        // Do not use Base64.
        String prefix = Long.toHexString(millis);

        UUID uuid = UUID.randomUUID();

        StringBuilder result = new StringBuilder();
        result.append(prefix);
        result.append("-"); // redundant, but more visually appealing.
        result.append(uuid.toString());
        return result.toString();
    }


    /**
     * Process the bundle
     * @param bundle
     */
    protected void process(String originalName, Bundle bundle) {
        // convert urn references to client-asserted logical ids. This is required
        // to keep the relationships valid when the resources span multiple bundles
        final Map<String,String> localRefMap = new HashMap<>();
        if (bundle.getEntry().size() <= this.maxBundleSize) {
            // Just copy the bundle as-is to the target
            saveBundle(originalName, bundle, -1);
        } else {
            // Build the map of local to external identifiers and break the bundle
            // into smaller bundles, translating POSTs into PUTs with externally
            // defined identifiers
            int bundleCount = 0;
            int resourceCount = 0;
            Bundle.Builder bb = newBundleBuilder();

            // Process every resource so that we can generate all the new
            // identifiers first, before we make the second pass and
            // generate the individual bundles with external identifiers
            for (Entry requestEntry: bundle.getEntry()) {

                String localIdentifier = retrieveLocalIdentifier(requestEntry);
                Resource resource = requestEntry.getResource();
                if (localIdentifier != null) {
                    if (HTTPVerb.PUT.equals(requestEntry.getRequest().getMethod())) {
                        // For PUTs, the external id must be already in the resource.
                        addLocalRefMapping(localRefMap, localIdentifier, resource);
                    } else if (HTTPVerb.POST.equals(requestEntry.getRequest().getMethod())) {
                        // For POSTs we need to create a new external id value which will
                        // be used for a PUT in the new bundle
                        String logicalId = createNewIdentityValue();
                        String externalRef = ModelSupport.getTypeName(resource.getClass()) + "/" + logicalId;

                        if (logger.isLoggable(Level.FINE)) {
                            logger.fine("Creating POST-to-PUT mapping " + localIdentifier + " --> " + externalRef);
                        }
                        localRefMap.put(localIdentifier, externalRef);
                    } else if (HTTPVerb.DELETE.equals(requestEntry.getRequest().getMethod())) {
                        // Make sure we add the local to external reference to the map in case another resource
                        // uses that reference
                        addLocalRefMapping(localRefMap, localIdentifier, resource);
                    }
                }
            }

            // Now iterate over the list again, building the new (smaller) bundles as we go
            for (Entry requestEntry: bundle.getEntry()) {

                // Update any references internal to the resource
                Resource resource = requestEntry.getResource();

                String localId = retrieveLocalIdentifier(requestEntry);
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("Processing localId: " + localId);
                }
                Entry.Builder entryBuilder = null;
                if (localId != null) {

                    if (HTTPVerb.PUT.equals(requestEntry.getRequest().getMethod())) {
                        // PUT remapped to PUT. The local id is already an external identifier
                        ReferenceMappingVisitor<Resource> visitor =
                                new ReferenceMappingVisitor<Resource>(localRefMap, localId);
                        resource.accept(visitor);
                        resource = visitor.getResult();

                        // Make sure the fullUrl matches the resource id
                        String url = ModelSupport.getTypeName(resource.getClass()) + "/" + resource.getId();
                        entryBuilder = Entry.builder();
                        entryBuilder.resource(resource);
                        entryBuilder.fullUrl(Uri.of(url));
                        entryBuilder.request(Request.builder().method(HTTPVerb.PUT).url(Url.of(url)).build());
                    } else if (HTTPVerb.POST.equals(requestEntry.getRequest().getMethod())) {
                        // POST remapped to PUT
                        // As well as updating local references, we also need to replace the resource.id value
                        // with the new identity we created earlier
                        String externalId = localRefMap.get(localId);
                        if (externalId != null) {
                            String newId = externalId.substring(externalId.lastIndexOf('/')+1);
                            IdReferenceMappingVisitor visitor = new IdReferenceMappingVisitor(localRefMap, localId, newId);
                            resource.accept(visitor);
                            resource = visitor.getResult();

                            entryBuilder = Entry.builder();
                            entryBuilder.resource(resource);
                            entryBuilder.fullUrl(Uri.of(externalId));
                            entryBuilder.request(Request.builder().method(HTTPVerb.PUT).url(Url.of(externalId)).build());
                        } else {
                            logger.warning("external reference missing for localId: " + localId);
                        }
                    } else if (HTTPVerb.DELETE.equals(requestEntry.getRequest().getMethod())) {
                        // Update any local references contained within the resource
                        ReferenceMappingVisitor<Resource> visitor =
                                new ReferenceMappingVisitor<Resource>(localRefMap, localId);
                        resource.accept(visitor);
                        resource = visitor.getResult();

                        entryBuilder = Entry.builder();
                        entryBuilder.resource(resource);

                        // The localId is already a qualified resourceType/id
                        entryBuilder.fullUrl(Uri.of(localId));
                        entryBuilder.request(Request.builder().method(HTTPVerb.DELETE).url(Url.of(localId)).build());
                    }

                    // If we built a new entry, add it to the current output bundle
                    if (entryBuilder != null) {
                        bb.entry(entryBuilder.build());

                        // Each time we hit the max number of resources we want in a bundle,
                        // save it off and start a new one
                        if (++resourceCount == this.maxBundleSize) {
                            resourceCount = 0;
                            saveBundle(originalName, bb.build(), bundleCount++);
                            bb = newBundleBuilder();
                        }
                    }
                }
            }

            // process the final chunk of resources
            if (resourceCount > 0) {
                saveBundle(originalName, bb.build(), bundleCount++);
            }
        }
    }

    /**
     * @return a new BundleBuilder instance with BundleType.TRANSACTION
     */
    private Bundle.Builder newBundleBuilder() {
        Bundle.Builder result = Bundle.builder();
        result.type(BundleType.TRANSACTION);
        return result;
    }

    /**
     * Save the newly created bundle to COS. If bundleNumber is negative,
     * this means that the bundle is the entire bundle and so does not need
     * to be saved with a postfixed name
     * @param newBundle the sub-Bundle generated from the larger bundle
     * @param bundleNumber incrementing number for each sub-Bundle we generate
     */
    protected void saveBundle(String originalName, Bundle newBundle, int bundleNumber) {
        int posn = originalName.lastIndexOf(".json");
        if (posn < 0) {
            posn = originalName.lastIndexOf(".JSON");
        }

        if (posn < 0) {
            logger.warning("Invalid bundle name: " + originalName);
            throw new IllegalArgumentException("Expecting bundle name to end in .json or .JSON");
        }

        String bundleName;
        if (bundleNumber < 0) {
            // Bundle isn't modified, so just use the original name under
            // the new prefix
            bundleName = String.format("%s/%s", this.targetPrefix, originalName);
        } else {
            // This is part of the original bundle, so make up a new name
            // and put it under the new prefix
            bundleName = String.format("%s/%s_SUB%03d.json", this.targetPrefix, originalName.substring(0, posn), bundleNumber);
        }

        writeBundle(newBundle, bundleName);
    }

    /**
     * Write the given bundle to COS
     * @param bundle
     * @param bundleName
     */
    protected void writeBundle(Bundle bundle, String bundleName) {
        ByteArrayOutputStream os = new ByteArrayOutputStream(4096);
        try {
            FHIRGenerator.generator(Format.JSON, false).generate(bundle, os);
        } catch (FHIRGeneratorException e) {
            throw new IllegalStateException(e);
        }

        final String payload = new String(os.toByteArray(), StandardCharsets.UTF_8);
        cosClient.write(targetBucket, bundleName, payload);
    }

    /**
     * This method will retrieve the local identifier associated with the specified bundle request entry, or return null
     * if the fullUrl field is not specified or doesn't contain a local identifier.
     *
     * @param requestEntry
     *            the bundle request entry
     * @param localRefMap
     *            the Map containing the local-to-external reference mappings
     * @return
     */
    private String retrieveLocalIdentifier(Bundle.Entry bundleEntry) {

        String localIdentifier = null;
        if (bundleEntry.getFullUrl() != null) {
            String fullUrl = bundleEntry.getFullUrl().getValue();
            if (fullUrl != null) {
                if (fullUrl.startsWith(LOCAL_REF_URN_PREFIX)) {
                    // e.g. urn:uuid:12345678-abcd-45e1-b5ef-75b155dde2b7
                    localIdentifier = fullUrl;
                } else if (fullUrl.startsWith(LOCAL_REF_RESOURCE_PREFIX)) {
                    // e.g. resource:1
                    localIdentifier = fullUrl;
                }
            }
        }
        return localIdentifier;
    }

    /**
     * This method will add a mapping to the local-to-external identifier map if the specified localIdentifier is
     * non-null.
     *
     * @param localRefMap
     *            the map containing the local-to-external identifier mappings
     * @param localIdentifier
     *            the localIdentifier previously obtained for the resource
     * @param resource
     *            the resource for which an external identifier will be built
     */
    private void addLocalRefMapping(Map<String, String> localRefMap, String localIdentifier, Resource resource) {
        if (localIdentifier != null) {
            String externalIdentifier =
                    ModelSupport.getTypeName(resource.getClass()) + "/" + resource.getId();
            localRefMap.put(localIdentifier, externalIdentifier);

            if (logger.isLoggable(Level.FINER)) {
                logger.finer("Added local/ext identifier mapping: " + localIdentifier + " --> "
                        + externalIdentifier);
            }
        }
    }

}
