This is the README file for the the JMeter test plan searchTest.jmx.

The searchTest.jmx test plan was written to just include several different searches over 19 different resources.  You can toggle any of the searches enabled or disabled to test and stress what you would like to do.  Look at the HTTP Request samplers in the Path: section to see what the search is doing.
This test plan was created with JMeter version 5.3

Also the JMeter plugin for Random CSV Data Set needs to be loaded before you can open this testplan in JMeter.  The use of the Random CSV Data plugin allows the test to run differently each time, as the randome picking of the FHIR ID's is across every iteration and threads.

Prerequisites for this test Plan:
* Information needed to be entered in the User Defined Variables config element in the test plan.
* CSV file of FHIR Patient ID's - path and file name to be stored in user defined variable patientIdFile ex: /User/me/file.csv
* CSV file of FHIR Practitioner ID's - path and file name to be stored in user defined variable pracIdFile ex: /User/me/file.csv
* CSV file of FHIR Organisation ID's - path and file name to be stored in user defined variable orgIdFile ex: /User/me/file.csv
* CSV file of FHIR Device ID's - path and file name to be stored in user defined variable deviceIdFile ex: /User/me/file.csv
* A single FHIR ID for the following resource types:  Patient, Practitioner, Claim, and Encounter to be stored in the user defined variables patientId, practId, claimId, and encounterId respectively. 

After opening this test plan you will need to add information to the User Defined Variables config element as described above.

Also the following variables will need to be filled in or verified:
* contextRoot - for the FHIR server URL, start and end with a / ex: /fhir-server/api/v4/
* targetHost - the url for the FHIR server
* targetPort - the port for the FHIR server
* httpHeadAuth - the encrypted basic auththentication
* httpHeadTenant - the tenant name configured in the FHIR server you want to target

You can toggle any of the tests enabled or disabled as desired to run the load you would like on the server.

The JMeter listeners added are standard JMeter listeners.  Feel free to add your own listener to suit your needs.

Here is a list of searches that make up the JMeter test plan. All data references are mock data.

