/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.bucket.scanner;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.bucket.api.BucketLoaderJob;
import org.linuxforhealth.fhir.bucket.api.BucketPath;
import org.linuxforhealth.fhir.bucket.api.CosItem;
import org.linuxforhealth.fhir.bucket.api.FileType;
import org.linuxforhealth.fhir.bucket.api.ResourceBundleData;
import org.linuxforhealth.fhir.bucket.api.ResourceBundleError;
import org.linuxforhealth.fhir.bucket.api.ResourceIdValue;
import org.linuxforhealth.fhir.bucket.api.ResourceRef;
import org.linuxforhealth.fhir.bucket.persistence.AddBucketPath;
import org.linuxforhealth.fhir.bucket.persistence.AddResourceBundle;
import org.linuxforhealth.fhir.bucket.persistence.AddResourceBundleErrors;
import org.linuxforhealth.fhir.bucket.persistence.AllocateJobs;
import org.linuxforhealth.fhir.bucket.persistence.ClearStaleAllocations;
import org.linuxforhealth.fhir.bucket.persistence.GetLastProcessedLineNumber;
import org.linuxforhealth.fhir.bucket.persistence.GetLogicalIds;
import org.linuxforhealth.fhir.bucket.persistence.GetResourceRefsForBundleLine;
import org.linuxforhealth.fhir.bucket.persistence.LoaderInstanceHeartbeat;
import org.linuxforhealth.fhir.bucket.persistence.MarkBundleDone;
import org.linuxforhealth.fhir.bucket.persistence.RecordLogicalId;
import org.linuxforhealth.fhir.bucket.persistence.RegisterLoaderInstance;
import org.linuxforhealth.fhir.bucket.persistence.ResourceTypeRec;
import org.linuxforhealth.fhir.bucket.persistence.ResourceTypesReader;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseAdapter;
import org.linuxforhealth.fhir.database.utils.api.ITransaction;
import org.linuxforhealth.fhir.database.utils.api.ITransactionProvider;
import org.linuxforhealth.fhir.model.resource.Patient;

/**
 * The data access layer encapsulating interactions with the FHIR bucket schema
 */
public class DataAccess {
    private static final Logger logger = Logger.getLogger(DataAccess.class.getName());

    // no heartbeats for 60 seconds means something has gone wrong
    private static final long HEARTBEAT_TIMEOUT_MS = 60000;

    // how many errors to insert per JDBC batch
    private int errorBatchSize = 10;

    // The adapter we use to execute database statements
    private final IDatabaseAdapter dbAdapter;

    // Simple transaction service for use outside of JEE
    private final ITransactionProvider transactionProvider;

    // Internal cache of resource types, which are created as part of schema deployment
    private final Map<String, Integer> resourceTypeMap = new ConcurrentHashMap<>();

    // The unique id string representing this instance of the loader
    private final String instanceId;

    // The id returned by the database when registering this loader instance
    private long loaderInstanceId;

    // the name of the schema holding all the tables
    private final String schemaName;

    /**
     * Public constructor
     * @param connectionPool
     * @param txProvider
     * @param schemaName
     */
    public DataAccess(IDatabaseAdapter dbAdapter, ITransactionProvider txProvider, String schemaName) {
        this.dbAdapter = dbAdapter;
        this.transactionProvider = txProvider;
        this.schemaName = schemaName;

        // Generate a unique id string to represent this instance of the loader while it's running
        UUID uuid = UUID.randomUUID();
        this.instanceId = uuid.toString();
    }

    /**
     * Initialize the object
     */
    public void init() {
        try (ITransaction tx = transactionProvider.getTransaction()) {
            try {
                List<ResourceTypeRec> resourceTypes = dbAdapter.runStatement(new ResourceTypesReader(this.schemaName));
                resourceTypes.stream().forEach(rt -> resourceTypeMap.put(rt.getResourceType(), rt.getResourceTypeId()));

                // Register this loader instance
                InetAddress addr = InetAddress.getLocalHost();
                RegisterLoaderInstance c1 = new RegisterLoaderInstance(this.schemaName, instanceId, addr.getHostName(), -1);
                this.loaderInstanceId = dbAdapter.runStatement(c1);
            } catch (UnknownHostException x) {
                logger.severe("FATAL ERROR. Failed to register instance");
                tx.setRollbackOnly();
                throw new IllegalStateException(x);
            }
        }
    }

