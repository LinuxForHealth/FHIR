/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.bucket.persistence;

import static org.linuxforhealth.fhir.bucket.persistence.SchemaConstants.ALLOCATION_ID;
import static org.linuxforhealth.fhir.bucket.persistence.SchemaConstants.BUCKET_NAME;
import static org.linuxforhealth.fhir.bucket.persistence.SchemaConstants.BUCKET_PATH;
import static org.linuxforhealth.fhir.bucket.persistence.SchemaConstants.BUCKET_PATHS;
import static org.linuxforhealth.fhir.bucket.persistence.SchemaConstants.BUCKET_PATH_ID;
import static org.linuxforhealth.fhir.bucket.persistence.SchemaConstants.CREATED_TSTAMP;
import static org.linuxforhealth.fhir.bucket.persistence.SchemaConstants.ERROR_TEXT;
import static org.linuxforhealth.fhir.bucket.persistence.SchemaConstants.ERROR_TEXT_LEN;
import static org.linuxforhealth.fhir.bucket.persistence.SchemaConstants.ERROR_TSTAMP;
import static org.linuxforhealth.fhir.bucket.persistence.SchemaConstants.ETAG;
import static org.linuxforhealth.fhir.bucket.persistence.SchemaConstants.FAILURE_COUNT;
import static org.linuxforhealth.fhir.bucket.persistence.SchemaConstants.FILE_TYPE;
import static org.linuxforhealth.fhir.bucket.persistence.SchemaConstants.FK;
import static org.linuxforhealth.fhir.bucket.persistence.SchemaConstants.HEARTBEAT_TSTAMP;
import static org.linuxforhealth.fhir.bucket.persistence.SchemaConstants.HOSTNAME;
import static org.linuxforhealth.fhir.bucket.persistence.SchemaConstants.HTTP_STATUS_CODE;
import static org.linuxforhealth.fhir.bucket.persistence.SchemaConstants.HTTP_STATUS_TEXT;
import static org.linuxforhealth.fhir.bucket.persistence.SchemaConstants.HTTP_STATUS_TEXT_LEN;
import static org.linuxforhealth.fhir.bucket.persistence.SchemaConstants.IDX;
import static org.linuxforhealth.fhir.bucket.persistence.SchemaConstants.JOB_ALLOCATION_SEQ;
import static org.linuxforhealth.fhir.bucket.persistence.SchemaConstants.LAST_MODIFIED;
import static org.linuxforhealth.fhir.bucket.persistence.SchemaConstants.LINE_NUMBER;
import static org.linuxforhealth.fhir.bucket.persistence.SchemaConstants.LOADER_INSTANCES;
import static org.linuxforhealth.fhir.bucket.persistence.SchemaConstants.LOADER_INSTANCE_ID;
import static org.linuxforhealth.fhir.bucket.persistence.SchemaConstants.LOADER_INSTANCE_KEY;
import static org.linuxforhealth.fhir.bucket.persistence.SchemaConstants.LOAD_COMPLETED;
import static org.linuxforhealth.fhir.bucket.persistence.SchemaConstants.LOAD_STARTED;
import static org.linuxforhealth.fhir.bucket.persistence.SchemaConstants.LOGICAL_ID;
import static org.linuxforhealth.fhir.bucket.persistence.SchemaConstants.LOGICAL_ID_BYTES;
import static org.linuxforhealth.fhir.bucket.persistence.SchemaConstants.LOGICAL_RESOURCES;
import static org.linuxforhealth.fhir.bucket.persistence.SchemaConstants.LOGICAL_RESOURCE_ID;
import static org.linuxforhealth.fhir.bucket.persistence.SchemaConstants.NOT_NULL;
import static org.linuxforhealth.fhir.bucket.persistence.SchemaConstants.NULLABLE;
import static org.linuxforhealth.fhir.bucket.persistence.SchemaConstants.OBJECT_NAME;
import static org.linuxforhealth.fhir.bucket.persistence.SchemaConstants.OBJECT_SIZE;
import static org.linuxforhealth.fhir.bucket.persistence.SchemaConstants.PID;
import static org.linuxforhealth.fhir.bucket.persistence.SchemaConstants.RESOURCE_BUNDLES;
import static org.linuxforhealth.fhir.bucket.persistence.SchemaConstants.RESOURCE_BUNDLE_ERRORS;
import static org.linuxforhealth.fhir.bucket.persistence.SchemaConstants.RESOURCE_BUNDLE_ID;
import static org.linuxforhealth.fhir.bucket.persistence.SchemaConstants.RESOURCE_BUNDLE_LOADS;
import static org.linuxforhealth.fhir.bucket.persistence.SchemaConstants.RESOURCE_BUNDLE_LOAD_ID;
import static org.linuxforhealth.fhir.bucket.persistence.SchemaConstants.RESOURCE_TYPE;
import static org.linuxforhealth.fhir.bucket.persistence.SchemaConstants.RESOURCE_TYPES;
import static org.linuxforhealth.fhir.bucket.persistence.SchemaConstants.RESOURCE_TYPE_ID;
import static org.linuxforhealth.fhir.bucket.persistence.SchemaConstants.RESPONSE_TIME_MS;
import static org.linuxforhealth.fhir.bucket.persistence.SchemaConstants.ROWS_PROCESSED;
import static org.linuxforhealth.fhir.bucket.persistence.SchemaConstants.SCAN_TSTAMP;
import static org.linuxforhealth.fhir.bucket.persistence.SchemaConstants.STATUS;
import static org.linuxforhealth.fhir.bucket.persistence.SchemaConstants.UNQ;
import static org.linuxforhealth.fhir.bucket.persistence.SchemaConstants.VERSION;

