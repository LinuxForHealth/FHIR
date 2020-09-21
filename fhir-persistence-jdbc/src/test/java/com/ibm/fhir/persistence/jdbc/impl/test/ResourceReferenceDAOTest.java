/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.impl.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.database.utils.derby.DerbyMaster;
import com.ibm.fhir.database.utils.derby.DerbyTranslator;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.dao.impl.ExternalResourceRef;
import com.ibm.fhir.persistence.jdbc.dao.impl.ResourceReferenceCacheImpl;
import com.ibm.fhir.persistence.jdbc.dao.impl.ResourceReferenceDAO;
import com.ibm.fhir.schema.derby.DerbyFhirDatabase;

/**
 * Unit tests for {@link ResourceReferenceDAO}
 */
public class ResourceReferenceDAOTest {

    private static final String DB_NAME = "target/derby/resrefdb";
    private DerbyFhirDatabase derby;
    
    // The connection used throughout the tests
    private Connection connection;

    // The cache used by the tests
    private ResourceReferenceCacheImpl cache = new ResourceReferenceCacheImpl(10);

    // The DAO being tested
    private ResourceReferenceDAO dao;
    
    private Set<String> resourceTypeNames = new HashSet<>();
    
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
        connection = derby.getConnection();
        dao = new ResourceReferenceDAO(new DerbyTranslator(), connection, cache);
    }

    @AfterClass
    public void shutdown() throws SQLException, FHIRPersistenceException {
        dao.close();
        cache.reset();
        connection.close();
        resourceTypeNames.clear();
    }
    
    @Test
    public void test1() throws Exception {
        // add some system names
        final String sys1 = "sys1";
        final String sys2 = "sys2";
        
        final long fromLogicalResourceId = 1;
        final int parameterNameId = 1;
        
        List<ExternalResourceRef> xrefs = new ArrayList<>();
        xrefs.add(new ExternalResourceRef(fromLogicalResourceId, parameterNameId, sys1, "ref1"));
        dao.addExternalReferences(xrefs);
        dao.flush();
        connection.commit();
    }
}