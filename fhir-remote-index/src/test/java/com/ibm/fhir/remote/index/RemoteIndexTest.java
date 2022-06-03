/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.remote.index;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.gson.Gson;
import com.ibm.fhir.database.utils.api.IConnectionProvider;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.PreparedStatementHelper;
import com.ibm.fhir.database.utils.derby.DerbyTranslator;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.persistence.index.RemoteIndexConstants;
import com.ibm.fhir.persistence.index.RemoteIndexMessage;
import com.ibm.fhir.persistence.index.SearchParametersTransportAdapter;
import com.ibm.fhir.remote.index.cache.IdentityCacheImpl;
import com.ibm.fhir.remote.index.database.CacheLoader;
import com.ibm.fhir.remote.index.database.PlainDerbyMessageHandler;

/**
 *
 */
public class RemoteIndexTest {
    private Properties testProps;

    private IConnectionProvider connectionProvider;
    private String[] TEST_RESOURCE_TYPES = {"Patient", "Observation" };
    private IdentityCacheImpl identityCache;
    private static final String SCHEMA_NAME = "FHIRDATA";
    private static final IDatabaseTranslator translator = new DerbyTranslator();

    private final String OBSERVATION = "Observation";
    private final String OBSERVATION_LOGICAL_ID = UUID.randomUUID().toString();
    private final int versionId = 1;
    private final Instant lastUpdated = Instant.now();
    private final String requestShard = null;
    private final String parameterHash = "1Z+NWYZb739Ava9Pd/d7wt2xecKmC2FkfLlCCml0I5M=";
    private final Instant ts1 = lastUpdated.plusMillis(1000);
    private final Instant ts2 = lastUpdated.plusMillis(2000);
    private final BigDecimal valueNumber = BigDecimal.valueOf(1.0);
    private final BigDecimal valueNumberLow = BigDecimal.valueOf(0.5);
    private final BigDecimal valueNumberHigh = BigDecimal.valueOf(1.5);
    private final String valueSystem = "system1";
    private final String valueCode = "code1";
    private final String refResourceType = "Patient";
    private final String refLogicalId = "pat1";
    private final Integer refVersion = 2;
    private final boolean wholeSystem = false;
    private final Integer compositeId = null;
    private final String valueString = "str1";
    private final String url = "http://some.profile/location";
    private final String profileVersion = "1.0";
    
    @BeforeClass
    public void bootstrapDatabase() throws Exception {
        final Set<String> resourceTypeNames = Set.of(TEST_RESOURCE_TYPES);
        this.testProps = TestUtil.readTestProperties("test-remote-index.properties");
        DerbyFhirFactory derbyInit;
        String dbDriverName = this.testProps.getProperty("dbDriverName");
        if (dbDriverName == null || !dbDriverName.contains("derby")) {
            throw new IllegalStateException("test properties missing derby driver configuration");
        }

        derbyInit = new DerbyFhirFactory(this.testProps, resourceTypeNames);
        this.connectionProvider = derbyInit.getConnectionProvider(false);
        Duration cacheDuration = Duration.ofDays(1);
        this.identityCache = new IdentityCacheImpl(
            100, cacheDuration,  // code systems
            100, cacheDuration,  // common token values
            100, cacheDuration,  // canonical values
            100, cacheDuration); // logical resource idents

        // Preload the cache so we have all the resource types available
        try (Connection c = connectionProvider.getConnection()) {
            CacheLoader cacheLoader = new CacheLoader(identityCache);
            cacheLoader.apply(c);
            c.commit();
        }
    }
    
