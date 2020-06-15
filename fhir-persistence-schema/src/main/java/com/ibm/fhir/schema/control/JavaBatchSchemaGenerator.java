/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.control;

import static com.ibm.fhir.schema.control.FhirSchemaConstants.PK;

import java.util.Arrays;
import java.util.List;

import com.ibm.fhir.database.utils.model.Generated;
import com.ibm.fhir.database.utils.model.GroupPrivilege;
import com.ibm.fhir.database.utils.model.PhysicalDataModel;
import com.ibm.fhir.database.utils.model.Privilege;
import com.ibm.fhir.database.utils.model.Table;

/**
 * Encapsulates the generation of the Liberty Java Batch schema artifacts.
 * This sql is generated using the Open Liberty tool ddlGen
 * <code>wlp/bin/ddlGen generate fhir-server</code>
 */
public class JavaBatchSchemaGenerator {
    public static final String BATCH_SCHEMANAME = "FHIR_JBATCH";
    public static final Boolean NOT_NULL = Boolean.FALSE;
    public static final Boolean NULL = Boolean.TRUE;

    public static final String JOBPARAMETER_TABLE = "JOBPARAMETER";
    public static final String NAME = "NAME";
    public static final String VALUE = "VALUE";
    public static final String FK_JOBEXECID = "FK_JOBEXECID";
    public static final String JP_FKJOBEXECID_IX = "JP_FKJOBEXECID_IX";

    public static final String JOBEXECUTION_TABLE = "JOBEXECUTION";
    public static final String JOBEXECID = "JOBEXECID";
    public static final String ENDTIME = "ENDTIME";
    public static final String EXECNUM = "EXECNUM";
    public static final String JOBPARAMETERS = "JOBPARAMETERS";
    public static final String LOGPATH = "LOGPATH";
    public static final String RESTURL = "RESTURL";
    public static final String SERVERID = "SERVERID";
    public static final String STARTTIME = "STARTTIME";

    public static final String GROUPASSOCIATION_TABLE = "GROUPASSOCIATION";
    public static final String FK_JOBINSTANCEID = "FK_JOBINSTANCEID";
    public static final String GROUPNAMES = "GROUPNAMES";

    public static final String JOBINSTANCE_TABLE = "JOBINSTANCE";
    public static final String JOBINSTANCEID = "JOBINSTANCEID";
    public static final String AMCNAME = "AMCNAME";
    public static final String BATCHSTATUS = "BATCHSTATUS";
    public static final String CREATETIME = "CREATETIME";
    public static final String EXITSTATUS = "EXITSTATUS";
    public static final String INSTANCESTATE = "INSTANCESTATE";
    public static final String JOBNAME = "JOBNAME";
    public static final String JOBXMLNAME = "JOBXMLNAME";
    public static final String JOBXML = "JOBXML";
    public static final String NUMEXECS = "NUMEXECS";
    public static final String RESTARTON = "RESTARTON";
    public static final String SUBMITTER = "SUBMITTER";
    public static final String UPDATETIME = "UPDATETIME";

    public static final String STEPTHREADINSTANCE_TABLE = "STEPTHREADINSTANCE";
    public static final String PARTNUM = "PARTNUM";
    public static final String STEPNAME = "STEPNAME";
    public static final String THREADTYPE = "THREADTYPE";
    public static final String CHECKPOINTDATA = "CHECKPOINTDATA";
    public static final String PARTITIONED = "PARTITIONED";
    public static final String PARTITIONPLANSIZE = "PARTITIONPLANSIZE";
    public static final String STARTCOUNT  = "STARTCOUNT";
    public static final String FK_LATEST_STEPEXECID = "FK_LATEST_STEPEXECID";

    public static final String STEPTHREADEXECUTION_TABLE = "STEPTHREADEXECUTION";
    public static final String STEPEXECID = "STEPEXECID";

    public static final String REMOTABLEPARTITION_TABLE = "REMOTABLEPARTITION";
    public static final String INTERNALSTATE = "INTERNALSTATE";
    public static final String LASTUPDATED = "LASTUPDATED";
    public static final String FK_JOBEXECUTIONID = "FK_JOBEXECUTIONID";
    public static final String FK_STEPEXECUTIONID = "FK_STEPEXECUTIONID";

