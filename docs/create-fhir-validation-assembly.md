# IBM FHIR Server - fhir-validation assembly creation 

### Pre-Requisites

**Repository**  
Navigate to https://github.com/IBM/FHIR 

Open a terminal window 

Clone to a local working directory 
`git clone git@github.com:IBM/FHIR.git`

**Maven**  
You must have maven installed to create the build. https://maven.apache.org/ 
It must be in the `PATH`.

**Dependencies**  
The `fhir-validation` module requires the `fhir-examples` be installed prior. 

``` 
 mvn clean install -f fhir-examples/ 
 ```

You should see `[INFO] BUILD SUCCESS`, and are ready to proceed.

This installs the examples which are part of the build. 
 
### Build 
 To create the distribution, you can run the following profile. 
  
 ``` 
 mvn clean package -f fhir-validation/ -Pfhir-validation-distribution
 ```
 It'll create the following zip file - `fhir-validation/target/fhir-validation-distribution.zip` 

You should see `[INFO] BUILD SUCCESS`, and the assembly is ready.

This file contains: 
 
 ```
  Length      Date    Time    Name
---------  ---------- -----   ----
        0  10-09-2019 14:18   fhir-validation-dist/
   302248  09-06-2019 15:58   fhir-validation-dist/antlr4-runtime-4.5.3.jar
  6181816  10-08-2019 06:36   fhir-validation-dist/fhir-model-4.0.0-SNAPSHOT.jar
   140564  09-06-2019 15:58   fhir-validation-dist/jakarta.json-1.1.5.jar
    18331  10-08-2019 06:35   fhir-validation-dist/fhir-core-4.0.0-SNAPSHOT.jar
    25058  09-11-2019 15:05   fhir-validation-dist/jakarta.annotation-api-1.3.5.jar
 10307966  10-08-2019 06:36   fhir-validation-dist/fhir-registry-4.0.0-SNAPSHOT.jar
   214788  09-06-2019 15:58   fhir-validation-dist/commons-io-2.6.jar
---------                     -------
 52769970                     13 files
 ```

<hr>
FHIR® is the registered trademark of HL7 and is used with the permission of HL7.