    /**
     * Create a record in the database to track this item if it doesn't
     * currently exist
     * @param item
     */
    public void registerBucketItem(CosItem item) {
        try (ITransaction tx = transactionProvider.getTransaction()) {
            try {
                String name;
                String path = item.getItemName();
                int idx = path.lastIndexOf('/');
                if (idx > 0) {
                    name = path.substring(idx+1); // everything after the last /
                    path = path.substring(0, idx+1); // up to and including the last /
                } else if (idx == 0) {
                    // item name is just '/' which we don't think is valid
                    name = null;
                } else {
                    // In the root "folder"
                    name = path;
                    path = "/";
                }

                if (name != null) {
                    AddBucketPath c1 = new AddBucketPath(this.schemaName, item.getBucketName(), path);
                    Long bucketPathId = dbAdapter.runStatement(c1);

                    // Now register the bundle using the bucket record we created/retrieved
                    AddResourceBundle c2 = new AddResourceBundle(schemaName, bucketPathId, name, item.getSize(), item.getFileType(),
                        item.geteTag(), item.getLastModified());
                    ResourceBundleData old = dbAdapter.runStatement(c2);
                    if (old != null && !old.matches(item.getSize(), item.geteTag(), item.getLastModified())) {
                        // log the fact that the item has been changed in COS and so we've updated our
                        // record of it in the bucket database -> it will be processed again.
                        logger.info("COS item changed, " + item.toString()
                        + ", old={size=" + old.getObjectSize() + ", etag=" + old.geteTag() + ", lastModified=" + old.getLastModified() + "}"
                        + ", new={size=" + item.getSize() + ", etag=" + item.geteTag() + ", lastModified=" + item.getLastModified() + "}"
                        );
                    }
                } else {
                    logger.warning("Bad item name: '" + item.toString());
                }
            } catch (Exception x) {
                tx.setRollbackOnly();
                throw x;
            }
        }
    }

    /**
     * Allocate up to free jobs to this loader instance
     * @param jobList
     * @param free
     */
    public void allocateJobs(List<BucketLoaderJob> jobList, FileType fileType, int free, int recycleSeconds, Collection<BucketPath> bucketPaths) {
        try (ITransaction tx = transactionProvider.getTransaction()) {
            try {
                // First business of the day is to check for liveness and clear
                // any allocations for instances we think are no longer active
                ClearStaleAllocations liveness = new ClearStaleAllocations(schemaName, loaderInstanceId, HEARTBEAT_TIMEOUT_MS, recycleSeconds);
                dbAdapter.runStatement(liveness);

                AllocateJobs cmd = new AllocateJobs(schemaName, jobList, fileType, loaderInstanceId, free, bucketPaths);
                dbAdapter.runStatement(cmd);
            } catch (Exception x) {
                tx.setRollbackOnly();
                throw x;
            }
        }
    }

    /**
     * Save the logical id
     * @param simpleName
     * @param logicalId
     */
    public void recordLogicalId(String resourceType, String logicalId, long resourceBundleLoadId, int lineNumber, Integer responseTimeMs) {
        try (ITransaction tx = transactionProvider.getTransaction()) {
            try {
                Integer resourceTypeId = resourceTypeMap.get(resourceType);
                if (resourceTypeId == null) {
                    // unlikely, unless the map hasn't been initialized properly
                    throw new IllegalStateException("resourceType not found: " + resourceType);
                }

                RecordLogicalId cmd = new RecordLogicalId(schemaName, resourceTypeId, logicalId, resourceBundleLoadId, lineNumber, responseTimeMs);
                dbAdapter.runStatement(cmd);
            } catch (Exception x) {
                tx.setRollbackOnly();
                throw x;
            }
        }
    }

    /**
     * Update the heartbeat tstamp of the record representing this loader instance
     * to tell everyone that we're still alive.
     */
    public void heartbeat() {
        try (ITransaction tx = transactionProvider.getTransaction()) {
            try {
                LoaderInstanceHeartbeat heartbeat = new LoaderInstanceHeartbeat(this.schemaName, this.loaderInstanceId);
                dbAdapter.runStatement(heartbeat);
            } catch (Exception x) {
                tx.setRollbackOnly();
                throw x;
            }
        }
    }

