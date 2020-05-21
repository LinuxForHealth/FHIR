/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.omop.generator.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import com.ibm.fhir.model.resource.Condition;
import com.ibm.fhir.model.resource.DomainResource;
import com.ibm.fhir.model.resource.Encounter;
import com.ibm.fhir.model.resource.Encounter.Hospitalization;
import com.ibm.fhir.model.resource.Medication;
import com.ibm.fhir.model.resource.Medication.Batch;
import com.ibm.fhir.model.resource.MedicationRequest;
import com.ibm.fhir.model.resource.MedicationRequest.DispenseRequest;
import com.ibm.fhir.model.resource.MedicationStatement;
import com.ibm.fhir.model.resource.Organization;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Practitioner;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.ValueSet;
import com.ibm.fhir.model.type.Address;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Decimal;
import com.ibm.fhir.model.type.Dosage;
import com.ibm.fhir.model.type.Duration;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.SimpleQuantity;
import com.ibm.fhir.model.type.UnsignedInt;
import com.ibm.fhir.model.type.code.AdministrativeGender;
import com.ibm.fhir.model.util.FHIRUtil;
import com.ibm.fhir.omop.generator.Generator;
import com.ibm.fhir.omop.mapping.Mapping;
import com.ibm.fhir.omop.model.Concept;
import com.ibm.fhir.omop.table.Table;
import com.ibm.fhir.omop.vocab.VocabService;
import com.ibm.fhir.profile.ValueSetSupport;

public abstract class GeneratorImpl implements Generator {
    protected static final int OMOP_NO_MATCHING_CONCEPT = 0;
    protected static final int OMOP_GENDER_FEMALE = 8532;
    protected static final int OMOP_GENDER_MALE = 8507;
    protected static final int OMOP_ETHNICITY_HISPANIC_OR_LATINO = 38003563;
    protected static final int OMOP_ETHNICITY_NOT_HISPANIC_OR_LATINO = 38003564;
    protected static final int OMOP_RACE_AMERICAN_INDIAN_OR_ALASKA_NATIVE = 8657;
    protected static final int OMOP_RACE_ASIAN = 8515;
    protected static final int OMOP_RACE_BLACK_OR_AFRICAN_AMERICAN = 8516;
    protected static final int OMOP_RACE_NATIVE_HAWAIIAN_OR_OTHER_PACIFIC_ISLANDER = 8557;
    protected static final int OMOP_RACE_WHITE = 8527;

    protected static final Set<Character> ROMAN_NUMERAL_CHARACTERS = new HashSet<>(Arrays.asList('i', 'I', 'v', 'V', 'x', 'X', 'l', 'L', 'c', 'C', 'd', 'D', 'm', 'M'));

    // code system to vocabulary id map
    protected static final Map<String, String> CODE_SYSTEM_MAP = new HashMap<>();
    static {
        CODE_SYSTEM_MAP.put("http://snomed.info/sct", "SNOMED");
        CODE_SYSTEM_MAP.put("http://hl7.org/fhir/sid/cvx", "CVX");
        CODE_SYSTEM_MAP.put("http://www.nlm.nih.gov/research/umls/rxnorm", "RxNorm");
        CODE_SYSTEM_MAP.put("http://loinc.org", "LOINC");
        CODE_SYSTEM_MAP.put("http://www.ama-assn.org/go/cpt", "CPT4");
        CODE_SYSTEM_MAP.put("http://unitsofmeasure.org", "UCUM");
        CODE_SYSTEM_MAP.put("http://hl7.org/fhir/ndfrt", "NDFRT");
        CODE_SYSTEM_MAP.put("http://hl7.org/fhir/sid/ndc", "NDC");
        CODE_SYSTEM_MAP.put("http://nucc.org/provider-taxonomy", "NUCC");
        CODE_SYSTEM_MAP.put("http://hl7.org/fhir/sid/icd-10-cm", "ICD10CM");
        CODE_SYSTEM_MAP.put("http://hl7.org/fhir/sid/icd-10", "ICD10");
        CODE_SYSTEM_MAP.put("http://hl7.org/fhir/sid/icd-9-cm", "ICD9CM");
    }
    protected static final Map<String, String> STATE_CODE_MAP = buildStateCodeMap();

    // FHIR reference <resource type>/<logical id> to primary key (integer) map
    protected final Map<String, Long> referenceMap = new HashMap<>();
    protected final Set<String> processed = new HashSet<>();
    protected final VocabService vocabService;

    protected long sequence = 0;

    public GeneratorImpl(VocabService vocabService) {
        this.vocabService = Objects.requireNonNull(vocabService);
    }

    @Override
    public Map<Table, List<String>> generate(Mapping mapping, Map<Class<? extends Resource>, List<Resource>> resourceMap) {
        Objects.requireNonNull(mapping);
        Objects.requireNonNull(resourceMap);

        Map<Table, List<String>> tableDataMap = new HashMap<>();

        generate(mapping, resourceMap, tableDataMap);

        return tableDataMap;
    }

