/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.davinci.hrex.provider.strategy;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Coverage;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Address;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.ContactPoint;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.visitor.DefaultVisitor;
import com.ibm.fhir.model.visitor.Visitable;
import com.ibm.fhir.operation.davinci.hrex.provider.strategy.MemberMatchResult.ResponseType;
import com.ibm.fhir.server.spi.operation.FHIROperationUtil;
import com.ibm.fhir.validation.FHIRValidator;
import com.ibm.fhir.validation.exception.FHIRValidationException;

/**
 * Per the HREX IG, the $member-match operation does the following:
 * 1. HealthPlan (Linked) makes a request to $member-match with:
 *      - demographic details
 *      - coverage to match
 *      - coverage to link (e.g. the HealthPlan that is to be enrolled or currently enrolled)
 * 2. HealthPlan (Match) checks the incoming Parameters validates against the profile.
 * 3. HealthPlan (Match) executes a local Search to find the Coverage
 * 4. HealhPlan (Match) (optionally) augments the Coverage details on the local system with a LINK.
 *
 * @implNote the following search parameters are needed.
 * Patient = identifier (USUAL, OFFICIAL), telecom, name, address, address-city, address-state, address-postalcode, address-country, gender, birthdate (uses eq), language
 * Coverage = patient, subscriber, payor, subscriber-id, identifier, beneficiary
 */
public class DefaultMemberMatchStrategy extends AbstractMemberMatch {

    private static final Logger LOG = Logger.getLogger(DefaultMemberMatchStrategy.class.getName());

    private Patient memberPatient;
    private Coverage coverageToMatch;
    private Coverage coverageToLink;

    @Override
    public String getMemberMatchIdentifier() {
        return "default";
    }

    @Override
    public void validate(Parameters input) throws FHIROperationException {
        /*
         * We need to validate the profile on the subsegments.
         */
        for (Parameters.Parameter parameter : input.getParameter()) {
            String name = parameter.getName().getValue();
            if ("MemberPatient".equals(name)) {
                validateResource(FHIRValidator.validator(), Patient.class, parameter.getResource(), "http://hl7.org/fhir/us/core/StructureDefinition/us-core-patient|3.1.1");
                memberPatient = (Patient) parameter.getResource();
            } else if ("CoverageToMatch".equals(name)) {
                validateResource(FHIRValidator.validator(), Coverage.class, parameter.getResource(), "http://hl7.org/fhir/us/davinci-hrex/StructureDefinition/hrex-coverage");
                coverageToMatch = (Coverage) parameter.getResource();
            } else if ("CoverageToLink".equals(name)) {
                validateResource(FHIRValidator.validator(), Coverage.class, parameter.getResource(), "http://hl7.org/fhir/us/davinci-hrex/StructureDefinition/hrex-coverage");
                coverageToLink = (Coverage) parameter.getResource();
            }
        }

        // Member Patient must exist
        if (memberPatient == null) {
            throw FHIROperationUtil.buildExceptionWithIssue("Must include the MemberPatient Parameter", IssueType.INVALID);
        }

        // Coverage To Match must exist
        if (coverageToMatch == null) {
            throw FHIROperationUtil.buildExceptionWithIssue("Must include the CoverageToMatch Parameter", IssueType.INVALID);
        }
    }

    /**
     * validates the resource (we do this as we don't yet support nested discriminators)
     *
     * @param cls
     * @param resource
     * @param profile
     * @return
     * @throws FHIROperationException
     */
    public void validateResource(FHIRValidator validator, Class<? extends Resource> cls, Resource resource, String profile) throws FHIROperationException {
        // Resource is null
        if (resource == null) {
            throw FHIROperationUtil.buildExceptionWithIssue("Unexpected missing resource for '" + profile + "'", IssueType.INVALID);
        }

        // Invalid Resource Type
        if (!resource.is(cls)) {
            throw FHIROperationUtil.buildExceptionWithIssue("Resource Type of the Parameter for '" + profile + "' is invalid", IssueType.INVALID);
        }

        /*
         * Validate and fail-fast
         */
        try {
            List<Issue> issues = validator.validate(resource, profile);
            for (Issue issue : issues) {
                switch(issue.getSeverity().getValueAsEnum()) {
                    case FATAL:
                    case ERROR:
                        throw FHIROperationUtil.buildExceptionWithIssue("Failed to validate against the input " + issue.getDiagnostics(), IssueType.INVALID);
                    default:
                        continue;
                }
            }
        } catch (FHIRValidationException fve) {
            throw FHIROperationUtil.buildExceptionWithIssue("Failed to validate against the input", IssueType.EXCEPTION, fve);
        }
    }

