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
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.dao.api.IResourceReferenceCache;

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
public class ResourceReferenceDAO implements AutoCloseable {
    private static final Logger logger = Logger.getLogger(ResourceReferenceDAO.class.getName());

    // hold on to the connection because we use batches to improve efficiency
    private final Connection connection;
    
    // The cache used to track the ids of the normalized entities we're managing
    private final IResourceReferenceCache cache;
    
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
    
    // The number of operations we allow before submitting a batch
    private static final int BATCH_SIZE = 100;
    
    /**
     * Public constructor
     * @param c
     */
    public ResourceReferenceDAO(IDatabaseTranslator t, Connection c, IResourceReferenceCache cache) {
        this.translator = t;
        this.connection = c;
        this.cache = cache;
    }

    /**
     * Execute any statements with pending batch entries
     * @throws FHIRPersistenceException
     */
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

    
    /**
     * Delete current external references for a given resource type and logical id. Typically
     * called when creating a new version of a resource or when re-indexing
     * @param resourceTypeId
     * @param logicalId
     */
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
            throw translator.translate(x);
        }

    }
    
    /**
     * Delete current local references for a given resource described by its
     * logical_resource_id. Typically called when creating a new version of a
     * resource or when re-indexing.
     * @param resourceType
     * @param logicalId
     */
    public void deleteLocalReferences(long logicalResourceId) {
        final String DML = "DELETE FROM local_references WHERE logical_resource_id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(DML)) {
            ps.setLong(1, logicalResourceId);
            ps.executeUpdate();
        } catch (SQLException x) {
            // make the exception a little bit more meaningful knowing the database type
            throw translator.translate(x);
        }
        
    }

    /**
     * Delete the membership this resource has with other compartments
     * @param logicalResourceId
     */
    public void deleteLogicalResourceCompartments(long logicalResourceId) {
        final String DML = "DELETE FROM logical_resource_compartments WHERE logical_resource_id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(DML)) {
            ps.setLong(1, logicalResourceId);
            ps.executeUpdate();
        } catch (SQLException x) {
            // make the exception a little bit more meaningful knowing the database type
            throw translator.translate(x);
        }
    }
        
    /**
     * Add the list of external references. Creates new external_system and external_reference_value
     * records as necessary
     * @param xrefs
     */
    public void addExternalReferences(Collection<ExternalResourceRef> xrefs) {
        // We need to be efficient about how we manage the external_systems and 
        // external_reference_values normalized records. It's important to
        // minimize the number of database round-trips.
        
        // Firstly, let's see which external system names we can find in our cache
        Set<String> externalSystemNames = xrefs.stream().map(xr -> xr.getExternalSystemName()).collect(Collectors.toSet());
    }
    
    public void addLocalReferences(Collection<LocalResourceRef> lrefs) {
        try {
            if (localReferencesBatch == null) {
                localReferencesBatch = connection.prepareStatement(INS_LOCAL_REF);
            }

            for (LocalResourceRef lrf: lrefs) {
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
            throw translator.translate(x);
        }
    }
}
