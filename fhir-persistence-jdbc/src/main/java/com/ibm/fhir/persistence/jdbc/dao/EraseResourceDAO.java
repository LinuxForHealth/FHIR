/*
 * (C) Copyright IBM Corp. 2021
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
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.concurrent.ManagedExecutorService;
import javax.naming.InitialContext;

import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.model.DbType;
import com.ibm.fhir.persistence.ResourceEraseRecord;
import com.ibm.fhir.persistence.erase.EraseDTO;
import com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCCache;
import com.ibm.fhir.persistence.jdbc.connection.FHIRDbFlavor;
import com.ibm.fhir.persistence.jdbc.dao.api.IResourceReferenceDAO;
import com.ibm.fhir.persistence.jdbc.dao.impl.ResourceDAOImpl;

/**
 * EraseDAO is the data access layer of the erase operation which executes directly
 * against the database using SQL statements to:
 * <li>check the resource exists</li>
 * <li>delete all versions from the resource table</li>
 * <li>delete all parameters</li>
 * <li>delete entries from global table</li>
 * <li>delete resource specific logical resource entry</li>
 */
public class EraseResourceDAO extends ResourceDAOImpl implements Callable<ResourceEraseRecord> {

    private static final String CLASSNAME = EraseResourceDAO.class.getSimpleName();
    private static final Logger LOG = Logger.getLogger(CLASSNAME);

    // Arbitrarily long meaning the executor gets to wait up to 10 days, which means the transaction
    // timeout will dominate. When we have it set, we'll defer to the set timeout.
    private static final long DEFAULT_TIMEOUT = TimeUnit.DAYS.toMillis(10);

    private static final String CALL_POSTGRES = "{CALL %s.ERASE_RESOURCE(?, ?, ?, ?)}";
    private static final String CALL_DB2 = "CALL %s.ERASE_RESOURCE(?, ?, ?, ?)";

    // The translator specific to the database type we're working with
    private final IDatabaseTranslator translator;

    private ResourceEraseRecord eraseRecord;
    private EraseDTO eraseDto;
    private Connection conn;
    private CallableStatement callPostgres;
    private CallableStatement callDb2;

    /**
     * Public constructor
     *
     * @param connection
     * @param translator
     * @param schemaName
     * @param flavor
     * @param cache
     * @param rrd
     */
    public EraseResourceDAO(Connection conn, IDatabaseTranslator translator, String schemaName, FHIRDbFlavor flavor, FHIRPersistenceJDBCCache cache,
            IResourceReferenceDAO rrd) {
        super(conn, schemaName, flavor, cache, rrd);
        this.translator = translator;
        this.conn = getConnection();
    }

    @Override
    public ResourceEraseRecord call() throws Exception {
        if (DbType.DB2.equals(getFlavor().getType()) && eraseDto.getVersion() == null) {
            runCallableStatement(callDb2);
        } else if (DbType.POSTGRESQL.equals(getFlavor().getType()) && eraseDto.getVersion() == null) {
            runCallableStatement(callPostgres);
        } else {
            // Uses the Native Java to execute a Resource Erase
            runInDao();
        }
        return eraseRecord;
    }

    /**
     * Execute the stored procedure/function to erase the content.
     *
     * @param callStr
     * @throws Exception
     */
    private void runCallableStatement(CallableStatement call) throws Exception {
        try {
            call.setString(1, eraseDto.getResourceType());
            call.setString(2, eraseDto.getLogicalId());
            call.setNull(3, eraseDto.getCount());
            call.registerOutParameter(4, Types.BIGINT);
            call.setQueryTimeout(1);

            call.execute();

            Integer deleted = (int) call.getLong(4);
            if (LOG.isLoggable(Level.FINEST)) {
                LOG.finest("Deleted from [" + eraseDto.getResourceType() + "/" + eraseDto.getLogicalId() + "] deleted [" + deleted + "]");
            }

            eraseRecord.setTotal(deleted);
            eraseRecord.setPartial(false);

            ResourceEraseRecord.Status status = ResourceEraseRecord.Status.DONE;
            if (deleted == -1) {
                status = ResourceEraseRecord.Status.NOT_FOUND;
            }
            eraseRecord.setStatus(status);
        } catch (SQLException x) {
            LOG.log(Level.SEVERE, "SQL Call to Erase Failed to execute", x);
            throw translator.translate(x);
        } finally {
            if (call != null) {
                call.close();
            }
        }
    }