    public static final String M_COMMIT = "M_COMMIT";
    public static final String M_FILTER = "M_FILTER";
    public static final String INTERNALSTATUS = "INTERNALSTATUS";
    public static final String USERDATA = "USERDATA";
    public static final String M_PROCESSSKIP = "M_PROCESSSKIP";
    public static final String M_READ = "M_READ";
    public static final String M_READSKIP = "M_READSKIP";
    public static final String M_ROLLBACK = "M_ROLLBACK";
    public static final String M_WRITE = "M_WRITE";
    public static final String M_WRITESKIP = "M_WRITESKIP";
    public static final String ISPARTITIONEDSTEP = "ISPARTITIONEDSTEP";
    public static final String FK_TOPLVL_STEPEXECID = "FK_TOPLVL_STEPEXECID";

    // The schema holding all the Java Batch data
    private final String schemaName;

    /**
     * Configures the destination schema for JavaBatch
     * @param schemaName
     */
    public JavaBatchSchemaGenerator(String schemaName) {
        this.schemaName = schemaName;
    }

    /**
     * Create the tables needed by the Liberty JBatch databaseStore
     * @param model
     */
    public void buildJavaBatchSchema(PhysicalDataModel model) {
        addJobInstanceTable(model);
        addJobExecutionTable(model);
        addStepThreadExecutionTable(model);
        addStepThreadInstanceTable(model);
        addRemotablePartitionTable(model);
        addGroupAssociationTable(model);
        addJobParameterTable(model);
    }

    protected List<GroupPrivilege> generateGroupPrivilege(){
        return Arrays.asList(
            new GroupPrivilege(FhirSchemaConstants.FHIR_BATCH_GRANT_GROUP, Privilege.SELECT),
            new GroupPrivilege(FhirSchemaConstants.FHIR_BATCH_GRANT_GROUP, Privilege.INSERT),
            new GroupPrivilege(FhirSchemaConstants.FHIR_BATCH_GRANT_GROUP, Privilege.DELETE),
            new GroupPrivilege(FhirSchemaConstants.FHIR_BATCH_GRANT_GROUP, Privilege.UPDATE));
    }

    /**
     * Adds the job table with the following values:
     <code>
     CREATE TABLE FHIR_BATCH.JOBPARAMETER (
         NAME VARCHAR(255), 
         VALUE VARCHAR(255),
         FK_JOBEXECID BIGINT)
     CREATE INDEX FHIR_BATCH.JP_FKJOBEXECID_IX ON FHIR_BATCH.JOBPARAMETER (FK_JOBEXECID)
     ALTER TABLE FHIR_BATCH.JOBPARAMETER ADD CONSTRAINT JBPRMETERFKJBXECID FOREIGN KEY (FK_JOBEXECID)
         REFERENCES FHIR_BATCH.JOBEXECUTION (JOBEXECID)
     </code>
     * @param model
     */
    public void addJobParameterTable(PhysicalDataModel model) {
        // The default for the value is 255, and chosen to update to 4096
        Table parameter = Table.builder(schemaName, JOBPARAMETER_TABLE)
                .addVarcharColumn(NAME, 255, NULL) // NAME VARCHAR(255)
                .addVarcharColumn(VALUE, 4096, NULL) // VALUE VARCHAR(255) (updated to 4096)
                .addBigIntColumn(FK_JOBEXECID, NOT_NULL) // FK_JOBEXECID BIGINT
                .addForeignKeyConstraintAltTarget("JBPRMETERFKJBXECID", schemaName, JOBEXECUTION_TABLE, JOBEXECID, FK_JOBEXECID)
                .addIndex("JP_FKJOBEXECID_IX", FK_JOBEXECID)
                .addPrivileges(generateGroupPrivilege())
                .build(model);
        model.addTable(parameter);
        model.addObject(parameter);
    }