import org.linuxforhealth.fhir.bucket.app.Main;
import org.linuxforhealth.fhir.database.utils.api.ISchemaAdapter;
import org.linuxforhealth.fhir.database.utils.api.SchemaApplyContext;
import org.linuxforhealth.fhir.database.utils.model.Generated;
import org.linuxforhealth.fhir.database.utils.model.PhysicalDataModel;
import org.linuxforhealth.fhir.database.utils.model.Sequence;
import org.linuxforhealth.fhir.database.utils.model.Table;

/**
 * Defines and manages the little schema used to coordinate multiple
 * servers and maintain a list of resource logical ids generated by
 * the FHIR server.
 */
public class FhirBucketSchema {
    private final String schemaName;

    /**
     * Public constructor
     * @param schemaName
     */
    public FhirBucketSchema(String schemaName) {
        this.schemaName = schemaName;
    }

    /**
     * Create the model
     * @param pdm
     */
    public void constructModel(PhysicalDataModel pdm) {

        addSequences(pdm);
        
        // each time this program runs it registers an entry in
        // the loader_instances table
        addLoaderInstances(pdm);
        
        // the bundle files discovered during the bucket scan
        addBucketPaths(pdm);
        addResourceBundles(pdm);
        addResourceBundleLoads(pdm);
        addResourceBundleErrors(pdm);

        // for recording the ids generated for each resource by the FHIR server
        // as we load the bundles discovered during the scan phase
        Table resourceTypes = addResourceTypes(pdm);        
        addLogicalResources(pdm, resourceTypes);
    }
        
    protected void addSequences(PhysicalDataModel pdm) {
        Sequence jobAllocationSeq = new Sequence(schemaName, JOB_ALLOCATION_SEQ, 1, 1, 1000);
        pdm.addObject(jobAllocationSeq);
    }

