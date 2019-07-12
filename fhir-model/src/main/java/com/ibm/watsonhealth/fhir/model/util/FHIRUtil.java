/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.util;

import static java.util.Objects.nonNull;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

import org.w3c.dom.Node;

import com.ibm.watsonhealth.fhir.exception.FHIRException;
import com.ibm.watsonhealth.fhir.exception.FHIROperationException;
import com.ibm.watsonhealth.fhir.model.format.Format;
import com.ibm.watsonhealth.fhir.model.generator.FHIRGenerator;
import com.ibm.watsonhealth.fhir.model.generator.exception.FHIRGeneratorException;
import com.ibm.watsonhealth.fhir.model.parser.FHIRJsonParser;
import com.ibm.watsonhealth.fhir.model.parser.FHIRParser;
import com.ibm.watsonhealth.fhir.model.parser.exception.FHIRParserException;
import com.ibm.watsonhealth.fhir.model.resource.Basic;
import com.ibm.watsonhealth.fhir.model.resource.Bundle;
import com.ibm.watsonhealth.fhir.model.resource.DomainResource;
import com.ibm.watsonhealth.fhir.model.resource.OperationOutcome;
import com.ibm.watsonhealth.fhir.model.resource.Resource;
import com.ibm.watsonhealth.fhir.model.resource.SubstanceSpecification.Code;
import com.ibm.watsonhealth.fhir.model.type.Base64Binary;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.Coding;
import com.ibm.watsonhealth.fhir.model.type.Date;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Decimal;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Instant;
import com.ibm.watsonhealth.fhir.model.type.IssueSeverity;
import com.ibm.watsonhealth.fhir.model.type.IssueType;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.ResourceType;
import com.ibm.watsonhealth.fhir.model.type.Time;
import com.ibm.watsonhealth.fhir.model.type.Uri;

public class FHIRUtil {
    private static final Logger log = Logger.getLogger(FHIRUtil.class.getName());
        
    /**
     * https://www.hl7.org/fhir/dstu2/references.html#regex
     */
    public static final Pattern REST_PATTERN = Pattern.compile("((http|https)://([A-Za-z0-9\\\\\\/\\.\\:\\%\\$\\-])*)?(Account|AllergyIntolerance|Appointment|AppointmentResponse|AuditEvent|Basic|Binary|BodySite|Bundle|CarePlan|Claim|ClaimResponse|ClinicalImpression|Communication|CommunicationRequest|Composition|ConceptMap|Condition|Conformance|Contract|Coverage|DataElement|DetectedIssue|Device|DeviceComponent|DeviceMetric|DeviceUseRequest|DeviceUseStatement|DiagnosticOrder|DiagnosticReport|DocumentManifest|DocumentReference|EligibilityRequest|EligibilityResponse|Encounter|EnrollmentRequest|EnrollmentResponse|EpisodeOfCare|ExplanationOfBenefit|FamilyMemberHistory|Flag|Goal|Group|HealthcareService|ImagingObjectSelection|ImagingStudy|Immunization|ImmunizationRecommendation|ImplementationGuide|List|Location|Media|Medication|MedicationAdministration|MedicationDispense|MedicationOrder|MedicationStatement|MessageHeader|NamingSystem|NutritionOrder|Observation|OperationDefinition|OperationOutcome|Order|OrderResponse|Organization|Patient|PaymentNotice|PaymentReconciliation|Person|Practitioner|Procedure|ProcedureRequest|ProcessRequest|ProcessResponse|Provenance|Questionnaire|QuestionnaireResponse|ReferralRequest|RelatedPerson|RiskAssessment|Schedule|SearchParameter|Slot|Specimen|StructureDefinition|Subscription|Substance|SupplyDelivery|SupplyRequest|TestScript|ValueSet|VisionPrescription)\\/[A-Za-z0-9\\-\\.]{1,64}(\\/_history\\/[A-Za-z0-9\\-\\.]{1,64})?");