    /**
     * Adds the group association table with the following values:
     <code>
     CREATE TABLE FHIR_BATCH.GROUPASSOCIATION (
         FK_JOBINSTANCEID BIGINT, 
         GROUPNAMES VARCHAR(255))
     CREATE INDEX FHIR_BATCH.GA_FKINSTANCEID_IX ON FHIR_BATCH.GROUPASSOCIATION (FK_JOBINSTANCEID)
     ALTER TABLE FHIR_BATCH.GROUPASSOCIATION ADD CONSTRAINT GRPSSCTNFKJBNSTNCD FOREIGN KEY (FK_JOBINSTANCEID)
         REFERENCES FHIR_BATCH.JOBINSTANCE (JOBINSTANCEID)
     </code>
     * @param model
     */
    public void addGroupAssociationTable(PhysicalDataModel model) {
        Table parameter = Table.builder(schemaName, GROUPASSOCIATION_TABLE)
                .addBigIntColumn(FK_JOBINSTANCEID, NULL) // FK_JOBINSTANCEID BIGINT
                .addVarcharColumn( GROUPNAMES, 255, NULL) // GROUPNAMES VARCHAR(255)
                .addIndex("GA_FKINSTANCEID_IX", FK_JOBINSTANCEID)
                .addForeignKeyConstraintAltTarget("GRPSSCTNFKJBNSTNCD", schemaName, JOBINSTANCE_TABLE, JOBINSTANCEID, FK_JOBINSTANCEID)
                .addPrivileges(generateGroupPrivilege())
                .build(model);
        model.addTable(parameter);
        model.addObject(parameter);
    }

