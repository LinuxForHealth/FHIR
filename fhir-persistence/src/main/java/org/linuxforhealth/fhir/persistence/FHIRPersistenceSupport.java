/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.generator.FHIRGenerator;
import org.linuxforhealth.fhir.model.generator.exception.FHIRGeneratorException;
import org.linuxforhealth.fhir.model.parser.FHIRJsonParser;
import org.linuxforhealth.fhir.model.parser.FHIRParser;
import org.linuxforhealth.fhir.model.parser.exception.FHIRParserException;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.util.FHIRUtil;
import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;
import org.linuxforhealth.fhir.persistence.util.InputOutputByteStream;
import org.linuxforhealth.fhir.search.SearchConstants;

/**
 * Collection of helper methods related to the persistence of FHIR resource payload data
 */
public class FHIRPersistenceSupport {
    // the logger to use for this class
    private static final Logger logger = Logger.getLogger(FHIRPersistenceSupport.class.getName());
    
    // initial buffer size for rendered payload
    private static final int DATA_BUFFER_INITIAL_SIZE = 10*1024; // 10KiB

    /**
     * Render the payload
     * @param resource
     * @param compress
     * @return
     */
    public static InputOutputByteStream render(Resource resource, boolean compress) throws FHIRGeneratorException, IOException {
        InputOutputByteStream ioStream = new InputOutputByteStream(DATA_BUFFER_INITIAL_SIZE);
        
        if (compress) {
            try (GZIPOutputStream zipStream = new GZIPOutputStream(ioStream.outputStream())) {
                FHIRGenerator.generator(Format.JSON, false).generate(resource, zipStream);
            } catch (IOException | FHIRGeneratorException x) {
                logger.log(Level.SEVERE, "Failed generating resource: '" + resource.getClass().getSimpleName() + "/" + resource.getId() + "'", x);
                throw x;
            }
        } else {
            // not compressed, so render directly to the ioStream
            try {
                FHIRGenerator.generator(Format.JSON, false).generate(resource, ioStream.outputStream());            
            } catch (FHIRGeneratorException x) {
                logger.log(Level.SEVERE, "Failed generating resource: '" + resource.getClass().getSimpleName() + "/" + resource.getId() + "'", x);
                throw x;
            }
        }
        return ioStream;
    }
    
    /**
     * Parse the given stream, using elements if needed
     * @param <T>
     * @param resourceType
     * @param in
     * @param elements
     * @param uncompress
     * @return
     */
    public static <T extends Resource> T parse(Class<T> resourceType, InputStream in, List<String> elements, boolean uncompress) throws FHIRParserException, IOException {
        T result;
        try {
            if (uncompress) {
                // Wrap the InputStream so we uncompress the content when reading...and
                // see we close the stream as required in the finally block
                in = new GZIPInputStream(in);
            }
            if (elements != null) {
                // parse/filter the resource using elements
                result = FHIRParser.parser(Format.JSON).as(FHIRJsonParser.class).parseAndFilter(in, elements);
                if (resourceType.equals(result.getClass()) && !FHIRUtil.hasTag(result, SearchConstants.SUBSETTED_TAG)) {
                    // add a SUBSETTED tag to this resource to indicate that its elements have been filtered
                    result = FHIRUtil.addTag(result, SearchConstants.SUBSETTED_TAG);
                }
            } else {
                result = FHIRParser.parser(Format.JSON).parse(in);
            }
        } finally {
            if (uncompress) {
                // make sure we always close the GZIPInputStream to avoid leaking resources it holds onto
                in.close();
            }
        }

        return result;
    }

    /**
     * Get the current time which can be used for the lastUpdated field
     * @return current time in UTC
     */
    public static org.linuxforhealth.fhir.model.type.Instant getCurrentInstant() {
        return org.linuxforhealth.fhir.model.type.Instant.now(ZoneOffset.UTC);
    }

    /**
     * Obtain the versionId value from the Resource meta element, converting
     * to an int for use by the persistence layer
     * @param resource
     * @return
     * @throws FHIRPersistenceException
     */
    public static int getMetaVersionId(Resource resource) throws FHIRPersistenceException {
        // Programming error if this is being called before the meta element has been set
        // properly on the resource
        if (resource.getMeta() == null || resource.getMeta().getVersionId() == null) {
            throw new FHIRPersistenceException("Resource missing meta versionId");
        }
        
        String versionIdValue = resource.getMeta().getVersionId().getValue();
        if (versionIdValue == null) {
            throw new FHIRPersistenceException("Resource missing meta versionId value");
        }
        return Integer.parseInt(versionIdValue);
    }

    /**
     * Get the lastUpdated value from a Resource as a {@link java.time.Instant}.
     * @param resource
     * @return
     */
    public static Instant getLastUpdatedFromResource(Resource resource) {
        org.linuxforhealth.fhir.model.type.Instant lastUpdated = resource.getMeta().getLastUpdated();
        return lastUpdated.getValue().toInstant();
    }

    /**
     * Get the lastUpdated time to use for the next version of a resource. If a current
     * version of the resource exists, pass its lastUpdated time as the currenLastUpdated
     * parameter (all times should be UTC).
     * @param currentLastUpdated
     * @return
     */
    public static org.linuxforhealth.fhir.model.type.Instant getNewLastUpdatedInstant(Instant currentLastUpdated) {
        Instant lastUpdated = Instant.now();
        // Clocks may drift or not be perfectly aligned in clusters. Updates for a given
        // resource may occur back-to-back and be processed on different nodes. We need
        // to make sure that the new lastUpdated time is always greater than the current
        // lastUpdated time so that these changes always appear in order. This can also
        // happen when virtual machines are migrated between nodes.
        if (currentLastUpdated != null && !lastUpdated.isAfter(currentLastUpdated)) {
            logger.warning("Current clock time " + lastUpdated 
                + " is not after the lastUpdated time of a current resource version: " + currentLastUpdated
                + ". If you see this message frequently, consider improving the accuracy of clock "
                + "synchronization in your cluster. Using current lastUpdated plus 1ms instead of "
                + "current time.");

            // our solution is to simply adjust lastUpdated so that it falls 1ms 
            // after the current lastUpdated value, thereby ensuring that
            // lastUpdated time always follows version order
            lastUpdated = currentLastUpdated.plusMillis(1);
        }
        return org.linuxforhealth.fhir.model.type.Instant.of(lastUpdated.atZone(ZoneOffset.UTC));
    }
}