    private static final Map<String, Class<?>> RESOURCE_TYPE_MAP = buildResourceTypeMap();

    private FHIRUtil() { }

    public static void init() {
        // allows us to initialize this class during startup
    }
    
    public static boolean isStandardResourceType(String name) {
        return RESOURCE_TYPE_MAP.containsKey(name);
    }
    
    public static Class<?> getResourceType(String name) {
        return RESOURCE_TYPE_MAP.get(name);
    }

    private static Map<String, Class<?>> buildResourceTypeMap() {
        Map<String, Class<?>> resourceTypeMap = new LinkedHashMap<>();
        for (ResourceType.ValueSet value : ResourceType.ValueSet.values()) {
            String resourceTypeName = value.value();
            try {
                Class<?> resourceTypeClass = Class.forName("com.ibm.watsonhealth.fhir.model.resource." + resourceTypeName);
                resourceTypeMap.put(resourceTypeName, resourceTypeClass);
            } catch (Exception e) {
                throw new Error(e);
            }
        }
        return resourceTypeMap;
    }
    
    public static String getTypeName(Class<?> type) {
        String typeName = type.getSimpleName();
        if (Code.class.isAssignableFrom(type)) {
            typeName = "code";
        } else if (isPrimitiveType(type)) {
            typeName = typeName.substring(0, 1).toLowerCase() + typeName.substring(1);
        }
        return typeName;
    }
    
    public static boolean isPrimitiveType(Class<?> type) {
        return Base64Binary.class.equals(type) ||
            com.ibm.watsonhealth.fhir.model.type.Boolean.class.equals(type) ||
            com.ibm.watsonhealth.fhir.model.type.String.class.isAssignableFrom(type) || 
            Uri.class.isAssignableFrom(type) ||
            DateTime.class.equals(type) || 
            Date.class.equals(type) ||
            Time.class.equals(type) || 
            Instant.class.equals(type) || 
            com.ibm.watsonhealth.fhir.model.type.Integer.class.isAssignableFrom(type) || 
            Decimal.class.equals(type);
    }

    /**
     * Read JSON from InputStream {@code stream} and parse it into a FHIR resource.
     * Non-mandatory elements which are not in {@code elementsToInclude} will be filtered out.
     * @param stream
     * @param elements a list of element names to include in the returned resource; null to skip filtering
     * @param lenient
     * @param validating
     * @return a fhir-model resource containing mandatory elements and the elements requested (if they are present in the JSON)
     */
    @SuppressWarnings("unchecked")
    public static <T extends Resource> T readAndFilterJson(Class<T> resourceType, InputStream stream, List<String> elementsToInclude) throws FHIRException {
        try {
            FHIRJsonParser parser = FHIRParser.parser(Format.JSON).as(FHIRJsonParser.class);
            parser.reset();
            Resource resource = parser.parseAndFilter(stream, elementsToInclude);
            return (T) resource;
        } catch (FHIRParserException e) {
            throw new FHIRException(e.getMessage() + ", location: '" + e.getPath() + "'", e);
        }
    }

    /**
     * Read a FHIR resource from {@code reader} in the requested {@code format}.
     */
    public static <T extends Resource> T read(Class<T> resourceType, Format format, Reader reader) throws FHIRException {
        if (Format.XML.equals(format)) {
            throw new UnsupportedOperationException();
        } else {
            // Format.JSON.equals(format)
            return readAndFilterJson(resourceType, reader, null);
        }
    }

