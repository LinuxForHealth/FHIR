/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bucket.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.bucket.api.BucketLoaderJob;
import com.ibm.fhir.bucket.api.FileType;
import com.ibm.fhir.database.utils.api.DataAccessException;
import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.IDatabaseSupplier;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;

/**
 * DAO to encapsulate all the SQL/DML used to retrieve and persist data
 * in the schema
 */
public class AllocateJobs implements IDatabaseStatement {
    private static final Logger logger = Logger.getLogger(AllocateJobs.class.getName());

    // The schema with all the database objects
    private final String schemaName;
    
    // The list of allocated jobs
    private final List<BucketLoaderJob> jobList;
    
    // The id of this loader instance being allocated the job
    private final long loaderInstanceId;

    // The number of jobs to allocate
    private final int free;

    /**
     * Public constructor
     * @param bucketName
     * @param bucketPath
     */
    public AllocateJobs(String schemaName, List<BucketLoaderJob> jobList, long loaderInstanceId, int free) {
        this.schemaName = schemaName;
        this.jobList = jobList;
        this.loaderInstanceId = loaderInstanceId;
        this.free = free;
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {

        // Get a new sequence number. This value is used to mark the records
        // allocated in this allocation run
        long allocationId;
        final String NEXTVAL = translator.selectSequenceNextValue(schemaName, "JOB_ALLOCATION_SEQ");
        try (PreparedStatement ps = c.prepareStatement(NEXTVAL)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                allocationId = rs.getLong(1);
            } else {
                throw new IllegalStateException("sequence did not provide a value!");
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, NEXTVAL, x);
            throw new DataAccessException("Get next allocationId failed");
        }
        
        // Mark the records we want to allocate using the unique allocationId we just obtained
        final String MARK = "UPDATE resource_bundles rb "
                + " SET allocation_id = ?, "
                + "     loader_instance_id = ?, "
                + "     load_started = CURRENT TIMESTAMP "
                + "WHERE rb.resource_bundle_id IN ( "
                + "     SELECT resource_bundle_id "
                + "       FROM resource_bundles "
                + "      WHERE allocation_id IS NULL "
                + "   ORDER BY resource_bundle_id "
                + "FETCH FIRST ? ROWS ONLY)";
        
        try (PreparedStatement ps = c.prepareStatement(MARK)) {
            ps.setLong(1, allocationId);
            ps.setLong(2, loaderInstanceId);
            ps.setInt(3, free);
            ps.executeUpdate();
        } catch (SQLException x) {
            logger.log(Level.SEVERE, MARK, x);
            throw new DataAccessException("Mark allocated jobs failed");
        }
        
        // Now fetch the records we just marked
        final String FETCH = "SELECT rb.resource_bundle_id, bp.bucket_name, bp.bucket_path, rb.object_name, rb.object_size, "
                + " rb.file_type "
                + " FROM resource_bundles rb, "
                + "      bucket_paths bp"
                + " WHERE rb.allocation_id = ? "
                + "   AND bp.bucket_path_id = rb.bucket_path_id";
        
        try (PreparedStatement ps = c.prepareStatement(FETCH)) {
            ps.setLong(1, allocationId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                long resourceBundleId = rs.getLong(1);
                String bucketName = rs.getString(2);
                String bucketPath = rs.getString(3);
                String objectName = rs.getString(4);
                long objectSize = rs.getLong(5);
                String fileTypeValue = rs.getString(6);
                jobList.add(new BucketLoaderJob(resourceBundleId, bucketName, bucketPath, objectName, objectSize,
                    FileType.valueOf(fileTypeValue)));
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, FETCH, x);
            throw new DataAccessException("Fetch allocated jobs failed");
        }
    }
}