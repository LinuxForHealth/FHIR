/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.omop.generator.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.ibm.fhir.model.resource.Condition;
import com.ibm.fhir.model.resource.Encounter;
import com.ibm.fhir.model.resource.Medication;
import com.ibm.fhir.model.resource.MedicationRequest;
import com.ibm.fhir.model.resource.MedicationStatement;
import com.ibm.fhir.model.resource.Organization;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Practitioner;
import com.ibm.fhir.model.type.Address;
import com.ibm.fhir.omop.vocab.VocabService;

public class V5GeneratorImpl extends GeneratorImpl {
    public V5GeneratorImpl(VocabService vocabService) {
        super(vocabService);
    }

    /*
    CREATE TABLE location
    (
      location_id           BIGINT          NOT NULL,
      address_1             VARCHAR(50)     NULL,
      address_2             VARCHAR(50)     NULL,
      city                  VARCHAR(50)     NULL,
      state                 VARCHAR(2)      NULL,
      zip                   VARCHAR(9)      NULL,
      county                VARCHAR(20)     NULL,
      country               VARCHAR(100)    NULL,
      location_source_value VARCHAR(50)     NULL,
      latitude              NUMERIC         NULL,
      longitude             NUMERIC         NULL
    )
    ;
    */
    @Override
    protected long generate(Address address, List<String> locationTableData) {
        long locationId = getNextValue();
        String address1 = (address.getLine().size() > 0) ? address.getLine().get(0).getValue() : null;
        String address2 = (address.getLine().size() > 1) ? address.getLine().get(1).getValue() : null;
        String city = (address.getCity() != null) ? address.getCity().getValue() : null;
        String state = (address.getState() != null) ? address.getState().getValue() : null;
        if (STATE_CODE_MAP.containsKey(state)) {
            state = STATE_CODE_MAP.get(state);
        }
        String zip = (address.getPostalCode() != null) ? address.getPostalCode().getValue() : null;
        if (zip != null && zip.length() > 5) {
            zip = zip.substring(0, 5);
        }
        String county = (address.getDistrict() != null) ? address.getDistrict().getValue() : null;
        String country = (address.getCountry() != null) ? address.getCountry().getValue() : null;
        String locationSourceValue = null;
        BigDecimal latitude = null;
        BigDecimal longitude = null;

        locationTableData.add(record(
            locationId,
            address1,
            address2,
            city,
            state,
            zip,
            county,
            country,
            locationSourceValue,
            latitude,
            longitude));

        return locationId;
    }

    /*
    CREATE TABLE condition_occurrence
    (
      condition_occurrence_id       BIGINT          NOT NULL,
      person_id                     BIGINT          NOT NULL,
      condition_concept_id          INTEGER         NOT NULL,
      condition_start_date          DATE            NULL,
      condition_start_datetime      TIMESTAMP       NOT NULL,
      condition_end_date            DATE            NULL,
      condition_end_datetime        TIMESTAMP       NULL,
      condition_type_concept_id     INTEGER         NOT NULL,
      condition_status_concept_id   INTEGER         NOT NULL,
      stop_reason                   VARCHAR(20)     NULL,
      provider_id                   BIGINT          NULL,
      visit_occurrence_id           BIGINT          NULL,
      visit_detail_id               BIGINT          NULL,
      condition_source_value        VARCHAR(50)     NULL,
      condition_source_concept_id   INTEGER         NOT NULL,
      condition_status_source_value VARCHAR(50)     NULL
    )
    ;
    */
    @Override
    protected void generate(Condition condition, List<String> conditionOccurrenceTableData) {
        long conditionOccurrenceId = getId(condition);
        long personId = getPersonId(condition);
        int conditionConceptId = getConditionConceptId(condition);
        java.sql.Date conditionStartDate = getConditionStartDate(condition);
        Timestamp conditionStartDateTime = getConditionStartDateTime(condition);
        java.sql.Date conditionEndDate = getConditionEndDate(condition);
        Timestamp conditionEndDateTime = getConditionEndDateTime(condition);
        int conditionTypeConceptId = getConditionTypeConceptId(condition);
        int conditionStatusConceptId = 0;
        String stopReason = null;
        Long providerId = getProviderId(condition);
        Long visitOccurrenceId = getVisitOccurrenceId(condition);
        Long visitDetailId = null;
        String conditionSourceValue = condition.getId();
        int conditionSourceConceptId = 0;
        String conditionStatusSourceValue = getConditionStatusSourceValue(condition);

        conditionOccurrenceTableData.add(record(
            conditionOccurrenceId,
            personId,
            conditionConceptId,
            conditionStartDate,
            conditionStartDateTime,
            conditionEndDate,
            conditionEndDateTime,
            conditionTypeConceptId,
            conditionStatusConceptId,
            stopReason,
            providerId,
            visitOccurrenceId,
            visitDetailId,
            conditionSourceValue,
            conditionSourceConceptId,
            conditionStatusSourceValue));

        processed.add(reference(condition));
    }