    @Override
    public void generate(Mapping mapping, Map<Class<? extends Resource>, List<Resource>> resourceMap, Map<Table, List<String>> tableDataMap) {
        Objects.requireNonNull(mapping);
        Objects.requireNonNull(resourceMap);
        Objects.requireNonNull(tableDataMap);

        validateResourceMap(mapping, resourceMap);

        switch (mapping) {
        case CONDITION_TO_CONDITION_OCCURRENCE: {
            List<Condition> conditions = getResources(resourceMap, Condition.class);
            List<String> conditionOccurrenceTableData = getTableData(tableDataMap, Table.CONDITION_OCCURRENCE);
            conditions.stream()
                .filter(condition -> !processed.contains(reference(condition)))
                .forEach(condition -> generate(condition, conditionOccurrenceTableData));
            break;
        }
        case ENCOUNTER_TO_VISIT_OCCURRENCE: {
            List<Encounter> encounters = getResources(resourceMap, Encounter.class);
            List<String> visitOccurrenceTableData = getTableData(tableDataMap, Table.VISIT_OCCURRENCE);
            encounters.stream()
                .filter(encounter -> !processed.contains(reference(encounter)))
                .forEach(encounter -> generate(encounter, visitOccurrenceTableData));
            break;
        }
        case MEDICATION_STATEMENT_AND_MEDICATION_REQUEST_AND_MEDICATION_TO_DRUG_EXPOSURE: {
            List<MedicationStatement> medicationStatements = getResources(resourceMap, MedicationStatement.class);
            List<MedicationRequest> medicationRequests = getResources(resourceMap, MedicationRequest.class);
            List<Medication> medications = getResources(resourceMap, Medication.class);
            List<String> drugExposureTableData = getTableData(tableDataMap, Table.DRUG_EXPOSURE);
            medicationStatements.stream()
                .filter(medicationStatement -> !processed.contains(reference(medicationStatement)))
                .forEach(medicationStatement -> generate(medicationStatement, findMedicationRequest(medicationStatement, medicationRequests), findMedication(medicationStatement, medications), drugExposureTableData));
            break;
        }
        case ORGANIZATION_TO_CARE_SITE_AND_LOCATION: {
            List<Organization> organizations = getResources(resourceMap, Organization.class);
            List<String> careSiteTableData = getTableData(tableDataMap, Table.CARE_SITE);
            List<String> locationTableData = getTableData(tableDataMap, Table.LOCATION);
            organizations.stream()
                .filter(organization -> !processed.contains(reference(organization)))
                .forEach(organization -> generate(organization, careSiteTableData, locationTableData));
            break;
        }
        case PATIENT_TO_PERSON_AND_LOCATION: {
            List<Patient> patients = getResources(resourceMap, Patient.class);
            List<String> personTableData = getTableData(tableDataMap, Table.PERSON);
            List<String> locationTableData = getTableData(tableDataMap, Table.LOCATION);
            patients.stream()
                .filter(patient -> !processed.contains(reference(patient)))
                .forEach(patient -> generate(patient, personTableData, locationTableData));
            break;
        }
        case PRACTITIONER_TO_PROVIDER: {
            List<Practitioner> practitioners = getResources(resourceMap, Practitioner.class);
            List<String> providerTableData = getTableData(tableDataMap, Table.PROVIDER);
            practitioners.stream()
                .filter(practitioner -> !processed.contains(reference(practitioner)))
                .forEach(practitioner -> generate(practitioner, providerTableData));
            break;
        }
        case ADVERSE_EVENT_TO_DEATH: {
            // TODO
            break;
        }
        case LOCATION_TO_CARE_SITE: {
            // TODO
            break;
        }
        case LOCATION_TO_LOCATION: {
            // TODO
            break;
        }
        case OBSERVATION_TO_OBSERVATION: {
            // TODO
            break;
        }
        case PROCEDURE_TO_DEVICE_EXPOSURE: {
            // TODO
            break;
        }
        case PROCEDURE_TO_PROCEDURE_OCCURRENCE: {
            // TODO
            break;
        }
        case SPECIMEN_TO_SPECIMEN: {
            // TODO
            break;
        }
        default:
            break;
        }
    }

    @Override
    public Set<String> getProcessedReferences() {
        return processed;
    }

    @Override
    public Map<String, Long> getReferenceMap() {
        return Collections.unmodifiableMap(referenceMap);
    }

    @Override
    public Set<String> getUnprocessedReferences() {
        Set<String> result = new LinkedHashSet<>(referenceMap.keySet());
        result.removeAll(processed);
        return Collections.unmodifiableSet(result);
    }

    protected String buildNameText(HumanName name) {
        StringJoiner joiner = new StringJoiner(" ");
        name.getPrefix().forEach(prefix -> joiner.add(prefix.getValue()));
        name.getGiven().forEach(given -> joiner.add(given.getValue()));
        if (name.getFamily() != null) {
            joiner.add(name.getFamily().getValue());
        }
        StringBuilder sb = new StringBuilder(joiner.toString());
        name.getSuffix().forEach(suffix -> sb.append(isRomanNumeral(suffix.getValue()) ? " " : ", ").append(suffix.getValue()));
        return sb.toString();
    }

    protected String escape(String value) {
        if (!requiresEscaping(value)) {
            return value;
        }
        return new StringBuilder()
            .append("\"")
            .append(value.replace("\"", "\"\""))
            .append("\"")
            .toString();
    }

