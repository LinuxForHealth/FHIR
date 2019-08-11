/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.derby;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Logger;

import com.ibm.watsonhealth.database.utils.derby.DerbyTranslator;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api.CodeSystemDAO;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api.FhirRefSequenceDAO;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api.FhirSequenceDAO;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api.ParameterNameDAO;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api.ParameterNormalizedDAO;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dao.impl.ParameterVisitorBatchDAO;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dao.impl.ParameterVisitorDAO;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dto.Parameter;

/**
CODE_REMOVED
 * to pass the parameter list into the stored procedure, but this approach
 * exposed some query optimizer issues in DB2 resulting in significant 
 * concurrency problems (related to dynamic statistics collection and
 * query compilation). The solution uses row type arrays instead, but these
 * aren't supported in Derby.
 * 
 * For Derby, we take a different tack. Rather than messing around with a 
 * (Java) Derby stored procedure, we can simply execute the necessary 
 * statements as a sequence of JDBC calls. This also simplifies debugging.
 * 
 * @author rarnold
 */
public class DerbyResourceDAO {
    private static final Logger logger = Logger.getLogger(DerbyResourceDAO.class.getName());
    private static final String CLASSNAME = DerbyResourceDAO.class.getSimpleName();
    
    private static final DerbyTranslator translator = new DerbyTranslator();
    
    private final ParameterNormalizedDAO parameterDAO;

    // DAO used to obtain sequence values from FHIR_SEQUENCE
    private final FhirSequenceDAO fhirSequenceDAO;

    // DAO used to obtain sequence values from FHIR_REF_SEQUENCE
    private final FhirRefSequenceDAO fhirRefSequenceDAO;

    // DAO used to manage parameter_names
    private final ParameterNameDAO parameterNameDAO;

    // DAO used to manage code_systems
    private final CodeSystemDAO codeSystemDAO;
    
    private final Connection conn;

    /**
     * public constructor
     * @param connection the database connection
     * @param parameterDAO the DAO/cache for obtaining parameter and code system ids
     */
    public DerbyResourceDAO(Connection connection, ParameterNormalizedDAO parameterDAO) {
        this.conn = connection;
        this.parameterDAO = parameterDAO;
        this.fhirSequenceDAO = new FhirSequenceDAOImpl(connection);
        this.fhirRefSequenceDAO = new FhirRefSequenceDAOImpl(connection);
        this.parameterNameDAO = new DerbyParameterNamesDAO(connection, fhirRefSequenceDAO);
        this.codeSystemDAO = new DerbyCodeSystemDAO(connection, fhirRefSequenceDAO);
    }

