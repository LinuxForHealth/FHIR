/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.impl.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.database.utils.derby.DerbyMaster;
import com.ibm.fhir.database.utils.derby.DerbyTranslator;
import com.ibm.fhir.database.utils.model.DbType;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.connection.FHIRDbFlavor;
import com.ibm.fhir.persistence.jdbc.connection.FHIRDbFlavorImpl;
import com.ibm.fhir.persistence.jdbc.dao.api.ParameterDAO;
import com.ibm.fhir.persistence.jdbc.dao.api.ResourceDAO;
import com.ibm.fhir.persistence.jdbc.dao.impl.ExternalResourceReferenceRec;
import com.ibm.fhir.persistence.jdbc.dao.impl.ParameterDAOImpl;
import com.ibm.fhir.persistence.jdbc.dao.impl.ResourceDAOImpl;
import com.ibm.fhir.persistence.jdbc.dao.impl.ResourceReferenceCacheImpl;
import com.ibm.fhir.persistence.jdbc.dao.impl.ResourceReferenceDAO;
import com.ibm.fhir.schema.derby.DerbyFhirDatabase;

/**
 * Unit tests for {@link ResourceReferenceDAO}
 */
public class ResourceReferenceDAOTest {
    private static final Logger logger = Logger.getLogger(ResourceReferenceDAOTest.class.getName());

    private static final String DB_NAME = "target/derby/resrefdb";
    private DerbyFhirDatabase derby;
    
    // The connection used throughout the tests
    private Connection connection;

    // The cache used by the tests
    private ResourceReferenceCacheImpl cache = new ResourceReferenceCacheImpl(10, 10);
    
    private ParameterDAO parameterDAO;
    
    private ResourceDAO resourceDAO;

    // The DAO being tested
    private ResourceReferenceDAO dao;
    
    
    private Set<String> resourceTypeNames = new HashSet<>();
    
    // The default schema name used in our Derby database
    private final String schemaName = "FHIRDATA";
    
    /**
     * Initialize the database we'll use to exercise the DAO
     */
    @BeforeClass
    public void init() throws SQLException {
        // Add the resource types names we're interested in. This is much faster
        // than creating the schema for the full set of resources.
        resourceTypeNames.add("Patient");
        resourceTypeNames.add("ExplanationOfBenefit");
        resourceTypeNames.add("Practitioner");
        
        // Make sure we're starting with a clean slate
        DerbyMaster.dropDatabase(DB_NAME);
        derby = new DerbyFhirDatabase(DB_NAME, resourceTypeNames);
        
        // Grab a connection which we use to initialize the DAO. This connetion is used
        // for the duration of the test
        connection = derby.getConnection();
        dao = new ResourceReferenceDAO(new DerbyTranslator(), connection, schemaName, cache);

        // Set up the DAO we need for access to some static config like resource types and parameter names
        FHIRDbFlavor flavor = new FHIRDbFlavorImpl(DbType.DERBY, false);
        parameterDAO = new ParameterDAOImpl(connection, schemaName, flavor);
        
        resourceDAO = new ResourceDAOImpl(connection, schemaName, flavor);
    }

    @AfterClass
    public void shutdown() throws SQLException, FHIRPersistenceException {
        dao.close();
        cache.reset();
        connection.close();
        resourceTypeNames.clear();
    }
    
    @Test
    public void testExternal() throws Exception {
        // need a couple of logical resources we can use to create relationships
        try {
            Map<String, Integer> resourceNameMap = resourceDAO.readAllResourceTypeNames();
    
            final String resourceType = "Patient";
            final int resourceTypeId = resourceNameMap.get(resourceType);
            final int parameterNameId = parameterDAO.readParameterNameId("patient");
            final long lr1 = dao.createGhostLogicalResource("Patient", "pat1");
            final long lr2 = dao.createGhostLogicalResource("Patient", "pat2");
    
            ExternalResourceReferenceRec rec1 = new ExternalResourceReferenceRec(parameterNameId, resourceType, resourceTypeId, "pat1", "sys1", "value1");
            ExternalResourceReferenceRec rec2 = new ExternalResourceReferenceRec(parameterNameId, resourceType, resourceTypeId, "pat1", "sys1", "value2");
            ExternalResourceReferenceRec rec3 = new ExternalResourceReferenceRec(parameterNameId, resourceType, resourceTypeId, "pat1", "sys2", "value1");
            ExternalResourceReferenceRec rec4 = new ExternalResourceReferenceRec(parameterNameId, resourceType, resourceTypeId, "pat1", "sys2", "value2");

            // rec1..4 are all patient pat1
            rec1.setLogicalResourceId(lr1);
            rec2.setLogicalResourceId(lr1);
            rec3.setLogicalResourceId(lr1);
            rec4.setLogicalResourceId(lr1);
            
            List<ExternalResourceReferenceRec> xrefs = Arrays.asList(rec1, rec2, rec3, rec4);
            dao.addExternalReferences(xrefs);
            connection.commit();
        } catch (Exception x) {
            // Log before cleanup throws another exception which could hide this issue
            logger.log(Level.SEVERE, "testExternal", x);
            try {
                connection.rollback();
            } catch (SQLException rbx) {
                // NOP. Catch this so we don't hide the original exception.
            }
            throw x;
        }
    }
}