    /*
    CREATE TABLE visit_occurrence
    (
      visit_occurrence_id           BIGINT          NOT NULL,
      person_id                     BIGINT          NOT NULL,
      visit_concept_id              INTEGER         NOT NULL,
      visit_start_date              DATE            NULL,
      visit_start_datetime          TIMESTAMP       NOT NULL,
      visit_end_date                DATE            NULL,
      visit_end_datetime            TIMESTAMP       NOT NULL,
      visit_type_concept_id         INTEGER         NOT NULL,
      provider_id                   BIGINT          NULL,
      care_site_id                  BIGINT          NULL,
      visit_source_value            VARCHAR(50)     NULL,
      visit_source_concept_id       INTEGER         NOT NULL,
      admitted_from_concept_id      INTEGER         NOT NULL,
      admitted_from_source_value    VARCHAR(50)     NULL,
      discharge_to_source_value     VARCHAR(50)     NULL,
      discharge_to_concept_id       INTEGER         NOT NULL,
      preceding_visit_occurrence_id BIGINT          NULL
    )
    ;
    */
    @Override
    protected void generate(Encounter encounter, List<String> visitOccurrenceTableData) {
        long visitOccurrenceId = getId(encounter);
        long personId = getPersonId(encounter);
        int visitConceptId = getVisitConceptId(encounter);
        java.sql.Date visitStartDate = getVisitStartDate(encounter);
        Timestamp visitStartDateTime = getVisitStartDateTime(encounter);
        java.sql.Date visitEndDate = getVisitEndDate(encounter);
        Timestamp visitEndDateTime = getVisitEndDateTime(encounter);
        long visitTypeConceptId = getVisitTypeConceptId(encounter);
        Long providerId = getProviderId(encounter);
        Long careSiteId = getCareSiteId(encounter);
        String visitSourceValue = encounter.getId();
        int visitSourceConceptId = 0;
        int admittedFromConceptId = 0;
        String admittedFromSourceValue = getAdmittedFromSourceValue(encounter);
        String dischargeToSourceValue = getDischargeToSourceValue(encounter);
        int dischargeToConceptId = getDischargeToConceptId(encounter);
        Long precedingVisitOccurrenceId = getPrecedingVisitOccurrenceId(encounter);

        visitOccurrenceTableData.add(record(
            visitOccurrenceId,
            personId,
            visitConceptId,
            visitStartDate,
            visitStartDateTime,
            visitEndDate,
            visitEndDateTime,
            visitTypeConceptId,
            providerId,
            careSiteId,
            visitSourceValue,
            visitSourceConceptId,
            admittedFromConceptId,
            admittedFromSourceValue,
            dischargeToSourceValue,
            dischargeToConceptId,
            precedingVisitOccurrenceId));

        processed.add(reference(encounter));
    }