    protected Medication findMedication(MedicationStatement medicationStatement, List<Medication> medications) {
        if (medicationStatement.getMedication() instanceof Reference) {
            Reference medication = medicationStatement.getMedication().as(Reference.class);
            if (medication.getReference() != null && medication.getReference().getValue() != null) {
                String reference = medication.getReference().getValue();
                return medications.stream()
                    .filter(med -> reference(med).equals(reference))
                    .findFirst()
                    .orElse(null);
            }
        }
        return null;
    }

    protected MedicationRequest findMedicationRequest(MedicationStatement medicationStatement, List<MedicationRequest> medicationRequests) {
        Set<String> references = medicationStatement.getBasedOn().stream()
                .map(reference -> reference.getReference())
                .filter(Objects::nonNull)
                .map(reference -> reference.getValue())
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        return medicationRequests.stream()
                .filter(medicationRequest -> references.contains(reference(medicationRequest)))
                .findFirst()
                .orElse(null);
    }

    protected abstract long generate(Address address, List<String> locationTableData);
    protected abstract void generate(Condition condition, List<String> conditionOccurrenceTableData);
    protected abstract void generate(Encounter encounter, List<String> visitOccurrenceTableData);
    protected abstract void generate(MedicationStatement medicationStatement, MedicationRequest medicationRequest, Medication medication, List<String> drugExposureTableData);
    protected abstract void generate(Organization organization, List<String> careSiteTableData, List<String> locationTableData);
    protected abstract void generate(Patient patient, List<String> personTableData, List<String> locationTableData);
    protected abstract void generate(Practitioner practitioner, List<String> providerTableData);

    protected String getAdmittedFromSourceValue(Encounter encounter) {
        if (encounter.getHospitalization() != null) {
            Hospitalization hospitalization = encounter.getHospitalization();
            if (hospitalization.getAdmitSource() != null) {
                CodeableConcept admitSource = hospitalization.getAdmitSource();
                if (!admitSource.getCoding().isEmpty()) {
                    Coding coding = admitSource.getCoding().get(0);
                    if (coding.getCode() != null) {
                        return coding.getCode().getValue();
                    }
                }
            }
        }
        return null;
    }

    protected Timestamp getBirthDateTime(Patient patient) {
        Extension birthTimeExtension = getExtension(patient, "http://hl7.org/fhir/StructureDefinition/patient-birthTime");
        if (birthTimeExtension != null && birthTimeExtension.getValue().is(DateTime.class)) {
            DateTime birthTime = birthTimeExtension.getValue().as(DateTime.class);
            if (birthTime.getValue() instanceof ZonedDateTime) {
                ZonedDateTime zonedDateTime = (ZonedDateTime) birthTime.getValue();
                return Timestamp.from(zonedDateTime.toInstant());
            }
        }
        return null;
    }

    protected Long getCareSiteId(Encounter encounter) {
        if (encounter.getServiceProvider() != null) {
            return getId(encounter.getServiceProvider());
        }
        return null;
    }

    protected Long getCareSiteId(Patient patient) {
        if (patient.getManagingOrganization() != null) {
            return getId(patient.getManagingOrganization());
        }
        return null;
    }

    protected String getCareSiteName(Organization organization) {
        if (organization.getName() != null) {
            return organization.getName().getValue();
        }
        return null;
    }

    protected int getConceptId(CodeableConcept codeableConcept) {
        for (Coding coding : codeableConcept.getCoding()) {
            int conceptId = getConceptId(coding);
            if (conceptId != OMOP_NO_MATCHING_CONCEPT) {
                return conceptId;
            }
        }
        return OMOP_NO_MATCHING_CONCEPT;
    }

    protected int getConceptId(Coding coding) {
        if (coding.getSystem() != null &&
                coding.getSystem().getValue() != null &&
                coding.getCode() != null &&
                coding.getCode().getValue() != null) {
            String system = coding.getSystem().getValue();
            String code = coding.getCode().getValue();
            String vocabularyId = CODE_SYSTEM_MAP.get(system);
            if (vocabularyId != null) {
                Concept concept = vocabService.getConcept(vocabularyId, code);
                if (concept != null) {
                    return concept.getConceptId();
                }
            }
        }
        return OMOP_NO_MATCHING_CONCEPT;
    }

    protected int getConditionConceptId(Condition condition) {
        if (condition.getCode() != null) {
            return getConceptId(condition.getCode());
        }
        return OMOP_NO_MATCHING_CONCEPT;
    }

    protected java.sql.Date getConditionEndDate(Condition condition) {
        if (condition.getAbatement() instanceof DateTime) {
            DateTime abatement = (DateTime) condition.getAbatement();
            if (abatement.getValue() instanceof ZonedDateTime) {
                ZonedDateTime zonedDateTime = (ZonedDateTime) abatement.getValue();
                return java.sql.Date.valueOf(LocalDate.from(zonedDateTime));
            }
        }
        return null;
    }

    protected Timestamp getConditionEndDateTime(Condition condition) {
        if (condition.getAbatement() instanceof DateTime) {
            DateTime abatement = (DateTime) condition.getAbatement();
            if (abatement.getValue() instanceof ZonedDateTime) {
                ZonedDateTime zonedDateTime = (ZonedDateTime) abatement.getValue();
                return Timestamp.from(zonedDateTime.toInstant());
            }
        }
        return null;
    }

