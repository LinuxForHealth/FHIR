#!/bin/bash

if [ $LOAD_UMLS = 'true' ]; then
  echo 'Loading UMLS data into TermGraph'
  java -classpath "/usr/local/lib/*" com.ibm.fhir.term.graph.loader.impl.UMLSTermGraphLoader -config "conf/janusgraph-cassandra-elasticsearch.properties"
fi

if [ $LOAD_MAP = 'true' ]; then
  echo 'Loading Snomed to ICD Map Data into TermGraph'
  java -classpath "/usr/local/lib/*" com.ibm.fhir.term.graph.loader.impl.SnoMedICD10MapTermGraphLoader -config "conf/janusgraph-cassandra-elasticsearch.properties"
fi
