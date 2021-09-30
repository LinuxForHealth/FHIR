#!/usr/bin/env bash

# ----------------------------------------------------------------------------
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
# ----------------------------------------------------------------------------

# This script is experimental and is provided as a point-in-time reference for downloading the spark/parquet dependencies.

JARS=(aws-java-sdk-core-1.11.596.jar
aws-java-sdk-s3-1.11.596.jar
chill_2.12-0.9.5.jar
commons-collections-3.2.2.jar
commons-compiler-3.0.16.jar
commons-configuration2-2.1.1.jar
guava-14.0.1.jar
hadoop-annotations-3.2.0.jar
hadoop-auth-3.2.0.jar
hadoop-common-3.2.0.jar
hadoop-hdfs-client-3.2.0.jar
hadoop-mapreduce-client-core-3.2.0.jar
hive-exec-2.3.7-core.jar
htrace-core4-4.1.0-incubating.jar
jackson-core-asl-1.9.13.jar
jackson-mapper-asl-1.9.13.jar
janino-3.0.16.jar
jcl-over-slf4j-1.7.30.jar
jersey-container-servlet-2.30.1.jar
json4s-ast_2.12-3.7.0-M5.jar
json4s-core_2.12-3.7.0-M5.jar
json4s-jackson_2.12-3.7.0-M5.jar
jul-to-slf4j-1.7.30.jar
kryo-shaded-4.0.2.jar
log4j-1.2.17.jar
metrics-core-4.1.1.jar
metrics-json-4.1.1.jar
netty-all-4.1.51.Final.jar
parquet-column-1.10.1.jar
parquet-common-1.10.1.jar
parquet-encoding-1.10.1.jar
parquet-format-2.4.0.jar
parquet-hadoop-1.10.1.jar
scala-reflect-2.12.10.jar
scala-xml_2.12-1.2.0.jar
slf4j-nop-1.7.32.jar
spark-catalyst_2.12-3.1.2.jar
spark-core_2.12-3.1.2.jar
spark-hive_2.12-3.1.2.jar
spark-kvstore_2.12-3.1.2.jar
spark-launcher_2.12-3.1.2.jar
spark-mllib_2.12-3.1.2.jar
spark-network-common_2.12-3.1.2.jar
spark-network-shuffle_2.12-3.1.2.jar
spark-sql_2.12-3.1.2.jar
spark-tags_2.12-3.1.2.jar
spark-unsafe_2.12-3.1.2.jar
stocator-1.1.3.jar
stream-2.9.6.jar
xbean-asm7-shaded-4.15.jar)

# Set the workspace
if [ -z "${WORKSPACE}" ]
then 
    echo "WORKSPACE NOT SET - e.g. 'export WORKSPACE=~/git/wffh/2021/FHIR'"
fi

# Cleanup the cache repository.
if [ -d cache-repo/ ]
then
    rm -rf cache-repo/
fi

# Recreate the cache-repo
mkdir -p cache-repo/

# Cleanup the deps.
if [ -d deps/ ]
then
    rm -rf deps/
fi

# Recreate the dependencies folder
mkdir -p deps/

# Get STOCATOR
curl -o cache-repo/stocator-1.1.3.jar -L https://repo1.maven.org/maven2/com/ibm/stocator/stocator/1.1.3/stocator-1.1.3.jar

# Get SLF47 (notice NOP)
curl -o cache-repo/slf4j-nop-1.7.32.jar -L https://repo1.maven.org/maven2/org/slf4j/slf4j-nop/1.7.32/slf4j-nop-1.7.32.jar

# Get Guava
curl -o cache-repo/guava-14.0.1.jar -L https://repo1.maven.org/maven2/com/google/guava/guava/14.0.1/guava-14.0.1.jar

# Get the AWS dependencies
mvn dependency:copy-dependencies -DincludeScope=provided -DoutputDirectory=$(pwd)/cache-repo/ -f ${WORKSPACE}/fhir-bulkdata-webapp

# Get the Spark Dependencies
curl -o cache-repo/hive-exec-2.3.7-core.jar -L https://repo1.maven.org/maven2/org/apache/hive/hive-exec/2.3.7/hive-exec-2.3.7-core.jar
curl -o cache-repo/jersey-container-servlet-2.30.1.jar -L https://repo1.maven.org/maven2/org/glassfish/jersey/containers/jersey-container-servlet/2.30.1/jersey-container-servlet-2.30.1.jar
curl -L -o cache-repo/netty-all-4.1.51.Final.jar https://repo1.maven.org/maven2/io/netty/netty-all/4.1.51.Final/netty-all-4.1.51.Final.jar
curl -L -o cache-repo/spark-catalyst_2.12-3.1.2.jar https://repo1.maven.org/maven2/org/apache/spark/spark-catalyst_2.12/3.1.2/spark-catalyst_2.12-3.1.2.jar
curl -L -o cache-repo/spark-core_2.12-3.1.2.jar https://repo1.maven.org/maven2/org/apache/spark/spark-core_2.12/3.1.2/spark-core_2.12-3.1.2.jar
curl -L -o cache-repo/spark-hive_2.12-3.1.2.jar https://repo1.maven.org/maven2/org/apache/spark/spark-hive_2.12/3.1.2/spark-hive_2.12-3.1.2.jar
curl -L -o cache-repo/spark-kvstore_2.12-3.1.2.jar https://repo1.maven.org/maven2/org/apache/spark/spark-kvstore_2.12/3.1.2/spark-kvstore_2.12-3.1.2.jar
curl -L -o cache-repo/spark-launcher_2.12-3.1.2.jar https://repo1.maven.org/maven2/org/apache/spark/spark-launcher_2.12/3.1.2/spark-launcher_2.12-3.1.2.jar
curl -L -o cache-repo/spark-mllib_2.12-3.1.2.jar https://repo1.maven.org/maven2/org/apache/spark/spark-mllib_2.12/3.1.2/spark-mllib_2.12-3.1.2.jar
curl -L -o cache-repo/spark-network-common_2.12-3.1.2.jar https://repo1.maven.org/maven2/org/apache/spark/spark-network-common_2.12/3.1.2/spark-network-common_2.12-3.1.2.jar
curl -L -o cache-repo/spark-network-shuffle_2.12-3.1.2.jar https://repo1.maven.org/maven2/org/apache/spark/spark-network-shuffle_2.12/3.1.2/spark-network-shuffle_2.12-3.1.2.jar
curl -L -o cache-repo/spark-sql_2.12-3.1.2.jar https://repo1.maven.org/maven2/org/apache/spark/spark-sql_2.12/3.1.2/spark-sql_2.12-3.1.2.jar
curl -L -o cache-repo/spark-tags_2.12-3.1.2.jar https://repo1.maven.org/maven2/org/apache/spark/spark-tags_2.12/3.1.2/spark-tags_2.12-3.1.2.jar
curl -L -o cache-repo/spark-unsafe_2.12-3.1.2.jar https://repo1.maven.org/maven2/org/apache/spark/spark-unsafe_2.12/3.1.2/spark-unsafe_2.12-3.1.2.jar

for JAR in ${JARS[@]}
do 
    if [ ! -f cache-repo/${JAR} ]
    then
        echo "NOT_FOUND: ${JAR}"
    else
        echo "FOUND: ${JAR}"
        cp cache-repo/${JAR} deps/
    fi
done

echo "All Done Grabbing the dependencies for parquet... move these files to userlib/"
