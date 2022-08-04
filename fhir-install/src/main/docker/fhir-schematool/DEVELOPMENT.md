# Development

The document helps developers setup the development environment for the IBM FHIR Server Schema Tool. 

1. Build the `fhir-examples` and `fhir-parent`

``` shell
export WORKSPACE=`pwd`
cd fhir-install/src/main/docker/fhir-schematool
mvn clean install -DskipTests -f ${WORKSPACE}/fhir-examples/
mvn clean install -DskipTests -f ${WORKSPACE}/fhir-parent/
```

2. Change to the `fhir-install/src/main/docker/fhir-schematool` directory

3. Prep the contents

``` shell
mkdir -p target
cp ${WORKSPACE}/fhir-persistence-schema/target/fhir-persistence-schema-*-cli.jar target/
cp ${WORKSPACE}/LICENSE target/LICENSE
```

4. Build the schema tool. 

``` shell
docker build --tag linuxforhealth/fhir-schematool:latest .
```

or 

``` shell
docker build --build-arg FHIR_VERSION=5.0.0 -t linuxforhealth/fhir-schematool:5.0.0 .
```

5. For Db2, change run. (You may have to edit the corresponding examples)

* onboard
``` shell
time docker run  --env ENV_TOOL_INPUT=`cat examples/db2/persistence-offboard-example.json |base64` linuxforhealth/fhir-schematool:latest | tee out.log
```

* offboard
``` shell
time docker run  --env ENV_TOOL_INPUT=`cat examples/db2/persistence-onboard-example.json |base64` linuxforhealth/fhir-schematool:latest | tee out.log
```

6. For Postgres, run. (You may have to edit the corresponding examples)

* onboard
``` shell
time docker run  --env ENV_TOOL_INPUT=`cat examples/postgres/persistence-offboard-example.json |base64` linuxforhealth/fhir-schematool:latest | tee out.log
```

* offboard
``` shell
time docker run  --env ENV_TOOL_INPUT=`cat examples/postgres/persistence-onboard-example.json |base64` linuxforhealth/fhir-schematool:latest | tee out.log
```

7. Confirm you see your changes work, and the Deployment works.

    * Schema is created, or deleted
    * No Error Logs (at least unexpected)
    * Output is as expected

# Run a Shell Check 

Sanity check of the Shell Script

```
brew install shellcheck
shellcheck run.sh
```