    protected java.sql.Date getConditionStartDate(Condition condition) {
        if (condition.getOnset() instanceof DateTime) {
            DateTime onset = (DateTime) condition.getOnset();
            if (onset.getValue() instanceof ZonedDateTime) {
                ZonedDateTime zonedDateTime = (ZonedDateTime) onset.getValue();
                return java.sql.Date.valueOf(LocalDate.from(zonedDateTime));
            }
        }
        return null;
    }

    protected Timestamp getConditionStartDateTime(Condition condition) {
        if (condition.getOnset() instanceof DateTime) {
            DateTime onset = (DateTime) condition.getOnset();
            if (onset.getValue() instanceof ZonedDateTime) {
                ZonedDateTime zonedDateTime = (ZonedDateTime) onset.getValue();
                return Timestamp.from(zonedDateTime.toInstant());
            }
        }
        return null;
    }

    protected String getConditionStatusSourceValue(Condition condition) {
        if (condition.getClinicalStatus() != null) {
            CodeableConcept clinicalStatus = condition.getClinicalStatus();
            if (!clinicalStatus.getCoding().isEmpty()) {
                Coding coding = clinicalStatus.getCoding().get(0);
                if (coding.getCode() != null) {
                    return coding.getCode().getValue();
                }
            }
        }
        return null;
    }

    protected int getConditionTypeConceptId(Condition condition) {
        if (!condition.getCategory().isEmpty()) {
            CodeableConcept category = condition.getCategory().get(0);
            if (!category.getCoding().isEmpty()) {
                Coding coding = category.getCoding().get(0);
                if (coding.getSystem() != null &&
                        coding.getSystem().getValue() != null &&
                        coding.getCode() != null &&
                        coding.getCode().getValue() != null) {
                    String system = coding.getSystem().getValue();
                    String code = coding.getCode().getValue();
                    if ("http://terminology.hl7.org/CodeSystem/condition-category".equals(system)) {
                        switch (code) {
                        case "problem-list-item":
                            return 38000245; // EHR problem list entry
                        case "encounter-diagnosis":
                            return 32020;    // EHR encounter diagnosis
                        }
                    }
                }
            }
        }
        return OMOP_NO_MATCHING_CONCEPT;
    }

    protected String getDayOfBirth(Patient patient) {
        if (patient.getBirthDate() != null && patient.getBirthDate().getValue() != null) {
            if (patient.getBirthDate().getValue().isSupported(ChronoField.DAY_OF_MONTH)) {
                return String.valueOf(patient.getBirthDate().getValue().get(ChronoField.DAY_OF_MONTH));
            }
        }
        return null;
    }

    protected Integer getDaysSupply(MedicationRequest medicationRequest) {
        if (medicationRequest.getDispenseRequest() != null) {
            DispenseRequest dispenseRequest = medicationRequest.getDispenseRequest();
            if (dispenseRequest.getExpectedSupplyDuration() != null) {
                Duration expectedSupplyDuration = dispenseRequest.getExpectedSupplyDuration();
                if (expectedSupplyDuration.getValue() != null) {
                    Decimal value = expectedSupplyDuration.getValue();
                    if (value.getValue() != null) {
                        return value.getValue().intValue();
                    }
                }
            }
        }
        return null;
    }

    protected Timestamp getDeathDateTime(Patient patient) {
        if (patient.getDeceased() instanceof DateTime) {
            DateTime deceasedDateTime = (DateTime) patient.getDeceased();
            if (deceasedDateTime.getValue() instanceof ZonedDateTime) {
                ZonedDateTime zonedDateTime = (ZonedDateTime) deceasedDateTime.getValue();
                return Timestamp.from(zonedDateTime.toInstant());
            }
        }
        return null;
    }

    protected int getDischargeToConceptId(Encounter encounter) {
        if (encounter.getHospitalization() != null) {
            Hospitalization hospitalization = encounter.getHospitalization();
            if (hospitalization.getDischargeDisposition() != null) {
                return getConceptId(hospitalization.getDischargeDisposition());
            }
        }
        return OMOP_NO_MATCHING_CONCEPT;
    }

    protected String getDischargeToSourceValue(Encounter encounter) {
        if (encounter.getHospitalization() != null) {
            Hospitalization hospitalization = encounter.getHospitalization();
            if (hospitalization.getDischargeDisposition() != null) {
                CodeableConcept dischargeDisposition = hospitalization.getDischargeDisposition();
                if (!dischargeDisposition.getCoding().isEmpty()) {
                    Coding coding = dischargeDisposition.getCoding().get(0);
                    if (coding.getCode() != null) {
                        return coding.getCode().getValue();
                    }
                }
            }
        }
        return null;
    }

    protected String getDoseUnitSourceValue(MedicationRequest medicationRequest) {
        // TODO Auto-generated method stub
        return null;
    }

    protected int getDrugConceptId(MedicationStatement medicationStatement) {
        if (medicationStatement.getMedication() instanceof CodeableConcept) {
            return getConceptId(medicationStatement.getMedication().as(CodeableConcept.class));
        }
        return 0;
    }