    /*
    CREATE TABLE drug_exposure
    (
      drug_exposure_id              BIGINT              NOT NULL,
      person_id                     BIGINT              NOT NULL,
      drug_concept_id               INTEGER             NOT NULL,
      drug_exposure_start_date      DATE                NULL,
      drug_exposure_start_datetime  TIMESTAMP           NOT NULL,
      drug_exposure_end_date        DATE                NULL,
      drug_exposure_end_datetime    TIMESTAMP           NOT NULL,
      verbatim_end_date             DATE                NULL,
      drug_type_concept_id          INTEGER             NOT NULL,
      stop_reason                   VARCHAR(20)         NULL,
      refills                       INTEGER             NULL,
      quantity                      NUMERIC             NULL,
      days_supply                   INTEGER             NULL,
      sig                           TEXT                NULL,
      route_concept_id              INTEGER             NOT NULL,
      lot_number                    VARCHAR(50)         NULL,
      provider_id                   BIGINT              NULL,
      visit_occurrence_id           BIGINT              NULL,
      visit_detail_id               BIGINT              NULL,
      drug_source_value             VARCHAR(50)         NULL,
      drug_source_concept_id        INTEGER             NOT NULL,
      route_source_value            VARCHAR(50)         NULL,
      dose_unit_source_value        VARCHAR(50)         NULL
    )
    ;
    */
    @Override
    protected void generate(MedicationStatement medicationStatement, MedicationRequest medicationRequest, Medication medication, List<String> drugExposureTableData) {
        if (medicationRequest == null) {
            return;
        }
        long drugExposureId = getId(medicationStatement);
        long personId = getPersonId(medicationStatement);
        long drugConceptId = getDrugConceptId(medicationStatement);
        java.sql.Date drugExposureStartDate = getDrugExposureStartDate(medicationStatement);
        Timestamp drugExposureStartDateTime = getDrugExposureStartDateTime(medicationStatement);
        java.sql.Date drugExposureEndDate = getDrugExposureEndDate(medicationStatement);
        Timestamp drugExposureEndDateTime = getDrugExposureEndDateTime(medicationStatement);
        java.sql.Date verbatimDate = getVerbatimDate(medicationRequest);
        int drugTypeConceptId = 0;
        String stopReason = getStopReason(medicationStatement);
        Integer refills = getRefills(medicationRequest);
        BigDecimal quantity = getQuantity(medicationRequest);
        Integer daysSupply = getDaysSupply(medicationRequest);
        String sig = getSig(medicationRequest);
        int routeConceptId = getRouteConceptId(medicationRequest);
        String lotNumber = getLotNumber(medication);
        Long providerId = getProviderId(medicationRequest);
        Long visitOccurrenceId = getVisitOccurrenceId(medicationStatement);
        Long visitDetailId = null;
        String drugSourceValue = null;
        int drugSourceConceptId = 0;
        String routeSourceValue = null;
        String doseUnitSourceValue = getDoseUnitSourceValue(medicationRequest);

        drugExposureTableData.add(record(
            drugExposureId,
            personId,
            drugConceptId,
            drugExposureStartDate,
            drugExposureStartDateTime,
            drugExposureEndDate,
            drugExposureEndDateTime,
            verbatimDate,
            drugTypeConceptId,
            stopReason,
            refills,
            quantity,
            daysSupply,
            sig,
            routeConceptId,
            lotNumber,
            providerId,
            visitOccurrenceId,
            visitDetailId,
            drugSourceValue,
            drugSourceConceptId,
            routeSourceValue,
            doseUnitSourceValue));

        processed.add(reference(medicationRequest));
    }

    /*
    CREATE TABLE care_site
    (
      care_site_id                  BIGINT          NOT NULL,
      care_site_name                VARCHAR(255)    NULL,
      place_of_service_concept_id   INTEGER         NOT NULL,
      location_id                   BIGINT          NULL,
      care_site_source_value        VARCHAR(50)     NULL,
      place_of_service_source_value VARCHAR(50)     NULL
    )
    ;
    */
    @Override
    protected void generate(Organization organization, List<String> careSiteTableData, List<String> locationTableData) {
        long careSiteId = getId(organization);
        String careSiteName = getCareSiteName(organization);
        int placeOfServiceConceptId = 0;
        Long locationId = getLocationId(organization, locationTableData);
        String careSiteSourceValue = organization.getId();
        String placeOfServiceSourceValue = getPlaceOfServiceSourceValue(organization);

        careSiteTableData.add(record(
            careSiteId,
            careSiteName,
            placeOfServiceConceptId,
            locationId,
            careSiteSourceValue,
            placeOfServiceSourceValue));

        processed.add(reference(organization));
    }

