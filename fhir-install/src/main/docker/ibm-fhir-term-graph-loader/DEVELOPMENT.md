---
layout: default
title:  IBM FHIR Term Graph Loader Tool - Development
date:   2021-09-28
permalink: /ibm-fhir-term-graph-loader-tool-development/
---

# Development

The document helps developers setup the development environment for the IBM FHIR Server Term Graph Loader Tool. 

1. Build the `fhir-examples` and `fhir-parent`

``` shell
export WORKSPACE=`pwd`
cd fhir-install/src/main/docker/ibm-fhir-term-graph-loader
mvn clean install -DskipTests -f ${WORKSPACE}/fhir-examples/
mvn clean install -DskipTests -f ${WORKSPACE}/fhir-parent/
```

2. Change to the `fhir-install/src/main/docker/ibm-fhir-term-graph-loader` directory

3. Prep the contents

``` shell
mkdir -p target
cp ${WORKSPACE}/term/fhir-term-graph-loader/target/fhir-term-graph-loader-*-cli.jar target/
cp ${WORKSPACE}/LICENSE target/LICENSE
```

4. Build the tool. 

``` shell
docker build --tag ibm-fhir-term-graph-loader:latest .
```

or 

``` shell
docker build --build-arg FHIR_VERSION=4.9.2 -t ibm-fhir-term-graph-loader:4.9.2 .
```

5. Run the shell. (You may have to edit the corresponding examples)

``` shell
time docker run ibmcom/ibm-fhir-term-graph-loader:latest | tee out.log
```

# Run a Shell Check 

Sanity check of the Shell Script

```
brew install shellcheck
shellcheck run.sh
```