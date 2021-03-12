---
title: "SQL Server (Debezium)"
sortTitle: "SQL"
connectorID: kc-source-sqlserver
direction: source
support: community
type: kafkaConnect
iconInitial: S
iconGradient: 1
documentationURL: https://debezium.io/docs/connectors/sqlserver/
download:
  -  { type: 'Download', url: 'https://repo1.maven.org/maven2/io/debezium/debezium-connector-sqlserver/1.2.0.Final/debezium-connector-sqlserver-1.2.0.Final-plugin.tar.gz' }
  -  { type: 'GitHub', url: 'https://github.com/debezium/debezium/tree/master/debezium-connector-sqlserver' }
---

Debezium is an open source distributed platform for change data capture. Debezium’s SQL Server Connector can monitor the row-level changes in the schemas of a SQL Server database and record to separate Kafka topics.
