/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bucket.scanner;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import com.ibm.fhir.bucket.api.BucketLoaderJob;
import com.ibm.fhir.bucket.api.CosItem;
import com.ibm.fhir.bucket.persistence.AddBucketPath;
import com.ibm.fhir.bucket.persistence.AddResourceBundle;
import com.ibm.fhir.bucket.persistence.AllocateJobs;
import com.ibm.fhir.bucket.persistence.RegisterLoaderInstance;
import com.ibm.fhir.bucket.persistence.ResourceTypeRec;
import com.ibm.fhir.bucket.persistence.ResourceTypesReader;
import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.api.ITransaction;
import com.ibm.fhir.database.utils.api.ITransactionProvider;

/**
 * The data access layer encapsulating interactions with the FHIR bucket schema
 */
public class DataAccess {
    private static final Logger logger = Logger.getLogger(DataAccess.class.getName());
    
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
                List<ResourceTypeRec> resourceTypes = dbAdapter.runStatement(new ResourceTypesReader());
                resourceTypes.stream().forEach(rt -> resourceTypeMap.put(rt.getResourceType(), rt.getResourceTypeId()));
                
                // Register this loader instance
                InetAddress addr = InetAddress.getLocalHost();
                RegisterLoaderInstance c1 = new RegisterLoaderInstance(instanceId, addr.getHostName(), -1);
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
                    AddBucketPath c1 = new AddBucketPath(item.getBucketName(), path);
                    Long bucketPathId = dbAdapter.runStatement(c1);
        
                    // Now register the bundle using the bucket record we created/retrieved
                    AddResourceBundle c2 = new AddResourceBundle(bucketPathId, name, item.getSize(), item.getFileType());
                    dbAdapter.runStatement(c2);
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
    public void allocateJobs(List<BucketLoaderJob> jobList, int free) {
        try (ITransaction tx = transactionProvider.getTransaction()) {
            try {
                AllocateJobs cmd = new AllocateJobs(schemaName, jobList, loaderInstanceId, free);
                dbAdapter.runStatement(cmd);
            } catch (Exception x) {
                tx.setRollbackOnly();
                throw x;
            }
        }
    }
}