    /**
     * Read JSON from {@code reader} and parse it into a FHIR resource.
     * Non-mandatory elements which are not in {@code elementsToInclude} will be filtered out.
     * @param reader
     * @param elements a list of element names to include in the returned resource; null to skip filtering
     * @return a fhir-model resource containing mandatory elements and the elements requested (if they are present in the JSON)
     */
    @SuppressWarnings("unchecked")
    public static <T extends Resource> T readAndFilterJson(Class<T> resourceType, Reader reader, List<String> elementsToInclude) throws FHIRException {
        try {
            FHIRJsonParser parser = FHIRParser.parser(Format.JSON).as(FHIRJsonParser.class);
            parser.reset();
            Resource resource = parser.parseAndFilter(reader, elementsToInclude);
            return (T) resource;
        } catch (FHIRParserException e) {
            throw new FHIRException(e.getMessage() + ", location: '" + e.getPath() + "'", e);
        }
    }

    public static <T extends Resource> T read(Node node) {
        throw new UnsupportedOperationException();
    }

    public static <T extends Resource> T toResource(Class<T> resourceType, JsonObject jsonObject) throws FHIRParserException{
        FHIRJsonParser parser = FHIRParser.parser(Format.JSON).as(FHIRJsonParser.class);
        parser.reset();
        return parser.parse(jsonObject);
    }

    public static <T extends Element> T toElement(Class<T> elementType, JsonObject jsonObject) {
        throw new UnsupportedOperationException();
    }

    /**
     * Write a resource in XML or JSON to a given output stream, without pretty-printing.
     * This method will close the output stream after writing to it, so passing System.out / System.err is discouraged.
     */
    public static <T extends Resource> void write(T resource, Format format, OutputStream stream) throws FHIRException {
        write(resource, format, stream, false);
    }

    /**
     * Write a resource in XML or JSON to a given output stream, with an option to pretty-print the output.
     * This method will close the output stream after writing to it, so passing System.out / System.err is discouraged.
     */
    public static <T extends Resource> void write(T resource, Format format, OutputStream stream, boolean formatted) throws FHIRException {
        try {
            FHIRGenerator.generator(format, formatted).generate(resource, stream);
        } catch (FHIRGeneratorException e) {
            throw new FHIRException(e.getMessage(), e);
        }
    }

    /**
     * Write a resource in XML or JSON using the passed writer, without pretty-printing.
     * This method will close the writer after writing to it.
     */
    public static <T extends Resource> void write(T resource, Format format, Writer writer) throws FHIRException {
        write(resource, format, writer, false);
    }

    /**
     * Write a resource in XML or JSON using the passed writer, with an option to pretty-print the output.
     * This method will close the writer after writing to it.
     */
    public static <T extends Resource> void write(T resource, Format format, Writer writer, boolean formatted) throws FHIRException {
        try {
            FHIRGenerator.generator(format, formatted).generate(resource, writer);
        } catch (Exception e) {
            throw new FHIRException(e.getMessage(), e);
        }
    }

    public static JsonObject toJsonObject(Resource resource) throws FHIRException {
        // write Resource to String
        StringWriter writer = new StringWriter();
        write(resource, Format.JSON, writer);
        String jsonString = writer.toString();

        // read JsonObject from String
        return Json.createReader(new StringReader(jsonString)).readObject();
    }

    public static JsonObjectBuilder toJsonObjectBuilder(Resource resource) throws FHIRException {
        return toJsonObjectBuilder(toJsonObject(resource));
    }

