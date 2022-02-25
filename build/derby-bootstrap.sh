#!/usr/bin/env bash
###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# Sample usage:
# build/derby-bootstrap.sh ~/path/to/wlp/usr/servers/defaultServer

# The full path to the directory of this script, no matter where its called from
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
export WORKSPACE="$( dirname "${DIR}" )"

# Set the default db location
DB_LOC=${WORKSPACE}/derby

# Allow user to override default location
if [ $# -gt 0 ]
then
    DB_LOC=$1/derby
fi

java -jar ${WORKSPACE}/fhir-persistence-schema/target/fhir-persistence-schema-*-cli.jar \
  --db-type derby --prop db.database=${DB_LOC}/fhirDB --prop db.create=Y \
  --update-schema
java -jar ${WORKSPACE}/fhir-persistence-schema/target/fhir-persistence-schema-*-cli.jar \
  --db-type derby --prop db.database=${DB_LOC}/profile --prop db.create=Y \
  --prop resourceTypes=Patient,Group,Practitioner,PractitionerRole,Person,RelatedPerson,Organization,Location,Observation,MedicationAdministration,StructureDefinition,ElementDefinition,CompartmentDefinition,CodeSystem,ValueSet,Encounter,Condition,MedicationRequest,Coverage,ServiceRequest,CarePlan,CareTeam,Claim,DiagnosticReport,ExplanationOfBenefit,Immunization,Procedure,Medication,Provenance,Consent \
  --update-schema
java -jar ${WORKSPACE}/fhir-persistence-schema/target/fhir-persistence-schema-*-cli.jar \
  --db-type derby --prop db.database=${DB_LOC}/reference --prop db.create=Y \
  --prop resourceTypes=Patient,Group,Practitioner,PractitionerRole,Device,Organization,Location,Medication,Observation,MedicationAdministration,StructureDefinition,ElementDefinition,CompartmentDefinition,CodeSystem,ValueSet \
  --update-schema
java -jar ${WORKSPACE}/fhir-persistence-schema/target/fhir-persistence-schema-*-cli.jar \
  --db-type derby --prop db.database=${DB_LOC}/study1 --prop db.create=Y \
  --prop resourceTypes=Patient,Group,Practitioner,PractitionerRole,Device,Organization,Location,Encounter,AllergyIntolerance,Observation,Condition,CarePlan,Provenance,Medication,MedicationAdministration,StructureDefinition,ElementDefinition,CompartmentDefinition,CodeSystem,ValueSet \
  --update-schema