    /**
     * Store the resource in the database, creating a new logical_resource entry if this is
     * the first version of this resource, or creating a new resource entry if this a new
     * version of an existing logical resource. The logic tracks closely the DB2 stored
     * procedure implementation, including locking of the logical_resource and handling
     * concurrency issues using the standard insert-or-update pattern:
     *   SELECT FOR UPDATE                 -- try and get a write lock
     *   IF NOT FOUND THEN                 -- doesn't exist, so we don't have a lock
     *     INSERT new logical resource     -- create the record - if OK, we own the lock
     *     IF DUPLICATE THEN               -- someone else beat us to the create
     *       SELECT FOR UPDATE             -- so we need to try again for a write lock
     *     ...
     *   ...
     * This works because we never delete a logical_resource record, and so don't have to deal
     * with concurrency issues caused when deletes are mingled with inserts/updates
     * 
     * Note the execution flow aligns very closely with the DB2 stored procedure
     * implementation (fhir-persistence-schema/src/main/resources/add_any_resource.sql)
     * @param conn
     * @param tablePrefix
     * @param p_logical_id
     * @param p_payload
     * @param p_last_updated
     * @param p_is_deleted
     * @param p_source_key
     * @param p_tx_correlation_id
     * @param p_changed_by
     * @param p_correlation_token
     * @param p_tenant_id
     * @param p_reason
     * @param p_event
     * @param p_site_id
     * @param p_study_id
     * @param p_service_id
     * @param p_patient_id
     * @param p_version
     * @param p_json_version
     * @param p_write_rep_log
     * @param o_resource_id
     * @return the resource_id for the entry we created
     * @throws Exception
     */
    public long storeResource(String tablePrefix, List<Parameter> parameters, String p_logical_id, byte[] p_payload, Timestamp p_last_updated, boolean p_is_deleted, 
        String p_source_key, String p_tx_correlation_id, String p_changed_by, String p_correlation_token, String p_tenant_id, 
        String p_reason, String p_event, String p_site_id, String p_study_id, String p_service_id, 
        String p_patient_id, Integer p_version, Integer p_json_version, boolean p_write_rep_log) throws Exception {

        final String METHODNAME = "storeResource() for " + tablePrefix + " resource";
        logger.entering(CLASSNAME, METHODNAME);

        Long v_logical_resource_id = null;
        Long v_current_resource_id = null;
        Long v_resource_id = null;
        Integer v_resource_type_id = null;
        boolean v_new_resource = false;
        boolean v_not_found = false;
        boolean v_duplicate = false;
        int v_version = 0;
        int v_insert_version = 0;

        String v_resource_type = tablePrefix;
        
        // Map the resource type name to the normalized id value in the database
        v_resource_type_id = getResourceTypeId(v_resource_type);
        if (v_resource_type_id == null) {
            // programming error, as this should've been created earlier
            throw new IllegalStateException("resource type not found: " + v_resource_type);
        }

        // Get a lock at the logical resource level
        final String SELECT_FOR_UPDATE = "SELECT logical_resource_id, current_resource_id FROM " + tablePrefix + "_logical_resources WHERE logical_id = ? FOR UPDATE";
        try (PreparedStatement stmt = conn.prepareStatement(SELECT_FOR_UPDATE)) {
            stmt.setString(1, p_logical_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                v_logical_resource_id = rs.getLong(1);
                v_current_resource_id = rs.getLong(2);
            }
            else {
                v_not_found = true;
                v_logical_resource_id = -1L; // just to be careful
            }
        }

        // Create the logical resource if we don't have it already
        if (v_not_found) {
            // grab the id we want to use for the new logical resource instance 
            final String sql2 = "VALUES(NEXT VALUE FOR fhir_sequence)";
            try (PreparedStatement stmt = conn.prepareStatement(sql2)) {
                ResultSet res = stmt.executeQuery();
                if (res.next()) {
                    v_logical_resource_id = res.getLong(1);
                }
                else {
                    // not going to happen, unless someone butchers the statement being executed
                    throw new IllegalStateException("VALUES failed to return a row: " + sql2);
                }
            }

            try {
                // insert the logical resource record
                final String sql3 = "INSERT INTO "+ tablePrefix + "_logical_resources (logical_resource_id, logical_id) VALUES (?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql3)) {
                    // bind parameters
                    stmt.setLong(1, v_logical_resource_id);
                    stmt.setString(2, p_logical_id);
                    stmt.executeUpdate();
                }
            } catch (SQLException e) {
                if (translator.isDuplicate(e)) {
                    v_duplicate = true;
                }
                else {
                    throw e;
                }
            }

            /**
             * remember that we have a concurrent system...so there is a possibility
             * that another thread snuck in before us and created the logical resource. This
             * is easy to handle, just turn around and read it
             */
            if (v_duplicate) {
                try (PreparedStatement stmt = conn.prepareStatement(SELECT_FOR_UPDATE)) {
                    // bind parameters
                    stmt.setString(1, p_logical_id);
                    ResultSet res = stmt.executeQuery();
                    if (res.next()) {
                        v_logical_resource_id = res.getLong(1);
                        v_current_resource_id = res.getLong(2);
                    }
                    else {
                        // Extremely unlikely as we should never delete logical resource records
                        throw new IllegalStateException("Logical resource was deleted: " + p_logical_id);
                    }
                }
            } 
            else {
                v_new_resource = true;
            }
        }

        if (!v_new_resource) {
            //resource exists, so if we are storing a specific version, do a quick check to make
            //sure that this version doesn't currently exist. This is only done when processing
            // replication messages which might be duplicated. We want the operation to be idempotent,
            // so if the resource already exists, we don't need to do anything else.

            if (p_version != null) {
                final String sqlStmt = "SELECT resource_id FROM " + tablePrefix + "_resources dr WHERE dr.logical_resource_id = ? AND dr.version_id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sqlStmt)) {
                    // bind parameters
                    stmt.setLong(1, v_logical_resource_id);
                    stmt.setLong(2, p_version);
                    ResultSet res = stmt.executeQuery();
                    if (res.next()) {
                        // this version of this resource already exists, so we bail out right away
                        v_resource_id = res.getLong(1);
                        return v_resource_id;
                    }
                }

            }