    protected java.sql.Date getDrugExposureEndDate(MedicationStatement medicationStatement) {
        if (medicationStatement.getEffective() instanceof Period) {
            Period effective = medicationStatement.getEffective().as(Period.class);
            if (effective.getStart() != null) {
                DateTime end = effective.getEnd();
                if (end.getValue() instanceof ZonedDateTime) {
                    ZonedDateTime zonedDateTime = (ZonedDateTime) end.getValue();
                    return java.sql.Date.valueOf(LocalDate.from(zonedDateTime));
                }
            }
        }
        return null;
    }

    protected Timestamp getDrugExposureEndDateTime(MedicationStatement medicationStatement) {
        if (medicationStatement.getEffective() instanceof Period) {
            Period effective = medicationStatement.getEffective().as(Period.class);
            if (effective.getStart() != null) {
                DateTime end = effective.getEnd();
                if (end.getValue() instanceof ZonedDateTime) {
                    ZonedDateTime zonedDateTime = (ZonedDateTime) end.getValue();
                    return Timestamp.from(zonedDateTime.toInstant());
                }
            }
        }
        return null;
    }

    protected java.sql.Date getDrugExposureStartDate(MedicationStatement medicationStatement) {
        if (medicationStatement.getEffective() instanceof Period) {
            Period effective = medicationStatement.getEffective().as(Period.class);
            if (effective.getStart() != null) {
                DateTime start = effective.getStart();
                if (start.getValue() instanceof ZonedDateTime) {
                    ZonedDateTime zonedDateTime = (ZonedDateTime) start.getValue();
                    return java.sql.Date.valueOf(LocalDate.from(zonedDateTime));
                }
            }
        }
        return null;
    }

    protected Timestamp getDrugExposureStartDateTime(MedicationStatement medicationStatement) {
        if (medicationStatement.getEffective() instanceof Period) {
            Period effective = medicationStatement.getEffective().as(Period.class);
            if (effective.getStart() != null) {
                DateTime start = effective.getStart();
                if (start.getValue() instanceof ZonedDateTime) {
                    ZonedDateTime zonedDateTime = (ZonedDateTime) start.getValue();
                    return Timestamp.from(zonedDateTime.toInstant());
                }
            }
        }
        return null;
    }

    protected int getEthnicityConceptId(Patient patient) {
        Extension ethnicityExtension = getExtension(patient, "http://hl7.org/fhir/us/core/StructureDefinition/us-core-race");
        if (ethnicityExtension != null) {
            Extension ombCategory = getExtension(ethnicityExtension, "ombCategory");
            if (ombCategory != null && ombCategory.getValue().is(Coding.class)) {
                Coding valueCoding = ombCategory.getValue().as(Coding.class);
                if (valueCoding.getCode() != null && valueCoding.getCode().getValue() != null) {
                    String code = valueCoding.getCode().getValue();
                    switch (code) {
                    case "2135-2":
                        return OMOP_ETHNICITY_HISPANIC_OR_LATINO;
                    case "2186-5":
                        return OMOP_ETHNICITY_NOT_HISPANIC_OR_LATINO;
                    }
                }
            }
        }
        return OMOP_NO_MATCHING_CONCEPT;
    }

    protected String getEthnicitySourceValue(Patient patient) {
        Extension ethnicityExtension = getExtension(patient, "http://hl7.org/fhir/us/core/StructureDefinition/us-core-race");
        if (ethnicityExtension != null) {
            Extension ombCategory = getExtension(ethnicityExtension, "ombCategory");
            if (ombCategory != null && ombCategory.getValue().is(Coding.class)) {
                Coding valueCoding = ombCategory.getValue().as(Coding.class);
                if (valueCoding.getCode() != null && valueCoding.getCode().getValue() != null) {
                    return valueCoding.getCode().getValue();
                }
            }
        }
        return null;
    }

    protected Extension getExtension(DomainResource domainResource, String url) {
        for (Extension extension : domainResource.getExtension()) {
            if (url.equals(extension.getUrl())) {
                return extension;
            }
        }
        return null;
    }

    protected Extension getExtension(Element element, String url) {
        for (Extension extension : element.getExtension()) {
            if (url.equals(extension.getUrl())) {
                return extension;
            }
        }
        return null;
    }

    protected int getGenderConceptId(AdministrativeGender gender) {
        if (AdministrativeGender.MALE.equals(gender)) {
            return OMOP_GENDER_MALE;
        }
        if (AdministrativeGender.FEMALE.equals(gender)) {
            return OMOP_GENDER_FEMALE;
        }
        return OMOP_NO_MATCHING_CONCEPT;
    }

    protected int getGenderConceptId(Patient patient) {
        return getGenderConceptId(patient.getGender());
    }

    protected int getGenderConceptId(Practitioner practitioner) {
        return getGenderConceptId(practitioner.getGender());
    }

    protected String getGenderSourceValue(Patient patient) {
        if (patient.getGender() != null) {
            return patient.getGender().getValue();
        }
        return null;
    }

    protected String getGenderSourceValue(Practitioner practitioner) {
        if (practitioner.getGender() != null) {
            return practitioner.getGender().getValue();
        }
        return null;
    }

    protected Long getId(Reference reference) {
        if (reference.getReference() == null || reference.getReference().getValue() == null) {
            return null;
        }
        Matcher matcher = FHIRUtil.REFERENCE_PATTERN.matcher(reference.getReference().getValue());
        if (matcher.matches()) {
            String resourceType = matcher.group(4);
            String id = matcher.group(5);
            if (resourceType != null && id != null) {
                return getId(resourceType, id);
            }
        }
        return null;
    }

