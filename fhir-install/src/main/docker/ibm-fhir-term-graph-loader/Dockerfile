# ----------------------------------------------------------------------------
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
# ----------------------------------------------------------------------------

# Build stage
FROM maven:3.6.0-jdk-11-slim AS build
COPY . /

RUN mvn -B org.apache.maven.plugins:maven-dependency-plugin:3.1.2:go-offline -f /fhir-examples -DexcludeReactor=true -Dmaven.wagon.http.retryHandler.count=3
RUN mvn -B org.apache.maven.plugins:maven-dependency-plugin:3.1.2:resolve-plugins -f /fhir-examples -DexcludeReactor=true -Dmaven.wagon.http.retryHandler.count=3
RUN mvn -f /fhir-examples/pom.xml install -DskipTests=true

RUN mvn -B org.apache.maven.plugins:maven-dependency-plugin:3.1.2:go-offline -f /fhir-parent -DexcludeReactor=true -Dmaven.wagon.http.retryHandler.count=3
RUN mvn -B org.apache.maven.plugins:maven-dependency-plugin:3.1.2:resolve-plugins -f /fhir-parent -DexcludeReactor=true -Dmaven.wagon.http.retryHandler.count=3
RUN mvn -f /fhir-parent/pom.xml install -DskipTests=true -pl ../fhir-term-graph -am

RUN mvn org.apache.maven.plugins:maven-dependency-plugin:3.2.0:copy-dependencies -f /fhir-parent -pl ../fhir-term-graph -am -DoutputDirectory=target/ftg

# Package stage
FROM openjdk:11-jre-slim

COPY --from=build run.sh /opt/
COPY --from=build /fhir-term-graph/target/*.jar /opt/lib/
COPY --from=build /fhir-term-graph/target/ftg/*.jar /opt/lib/

ENTRYPOINT ["/opt/run.sh"]