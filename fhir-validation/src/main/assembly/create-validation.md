 To create the distribution, you can run the following profile. 
  
 ``` 
 mvn clean package -f fhir-validation/ -Pfhir-validation-distribution
 ```
 It'll create the following zip file. 
 
 ```
 fhir-validation/target/fhir-validation-distribution.zip
  Length      Date    Time    Name
---------  ---------- -----   ----
        0  10-09-2019 14:08   fhir-validation-dist/
   302248  09-06-2019 15:58   fhir-validation-dist/antlr4-runtime-4.5.3.jar
 34010947  09-24-2019 08:19   fhir-validation-dist/fhir-examples-4.0.0-SNAPSHOT.jar
   870504  09-06-2019 15:58   fhir-validation-dist/testng-6.9.10.jar
  6181816  10-08-2019 06:36   fhir-validation-dist/fhir-model-4.0.0-SNAPSHOT.jar
   281694  09-06-2019 15:58   fhir-validation-dist/bsh-2.0b4.jar
   140564  09-06-2019 15:58   fhir-validation-dist/jakarta.json-1.1.5.jar
   352550  10-08-2019 06:36   fhir-validation-dist/fhir-model-4.0.0-SNAPSHOT-tests.jar
    25058  09-11-2019 15:05   fhir-validation-dist/jakarta.annotation-api-1.3.5.jar
    63504  09-06-2019 15:58   fhir-validation-dist/jcommander-1.48.jar
 10307966  10-08-2019 06:36   fhir-validation-dist/fhir-registry-4.0.0-SNAPSHOT.jar
    18331  10-08-2019 06:35   fhir-validation-dist/fhir-core-4.0.0-SNAPSHOT.jar
   214788  09-06-2019 15:58   fhir-validation-dist/commons-io-2.6.jar
---------                     -------
 52769970                     13 files
 ```