    /**
     * Adds the Step Thread Execution table with the following values:
     <code>
     CREATE TABLE FHIR_BATCH.STEPTHREADEXECUTION(
         STEPEXECID BIGINT GENERATED BY DEFAULT AS IDENTITY,
         THREADTYPE VARCHAR(31 OCTETS),
         BATCHSTATUS INT NOT NULL,
         M_COMMIT BIGINT NOT NULL,
         ENDTIME TIMESTAMP,
         EXITSTATUS VARCHAR(512 OCTETS),
         M_FILTER BIGINT NOT NULL,
         INTERNAL_STATUS INT NOT NULL,
         PARTNUM INT NOT NULL,
         USERDATA BLOB(2147483647)
         INLINE LENGTH 10240 NOT NULL,
         M_PROCESSSKIP BIGINT NOT NULL,
         M_READ BIGINT NOT NULL,
         M_READSKIP BIGINT NOT NULL,
         M_ROLLBACK BIGINT NOT NULL,
         STARTTIME TIMESTAMP,
         STEPNAME VARCHAR(128 OCTETS) NOT NULL,
         M_WRITE BIGINT NOT NULL,
         M_WRITESKIP BIGINT NOT NULL,
         FK_JOBEXECID BIGINT NOT NULL,
         FK_TOPLVL_STEPEXECID BIGINT NOT NULL,
         ISPARTITIONEDSTEP SMALLINT,
         CONSTRAINT PK_STEPTHREADEXECUTION PRIMARY KEY (STEPEXECID))
     CREATE INDEX FHIR_BATCH.STE_FKJOBEXECID_IX ON FHIR_BATCH.STEPTHREADEXECUTION (FK_JOBEXECID)
     CREATE INDEX FHIR_BATCH.STE_FKTLSTEPEID_IX ON FHIR_BATCH.STEPTHREADEXECUTION (FK_TOPLVL_STEPEXECID)
     ALTER TABLE FHIR_BATCH.STEPTHREADEXECUTION ADD CONSTRAINT STPTHRADEXECUTION0 UNIQUE (FK_JOBEXECID, STEPNAME, PARTNUM)
     ALTER TABLE FHIR_BATCH.STEPTHREADEXECUTION ADD CONSTRAINT STPTHFKTPLVLSTPXCD FOREIGN KEY (FK_TOPLVL_STEPEXECID) 
         REFERENCES FHIR_BATCH.STEPTHREADEXECUTION (STEPEXECID)
     ALTER TABLE FHIR_BATCH.STEPTHREADEXECUTION ADD CONSTRAINT STPTHRDXCTNFKJBXCD FOREIGN KEY (FK_JOBEXECID)
         REFERENCES FHIR_BATCH.JOBEXECUTION (JOBEXECID)
     </code>
     * @param model
     */
    public void addStepThreadExecutionTable(PhysicalDataModel model) {
        Table stepThreadExecutionTable = Table.builder(schemaName, STEPTHREADEXECUTION_TABLE)
                .addBigIntColumn(STEPEXECID, NOT_NULL) // STEPEXECID BIGINT
                .setIdentityColumn(STEPEXECID, Generated.BY_DEFAULT) // GENERATED BY DEFAULT AS IDENTITY NOT NULL
                .addVarcharColumn(THREADTYPE, 31, NULL) // THREADTYPE VARCHAR(31)
                .addIntColumn(BATCHSTATUS, NOT_NULL) // BATCHSTATUS INTEGER NOT NULL
                .addBigIntColumn(M_COMMIT, NOT_NULL) // M_COMMIT BIGINT NOT NULL
                .addTimestampColumn(ENDTIME, NULL) // ENDTIME TIMESTAMP
                .addVarcharColumn(EXITSTATUS, 512, NULL) // EXITSTATUS VARCHAR(512)
                .addBigIntColumn(M_FILTER, NOT_NULL) // M_FILTER BIGINT NOT NULL
                .addIntColumn(INTERNALSTATUS, NOT_NULL) // INTERNALSTATUS INTEGER NOT NULL
                .addIntColumn(PARTNUM, NOT_NULL) // PARTNUM INTEGER NOT NULL
                .addBlobColumn(USERDATA, 2147483647, 10240, NULL) // USERDATA BLOB(2147483647)
                .addBigIntColumn(M_PROCESSSKIP, NOT_NULL) // M_PROCESSSKIP BIGINT NOT NULL
                .addBigIntColumn(M_READ, NOT_NULL) // M_READ BIGINT NOT NULL
                .addBigIntColumn(M_READSKIP, NOT_NULL) // M_READSKIP BIGINT NOT NULL
                .addBigIntColumn(M_ROLLBACK, NOT_NULL) // M_ROLLBACK BIGINT NOT NULL
                .addTimestampColumn(STARTTIME, NULL) // STARTTIME TIMESTAMP
                .addVarcharColumn(STEPNAME, 128, NOT_NULL) // STEPNAME VARCHAR(128) NOT NULL
                .addBigIntColumn(M_WRITE, NOT_NULL) // M_WRITE BIGINT NOT NULL
                .addBigIntColumn(M_WRITESKIP, NOT_NULL) // M_WRITESKIP BIGINT NOT NULL
                .addBigIntColumn(FK_JOBEXECID, NOT_NULL) // FK_JOBEXECID BIGINT NOT NULL
                .addBigIntColumn(FK_TOPLVL_STEPEXECID, NULL) // FK_TOPLVL_STEPEXECID BIGINT
                .addSmallIntColumn(ISPARTITIONEDSTEP, 0, NULL) // ISPARTITIONEDSTEP SMALLINT DEFAULT 0
                .addPrimaryKey(PK + STEPTHREADEXECUTION_TABLE, STEPEXECID) // PRIMARY KEY (STEPEXECID)
                .addIndex("STE_FKJOBEXECID_IX", FK_JOBEXECID) // STE_FKJOBEXECID_IX (FK_JOBEXECID)
                .addIndex("STE_FKTLSTEPEID_IX", FK_TOPLVL_STEPEXECID) // STE_FKTLSTEPEID_IX (FK_TOPLVL_STEPEXECID)
                .addUniqueConstraint("STPTHRADEXECUTION0", FK_JOBEXECID, STEPNAME, PARTNUM) // STPTHRADEXECUTION0 UNIQUE (FK_JOBEXECID, STEPNAME, PARTNUM)
                .addForeignKeyConstraintSelf("STPTHFKTPLVLSTPXCD", schemaName, STEPTHREADEXECUTION_TABLE, STEPEXECID, FK_TOPLVL_STEPEXECID)
                    // STPTHFKTPLVLSTPXCD FOREIGN KEY (FK_TOPLVL_STEPEXECID) -> REFERENCES FHIR_BATCH.STEPTHREADEXECUTION (STEPEXECID)
                .addForeignKeyConstraintAltTarget("STPTHRDXCTNFKJBXCD", schemaName, JOBEXECUTION_TABLE, JOBEXECID, FK_JOBEXECID)
                    // STPTHRDXCTNFKJBXCD FOREIGN KEY (FK_JOBEXECID) (FK_JOBEXECID)
                .addPrivileges(generateGroupPrivilege())
                .build(model);
        model.addTable(stepThreadExecutionTable);
        model.addObject(stepThreadExecutionTable);
    }

