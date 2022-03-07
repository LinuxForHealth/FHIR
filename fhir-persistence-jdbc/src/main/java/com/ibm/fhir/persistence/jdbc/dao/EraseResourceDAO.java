/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.model.DbType;
import com.ibm.fhir.persistence.ResourceEraseRecord;
import com.ibm.fhir.persistence.erase.EraseDTO;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCCache;
import com.ibm.fhir.persistence.jdbc.FHIRResourceDAOFactory;
import com.ibm.fhir.persistence.jdbc.connection.FHIRDbFlavor;
import com.ibm.fhir.persistence.jdbc.dao.api.FhirSequenceDAO;
import com.ibm.fhir.persistence.jdbc.dao.api.IResourceReferenceDAO;
import com.ibm.fhir.persistence.jdbc.dao.impl.ResourceDAOImpl;
import com.ibm.fhir.persistence.jdbc.dto.ErasedResourceRec;
import com.ibm.fhir.persistence.jdbc.util.ParameterTableSupport;

/**
 * EraseDAO is the data access layer of the erase operation which executes directly
 * against the database using SQL statements to:
 * <li>check the resource exists</li>
 * <li>delete all versions from the resource table</li>
 * <li>delete all parameters</li>
 * <li>delete entries from global table</li>
 * <li>delete resource specific logical resource entry</li>
 *
 * @implNote the first version of this class used a ThreadPool from OpenLiberty,
 * in the Runnable, the timeout was managed through a Parameter passed in from the client.
 * This used a ManagedConnection (see WSRdbManagedConnectionImpl.java in OpenLiberty) and a new threadid,
 * even with a passed JEEContext,SecurityContext and BaseContext in the ThreadPool
 * Postgres forks a new Connection and activates a new UserTransaction (with one already running).
 * This is not ideal, however, to manage this we've implemented a version specific erase.
 * If a timeout is hit (however unlikely), the user is expected to erase each version, and then erase the whole thing.
 */
public class EraseResourceDAO extends ResourceDAOImpl {

    private static final String CLASSNAME = EraseResourceDAO.class.getSimpleName();
    private static final Logger LOG = Logger.getLogger(CLASSNAME);

    private static final String CALL_POSTGRES = "{CALL %s.ERASE_RESOURCE(?, ?, ?, ?)}";
    private static final String CALL_DB2 = "CALL %s.ERASE_RESOURCE(?, ?, ?, ?)";

    // The translator specific to the database type we're working with
    private final IDatabaseTranslator translator;

    // The name of the admin schema where we find the SV_TENANT_ID variable
    private final String adminSchemaName;

    private ResourceEraseRecord eraseRecord;
    private EraseDTO eraseDto;

    /**
     * Public constructor
     *
     * @param conn
     * @param adminSchemaName
     * @param translator
     * @param schemaName
     * @param flavor
     * @param cache
     * @param rrd
     */
    public EraseResourceDAO(Connection conn, String adminSchemaName, IDatabaseTranslator translator, String schemaName, FHIRDbFlavor flavor, FHIRPersistenceJDBCCache cache,
            IResourceReferenceDAO rrd) {
        super(conn, schemaName, flavor, cache, rrd);
        this.adminSchemaName = adminSchemaName;
        this.translator = translator;
    }

    /**
     * Execute the stored procedure/function to erase the content.
     *
     * @param callStr the sql that should be executed
     * @param erasedResourceGroupId the id used to group together ERASED_RESOURCES records made by this procedure
     * @throws Exception
     */
    private void runCallableStatement(String callStr, long erasedResourceGroupId) throws Exception {
        try (CallableStatement call = getConnection().prepareCall(String.format(callStr, getSchemaName()))){
            call.setString(1, eraseDto.getResourceType());
            call.setString(2, eraseDto.getLogicalId());
            call.setLong(3, erasedResourceGroupId);
            call.registerOutParameter(4, Types.BIGINT);
            call.execute();

            int deleted = (int) call.getLong(4);
            if (LOG.isLoggable(Level.FINEST)) {
                LOG.finest("Deleted from [" + eraseDto.getResourceType() + "/" + eraseDto.getLogicalId() + "] deleted [" + deleted + "]");
            }

            eraseRecord.setTotal(deleted);

            ResourceEraseRecord.Status status = ResourceEraseRecord.Status.DONE;
            if (deleted == -1) {
                status = ResourceEraseRecord.Status.NOT_FOUND;
            }
            eraseRecord.setStatus(status);
        } catch (SQLException x) {
            LOG.log(Level.SEVERE, "SQL Call to Erase Failed to execute", x);
            throw translator.translate(x);
        }
    }

