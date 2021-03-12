---
title: "PostgreSQL (Debezium)"
sortTitle: "PostgreSQL"
connectorID: kc-source-postgresql
direction: source
support: IBM
type: kafkaConnect
iconInitial: P
iconGradient: 3
documentationURL: https://debezium.io/docs/connectors/postgresql/
download:
  -  { type: 'Download', url: 'https://repo1.maven.org/maven2/io/debezium/debezium-connector-postgres/1.2.0.Final/debezium-connector-postgres-1.2.0.Final-plugin.tar.gz' }
  -  { type: 'GitHub', url: 'https://github.com/debezium/debezium/tree/master/debezium-connector-postgres' }  
---

Debezium is an open source distributed platform for change data capture. Debezium’s PostgreSQL connector can monitor the row-level changes in the schemas of a PostgreSQL database and record them in separate Kafka topics.