    protected long getId(Resource resource) {
        return referenceMap.computeIfAbsent(reference(resource), k -> getNextValue());
    }

    protected long getId(String resourceType, String id) {
        return referenceMap.computeIfAbsent(reference(resourceType, id), k -> getNextValue());
    }

    protected Long getLocationId(Organization organization, List<String> locationTableData) {
        if (!organization.getAddress().isEmpty()) {
            return generate(organization.getAddress().get(0), locationTableData);
        }
        return null;
    }

    protected Long getLocationId(Patient patient, List<String> locationTableData) {
        if (!patient.getAddress().isEmpty()) {
            return generate(patient.getAddress().get(0), locationTableData);
        }
        return null;
    }

    protected String getLotNumber(Medication medication) {
        if (medication != null) {
            if (medication.getBatch() != null) {
                Batch batch = medication.getBatch();
                if (batch.getLotNumber() != null) {
                    return batch.getLotNumber().getValue();
                }
            }
        }
        return null;
    }

    protected String getMonthOfBirth(Patient patient) {
        if (patient.getBirthDate() != null && patient.getBirthDate().getValue() != null) {
            if (patient.getBirthDate().getValue().isSupported(ChronoField.MONTH_OF_YEAR)) {
                return String.valueOf(patient.getBirthDate().getValue().get(ChronoField.MONTH_OF_YEAR));
            }
        }
        return null;
    }

    protected String getName(HumanName name) {
        if (name != null) {
            if (name.getText() != null) {
                return name.getText().getValue();
            }
            return buildNameText(name);
        }
        return null;
    }

    protected long getNextValue() {
        return ++sequence;
    }

    protected String getNPI(Practitioner practitioner) {
        for (Identifier identifier : practitioner.getIdentifier()) {
            if (identifier.getSystem() != null &&
                    identifier.getSystem().getValue() != null) {
                String system = identifier.getSystem().getValue();
                if ("http://hl7.org/fhir/sid/us-npi".equals(system)) {
                    if (identifier.getValue() != null &&
                            identifier.getValue().getValue() != null) {
                        return identifier.getValue().getValue();
                    }
                }
            }
        }
        return null;
    }

    protected long getPersonId(Condition condition) {
        if (condition.getSubject() != null) {
            return getId(condition.getSubject());
        }
        return 0;
    }

    protected long getPersonId(Encounter encounter) {
        if (encounter.getSubject() != null) {
            return getId(encounter.getSubject());
        }
        return 0;
    }

    protected long getPersonId(MedicationStatement medicationStatement) {
        if (medicationStatement.getSubject() != null) {
            return getId(medicationStatement.getSubject());
        }
        return 0;
    }

    protected String getPlaceOfServiceSourceValue(Organization organization) {
        if (!organization.getType().isEmpty()) {
            CodeableConcept type = organization.getType().get(0);
            if (type.getText() != null) {
                return type.getText().getValue();
            }
            if (!type.getCoding().isEmpty()) {
                Coding coding = type.getCoding().get(0);
                if (coding.getDisplay() != null) {
                    return coding.getDisplay().getValue();
                }
            }
        }
        return null;
    }

    protected Long getPrecedingVisitOccurrenceId(Encounter encounter) {
        if (encounter.getPartOf() != null) {
            return getId(encounter.getPartOf());
        }
        return null;
    }

    protected Long getProviderId(Condition condition) {
        if (condition.getAsserter() != null) {
            return getId(condition.getAsserter());
        }
        return null;
    }

    protected Long getProviderId(Encounter encounter) {
        if (!encounter.getParticipant().isEmpty()) {
            Encounter.Participant participant = encounter.getParticipant().get(0);
            return getId(participant.getIndividual());
        }
        return null;
    }

    protected Long getProviderId(MedicationRequest medicationRequest) {
        if (medicationRequest.getRequester() != null) {
            return getId(medicationRequest.getRequester());
        }
        return null;
    }

    protected Long getProviderId(Patient patient) {
        if (!patient.getGeneralPractitioner().isEmpty()) {
            return getId(patient.getGeneralPractitioner().get(0));
        }
        return null;
    }

    protected String getProviderName(Practitioner practitioner) {
        if (!practitioner.getName().isEmpty()) {
            return getName(practitioner.getName().get(0));
        }
        return null;
    }

    protected BigDecimal getQuantity(MedicationRequest medicationRequest) {
        if (medicationRequest.getDispenseRequest() != null) {
            DispenseRequest dispenseRequest = medicationRequest.getDispenseRequest();
            if (dispenseRequest.getQuantity() != null) {
                SimpleQuantity quantity = dispenseRequest.getQuantity();
                if (quantity.getValue() != null) {
                    Decimal value = quantity.getValue();
                    if (value.getValue() != null) {
                        return value.getValue();
                    }
                }
            }
        }
        return null;
    }

