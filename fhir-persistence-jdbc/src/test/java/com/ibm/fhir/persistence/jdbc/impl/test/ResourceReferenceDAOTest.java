/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.impl.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.ibm.fhir.database.utils.derby.DerbyMaster;
import com.ibm.fhir.database.utils.derby.DerbyTranslator;
import com.ibm.fhir.database.utils.model.DbType;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCCache;
import com.ibm.fhir.persistence.jdbc.cache.CommonTokenValuesCacheImpl;
import com.ibm.fhir.persistence.jdbc.cache.FHIRPersistenceJDBCCacheImpl;
import com.ibm.fhir.persistence.jdbc.cache.NameIdCache;
import com.ibm.fhir.persistence.jdbc.connection.FHIRDbFlavor;
import com.ibm.fhir.persistence.jdbc.connection.FHIRDbFlavorImpl;
import com.ibm.fhir.persistence.jdbc.dao.impl.ResourceReferenceDAO;
import com.ibm.fhir.persistence.jdbc.postgresql.DerbyResourceReferenceDAO;
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
        
        CommonTokenValuesCacheImpl tokenValuesCache = new CommonTokenValuesCacheImpl(10, 10);
        FHIRPersistenceJDBCCache cache = new FHIRPersistenceJDBCCacheImpl(new NameIdCache<Integer>(), new NameIdCache<Integer>(), tokenValuesCache);
        
        // Grab a connection which we use to initialize the DAO. This connetion is used
        // for the duration of the test
        connection = derby.getConnection();
        dao = new DerbyResourceReferenceDAO(new DerbyTranslator(), connection, schemaName, cache.getResourceReferenceCache());

        // Set up the DAO we need for access to some static config like resource types and parameter names
        FHIRDbFlavor flavor = new FHIRDbFlavorImpl(DbType.DERBY, false);
    }

    @AfterClass
    public void shutdown() throws SQLException, FHIRPersistenceException {
        // If there are pending batch operations flushed here, the dao.close() will flush and
        // start a transaction which will cause a problem when closing the connection. This
        // would be an error in the way the test is written. Fix it there, not here.
        dao.close();
        connection.close();
        resourceTypeNames.clear();
    }
}