    /**
     * Add the definition of the BUCKET_PATHS table to the model
     * @param pdm
     * @return
     */
    protected Table addLoaderInstances(PhysicalDataModel pdm) {

        Table bucketPaths = Table.builder(schemaName, LOADER_INSTANCES)
                .addBigIntColumn(   LOADER_INSTANCE_ID,              NOT_NULL)
                .setIdentityColumn( LOADER_INSTANCE_ID, Generated.ALWAYS)
                .addVarcharColumn( LOADER_INSTANCE_KEY,          36, NOT_NULL)
                .addVarcharColumn(            HOSTNAME,          64, NOT_NULL)
                .addIntColumn(                     PID,              NOT_NULL)
                .addTimestampColumn(  HEARTBEAT_TSTAMP,              NOT_NULL)
                .addVarcharColumn(              STATUS,           8, NOT_NULL)
                .addUniqueIndex(UNQ + "_loader_instances_key", LOADER_INSTANCE_KEY)
                .addPrimaryKey(LOADER_INSTANCES + "_PK", LOADER_INSTANCE_ID)
                .build(pdm);
        
        pdm.addTable(bucketPaths);
        pdm.addObject(bucketPaths);
        
        return bucketPaths;
    }

    /**
     * Add the definition of the BUCKET_PATHS table to the model
     * @param pdm
     * @return
     */
    protected Table addBucketPaths(PhysicalDataModel pdm) {
        
        // The FK BUCKET_PATH_ID starts as null, but is set when the
        // scanner thread of a loader claims the bucket/path for scanning,
        // and is set to NULL again after the scan is complete
        Table bucketPaths = Table.builder(schemaName, BUCKET_PATHS)
                .addBigIntColumn(       BUCKET_PATH_ID,              NOT_NULL)
                .setIdentityColumn(BUCKET_PATH_ID, Generated.ALWAYS)
                .addVarcharColumn(         BUCKET_NAME,          64, NOT_NULL)
                .addVarcharColumn(         BUCKET_PATH,         256, NOT_NULL)
                .addUniqueIndex(UNQ + "_bucket_paths_nmpth", BUCKET_NAME, BUCKET_PATH)
                .addPrimaryKey(BUCKET_PATHS + "_PK", BUCKET_PATH_ID)
                .build(pdm);
        
        pdm.addTable(bucketPaths);
        pdm.addObject(bucketPaths);
        
        return bucketPaths;
    }
   
    /**
     * Add the definition of the RESOURCE_BUNDLES table to the model
     * @param pdm
     * @return
     */
    protected Table addResourceBundles(PhysicalDataModel pdm) {
        // Note that the object_name is relative to the bundle path associated
        // with each record
        Table resourceBundles = Table.builder(schemaName, RESOURCE_BUNDLES)
                .addBigIntColumn(  RESOURCE_BUNDLE_ID,             NOT_NULL)
                .setIdentityColumn(RESOURCE_BUNDLE_ID,  Generated.ALWAYS)
                .addBigIntColumn(      BUCKET_PATH_ID,             NOT_NULL)
                .addVarcharColumn(        OBJECT_NAME,        128, NOT_NULL)
                .addBigIntColumn(         OBJECT_SIZE,             NOT_NULL)
                .addVarcharColumn(          FILE_TYPE,         12, NOT_NULL)
                .addVarcharColumn(               ETAG,         64, NOT_NULL) // COS is returning more than 32 character strings
                .addTimestampColumn(    LAST_MODIFIED,             NOT_NULL)
                .addTimestampColumn(      SCAN_TSTAMP,             NOT_NULL)
                .addIntColumn(                VERSION,             NOT_NULL) // the number of times we've seen this file change
                .addBigIntColumn(       ALLOCATION_ID,             NULLABLE) // Most recent load allocation
                .addBigIntColumn(  LOADER_INSTANCE_ID,             NULLABLE) // on this loader instance
                .addUniqueIndex(UNQ + "_resource_bundle_bktnm", BUCKET_PATH_ID, OBJECT_NAME)
                .addIndex(IDX + "_resource_bundle_allocid", ALLOCATION_ID)
                .addPrimaryKey(RESOURCE_BUNDLES + "_PK", RESOURCE_BUNDLE_ID)
                .addForeignKeyConstraint(FK + "_" + RESOURCE_BUNDLES + "_BKT", schemaName, BUCKET_PATHS, BUCKET_PATH_ID)
                .build(pdm);
        
        pdm.addTable(resourceBundles);
        pdm.addObject(resourceBundles);
        
        return resourceBundles;
    }