    protected int getRaceConceptId(Patient patient) {
        Extension raceExtension = getExtension(patient, "http://hl7.org/fhir/us/core/StructureDefinition/us-core-race");
        if (raceExtension != null) {
            Extension ombCategory = getExtension(raceExtension, "ombCategory");
            if (ombCategory != null && ombCategory.getValue().is(Coding.class)) {
                Coding valueCoding = ombCategory.getValue().as(Coding.class);
                if (valueCoding.getCode() != null && valueCoding.getCode().getValue() != null) {
                    String code = valueCoding.getCode().getValue();
                    switch (code) {
                    case "1002-5":
                        return OMOP_RACE_AMERICAN_INDIAN_OR_ALASKA_NATIVE;
                    case "2028-9":
                        return OMOP_RACE_ASIAN;
                    case "2054-5":
                        return OMOP_RACE_BLACK_OR_AFRICAN_AMERICAN;
                    case "2076-8":
                        return OMOP_RACE_NATIVE_HAWAIIAN_OR_OTHER_PACIFIC_ISLANDER;
                    case "2106-3":
                        return OMOP_RACE_WHITE;
                    }
                }
            }
        }
        return OMOP_NO_MATCHING_CONCEPT;
    }

    protected String getRaceSourceValue(Patient patient) {
        Extension raceExtension = getExtension(patient, "http://hl7.org/fhir/us/core/StructureDefinition/us-core-race");
        if (raceExtension != null) {
            Extension ombCategory = getExtension(raceExtension, "ombCategory");
            if (ombCategory != null && ombCategory.getValue().is(Coding.class)) {
                Coding valueCoding = ombCategory.getValue().as(Coding.class);
                if (valueCoding.getCode() != null && valueCoding.getCode().getValue() != null) {
                    return valueCoding.getCode().getValue();
                }
            }
        }
        return null;
    }

    protected Integer getRefills(MedicationRequest medicationRequest) {
        if (medicationRequest.getDispenseRequest() != null) {
            DispenseRequest dispenseRequest = medicationRequest.getDispenseRequest();
            if (dispenseRequest.getNumberOfRepeatsAllowed() != null) {
                UnsignedInt numberOfRepeatsAllowed = dispenseRequest.getNumberOfRepeatsAllowed();
                return numberOfRepeatsAllowed.getValue();
            }
        }
        return null;
    }

    protected <T extends Resource> List<T> getResources(Map<Class<? extends Resource>, List<Resource>> resourceMap, Class<T> resourceType) {
        return resourceMap.get(resourceType).stream()
                .map(resource -> resource.as(resourceType))
                .collect(Collectors.toList());
    }

    protected int getRouteConceptId(MedicationRequest medicationRequest) {
        return medicationRequest.getDosageInstruction().stream()
            .map(dosageInstruction -> dosageInstruction.getRoute())
            .filter(Objects::nonNull)
            .map(route -> getConceptId(route))
            .filter(conceptId -> conceptId != OMOP_NO_MATCHING_CONCEPT)
            .findFirst()
            .orElse(OMOP_NO_MATCHING_CONCEPT);
    }

    protected String getSig(MedicationRequest medicationRequest) {
        if (!medicationRequest.getDosageInstruction().isEmpty()) {
            Dosage dosage = medicationRequest.getDosageInstruction().get(0);
            if (dosage.getText() != null) {
                return dosage.getText().getValue();
            }
        }
        return null;
    }

    protected String getStopReason(MedicationStatement medicationStatement) {
        if (!medicationStatement.getStatusReason().isEmpty()) {
            CodeableConcept statusReason = medicationStatement.getStatusReason().get(0);
            if (statusReason.getText() != null) {
                return statusReason.getText().getValue();
            }
            if (!statusReason.getCoding().isEmpty()) {
                Coding coding = statusReason.getCoding().get(0);
                if (coding.getDisplay() != null) {
                    return coding.getDisplay().getValue();
                }
            }
        }
        return null;
    }

    protected List<String> getTableData(Map<Table, List<String>> tableDataMap, Table table) {
        return tableDataMap.computeIfAbsent(table, t -> new ArrayList<>());
    }

    protected java.sql.Date getVerbatimDate(MedicationRequest medicationRequest) {
        if (medicationRequest.getDispenseRequest() != null) {
            DispenseRequest dispenseRequest = medicationRequest.getDispenseRequest();
            if (dispenseRequest.getValidityPeriod() != null) {
                Period validityPeriod = dispenseRequest.getValidityPeriod();
                if (validityPeriod.getEnd() != null) {
                    DateTime end = validityPeriod.getEnd();
                    if (end.getValue() instanceof ZonedDateTime) {
                        ZonedDateTime zonedDateTime = (ZonedDateTime) end.getValue();
                        return java.sql.Date.valueOf(LocalDate.from(zonedDateTime.toInstant()));
                    }
                }
            }
        }
        return null;
    }

    protected int getVisitConceptId(Encounter encounter) {
        Coding clazz = encounter.getClazz();
        if (clazz.getCode() != null && clazz.getCode().getValue() != null) {
            String code = clazz.getCode().getValue();
            switch (code) {
            case "AMB":        // ambulatory
                return 9202;   // "Outpatient Visit"
            case "EMER":       // emergency
                return 9203;   // "Emergency Room Visit"
            case "HH":         // home health
                return 581476; // "Home Visit"
            case "IMP":        // inpatient encounter
            case "ACUTE":      // inpatient acute
            case "NONAC":      // inpatient non-acute
                return 9201;   // "Inpatient Visit"
            case "FLD":        // field
            case "OBSENC":     // observation encounter
            case "PRENC":      // pre-admission
            case "SS":         // short stay
            case "VR":         // virtual
            default:
                break;
            }
        }
        return OMOP_NO_MATCHING_CONCEPT;
    }