    /**
     * Get a list of messages to process
     * @return
     */
    private List<String> getMessages(long logicalResourceId) {
        RemoteIndexMessage sent = new RemoteIndexMessage();
        sent.setMessageVersion(RemoteIndexConstants.MESSAGE_VERSION);

        // Create an Observation resource with a few parameters
        SearchParametersTransportAdapter adapter = new SearchParametersTransportAdapter(OBSERVATION, OBSERVATION_LOGICAL_ID, logicalResourceId, 
            versionId, lastUpdated, requestShard, parameterHash);
        adapter.dateValue("date-param", ts1, ts2, null, true);
        adapter.locationValue("location-param", 0.1, 0.2, null);
        adapter.numberValue("number-param", valueNumber, valueNumberLow, valueNumberHigh, null);
        adapter.profileValue("profile-param", url, profileVersion, null, true);
        adapter.quantityValue("quantity-param", valueSystem, valueCode, valueNumber, valueNumberLow, valueNumberHigh, compositeId);
        adapter.referenceValue("reference-param", refResourceType, refLogicalId, refVersion, compositeId);
        adapter.securityValue("security-param", valueSystem, valueCode, wholeSystem);
        adapter.stringValue("string-param", valueString, compositeId, wholeSystem);
        adapter.tagValue("tag-param", valueSystem, valueCode, wholeSystem);
        adapter.tokenValue("token-param", valueSystem, valueCode, compositeId);

        sent.setData(adapter.build());
        final String payload = marshallToString(sent);
     
        final List<String> result = new ArrayList<>();
        result.add(payload);
        return result;
    }

    /**
     * Marshall the message to a string
     * @param message
     * @return
     */
    private String marshallToString(RemoteIndexMessage message) {
        final Gson gson = new Gson();
        return gson.toJson(message);
    }

    @Test
    public void testFill() throws Exception {
        final long logicalResourceId;
        try (Connection c = connectionProvider.getConnection()) {
            logicalResourceId = addObservationLogicalResource(c, OBSERVATION_LOGICAL_ID);
            c.commit();
        }

        try (Connection c = connectionProvider.getConnection()) {
            PlainDerbyMessageHandler handler = new PlainDerbyMessageHandler(c, SCHEMA_NAME, identityCache, 1000L);
            handler.process(getMessages(logicalResourceId));
            checkData(c, logicalResourceId);
        }
    }

    /**
     * Inject the logical_resource_ident, logical_resources and observation_logical_resources
     * record as we would normally see added by the FHIR server. We're not dealing with any
     * concurrency here, so we just use 3 simple inserts.
     * @param c
     * @throws SQLException
     */
    private long addObservationLogicalResource(Connection c, String logicalId) throws SQLException {
        final int resourceTypeId = identityCache.getResourceTypeId(OBSERVATION);
        final String getNextLogicalId = translator.selectSequenceNextValue(SCHEMA_NAME, "fhir_sequence");
        long logicalResourceId;
        try (Statement s = c.createStatement()) {
            ResultSet rs = s.executeQuery(getNextLogicalId);
            if (rs.next()) {
                logicalResourceId = rs.getLong(1);
            } else {
                throw new IllegalStateException("no row from '" + getNextLogicalId + "'");
            }
        }
        
        final String insertIdent = "INSERT INTO logical_resource_ident(logical_resource_id, resource_type_id, logical_id) VALUES (?, ?, ?)";
        try (PreparedStatement ps = c.prepareStatement(insertIdent)) {
            ps.setLong(1, logicalResourceId);
            ps.setInt(2, resourceTypeId);
            ps.setString(3, logicalId);
            ps.executeUpdate();
        }

        final Timestamp lastUpdated = Timestamp.from(this.lastUpdated);
        final String insertLogicalResource = "INSERT INTO logical_resources(logical_resource_id, resource_type_id, logical_id, last_updated, is_deleted, parameter_hash)"
                + " VALUES (?,?,?,?,?,?)";
        try (PreparedStatement ps = c.prepareStatement(insertLogicalResource)) {
            PreparedStatementHelper psh = new PreparedStatementHelper(ps);
            
            psh.setLong(logicalResourceId)
            .setInt(resourceTypeId)
            .setString(logicalId)
            .setTimestamp(lastUpdated)
            .setString("N")
            .setString(parameterHash);
            ps.executeUpdate();
        }

        final String insertObservationLogicalResource = "INSERT INTO observation_logical_resources(logical_resource_id, logical_id, is_deleted, last_updated, version_id)"
                + " VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = c.prepareStatement(insertObservationLogicalResource)) {
            PreparedStatementHelper psh = new PreparedStatementHelper(ps);
            
            psh.setLong(logicalResourceId)
            .setString(logicalId)
            .setString("N")
            .setTimestamp(lastUpdated)
            .setInt(this.versionId);
            ps.executeUpdate();
        }

        return logicalResourceId;
    }
    /**
     * Check that the data in the processed messages now exists in
     * the database
     * @param c
     * @throws Exception
     */
    private void checkData(Connection c, long logicalResourceId) throws Exception {
        
    }
}