    // copy an immutable JsonObject into a mutable JsonObjectBuilder
    public static JsonObjectBuilder toJsonObjectBuilder(JsonObject jsonObject) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        // JsonObject is a Map<String, JsonValue>
        for (String key : jsonObject.keySet()) {
            JsonValue value = jsonObject.get(key);
            builder.add(key, value);
        }
        return builder;
    }

    public static OperationOutcome.Issue buildOperationOutcomeIssue(String msg, IssueType.ValueSet code) {
        return buildOperationOutcomeIssue(IssueSeverity.ValueSet.FATAL, code, msg, null);
    }

    public static OperationOutcome.Issue buildOperationOutcomeIssue(IssueSeverity.ValueSet severity, IssueType.ValueSet code, String diagnostics, String location) {
        return OperationOutcome.Issue.builder(IssueSeverity.of(severity), IssueType.of(code))
                .diagnostics(com.ibm.watsonhealth.fhir.model.type.String.of(diagnostics))
                .location(Collections.singletonList(com.ibm.watsonhealth.fhir.model.type.String.of(location))).build();
    }

    /**
     * Build an OperationOutcome that contains the specified list of operation outcome issues.
     */
    public static OperationOutcome buildOperationOutcome(List<OperationOutcome.Issue> issues) {
        // Build an OperationOutcome and stuff the issues into it.
        return OperationOutcome.builder(issues).build();
    }

    /**
     * Build an OperationOutcome with an id and a list of issues from exception e.
     */
    public static OperationOutcome buildOperationOutcome(FHIROperationException e, boolean includeCausedByClauses) {
        if (e.getIssues() != null && e.getIssues().size() > 0) {
            Id id = Id.builder().value(e.getUniqueId()).build();
            return buildOperationOutcome(e.getIssues()).toBuilder().id(id).build();
        } else {
            return buildOperationOutcome((FHIRException) e, includeCausedByClauses);
        }
    }

    /**
     * Build an OperationOutcome with an id from exception e and a single issue of type 'exception' and severity 'fatal'.
     */
    public static OperationOutcome buildOperationOutcome(FHIRException e, boolean includeCausedByClauses) {
        Id id = Id.builder().value(e.getUniqueId()).build();
        return buildOperationOutcome((Exception) e, includeCausedByClauses).toBuilder().id(id).build();
    }

    /**
     * Build an OperationOutcome for the specified exception with a single issue of type 'exception' and severity 'fatal'.
     */
    public static OperationOutcome buildOperationOutcome(Exception exception, boolean includeCausedByClauses) {
        return buildOperationOutcome(exception, null, null, includeCausedByClauses);
    }

    /**
     * Build an OperationOutcome for the specified exception.
     */
    public static OperationOutcome buildOperationOutcome(Exception exception, IssueType.ValueSet issueType, IssueSeverity.ValueSet severity, boolean includeCausedByClauses) {
        // First, build a set of exception messages to be included in the OperationOutcome.
        // We'll include the exception message from each exception in the hierarchy,
        // following the "causedBy" exceptions.
        StringBuilder msgs = new StringBuilder();
        Throwable e = exception;
        String causedBy = "";
        while (e != null) {
            msgs.append(causedBy + e.getClass().getSimpleName() + ": " + (e.getMessage() != null ? e.getMessage() : "<null message>"));
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
     * @param issueType defaults to IssueTypeList.EXCEPTION
     * @param severity defaults to IssueSeverityList.FATAL
     */
    public static OperationOutcome buildOperationOutcome(String message, IssueType.ValueSet issueType, IssueSeverity.ValueSet severity) {
        if (issueType == null) {
            issueType = IssueType.ValueSet.PROCESSING;
        }
        if (severity == null) {
            severity = IssueSeverity.ValueSet.FATAL;
        }
        // Build an OperationOutcomeIssue that contains the exception messages.
        OperationOutcome.Issue ooi = OperationOutcome.Issue.builder(IssueSeverity.of(severity), IssueType.of(issueType))
                .diagnostics(com.ibm.watsonhealth.fhir.model.type.String.of(message))
                .build();

        // Next, build the OperationOutcome.
        OperationOutcome oo = OperationOutcome.builder(Collections.singletonList(ooi)).build();
        return oo;
    }

    /**
     * Builds a relative "Location" header value for the specified resource. This will be a string of the form
     * "<resource-type>/<id>/_history/<version>". Note that the server will turn this into an absolute URL prior to
     * returning it to the client.
     *
     * @param resource
     *            the resource for which the location header value should be returned
     */
    public static URI buildLocationURI(String type, Resource resource) {
        String resourceTypeName = resource.getClass().getSimpleName();
        if (!resourceTypeName.equals(type)) {
            resourceTypeName = type;
        }
        return URI.create(resourceTypeName + "/" + resource.getId().getValue()
            + "/_history/" + resource.getMeta().getVersionId().getValue());
    }

    /**
     * Resolve reference {@code ref} to a bundle entry or a resource contained within {@code resource} and return the corresponding resource container.
     * Resolving {@code ref} to a resource that exists outside of the bundle is not yet supported, but this support may be added in the future.
     * @throws Exception if the resource could not be found, the reference has no value, or the value does not match the expected format for a reference
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
     * @see https://www.hl7.org/fhir/DSTU2/references.html#contained
     * @throws Exception if the resource could not be found, the reference has no value, or the value does not match the expected format for a contained reference
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
                Id id = containedResource.getId();
                if (id != null) {
                    if (referenceUriString.equals(id.getValue())) {
                        return containedResource;
                    }
                }
            }
        }
        throw new FHIRException("Resource does not contain the referenced resource");
    }

    /**
     * Resolve the reference {@code ref} to an entry within {@code bundle} and return the corresponding resource
     * @see https://www.hl7.org/fhir/dstu2/bundle.html#references
     * @param resourceType
     * @param bundle
     * @param sourceEntry allowed to be null if and only if the reference is absolute
     * @param ref
     * @throws Exception 
     *        if the resource could not be found, the reference has no value, or the value does not match the expected format for a bundle reference
     * @throws ClassCastException
     *        if the referenced resource cannot be cast to type {@code resourceType}
     */
    @SuppressWarnings("unchecked")
    public static <T extends Resource> T resolveBundleReference(Class<T> resourceType, Bundle bundle, Bundle.Entry sourceEntry, Reference ref) throws Exception {
        Bundle.Entry targetEntry = resolveBundleReference(bundle, sourceEntry, ref);
        return (T) targetEntry.getResource();
    }

    /**
     * Resolve the reference {@code ref} to an entry within {@code bundle}
     * @see https://www.hl7.org/fhir/dstu2/bundle.html#references
     * @param bundle
     * @param sourceEntry allowed to be null if and only if the reference is absolute
     * @param ref
     * @throws FHIRException if the resource could not be found or the reference has no value
     * @throws URISyntaxException if the {@code ref} value is not a valid URI
     * @throws IllegalArgumentException if {@code ref} contains a fragment reference
     */
    public static Bundle.Entry resolveBundleReference(Bundle bundle, Bundle.Entry sourceEntry, Reference ref) throws FHIRException, URISyntaxException {
        if (ref == null || ref.getReference() == null || ref.getReference().getValue() == null) {
            throw new FHIRException("Reference must have a nonempty value to be resolved");
        }

        String referenceUriString = ref.getReference().getValue();
        URI referenceUri = new URI(referenceUriString);
        if (!referenceUri.isAbsolute()) {
            if (referenceUriString.startsWith("#")) {
                throw new IllegalArgumentException("Cannot resolve fragment reference " + referenceUriString + " to a BundleEntry. See resolveReference instead.");
            }

            // 1. If the reference is not an absolute reference, convert it
            Uri sourceEntryFullUrl = sourceEntry.getFullUrl();
            if (sourceEntryFullUrl != null) {
                String sourceEntryUriString = sourceEntryFullUrl.getValue();
                URI sourceEntryUri = new URI(sourceEntryUriString);
                if (!sourceEntryUri.isAbsolute()) {
                    throw new FHIRException("The Bundle entry that contains the reference must have an absolute fullUrl to resolve relative references");
                }
                // if the fullUrl of the resource that contains the reference is a RESTful one (see the RESTful URL regex), extract the [base], and append the reference to it
                Matcher restUrlMatcher = REST_PATTERN.matcher(sourceEntryUriString);
                if (restUrlMatcher.matches() && restUrlMatcher.groupCount() > 0) {
                    String urlBase = restUrlMatcher.group(1);
                    referenceUriString = urlBase + referenceUriString;
                }
            }
            // otherwise, treat the fullUrl as a normal URL, and follow the normal method for Resolving Relative References to Absolute Form
        }

        //If the reference is version specific (either relative or absolute),
        //then remove the version from the URL before matching fullUrl, and then match the version based on Resource.meta.versionId
        String version = referenceUri.getFragment();
        if (version != null) {
            referenceUriString = referenceUriString.substring(0, referenceUriString.length() - version.length());
        }
        // 2. Look for an entry with a fullUrl that contains the URL in the reference
        for (Bundle.Entry entry : bundle.getEntry()) {
            Uri fullUrl = entry.getFullUrl();
            if (fullUrl != null){
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
                    } catch(Exception e) {
                        log.log(Level.SEVERE,"Unable to retrieve resource " + referenceUriString + " from the bundle", e);
                    }
                }
            }
        }
        // If no match is found, the resource is not in the bundle, and must be found elsewhere
        throw new FHIRException("Bundle does not contain the referenced resource and retrieval of resources outside the bundle is not supported.");
    }

 
    /**
     * Returns the string value of the specified extension element within
     * the specified resource.
     * @param resource
     * @param extensionUrl
     * @return the value of the first such extension with a valueString or null if the resource has no such extensions
     */
    public static String getExtensionStringValue(Resource resource, String extensionUrl) {
        if (nonNull(resource) && nonNull(extensionUrl)) {
            if (DomainResource.class.isAssignableFrom(resource.getClass())) {
                DomainResource dr = (DomainResource) resource;
                for (Extension ext : dr.getExtension()) {
                    if (ext.getUrl() != null && ext.getValue() != null
                            && ext.getUrl().equals(extensionUrl)) {
                        return ext.getValue().as(com.ibm.watsonhealth.fhir.model.type.String.class).getValue();
                    }
                }
            }
        }
        return null;
    }

    /**
     * Determines if the passed Resource contains the passed Meta tag.
     * @param resource The Resource to be examined.
     * @param searchTag - The tag to be searched for.
     * @return boolean - true if the Resource contains the passed tag; false otherwise.
     */
    public static boolean containsTag(Resource resource, Coding searchTag) {
        boolean tagFound = false;

        if (nonNull(resource) && nonNull(resource.getMeta()) && nonNull(resource.getMeta().getTag())) {
            for (Coding tag : resource.getMeta().getTag()) {
                if (nonNull(tag.getSystem()) && tag.getSystem().getValue().equals(searchTag.getSystem().getValue()) &&
                    nonNull(tag.getCode()) && tag.getCode().getValue().equals(searchTag.getCode().getValue())) {
                    tagFound = true;
                    break;
                }
            }
        }
        return tagFound;
    }
    
 
    // add for FHIRResource.java
    private static final String BASIC_RESOURCE_TYPE_URL = "http://ibm.com/watsonhealth/fhir/basic-resource-type";
    
    /**
     * Returns the resource type (as a String) of the specified resource.   For a virtual resource,
     * this will be the actual virtual resource type (not Basic).
     * @param resource the resource
     * @return the name of the resource type associated with the resource
     */
    public static String getResourceTypeName(Resource resource) {
        if (resource instanceof Basic) {
            Basic basic = (Basic) resource;
            CodeableConcept cc = basic.getCode();
            if (cc != null) {
                List<Coding> codingList = cc.getCoding();
                if (codingList != null) {
                    for (Coding coding : codingList) {
                        if (coding.getSystem() != null) {
                            String system = coding.getSystem().getValue();
                            if (BASIC_RESOURCE_TYPE_URL.equals(system)) {
                                return coding.getCode().getValue();
                            }
                        }
                    }
                }
            }
        }

        return resource.getClass().getSimpleName();
    }
    
    public static List<String> getResourceTypeNames() {
        List<String> typeNameList = new ArrayList<String>();
        typeNameList.addAll(RESOURCE_TYPE_MAP.keySet());
        return typeNameList;
    }
}