    /**
     * Adds the Remotable Partition table with the following values:
     <code>
     CREATE TABLE FHIR_BATCH.REMOTABLEPARTITION (
         PARTNUM INTEGER NOT NULL,
         STEPNAME VARCHAR(255) NOT NULL,
         INTERNALSTATE INTEGER,
         LASTUPDATED TIMESTAMP,
         LOGPATH VARCHAR(512),
         RESTURL VARCHAR(512),
         SERVERID VARCHAR(256),
         FK_JOBEXECUTIONID BIGINT NOT NULL,
         FK_STEPEXECUTIONID BIGINT,
         PRIMARY KEY (PARTNUM, STEPNAME, FK_JOBEXECUTIONID))
     ALTER TABLE FHIR_BATCH.REMOTABLEPARTITION ADD CONSTRAINT RMOTABLEPARTITION0 UNIQUE (FK_JOBEXECUTIONID, STEPNAME, PARTNUM)
     ALTER TABLE FHIR_BATCH.REMOTABLEPARTITION ADD CONSTRAINT RMTBLPRTFKSTPXCTND FOREIGN KEY (FK_STEPEXECUTIONID)
         REFERENCES FHIR_BATCH.STEPTHREADEXECUTION (STEPEXECID)
     ALTER TABLE FHIR_BATCH.REMOTABLEPARTITION ADD CONSTRAINT RMTBLPRTTFKJBXCTND FOREIGN KEY (FK_JOBEXECUTIONID)
         REFERENCES FHIR_BATCH.JOBEXECUTION (JOBEXECID)
     </code>
     * @param model
     */
    public void addRemotablePartitionTable(PhysicalDataModel model) {
        Table remotablePartition = Table.builder(schemaName, REMOTABLEPARTITION_TABLE)
                .addIntColumn(PARTNUM, NOT_NULL) // PARTNUM INTEGER NOT NULL
                .addVarcharColumn(STEPNAME, 255, NOT_NULL) // STEPNAME VARCHAR(255) NOT NULL
                .addIntColumn(INTERNALSTATE, NULL) // NTERNALSTATE INTEGER
                .addTimestampColumn(LASTUPDATED, NULL) // LASTUPDATED TIMESTAMP
                .addVarcharColumn(LOGPATH, 512, NULL) // LOGPATH VARCHAR(512)
                .addVarcharColumn(RESTURL, 512, NULL) // RESTURL VARCHAR(512)
                .addVarcharColumn(SERVERID, 256, NULL) // SERVERID VARCHAR(256)
                .addBigIntColumn(FK_JOBEXECUTIONID, NOT_NULL) // FK_JOBEXECUTIONID BIGINT NOT NULL
                .addBigIntColumn(FK_STEPEXECUTIONID, NOT_NULL) // FK_STEPEXECUTIONID BIGINT
                .addPrimaryKey(PK + REMOTABLEPARTITION_TABLE, PARTNUM, STEPNAME, FK_JOBEXECUTIONID)
                    // PRIMARY KEY (PARTNUM, STEPNAME, FK_JOBEXECUTIONID)
                .addForeignKeyConstraintAltTarget("RMTBLPRTFKSTPXCTND", schemaName, STEPTHREADEXECUTION_TABLE, STEPEXECID, FK_STEPEXECUTIONID)
                .addForeignKeyConstraintAltTarget("RMTBLPRTTFKJBXCTND", schemaName, JOBEXECUTION_TABLE, JOBEXECID, FK_STEPEXECUTIONID)
                .addUniqueConstraint("RMOTABLEPARTITION0", FK_JOBEXECUTIONID, STEPNAME, PARTNUM)
                .addPrivileges(generateGroupPrivilege())
                .build(model);
        model.addTable(remotablePartition);
        model.addObject(remotablePartition);
    }