```
[base]/ExplanationOfBenefit?patient=Patient/id-abc-123&created=ge2001&created=lt2023&_sort=-created&_count=10
[base]/ExplanationOfBenefit?patient=Patient/id-abc-123&created=ge2001&created=lt2021&_include=ExplanationOfBenefit:patient&_include=ExplanationOfBenefit:provider&_include=ExplanationOfBenefit:care-team&_include=ExplanationOfBenefit:coverage
[base]/ExplanationOfBenefit?patient=Patient/id-abc-123&created=ge2001&created=lt2021&_include=ExplanationOfBenefit:patient&_include=ExplanationOfBenefit:provider&_include=ExplanationOfBenefit:care-team&_include=ExplanationOfBenefit:coverage&_include=ExplanationOfBenefit:facility&_include=ExplanationOfBenefit:claim&_include=ExplanationOfBenefit:payee&_include=ExplanationOfBenefit:enterer&_include=ExplanationOfBenefit:encounter
[base]/ExplanationOfBenefit?patient=Patient/id-abc-123&created=ge2001&created=lt2021&_include=ExplanationOfBenefit:*&_count=1
[base]/Patient/id-abc-123/ExplanationOfBenefit?created=ge2001&created=lt2021&_include=ExplanationOfBenefit:*&_count=1
[base]/Patient/id-abc-123/ExplanationOfBenefit?created=ge2001&created=lt2021&_include=ExplanationOfBenefit:patient&_include=ExplanationOfBenefit:provider&_include=ExplanationOfBenefit:care-team&_include=ExplanationOfBenefit:claim&_include=ExplanationOfBenefit:payee&_include=ExplanationOfBenefit:enterer&_include=ExplanationOfBenefit:encounter
[base]/Patient/id-abc-123/ExplanationOfBenefit?created=ge2001&created=lt2021&_include=ExplanationOfBenefit:*&_count=1
[base]/Patient/id-abc-123/ExplanationOfBenefit?created=ge2001&created=lt2021&_count=1
[base]/Patient/id-abc-123
[base]/Practitioner/id-abc-123
[base]/Claim/id-abc-123
[base]/Encounter/id-abc-123
[base]/AllergyIntolerance?patient=Patient/id-abc-123&clinical-status=inactive&_include=AllergyIntolerance:*
[base]/Patient/id-abc-123/AllergyIntolerance?clinical-status=active&_revinclude=Provenance:target
[base]/Patient/id-abc-123/AllergyIntolerance?clinical-status=resolved
[base]/CarePlan?patient=Patient/id-abc-123&_include=CarePlan:*&_count=1
[base]/CarePlan?patient=Patient/id-abc-123&date=gt2005-01&status=revoked&category=assess-plan
[base]/CarePlan?patient=Patient/id-abc-123&date=gt2002-01&status=draft&_revinclude=Provenance:target
[base]/Patient/id-abc-123/CarePlan?date=gt2005-01&status=completed&category=assess-plan
[base]/CareTeam?patient=Patient/id-abc-123&status=suspended&_include=CareTeam:*
[base]/CareTeam?patient=Patient/id-abc-123&status=entered-in-error&_revinclude=Provenance:target
[base]/Patient/id-abc-123/CareTeam?status=active
[base]/Claim?patient=Patient/id-abc-123&use=claim&_include=Claim:*
[base]/Claim?patient=Patient/id-abc-123&use:missing=false&created=gt2002-01
[base]/Patient/id-abc-123/Claim?use:missing=false&created=ge2002-01
[base]/Condition?patient=Patient/id-abc-123&_include=Condition:*&_count=1
[base]/Condition?patient=Patient/id-abc-123&clinical-status=active&category=problem-list-item&_revinclude=Provenance:*
[base]/Condition?patient=Patient/id-abc-123&clinical-status:missing=false&onset-date=le2020-10
[base]/Patient/id-abc-123/Condition?clinical-status:missing=false&onset-date=le2020-10&code:missing=true
[base]/Device?patient=Patient/id-abc-123&_include=Device:*
[base]/Device?_id=id-abc-123&_include=Device:*
[base]/Device?patient=Patient/id-abc-123&_revinclude=Device:*&type:missing=false
[base]/Device?patient=Patient/id-abc-123&_revinclude=Provenance:*&type:missing=false&_include=Device:*
[base]/DiagnosticReport?patient=Patient/id-abc-123&date=gt1990-01-02&date=lt2021&_include=DiagnosticReport:*&_count=1
[base]/DiagnosticReport?patient=Patient/id-abc-123&date=gt1990-01-02&date=lt2021&category=LAB&status=final&_revinclude=Provenance:*&_count=1
[base]/DiagnosticReport?patient=Patient/id-abc-123&date=gt1990-01-02&date=lt2021&category=LAB&status=final&code=10190-7
[base]/Patient/id-abc-123/DiagnosticReport?date=gt1990-01-02&date=lt2021&category=LAB&status=final&code:missing=true
[base]/Encounter?patient=Patient/id-abc-123&_include=Encounter:*&date=ge2004
[base]/Encounter?status=finished&type:missing=false&patient=Patient/id-abc-123&_revinclude=Provenance:target&date=ge2004
[base]/Patient/id-abc-123/Encounter?status=planned&class=FLD&type:missing=false&date=ge2000
[base]/Patient/id-abc-123/Encounter?status:missing=false&class:missing=true&type:missing=false&date=le2021&identifier:missing=true
[base]/Goal?subject=Patient/id-abc-123&_include=Goal:*&target-date=gt1990&lifecycle-status=active
[base]/Goal?subject=Patient/id-abc-123&_revinclude=Provenance:*&target-date=gt1990&lifecycle-status:missing=false
[base]/Patient/id-abc-123/Goal?target-date=gt1990&lifecycle-status=planned
[base]/ImagingStudy?subject=Patient/id-abc-123&_include=ImagingStudy:*
[base]/Immunization?patient=Patient/id-abc-123&_include=Immunization:*&date=lt2022
[base]/Patient/id-abc-123/Immunization?_revinclude=Provenance:*&date=lt2022&status=completed
[base]/Patient/id-abc-123/Immunization?_revinclude=Provenance:*&date=lt2022&status:missing=true
[base]/MedicationAdministration?subject=Patient/id-abc-123&_include=MedicationAdministration:*
[base]/Patient/id-abc-123/MedicationAdministration
[base]/MedicationRequest?_include=MedicationRequest:*&patient=Patient/id-abc-123&authoredon=gt1980
[base]/MedicationRequest?_revinclude=Provenance:*&patient=Patient/id-abc-123&authoredon=gt1980&intent:missing=false&status=completed
[base]/Patient/id-abc-123/MedicationRequest?intent:missing=false&status=active
[base]/Observation?patient=Patient/id-abc-123&_include=Observation:*&date=lt2025-12-31
[base]/Observation?patient=Patient/id-abc-123&_revinclude=Provenance:target&date=gt2000-12-31&status=final
[base]/Patient/id-abc-123/Observation?date=gt2000-12-31&category=labratory
[base]/Patient/id-abc-123/Observation?date=gt2000-12-31&category:missing=true&code:missing=false
[base]/Organization?_id=id-abc-123&address=a
[base]/Organization?_id=id-abc-123&_include=Organization:*&_revinclude=Endpoint:organization&_revinclude=Provenance:*
[base]/Patient?_id=id-abc-123&birthdate=lt2020&_include=Patient:*
[base]/Patient?_id=id-abc-123&gender=female&_revinclude=Provenance:*&given:missing=false&name=a&family:missing=false
[base]/Patient?_id=id-abc-123&gender=male&name=e
[base]/Practitioner?_id=id-abc-123&_include=Practitioner:*
[base]/Practitioner?_id=id-abc-123&gender:missing=false&_revinclude=Provenance:*
[base]/Procedure?patient=Patient/id-abc-123&_include=Procedure:*&date=lt2030&status:missing=false
[base]/Procedure?patient=Patient/id-abc-123&_revinclude=Provenance:*&date=lt2030&code:missing=true
[base]/Patient/id-abc-123/Procedure?date=lt2030&_count=1
[base]/Account?patient=Patient/id-abc-123
[base]/AllergyIntolerance?patient=Patient/id-abc-123
[base]/AuditEvent?patient=Patient/id-abc-123
[base]/Basic?patient=Patient/id-abc-123
[base]/CarePlan?patient=Patient/id-abc-123
[base]/CareTeam?patient=Patient/id-abc-123
[base]/Claim?patient=Patient/id-abc-123
[base]/ClinicalImpression?patient=Patient/id-abc-123
[base]/Communication?patient=Patient/id-abc-123
[base]/CommunicationRequest?patient=Patient/id-abc-123
[base]/Composition?patient=Patient/id-abc-123
[base]/Condition?patient=Patient/id-abc-123
[base]/Consent?patient=Patient/id-abc-123
[base]/Coverage?patient=Patient/id-abc-123
[base]/CoverageEligibilityRequest?patient=Patient/id-abc-123
[base]/CoverageEligibilityResponse?patient=Patient/id-abc-123
[base]/DetectedIssue?patient=Patient/id-abc-123
[base]/DeviceUseStatement?patient=Patient/id-abc-123
[base]/DocumentManifest?patient=Patient/id-abc-123
[base]/Encounter?patient=Patient/id-abc-123
[base]/EnrollmentRequest?patient=Patient/id-abc-123
[base]/ExplanationOfBenefit?patient=Patient/id-abc-123
[base]/FamilyMemberHistory?patient=Patient/id-abc-123
[base]/Goal?patient=Patient/id-abc-123
[base]/GuidanceResponse?patient=Patient/id-abc-123
[base]/ImagingStudy?patient=Patient/id-abc-123
[base]/Immunization?patient=Patient/id-abc-123
[base]/ImmunizationEvaluation?patient=Patient/id-abc-123
[base]/MeasureReport?patient=Patient/id-abc-123
[base]/Media?patient=Patient/id-abc-123
[base]/MedicationDispense?patient=Patient/id-abc-123
[base]/MedicationStatement?patient=Patient/id-abc-123
[base]/MolecularSequence?patient=Patient/id-abc-123
[base]/NutritionOrder?patient=Patient/id-abc-123
[base]/Observation?patient=Patient/id-abc-123
[base]/Provenance?patient=Patient/id-abc-123
[base]/ResearchSubject?patient=Patient/id-abc-123
[base]/Task?patient=Patient/id-abc-123
[base]/Device?patient=Patient/id-abc-123
[base]/DiagnosticReport?patient=Patient/id-abc-123
[base]/MedicationAdministration?patient=Patient/id-abc-123
[base]/MedicationRequest?patient=Patient/id-abc-123
[base]/Procedure?patient=Patient/id-abc-123
[base]/ExplanationOfBenefit?claim:Claim.created=2015-10-16&patient:Patient._id=id-abc-123
[base]/ExplanationOfBenefit?claim:Claim.created=2015-10-16&patient:Patient._id=id-abc-123&_include=ExplanationOfBenefit:*
[base]/Patient/id-abc-123/ExplanationOfBenefit?claim:Claim.created=2015-10-16
[base]/Patient/id-abc-123/ExplanationOfBenefit?claim:Claim.created=2015-10-16&_include=ExplanationOfBenefit:*
[base]/Patient/id-abc-123/ExplanationOfBenefit?_include=ExplanationOfBenefit:*&_count=100
[base]/Patient/id-abc-123/Claim?_lastUpdated=ge2020-10-29
[base]/Patient?_id=id-abc-123&_lastUpdated=ge2020
[base]?_id=id-abc-123
[base]?_lastUpdated=2020-10-31
[base]?_id=id-abc-123&_type=ExplanationOfBenefit
[base]?_id=id-abc-123&_type=ExplanationOfBenefit&patient:Patient.gender=male
[base]?_lastUpdated=2020-10-20T19:39:06Z
[base]?_lastUpdated=ge2020-10
[base]?_lastUpdated=2020-10&_type=ExplanationOfBenefit
[base]?_lastUpdated=2020-10-30&_type=Patient
[base]?_lastUpdated=2020-10-30&_type=Practitioner
[base]/Patient?birthdate=1991-10-22
[base]/Organization?_id=id-abc-123&_revinclude=Location:organization&_revinclude=HealthcareService:organization
[base]/Organization?_has:CareTeam:participant:participant:Practitioner.gender=female&_sort=-name&_summary=false&_count=1000&_page=2
[base]/Observation?code-value-quantity=http://loinc.org%7c2339-0$69.1
[base]/Observation?code-value-quantity=http://loinc.org%7c2339-0$68.6
[base]/Observation?code-value-quantity=http://loinc.org%7c2339-0$69.1&_revinclude=DiagnosticReport:result
[base]/DiagnosticReport?result.code-value-quantity=http://loinc.org%7c2339-0$69.1
[base]/Observation?code-value-concept=http://loinc.org%7c5778-6$http://snomed.info/sct%7c371254008
[base]/Observation?code-value-concept=http://loinc.org%7c5778-6$371254008
[base]/Observation?code-value-concept=5778-6$371254008
[base]/Patient?_has:Observation:patient:code-value-quantity=http://loinc.org%7c2339-0$68.6
[base]/Observation?code-value-quantity=http://loinc.org%7c2339-0$68.6&_has:DiagnosticReport:result:_lastUpdated=gt2020-10-01&_include=Observation:*
[base]/Patient/id-abc-123/Claim?_has:ExplanationOfBenefit:claim:created=ge2010&_revinclude=ExplanationOfBenefit:claim
[base]/Patient/id-abc-123/Observation?_has:DiagnosticReport:result:code=unknown&_revinclude=DiagnosticReport:result
[base]/Patient/id-abc-123/CareTeam?status=inactive
[base]/Patient/id-abc-123/CareTeam?status=inactive&_include=CareTeam:encounter
[base]/Patient/id-abc-123/CareTeam?status=inactive&_include=CareTeam:encounter&_revinclude=CarePlan:care-team
[base]/Patient/id-abc-123/CareTeam?status=inactive&_include=CareTeam:encounter&_revinclude=CarePlan:care-team&participant:Practitioner.gender=male
[base]/Patient/id-abc-123/CareTeam?status=inactive&_include=CareTeam:encounter&_revinclude=CarePlan:care-team&participant:Practitioner.gender=male&_include=CareTeam:subject:Patient
[base]/Patient/id-abc-123/CareTeam?status=inactive&_include=CareTeam:encounter&_revinclude=CarePlan:care-team&participant:Practitioner.gender=male&_include=CareTeam:subject:Patient&_include=CareTeam:participant:Organization
[base]/Patient/id-abc-123/CareTeam?status=inactive&_include=CareTeam:encounter&_revinclude=CarePlan:care-team&participant:Practitioner.gender=male&_include=CareTeam:subject:Patient&_include=CareTeam:participant:Organization&_summary=true
[base]/Patient/id-abc-123/CareTeam?status=inactive&_include=CareTeam:encounter&_revinclude=CarePlan:care-team&participant:Practitioner.gender=male&_include=CareTeam:subject:Patient&_include=CareTeam:participant:Organization&_summary=true&_has:CarePlan:care-team:status=completed
[base]/Patient/id-abc-123/Patient?_has:Encounter:subject:_has:Observation:encounter:value-quantity=10&_sort=-birthdate
[base]/Patient/id-abc-123/CareTeam?status=inactive&_include=CareTeam:encounter&_total=estimate
[base]/Patient/id-abc-123/CareTeam?_total=accurate&status=inactive&_include=CareTeam:encounter&_revinclude=CarePlan:care-team
[base]/Patient/id-abc-123/CareTeam?status=inactive&_include=CareTeam:encounter&_revinclude=CarePlan:care-team&participant:Practitioner.gender=male&_total=none&_include=CareTeam:subject:Patient&_include=CareTeam:participant:Organization&_summary=true
[base]/ExplanationOfBenefit?patient=Patient/id-abc-123&created=ge2001&created=lt2021&_include=ExplanationOfBenefit:patient&_include=ExplanationOfBenefit:provider&_include=ExplanationOfBenefit:care-team&_include=ExplanationOfBenefit:coverage&_include:iterate=Coverage:*
[base]/Patient/id-abc-123/ExplanationOfBenefit?created=ge2001&created=lt2021&_include=ExplanationOfBenefit:*&_include:iterate=Organization:*&_include:iterate=Device:*&_include:iterate=Location:*&_include:iterate=Patient:*&_count=1
```