    /**
     * Executes the SQL logic as part of the dao rather than via a stored procedure/function.
     * 
     * @param erasedResourceGroupId
     * @throws SQLException
     */
    public void runInDao(long erasedResourceGroupId) throws FHIRPersistenceException {
        String resourceType = eraseDto.getResourceType();
        String logicalId = eraseDto.getLogicalId();

        long logicalResourceId = -1;
        int version = -1;
        Integer total = 0;

        // Prep 1: Get the v_resource_type_id. Cannot be null
        Integer resourceTypeId = getResourceTypeId(resourceType);

        // Prep 2: Get the logical from the system-wide logical resource level
        final String GET_LOGICAL_RESOURCES_SYSTEM =
                "SELECT logical_resource_id " +
                        "  FROM logical_resources" +
                        "  WHERE resource_type_id = ? AND logical_id = ?";
        // Note the addForUpdate is important on the logical_resources query
        try (PreparedStatement stmt = getConnection().prepareStatement(translator.addForUpdate(GET_LOGICAL_RESOURCES_SYSTEM))) {
            stmt.setLong(1, resourceTypeId);
            stmt.setString(2, logicalId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                logicalResourceId = rs.getLong(1);
            }
        } catch (SQLException x) {
            LOG.log(Level.SEVERE, GET_LOGICAL_RESOURCES_SYSTEM, x);
            throw translator.translate(x);
        }

        // Not found, return and stop processing.
        if (logicalResourceId == -1) {
            eraseRecord.setStatus(ResourceEraseRecord.Status.NOT_FOUND);
            return;
        }

        // Step 1: Get the Details for the Resource/Logical_Resource
        // the version_id need to be fetched.
        // these should never be null since we have a lock, and the resource exists.
        final String LOGICAL_RESOURCE_DETAILS =
                "SELECT LR1.VERSION_ID" +
                        " FROM " + resourceType + "_LOGICAL_RESOURCES LR1" +
                        " WHERE LR1.LOGICAL_RESOURCE_ID = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(LOGICAL_RESOURCE_DETAILS)) {
            stmt.setLong(1, logicalResourceId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                version = rs.getInt(1);
            }
        } catch (SQLException x) {
            LOG.log(Level.SEVERE, LOGICAL_RESOURCE_DETAILS, x);
            throw translator.translate(x);
        }

        if (version == -1) {
            // The resource is not found
            eraseRecord.setStatus(ResourceEraseRecord.Status.NOT_FOUND);
            return;
        }

        if (eraseDto.getVersion() != null && eraseDto.getVersion() > version) {
            // The requested version to erase is greater than the actual version
            eraseRecord.setStatus(ResourceEraseRecord.Status.NOT_SUPPORTED_GREATER);
            return;
        }

        // Skip iff it is version 1
        if (eraseDto.getVersion() != null && version != 1 && eraseDto.getVersion() == version) {
            // The requested version to erase is equal the actual version
            eraseRecord.setStatus(ResourceEraseRecord.Status.NOT_SUPPORTED_LATEST);
            return;
        }

        // If the version is 1, we need to fall all the way through and treat it
        // as a whole.
        if (eraseDto.getVersion() != null && version != 1) {
            
            // Record the affected record
            final String INSERT_ERASED_RESOURCES = translator.getType() == DbType.DB2
                    ? "INSERT INTO erased_resources(mt_id, erased_resource_group_id, resource_type_id, logical_id, version_id) "
                    + "     VALUES (" + adminSchemaName + ".SV_TENANT_ID, ?, ?, ?, ?)"
                    : "INSERT INTO erased_resources(erased_resource_group_id, resource_type_id, logical_id, version_id) "
                    + "     VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = getConnection().prepareStatement(INSERT_ERASED_RESOURCES)) {
                stmt.setLong(1, erasedResourceGroupId);
                stmt.setInt(2, resourceTypeId);
                stmt.setString(3, logicalId);
                stmt.setInt(4, eraseDto.getVersion());
                stmt.executeUpdate();
            } catch (SQLException x) {
                LOG.log(Level.SEVERE, INSERT_ERASED_RESOURCES, x);
                throw translator.translate(x);
            }

            // Update the specific version's PAYLOAD by updating the resource
            final String UPDATE_RESOURCE_PAYLOAD =
                    "UPDATE " + resourceType + "_RESOURCES" +
                            "    SET DATA = NULL" +
                            "    WHERE LOGICAL_RESOURCE_ID = ? AND VERSION_ID = ?";
            try (PreparedStatement stmt = getConnection().prepareStatement(UPDATE_RESOURCE_PAYLOAD)) {
                stmt.setLong(1, logicalResourceId);
                stmt.setInt(2, eraseDto.getVersion());
                int count = stmt.executeUpdate();
                total += count;
            } catch (SQLException x) {
                LOG.log(Level.SEVERE, UPDATE_RESOURCE_PAYLOAD, x);
                throw translator.translate(x);
            }

