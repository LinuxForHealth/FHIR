/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.omop.table;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.ibm.fhir.omop.version.Version;

/**
 * An enumeration that contains metadata about the OMOP Common Data Model Schema
 */
public enum Table {
    ATTRIBUTE_DEFINITION("attribute_definition",
        Arrays.asList("attribute_definition_id", "attribute_name", "attribute_description", "attribute_type_concept_id", "attribute_syntax"),
        Arrays.asList("attribute_definition_id", "attribute_name", "attribute_description", "attribute_type_concept_id", "attribute_syntax")),
    CARE_SITE("care_site",
        Arrays.asList("care_site_id", "care_site_name", "place_of_service_concept_id", "location_id", "care_site_source_value", "place_of_service_source_value"),
        Arrays.asList("care_site_id", "care_site_name", "place_of_service_concept_id", "location_id", "care_site_source_value", "place_of_service_source_value")),
    CDM_SOURCE("cdm_source",
        Arrays.asList("cdm_source_name", "cdm_source_abbreviation", "cdm_holder", "source_description", "source_documentation_reference", "cdm_etl_reference", "source_release_date", "cdm_release_date", "cdm_version", "vocabulary_version"),
        Arrays.asList("cdm_source_name", "cdm_source_abbreviation", "cdm_holder", "source_description", "source_documentation_reference", "cdm_etl_reference", "source_release_date", "cdm_release_date", "cdm_version", "vocabulary_version")),
    CONCEPT("concept",
        Arrays.asList("concept_id", "concept_name", "domain_id", "vocabulary_id", "concept_class_id", "standard_concept", "concept_code", "valid_start_date", "valid_end_date", "invalid_reason"),
        Arrays.asList("concept_id", "concept_name", "domain_id", "vocabulary_id", "concept_class_id", "standard_concept", "concept_code", "valid_start_date", "valid_end_date", "invalid_reason")),
    CONCEPT_ANCESTOR("concept_ancestor",
        Arrays.asList("ancestor_concept_id", "descendant_concept_id", "min_levels_of_separation", "max_levels_of_separation"),
        Arrays.asList("ancestor_concept_id", "descendant_concept_id", "min_levels_of_separation", "max_levels_of_separation")),
    CONCEPT_CLASS("concept_class",
        Arrays.asList("concept_class_id", "concept_class_name", "concept_class_concept_id"),
        Arrays.asList("concept_class_id", "concept_class_name", "concept_class_concept_id")),
    CONCEPT_RELATIONSHIP("concept_relationship",
        Arrays.asList("concept_id_1", "concept_id_2", "relationship_id", "valid_start_date", "valid_end_date", "invalid_reason"),
        Arrays.asList("concept_id_1", "concept_id_2", "relationship_id", "valid_start_date", "valid_end_date", "invalid_reason")),
    CONCEPT_SYNONYM("concept_synonym",
        Arrays.asList("concept_id", "concept_synonym_name", "language_concept_id"),
        Arrays.asList("concept_id", "concept_synonym_name", "language_concept_id")),
    CONDITION_ERA("condition_era",
        Arrays.asList("condition_era_id", "person_id", "condition_concept_id", "condition_era_start_datetime", "condition_era_end_datetime", "condition_occurrence_count"),
        Arrays.asList("condition_era_id", "person_id", "condition_concept_id", "condition_era_start_datetime", "condition_era_end_datetime", "condition_occurrence_count")),
    CONDITION_OCCURRENCE("condition_occurrence",
        Arrays.asList("condition_occurrence_id", "person_id", "condition_concept_id", "condition_start_date", "condition_start_datetime", "condition_end_date", "condition_end_datetime", "condition_type_concept_id", "condition_status_concept_id", "stop_reason", "provider_id", "visit_occurrence_id", "visit_detail_id", "condition_source_value", "condition_source_concept_id", "condition_status_source_value"),
        Arrays.asList("condition_occurrence_id", "person_id", "condition_concept_id", "condition_start_date", "condition_start_datetime", "condition_end_date", "condition_end_datetime", "condition_type_concept_id", "condition_status_concept_id", "stop_reason", "provider_id", "visit_occurrence_id", "visit_detail_id", "condition_source_value", "condition_source_concept_id", "condition_status_source_value")),
    COST("cost",
        Arrays.asList("cost_id", "person_id", "cost_event_id", "cost_event_field_concept_id", "cost_concept_id", "cost_type_concept_id", "currency_concept_id", "cost", "incurred_date", "billed_date", "paid_date", "revenue_code_concept_id", "drg_concept_id", "cost_source_value", "cost_source_concept_id", "revenue_code_source_value", "drg_source_value", "payer_plan_period_id"),
        Arrays.asList("cost_id", "person_id", "cost_event_id", "cost_event_field_concept_id", "cost_concept_id", "cost_type_concept_id", "currency_concept_id", "cost", "incurred_date", "billed_date", "paid_date", "revenue_code_concept_id", "drg_concept_id", "cost_source_value", "cost_source_concept_id", "revenue_code_source_value", "drg_source_value", "payer_plan_period_id")),
    DEATH("death",
        Arrays.asList("person_id", "death_date", "death_datetime", "death_type_concept_id", "cause_concept_id", "cause_source_value", "cause_source_concept_id"),
        Collections.emptyList()),
    DEVICE_EXPOSURE("device_exposure",
        Arrays.asList("device_exposure_id", "person_id", "device_concept_id", "device_exposure_start_date", "device_exposure_start_datetime", "device_exposure_end_date", "device_exposure_end_datetime", "device_type_concept_id", "unique_device_id", "quantity", "provider_id", "visit_occurrence_id", "visit_detail_id", "device_source_value", "device_source_concept_id"),
        Arrays.asList("device_exposure_id", "person_id", "device_concept_id", "device_exposure_start_date", "device_exposure_start_datetime", "device_exposure_end_date", "device_exposure_end_datetime", "device_type_concept_id", "unique_device_id", "quantity", "provider_id", "visit_occurrence_id", "visit_detail_id", "device_source_value", "device_source_concept_id")),
    DOMAIN("domain",
        Arrays.asList("domain_id", "domain_name", "domain_concept_id"),
        Arrays.asList("domain_id", "domain_name", "domain_concept_id")),
    DOSE_ERA("dose_era",
        Arrays.asList("dose_era_id", "person_id", "drug_concept_id", "unit_concept_id", "dose_value", "dose_era_start_datetime", "dose_era_end_datetime"),
        Arrays.asList("dose_era_id", "person_id", "drug_concept_id", "unit_concept_id", "dose_value", "dose_era_start_datetime", "dose_era_end_datetime")),
    DRUG_ERA("drug_era",
        Arrays.asList("drug_era_id", "person_id", "drug_concept_id", "drug_era_start_datetime", "drug_era_end_datetime", "drug_exposure_count", "gap_days"),
        Arrays.asList("drug_era_id", "person_id", "drug_concept_id", "drug_era_start_datetime", "drug_era_end_datetime", "drug_exposure_count", "gap_days")),
    DRUG_EXPOSURE("drug_exposure",
        Arrays.asList("drug_exposure_id", "person_id", "drug_concept_id", "drug_exposure_start_date", "drug_exposure_start_datetime", "drug_exposure_end_date", "drug_exposure_end_datetime", "verbatim_end_date", "drug_type_concept_id", "stop_reason", "refills", "quantity", "days_supply", "sig", "route_concept_id", "lot_number", "provider_id", "visit_occurrence_id", "visit_detail_id", "drug_source_value", "drug_source_concept_id", "route_source_value", "dose_unit_source_value"),
        Arrays.asList("drug_exposure_id", "person_id", "drug_concept_id", "drug_exposure_start_date", "drug_exposure_start_datetime", "drug_exposure_end_date", "drug_exposure_end_datetime", "verbatim_end_date", "drug_type_concept_id", "stop_reason", "refills", "quantity", "days_supply", "sig", "route_concept_id", "lot_number", "provider_id", "visit_occurrence_id", "visit_detail_id", "drug_source_value", "drug_source_concept_id", "route_source_value", "dose_unit_source_value")),
    DRUG_STRENGTH("drug_strength",
        Arrays.asList("drug_concept_id", "ingredient_concept_id", "amount_value", "amount_unit_concept_id", "numerator_value", "numerator_unit_concept_id", "denominator_value", "denominator_unit_concept_id", "box_size", "valid_start_date", "valid_end_date", "invalid_reason"),
        Arrays.asList("drug_concept_id", "ingredient_concept_id", "amount_value", "amount_unit_concept_id", "numerator_value", "numerator_unit_concept_id", "denominator_value", "denominator_unit_concept_id", "box_size", "valid_start_date", "valid_end_date", "invalid_reason")),
    FACT_RELATIONSHIP("fact_relationship",
        Arrays.asList("domain_concept_id_1", "fact_id_1", "domain_concept_id_2", "fact_id_2", "relationship_concept_id"),
        Arrays.asList("domain_concept_id_1", "fact_id_1", "domain_concept_id_2", "fact_id_2", "relationship_concept_id")),
    LOCATION("location",
        Arrays.asList("location_id", "address_1", "address_2", "city", "state", "zip", "county", "country", "location_source_value"),
        Arrays.asList("location_id", "address_1", "address_2", "city", "state", "zip", "county", "country", "location_source_value", "latitude", "longitude")),
    LOCATION_HISTORY("location_history",
        Collections.emptyList(),
        Arrays.asList("location_history_id", "location_id", "relationship_type_concept_id", "domain_id", "entity_id", "start_date", "end_date")),
    MEASUREMENT("measurement",
        Arrays.asList("measurement_id", "person_id", "measurement_concept_id", "measurement_date", "measurement_datetime", "measurement_time", "measurement_type_concept_id", "operator_concept_id", "value_as_number", "value_as_concept_id", "unit_concept_id", "range_low", "range_high", "provider_id", "visit_occurrence_id", "visit_detail_id", "measurement_source_value", "measurement_source_concept_id", "unit_source_value", "value_source_value"),
        Arrays.asList("measurement_id", "person_id", "measurement_concept_id", "measurement_date", "measurement_datetime", "measurement_time", "measurement_type_concept_id", "operator_concept_id", "value_as_number", "value_as_concept_id", "unit_concept_id", "range_low", "range_high", "provider_id", "visit_occurrence_id", "visit_detail_id", "measurement_source_value", "measurement_source_concept_id", "unit_source_value", "value_source_value")),
    METADATA("metadata",
        Arrays.asList("metadata_concept_id", "metadata_type_concept_id", "name", "value_as_string", "value_as_concept_id", "metadata_date", "metadata_datetime"),
        Arrays.asList("metadata_concept_id", "metadata_type_concept_id", "name", "value_as_string", "value_as_concept_id", "metadata_date", "metadata_datetime")),
    NOTE("note",
        Arrays.asList("note_id", "person_id", "note_event_id", "note_event_field_concept_id", "note_date", "note_datetime", "note_type_concept_id", "note_class_concept_id", "note_title", "note_text", "encoding_concept_id", "language_concept_id", "provider_id", "visit_occurrence_id", "visit_detail_id", "note_source_value"),
        Arrays.asList("note_id", "person_id", "note_event_id", "note_event_field_concept_id", "note_date", "note_datetime", "note_type_concept_id", "note_class_concept_id", "note_title", "note_text", "encoding_concept_id", "language_concept_id", "provider_id", "visit_occurrence_id", "visit_detail_id", "note_source_value")),
    NOTE_NLP("note_nlp",
        Arrays.asList("note_nlp_id", "note_id", "section_concept_id", "snippet", "offset", "lexical_variant", "note_nlp_concept_id", "nlp_system", "nlp_date", "nlp_datetime", "term_exists", "term_temporal", "term_modifiers", "note_nlp_source_concept_id"),
        Arrays.asList("note_nlp_id", "note_id", "section_concept_id", "snippet", "offset", "lexical_variant", "note_nlp_concept_id", "nlp_system", "nlp_date", "nlp_datetime", "term_exists", "term_temporal", "term_modifiers", "note_nlp_source_concept_id")),
    OBSERVATION("observation",
        Arrays.asList("observation_id", "person_id", "observation_concept_id", "observation_date", "observation_datetime", "observation_type_concept_id", "value_as_number", "value_as_string", "value_as_concept_id", "qualifier_concept_id", "unit_concept_id", "provider_id", "visit_occurrence_id", "visit_detail_id", "observation_source_value", "observation_source_concept_id", "unit_source_value", "qualifier_source_value", "observation_event_id", "obs_event_field_concept_id", "value_as_datetime"),
        Arrays.asList("observation_id", "person_id", "observation_concept_id", "observation_date", "observation_datetime", "observation_type_concept_id", "value_as_number", "value_as_string", "value_as_concept_id", "qualifier_concept_id", "unit_concept_id", "provider_id", "visit_occurrence_id", "visit_detail_id", "observation_source_value", "observation_source_concept_id", "unit_source_value", "qualifier_source_value", "observation_event_id", "obs_event_field_concept_id", "value_as_datetime")),
    OBSERVATION_PERIOD("observation_period",
        Arrays.asList("observation_period_id", "person_id", "observation_period_start_date", "observation_period_end_date", "period_type_concept_id"),
        Arrays.asList("observation_period_id", "person_id", "observation_period_start_date", "observation_period_end_date", "period_type_concept_id")),
    PAYER_PLAN_PERIOD("payer_plan_period",
        Arrays.asList("payer_plan_period_id", "person_id", "contract_person_id", "payer_plan_period_start_date", "payer_plan_period_end_date", "payer_concept_id", "plan_concept_id", "contract_concept_id", "sponsor_concept_id", "stop_reason_concept_id", "payer_source_value", "payer_source_concept_id", "plan_source_value", "plan_source_concept_id", "contract_source_value", "contract_source_concept_id", "sponsor_source_value", "sponsor_source_concept_id", "family_source_value", "stop_reason_source_value", "stop_reason_source_concept_id"),
        Arrays.asList("payer_plan_period_id", "person_id", "contract_person_id", "payer_plan_period_start_date", "payer_plan_period_end_date", "payer_concept_id", "plan_concept_id", "contract_concept_id", "sponsor_concept_id", "stop_reason_concept_id", "payer_source_value", "payer_source_concept_id", "plan_source_value", "plan_source_concept_id", "contract_source_value", "contract_source_concept_id", "sponsor_source_value", "sponsor_source_concept_id", "family_source_value", "stop_reason_source_value", "stop_reason_source_concept_id")),
    PERSON("person",
        Arrays.asList("person_id", "gender_concept_id", "year_of_birth", "month_of_birth", "day_of_birth", "birth_datetime", "death_datetime", "race_concept_id", "ethnicity_concept_id", "location_id", "provider_id", "care_site_id", "person_source_value", "gender_source_value", "gender_source_concept_id", "race_source_value", "race_source_concept_id", "ethnicity_source_value", "ethnicity_source_concept_id"),
        Arrays.asList("person_id", "gender_concept_id", "year_of_birth", "month_of_birth", "day_of_birth", "birth_datetime", "death_datetime", "race_concept_id", "ethnicity_concept_id", "location_id", "provider_id", "care_site_id", "person_source_value", "gender_source_value", "gender_source_concept_id", "race_source_value", "race_source_concept_id", "ethnicity_source_value", "ethnicity_source_concept_id")),
    PROCEDURE_OCCURRENCE("procedure_occurrence",
        Arrays.asList("procedure_occurrence_id", "person_id", "procedure_concept_id", "procedure_date", "procedure_datetime", "procedure_type_concept_id", "modifier_concept_id", "quantity", "provider_id", "visit_occurrence_id", "visit_detail_id", "procedure_source_value", "procedure_source_concept_id", "modifier_source_value"),
        Arrays.asList("procedure_occurrence_id", "person_id", "procedure_concept_id", "procedure_date", "procedure_datetime", "procedure_type_concept_id", "modifier_concept_id", "quantity", "provider_id", "visit_occurrence_id", "visit_detail_id", "procedure_source_value", "procedure_source_concept_id", "modifier_source_value")),
    PROVIDER("provider",
        Arrays.asList("provider_id", "provider_name", "NPI", "DEA", "specialty_concept_id", "care_site_id", "year_of_birth", "gender_concept_id", "provider_source_value", "specialty_source_value", "specialty_source_concept_id", "gender_source_value", "gender_source_concept_id"),
        Arrays.asList("provider_id", "provider_name", "NPI", "DEA", "specialty_concept_id", "care_site_id", "year_of_birth", "gender_concept_id", "provider_source_value", "specialty_source_value", "specialty_source_concept_id", "gender_source_value", "gender_source_concept_id")),
    RELATIONSHIP("relationship",
        Arrays.asList("relationship_id", "relationship_name", "is_hierarchical", "defines_ancestry", "reverse_relationship_id", "relationship_concept_id"),
        Arrays.asList("relationship_id", "relationship_name", "is_hierarchical", "defines_ancestry", "reverse_relationship_id", "relationship_concept_id")),
    SOURCE_TO_CONCEPT_MAP("source_to_concept_map",
        Arrays.asList("source_code", "source_concept_id", "source_vocabulary_id", "source_code_description", "target_concept_id", "target_vocabulary_id", "valid_start_date", "valid_end_date", "invalid_reason"),
        Arrays.asList("source_code", "source_concept_id", "source_vocabulary_id", "source_code_description", "target_concept_id", "target_vocabulary_id", "valid_start_date", "valid_end_date", "invalid_reason")),
    SPECIMEN("specimen",
        Arrays.asList("specimen_id", "person_id", "specimen_concept_id", "specimen_type_concept_id", "specimen_date", "specimen_datetime", "quantity", "unit_concept_id", "anatomic_site_concept_id", "disease_status_concept_id", "specimen_source_id", "specimen_source_value", "unit_source_value", "anatomic_site_source_value", "disease_status_source_value"),
        Arrays.asList("specimen_id", "person_id", "specimen_concept_id", "specimen_type_concept_id", "specimen_date", "specimen_datetime", "quantity", "unit_concept_id", "anatomic_site_concept_id", "disease_status_concept_id", "specimen_source_id", "specimen_source_value", "unit_source_value", "anatomic_site_source_value", "disease_status_source_value")),
    SURVEY_CONDUCT("survey_conduct",
        Collections.emptyList(),
        Arrays.asList("survey_conduct_id", "person_id", "survey_concept_id", "survey_start_date", "survey_start_datetime", "survey_end_date", "survey_end_datetime", "provider_id", "assisted_concept_id", "respondent_type_concept_id", "timing_concept_id", "collection_method_concept_id", "assisted_source_value", "respondent_type_source_value", "timing_source_value", "collection_method_source_value", "survey_source_value", "survey_source_concept_id", "survey_source_identifier", "validated_survey_concept_id", "validated_survey_source_value", "survey_version_number", "visit_occurrence_id", "visit_detail_id", "response_visit_occurrence_id")),
    VISIT_DETAIL("visit_detail",
        Arrays.asList("visit_detail_id", "person_id", "visit_detail_concept_id", "visit_detail_start_date", "visit_detail_start_datetime", "visit_detail_end_date", "visit_detail_end_datetime", "visit_detail_type_concept_id", "provider_id", "care_site_id", "discharge_to_concept_id", "admitted_from_concept_id", "admitted_from_source_value", "visit_detail_source_value", "visit_detail_source_concept_id", "discharge_to_source_value", "preceding_visit_detail_id", "visit_detail_parent_id", "visit_occurrence_id"),
        Arrays.asList("visit_detail_id", "person_id", "visit_detail_concept_id", "visit_detail_start_date", "visit_detail_start_datetime", "visit_detail_end_date", "visit_detail_end_datetime", "visit_detail_type_concept_id", "provider_id", "care_site_id", "discharge_to_concept_id", "admitted_from_concept_id", "admitted_from_source_value", "visit_detail_source_value", "visit_detail_source_concept_id", "discharge_to_source_value", "preceding_visit_detail_id", "visit_detail_parent_id", "visit_occurrence_id")),
    VISIT_OCCURRENCE("visit_occurrence",
        Arrays.asList("visit_occurrence_id", "person_id", "visit_concept_id", "visit_start_date", "visit_start_datetime", "visit_end_date", "visit_end_datetime", "visit_type_concept_id", "provider_id", "care_site_id", "visit_source_value", "visit_source_concept_id", "admitted_from_concept_id", "admitted_from_source_value", "discharge_to_source_value", "discharge_to_concept_id", "preceding_visit_occurrence_id"),
        Arrays.asList("visit_occurrence_id", "person_id", "visit_concept_id", "visit_start_date", "visit_start_datetime", "visit_end_date", "visit_end_datetime", "visit_type_concept_id", "provider_id", "care_site_id", "visit_source_value", "visit_source_concept_id", "admitted_from_concept_id", "admitted_from_source_value", "discharge_to_source_value", "discharge_to_concept_id", "preceding_visit_occurrence_id")),
    VOCABULARY("vocabulary",
        Arrays.asList("vocabulary_id", "vocabulary_name", "vocabulary_reference", "vocabulary_version", "vocabulary_concept_id"),
        Arrays.asList("vocabulary_id", "vocabulary_name", "vocabulary_reference", "vocabulary_version", "vocabulary_concept_id"));

    private final String tableName;
    private final List<String> v5ColumnNames;
    private final List<String> v6ColumnNames;

    Table(String tableName, List<String> v5ColumnNames, List<String> v6ColumnNames) {
        this.tableName = tableName;
        this.v5ColumnNames = Collections.unmodifiableList(v5ColumnNames);
        this.v6ColumnNames = Collections.unmodifiableList(v6ColumnNames);
    }

    public String tableName() {
        return tableName;
    }

    public List<String> columnNames(Version version) {
        switch (version) {
        case OMOP_CDM_V5_3_1:
            return v5ColumnNames;
        case OMOP_CDM_V6_0:
            return v6ColumnNames;
        default:
            throw new IllegalArgumentException("Unsupported version: " + version);
        }
    }
}
