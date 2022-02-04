/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bucket.persistence.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.bucket.api.BucketLoaderJob;
import com.ibm.fhir.bucket.api.BucketPath;
import com.ibm.fhir.bucket.api.FileType;
import com.ibm.fhir.bucket.api.ResourceBundleData;
import com.ibm.fhir.bucket.api.ResourceBundleError;
import com.ibm.fhir.bucket.api.ResourceIdValue;
import com.ibm.fhir.bucket.api.ResourceRef;
import com.ibm.fhir.bucket.persistence.AddBucketPath;
import com.ibm.fhir.bucket.persistence.AddResourceBundle;
import com.ibm.fhir.bucket.persistence.AddResourceBundleErrors;
import com.ibm.fhir.bucket.persistence.AllocateJobs;
import com.ibm.fhir.bucket.persistence.ClearStaleAllocations;
import com.ibm.fhir.bucket.persistence.FhirBucketSchema;
import com.ibm.fhir.bucket.persistence.GetLastProcessedLineNumber;
import com.ibm.fhir.bucket.persistence.GetResourceRefsForBundleLine;
import com.ibm.fhir.bucket.persistence.MarkBundleDone;
import com.ibm.fhir.bucket.persistence.MergeResourceTypes;
import com.ibm.fhir.bucket.persistence.MergeResources;
import com.ibm.fhir.bucket.persistence.RecordLogicalId;
import com.ibm.fhir.bucket.persistence.RecordLogicalIdList;
import com.ibm.fhir.bucket.persistence.RegisterLoaderInstance;
import com.ibm.fhir.bucket.persistence.ResourceRec;
import com.ibm.fhir.bucket.persistence.ResourceTypeRec;
import com.ibm.fhir.bucket.persistence.ResourceTypesReader;
import com.ibm.fhir.core.FHIRVersionParam;
import com.ibm.fhir.core.util.ResourceTypeHelper;
import com.ibm.fhir.database.utils.api.ITransaction;
import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.database.utils.common.JdbcTarget;
import com.ibm.fhir.database.utils.derby.DerbyAdapter;
import com.ibm.fhir.database.utils.derby.DerbyConnectionProvider;
import com.ibm.fhir.database.utils.derby.DerbyMaster;
import com.ibm.fhir.database.utils.model.PhysicalDataModel;
import com.ibm.fhir.database.utils.pool.PoolConnectionProvider;
import com.ibm.fhir.database.utils.transaction.SimpleTransactionProvider;
import com.ibm.fhir.database.utils.version.CreateVersionHistory;
import com.ibm.fhir.database.utils.version.VersionHistoryService;

/**
 * Tests the FHIR bucket schema
 */
@Test(singleThreaded = true)
public class FhirBucketSchemaTest {
    private static final Logger logger = Logger.getLogger(FhirBucketSchemaTest.class.getName());

    private static final String DB_NAME = "target/derby/bucketDB";

    // put everything into the one Derby schema
    private static final String ADMIN_SCHEMA_NAME = "APP";
    private static final String DATA_SCHEMA_NAME = "APP";

    // The database we set up
    private DerbyMaster db;

    // Connection pool used to work alongside the transaction provider
    private PoolConnectionProvider connectionPool;

    // Simple transaction service for use outside of JEE
    private ITransactionProvider transactionProvider;

    // The UUID id we use for the loader id of this test
    private final UUID uuid = UUID.randomUUID();

    // The id allocated when we register this loader instance
    private long loaderInstanceId;


    @BeforeClass
    public void prepare() throws Exception {
        DerbyMaster.dropDatabase(DB_NAME);
        db = new DerbyMaster(DB_NAME);

        this.connectionPool = new PoolConnectionProvider(new DerbyConnectionProvider(db, null), 10);
        this.transactionProvider = new SimpleTransactionProvider(connectionPool);

        // Lambdas are quite tasty for this sort of thing
        db.runWithAdapter(adapter -> CreateVersionHistory.createTableIfNeeded(ADMIN_SCHEMA_NAME, adapter));

        // apply the model we've defined to the new Derby database
        VersionHistoryService vhs = createVersionHistoryService();

        // Create the schema in a managed transaction
        FhirBucketSchema schema = new FhirBucketSchema(DATA_SCHEMA_NAME);
        PhysicalDataModel pdm = new PhysicalDataModel();
        schema.constructModel(pdm);

        // Now apply the physical data model to the database
        try (ITransaction tx = transactionProvider.getTransaction()) {
            try {
                db.createSchema(connectionPool, vhs, pdm);
            } catch (Throwable t) {
                // mark the transaction for rollback
                tx.setRollbackOnly();
                throw t;
            }
        }

    }

