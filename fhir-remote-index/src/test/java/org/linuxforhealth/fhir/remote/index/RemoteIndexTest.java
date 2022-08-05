/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.remote.index;

import static org.testng.Assert.assertEquals;

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
import java.util.logging.Logger;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import org.linuxforhealth.fhir.database.utils.api.IConnectionProvider;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseTranslator;
import org.linuxforhealth.fhir.database.utils.common.PreparedStatementHelper;
import org.linuxforhealth.fhir.database.utils.common.ResultSetReader;
import org.linuxforhealth.fhir.database.utils.derby.DerbyTranslator;
import org.linuxforhealth.fhir.model.test.TestUtil;
import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;
import org.linuxforhealth.fhir.persistence.helper.RemoteIndexSupport;
import org.linuxforhealth.fhir.persistence.index.RemoteIndexConstants;
import org.linuxforhealth.fhir.persistence.index.RemoteIndexMessage;
import org.linuxforhealth.fhir.persistence.index.SearchParametersTransportAdapter;
import org.linuxforhealth.fhir.persistence.params.api.IMessageHandler;
import org.linuxforhealth.fhir.persistence.params.batch.ParameterValueCollector;
import org.linuxforhealth.fhir.persistence.params.database.PlainDerbyParamValueProcessor;
import org.linuxforhealth.fhir.remote.index.cache.IdentityCacheImpl;
import org.linuxforhealth.fhir.remote.index.database.CacheLoader;
import org.linuxforhealth.fhir.remote.index.database.RemoteIndexMessageHandler;

/**
 * Unit test for remote index message handling and database processing
 */
public class RemoteIndexTest {
    private static final Logger logger = Logger.getLogger(RemoteIndexTest.class.getName());
    private Properties testProps;

    private IConnectionProvider connectionProvider;
    private String[] TEST_RESOURCE_TYPES = {"Patient", "Observation" };
    private IdentityCacheImpl identityCache;
    private static final String SCHEMA_NAME = "FHIRDATA";
    private static final boolean WHOLE_SYSTEM = true;
    private static final IDatabaseTranslator translator = new DerbyTranslator();
    private static final String OBSERVATION = "Observation";

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
    private final Integer compositeId = null;
    private final String valueString = "str1";
    private final String url = "http://some.profile/location";
    private final String profileVersion = "1.0";
    private final String instanceIdentifier = "a-unique-id-value-1";
    
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
        sent.setInstanceIdentifier(instanceIdentifier);

        // Create an Observation resource with a few parameters
        SearchParametersTransportAdapter adapter = new SearchParametersTransportAdapter(OBSERVATION, OBSERVATION_LOGICAL_ID, logicalResourceId, 
            versionId, lastUpdated, requestShard, parameterHash);
        adapter.stringValue("string-param", valueString, compositeId, WHOLE_SYSTEM);
        adapter.dateValue("date-param", ts1, ts2, null, WHOLE_SYSTEM);
        adapter.numberValue("number-param", valueNumber, valueNumberLow, valueNumberHigh, null);
        adapter.quantityValue("quantity-param", valueSystem, valueCode, valueNumber, valueNumberLow, valueNumberHigh, compositeId);
        adapter.tokenValue("token-param", valueSystem, valueCode, compositeId, WHOLE_SYSTEM);
        adapter.locationValue("location-param", 0.1, 0.2, null);
        adapter.referenceValue("reference-param", refResourceType, refLogicalId, refVersion, compositeId);
        adapter.securityValue("security-param", valueSystem, valueCode, WHOLE_SYSTEM);
        adapter.profileValue("profile-param", url, profileVersion, null, WHOLE_SYSTEM);
        adapter.tagValue("tag-param", valueSystem, valueCode, WHOLE_SYSTEM);