    /**
     * Adds the Job Execution table with the following values:
     <code>
     CREATE TABLE FHIR_BATCH.JOBEXECUTION(
         JOBEXECID BIGINT GENERATED BY DEFAULT AS IDENTITY,
         BATCHSTATUS INT NOT NULL,
         CREATETIME TIMESTAMP NOT NULL,
         ENDTIME TIMESTAMP,
         EXECNUM INT NOT NULL,
         EXITSTATUS VARCHAR(2048 OCTETS) NOT NULL,
         JOBPARAMETERS BLOB(2147483647) INLINE LENGTH 10240,
         UPDATETIME TIMESTAMP,
         LOGPATH VARCHAR(512 OCTETS),
         RESTURL VARCHAR(512 OCTETS),
         SERVERID VARCHAR(256 OCTETS),
         STARTTIME TIMESTAMP,
         FK_JOBINSTANCEID BIGINT NOT NULL,
         CONSTRAINT PK_JOBEXECUTION PRIMARY KEY (JOBEXECID))
     CREATE INDEX FHIR_BATCH.JE_FKINSTANCEID_IX ON FHIR_BATCH.JOBEXECUTION (FK_JOBINSTANCEID)
     ALTER TABLE FHIR_BATCH.JOBEXECUTION ADD CONSTRAINT JBXCTNFKJBNSTNCEID FOREIGN KEY (FK_JOBINSTANCEID)
         REFERENCES FHIR_BATCH.JOBINSTANCE (JOBINSTANCEID)
     </code>
     * @param model
     */
    public void addJobExecutionTable(PhysicalDataModel model) {
        Table jobInstance = Table.builder(schemaName, JOBEXECUTION_TABLE)
                .addBigIntColumn(JOBEXECID, NOT_NULL) // JOBEXECID BIGINT
                .setIdentityColumn(JOBEXECID, Generated.BY_DEFAULT) // GENERATED BY DEFAULT AS IDENTITY NOT NULL
                .addIntColumn(BATCHSTATUS, NOT_NULL) // BATCHSTATUS INTEGER NOT NULL
                .addTimestampColumn(CREATETIME, NOT_NULL) // CREATETIME TIMESTAMP NOT NULL
                .addTimestampColumn(ENDTIME, NULL) // ENDTIME TIMESTAMP
                .addIntColumn(EXECNUM, NOT_NULL) // EXECNUM INTEGER NOT NULL
                .addVarcharColumn(EXITSTATUS, 2048, NULL) // EXITSTATUS VARCHAR(512) - Default was 512, we upped to 2048
                .addBlobColumn(JOBPARAMETERS, 2147483647, 10240, NULL) // JOBPARAMETERS BLOB(2147483647)
                .addTimestampColumn(UPDATETIME, NULL) // UPDATETIME TIMESTAMP
                .addVarcharColumn(LOGPATH, 512, NULL) // LOGPATH VARCHAR(512)
                .addVarcharColumn(RESTURL, 512, NULL) // RESTURL VARCHAR(512)
                .addVarcharColumn(SERVERID, 256, NULL) // SERVERID VARCHAR(256)
                .addTimestampColumn(STARTTIME, NULL) // STARTTIME TIMESTAMP
                .addBigIntColumn(FK_JOBINSTANCEID, NOT_NULL) // FK_JOBINSTANCEID BIGINT NOT NULL
                .addPrimaryKey(PK + JOBEXECUTION_TABLE, JOBEXECID) // PRIMARY KEY (JOBEXECID)
                .addIndex("JE_FKINSTANCEID_IX", FK_JOBINSTANCEID) // JE_FKINSTANCEID_IX (FK_JOBINSTANCEID)
                .addForeignKeyConstraintAltTarget("JBXCTNFKJBNSTNCEID", schemaName, JOBINSTANCE_TABLE, JOBINSTANCEID, FK_JOBINSTANCEID)
                    // JBXCTNFKJBNSTNCEID (FK_JOBINSTANCEID)
                .addPrivileges(generateGroupPrivilege())
                .build(model);
        model.addTable(jobInstance);
        model.addObject(jobInstance);
    }

