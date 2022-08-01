#!/usr/bin/env bash
###############################################################################
# (C) Copyright IBM Corp. 2022
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# invoke this script with 'source' or '. set_tenant1_datastore_vars.sh'
# so that the variables are available in the session that invoked it

# note: any edits to this file should also be made in build/pre-integration-test.ps1

export TENANT1_PROFILE_RESOURCE_TYPES=Patient,Group,Practitioner,PractitionerRole,Person,RelatedPerson,Organization,Location,AllergyIntolerance,Observation,MedicationAdministration,StructureDefinition,ElementDefinition,CompartmentDefinition,CodeSystem,ValueSet,Encounter,Condition,MedicationRequest,Coverage,ServiceRequest,CarePlan,CareTeam,Claim,DiagnosticReport,ExplanationOfBenefit,Immunization,Procedure,Substance,Medication,Provenance,Consent

export TENANT1_REFERENCE_RESOURCE_TYPES=Patient,Group,Practitioner,PractitionerRole,Device,Organization,Location,Medication,Observation,MedicationAdministration,Procedure,Substance,StructureDefinition,ElementDefinition,CompartmentDefinition,CodeSystem,ValueSet

export TENANT1_STUDY1_RESOURCE_TYPES=Patient,Group,Practitioner,PractitionerRole,Device,Organization,Location,Encounter,AllergyIntolerance,Observation,Condition,CarePlan,Provenance,Medication,MedicationAdministration,Procedure,Substance,StructureDefinition,ElementDefinition,CompartmentDefinition,CodeSystem,ValueSet
