# Datasources used for server integration tests

```
datasource-db2.xml
datasource-derby.xml
datasource-postgresql.xml
```

Just one of these datasource definition files should be copied into the target Liberty configDropins/overrides folder. If more than one of these is present at the same time it will break the Liberty configuration because the datasource ids and JNDI location are common among the files.