    /**
     * Adds the Step Thread Instance table with the following values:
     <code>
     CREATE TABLE FHIR_BATCH.STEPTHREADINSTANCE(
         PARTNUM BIGINT NOT NULL,
         STEPNAME VARCHAR(128 OCTETS) NOT NULL,
         THREADTYPE VARCHAR(31 OCTETS),
         CHECKPOINTDATA BLOB(2147483647) INLINE LENGTH 10240,
         FK_JOBINSTANCEID BIGINT NOT NULL,
         FK_LATEST_STEPEXECID BIGINT NOT NULL,
         PARTITIONED SMALLINT NOT NULL,
         PARTITIONPLANSIZE INT,
         STARTCOUNT INT,
         CONSTRAINT PK_STEPTHREADINSTANCE PRIMARY KEY (PARTNUM, STEPNAME, FK_JOBINSTANCEID))
     CREATE INDEX FHIR_BATCH.STI_FKINSTANCEID_IX ON FHIR_BATCH.STEPTHREADINSTANCE (FK_JOBINSTANCEID)
     CREATE INDEX FHIR_BATCH.STI_FKLATEST_SEI_IX ON FHIR_BATCH.STEPTHREADINSTANCE (FK_LATEST_STEPEXECID)
     ALTER TABLE FHIR_BATCH.STEPTHREADINSTANCE ADD CONSTRAINT STPTHRFKLTSTSTPXCD FOREIGN KEY (FK_LATEST_STEPEXECID)
         REFERENCES FHIR_BATCH.STEPTHREADEXECUTION (STEPEXECID)
     ALTER TABLE FHIR_BATCH.STEPTHREADINSTANCE ADD CONSTRAINT STPTHRDNFKJBNSTNCD FOREIGN KEY (FK_JOBINSTANCEID)
         REFERENCES FHIR_BATCH.JOBINSTANCE (JOBINSTANCEID)
     </code>
     * @param model
     */
    public void addStepThreadInstanceTable(PhysicalDataModel model) {
        Table jobInstance = Table.builder(schemaName, STEPTHREADINSTANCE_TABLE)
                .addIntColumn(PARTNUM, NOT_NULL) // PARTNUM INTEGER NOT NULL
                .addVarcharColumn(STEPNAME, 128, NOT_NULL) // STEPNAME VARCHAR(128) NOT NULL
                .addVarcharColumn(THREADTYPE, 31, NULL) // THREADTYPE VARCHAR(31)
                .addBlobColumn(CHECKPOINTDATA, 2147483647, 10240, NULL) // CHECKPOINTDATA BLOB(2147483647)
                .addBigIntColumn(FK_JOBINSTANCEID, NOT_NULL) // FK_JOBINSTANCEID BIGINT NOT NULL
                .addBigIntColumn(FK_LATEST_STEPEXECID, NOT_NULL) // FK_LATEST_STEPEXECID BIGINT NOT NULL
                .addSmallIntColumn(PARTITIONED, 0, NULL) //PARTITIONED SMALLINT DEFAULT 0 NOT NULL
                .addIntColumn(PARTITIONPLANSIZE, NULL) // PARTITIONPLANSIZE INTEGER
                .addIntColumn(STARTCOUNT,NULL) // STARTCOUNT INTEGER
                .addIndex("STI_FKINSTANCEID_IX", FK_JOBINSTANCEID)
                .addIndex("STI_FKLATEST_SEI_IX", FK_LATEST_STEPEXECID)
                // STPTHRFKLTSTSTPXCD (FK_LATEST_STEPEXECID) REFERENCES FHIR_JBATCH.STEPTHREADEXECUTION (STEPEXECID)
                .addForeignKeyConstraintAltTarget("STPTHRFKLTSTSTPXCD", schemaName, STEPTHREADEXECUTION_TABLE, STEPEXECID, FK_LATEST_STEPEXECID)
                // STPTHRDNFKJBNSTNCD (FK_JOBINSTANCEID) REFERENCES FHIR_JBATCH.JOBINSTANCE (JOBINSTANCEID)
                .addForeignKeyConstraintAltTarget("STPTHRDNFKJBNSTNCD", schemaName, JOBINSTANCE_TABLE, JOBINSTANCEID, FK_JOBINSTANCEID)
                .addPrimaryKey(PK + STEPTHREADINSTANCE_TABLE, PARTNUM, STEPNAME, FK_JOBINSTANCEID)
                    // PRIMARY KEY (PARTNUM, STEPNAME, FK_JOBINSTANCEID))
                .addPrivileges(generateGroupPrivilege())
                .build(model);
        model.addTable(jobInstance);
        model.addObject(jobInstance);
    }

