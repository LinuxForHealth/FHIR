/*
 * (C) Copyright IBM Corp. 2016, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.util;

import static com.ibm.fhir.model.config.FHIRModelConfig.getToStringFormat;
import static com.ibm.fhir.model.config.FHIRModelConfig.getToStringIndentAmount;
import static com.ibm.fhir.model.config.FHIRModelConfig.getToStringPrettyPrinting;
import static com.ibm.fhir.model.type.String.string;
import static java.util.Objects.nonNull;

import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.config.FHIRModelConfig;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.model.resource.DomainResource;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.Uuid;
import com.ibm.fhir.model.type.code.BundleType;
import com.ibm.fhir.model.type.code.DataAbsentReason;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.visitor.Visitable;

import jakarta.json.Json;
import jakarta.json.JsonBuilderFactory;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;

/**
 * Utility methods for working with the FHIR object model.
 */
public class FHIRUtil {
    private static final Logger log = Logger.getLogger(FHIRUtil.class.getName());
    public static final Pattern REFERENCE_PATTERN = buildReferencePattern();
    public static final Extension DATA_ABSENT_REASON_UNKNOWN = Extension.builder()
            .url("http://hl7.org/fhir/StructureDefinition/data-absent-reason")
            .value(DataAbsentReason.UNKNOWN)
            .build();
    public static final com.ibm.fhir.model.type.String STRING_DATA_ABSENT_REASON_UNKNOWN = com.ibm.fhir.model.type.String.builder()
            .extension(DATA_ABSENT_REASON_UNKNOWN)
            .build();
    private static final JsonBuilderFactory BUILDER_FACTORY = Json.createBuilderFactory(null);
    public static final OperationOutcome ALL_OK = OperationOutcome.builder()
        .issue(Issue.builder()
        .severity(IssueSeverity.INFORMATION)
        .code(IssueType.INFORMATIONAL)
            .details(CodeableConcept.builder()
                .text(string("All OK"))
                .build())
            .build())
        .build();

    private FHIRUtil() { }

    /**
     * Loads the class in the classloader in order to initialize static members.
     * Call this before using the class in order to avoid a slight performance hit on first use.
     */
    public static void init() {
        // allows us to initialize this class during startup
    }