    /**
     * Run some simple tests against the schema
     */
    @Test
    public void basicBucketSchemaTests() {
        assertNotNull(db);

        DerbyAdapter adapter = new DerbyAdapter(connectionPool);
        try (ITransaction tx = transactionProvider.getTransaction()) {
            try {
                RegisterLoaderInstance c1 = new RegisterLoaderInstance(DATA_SCHEMA_NAME, uuid.toString(), "host", 1234);
                Long lid = adapter.runStatement(c1);
                assertNotNull(lid);
                this.loaderInstanceId = lid; // save for future tests

                AddBucketPath c2 = new AddBucketPath(DATA_SCHEMA_NAME, "bucket1", "/path/to/dir1/");
                Long bucketId = adapter.runStatement(c2);
                assertNotNull(bucketId);

                // Try again with the same data...should get back the id
                Long id2 = adapter.runStatement(c2);
                assertNotNull(id2);
                assertEquals(id2, bucketId);

                AddBucketPath c3 = new AddBucketPath(DATA_SCHEMA_NAME, "bucket1", "/path/to/dir1/");
                Long id3 = adapter.runStatement(c3);
                assertNotNull(id3);
                assertEquals(id3, id2);

                // Register a resource bundle under the first bucket path "bucket1:/path/to/dir1/"
                AddResourceBundle c4 = new AddResourceBundle(DATA_SCHEMA_NAME, bucketId, "patient1.json", 1024, FileType.JSON, "1234abcd", new Date());
                ResourceBundleData id4 = adapter.runStatement(c4);
                assertNotNull(id4);

                // Try adding the same record again (should be ignored because we didn't change it)
                ResourceBundleData id5 = adapter.runStatement(c4);
                assertNotNull(id5);
                assertEquals(id4.getResourceBundleId(), id5.getResourceBundleId());

                // Add a second resource bundle record
                AddResourceBundle c5 = new AddResourceBundle(DATA_SCHEMA_NAME, bucketId, "patient2.json", 1024, FileType.JSON, "1234abcd", new Date());
                ResourceBundleData id6 = adapter.runStatement(c5);
                assertNotNull(id6);
                assertNotEquals(id6.getResourceBundleId(), id5.getResourceBundleId());

                // Populate the resource types table
                Set<String> resourceTypes = ResourceTypeHelper.getR4bResourceTypesFor(FHIRVersionParam.VERSION_43);
                MergeResourceTypes c6 = new MergeResourceTypes(DATA_SCHEMA_NAME, resourceTypes);
                adapter.runStatement(c6);


            } catch (Throwable t) {
                // mark the transaction for rollback
                tx.setRollbackOnly();
                throw t;
            }
        }
    }

    @Test(dependsOnMethods = { "basicBucketSchemaTests" })
    public void readResourceTypesTest() {
        DerbyAdapter adapter = new DerbyAdapter(connectionPool);
        try (ITransaction tx = transactionProvider.getTransaction()) {
            try {
                ResourceTypesReader c1 = new ResourceTypesReader(DATA_SCHEMA_NAME);
                List<ResourceTypeRec> resourceTypes = adapter.runStatement(c1);

                // Check against our reference set of resources
                Set<String> reference = ResourceTypeHelper.getR4bResourceTypesFor(FHIRVersionParam.VERSION_43);

                assertTrue(reference.size() > 0);
                assertEquals(resourceTypes.size(), reference.size());
                for (ResourceTypeRec rec: resourceTypes) {
                    assertTrue(reference.contains(rec.getResourceType()));
                }

            } catch (Throwable t) {
                // mark the transaction for rollback
                tx.setRollbackOnly();
                throw t;
            }
        }
    }

