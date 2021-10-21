#!/usr/bin/env bash

set -eu -o pipefail

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# Cache all dependencies and plugins used in build to avoid downstream timeouts during
# content fetches

mvn clean install -f fhir-tools -DskipTests
mvn clean install -f fhir-examples -DskipTests
mvn -T2C org.apache.maven.plugins:maven-dependency-plugin:3.1.2:go-offline -f fhir-examples -DexcludeReactor=true
mvn -T2C org.apache.maven.plugins:maven-dependency-plugin:3.1.2:resolve-plugins -f fhir-examples -DexcludeReactor=true

mvn -T2C org.apache.maven.plugins:maven-dependency-plugin:3.1.2:go-offline -f fhir-tools -DexcludeReactor=true
mvn -T2C org.apache.maven.plugins:maven-dependency-plugin:3.1.2:resolve-plugins -f fhir-tools -DexcludeReactor=true

mvn clean package install -ntp -B -N -f fhir-parent
mvn -T2C org.apache.maven.plugins:maven-dependency-plugin:3.1.2:go-offline -f fhir-parent -DexcludeReactor=true
mvn -T2C org.apache.maven.plugins:maven-dependency-plugin:3.1.2:resolve-plugins -f fhir-parent -DexcludeReactor=true

# EOF