    /**
     * Adds the Job Instance table with the following values:
     <code>
     CREATE TABLE FHIR_BATCH.JOBINSTANCE(
         JOBINSTANCEID BIGINT GENERATED BY DEFAULT AS IDENTITY,
         AMCNAME VARCHAR(512 OCTETS),
         BATCHSTATUS INT NOT NULL,
         CREATETIME TIMESTAMP NOT NULL,
         EXITSTATUS VARCHAR(2048 OCTETS),
         INSTANCESTATE INT NOT NULL,
         JOBNAME VARCHAR(256 OCTETS),
         JOBXMLNAME VARCHAR(128 OCTETS),
         JOBXML BLOB(2147483647) INLINE LENGTH 10240,
         NUMEXECS INT NOT NULL, RESTARTON VARCHAR(128 OCTETS),
         SUBMITTER VARCHAR(256 OCTETS),
         UPDATETIME TIMESTAMP,
         CONSTRAINT PK_JOBINSTANCEID PRIMARY KEY (JOBINSTANCEID)
         )
     </code>
     * @param model
     */
    public void addJobInstanceTable(PhysicalDataModel model) {
        Table jobInstance = Table.builder(schemaName, JOBINSTANCE_TABLE)
                .addBigIntColumn(JOBINSTANCEID, NOT_NULL) // JOBINSTANCEID BIGINT
                .setIdentityColumn(JOBINSTANCEID, Generated.BY_DEFAULT) // GENERATED BY DEFAULT AS IDENTITY NOT NULL
                .addVarcharColumn(AMCNAME, 512, NULL) // AMCNAME VARCHAR(512)
                .addIntColumn(BATCHSTATUS, NOT_NULL) // BATCHSTATUS INTEGER NOT NULL
                .addTimestampColumn(CREATETIME, NOT_NULL) // CREATETIME TIMESTAMP NOT NULL
                // Increased the EXIT STATUS to 2048
                .addVarcharColumn(EXITSTATUS, 2048, NULL) // EXITSTATUS VARCHAR(512)
                .addIntColumn(INSTANCESTATE, NOT_NULL) // INSTANCESTATE INTEGER NOT NULL
                .addVarcharColumn(JOBNAME, 256, NULL) // JOBNAME VARCHAR(256)
                .addVarcharColumn(JOBXMLNAME, 128, NULL) // JOBXMLNAME VARCHAR(128)
                .addBlobColumn(JOBXML, 2147483647, 10240, NULL) // JOBXML BLOB(2147483647)
                .addIntColumn(NUMEXECS, NOT_NULL) // NUMEXECS INTEGER NOT NULL
                .addVarcharColumn(RESTARTON, 128, NULL) // RESTARTON VARCHAR(128)
                .addVarcharColumn(SUBMITTER, 256, NULL) // SUBMITTER VARCHAR(256)
                .addTimestampColumn(UPDATETIME, NULL) // UPDATETIME TIMESTAMP
                .addPrimaryKey(PK + JOBINSTANCEID, JOBINSTANCEID) // PRIMARY KEY (JOBINSTANCEID)
                .addPrivileges(generateGroupPrivilege())
                .build(model);
        model.addTable(jobInstance);
        model.addObject(jobInstance);
    }
}