    /**
     * Executes the SQL logic as part of the dao rather than
     *
     * @throws SQLException
     */
    public void runInDao() throws SQLException {
        String resourceType = eraseDto.getResourceType();
        String logicalId = eraseDto.getLogicalId();

        long resourceTypeId = -1;
        long logicalResourceId = -1;
        long resourceId = -1;
        int version = -1;
        Integer total = 0;

        Connection conn = getConnection();

        // Prep 1: Get the v_resource_type_id
        final String GET_RESOURCE_TYPE_ID =
                "SELECT resource_type_id" +
                        "  FROM resource_types" +
                        "  WHERE resource_type = ?";
        try (PreparedStatement stmt = conn.prepareStatement(GET_RESOURCE_TYPE_ID)) {
            stmt.setString(1, resourceType);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                resourceTypeId = rs.getLong(1);
            }
        } catch (SQLException x) {
            LOG.log(Level.SEVERE, GET_RESOURCE_TYPE_ID, x);
            throw translator.translate(x);
        }

        // Prep 2: Get the logical from the system-wide logical resource level
        final String GET_LOGICAL_RESOURCES_SYSTEM =
                "SELECT logical_resource_id " +
                        "  FROM logical_resources" +
                        "  WHERE resource_type_id = ? AND logical_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(GET_LOGICAL_RESOURCES_SYSTEM)) {
            stmt.setLong(1, resourceTypeId);
            stmt.setString(2, logicalId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                logicalResourceId = rs.getLong(1);
            }
        } catch (SQLException x) {
            LOG.log(Level.SEVERE, GET_RESOURCE_TYPE_ID, x);
            throw translator.translate(x);
        }

        /// Not found, return and stop processing.
        if (resourceTypeId == -1) {
            eraseRecord.setStatus(ResourceEraseRecord.Status.NOT_FOUND);
            return;
        }

        // Step 1: Get the Details for the Resource/Logical_Resource
        // the resource_id and version_id need to be fetched.
        // these should never be null since we have a lock, and the resource exists.
        final String RESOURCE_LOGICAL_DETAILS =
                "SELECT R1.RESOURCE_ID, R1.VERSION_ID" +
                        "    FROM " + resourceType + "_RESOURCES R1" +
                        "    WHERE R1.LOGICAL_RESOURCE_ID = ?" +
                        "    ORDER BY R1.VERSION_ID DESC";
        try (PreparedStatement stmt = conn.prepareStatement(RESOURCE_LOGICAL_DETAILS)) {
            stmt.setLong(1, logicalResourceId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                resourceId = rs.getLong(1);
                version = rs.getInt(2);
            }
        } catch (SQLException x) {
            LOG.log(Level.SEVERE, RESOURCE_LOGICAL_DETAILS, x);
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

        if (eraseDto.getVersion() != null && eraseDto.getVersion() == version) {
            // The requested version to erase is equal the actual version
            eraseRecord.setStatus(ResourceEraseRecord.Status.NOT_SUPPORTED_LATEST);
            return;
        }

        if (eraseDto.getVersion() != null) {
            // Update the specific version's PAYLOAD by updating the resource
            final String UPDATE_RESOURCE_PAYLOAD =
                    "UPDATE " + resourceType + "_RESOURCES R1" +
                            "    SET R1.DATA = NULL" +
                            "    WHERE R1.LOGICAL_RESOURCE_ID = ? AND  R1.VERSION_ID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(UPDATE_RESOURCE_PAYLOAD)) {
                stmt.setLong(1, logicalResourceId);
                stmt.setInt(2, eraseDto.getVersion());
                int count = stmt.executeUpdate();
                total += count;
            } catch (SQLException x) {
                LOG.log(Level.SEVERE, UPDATE_RESOURCE_PAYLOAD, x);
                throw translator.translate(x);
            }
            eraseRecord.setTotal(total);
            eraseRecord.setStatus(ResourceEraseRecord.Status.VERSION);
            return;
        }
        // Step 2: Delete All Versions from Resources Table
        // Create the prepared statement to delete Resource Versions in chunks
        // Implementation note: fetch must be the last part of the sub-select
        final String DELETE_ALL_VERSIONS =
                "DELETE FROM " + resourceType + "_RESOURCES WHERE RESOURCE_ID IN (" +
                        "    SELECT R1.RESOURCE_ID FROM  " + resourceType + "_RESOURCES R1" +
                        "    WHERE R1.LOGICAL_RESOURCE_ID = ? AND  R1.VERSION_ID <= ?" +
                        "        FETCH FIRST 1000 ROWS ONLY)";
        try (PreparedStatement stmt = conn.prepareStatement(DELETE_ALL_VERSIONS)) {
            int count = 1;
            while (count > 0) {
                stmt.setLong(1, logicalResourceId);
                stmt.setInt(2, version);
                count = stmt.executeUpdate();
                total += count;
            }
        } catch (SQLException x) {
            LOG.log(Level.SEVERE, DELETE_ALL_VERSIONS, x);
            throw translator.translate(x);
        }

        deleteFromAllParametersTables(conn, resourceType, logicalResourceId);

        // Step 4: Delete from Logical Resources table
        final String DELETE_LOGICAL_RESOURCE =
                "DELETE FROM " + resourceType + "_LOGICAL_RESOURCES WHERE LOGICAL_RESOURCE_ID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(DELETE_LOGICAL_RESOURCE)) {
            stmt.setLong(1, logicalResourceId);
            int count = stmt.executeUpdate();
            LOG.fine(() -> "Count of Resource_LR deleted is " + count);
        } catch (SQLException x) {
            LOG.log(Level.SEVERE, DELETE_LOGICAL_RESOURCE, x);
            throw translator.translate(x);
        }

        // Step 5: Delete from Global Logical Resources
        final String GLOBAL_DELETE_LOGICAL_RESOURCE =
                "DELETE FROM LOGICAL_RESOURCES WHERE LOGICAL_RESOURCE_ID = ? AND RESOURCE_TYPE_ID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(GLOBAL_DELETE_LOGICAL_RESOURCE)) {
            stmt.setLong(1, logicalResourceId);
            stmt.setLong(2, resourceTypeId);
            int count = stmt.executeUpdate();
            LOG.fine(() -> "Count of LR deleted is " + count);
        } catch (SQLException x) {
            LOG.log(Level.SEVERE, GLOBAL_DELETE_LOGICAL_RESOURCE, x);
            throw translator.translate(x);
        }

        // Step 6: Delete from resource_change_log
        final String CL_DELETE =
                "DELETE FROM resource_change_log WHERE LOGICAL_RESOURCE_ID = ? AND RESOURCE_TYPE_ID = ?"
                        + " AND RESOURCE_ID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(CL_DELETE)) {
            stmt.setLong(1, logicalResourceId);
            stmt.setLong(2, resourceTypeId);
            stmt.setLong(3, resourceId);
            int count = stmt.executeUpdate();
            LOG.fine(() -> "Count of resource_change_log deleted is " + count);
        } catch (SQLException x) {
            LOG.log(Level.SEVERE, CL_DELETE, x);
            throw translator.translate(x);
        }

        eraseRecord.setTotal(total);
        eraseRecord.setPartial(false);
        eraseRecord.setStatus(ResourceEraseRecord.Status.DONE);
    }

    /**
     * Deletes from the Parameters
     *
     * @param conn
     * @param tablePrefix
     * @param logicalResourceId
     * @throws SQLException
     */
    public void deleteFromAllParametersTables(Connection conn, String tablePrefix, long logicalResourceId) throws SQLException {
        final String method = "deleteFromAllParametersTables";
        LOG.entering(CLASSNAME, method);

        // existing resource, so need to delete all its parameters
        deleteFromParameterTable(conn, tablePrefix + "_str_values", logicalResourceId);
        deleteFromParameterTable(conn, tablePrefix + "_number_values", logicalResourceId);
        deleteFromParameterTable(conn, tablePrefix + "_date_values", logicalResourceId);
        deleteFromParameterTable(conn, tablePrefix + "_latlng_values", logicalResourceId);
        deleteFromParameterTable(conn, tablePrefix + "_resource_token_refs", logicalResourceId);
        deleteFromParameterTable(conn, tablePrefix + "_quantity_values", logicalResourceId);
        deleteFromParameterTable(conn, "str_values", logicalResourceId);
        deleteFromParameterTable(conn, "date_values", logicalResourceId);
        deleteFromParameterTable(conn, "resource_token_refs", logicalResourceId);
        LOG.exiting(CLASSNAME, method);
    }

    /**
     * Delete all parameters for the given logical resource id from the parameters table
     *
     * @param conn
     * @param tableName
     * @param logicalResourceId
     * @throws SQLException
     */
    public void deleteFromParameterTable(Connection conn, String tableName, long logicalResourceId) throws SQLException {
        final String DML = "DELETE FROM " + tableName + " WHERE logical_resource_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(DML)) {
            // bind parameters
            stmt.setLong(1, logicalResourceId);
            int deleted = stmt.executeUpdate();
            if (LOG.isLoggable(Level.FINEST)) {
                LOG.finest("Deleted from [" + tableName + "] deleted [" + deleted + "] for logicalResourceId [" + logicalResourceId + "]");
            }
        } catch (SQLException x) {
            LOG.log(Level.SEVERE, DML, x);
            throw translator.translate(x);
        }
    }

    /**
     * processes the erase
     *
     * @param eraseRecord
     *            the output
     * @param eraseDto
     *            the input
     * @throws Exception
     */
    public void erase(ResourceEraseRecord eraseRecord, EraseDTO eraseDto) throws Exception {
        this.eraseRecord = eraseRecord;
        this.eraseDto = eraseDto;


        if (DbType.DB2.equals(getFlavor().getType())) {
            callDb2 = conn.prepareCall(String.format(CALL_DB2, getSchemaName()));
        } else if (DbType.POSTGRESQL.equals(getFlavor().getType())) {
            callPostgres = conn.prepareCall(String.format(CALL_POSTGRES, getSchemaName()));
        }

        // We can use this pattern to work across multiple threads
        // Warning, please do not operate on the same connection.
        InitialContext ctx = new InitialContext();
        ManagedExecutorService svc = (ManagedExecutorService) ctx.lookup("concurrent/fhir-erase");

        Future<ResourceEraseRecord> future = svc.submit(this);

        // Manage the Timeout
        long timeout = DEFAULT_TIMEOUT;
        if (eraseDto.getTimeout() != null) {
            timeout = TimeUnit.SECONDS.toMillis(eraseDto.getTimeout());
        }

        // Wait for the execution is done.
        for (int i = 0; i < timeout; i += 10) {
            if (LOG.isLoggable(Level.FINEST)) {
                LOG.finest("Waiting on task to finish [" + i + "]");
            }

            Thread.sleep(10);
            if (future.isDone() || future.isCancelled()) {
                LOG.finest(() -> "Future is Done - [" + eraseDto.getResourceType() + "]");
                break;
            }
        }
    }
}