    protected java.sql.Date getVisitEndDate(Encounter encounter) {
        if (encounter.getPeriod() != null) {
            Period period = encounter.getPeriod();
            if (period.getEnd() != null) {
                DateTime end = period.getEnd();
                if (end.getValue() instanceof ZonedDateTime) {
                    ZonedDateTime zonedDateTime = (ZonedDateTime) end.getValue();
                    return java.sql.Date.valueOf(LocalDate.from(zonedDateTime));
                }
            }
        }
        return null;
    }

    protected Timestamp getVisitEndDateTime(Encounter encounter) {
        if (encounter.getPeriod() != null) {
            Period period = encounter.getPeriod();
            if (period.getStart() != null) {
                DateTime end = period.getEnd();
                if (end.getValue() instanceof ZonedDateTime) {
                    ZonedDateTime zonedDateTime = (ZonedDateTime) end.getValue();
                    return Timestamp.from(zonedDateTime.toInstant());
                }
            }
        }
        return null;
    }

    protected Long getVisitOccurrenceId(Condition condition) {
        if (condition.getEncounter() != null) {
            return getId(condition.getEncounter());
        }
        return null;
    }

    protected Long getVisitOccurrenceId(MedicationStatement medicationStatement) {
        if (medicationStatement.getContext() != null) {
            return getId(medicationStatement.getContext());
        }
        return null;
    }

    protected java.sql.Date getVisitStartDate(Encounter encounter) {
        if (encounter.getPeriod() != null) {
            Period period = encounter.getPeriod();
            if (period.getStart() != null) {
                DateTime start = period.getStart();
                if (start.getValue() instanceof ZonedDateTime) {
                    ZonedDateTime zonedDateTime = (ZonedDateTime) start.getValue();
                    return java.sql.Date.valueOf(LocalDate.from(zonedDateTime));
                }
            }
        }
        return null;
    }

    protected Timestamp getVisitStartDateTime(Encounter encounter) {
        if (encounter.getPeriod() != null) {
            Period period = encounter.getPeriod();
            if (period.getStart() != null) {
                DateTime start = period.getStart();
                if (start.getValue() instanceof ZonedDateTime) {
                    ZonedDateTime zonedDateTime = (ZonedDateTime) start.getValue();
                    return Timestamp.from(zonedDateTime.toInstant());
                }
            }
        }
        return null;
    }

    protected int getVisitTypeConceptId(Encounter encounter) {
        for (CodeableConcept type : encounter.getType()) {
            int conceptId = getConceptId(type);
            if (conceptId != OMOP_NO_MATCHING_CONCEPT) {
                return conceptId;
            }
        }
        return OMOP_NO_MATCHING_CONCEPT;
    }

    protected String getYearOfBirth(Date birthDate) {
        if (birthDate != null && birthDate.getValue() != null) {
            if (birthDate.getValue().isSupported(ChronoField.YEAR)) {
                return String.valueOf(birthDate.getValue().get(ChronoField.YEAR));
            }
        }
        return null;
    }

    protected String getYearOfBirth(Patient patient) {
        return getYearOfBirth(patient.getBirthDate());
    }

    protected String getYearOfBirth(Practitioner practitioner) {
        return getYearOfBirth(practitioner.getBirthDate());
    }

    protected boolean isRomanNumeral(String s) {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (!ROMAN_NUMERAL_CHARACTERS.contains(c)) {
                return false;
            }
        }
        return true;
    }

    protected String record(Object... values) {
        StringJoiner joiner = new StringJoiner(",");
        for (Object value : values) {
            joiner.add((value != null) ? escape(value.toString()) : "");
        }
        return joiner.toString();
    }

    protected String reference(Resource resource) {
        return reference(resource.getClass().getSimpleName(), resource.getId());
    }

    protected String reference(String resourceType, String id) {
        return resourceType + "/" + id;
    }

    protected boolean requiresEscaping(String value) {
        return value.contains(",") ||
                value.contains("\"") ||
                value.contains("\r") ||
                value.contains("\n");
    }

    protected void validateResourceMap(Mapping mapping, Map<Class<? extends Resource>, List<Resource>> resourceMap) {
        for (Class<? extends Resource> resourceType : mapping.resourceTypes()) {
            if (!resourceMap.containsKey(resourceType)) {
                throw new IllegalArgumentException("The resource map does not contain resources for resource type: " + resourceType.getSimpleName());
            }
        }
    }

    protected static Map<String, String> buildStateCodeMap() {
        Map<String, String> stateCodeMap = new LinkedHashMap<>();
        ValueSet valueSet = ValueSetSupport.getValueSet("http://hl7.org/fhir/us/core/ValueSet/us-core-usps-state");
        valueSet.getCompose().getInclude().stream()
            .map(include -> include.getConcept())
            .flatMap(List::stream)
            .forEach(concept -> stateCodeMap.put(concept.getDisplay().getValue(), concept.getCode().getValue()));
        return Collections.unmodifiableMap(stateCodeMap);
    }
}
