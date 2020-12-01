# Removing a tenant from the database

Use this guide when removing a tenant from the Db2 multi-tenant database. The tenant's data will be removed from the database along with any associated storage.

To obtain a list of the tenants currently managed in a given database, run the following:

```sh
    java -jar schema/fhir-persistence-schema-*-cli.jar \
      --prop-file db2.properties --list-tenants
```

```
 TenantId     Status       TenantName Schema
        1  ALLOCATED            test1 FHIRDATA
        2  ALLOCATED            test2 FHIRDATA2
        3    DROPPED            test3 <not-known>
```

Note that after the tenant is dropped, it is no longer associated with a schema.

## Background

Tenant data is stored in a Db2 table partition attached to each of the IBM FHIR Server data tables. Removal occurs in two phases:

- **Phase 1**. The tenant's partitions are detached from each data table and converted into a stand-alone table.
- **Phase 2**. The stand-alone tables are dropped. Once all the tables are dropped, the tablespace will also be dropped.

In Db2, the phase 1 process is asynchronous, so phase 2 can not be completed until all the detach operations are completed, which happens in the background after the detach unit of work (UOW) is committed.

If phase 2 is executed before the Db2 async process is complete, not all the tables will be dropped, which prevents the tablespace from being dropped. Therefore it might be necessary to run the phase 2 command more than once if an error is reported when the tablespace drop command is executed.

## Availability and Data Integrity

Read the Db2 documentation on [partition detach](https://www.ibm.com/support/knowledgecenter/SSEPGG_11.5.0/com.ibm.db2.luw.admin.partition.doc/doc/t0021576.html).

Note the workaround for detaching partitions which have foreign key referential integrity constraints:

> DETACH PARTITION is not allowed on a table that is the parent of an enforced referential integrity (RI) relationship. If you have tables with an enforced RI relationship and want to detach a data partition from the parent table, a workaround is available. In the following example, all statements are run within the same unit of work (UOW) to lock out concurrent updates:

```
// Change the RI constraint to informational:
ALTER TABLE child ALTER FOREIGN KEY fk NOT ENFORCED;

ALTER TABLE parent DETACH PARTITION p0 INTO TABLE pdet;

SET INTEGRITY FOR child OFF;

// Change the RI constraint back to enforced:
ALTER TABLE child ALTER FOREIGN KEY fk ENFORCED;

SET INTEGRITY FOR child ALL IMMEDIATE UNCHECKED;
// Assuming that the CHILD table does not have any dependencies on partition P0,
//   and that no updates on the CHILD table are permitted
//   until this UOW is complete,
//   no RI violation is possible during this UOW.

COMMIT WORK;
```

This process is executed as part of the phase 1 command. The process disables all the referential integrity checks on the data tables, for all tenants sharing the schema. This introduces a small risk for corruption from:

* a bug in the IBM FHIR Server persistence layer
* SQL manipulation of the data outside the control of the FHIR server.

This risk is considered very small, although it is acknowledged that referential integrity constraints exist for a reason. If the risk is deemed too great, then **phase 1** should be performed during a maintenance window, when all write access to the FHIR server is stopped. Read access remains safe at all times.

If a tenant must be deprovisioned in this scenario, the tenant can be frozen immediately then **phase 1** and **phase 2** can be run later during a maintenance window. 

## Tenant Removal Guide

Firstly, freeze the tenant to prevent any further access by the FHIR server:

```sh
    java -jar schema/fhir-persistence-schema-*-cli.jar \
      --prop-file db2.properties --freeze-tenant <tenant-name>
```

Where `<tenant-name>` is the name of the tenant you wish to block from the list. Once a tenant is frozen, it can only be unfrozen using SQL commands. Unfreezing a tenant is not designed to be a supported use case.

Freezing a tenant does not drop any of the tenant's data. Follow the phase 1 and phase 2 steps below to remove the tenant's data from the database.

Next, run **phase 1** once:

```sh
    java -jar schema/fhir-persistence-schema-*-cli.jar \
      --prop-file db2.properties
      --drop-tenant <tenant-name>
```

No schema name is required if this is the first run - the schema name is deduced by querying the Db2 catalog.

The phase 1 run will attempt to drop as many of the detached partitions as it can, but will inevitably fail when attempting to drop the tablespace because some of the detached partitions will not have been processed in time by the Db2 asynchronous process.

Running phase 1 again should not be necessary but is possible, although doing so may require the --schemaName argument to be provided if it is run after all the partitions for the named tenant have been dropped. It should only be necessary to run phase 1 again if the detach operations failed.


Repeat **phase 2** until successful:

```sh
    java -jar schema/fhir-persistence-schema-*-cli.jar \
      --prop-file db2.properties
      --schema-name FHIRDATA
      --drop-detached <tenant-name>
```

**NOTE!** For phase 2, the name of the schema **must** be given. This is required because the partitions have already been detached in phase 1, so the query which deduces the schema name from the Db2 catalog is no longer effective.

If no error is reported, the tenant's data and tablespace have been removed successfully. The end of the log for the above run should look something like this:

```
2020-06-10 11:34:35.329 00000001    INFO   com.ibm.fhir.schema.app.Main Dropping tablespace for tenant 3/performance2
2020-06-10 11:34:35.505 00000001    INFO   com.ibm.fhir.schema.app.Main Processing took:  10.998 s
2020-06-10 11:34:35.505 00000001    INFO   com.ibm.fhir.schema.app.Main SCHEMA CHANGE: OK
```

If an exception is thrown at the end, it means the tablespace wasn't empty when the drop was attempted. Simply repeat phase 2 after waiting a couple of minutes for the background Db2 tasks to complete.


## Tenant Name Reuse

To reuse a tenant name the old tenant meda-data must first be removed. Run the following command after **phase 2** has completed successfully. This is only necessary if you need to reuse the tenant name, or ensure it is removed from the database for other business reasons.

```sh
    java -jar schema/fhir-persistence-schema-*-cli.jar \
      --prop-file db2.properties
      --delete-tenant-meta <tenant-name>
```

This removes tenant meta-data stored in the `FHIR_ADMIN.TENANTS` and `FHIR_ADMIN.TENANT_KEYS` tables. Only tenants in the `DROPPED` state can be removed. Attempts to remove tenants in other states will throw an error. Use the `--list-tenants` option to check the current state of each tenant.
