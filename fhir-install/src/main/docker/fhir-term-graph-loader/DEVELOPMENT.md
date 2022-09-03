# Development

The document helps developers setup the development environment for the LinuxForHealth FHIR Server - Term Graph Loader Tool. 

1. Build the `fhir-examples` and `fhir-parent`

``` shell
export WORKSPACE=`pwd`
cd fhir-install/src/main/docker/fhir-term-graph-loader
mvn clean install -DskipTests -f ${WORKSPACE}/fhir-examples/
mvn clean install -DskipTests -f ${WORKSPACE}/fhir-parent/
```

2. Change to the `fhir-install/src/main/docker/fhir-term-graph-loader` directory

3. Prep the contents

``` shell
mkdir -p target
cp ${WORKSPACE}/term/fhir-term-graph-loader/target/fhir-term-graph-loader-*-cli.jar target/
cp ${WORKSPACE}/LICENSE target/LICENSE
```

4. Build the tool. 

``` shell
docker build --tag linuxforhealth/fhir-term-loader:latest .
```

or 

``` shell
docker build --build-arg FHIR_VERSION=5.0.0 -t linuxforhealth/fhir-term-loader:5.0.0 .
```

5. Run the shell. (You may have to edit the corresponding examples)

``` shell
time docker run --rm -e LOAD_UMLS=true linuxforhealth/fhir-term-loader:latest| tee out.log
```

``` shell
time docker run --rm -e LOAD_MAP=true linuxforhealth/fhir-term-loader:latest| tee out.log
```

You'll also have to mount a config properties and pass in a configuration environment variable.

# Run a Shell Check 

Sanity check of the Shell Script

```
brew install shellcheck
shellcheck run.sh
```