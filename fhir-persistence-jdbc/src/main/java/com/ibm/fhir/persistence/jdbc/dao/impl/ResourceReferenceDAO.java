/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.ibm.fhir.database.utils.api.DataAccessException;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.dao.api.ICommonTokenValuesCache;
import com.ibm.fhir.persistence.jdbc.dao.api.IResourceReferenceDAO;
import com.ibm.fhir.persistence.jdbc.dto.CommonTokenValue;
import com.ibm.fhir.schema.control.FhirSchemaConstants;

/**
 * DAO to handle maintenance of the local and external reference tables
 * which contain the relationships described by "reference" elements in
 * each resource (e.g. Observation.subject).
 * 
 * The DAO uses a cache for looking up the ids for various entities. The
 * DAO can create new entries, but these can only be used locally until
 * the transaction commits, at which point they can be consolidated into
 * the shared cache. This has the benefit that we reduce the number of times
 * we need to lock the global cache, because we only update it once per
 * transaction.
 * 
 * For improved performance, we also make use of batch statements which
 * are managed as member variables. This is why it's important to close
 * this DAO before the transaction commits, ensuring that any outstanding
 * DML batched but not yet executed is processed. Calling close does not
 * close the provided Connection. That is up to the caller to manage.
 * Close does close any statements which are opened inside the class.
 */
public class ResourceReferenceDAO implements IResourceReferenceDAO, AutoCloseable {
    private static final Logger logger = Logger.getLogger(ResourceReferenceDAO.class.getName());
    
    private final String schemaName;

    // hold on to the connection because we use batches to improve efficiency
    private final Connection connection;
    
    // The cache used to track the ids of the normalized entities we're managing
    private final ICommonTokenValuesCache cache;
    
    // The translator for the type of database we are connected to
    private final IDatabaseTranslator translator;
        
    // batch statement for inserting records int local_references
    private static final String INS_LOCAL_REF = "INSERT INTO local_references(parameter_name_id, logical_resource_id, ref_logical_resource_id) VALUES (?,?,?)";
    private PreparedStatement localReferencesBatch;
    private int localReferencesBatchCount = 0;

    // batch statement for inserting records into external_systems
    private static final String INS_EXT_SYS = "INSERT INTO external_systems (external_system_name) VALUES (?) RETURNING external_system_id";
    private PreparedStatement externalSystemsBatch;
    private int externalSystemsBatchCount = 0;
    
    // batch statement for inserting records into external_references
    private static final String INS_EXT_REF = "INSERT INTO external_references (parameter_name_id, external_system_id, external_reference_value_id, logical_resource_id) VALUES (?,?,?,?)";
    private PreparedStatement externalReferencesBatch;
    private int externalReferencesBatchCount = 0;

    // batch statement for inserting records into external_reference_values
    private static final String INS_EXT_REF_VALUE = "INSERT INTO external_reference_values (external_reference_value) VALUES (?) RETURNING external_reference_value_id";
    private PreparedStatement externalReferenceValuesBatch;
    private int externalReferenceValuesBatchCount = 0;

    // batch statement for inserting records into logical_resource_compartments
    private static final String INS_COMPARTMENT = "INSERT INTO logical_resource_compartments(compartment_name_id, logical_resource_id, last_updated, compartment_logical_resource_id) "
            + "VALUES (?, ?, ?, ?)";
    private PreparedStatement logicalResourceCompartmentsBatch;
    private int logicalResourceCompartmentsBatchCount = 0;
    
    private final ReferencesSequenceDAO referencesSequenceDAO;
    
    // The number of operations we allow before submitting a batch
    private static final int BATCH_SIZE = 100;
    
    /**
     * Public constructor
     * @param c
     */
    public ResourceReferenceDAO(IDatabaseTranslator t, Connection c, String schemaName, ICommonTokenValuesCache cache) {
        this.translator = t;
        this.connection = c;
        this.cache = cache;
        this.schemaName = schemaName;
        this.referencesSequenceDAO = new ReferencesSequenceDAO(c, schemaName, t);
    }

    @Override
    public void flush() throws FHIRPersistenceException {
        try {
            if (localReferencesBatchCount > 0) {
                localReferencesBatch.executeBatch();
                localReferencesBatchCount = 0;
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, INS_LOCAL_REF, x);
            throw translator.translate(x);
        }
    }