    /**
     * Track each time we attempt to load a bundle. When recycleSeconds is set
     * (see {@link Main}), the same bundle can be loaded over and over, so this
     * allows us to track performance over time. Each LOGICAL_INSTANCES record
     * created from a particular load run is also tied to this table via its
     * RESOURCE_BUNDLE_LOAD_ID foreign key
     * @param pdm
     * @return
     */
    protected Table addResourceBundleLoads(PhysicalDataModel pdm) {
        
        Table resourceBundles = Table.builder(schemaName, RESOURCE_BUNDLE_LOADS)
                .addBigIntColumn(  RESOURCE_BUNDLE_LOAD_ID,        NOT_NULL)
                .setIdentityColumn(RESOURCE_BUNDLE_LOAD_ID,  Generated.ALWAYS)
                .addBigIntColumn(  RESOURCE_BUNDLE_ID,             NOT_NULL)
                .addBigIntColumn(       ALLOCATION_ID,             NOT_NULL)
                .addBigIntColumn(  LOADER_INSTANCE_ID,             NOT_NULL)
                .addIntColumn(                VERSION,             NOT_NULL) // current version of the file when we started processing
                .addTimestampColumn(     LOAD_STARTED,             NOT_NULL)
                .addTimestampColumn(   LOAD_COMPLETED,             NULLABLE)
                .addIntColumn(         ROWS_PROCESSED,             NULLABLE)
                .addIntColumn(          FAILURE_COUNT,             NULLABLE)
                .addPrimaryKey(RESOURCE_BUNDLE_LOADS + "_PK", RESOURCE_BUNDLE_LOAD_ID)
                .addUniqueIndex(UNQ + "_" + RESOURCE_BUNDLE_LOADS + "RBAL", RESOURCE_BUNDLE_ID, ALLOCATION_ID)
                .addIndex("IDX_" + RESOURCE_BUNDLE_LOADS + "_RBV", RESOURCE_BUNDLE_ID, VERSION)
                .addForeignKeyConstraint(FK + "_" + RESOURCE_BUNDLE_LOADS + "_RB", schemaName, RESOURCE_BUNDLES, RESOURCE_BUNDLE_ID)
                .addForeignKeyConstraint(FK + "_" + RESOURCE_BUNDLE_LOADS + "_LI", schemaName, LOADER_INSTANCES, LOADER_INSTANCE_ID)
                .build(pdm);
        
        pdm.addTable(resourceBundles);
        pdm.addObject(resourceBundles);
        
        return resourceBundles;
    }
    
    /**
     * Add the definition of the RESOURCE_TYPES table to the model
     * @param pdm
     * @return
     */
    protected Table addResourceTypes(PhysicalDataModel pdm) {
        Table resourceTypesTable = Table.builder(schemaName, RESOURCE_TYPES)
                .addIntColumn(     RESOURCE_TYPE_ID,            NOT_NULL)
                .setIdentityColumn(RESOURCE_TYPE_ID, Generated.ALWAYS)
                .addVarcharColumn(    RESOURCE_TYPE,        64, NOT_NULL)
                .addUniqueIndex(UNQ + "_resource_types_rt", RESOURCE_TYPE)
                .addPrimaryKey(RESOURCE_TYPES + "_PK", RESOURCE_TYPE_ID)
                .build(pdm);
        
        pdm.addTable(resourceTypesTable);
        pdm.addObject(resourceTypesTable);
        
        return resourceTypesTable;
    }

