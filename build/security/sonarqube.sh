#!/usr/bin/env bash
###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

export SONAR_RUNNER_OPTS="-Xmx3062m -XX:MaxPermSize=512m -XX:ReservedCodeCacheSize=128m"
export MAVEN_OPTS="-Xmx4096m"

docker-compose build -f resources/sonarqube/docker-compose.yml

# The parser chokes the HEAP (the parsers are approximately 2 50K files)
mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.7.0.1746:sonar \
    -Dsonar.login=admin -Dsonar.password=admin -Dsonar.host.url=http://localhost:9000
    -f ../../../fhir-parent -Dsonar.exclusions="src/java/test/**","src/main/java/com/ibm/fhir/model/parser/**"

# Must download the report.

# EOF