    /*
    CREATE TABLE person
    (
      person_id                     BIGINT      NOT NULL,
      gender_concept_id             INTEGER     NOT NULL,
      year_of_birth                 INTEGER     NOT NULL,
      month_of_birth                INTEGER     NULL,
      day_of_birth                  INTEGER     NULL,
      birth_datetime                TIMESTAMP   NULL,
      death_datetime                TIMESTAMP   NULL,
      race_concept_id               INTEGER     NOT NULL,
      ethnicity_concept_id          INTEGER     NOT NULL,
      location_id                   BIGINT      NULL,
      provider_id                   BIGINT      NULL,
      care_site_id                  BIGINT      NULL,
      person_source_value           VARCHAR(50) NULL,
      gender_source_value           VARCHAR(50) NULL,
      gender_source_concept_id      INTEGER     NOT NULL,
      race_source_value             VARCHAR(50) NULL,
      race_source_concept_id        INTEGER     NOT NULL,
      ethnicity_source_value        VARCHAR(50) NULL,
      ethnicity_source_concept_id   INTEGER     NOT NULL
    )
    ;
    */
    @Override
    protected void generate(Patient patient, List<String> personTableData, List<String> locationTableData) {
        long personId = getId(patient);
        int genderConceptId = getGenderConceptId(patient);
        String yearOfBirth = getYearOfBirth(patient);
        String monthOfBirth = getMonthOfBirth(patient);
        String dayOfBirth = getDayOfBirth(patient);
        Timestamp birthDateTime = getBirthDateTime(patient);
        Timestamp deathDateTime = getDeathDateTime(patient);
        int raceConceptId = getRaceConceptId(patient);
        int ethnicityConceptId = getEthnicityConceptId(patient);
        Long locationId = getLocationId(patient, locationTableData);
        Long providerId = getProviderId(patient);
        Long careSiteId = getCareSiteId(patient);
        String personSourceValue = patient.getId();
        String genderSourceValue = getGenderSourceValue(patient);
        int genderSourceConceptId = 0;
        String raceSourceValue = getRaceSourceValue(patient);
        int raceSourceConceptId = 0;
        String ethnicitySourceValue = getEthnicitySourceValue(patient);
        int ethnicitySourceConceptId = 0;

        personTableData.add(record(
            personId,
            genderConceptId,
            yearOfBirth,
            monthOfBirth,
            dayOfBirth,
            birthDateTime,
            deathDateTime,
            raceConceptId,
            ethnicityConceptId,
            locationId,
            providerId,
            careSiteId,
            personSourceValue,
            genderSourceValue,
            genderSourceConceptId,
            raceSourceValue,
            raceSourceConceptId,
            ethnicitySourceValue,
            ethnicitySourceConceptId));

        processed.add(reference(patient));
    }

    /*
    CREATE TABLE provider
    (
      provider_id                   BIGINT          NOT NULL,
      provider_name                 VARCHAR(255)    NULL,
      NPI                           VARCHAR(20)     NULL,
      DEA                           VARCHAR(20)     NULL,
      specialty_concept_id          INTEGER         NOT NULL,
      care_site_id                  BIGINT          NULL,
      year_of_birth                 INTEGER         NULL,
      gender_concept_id             INTEGER         NOT NULL,
      provider_source_value         VARCHAR(50)     NULL,
      specialty_source_value        VARCHAR(50)     NULL,
      specialty_source_concept_id   INTEGER         NULL,
      gender_source_value           VARCHAR(50)     NULL,
      gender_source_concept_id      INTEGER         NOT NULL
    )
    ;
    */
    @Override
    protected void generate(Practitioner practitioner, List<String> providerTableData) {
        long providerId = getId(practitioner);
        String providerName = getProviderName(practitioner);
        String npi = getNPI(practitioner);
        String dea = null;
        int specialtyConceptId = 0;
        Long careSiteId = null;
        String yearOfBirth = getYearOfBirth(practitioner);
        int genderConceptId = getGenderConceptId(practitioner);
        String providerSourceValue = practitioner.getId();
        String specialtySourceValue = null;
        Integer specialtySourceConceptId = null;
        String genderSourceValue = getGenderSourceValue(practitioner);
        int genderSourceConceptId = 0;

        providerTableData.add(record(
            providerId,
            providerName,
            npi,
            dea,
            specialtyConceptId,
            careSiteId,
            yearOfBirth,
            genderConceptId,
            providerSourceValue,
            specialtySourceValue,
            specialtySourceConceptId,
            genderSourceValue,
            genderSourceConceptId));

        processed.add(reference(practitioner));
    }
}