    /**
     * Converts a Visitable (Element or Resource) instance to a string using a FHIRGenerator.
     *
     * <p>The toString format (JSON or XML) can be specified through {@link FHIRModelConfig#setToStringFormat(Format)}.
     *
     * @param visitable
     *     the Element or Resource instance to be converted
     * @return
     *     the String version of the element or resource
     */
    public static String toString(Visitable visitable) {
        try {
            FHIRGenerator generator = FHIRGenerator.generator(getToStringFormat(), getToStringPrettyPrinting());
            if (generator.isPropertySupported(FHIRGenerator.PROPERTY_INDENT_AMOUNT)) {
                // indent amount is only supported by the XML generator and is only applicable if prettyPrinting is turned on
                generator.setProperty(FHIRGenerator.PROPERTY_INDENT_AMOUNT, getToStringIndentAmount());
            }
            StringWriter writer = new StringWriter();
            generator.generate(visitable, writer);
            return writer.toString();
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    private static Pattern buildReferencePattern() {
        StringBuilder sb = new StringBuilder();
        sb.append("((http|https)://([A-Za-z0-9\\\\\\/\\.\\:\\%\\$\\-])*)?(");
        sb.append(ModelSupport.getResourceTypes(false).stream()
                .map(Class::getSimpleName)
                .collect(Collectors.joining("|")));
        sb.append(")\\/([A-Za-z0-9\\-\\.]{1,64})(\\/_history\\/([A-Za-z0-9\\-\\.]{1,64}))?");
        return Pattern.compile(sb.toString());
    }

    // copy an immutable JsonObject into a mutable JsonObjectBuilder
    public static JsonObjectBuilder toJsonObjectBuilder(JsonObject jsonObject) {
        JsonObjectBuilder builder = BUILDER_FACTORY.createObjectBuilder();
        // JsonObject is a Map<String, JsonValue>
        for (String key : jsonObject.keySet()) {
            JsonValue value = jsonObject.get(key);
            builder.add(key, value);
        }
        return builder;
    }

    public static OperationOutcome.Issue buildOperationOutcomeIssue(String msg, IssueType code) {
        return buildOperationOutcomeIssue(IssueSeverity.FATAL, code, msg, "<empty>");
    }

    public static OperationOutcome.Issue buildOperationOutcomeIssue(IssueSeverity severity, IssueType code, String details) {
        return buildOperationOutcomeIssue(severity, code, details, null);
    }

    public static OperationOutcome.Issue buildOperationOutcomeIssue(IssueSeverity severity, IssueType code, String details,
            String expression) {
        if (details == null || details.isEmpty()) {
            details = "<no details>";
        }
        if (expression == null || expression.isEmpty()) {
            expression = "<no expression>";
        }
        return OperationOutcome.Issue.builder()
                .severity(severity)
                .code(code)
                .details(CodeableConcept.builder().text(string(details)).build())
                .expression(Collections.singletonList(string(expression)))
                .build();
    }

    /**
     * Build an OperationOutcome that contains the specified list of operation outcome issues.
     */
    public static OperationOutcome buildOperationOutcome(Collection<OperationOutcome.Issue> issues) {
        // If there are no issues, then return the ALL OK OperationOutcome
        if (issues == null || issues.isEmpty()) {
            return ALL_OK;
        }
        // Otherwise build an OperationOutcome and stuff the issues into it.
        return OperationOutcome.builder().issue(issues).build();
    }

    /**
     * Build an OperationOutcome with an id and a list of issues from exception e.
     */
    public static OperationOutcome buildOperationOutcome(FHIROperationException e, boolean includeCausedByClauses) {
        if (e.getIssues() != null && e.getIssues().size() > 0) {
            String id = e.getUniqueId();
            return buildOperationOutcome(e.getIssues()).toBuilder().id(id).build();
        } else {
            return buildOperationOutcome((FHIRException) e, includeCausedByClauses);
        }
    }

    /**
     * Build an OperationOutcome with an id from exception e and a single issue of type 'exception' and severity
     * 'fatal'.
     */
    public static OperationOutcome buildOperationOutcome(FHIRException e, boolean includeCausedByClauses) {
        String id = e.getUniqueId();
        return buildOperationOutcome((Exception) e, includeCausedByClauses).toBuilder().id(id).build();
    }

    /**
     * Build an OperationOutcome for the specified exception with a single issue of type 'exception' and severity
     * 'fatal'.
     */
    public static OperationOutcome buildOperationOutcome(Exception exception, boolean includeCausedByClauses) {
        return buildOperationOutcome(exception, null, null, includeCausedByClauses);
    }

    /**
     * Build an OperationOutcome for the specified exception.
     */
    public static OperationOutcome buildOperationOutcome(Exception exception, IssueType issueType, IssueSeverity severity,
            boolean includeCausedByClauses) {
        // First, build a set of exception messages to be included in the OperationOutcome.
        // We'll include the exception message from each exception in the hierarchy, following the "causedBy" exceptions.
        StringBuilder msgs = new StringBuilder();
        Throwable e = exception;
        String causedBy = "";
        while (e != null) {
            msgs.append(causedBy + e.getClass().getSimpleName() + ": "
                    + (e.getMessage() != null ? e.getMessage().replaceAll("<", "&lt;").replaceAll(">", "&gt;") : "&lt;null message&gt;"));
            e = e.getCause();
            causedBy = System.lineSeparator() + "Caused by: ";

            // Force an exit from the loop if the caller doesn't want the caused-by clauses added.
            if (!includeCausedByClauses) {
                e = null;
            }
        }
        return buildOperationOutcome(msgs.toString(), issueType, severity);
    }

    /**
     * Build an OperationOutcome for the specified exception.
     *
     * @param issueType
     *            defaults to IssueTypeList.EXCEPTION
     * @param severity
     *            defaults to IssueSeverityList.FATAL
     */
    public static OperationOutcome buildOperationOutcome(String message, IssueType issueType, IssueSeverity severity) {
        if (issueType == null) {
            issueType = IssueType.EXCEPTION;
        }
        if (severity == null) {
            severity = IssueSeverity.FATAL;
        }
        // Build an OperationOutcomeIssue that contains the exception messages.
        OperationOutcome.Issue ooi = OperationOutcome.Issue.builder()
                .severity(severity)
                .code(issueType)
                .details(CodeableConcept.builder().text(string(message)).build())
                .build();

        // Next, build the OperationOutcome.
        OperationOutcome oo = OperationOutcome.builder().issue(Collections.singletonList(ooi)).build();
        return oo;
    }

    /**
     * Builds a relative "Location" header value for the specified resource. This will be a string of the form
     * <code>"[resource-type]/[id]/_history/[version]"</code>. Note that the server will turn this into an absolute URL prior to
     * returning it to the client.
     *
     * @param type
     *            the resource type name
     * @param resource
     *            the resource for which the location header value should be returned
     */
    public static URI buildLocationURI(String type, Resource resource) {
        StringBuilder sb = new StringBuilder();
        sb.append(type);
        sb.append("/");
        sb.append(resource.getId());
        sb.append("/_history/");
        sb.append(resource.getMeta().getVersionId().getValue());
        return URI.create(sb.toString());
    }

    /**
     * Builds a relative "Location" header value for the specified resource type/id/version. This will be a string of the form
     * <code>"[resource-type]/[id]/_history/[version]"</code>. Note that the server will turn this into an absolute URL prior to
     * returning it to the client.
     * 
     * @param type the resource type name
     * @param id the resource logical id value
     * @param version the resource version
     * @return
     */
    public static URI buildLocationURI(String type, String id, int version) {
        StringBuilder sb = new StringBuilder();
        sb.append(type);
        sb.append("/");
        sb.append(id);
        sb.append("/_history/");
        sb.append(version);
        return URI.create(sb.toString());
    }

    /**
     * Resolve reference {@code ref} to a bundle entry or a resource contained within {@code resource} and return the
     * corresponding resource container. Resolving {@code ref} to a resource that exists outside of the bundle is not
     * yet supported, but this support may be added in the future.
     *
     * @throws Exception
     *             if the resource could not be found, the reference has no value, or the value does not match the
     *             expected format for a reference
     */
    public static Resource resolveReference(Reference ref, DomainResource resource, Bundle bundle, Bundle.Entry entry) throws Exception {
        switch (ReferenceType.of(ref)) {
        case CONTAINED:
            return resolveContainedReference(resource, ref);
        case ABSOLUTE_FHIR_URL:
        case RELATIVE_FHIR_URL:
        case ABSOLUTE_UUID:
        case ABSOLUTE_OID:
        case ABSOLUTE_OTHER_URL:
        case OTHER:
            Bundle.Entry targetEntry = resolveBundleReference(bundle, entry, ref);
            return targetEntry.getResource();
        case NO_REFERENCE_VALUE:
            throw new FHIRException("Reference must have a nonempty value to be resolved");
        case INVALID:
        default:
            throw new FHIRException("Cannot resolve invalid reference value " + ref.getReference().getValue());
        }
    }

    /**
     * Resolve the reference {@code ref} to a bundle entry and return the corresponding resource container
     *
     * @see https://www.hl7.org/fhir/r4/references.html#contained
     * @throws Exception
     *             if the resource could not be found, the reference has no value, or the value does not match the
     *             expected format for a contained reference
     */
    public static Resource resolveContainedReference(DomainResource resource, Reference ref) throws Exception {
        if (ref == null || ref.getReference() == null || ref.getReference().getValue() == null) {
            throw new FHIRException("Reference must have a nonempty value to be resolved");
        }
        String referenceUriString = ref.getReference().getValue();
        if (referenceUriString.startsWith("#")) {
            referenceUriString = referenceUriString.substring(1);
            List<Resource> containedResources = resource.getContained();
            for (Resource containedResource : containedResources) {
                String id = containedResource.getId();
                if (id != null) {
                    if (referenceUriString.equals(id)) {
                        return containedResource;
                    }
                }
            }
        }
        throw new FHIRException("Resource does not contain the referenced resource");
    }

    /**
     * Resolve the reference {@code ref} to an entry within {@code bundle} and return the corresponding resource
     *
     * @see https://www.hl7.org/fhir/r4/bundle.html#references
     * @param resourceType
     * @param bundle
     * @param sourceEntry
     *            allowed to be null if and only if the reference is absolute
     * @param ref
     * @throws Exception
     *             if the resource could not be found, the reference has no value, or the value does not match the
     *             expected format for a bundle reference
     * @throws ClassCastException
     *             if the referenced resource cannot be cast to type {@code resourceType}
     */
    @SuppressWarnings("unchecked")
    public static <T extends Resource> T resolveBundleReference(Class<T> resourceType, Bundle bundle, Bundle.Entry sourceEntry, Reference ref)
        throws Exception {
        Bundle.Entry targetEntry = resolveBundleReference(bundle, sourceEntry, ref);
        return (T) targetEntry.getResource();
    }

    /**
     * Resolve the reference {@code ref} to an entry within {@code bundle}
     *
     * @see https://www.hl7.org/fhir/r4/bundle.html#references
     * @param bundle
     * @param sourceEntry
     *            allowed to be null if and only if the reference is absolute
     * @param ref
     * @throws FHIRException
     *             if the resource could not be found or the reference has no value
     * @throws URISyntaxException
     *             if the {@code ref} value is not a valid URI
     * @throws IllegalArgumentException
     *             if {@code ref} contains a fragment reference
     */
    public static Bundle.Entry resolveBundleReference(Bundle bundle, Bundle.Entry sourceEntry, Reference ref) throws FHIRException, URISyntaxException {
        if (ref == null || ref.getReference() == null || ref.getReference().getValue() == null) {
            throw new FHIRException("Reference must have a nonempty value to be resolved");
        }

        String referenceUriString = ref.getReference().getValue();
        URI referenceUri = new URI(referenceUriString);
        if (!referenceUri.isAbsolute()) {
            if (referenceUriString.startsWith("#")) {
                throw new IllegalArgumentException("Cannot resolve fragment reference " + referenceUriString
                        + " to a BundleEntry. See resolveReference instead.");
            }

            // 1. If the reference is not an absolute reference, convert it
            Uri sourceEntryFullUrl = sourceEntry.getFullUrl();
            if (sourceEntryFullUrl != null) {
                String sourceEntryUriString = sourceEntryFullUrl.getValue();
                URI sourceEntryUri = new URI(sourceEntryUriString);
                if (!sourceEntryUri.isAbsolute()) {
                    throw new FHIRException("The Bundle entry that contains the reference must have an absolute fullUrl to resolve relative references");
                }
                // if the fullUrl of the resource that contains the reference is a RESTful one (see the RESTful URL
                // regex), extract the [base], and append the reference to it
                Matcher restUrlMatcher = REFERENCE_PATTERN.matcher(sourceEntryUriString);
                if (restUrlMatcher.matches() && restUrlMatcher.groupCount() > 0) {
                    String urlBase = restUrlMatcher.group(1);
                    referenceUriString = urlBase + referenceUriString;
                }
            }
            // otherwise, treat the fullUrl as a normal URL, and follow the normal method for Resolving Relative
            // References to Absolute Form
        }

        // If the reference is version specific (either relative or absolute),
        // then remove the version from the URL before matching fullUrl, and then match the version based on
        // Resource.meta.versionId
        String version = referenceUri.getFragment();
        if (version != null) {
            referenceUriString = referenceUriString.substring(0, referenceUriString.length() - version.length());
        }
        // 2. Look for an entry with a fullUrl that contains the URL in the reference
        for (Bundle.Entry entry : bundle.getEntry()) {
            Uri fullUrl = entry.getFullUrl();
            if (fullUrl != null) {
                String fullUrlValue = entry.getFullUrl().getValue();
                if (fullUrlValue != null && fullUrlValue.equals(referenceUriString)) {
                    try {
                        Resource resource = entry.getResource();
                        if (version != null && resource.getMeta() != null && resource.getMeta().getVersionId() != null) {
                            Id versionId = resource.getMeta().getVersionId();
                            if (version.equals(versionId.getValue())) {
                                return entry;
                            }
                        } else {
                            return entry;
                        }
                    } catch (Exception e) {
                        log.log(Level.SEVERE, "Unable to retrieve resource " + referenceUriString + " from the bundle", e);
                    }
                }
            }
        }
        // If no match is found, the resource is not in the bundle, and must be found elsewhere
        throw new FHIRException("Bundle does not contain the referenced resource and retrieval of resources outside the bundle is not supported.");
    }

    /**
     * Returns the string value of the specified extension element within the specified resource.
     *
     * @param resource
     * @param extensionUrl
     * @return the value of the first such extension with a valueString or null if the resource has no such extensions
     */
    public static String getExtensionStringValue(Resource resource, String extensionUrl) {
        String value = null;
        if (nonNull(resource) && nonNull(extensionUrl)) {
            if (resource instanceof DomainResource) {
                DomainResource dr = (DomainResource) resource;
                value = getExtensionStringValue(extensionUrl, dr.getExtension());
            }
        }
        return value;
    }

    /**
     * Returns the string value of the specified extension element within the specified element.
     *
     * @param element
     * @param extensionUrl
     * @return the value of the first such extension with a valueString or null if the resource has no such extensions
     */
    public static String getExtensionStringValue(Element element, String extensionUrl) {
        String value = null;
        if (nonNull(element) && nonNull(extensionUrl)) {
            value = getExtensionStringValue(extensionUrl, element.getExtension());
        }
        return value;
    }

    /**
     * @return the value of the first extension with a url of {@code extensionUrl} and a value of type
     *     {@code com.ibm.fhir.model.type.String} (or a subclass); null if the list has no such extensions
     */
    private static String getExtensionStringValue(String extensionUrl, List<Extension> extensions) {
        String value = null;
        for (Extension ext : extensions) {
            if (ext.getValue() != null && ext.getUrl().equals(extensionUrl) &&
                    ext.getValue().is(com.ibm.fhir.model.type.String.class)) {
                value = ext.getValue().as(com.ibm.fhir.model.type.String.class).getValue();
                break;
            }
        }
        return value;
    }

    public static boolean hasTag(Resource resource, Coding tag) {
        Objects.requireNonNull(resource);
        Objects.requireNonNull(tag);
        if (resource.getMeta() == null) {
            return false;
        }
        for (Coding t : resource.getMeta().getTag()) {
            // compare tags based on system/code
            // version and display are ignored
            if (tag.getSystem() != null &&
                tag.getSystem().equals(t.getSystem()) &&
                tag.getCode() != null &&
                tag.getCode().equals(t.getCode())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return a copy of resource {@code resource} with tag {@code tag}
     * @param <T>
     * @param resource
     *          the resource to add the tag too
     * @param tag
     *          the tag to add
     * @return a copy of the resource with the new tag (if it didn't already exist), or the resource passed in if the tag is already present
     */
    public static <T extends Resource> T addTag(T resource, Coding tag) {
        Objects.requireNonNull(resource);
        Objects.requireNonNull(tag);
        if (hasTag(resource, tag)) {
            return resource;
        }
        Meta meta = resource.getMeta();
        Meta.Builder metaBuilder = (meta == null) ? Meta.builder() : meta.toBuilder();
        // re-build resource with updated meta element
        @SuppressWarnings("unchecked")
        T updatedResource = (T) resource.toBuilder()
                                    .meta(metaBuilder
                                        .tag(tag)
                                        .build())
                                    .build();
        return updatedResource;
    }

    /**
     * Determine if any of the issues in the list of issues are failure issues
     *
     * @param issues
     * @return
     */
    public static boolean anyFailureInIssues(List<OperationOutcome.Issue> issues) {
        for (OperationOutcome.Issue issue : issues) {
            if (isFailure(issue.getSeverity())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determine if the given severity should be treated as a failure
     *
     * @param severity
     * @return
     */
    public static boolean isFailure(IssueSeverity severity) {
        switch (severity.getValueAsEnum()) {
        case INFORMATION:
        case WARNING:
            return false;
        default:
            return true;
        }
    }

    /**
     * Create a self-contained bundle from the passed map of resources, replacing Resource.id values and
     * references with a generated UUID.
     *
     * @param bundleType
     *            The type of bundle to create
     * @param resources
     *            A mapping from String identifiers to Resources. For resources with no logical id, the key can be any
     *            string
     * @return a Bundle with the passed resources with ids and references replaced by UUIDs
     */
    public static Bundle createStandaloneBundle(BundleType bundleType, Map<String,Resource> resources) {
        Map<String,String> localRefMap = new HashMap<>();

        List<Entry> entries = new ArrayList<>();
        for (String key : resources.keySet()) {
            Uuid uuid = Uuid.of("urn:uuid:" + UUID.randomUUID());
            localRefMap.put(key, uuid.getValue());
            entries.add(Entry.builder()
                .fullUrl(uuid)
                .resource(resources.get(key))
                .build());
        }

        Bundle bundle = Bundle.builder()
            .type(bundleType)
            .entry(entries)
            .build();

        ReferenceMappingVisitor<Bundle> referenceMappingVisitor = new ReferenceMappingVisitor<>(localRefMap);
        bundle.accept(referenceMappingVisitor);
        return referenceMappingVisitor.getResult();
    }
}