    @Override
    public MemberMatchResult executeMemberMatch() throws FHIROperationException {
        LOG.entering(this.getClass().getName(), "executeMemberMatch");
        MemberMatchPatientSearchCompiler patientCompiler = new MemberMatchPatientSearchCompiler();
        memberPatient.accept(patientCompiler);

        GetPatientIdentifier getPatientIdentifier = new GetPatientIdentifier();
        try {
            // Compartment / CompartmentId is OK to be null in this case as we are not doing any includes
            // defined by the customer, it's all in the Compiler.
            String requestUri = FHIRRequestContext.get().getOriginalRequestUri();
            if (LOG.isLoggable(Level.FINE)) {
                LOG.fine("Search Parameters used to find the member in 'Patient' " + patientCompiler.getSearchParameters());
            }
            Bundle patientBundle = resourceHelper().doSearch("Patient", null, null, patientCompiler.getSearchParameters(), requestUri);
            int size = patientBundle.getEntry().size();
            if (size == 0) {
                returnNoMatchException();
                return MemberMatchResult.builder()
                        .responseType(ResponseType.NO_MATCH)
                        .build();
            } else if (size != 1) {
                returnMultipleMatchesException();
                return MemberMatchResult.builder()
                        .responseType(ResponseType.MULTIPLE)
                        .build();
            }

            String patientReference = "Patient/" + patientBundle.getEntry().get(0).getResource().getId();

            // Compiles the Coverage Search Parameters
            MemberMatchCovergeSearchCompiler coverageCompiler = new MemberMatchCovergeSearchCompiler(patientReference);
            coverageToMatch.accept(coverageCompiler);

            // essentially a search on beneficiary
            if (LOG.isLoggable(Level.FINE)) {
                LOG.fine("Search Parameters used to find the member's Coverage " + coverageCompiler.getSearchParameters());
            }
            Bundle coverageBundle = resourceHelper().doSearch("Coverage", null, null, coverageCompiler.getSearchParameters(), requestUri);

            if (coverageBundle.getEntry().isEmpty()) {
                // This may warrant a separate exception in custom implementations.
                // Warning you may want to carefully consider this as it may give more details than desired.
                returnNoMatchException();
                return MemberMatchResult.builder()
                        .responseType(ResponseType.NO_MATCH)
                        .build();
            }

            Patient patient = patientBundle.getEntry().get(0).getResource().as(Patient.class);
            patient.accept(getPatientIdentifier);

            /*
             * QA: Since there is filtering on the codesystem and value, the MB has to exist.
             * if it doesn't there is no MATCH.
             */
            if (getPatientIdentifier.getSystem() == null || getPatientIdentifier.getValue() == null) {
                returnNoMatchException();
                return MemberMatchResult.builder()
                        .responseType(ResponseType.NO_MATCH)
                        .build();
            }
        } catch (FHIROperationException foe){
            throw foe;
        } catch (Exception ex) {
            LOG.throwing(getClass().getSimpleName(), "executeMemberMatch", ex);
            throw FHIROperationUtil.buildExceptionWithIssue("Error executing the MemberMatch", IssueType.EXCEPTION, ex);
        }

        updateLinkedCoverageResource();

        LOG.exiting(this.getClass().getName(), "executeMemberMatch");
        return MemberMatchResult.builder()
                    .responseType(ResponseType.SINGLE)
                    .type("http://terminology.hl7.org/CodeSystem/v2-0203", "MB")
                    .system(getPatientIdentifier.getSystem())
                    .value(getPatientIdentifier.getValue())
                    .build();
    }

    /**
     * updates the linked coverage resource (note, this is a WRITE, and it not contianed in this implementation.
     * @implNote for specific implementations and business logic, one should update the coverage in the local system.
     */
    private void updateLinkedCoverageResource() {
        // Technically, a NOP
        LOG.fine(() -> "Coverage to link is included =[" + (coverageToLink != null) + "]");
    }

    /**
     * Gets the FIRST Patient Identifier.
     */
    public static class GetPatientIdentifier extends DefaultVisitor {
        private String system;
        private String value;

        /**
         * public constructor which automatically enables child element processing.
         */
        public GetPatientIdentifier() {
            super(true);
        }

        @Override
        public boolean visit(String elementName, int elementIndex, Identifier identifier) {
            // SearchParameter: identifier
            // value is a guard that protects against selecting more than one value/system.
            if (value == null && "identifier".equals(elementName)) {
                if (identifier.getUse() != null) {
                    switch(identifier.getUse().getValueAsEnum()) {
                        case USUAL:
                        case OFFICIAL:
                            addIdValue(identifier);
                        break;
                        default:
                            LOG.fine("Identifier contains an unexpected use [" + identifier.getUse().getValueAsEnum() + "]");
                    }
                } else {
                    addIdValue(identifier);
                }
            }
            return false;
        }

