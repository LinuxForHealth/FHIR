This readme applies to all files in this folder:

# Datasources used for server integration tests

```
datasource-derby.xml
datasource-postgresql.xml
```

These files are copied to the overrides folder and renamed to datasource.xml.

Only one of these datasource definition files should be copied into the target Liberty configDropins/overrides folder. If more than one of these is present at the same time it will break the Liberty configuration because the datasource ids and JNDI location are common among the files.
