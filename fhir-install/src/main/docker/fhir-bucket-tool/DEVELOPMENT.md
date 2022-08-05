# Development

The document helps developers setup the development environment for the IBM FHIR Server - Bucket Tool. 

1. Build the `fhir-examples` and `fhir-parent`

``` shell
export WORKSPACE=`pwd`
cd fhir-install/src/main/docker/fhir-bucket-tool
mvn clean install -DskipTests -f ${WORKSPACE}/fhir-examples/
mvn clean install -DskipTests -f ${WORKSPACE}/fhir-parent/
```

2. Change to the `fhir-install/src/main/docker/fhir-bucket-tool` directory

3. Prep the contents

``` shell
mkdir -p target
cp ${WORKSPACE}/fhir-bucket/target/fhir-bucket-*-cli.jar target/
cp ${WORKSPACE}/LICENSE target/LICENSE
```

4. Build the bucket tool. 

``` shell
docker build --build-arg FHIR_VERSION=5.0.0 -t linuxforhealth/fhir-bucket-tool:5.0.0 .
```

5. Run

``` shell
time docker run linuxforhealth/fhir-bucket-tool:latest $ARGS | tee out.log
```

# Run a Shell Check 

Sanity check of the Shell Script

```
brew install shellcheck
shellcheck run.sh
```