        /**
         * adds the id value to the set.
         * @param identifier
         */
        private void addIdValue(Identifier identifier) {
            if (identifier.getType() != null
                    && identifier.getType().getCoding() != null) {
                // If there is  more than one Coding it has to be MB.
                for (Coding coding : identifier.getType().getCoding()) {
                    if (coding.getSystem() != null
                            && "http://terminology.hl7.org/CodeSystem/v2-0203"
                                    .equals(coding.getSystem().getValue())
                            && coding.getCode() != null
                            && "MB".equals(coding.getCode().getValue())) {
                        // We only want to extract the code system v2-0203 with MB
                        Uri sys = identifier.getSystem();
                        com.ibm.fhir.model.type.String val = identifier.getValue();
                        if (sys != null && sys.getValue() != null) {
                            this.system = sys.getValue();
                        }
                        if (val != null && val.getValue() != null) {
                            this.value = val.getValue();
                        }
                    }
                }
            }
        }

        /**
         * @return the system
         */
        public String getSystem() {
            return system;
        }

        /**
         * @return the value
         */
        public String getValue() {
            return value;
        }
    }

    /**
     * Enables the Processing of a Patient Resource into a MultivaluedMap, which is subsequently used for the Search Operation.
     * Relevant Structure Definition is at {@link http://hl7.org/fhir/us/core/STU3.1.1/StructureDefinition-us-core-patient.html}
     *
     * @implNote as there are no SearchParameters for us-core-race, us-core-ethnicity, us-core-birthsex these elements in
     * the US Core Patient profile are ignored. These Extensions may be an area of future enhancement. Other than these extensions,
     * only MUST SUPPORT elements are considered.
     */
    public static class MemberMatchPatientSearchCompiler extends DefaultVisitor {
        private MultivaluedMap<String,String> searchParams = new MultivaluedHashMap<String,String>();
        private Set<String> telecom = new HashSet<>();
        private Set<String> given = new HashSet<>();
        private Set<String> family = new HashSet<>();
        private Set<String> ids = new HashSet<>();

        private Set<String> addressLine = new HashSet<>();
        private Set<String> addressCity = new HashSet<>();
        private Set<String> addressState = new HashSet<>();
        private Set<String> addressPostalCode = new HashSet<>();
        private Set<String> addressCountry = new HashSet<>();

        private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        /**
         * public constructor which automatically enables child element processing.
         */
        public MemberMatchPatientSearchCompiler() {
            super(true);
        }

        /**
         * gets the search parameters
         * @return
         */
        public MultivaluedMap<String,String> getSearchParameters() {

            MultivaluedMap<String,String> temp = new MultivaluedHashMap<String, String>(searchParams);
            if (!telecom.isEmpty()) {
                String val = telecom.stream().collect(Collectors.joining(","));
                temp.add("telecom", val);
            }
            if (!given.isEmpty()) {
                String val = given.stream().collect(Collectors.joining(","));
                temp.add("name", val);
            }
            if (!family.isEmpty()) {
                String val = family.stream().collect(Collectors.joining(","));
                temp.add("name", val);
            }
            if (!ids.isEmpty()) {
                String val = ids.stream().collect(Collectors.joining(","));
                temp.add("identifier", val);
            }

            /*
             * Add the Address SearchParameters
             */
            if (!addressLine.isEmpty()) {
                String val = addressLine.stream().collect(Collectors.joining(","));
                temp.add("address", val);
            }
            if (!addressCity.isEmpty()) {
                String val = addressCity.stream().collect(Collectors.joining(","));
                temp.add("address-city", val);
            }
            if (!addressState.isEmpty()) {
                String val = addressState.stream().collect(Collectors.joining(","));
                temp.add("address-state", val);
            }
            if (!addressPostalCode.isEmpty()) {
                String val = addressPostalCode.stream().collect(Collectors.joining(","));
                temp.add("address-postalcode", val);
            }
            if (!addressCountry.isEmpty()) {
                String val = addressCountry.stream().collect(Collectors.joining(","));
                temp.add("address-country", val);
            }
            return temp;
        }

