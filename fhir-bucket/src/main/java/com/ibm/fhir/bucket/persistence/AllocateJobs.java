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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.bucket.api.BucketLoaderJob;
import com.ibm.fhir.bucket.api.FileType;
import com.ibm.fhir.database.utils.api.DataAccessException;
import com.ibm.fhir.database.utils.api.IDatabaseStatement;
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
    
    // Limit allocations to the given file type
    private final FileType fileType;
    
    // The id of this loader instance being allocated the job
    private final long loaderInstanceId;

    // The number of jobs to allocate
    private final int free;

    /**
     * Public constructor
     * @param bucketName
     * @param bucketPath
     */
    public AllocateJobs(String schemaName, List<BucketLoaderJob> jobList, FileType fileType, long loaderInstanceId, int free) {
        this.schemaName = schemaName;
        this.jobList = jobList;
        this.fileType = fileType;
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
        // Note the ORDER BY in the inner select is important to avoid deadlocks when running
        // concurrent loaders
        final String currentTimestamp = translator.currentTimestampString();
        final String MARK = ""
                + " UPDATE resource_bundles rb "
                + "    SET allocation_id = ?, "
                + "        loader_instance_id = ? "
                + "  WHERE rb.resource_bundle_id IN ( "
                + "     SELECT resource_bundle_id "
                + "       FROM resource_bundles "
                + "      WHERE allocation_id IS NULL "
                + "        AND file_type = ? "
                + "   ORDER BY last_modified, resource_bundle_id "
                + "      FETCH FIRST ? ROWS ONLY)";
        
        try (PreparedStatement ps = c.prepareStatement(MARK)) {
            ps.setLong(1, allocationId);
            ps.setLong(2, loaderInstanceId);
            ps.setString(3, fileType.name());
            ps.setInt(4, free);
            ps.executeUpdate();
        } catch (SQLException x) {
            logger.log(Level.SEVERE, MARK, x);
            throw new DataAccessException("Mark allocated jobs failed");
        }

        // Create new RESOURCE_BUNDLE_LOAD records for each of the bundles
        // that have just been allocated
        final String INS = ""
                + " INSERT INTO resource_bundle_loads (resource_bundle_id, allocation_id, loader_instance_id, load_started, version) "
                + "      SELECT rb.resource_bundle_id, rb.allocation_id, rb.loader_instance_id, " + currentTimestamp + ", rb.version "
                + "        FROM resource_bundles rb "
                + "       WHERE rb.allocation_id = ? " // allocation_id is assigned to us from a sequence, so no other loader will have it
            ;
        try (PreparedStatement ps = c.prepareStatement(INS)) {
            ps.setLong(1, allocationId);
            ps.executeUpdate();
        } catch (SQLException x) {
            logger.log(Level.SEVERE, INS, x);
            throw new DataAccessException("Insert allocated jobs failed");
        }
        
        // Now fetch the records we just marked. Order by just provides consistent ordering
        final String FETCH = ""
                + "SELECT bl.resource_bundle_load_id, rb.resource_bundle_id, bp.bucket_name, bp.bucket_path, "
                + "       rb.object_name, rb.object_size, rb.file_type, rb.version "
                + "  FROM resource_bundles rb, "
                + "       bucket_paths bp,"
                + "       resource_bundle_loads bl "
                + " WHERE rb.allocation_id = ? "
                + "   AND bp.bucket_path_id = rb.bucket_path_id "
                + "   AND bl.resource_bundle_id = rb.resource_bundle_id "
                + "   AND bl.allocation_id = rb.allocation_id "
                + "ORDER BY rb.last_modified, rb.resource_bundle_id ";
        
        try (PreparedStatement ps = c.prepareStatement(FETCH)) {
            ps.setLong(1, allocationId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                long resourceBundleLoadId = rs.getLong(1);
                long resourceBundleId = rs.getLong(2);
                String bucketName = rs.getString(3);
                String bucketPath = rs.getString(4);
                String objectName = rs.getString(5);
                long objectSize = rs.getLong(6);
                String fileTypeValue = rs.getString(7);
                int version = rs.getInt(8);
                jobList.add(new BucketLoaderJob(resourceBundleLoadId, resourceBundleId, bucketName, bucketPath, objectName, objectSize,
                    FileType.valueOf(fileTypeValue), version));
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, FETCH, x);
            throw new DataAccessException("Fetch allocated jobs failed");
        }
    }
}