    @Test(dependsOnMethods = { "readResourceTypesTest" })
    public void resourceBundlesTest() {
        DerbyAdapter adapter = new DerbyAdapter(connectionPool);
        try (ITransaction tx = transactionProvider.getTransaction()) {
            try {
                // Need a bucket_path so we can create a resource_bundle
                AddBucketPath c1 = new AddBucketPath(DATA_SCHEMA_NAME, "bucket1", "/path/to/dir2/");
                Long bucketPathId = adapter.runStatement(c1);

                // Test creation of resource bundles
                AddResourceBundle c2 = new AddResourceBundle(DATA_SCHEMA_NAME, bucketPathId, "patient1.json", 1024, FileType.JSON, "abcd123", new Date());
                ResourceBundleData resourceBundleData = adapter.runStatement(c2);
                assertNotNull(resourceBundleData);
            } catch (Throwable t) {
                // mark the transaction for rollback
                tx.setRollbackOnly();
                throw t;
            }
        }
    }

    @Test(dependsOnMethods = { "resourceBundlesTest" })
    public void allocateJobsTest() {
        DerbyAdapter adapter = new DerbyAdapter(connectionPool);
        try (ITransaction tx = transactionProvider.getTransaction()) {
            try {
                List<BucketPath> bucketPaths = new ArrayList<>();
                List<BucketLoaderJob> jobList = new ArrayList<>();
                AllocateJobs c2 = new AllocateJobs(DATA_SCHEMA_NAME, jobList, FileType.JSON, loaderInstanceId, 2, bucketPaths);
                adapter.runStatement(c2);

                // check we got the jobs we expected
                assertEquals(jobList.size(), 2);
                assertEquals(jobList.get(0).getObjectKey(), "/path/to/dir1/patient1.json");
                assertEquals(jobList.get(1).getObjectKey(), "/path/to/dir1/patient2.json");

                // Allocate the remaining job and check
                jobList.clear();
                AllocateJobs c3 = new AllocateJobs(DATA_SCHEMA_NAME, jobList, FileType.JSON, loaderInstanceId, 2, bucketPaths);
                adapter.runStatement(c3);
                assertEquals(jobList.size(), 1);
                assertEquals(jobList.get(0).getObjectKey(), "/path/to/dir2/patient1.json");

                // Remove any stale allocations. Give a fake loaderInstanceId because
                // we don't touch stuff that we own. Make the timeout negative to force
                // the timeout
                ClearStaleAllocations c4 = new ClearStaleAllocations(DATA_SCHEMA_NAME, loaderInstanceId + 1, -60000, -1);
                adapter.runStatement(c4);

                // Now we should be able to see all 3 allocations be reassigned
                jobList.clear();
                AllocateJobs c5 = new AllocateJobs(DATA_SCHEMA_NAME, jobList, FileType.JSON, loaderInstanceId, 3, bucketPaths);
                adapter.runStatement(c5);
                assertEquals(jobList.size(), 3);

                MarkBundleDone c6 = new MarkBundleDone(DATA_SCHEMA_NAME, jobList.get(0).getResourceBundleLoadId(), 0, 1);
                adapter.runStatement(c6);

                // recycle completed jobs immediately
                ClearStaleAllocations c7 = new ClearStaleAllocations(DATA_SCHEMA_NAME, loaderInstanceId + 1, 100000, 0);
                adapter.runStatement(c7);

                // Grab the job-list again. Should get 3
                jobList.clear();
                adapter.runStatement(c5);
                assertEquals(jobList.size(), 3);

                // With a job, we have a resource_bundle_loads record, so we can create some resources
                Map<String, Integer> resourceTypeMap = new HashMap<>();
                List<ResourceTypeRec> resourceTypes = adapter.runStatement(new ResourceTypesReader(DATA_SCHEMA_NAME));
                resourceTypes.stream().forEach(rt -> resourceTypeMap.put(rt.getResourceType(), rt.getResourceTypeId()));
                final int patientTypeId = resourceTypeMap.get("Patient");
                BucketLoaderJob job = jobList.get(0);
                List<ResourceRec> resources = new ArrayList<>();
                final int LINE_COUNT = 5;
                for (int i=0; i<LINE_COUNT; i++) {
                    resources.add(new ResourceRec(patientTypeId, "patient-" + i, job.getResourceBundleLoadId(), i));
                }
                adapter.runStatement(new MergeResources(resources));

                // Single logical id upload
                final int lineNumber = LINE_COUNT;
                RecordLogicalId c8 = new RecordLogicalId(DATA_SCHEMA_NAME, patientTypeId, "patient-5", job.getResourceBundleLoadId(), lineNumber, 0);
                adapter.runStatement(c8);

                // Check that the max line number for the given bundle
                GetLastProcessedLineNumber c9 = new GetLastProcessedLineNumber(DATA_SCHEMA_NAME, job.getResourceBundleId(), job.getVersion());
                Integer lastLine = adapter.runStatement(c9);
                assertNotNull(lastLine);
                assertEquals(lastLine.intValue(), lineNumber);


                // Add some resource bundle errors
                // int lineNumber, String errorText, Integer responseTimeMs, Integer httpStatusCode, String httpStatusText
                List<ResourceBundleError> errors = new ArrayList<>();
                errors.add(new ResourceBundleError(0, "error1", null, null, null));
                errors.add(new ResourceBundleError(1, "error2", 60000, 400, "timeout"));
                AddResourceBundleErrors c10 = new AddResourceBundleErrors(DATA_SCHEMA_NAME, job.getResourceBundleLoadId(), errors, 10);
                adapter.runStatement(c10);

                // Fetch some ResourceRefs for a line we know we have loaded
                GetResourceRefsForBundleLine c11 = new GetResourceRefsForBundleLine(DATA_SCHEMA_NAME, job.getResourceBundleId(), job.getVersion(), lineNumber);
                List<ResourceRef> refs = adapter.runStatement(c11);
                assertNotNull(refs);
                assertEquals(refs.size(), 1);

                // And an empty list
                GetResourceRefsForBundleLine c12 = new GetResourceRefsForBundleLine(DATA_SCHEMA_NAME, job.getResourceBundleId(), job.getVersion(), lineNumber+1);
                refs = adapter.runStatement(c12);
                assertNotNull(refs);
                assertEquals(refs.size(), 0);

                // Test batch insert of resource id values
                List<ResourceIdValue> resourceIdValues = new ArrayList<>();
                resourceIdValues.add(new ResourceIdValue("Patient", "patient-123"));
                resourceIdValues.add(new ResourceIdValue("Patient", "patient-124"));
                RecordLogicalIdList c13 = new RecordLogicalIdList(job.getResourceBundleLoadId(), lastLine+1, resourceIdValues, resourceTypeMap, 10);
                adapter.runStatement(c13);

                // Make sure we can find both resources
                GetResourceRefsForBundleLine c14 = new GetResourceRefsForBundleLine(DATA_SCHEMA_NAME, job.getResourceBundleId(), job.getVersion(), lastLine+1);
                refs = adapter.runStatement(c14);
                assertNotNull(refs);
                assertEquals(refs.size(), 2);

            } catch (Throwable t) {
                // mark the transaction for rollback
                tx.setRollbackOnly();
                throw t;
            }
        }
    }

    @AfterClass
    public void tearDown() throws Exception {
        if (db != null) {
            db.close();
        }
    }

    /**
     * Create the version history table and a simple service which is used to
     * access information from it.
     *
     * @throws SQLException
     */
    protected VersionHistoryService createVersionHistoryService() throws SQLException {

        // No complex transaction handling required here. Simply check if the versions
        // table exists, and if not, create it.
        try (Connection c = db.getConnection()) {
            try {
                JdbcTarget target = new JdbcTarget(c);
                DerbyAdapter derbyAdapter = new DerbyAdapter(target);
                CreateVersionHistory.createTableIfNeeded(ADMIN_SCHEMA_NAME, derbyAdapter);
                c.commit();
            } catch (SQLException x) {
                logger.log(Level.SEVERE, "failed to create version history table", x);
                c.rollback(); // may generate its own exception if the database is messed up
                throw x;
            }
        }

        // Current version history for the data schema.
        VersionHistoryService vhs = new VersionHistoryService(ADMIN_SCHEMA_NAME, DATA_SCHEMA_NAME);
        vhs.setTransactionProvider(transactionProvider);
        vhs.setTarget(new DerbyAdapter(this.connectionPool));
        vhs.init();
        return vhs;
    }

}