    @Override
    public void close() throws FHIRPersistenceException {
        flush();
    }

    @Override
    public ICommonTokenValuesCache getResourceReferenceCache() {
        return this.cache;
    }

    /**
     * Look up the database id for the given externalSystemName
     * @param externalSystemName
     * @return the database id, or null if no record exists
     */
    public Integer queryExternalSystemId(String externalSystemName) {
        Integer result;
        
        final String SQL = "SELECT external_system_id FROM external_systems where external_system_name = ?";

        try (PreparedStatement ps = connection.prepareStatement(SQL)) {
            ps.setString(1, externalSystemName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            } else {
                result = null;
            }
        } catch (SQLException x) {
            // make the exception a little bit more meaningful knowing the database type
            throw translator.translate(x);
        }
        
        return result;
    }
    
    /**
     * Find the database id for the given externalReferenceValue
     * @param externalReferenceValue
     * @return
     */
    public Integer queryExternalReferenceValueId(String externalReferenceValue) {
        Integer result;
        
        final String SQL = "SELECT external_reference_value_id FROM external_reference_values WHERE external_reference_value = ?";
        try (PreparedStatement ps = connection.prepareStatement(SQL)) {
            ps.setString(1, externalReferenceValue);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            } else {
                result = null;
            }
        } catch (SQLException x) {
            // make the exception a little bit more meaningful knowing the database type
            logger.log(Level.SEVERE, SQL, x);
            throw translator.translate(x);
        }
        