    /**
     * Add the LOGICAL_RESOURCES table definition to the physical data model
     * @param pdm
     * @param resourceTypes
     */
    protected void addLogicalResources(PhysicalDataModel pdm, Table resourceTypes) {
        final String tableName = LOGICAL_RESOURCES;

        // note that the same bundle can be loaded multiple times,
        // and also that each bundle may contain several resources
        Table tbl = Table.builder(schemaName, tableName)
                .addBigIntColumn(       LOGICAL_RESOURCE_ID,                   NOT_NULL)
                .setIdentityColumn(     LOGICAL_RESOURCE_ID, Generated.ALWAYS)
                .addIntColumn(             RESOURCE_TYPE_ID,                   NOT_NULL)
                .addVarcharColumn(               LOGICAL_ID, LOGICAL_ID_BYTES, NOT_NULL) // the id assigned by the FHIR server
                .addBigIntColumn(   RESOURCE_BUNDLE_LOAD_ID,                   NOT_NULL) // created during this load
                .addIntColumn(                  LINE_NUMBER,                   NOT_NULL) // from this line in the file
                .addTimestampColumn(         CREATED_TSTAMP,                   NOT_NULL) // at this time
                .addIntColumn(             RESPONSE_TIME_MS,                   NULLABLE) // with this response time (not for bundles)
                .addPrimaryKey(tableName + "_PK", LOGICAL_RESOURCE_ID)
                .addUniqueIndex("UNQ_" + LOGICAL_RESOURCES + "_RTLI", RESOURCE_TYPE_ID, LOGICAL_ID)
                .addIndex("IDX_" + LOGICAL_RESOURCES + "_RBLN", RESOURCE_BUNDLE_LOAD_ID, LINE_NUMBER)
                .addForeignKeyConstraint(FK + tableName + "_RTID", schemaName, RESOURCE_TYPES, RESOURCE_TYPE_ID)
                .addForeignKeyConstraint(FK + tableName + "_RBID", schemaName, RESOURCE_BUNDLE_LOADS, RESOURCE_BUNDLE_LOAD_ID)
                .build(pdm);
        
        pdm.addTable(tbl);
        pdm.addObject(tbl);
    }

    /**
     * Add the RESOURCE_BUNDLE_ERRORS table to the {@link PhysicalDataModel}
     * @param pdm
     */
    protected void addResourceBundleErrors(PhysicalDataModel pdm) {
        final String tableName = RESOURCE_BUNDLE_ERRORS;

        // track errors associated with a particular RESOURCE_BUNDLE_LOADS
        Table tbl = Table.builder(schemaName, tableName)
                .addBigIntColumn(   RESOURCE_BUNDLE_LOAD_ID,                       NOT_NULL)
                .addIntColumn(                  LINE_NUMBER,                       NOT_NULL)
                .addVarcharColumn(               ERROR_TEXT,       ERROR_TEXT_LEN, NOT_NULL)
                .addTimestampColumn(           ERROR_TSTAMP,                       NOT_NULL)
                .addIntColumn(             RESPONSE_TIME_MS,                       NULLABLE)
                .addIntColumn(             HTTP_STATUS_CODE,                       NULLABLE)
                .addVarcharColumn(         HTTP_STATUS_TEXT, HTTP_STATUS_TEXT_LEN, NULLABLE)
                .addUniqueIndex("UNQ_" + RESOURCE_BUNDLE_ERRORS + "RBLN", RESOURCE_BUNDLE_LOAD_ID, LINE_NUMBER)
                .addForeignKeyConstraint(FK + tableName + "_RBID", schemaName, RESOURCE_BUNDLE_LOADS, RESOURCE_BUNDLE_LOAD_ID)
                .build(pdm);
        
        pdm.addTable(tbl);
        pdm.addObject(tbl);
    }

    /**
     * Apply the model to the database. Will generate the DDL and execute it
     * @param adapter
     * @param context
     * @param pdm
     */
    protected void applyModel(ISchemaAdapter adapter, SchemaApplyContext context, PhysicalDataModel pdm) {
        pdm.apply(adapter, context);
    }    
}