        @Override
        public boolean visit(String elementName, int elementIndex, Identifier identifier) {
            // SearchParameter: identifier
            if ("identifier".equals(elementName)) {
                if (identifier.getUse() != null) {
                    switch(identifier.getUse().getValueAsEnum()) {
                        case USUAL:
                        case OFFICIAL:
                            addIdValue(identifier);
                        break;
                        default:
                            LOG.fine("Identifier contains an unexected use " + identifier.getUse().getValueAsEnum());
                    }
                } else {
                    addIdValue(identifier);
                }
            }
            return false;
        }

        /**
         * adds the id value to the set.
         * @param identifier
         */
        private void addIdValue(Identifier identifier) {
            Uri system = identifier.getSystem();
            com.ibm.fhir.model.type.String value = identifier.getValue();
            if (system != null
                    && system.getValue() != null
                    && value != null
                    && value.getValue() != null) {
                ids.add(system.getValue() + "|" + value.getValue());
            }
        }

        @Override
        public boolean visit(String elementName, int elementIndex, HumanName humanName) {
            // SearchParameter: name
            if ("name".equals(elementName)) {
                if (humanName.getFamily() != null && humanName.getFamily().getValue() != null) {
                    family.add(humanName.getFamily().getValue());
                }
                if (humanName.getGiven() != null && !humanName.getGiven().isEmpty()) {
                    for (com.ibm.fhir.model.type.String hn : humanName.getGiven()) {
                        if (hn != null && hn.getValue() != null) {
                            given.add(hn.getValue());
                        }
                    }
                }
            }
            return false;
        }

        @Override
        public boolean visit(String elementName, int elementIndex, ContactPoint contactPoint) {
            // SearchParameter: contactPoint
            if ("telecom".equals(elementName)
                    && contactPoint.getValue() != null
                    && contactPoint.getValue().getValue() != null) {
                telecom.add(contactPoint.getValue().getValue());
            }
            return false;
        }

        @Override
        public boolean visit(String elementName, int elementIndex, Code code) {
            // SearchParameter: gender
            if ("gender".equals(elementName) && code.getValue() != null) {
                String gender = code.getValue();
                searchParams.add("gender", gender);
            }
            return false;
        }

        @Override
        public boolean visit(String elementName, int elementIndex, Date date) {
            // SearchParameter: birthdate
            if ("birthDate".equals(elementName) && date.getValue() != null) {
                TemporalAccessor acc = date.getValue();
                searchParams.add("birthdate", "eq" + formatter.format(acc));
            }
            return false;
        }

        @Override
        public boolean visit(String elementName, int elementIndex, Address address) {
            // SearchParameter: address, address-city, address-country, address-postalcode, address-state
            if ("address".equals(elementName)) {
                if (address.getUse() != null) {
                    // Only pick a subset of addresses (OLD, TEMP are not helpful)
                    switch (address.getUse().getValueAsEnum()) {
                        case HOME:
                        case WORK:
                        case BILLING:
                            addAddress(address);
                        break;
                        default:
                            LOG.fine("address contains an unexected use " + address.getUse().getValueAsEnum());
                    }
                } else {
                    addAddress(address);
                }
            }
            return false;
        }

        /**
         *
         * @param address (not-null)
         */
        private void addAddress(Address address) {
            // address
            List<com.ibm.fhir.model.type.String> lines = address.getLine();
            if (!lines.isEmpty()) {
                String tmpLines = lines
                        .stream()
                        .map(l -> l.getValue())
                        .filter(l -> !Objects.isNull(l))
                        .collect(Collectors.joining(" "));
                if (!tmpLines.trim().isEmpty()) {
                    addressLine.add(tmpLines);
                }
            }

            // address-city
            if (address.getCity() != null && address.getCity().getValue() != null) {
                this.addressCity.add(address.getCity().getValue());
            }

            // address-country
            if (address.getCountry() != null && address.getCountry().getValue() != null) {
                this.addressCountry.add(address.getCountry().getValue());
            }

            // address-postalcode
            if (address.getPostalCode() != null && address.getPostalCode().getValue() != null) {
                this.addressPostalCode.add(address.getPostalCode().getValue());
            }

            // address-state
            if (address.getState() != null && address.getState().getValue() != null) {
                this.addressState.add(address.getState().getValue());
            }
        }

        @Override
        public boolean visit(String elementName, int elementIndex, BackboneElement backboneElement) {
            // SearchParameter: language
            if ("communication".equals(elementName) && backboneElement instanceof Patient.Communication) {
                Patient.Communication comms = backboneElement.as(Patient.Communication.class);
                CodeableConcept language = comms.getLanguage();
                Set<String> vals = new HashSet<>();
                if (language != null) {
                    for (Coding coding : language.getCoding()) {
                        if (coding.getSystem() != null && coding.getCode() != null) {
                            String system = coding.getSystem().getValue();
                            String code = coding.getCode().getValue();
                            vals.add(system + "|" + code);
                        } else {
                            LOG.fine("Unexpected data with coding " + coding);
                        }
                    }
                }
                // Values
                if (!vals.isEmpty()) {
                    String val = vals.stream().collect(Collectors.joining(","));
                    searchParams.put("language", Arrays.asList(val));
                }
            }
            return false;
        }

