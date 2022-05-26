/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.omop.mapping;

import java.util.List;

import com.ibm.fhir.model.resource.AdverseEvent;
import com.ibm.fhir.model.resource.Condition;
import com.ibm.fhir.model.resource.Encounter;
import com.ibm.fhir.model.resource.Location;
import com.ibm.fhir.model.resource.Medication;
import com.ibm.fhir.model.resource.MedicationRequest;
import com.ibm.fhir.model.resource.MedicationStatement;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.Organization;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Practitioner;
import com.ibm.fhir.model.resource.Procedure;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.Specimen;
import com.ibm.fhir.omop.table.Table;

/**
 * Many-to-many mappings between FHIR resources and OMOP Common Data Model tables based on the <a href="https://build.fhir.org/ig/HL7/cdmh/profiles.html">CDMH Implementation Guide</a>
 * and the <a href="https://www.ohdsi.org/web/wiki/doku.php?id=projects:workgroups:mappings_between_ohdsi_cdm_and_fhir">OHSDI FHIR Workgroup wiki page</a>
 */
public enum Mapping {
    PATIENT_TO_PERSON_AND_LOCATION(List.of(Patient.class), List.of(Table.PERSON, Table.LOCATION)),
    ENCOUNTER_TO_VISIT_OCCURRENCE(List.of(Encounter.class), List.of(Table.VISIT_OCCURRENCE)),
    LOCATION_TO_CARE_SITE(List.of(Location.class), List.of(Table.CARE_SITE)),
    CONDITION_TO_CONDITION_OCCURRENCE(List.of(Condition.class), List.of(Table.CONDITION_OCCURRENCE)),
    ADVERSE_EVENT_TO_DEATH(List.of(AdverseEvent.class), List.of(Table.DEATH)),
    PROCEDURE_TO_DEVICE_EXPOSURE(List.of(Procedure.class), List.of(Table.DEVICE_EXPOSURE)),
    MEDICATION_STATEMENT_AND_MEDICATION_REQUEST_AND_MEDICATION_TO_DRUG_EXPOSURE(List.of(MedicationStatement.class, MedicationRequest.class, Medication.class), List.of(Table.DRUG_EXPOSURE)),
    LOCATION_TO_LOCATION(List.of(Location.class), List.of(Table.LOCATION)),
    OBSERVATION_TO_OBSERVATION(List.of(Observation.class), List.of(Table.OBSERVATION)),
    PROCEDURE_TO_PROCEDURE_OCCURRENCE(List.of(Procedure.class), List.of(Table.PROCEDURE_OCCURRENCE)),
    PRACTITIONER_TO_PROVIDER(List.of(Practitioner.class), List.of(Table.PROVIDER)),
    SPECIMEN_TO_SPECIMEN(List.of(Specimen.class), List.of(Table.SPECIMEN)),
    ORGANIZATION_TO_CARE_SITE_AND_LOCATION(List.of(Organization.class), List.of(Table.CARE_SITE, Table.LOCATION));

    private final List<Class<? extends Resource>> resourceTypes;
    private final List<Table> tables;

    Mapping(List<Class<? extends Resource>> resourceTypes, List<Table> tables) {
        this.resourceTypes = resourceTypes;
        this.tables = tables;
    }

    public List<Class<? extends Resource>> resourceTypes() {
        return resourceTypes;
    }

    public List<Table> tables() {
        return tables;
    }
}