    /**
     * @param job
     */
    public void markJobDone(BucketLoaderJob job) {
        try (ITransaction tx = transactionProvider.getTransaction()) {
            try {
                MarkBundleDone c1 = new MarkBundleDone(schemaName, job.getResourceBundleLoadId(), job.getFailureCount(), job.getCompletedCount());
                dbAdapter.runStatement(c1);
            } catch (Exception x) {
                tx.setRollbackOnly();
                throw x;
            }
        }
    }

    /**
     * Load the list of resourceType/logicalId DTO objects as a batch in one transaction
     * @param resourceBundleId
     * @param lineNumber
     * @param idValues
     */
    public void recordLogicalIds(long resourceBundleLoadId, int lineNumber, List<ResourceIdValue> idValues, int batchSize) {
        try (ITransaction tx = transactionProvider.getTransaction()) {
            try {
                for (ResourceIdValue idv: idValues) {
                    Integer resourceTypeId = resourceTypeMap.get(idv.getResourceType());
                    if (resourceTypeId == null) {
                        // unlikely, unless the map hasn't been initialized properly
                        throw new IllegalStateException("resourceType not found: " + idv.getResourceType());
                    }

                    // individual inserts to handle issues with duplicates
                    RecordLogicalId cmd = new RecordLogicalId(schemaName, resourceTypeId, idv.getLogicalId(), resourceBundleLoadId, lineNumber, -1);
                    dbAdapter.runStatement(cmd);
                }


            } catch (Exception x) {
                tx.setRollbackOnly();
                throw x;
            }
        }

    }

    /**
     * Save the errors generated when loading the given resource bundle. Because a given
     * bundle may be loaded multiple times with different outcomes, the error records are
     * each associated with the current loaderInstanceId. This can occur when a loader
     * dies before the bundle completes.
     * @param resourceBundleId
     * @param lineNumber
     * @param errors
     * @param batchSize
     */
    public void recordErrors(long resourceBundleLoadId, int lineNumber, List<ResourceBundleError> errors) {
        try (ITransaction tx = transactionProvider.getTransaction()) {
            try {
                AddResourceBundleErrors cmd = new AddResourceBundleErrors(schemaName, resourceBundleLoadId, errors, errorBatchSize);
                dbAdapter.runStatement(cmd);
            } catch (Exception x) {
                tx.setRollbackOnly();
                throw x;
            }
        }
    }

    /**
     * Get the last processed line number for the given resource bundle identified by its id.
     * This is calculated by looking for the max line_number value recorded for the bundle
     * in the logical_resources table.
     * @param resourceBundleId
     * @param version
     */
    public Integer getLastProcessedLineNumber(long resourceBundleId, int version) {
        try (ITransaction tx = transactionProvider.getTransaction()) {
            try {
                GetLastProcessedLineNumber cmd = new GetLastProcessedLineNumber(this.schemaName, resourceBundleId, version);
                return dbAdapter.runStatement(cmd);
            } catch (Exception x) {
                tx.setRollbackOnly();
                throw x;
            }
        }
    }

    /**
     * Get the list of resourceType/logicalId resource references generated when processing
     * the given lineNumber of the identified resource bundle and its version
     * @param resourceBundleId
     * @param version
     * @param lineNumber
     * @return
     */
    public List<ResourceRef> getResourceRefsForLine(long resourceBundleId, int version, int lineNumber) {
        try (ITransaction tx = transactionProvider.getTransaction()) {
            try {
                GetResourceRefsForBundleLine cmd = new GetResourceRefsForBundleLine(this.schemaName, resourceBundleId, version, lineNumber);
                return dbAdapter.runStatement(cmd);
            } catch (Exception x) {
                tx.setRollbackOnly();
                throw x;
            }
        }
    }

    /**
     * @param patientIds
     * @param patientsPerBatch
     */
    public void selectRandomPatientIds(List<String> patientIds, int maxPatients) {
        try (ITransaction tx = transactionProvider.getTransaction()) {
            try {
                // Grab the list of patient logical ids
                GetLogicalIds cmd = new GetLogicalIds(this.schemaName, patientIds, Patient.class.getSimpleName(), maxPatients);
                dbAdapter.runStatement(cmd);
            } catch (Exception x) {
                tx.setRollbackOnly();
                throw x;
            }
        }
    }
}