            // Grab the version value for the current version (identified by v_current_resource_id)
            final String sql4 = "SELECT version_id FROM " + tablePrefix + "_resources WHERE resource_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql4)) {
                stmt.setLong(1, v_current_resource_id);
                ResultSet res = stmt.executeQuery();
                if (res.next()) {
                    v_version = res.getInt(1);
                }
                else {
                    throw new IllegalStateException("current resource not found: " 
                            + tablePrefix + "_resources.resource_id=" + v_current_resource_id);
                }
            }

            //If we have been passed a version number, this means that this is a replicated
            //resource, and so we only need to delete parameters if the given version is 
            // later than the current version
            if (p_version == null || p_version > v_version) {
                // existing resource, so need to delete all its parameters
                deleteFromParameterTable(conn, tablePrefix + "_str_values", v_current_resource_id);
                deleteFromParameterTable(conn, tablePrefix + "_number_values", v_current_resource_id);
                deleteFromParameterTable(conn, tablePrefix + "_date_values", v_current_resource_id);
                deleteFromParameterTable(conn, tablePrefix + "_latlng_values", v_current_resource_id);
                deleteFromParameterTable(conn, tablePrefix + "_token_values", v_current_resource_id);
                deleteFromParameterTable(conn, tablePrefix + "_quantity_values", v_current_resource_id);
            }
        }
        
        // Persist the data using the given version number if required
        if (p_version != null) {
            v_insert_version = p_version;
        } 
        else {
            // remember we have a write (update) lock on the logical version, so we can safely calculate
            // the next version value here
            v_insert_version = v_version + 1;
            
            // Check the version number we're going to use matches the version
            // number injected by the FHIR server into the JSON payload
            if (v_insert_version != p_json_version) {
                throw new SQLException("Concurrent update - mismatch of version in JSON", "99001");
            }
        }


        /**
         * Create the new resource version.
         * Alpha version uses last_updated time from the app-server, so we keep that here
         */
        String sql2 = "VALUES (NEXT VALUE FOR fhir_sequence)";
        try (PreparedStatement stmt = conn.prepareStatement(sql2)) {
            ResultSet res = stmt.executeQuery();
            if (res.next()) {
                v_resource_id = res.getLong(1); //Assign result of the above query
            } 
            else {
                // unlikely
                throw new IllegalStateException("no row returned: " + sql2);
            }
        }

        // Finally we get to the big resource data insert
        String sql3 = "INSERT INTO " + tablePrefix + "_resources (resource_id, logical_resource_id, version_id, data, last_updated, is_deleted, "
                + "tx_correlation_id, changed_by, correlation_token, tenant_id, reason, site_id, study_id, service_id, patient_id) "
                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql3)) {
            // bind parameters
            stmt.setLong(1, v_resource_id);
            stmt.setLong(2, v_logical_resource_id);
            stmt.setInt(3, v_insert_version);
            stmt.setBytes(4, p_payload);
            stmt.setTimestamp(5, p_last_updated);
            stmt.setString(6, p_is_deleted ? "Y" : "N");
            stmt.setString(7,p_tx_correlation_id);
            stmt.setString(8, p_changed_by);
            stmt.setString(9, p_correlation_token);
            stmt.setString(10, p_tenant_id);
            stmt.setString(11, p_reason);
            stmt.setString(12, p_site_id);
            stmt.setString(13, p_study_id);
            stmt.setString(14, p_service_id);
            stmt.setString(15, p_patient_id);
            stmt.executeUpdate();
        }

        if (p_version == null || p_version > v_version) {
            //only update the logical resource if the resource we are adding supercedes the
            //current resource
            String sql4 = "UPDATE " + tablePrefix + "_logical_resources SET current_resource_id = ? WHERE logical_resource_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql4)) {
                // bind parameters
                stmt.setLong(1, v_resource_id);
                stmt.setLong(2, v_logical_resource_id);
                stmt.executeUpdate();
            }

            // To keep things simple for the Derby use-case, we just use a visitor to
            // handle inserts of parameters directly in the resource parameter tables.
            // Note we don't get any parameters for the resource soft-delete operation
            if (parameters != null) {
                // Derby doesn't support partitioned multi-tenancy, so we disable it on the DAO:
                try (ParameterVisitorBatchDAO pvd = new ParameterVisitorBatchDAO(conn, null, tablePrefix, false, v_resource_id, 100, 
                    new ParameterNameCacheAdapter(parameterNameDAO), new CodeSystemCacheAdapter(codeSystemDAO))) {
                    for (Parameter p: parameters) {
                        p.visit(pvd);
                    }
                }
            }
        }

        /**
         * finally we insert a record into our replication log table. This also covers
         * us for Part 11 audit. The replicator process reads records from this table
         * and delivers them into kafka, then deleting the row once it is convinced
         * kafka has received the record. Don't do this if this is a replicated
         * resource (where we are given a p_version value)
         */
        if (p_version == null && p_write_rep_log) {
            String sql6 = "INSERT INTO fhir_replication_log (last_updated, resource_type_id, resource_id, previous_resource_id, version_id, is_deleted, source_key, tx_correlation_id, changed_by, correlation_token, tenant_id, reason, event,site_id, study_id, patient_id, service_id) VALUES (?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql6)) {
                // bind parameters
                stmt.setTimestamp(1, p_last_updated);
                stmt.setLong(2, v_resource_type_id);
                stmt.setLong(3, v_resource_id);
                stmt.setLong(4, v_current_resource_id);
                stmt.setInt(5, v_version + 1);
                stmt.setString(6, p_is_deleted ? "Y" : "N");
                stmt.setString(7, p_source_key);
                stmt.setString(8, p_tx_correlation_id);
                stmt.setString(9, p_changed_by);
                stmt.setString(10, p_correlation_token);
                stmt.setString(11, p_tenant_id);
                stmt.setString(12, p_reason);
                stmt.setString(13, p_event);
                stmt.setString(14, p_site_id);
                stmt.setString(15, p_study_id);
                stmt.setString(16, p_patient_id);
                stmt.setString(17, p_service_id);

                stmt.executeUpdate();
            }
        }

        logger.exiting(CLASSNAME, METHODNAME);
        return v_resource_id;
    }
    

    /**
     * Delete all parameters for the given resourceId from the parameters table
     * @param tableName
     * @param resourceId
     */
    protected void deleteFromParameterTable(Connection conn, String tableName, long resourceId) throws SQLException {
        String delStrValues = "DELETE FROM " + tableName + " WHERE resource_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(delStrValues)) {
            // bind parameters
            stmt.setLong(1, resourceId);
            stmt.executeUpdate();
        }
        
    }

    /**
     * Read the id for the named type
     * @param resourceTypeName
     * @return the database id, or null if the named record is not found
     * @throws SQLException
     */
    protected Integer getResourceTypeId(String resourceTypeName) throws SQLException {
        Integer result;
        
        String sql1 = "SELECT resource_type_id FROM resource_types WHERE resource_type = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql1)) {
            stmt.setString(1, resourceTypeName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            } 
            else {
                result = null;
            }
        }        
        
        return result;
    }


    /**
     * stored-procedure-less implementation for managing the resource_types table
     * @param resourceTypeName
     */
    public int getOrCreateResourceType(String resourceTypeName) throws SQLException {
        // As the system is concurrent, we have to handle cases where another thread
        // might create the entry after we selected and found nothing
        Integer result = getResourceTypeId(resourceTypeName);
         
        // Create the resource if we don't have it already (set by the continue handler)
        if (result == null) {
            try {
                result = fhirRefSequenceDAO.nextValue();
             
                String INS = "INSERT INTO resource_types (resource_type_id, resource_type) VALUES (?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(INS)) {
                    // bind parameters
                    stmt.setInt(1, result);
                    stmt.setString(2, resourceTypeName);
                    stmt.executeUpdate();
                }
            } catch (SQLException e) {
                if ("23505".equals(e.getSQLState())) {
                    // another thread snuck in and created the record, so we need to fetch the correct id
                    result = getResourceTypeId(resourceTypeName);
                }
            }

        }
        
        return result;
    }
}