        sent.setData(adapter.build());
        final String payload = RemoteIndexSupport.marshallToString(sent);     
        final List<String> result = new ArrayList<>();
        result.add(payload);
        return result;
    }

    @Test
    public void testFill() throws Exception {
        final long logicalResourceId;
        try (Connection c = connectionProvider.getConnection()) {
            logicalResourceId = addObservationLogicalResource(c, OBSERVATION_LOGICAL_ID);
            c.commit();
        }

        try (Connection c = connectionProvider.getConnection()) {
            try {

                ParameterValueCollector paramValueCollector = new ParameterValueCollector(identityCache);
                PlainDerbyParamValueProcessor paramValueProcessor = new PlainDerbyParamValueProcessor(c, SCHEMA_NAME, identityCache);
                IMessageHandler handler = new RemoteIndexMessageHandler(c, instanceIdentifier, 1000L, paramValueCollector, paramValueProcessor);
                handler.process(getMessages(logicalResourceId));
                checkData(c, logicalResourceId);
                c.commit();
            } catch (Throwable t) {
                safeRollback(c);
                throw t;
            }
        }
    }

    /**
     * Try and rollback the transaction, squashing any exception
     * @param c
     */
    private void safeRollback(Connection c) {
        try {
            c.rollback();
        } catch (SQLException x) {
            logger.warning("rollback failed: " + x.getMessage());
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
        // check the resource level parameters
        checkStringParam(c, OBSERVATION, logicalResourceId, valueString);
        checkDateParam(c, OBSERVATION, logicalResourceId, ts1, ts2);
        checkNumberParam(c, OBSERVATION, logicalResourceId, valueNumber, valueNumberLow, valueNumberHigh);
        checkLocationParam(c, OBSERVATION, logicalResourceId, 0.1, 0.2);
        checkProfileParam(c, OBSERVATION, logicalResourceId, url, profileVersion);
        checkQuantityParam(c, OBSERVATION, logicalResourceId, valueSystem, valueCode, valueNumber, valueNumberLow, valueNumberHigh);
        checkReferenceParam(c, OBSERVATION, logicalResourceId, refResourceType, refLogicalId);
        checkTagParam(c, OBSERVATION, logicalResourceId, valueSystem, valueCode);
        checkSecurityParam(c, OBSERVATION, logicalResourceId, valueSystem, valueCode);
        checkTokenParam(c, OBSERVATION, logicalResourceId, valueSystem, valueCode);

        // check the whole-system level parameters
        checkStringSystemParam(c, OBSERVATION, logicalResourceId, valueString);
        checkDateSystemParam(c, OBSERVATION, logicalResourceId, ts1, ts2);
        checkProfileSystemParam(c, OBSERVATION, logicalResourceId, url, profileVersion);
        checkTagSystemParam(c, OBSERVATION, logicalResourceId, valueSystem, valueCode);
        checkSecuritySystemParam(c, OBSERVATION, logicalResourceId, valueSystem, valueCode);
    }

    /**
     * @param c
     * @param resourceType
     * @param logicalResourceId
     * @param valueSystem
     * @param valueCode
     * @param valueNumber
     * @param valueNumberLow
     * @param valueNumberHigh
     */
    private void checkQuantityParam(Connection c, String resourceType, long logicalResourceId, String valueSystem, String valueCode, BigDecimal valueNumber,
        BigDecimal valueNumberLow, BigDecimal valueNumberHigh) throws Exception {
        final String select = ""
                + "SELECT c.code_system_name, p.code, p.quantity_value, p.quantity_value_low, p.quantity_value_high "
                + "  FROM " + resourceType + "_quantity_values p "
                + "  JOIN code_systems c ON c.code_system_id = p.code_system_id "
                + " WHERE p.logical_resource_id = ?";
        try (PreparedStatement ps = c.prepareStatement(select)) {
            ps.setLong(1, logicalResourceId);
            ResultSet rs = ps.executeQuery();
            ResultSetReader rsr = new ResultSetReader(rs);
            if (rsr.next()) {
                assertEquals(rsr.getString(), valueSystem);
                assertEquals(rsr.getString(), valueCode);
                assertEquals(rsr.getBigDecimal(), valueNumber);
                assertEquals(rsr.getBigDecimal(), valueNumberLow);
                assertEquals(rsr.getBigDecimal(), valueNumberHigh);
            } else {
                throw new FHIRPersistenceException("missing value: " + select);
            }
    
            if (rs.next()) {
                // there can be only one
                throw new FHIRPersistenceException("more than one quantity parameter");
            }
        }
        
    }

    private void checkStringParam(Connection c, String resourceType, long logicalResourceId, String valueString) throws Exception {
        final String select = "SELECT str_value FROM " + resourceType + "_str_values WHERE logical_resource_id = ?";
        try (PreparedStatement ps = c.prepareStatement(select)) {
            ps.setLong(1, logicalResourceId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                assertEquals(rs.getString(1), valueString);
            } else {
                throw new FHIRPersistenceException("missing value: " + select);
            }

            if (rs.next()) {
                // there can be only one
                throw new FHIRPersistenceException("more than one string parameter");
            }
        }
    }

    private void checkStringSystemParam(Connection c, String resourceType, long logicalResourceId, String valueString) throws Exception {
        final String select = "SELECT str_value FROM str_values WHERE logical_resource_id = ?";
        try (PreparedStatement ps = c.prepareStatement(select)) {
            ps.setLong(1, logicalResourceId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                assertEquals(rs.getString(1), valueString);
            } else {
                throw new FHIRPersistenceException("missing value: " + select);
            }

            if (rs.next()) {
                // there can be only one
                throw new FHIRPersistenceException("more than one string parameter");
            }
        }
    }

    private void checkNumberParam(Connection c, String resourceType, long logicalResourceId, BigDecimal numberValue, 
            BigDecimal numberValueLow, BigDecimal numberValueHigh) throws Exception {
        final String select = "SELECT number_value, number_value_low, number_value_high FROM " + resourceType + "_number_values WHERE logical_resource_id = ?";
        try (PreparedStatement ps = c.prepareStatement(select)) {
            ps.setLong(1, logicalResourceId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                assertEquals(rs.getBigDecimal(1), numberValue);
                assertEquals(rs.getBigDecimal(2), numberValueLow);
                assertEquals(rs.getBigDecimal(3), numberValueHigh);
            } else {
                throw new FHIRPersistenceException("missing value: " + select);
            }

            if (rs.next()) {
                // there can be only one
                throw new FHIRPersistenceException("more than one number parameter");
            }
        }
    }

    private void checkDateParam(Connection c, String resourceType, long logicalResourceId, Instant dateStart, 
        Instant dateEnd) throws Exception {
        final String select = "SELECT date_start, date_end FROM " + resourceType + "_date_values WHERE logical_resource_id = ?";
        try (PreparedStatement ps = c.prepareStatement(select)) {
            ps.setLong(1, logicalResourceId);
            ResultSet rs = ps.executeQuery();
            ResultSetReader rsr = new ResultSetReader(rs);
            if (rsr.next()) {
                assertEquals(rsr.getTimestamp(), Timestamp.from(dateStart));
                assertEquals(rsr.getTimestamp(), Timestamp.from(dateEnd));
            } else {
                throw new FHIRPersistenceException("missing value: " + select);
            }
    
            if (rs.next()) {
                // there can be only one
                throw new FHIRPersistenceException("more than one date parameter");
            }
        }
    }

    private void checkDateSystemParam(Connection c, String resourceType, long logicalResourceId, Instant dateStart, 
        Instant dateEnd) throws Exception {
        final String select = "SELECT date_start, date_end FROM date_values WHERE logical_resource_id = ?";
        try (PreparedStatement ps = c.prepareStatement(select)) {
            ps.setLong(1, logicalResourceId);
            ResultSet rs = ps.executeQuery();
            ResultSetReader rsr = new ResultSetReader(rs);
            if (rsr.next()) {
                assertEquals(rsr.getTimestamp(), Timestamp.from(dateStart));
                assertEquals(rsr.getTimestamp(), Timestamp.from(dateEnd));
            } else {
                throw new FHIRPersistenceException("missing date system value: " + select);
            }
    
            if (rs.next()) {
                // there can be only one
                throw new FHIRPersistenceException("more than one date system parameter");
            }
        }
    }
    
    private void checkLocationParam(Connection c, String resourceType, long logicalResourceId, double latitude, 
            double longitude) throws Exception {
        final String select = "SELECT latitude_value, longitude_value FROM " + resourceType + "_latlng_values WHERE logical_resource_id = ?";
        try (PreparedStatement ps = c.prepareStatement(select)) {
            ps.setLong(1, logicalResourceId);
            ResultSet rs = ps.executeQuery();
            ResultSetReader rsr = new ResultSetReader(rs);
            if (rsr.next()) {
                assertEquals(rsr.getDouble(), latitude);
                assertEquals(rsr.getDouble(), longitude);
            } else {
                throw new FHIRPersistenceException("missing value: " + select);
            }
    
            if (rs.next()) {
                // there can be only one
                throw new FHIRPersistenceException("more than one date parameter");
            }
        }
    }

    private void checkProfileParam(Connection c, String resourceType, long logicalResourceId, String profile, String version) throws Exception { 
        final String select = ""
                + "SELECT c.url, p.version FROM " + resourceType + "_profiles p"
                + "  JOIN common_canonical_values c ON c.canonical_id = p.canonical_id "
                + " WHERE logical_resource_id = ?";
    
        try (PreparedStatement ps = c.prepareStatement(select)) {
            ps.setLong(1, logicalResourceId);
            ResultSet rs = ps.executeQuery();
            ResultSetReader rsr = new ResultSetReader(rs);
            if (rsr.next()) {
                assertEquals(rsr.getString(), profile);
                assertEquals(rsr.getString(), version);
            } else {
                throw new FHIRPersistenceException("missing value: " + select);
            }
    
            if (rs.next()) {
                // there can be only one
                throw new FHIRPersistenceException("more than one profile parameter");
            }
        }
    }

    private void checkProfileSystemParam(Connection c, String resourceType, long logicalResourceId, String profile, String version) throws Exception { 
        final String select = ""
                + "SELECT c.url, p.version FROM logical_resource_profiles p"
                + "  JOIN common_canonical_values c ON c.canonical_id = p.canonical_id "
                + " WHERE logical_resource_id = ?";
    
        try (PreparedStatement ps = c.prepareStatement(select)) {
            ps.setLong(1, logicalResourceId);
            ResultSet rs = ps.executeQuery();
            ResultSetReader rsr = new ResultSetReader(rs);
            if (rsr.next()) {
                assertEquals(rsr.getString(), profile);
                assertEquals(rsr.getString(), version);
            } else {
                throw new FHIRPersistenceException("missing profile system value: " + select);
            }
    
            if (rs.next()) {
                // there can be only one
                throw new FHIRPersistenceException("more than one profile system parameter");
            }
        }
    }

    private void checkSecurityParam(Connection c, String resourceType, long logicalResourceId, String codeSystem, String tokenValue) throws Exception { 
        final String select = ""
                + "SELECT 1 FROM " + resourceType + "_security p"
                + "  JOIN common_token_values c ON c.common_token_value_id = p.common_token_value_id "
                + "  JOIN code_systems s ON s.code_system_id = c.code_system_id "
                + " WHERE logical_resource_id = ? "
                + "   AND s.code_system_name = ? "
                + "   AND c.token_value = ? ";
    
        try (PreparedStatement ps = c.prepareStatement(select)) {
            ps.setLong(1, logicalResourceId);
            ps.setString(2, codeSystem);
            ps.setString(3, tokenValue);
            ResultSet rs = ps.executeQuery();
            ResultSetReader rsr = new ResultSetReader(rs);
            if (rsr.next()) {
                // OK
            } else {
                throw new FHIRPersistenceException("missing security value: " + select);
            }
    
            if (rs.next()) {
                // there can be only one
                throw new FHIRPersistenceException("more than one security parameter");
            }
        }
    }

    private void checkSecuritySystemParam(Connection c, String resourceType, long logicalResourceId, String codeSystem, String tokenValue) throws Exception { 
        final String select = ""
                + "SELECT 1 FROM logical_resource_security p"
                + "  JOIN common_token_values c ON c.common_token_value_id = p.common_token_value_id "
                + "  JOIN code_systems s ON s.code_system_id = c.code_system_id "
                + " WHERE logical_resource_id = ? "
                + "   AND s.code_system_name = ? "
                + "   AND c.token_value = ? ";
    
        try (PreparedStatement ps = c.prepareStatement(select)) {
            ps.setLong(1, logicalResourceId);
            ps.setString(2, codeSystem);
            ps.setString(3, tokenValue);
            ResultSet rs = ps.executeQuery();
            ResultSetReader rsr = new ResultSetReader(rs);
            if (rsr.next()) {
                // OK
            } else {
                throw new FHIRPersistenceException("missing security value: " + select);
            }
    
            if (rs.next()) {
                // there can be only one
                throw new FHIRPersistenceException("more than one security parameter");
            }
        }
    }

    private void checkTagParam(Connection c, String resourceType, long logicalResourceId, String codeSystem, String tokenValue) throws Exception { 
        final String select = ""
                + "SELECT 1 FROM " + resourceType + "_tags p"
                + "  JOIN common_token_values c ON c.common_token_value_id = p.common_token_value_id "
                + "  JOIN code_systems s ON s.code_system_id = c.code_system_id "
                + " WHERE logical_resource_id = ? "
                + "   AND s.code_system_name = ? "
                + "   AND c.token_value = ? ";
    
        try (PreparedStatement ps = c.prepareStatement(select)) {
            ps.setLong(1, logicalResourceId);
            ps.setString(2, codeSystem);
            ps.setString(3, tokenValue);
            ResultSet rs = ps.executeQuery();
            ResultSetReader rsr = new ResultSetReader(rs);
            if (rsr.next()) {
                // OK
            } else {
                throw new FHIRPersistenceException("missing tag value: " + select);
            }
    
            if (rs.next()) {
                // there can be only one
                throw new FHIRPersistenceException("more than one tag parameter");
            }
        }
    }

    private void checkTagSystemParam(Connection c, String resourceType, long logicalResourceId, String codeSystem, String tokenValue) throws Exception { 
        final String select = ""
                + "SELECT 1 FROM logical_resource_tags p"
                + "  JOIN common_token_values c ON c.common_token_value_id = p.common_token_value_id "
                + "  JOIN code_systems s ON s.code_system_id = c.code_system_id "
                + " WHERE logical_resource_id = ? "
                + "   AND s.code_system_name = ? "
                + "   AND c.token_value = ? ";
    
        try (PreparedStatement ps = c.prepareStatement(select)) {
            ps.setLong(1, logicalResourceId);
            ps.setString(2, codeSystem);
            ps.setString(3, tokenValue);
            ResultSet rs = ps.executeQuery();
            ResultSetReader rsr = new ResultSetReader(rs);
            if (rsr.next()) {
                // OK
            } else {
                throw new FHIRPersistenceException("missing tag value: " + select);
            }
    
            if (rs.next()) {
                // there can be only one
                throw new FHIRPersistenceException("more than one tag parameter");
            }
        }
    }

    private void checkTokenParam(Connection c, String resourceType, long logicalResourceId, String codeSystem, String tokenValue) throws Exception { 
        final String select = ""
                + "SELECT 1 FROM " + resourceType + "_resource_token_refs p"
                + "  JOIN common_token_values c ON c.common_token_value_id = p.common_token_value_id "
                + "  JOIN code_systems s ON s.code_system_id = c.code_system_id "
                + " WHERE logical_resource_id = ? "
                + "   AND s.code_system_name = ? "
                + "   AND c.token_value = ? ";
    
        try (PreparedStatement ps = c.prepareStatement(select)) {
            ps.setLong(1, logicalResourceId);
            ps.setString(2, codeSystem);
            ps.setString(3, tokenValue);
            ResultSet rs = ps.executeQuery();
            ResultSetReader rsr = new ResultSetReader(rs);
            if (rsr.next()) {
                // OK
            } else {
                throw new FHIRPersistenceException("missing token value: " + select);
            }
    
            if (rs.next()) {
                // there can be only one
                throw new FHIRPersistenceException("more than one token parameter");
            }
        }
    }

    private void checkReferenceParam(Connection c, String resourceType, long logicalResourceId, String refResourceType,
        String refLogicalId) throws Exception {
        final String select = ""
                + "SELECT 1 FROM " + resourceType + "_ref_values p "
                + "  JOIN logical_resource_ident i ON i.logical_resource_id = p.ref_logical_resource_id "
                + "  JOIN resource_types rrt ON rrt.resource_type_id = i.resource_type_id "
                + " WHERE p.logical_resource_id = ?"
                + "   AND rrt.resource_type = ? "
                + "   AND i.logical_id = ? ";
        try (PreparedStatement ps = c.prepareStatement(select)) {
            ps.setLong(1, logicalResourceId);
            ps.setString(2, refResourceType);
            ps.setString(3, refLogicalId);
            ResultSet rs = ps.executeQuery();
            ResultSetReader rsr = new ResultSetReader(rs);
            if (rsr.next()) {
                // ok
            } else {
                throw new FHIRPersistenceException("missing value: " + select);
            }
    
            if (rs.next()) {
                // there can be only one
                throw new FHIRPersistenceException("more than one date parameter");
            }
        }
    }

}