            // Step 2: Delete from resource_change_log for the version
            final String RCL_DELETE =
                    "DELETE FROM RESOURCE_CHANGE_LOG"
                   + " WHERE RESOURCE_ID IN ("
                       + " SELECT RESOURCE_ID"
                       + " FROM " + eraseDto.getResourceType() + "_RESOURCES"
                       + " WHERE LOGICAL_RESOURCE_ID = ?"
                           + " AND VERSION_ID = ?)";
            try (PreparedStatement stmt = getConnection().prepareStatement(RCL_DELETE)) {
                stmt.setLong(1, logicalResourceId);
                stmt.setInt(2, eraseDto.getVersion());
                int count = stmt.executeUpdate();
                LOG.fine(() -> "Count of resource_change_log deleted is " + count);
            } catch (SQLException x) {
                LOG.log(Level.SEVERE, RCL_DELETE, x);
                throw translator.translate(x);
            }

            eraseRecord.setTotal(total);
            eraseRecord.setStatus(ResourceEraseRecord.Status.VERSION);
            return;
        }

        // The entire logical resource is being erased, so don't include a version when we record this
        final String INSERT_ERASED_RESOURCES = translator.getType() == DbType.DB2
                ? "INSERT INTO erased_resources(mt_id, erased_resource_group_id, resource_type_id, logical_id) "
                + "     VALUES (" + adminSchemaName + ".SV_TENANT_ID, ?, ?, ?)"
                : "INSERT INTO erased_resources(erased_resource_group_id, resource_type_id, logical_id) "
                + "     VALUES (?, ?, ?)";
        try (PreparedStatement stmt = getConnection().prepareStatement(INSERT_ERASED_RESOURCES)) {
            stmt.setLong(1, erasedResourceGroupId);
            stmt.setInt(2, resourceTypeId);
            stmt.setString(3, logicalId);
            stmt.executeUpdate();
        } catch (SQLException x) {
            LOG.log(Level.SEVERE, INSERT_ERASED_RESOURCES, x);
            throw translator.translate(x);
        }

        // Step 2: Delete from resource_change_log
        final String RCL_DELETE =
                "DELETE FROM RESOURCE_CHANGE_LOG"
               + " WHERE RESOURCE_ID IN ("
                   + " SELECT RESOURCE_ID"
                   + " FROM " + eraseDto.getResourceType() + "_RESOURCES"
                   + " WHERE LOGICAL_RESOURCE_ID = ?)";
        try (PreparedStatement stmt = getConnection().prepareStatement(RCL_DELETE)) {
            stmt.setLong(1, logicalResourceId);
            int count = stmt.executeUpdate();
            LOG.fine(() -> "Count of resource_change_log deleted is " + count);
        } catch (SQLException x) {
            LOG.log(Level.SEVERE, RCL_DELETE, x);
            throw translator.translate(x);
        }

        // Step 3: Delete All Versions from Resources Table
        // Create the prepared statement to delete Resource Versions in chunks
        // Implementation note: fetch must be the last part of the sub-select
        final String DELETE_ALL_VERSIONS =
                "DELETE FROM " + resourceType + "_RESOURCES" +
                        "    WHERE LOGICAL_RESOURCE_ID = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(DELETE_ALL_VERSIONS)) {
            stmt.setLong(1, logicalResourceId);
            total = stmt.executeUpdate();
        } catch (SQLException x) {
            LOG.log(Level.SEVERE, DELETE_ALL_VERSIONS, x);
            throw translator.translate(x);
        }

        // Step 4: Delete from parameters tables
        try {
            deleteFromAllParametersTables(resourceType, logicalResourceId);
        } catch (SQLException x) {
            LOG.log(Level.SEVERE, "while deleting from parameter tables", x);
            throw translator.translate(x);
        }

        // Step 5: Delete from Logical Resources table
        final String DELETE_LOGICAL_RESOURCE =
                "DELETE FROM " + resourceType + "_LOGICAL_RESOURCES WHERE LOGICAL_RESOURCE_ID = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(DELETE_LOGICAL_RESOURCE)) {
            stmt.setLong(1, logicalResourceId);
            int count = stmt.executeUpdate();
            LOG.fine(() -> "Count of Resource_LR deleted is " + count);
        } catch (SQLException x) {
            LOG.log(Level.SEVERE, DELETE_LOGICAL_RESOURCE, x);
            throw translator.translate(x);
        }

        // Step 6: Delete from Global Logical Resources
        final String GLOBAL_DELETE_LOGICAL_RESOURCE =
                "DELETE FROM LOGICAL_RESOURCES WHERE LOGICAL_RESOURCE_ID = ? AND RESOURCE_TYPE_ID = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(GLOBAL_DELETE_LOGICAL_RESOURCE)) {
            stmt.setLong(1, logicalResourceId);
            stmt.setLong(2, resourceTypeId);
            int count = stmt.executeUpdate();
            LOG.fine(() -> "Count of LR deleted is " + count);
        } catch (SQLException x) {
            LOG.log(Level.SEVERE, GLOBAL_DELETE_LOGICAL_RESOURCE, x);
            throw translator.translate(x);
        }

        eraseRecord.setTotal(total);
        eraseRecord.setStatus(ResourceEraseRecord.Status.DONE);
    }

    /**
     * Deletes from the Parameters
     *
     * @param tablePrefix
     * @param logicalResourceId
     * @throws SQLException
     */
    public void deleteFromAllParametersTables(String tablePrefix, long logicalResourceId) throws SQLException {
        final String method = "deleteFromAllParametersTables";
        LOG.entering(CLASSNAME, method);
        ParameterTableSupport.deleteFromParameterTables(getConnection(), tablePrefix, logicalResourceId);
        LOG.exiting(CLASSNAME, method);
    }

    /**
     * processes the erase
     *
     * @param eraseRecord the output
     * @param eraseDto the input
     * @throws Exception
     */
    public long erase(ResourceEraseRecord eraseRecord, EraseDTO eraseDto) throws Exception {
        this.eraseRecord = eraseRecord;
        this.eraseDto = eraseDto;
        
        // Assign the ERASE_RESOURCE_GROUP_ID which is used to record all the
        // logical_resource and resource_versions erased here
        FhirSequenceDAO fhirSequence = FHIRResourceDAOFactory.getSequenceDAO(getConnection(), getFlavor());
        long erasedResourceGroupId = fhirSequence.nextValue();

        if (DbType.DB2.equals(getFlavor().getType()) && eraseDto.getVersion() == null) {
            runCallableStatement(CALL_DB2, erasedResourceGroupId);
        } else if (getFlavor().isFamilyPostgreSQL() && eraseDto.getVersion() == null) {
            runCallableStatement(CALL_POSTGRES, erasedResourceGroupId);
        } else {
            // Uses the Native Java to execute a Resource Erase
            runInDao(erasedResourceGroupId);
        }

        // So we know which records have been erased
        return erasedResourceGroupId;
    }

    /**
     * Fetch all the ERASED_RESOURCE records associated with the given erasedResourceGroupId
     * @param erasedResourceGroupId
     * @return
     */
    public List<ErasedResourceRec> getErasedResourceRecords(long erasedResourceGroupId) {
        List<ErasedResourceRec> result = new ArrayList<>();
        
        final String SELECT_RECORDS =
                "SELECT erased_resource_id, resource_type_id, logical_id, version_id " +
                "  FROM erased_resources " +
                " WHERE erased_resource_group_id = ?";

        try (PreparedStatement stmt = getConnection().prepareStatement(SELECT_RECORDS)) {
            stmt.setLong(1, erasedResourceGroupId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                long erasedResourceId = rs.getLong(1);
                int resourceTypeId = rs.getInt(2);
                String logicalId = rs.getString(3);
                Integer versionId = rs.getInt(4);
                if (rs.wasNull()) {
                    versionId = null;
                }
                
                ErasedResourceRec rec = new ErasedResourceRec(erasedResourceId, resourceTypeId, logicalId, versionId);
                result.add(rec);
            }
        } catch (SQLException x) {
            LOG.log(Level.SEVERE, SELECT_RECORDS, x);
            throw translator.translate(x);
        }
        
        return result;
    }

    /**
     * Delete all the ERASED_RESOURCE records belonging to the given erasedResourceGroupId
     * @param erasedResourceGroupId
     */
    public void clearErasedResourcesInGroup(long erasedResourceGroupId) {
        final String DEL =
                "DELETE FROM erased_resources " +
                "      WHERE erased_resource_group_id = ?";

        try (PreparedStatement stmt = getConnection().prepareStatement(DEL)) {
            stmt.setLong(1, erasedResourceGroupId);
            stmt.executeUpdate();
        } catch (SQLException x) {
            LOG.log(Level.SEVERE, DEL, x);
            throw translator.translate(x);
        }
    }
}