        return result;
    }
    
    /**
     * Get a list of matching records from external_reference_values. Cheaper to do as one
     * query instead of individuals
     * @param externalReferenceValue
     * @return
     */
    public List<ExternalReferenceValue> queryExternalReferenceValues(String... externalReferenceValues) {
        List<ExternalReferenceValue> result = new ArrayList<>();
        if (externalReferenceValues.length == 0) {
            throw new IllegalArgumentException("externalReferenceValues array cannot be empty");
        }

        final StringBuilder sql = new StringBuilder();
        sql.append("SELECT external_reference_value_id, external_reference_value FROM external_reference_values WHERE external_reference_value IN (");
        
        for (int i=0; i<externalReferenceValues.length; i++) {
            if (i == 0) {
                sql.append("?");
            } else {
                sql.append(",?");
            }
        }
        sql.append(")");
        
        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int a = 1;
            for (String xrv: externalReferenceValues) {
                ps.setString(a++, xrv);
            }
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(new ExternalReferenceValue(rs.getLong(1), rs.getString(2)));
            }
        } catch (SQLException x) {
            // make the exception a little bit more meaningful knowing the database type
            logger.log(Level.SEVERE, sql.toString(), x);
            throw translator.translate(x);
        }
        
        return result;
    }
    
    public List<ExternalSystem> queryExternalSystems(String... externalSystemNames) {
        List<ExternalSystem> result = new ArrayList<>();
        if (externalSystemNames.length == 0) {
            throw new IllegalArgumentException("externalReferenceValues array cannot be empty");
        }

        final StringBuilder sql = new StringBuilder();
        sql.append("SELECT external_system_id, external_system_name FROM external_systems WHERE external_system_name IN (");
        
        for (int i=0; i<externalSystemNames.length; i++) {
            if (i == 0) {
                sql.append("?");
            } else {
                sql.append(",?");
            }
        }
        sql.append(")");
        
        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int a = 1;
            for (String xrv: externalSystemNames) {
                ps.setString(a++, xrv);
            }
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(new ExternalSystem(rs.getLong(1), rs.getString(2)));
            }
        } catch (SQLException x) {
            // make the exception a little bit more meaningful knowing the database type
            logger.log(Level.SEVERE, sql.toString(), x);
            throw translator.translate(x);
        }
        
        return result;
    }

    @Override
    public void deleteExternalReferences(int resourceTypeId, String logicalId) {
        final String DML = "DELETE FROM external_references "
                + "WHERE logical_resource_id IN ( "
                + " SELECT logical_resource_id FROM logical_resources "
                + "  WHERE resource_type_id = ? "
                + "    AND logical_id = ?)";
        
        try (PreparedStatement ps = connection.prepareStatement(DML)) {
            ps.setInt(1, resourceTypeId);
            ps.setString(2, logicalId);
            ps.executeUpdate();
        } catch (SQLException x) {
            // make the exception a little bit more meaningful knowing the database type
            logger.log(Level.SEVERE, DML, x);
            throw translator.translate(x);
        }

    }

    @Override
    public void deleteLocalReferences(long logicalResourceId) {
        final String DML = "DELETE FROM local_references WHERE logical_resource_id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(DML)) {
            ps.setLong(1, logicalResourceId);
            ps.executeUpdate();
        } catch (SQLException x) {
            // make the exception a little bit more meaningful knowing the database type
            logger.log(Level.SEVERE, DML, x);
            throw translator.translate(x);
        }
    }

    @Override
    public void deleteLogicalResourceCompartments(long logicalResourceId) {
        final String DML = "DELETE FROM logical_resource_compartments WHERE logical_resource_id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(DML)) {
            ps.setLong(1, logicalResourceId);
            ps.executeUpdate();
        } catch (SQLException x) {
            // make the exception a little bit more meaningful knowing the database type
            logger.log(Level.SEVERE, DML, x);
            throw translator.translate(x);
        }
    }

    @Override
    public void addCommonTokenValues(String resourceType, Collection<ResourceTokenValueRec> xrefs) {
        // Grab the ids for all the code-systems, and upsert any misses
        List<ResourceTokenValueRec> systemMisses = new ArrayList<>();
        cache.resolveCodeSystems(xrefs, systemMisses);
        upsertCodeSystems(systemMisses);

        // Now that all the code-systems ids are known, we can search the cache
        // for all the token values, upserting anything new
        List<ResourceTokenValueRec> valueMisses = new ArrayList<>();
        cache.resolveTokenValues(xrefs, valueMisses);
        upsertCommonTokenValues(valueMisses);
        
        // Now all the values should have ids assigned so we can go ahead and insert them
        // as a batch
        final String tableName = resourceType + "_TOKEN_VALUES_MAP";
        DataDefinitionUtil.assertValidName(tableName);
        final String insert = "INSERT INTO " + tableName + "("
                + "logical_resource_id, common_token_value_id) "
                + "VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(insert)) {
            int count = 0;
            for (ResourceTokenValueRec xr: xrefs) {
                ps.setLong(1, xr.getLogicalResourceId());
                ps.setLong(2, xr.getCommonTokenValueId());
                ps.addBatch();
                if (++count == BATCH_SIZE) {
                    ps.executeBatch();
                    count = 0;
                }
            }
            
            if (count > 0) {
                ps.executeBatch();
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, insert, x);
            throw translator.translate(x);
        }
    }
    
    /**
     * Add all the systems we currently don't have in the database. If all target
     * databases handled MERGE properly this would be easy, but they don't so
     * we go old-school with a negative outer join instead (which is pretty much
     * what MERGE does behind the scenes anyway).
     * INSERT INTO fhirdata.external_systems (external_system_name)
      SELECT v.name FROM fhirdata.external_systems s
  LEFT OUTER JOIN
      (VALUES ('hello'), ('world')) AS v(name)
           ON (s.external_system_name = v.name)
        WHERE s.external_system_name IS NULL;
     * @param systems
     */
    public void upsertCodeSystems(List<ResourceTokenValueRec> systems) {
        if (systems.isEmpty()) {
            return;
        }
        
        // Unique list so we don't try and create the same name more than once
        Set<String> systemNames = systems.stream().map(xr -> xr.getCodeSystemValue()).collect(Collectors.toSet());
        StringBuilder paramList = new StringBuilder();
        for (int i=0; i<systemNames.size(); i++) {
            if (paramList.length() > 0) {
                paramList.append(", ");
            }
            paramList.append("CAST(? AS VARCHAR(" + FhirSchemaConstants.MAX_SEARCH_STRING_BYTES + "))");
        }
        
        // query is a negative outer join so we only pick the rows where
        // the row "s" from the actual table doesn't exist.
        final String nextVal = translator.nextValue(schemaName, "fhir_sequence");
       StringBuilder insert = new StringBuilder();
        insert.append("INSERT INTO code_systems (code_system_id, code_system_name) ");
        insert.append("     SELECT ");
        insert.append(nextVal).append(", v.name ");
        insert.append(" FROM ");
        insert.append("     (VALUES ").append(paramList).append(" ) AS v(name) ");
        insert.append(" LEFT OUTER JOIN code_systems s ");
        insert.append("              ON s.code_system_name = v.name ");
        insert.append("           WHERE s.code_system_name IS NULL");
        
        // Note, we use PreparedStatement here on purpose. Partly because it's
        // secure coding best practice, but also because many resources will have the
        // same number of parameters, and hopefully we'll therefore share a small subset
        // of statements for better performance. Although once the cache warms up, this
        // shouldn't be called at all.
        StringBuilder inList = new StringBuilder(); // for the select query later
        try (PreparedStatement ps = connection.prepareStatement(insert.toString())) {
            // bind all the external_system_name values as parameters
            int a = 1;
            for (String name: systemNames) {
                ps.setString(a++, name);
                
                if (inList.length() > 0) {
                    inList.append(",");
                }
                inList.append("?");
            }
            
            ps.executeUpdate();
        } catch (SQLException x) {
            logger.log(Level.SEVERE, insert.toString(), x);
            throw translator.translate(x);
        }
        
        // Now grab the ids for the rows we just created. If we had a RETURNING implementation
        // which worked reliably across all our database platforms, we wouldn't need this
        // second query.
        StringBuilder select = new StringBuilder();
        select.append("SELECT code_system_name, code_system_id FROM code_systems WHERE code_system_name IN (");
        select.append(inList);
        select.append(")");
        
        Map<String, Integer> idMap = new HashMap<>();
        try (PreparedStatement ps = connection.prepareStatement(select.toString())) {
            // load a map with all the ids we need which we can then use to update the
            // ExternalResourceReferenceRec objects
            int a = 1;
            for (String name: systemNames) {
                ps.setString(a++, name);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                idMap.put(rs.getString(1), rs.getInt(2));
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, select.toString(), x);
            throw translator.translate(x);
        }
        
        // Now update the ids for all the matching systems in our list
        for (ResourceTokenValueRec xr: systems) {
            Integer id = idMap.get(xr.getCodeSystemValue());
            if (id != null) {
                xr.setCodeSystemValueId(id);

                // Add this value to the (thread-local) cache
                cache.addCodeSystem(xr.getCodeSystemValue(), id);
            } else {
                // Unlikely...but need to handle just in case
                logger.severe("Record for code_system_name '" + xr.getCodeSystemValue() + "' inserted but not found");
                throw new IllegalStateException("id deleted from database!");
            }
        }
    }
    
    /**
     * Add reference value records for each unique reference name in the given list
     * @param values
     */
    public void upsertCommonTokenValues(List<ResourceTokenValueRec> values) {
        if (values.isEmpty()) {
            return;
        }
        
        // Unique list so we don't try and create the same name more than once
        Set<CommonTokenValue> tokenValues = values.stream().map(xr -> new CommonTokenValue(xr.getParameterNameId(), xr.getCodeSystemValueId(), xr.getTokenValue())).collect(Collectors.toSet());

        // Build a string of parameter values we use in the query to drive the insert statement.
        // The database needs to know the type when it parses the query, hence the slightly verbose CAST functions:
        // VALUES ((CAST(? AS VARCHAR(1234)), CAST(? AS INT), CAST(? AS INT)), (...)) AS V(common_token_value, parameter_name_id, code_system_id)
        StringBuilder inList = new StringBuilder(); // for the select query later
        StringBuilder paramList = new StringBuilder();
        for (int i=0; i<tokenValues.size(); i++) {
            if (paramList.length() > 0) {
                paramList.append(", ");
            }
            paramList.append("(CAST(? AS VARCHAR(" + FhirSchemaConstants.MAX_TOKEN_VALUE_BYTES + "))");
            paramList.append(",CAST(? AS INT), CAST(? AS INT))");
            
            // also build the inList for the select statement later
            if (inList.length() > 0) {
                inList.append(",");
            }
            inList.append("(?,?,?)");
        }
        
        // query is a negative outer join so we only pick the rows where
        // the row "s" from the actual table doesn't exist.
        StringBuilder insert = new StringBuilder();
        insert.append("INSERT INTO common_token_values (parameter_name_id, code_system_id, token_value) ");
        insert.append("     SELECT v.parameter_name_id, v.code_system_id, v.token_value FROM ");
        insert.append("     (VALUES ").append(paramList).append(" ) AS v(token_value, parameter_name_id, code_system_id) ");
        insert.append(" LEFT OUTER JOIN common_token_values ctv ");
        insert.append("              ON ctv.token_value = v.token_value ");
        insert.append("             AND ctv.code_system_id = v.code_system_id ");
        insert.append("             AND ctv.parameter_name_id = v.parameter_name_id ");
        insert.append("      WHERE ctv.token_value IS NULL");
        
        // Note, we use PreparedStatement here on purpose. Partly because it's
        // secure coding best practice, but also because many resources will have the
        // same number of parameters, and hopefully we'll therefore share a small subset
        // of statements for better performance. Although once the cache warms up, this
        // shouldn't be called at all.
        try (PreparedStatement ps = connection.prepareStatement(insert.toString())) {
            // bind all the name values as parameters
            int a = 1;
            for (CommonTokenValue tv: tokenValues) {
                ps.setString(a++, tv.getTokenValue());
                ps.setInt(a++, tv.getParameterNameId());
                ps.setInt(a++, tv.getCodeSystemId());
            }
            
            ps.executeUpdate();
        } catch (SQLException x) {
            logger.log(Level.SEVERE, insert.toString(), x);
            throw translator.translate(x);
        }
        
        
        // Now grab the ids for the rows we just created. If we had a RETURNING implementation
        // which worked reliably across all our database platforms, we wouldn't need this
        // second query.
        // Derby doesn't support IN LISTS with multiple members, so we have to join against
        // a VALUES again. No big deal...probably similar amount of work for the database
        StringBuilder select = new StringBuilder();
        select.append("     SELECT ctv.parameter_name_id, ctv.code_system_id, ctv.token_value, ctv.common_token_value_id FROM ");
        select.append("     (VALUES ").append(paramList).append(" ) AS v(token_value, parameter_name_id, code_system_id) ");
        select.append("       JOIN common_token_values ctv ");
        select.append("              ON ctv.token_value = v.token_value ");
        select.append("             AND ctv.code_system_id = v.code_system_id ");
        select.append("             AND ctv.parameter_name_id = v.parameter_name_id ");
        
        // Grab the ids
        Map<CommonTokenValue, Long> idMap = new HashMap<>();
        try (PreparedStatement ps = connection.prepareStatement(select.toString())) {
            int a = 1;
            for (CommonTokenValue tv: tokenValues) {
                ps.setString(a++, tv.getTokenValue());
                ps.setInt(a++, tv.getParameterNameId());
                ps.setInt(a++, tv.getCodeSystemId());
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // parameter_name_id, code_system_id, token_value
                CommonTokenValue key = new CommonTokenValue(rs.getInt(1), rs.getInt(2), rs.getString(3));
                idMap.put(key, rs.getLong(4));
            }
        } catch (SQLException x) {
            throw translator.translate(x);
        }
        
        // Now update the ids for all the matching systems in our list
        for (ResourceTokenValueRec xr: values) {
            CommonTokenValue key = new CommonTokenValue(xr.getParameterNameId(), xr.getCodeSystemValueId(), xr.getTokenValue());
            Long id = idMap.get(key);
            if (id != null) {
                xr.setCommonTokenValueId(id);

                // update the thread-local cache with this id. The values aren't committed to the shared cache
                // until the transaction commits
                cache.addTokenValue(key, id);
            } else {
                // Unlikely...but need to handle just in case
                logger.severe("Record for external_reference_value '" + xr.getTokenValue() + "' inserted but not found");
                throw new IllegalStateException("id deleted from database!");
            }
        }
    }

    @Override
    public void addLocalReferences(Collection<LocalResourceReferenceRec> lrefs) {
        try {
            if (localReferencesBatch == null) {
                localReferencesBatch = connection.prepareStatement(INS_LOCAL_REF);
            }

            for (LocalResourceReferenceRec lrf: lrefs) {
                localReferencesBatch.setInt(1, lrf.getParameterNameId());
                localReferencesBatch.setLong(2, lrf.getLogicalResourceId());
                localReferencesBatch.setLong(3, lrf.getRefLogicalResourceId());
                localReferencesBatch.addBatch();
                
                if (++localReferencesBatchCount == BATCH_SIZE) {
                    localReferencesBatch.executeBatch();
                    localReferencesBatchCount = 0;
                }
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, INS_LOCAL_REF, x);
            throw translator.translate(x);
        }
    }
    
    /**
     * Delete any records in the external_reference_values table which are
     * no longer used by any external_references. Maintenance function.
     */
    public void deleteUnusedExternalReferenceValues() {
        final String DML = "DELETE FROM external_reference_values xrv"
                + " WHERE NOT EXISTS (SELECT 1 FROM external_references xr "
                + "                    WHERE xr.external_reference_value_id = xrv.external_reference_value_id)";
        try (Statement s = connection.createStatement()) {
            s.executeUpdate(DML);
        } catch (SQLException x) {
            // make the exception a little bit more meaningful knowing the database type
            logger.log(Level.SEVERE, DML, x);
            throw translator.translate(x);
        }
    }
    
    /**
     * Delete any records in the external_systems table which are no
     * longer used by any external_references record. Maintenance function.
     */
    public void deleteUnusedExternalSystems() {
        final String DML = "DELETE FROM external_systems xs"
                + " WHERE NOT EXISTS (SELECT 1 FROM external_references xr "
                + "                    WHERE xs.external_system_id = xr.external_system_id)";
        try (Statement s = connection.createStatement()) {
            s.executeUpdate(DML);
        } catch (SQLException x) {
            // make the exception a little bit more meaningful knowing the database type
            logger.log(Level.SEVERE, DML, x);
            throw translator.translate(x);
        }
    }

    @Override
    public long createGhostLogicalResource(String resourceType, String logicalId) throws FHIRPersistenceException {
        long result;
        // need to achieve two things:
        //   1. idempotent create
        //   2. get the logical_resource_id
        // because RETURNING isn't available in Derby, we make this an upsert followed by a select.
        // TODO. stored procs could reduce round-trips
        final String nextVal = translator.nextValue(schemaName, "fhir_sequence");
        final String insert = ""
                + "INSERT INTO logical_resources (logical_resource_id, resource_type_id, logical_id) "
                + "SELECT " + nextVal + ", "
                + "       src.resource_type_id, src.logical_id "
                + "  FROM (SELECT rt.resource_type_id, CAST(? AS VARCHAR(" + FhirSchemaConstants.LOGICAL_ID_BYTES + ")) AS logical_id "
                + "          FROM resource_types rt "
                + "         WHERE rt.resource_type = ? ) AS src "
                + "LEFT OUTER JOIN logical_resources lr "
                + "             ON (lr.resource_type_id = src.resource_type_id "
                + "            AND lr.logical_id = src.logical_id) "
                + " WHERE lr.logical_id IS NULL";
        try (PreparedStatement ps = connection.prepareStatement(insert)) {
            ps.setString(1, logicalId);
            ps.setString(2, resourceType);
            int created = ps.executeUpdate();
            
            if (created > 0 && logger.isLoggable(Level.FINE)) {
                logger.fine("Created new ghost record for logical resource " + resourceType + "/" + logicalId);
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, insert, x);
            throw translator.translate(x);
        }
        
        // Select the id for the record we just upserted
        final String select = ""
                + "SELECT logical_resource_id "
                + "  FROM logical_resources lr, "
                + "       resource_types rt "
                + " WHERE rt.resource_type = ? "
                + "   AND lr.resource_type_id = rt.resource_type_id "
                + "   AND lr.logical_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(select)) {
            ps.setString(1, resourceType);
            ps.setString(2, logicalId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getLong(1);
            } else {
                // not gonna happen
                throw new DataAccessException("logical resource record does not exist after upsert!");
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, select, x);
            throw translator.translate(x);
        }
        return result;
    }
}
