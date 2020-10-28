-------------------------------------------------------------------------------
-- (C) Copyright IBM Corp. 2020
--
-- SPDX-License-Identifier: Apache-2.0
-------------------------------------------------------------------------------
-- Run once after the schema change implemented in issue 1366 and
-- the reindex operation reprocessed all the resources. Reclaims
-- space so that we can assess how much space the new schema
-- uses compared to the old design.

VACUUM FULL fhirdata.CarePlan_str_values;
VACUUM FULL fhirdata.CarePlan_date_values;
VACUUM FULL fhirdata.CarePlan_number_values;
VACUUM FULL fhirdata.CarePlan_quantity_values;
VACUUM FULL fhirdata.CarePlan_composites;
VACUUM FULL fhirdata.CarePlan_latlng_values;
VACUUM FULL fhirdata.CareTeam_str_values;
VACUUM FULL fhirdata.CareTeam_date_values;
VACUUM FULL fhirdata.CareTeam_number_values;
VACUUM FULL fhirdata.CareTeam_quantity_values;
VACUUM FULL fhirdata.CareTeam_composites;
VACUUM FULL fhirdata.CareTeam_latlng_values;
VACUUM FULL fhirdata.Condition_str_values;
VACUUM FULL fhirdata.Condition_date_values;
VACUUM FULL fhirdata.Condition_number_values;
VACUUM FULL fhirdata.Condition_quantity_values;
VACUUM FULL fhirdata.Condition_composites;
VACUUM FULL fhirdata.Condition_latlng_values;
VACUUM FULL fhirdata.DiagnosticReport_str_values;
VACUUM FULL fhirdata.DiagnosticReport_date_values;
VACUUM FULL fhirdata.DiagnosticReport_number_values;
VACUUM FULL fhirdata.DiagnosticReport_quantity_values;
VACUUM FULL fhirdata.DiagnosticReport_composites;
VACUUM FULL fhirdata.DiagnosticReport_latlng_values;
VACUUM FULL fhirdata.Encounter_str_values;
VACUUM FULL fhirdata.Encounter_date_values;
VACUUM FULL fhirdata.Encounter_number_values;
VACUUM FULL fhirdata.Encounter_quantity_values;
VACUUM FULL fhirdata.Encounter_composites;
VACUUM FULL fhirdata.Encounter_latlng_values;
VACUUM FULL fhirdata.Goal_str_values;
VACUUM FULL fhirdata.Goal_date_values;
VACUUM FULL fhirdata.Goal_number_values;
VACUUM FULL fhirdata.Goal_quantity_values;
VACUUM FULL fhirdata.Goal_composites;
VACUUM FULL fhirdata.Goal_latlng_values;
VACUUM FULL fhirdata.Immunization_str_values;
VACUUM FULL fhirdata.Immunization_date_values;
VACUUM FULL fhirdata.Immunization_number_values;
VACUUM FULL fhirdata.Immunization_quantity_values;
VACUUM FULL fhirdata.Immunization_composites;
VACUUM FULL fhirdata.Immunization_latlng_values;
VACUUM FULL fhirdata.MedicationRequest_str_values;
VACUUM FULL fhirdata.MedicationRequest_date_values;
VACUUM FULL fhirdata.MedicationRequest_number_values;
VACUUM FULL fhirdata.MedicationRequest_quantity_values;
VACUUM FULL fhirdata.MedicationRequest_composites;
VACUUM FULL fhirdata.MedicationRequest_latlng_values;
VACUUM FULL fhirdata.Organization_str_values;
VACUUM FULL fhirdata.Organization_date_values;
VACUUM FULL fhirdata.Organization_number_values;
VACUUM FULL fhirdata.Organization_quantity_values;
VACUUM FULL fhirdata.Organization_composites;
VACUUM FULL fhirdata.Organization_latlng_values;
VACUUM FULL fhirdata.Patient_str_values;
VACUUM FULL fhirdata.Patient_date_values;
VACUUM FULL fhirdata.Patient_number_values;
VACUUM FULL fhirdata.Patient_quantity_values;
VACUUM FULL fhirdata.Patient_composites;
VACUUM FULL fhirdata.Patient_latlng_values;
VACUUM FULL fhirdata.Practitioner_str_values;
VACUUM FULL fhirdata.Practitioner_date_values;
VACUUM FULL fhirdata.Practitioner_number_values;
VACUUM FULL fhirdata.Practitioner_quantity_values;
VACUUM FULL fhirdata.Practitioner_composites;
VACUUM FULL fhirdata.Practitioner_latlng_values;
VACUUM FULL fhirdata.Procedure_str_values;
VACUUM FULL fhirdata.Procedure_date_values;
VACUUM FULL fhirdata.Procedure_number_values;
VACUUM FULL fhirdata.Procedure_quantity_values;
VACUUM FULL fhirdata.Procedure_composites;
VACUUM FULL fhirdata.Procedure_latlng_values;
