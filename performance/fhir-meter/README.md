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