        @Override
        public boolean visit(java.lang.String elementName, int elementIndex, Resource resource) {
            // contained resources shouldn't be processed.
            if ("contained".equals(elementName)){
                return false;
            }
            return visit(elementName, elementIndex, (Visitable) resource);
        }
    }

    /**
     * Compiles the Coverage Search for the Patient.
     *
     * @implNote Coverage is a bit unique here.
     * It's the CoverageToMatch - details of prior health plan coverage provided by the member,
     * typically from their health plan coverage card and has dubious provenance.
     *
     * It's also worth mentioning don't fall into the trap of Coverage.type and Coverage.class.type.
     * These are not equivalent, and should not be searched in lieu of each other.
     */
    public static class MemberMatchCovergeSearchCompiler extends DefaultVisitor {
        private MultivaluedMap<String,String> searchParams = new MultivaluedHashMap<String,String>();
        private String subscriber;
        private Set<String> ids = new HashSet<>();

        /**
         * public constructor which automatically enables child element processing.
         */
        public MemberMatchCovergeSearchCompiler(String subscriber) {
            super(true);
            this.subscriber = subscriber;
        }

        /**
         * gets the search parameters
         * @return
         */
        public MultivaluedMap<String,String> getSearchParameters() {
            MultivaluedMap<String,String> temp = new MultivaluedHashMap<String,String>(searchParams);

            if (!ids.isEmpty()) {
                String val = ids.stream().collect(Collectors.joining(","));
                temp.add("identifier", val);
            }

            // The subscriber in $member-match is not necessarily the patient (a.k.a. Coverage.beneficiary).
            searchParams.add("beneficiary", subscriber);
            return temp;
        }

        @Override
        public boolean visit(String elementName, int elementIndex, Identifier identifier) {
            // SearchParameter: identifier
            if ("identifier".equals(elementName)) {
                if (identifier.getUse() != null) {
                    switch(identifier.getUse().getValueAsEnum()) {
                        case USUAL:
                        case OFFICIAL:
                            addIdValue(identifier);
                        break;
                        default:
                            LOG.fine("Identifier contains an unexected use " + identifier.getUse().getValueAsEnum());
                    }
                } else {
                    addIdValue(identifier);
                }
            }
            return false;
        }

        /**
         * adds the id value to the set.
         * @param identifier
         */
        private void addIdValue(Identifier identifier) {
            Uri system = identifier.getSystem();
            com.ibm.fhir.model.type.String value = identifier.getValue();
            if (system != null
                    && system.getValue() != null
                    && value != null
                    && value.getValue() != null) {
                // It may be that we have to remove the system as it may be implied on the system.
                ids.add(system.getValue() + "|" + value.getValue());
            }
        }

        @Override
        public boolean visit(String elementName, int elementIndex, Reference reference) {
            if ("beneficiary".equals(elementName)
                    && reference.getReference() != null
                    && reference.getReference().getValue() != null) {
                // Plan beneficiary
                searchParams.put("patient", Arrays.asList(reference.getReference().getValue()));
            } else if ("payor".equals(elementName)
                    && reference.getReference() != null
                    && reference.getReference().getValue() != null) {
                //Issuer of the Policy
                searchParams.put("payor", Arrays.asList(reference.getReference().getValue()));
            } else if ("subscriber".equals(elementName)
                    && reference.getReference() != null
                    && reference.getReference().getValue() != null) {
                // Reference to the subscriber
                searchParams.put("subscriber", Arrays.asList(reference.getReference().getValue()));
            }
            return false;
        }

        @Override
        public boolean visit(String elementName, int elementIndex, com.ibm.fhir.model.type.String string) {
            if ("subscriberId".equals(elementName)) {
                // HREX has a custom search parameter - subscriber-id.
                searchParams.put("subscriber-id", Arrays.asList(string.getValue()));
            }
            return false;
        }

        @Override
        public boolean visit(java.lang.String elementName, int elementIndex, Resource resource) {
            // contained resources shouldn't be processed.
            if ("contained".equals(elementName)){
                return false;
            }
            return visit(elementName, elementIndex, (Visitable) resource);
        }
    }
}