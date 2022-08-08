/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.test.erase;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.database.utils.api.IDatabaseTranslator;
import org.linuxforhealth.fhir.database.utils.api.SchemaType;
import org.linuxforhealth.fhir.database.utils.common.JdbcPropertyAdapter;
import org.linuxforhealth.fhir.database.utils.model.DbType;
import org.linuxforhealth.fhir.database.utils.postgres.PostgresTranslator;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.util.ModelSupport;
import org.linuxforhealth.fhir.persistence.ResourceEraseRecord;
import org.linuxforhealth.fhir.persistence.erase.EraseDTO;
import org.linuxforhealth.fhir.persistence.jdbc.FHIRPersistenceJDBCCache;
import org.linuxforhealth.fhir.persistence.jdbc.connection.FHIRDbFlavor;
import org.linuxforhealth.fhir.persistence.jdbc.connection.FHIRDbFlavorImpl;
import org.linuxforhealth.fhir.persistence.jdbc.dao.EraseResourceDAO;
import org.linuxforhealth.fhir.persistence.jdbc.dao.api.ICommonValuesCache;
import org.linuxforhealth.fhir.persistence.jdbc.dao.api.IIdNameCache;
import org.linuxforhealth.fhir.persistence.jdbc.dao.api.ILogicalResourceIdentCache;
import org.linuxforhealth.fhir.persistence.jdbc.dao.api.INameIdCache;
import org.linuxforhealth.fhir.schema.app.util.CommonUtil;
import org.linuxforhealth.fhir.schema.control.FhirSchemaConstants;

/**
 * EraseTestMain is a test driver for the EraseResourceDAO so it can be debugged during development.
 * input is ResourceType LogicalId
 * Main Patient 1791f19a126-28b0239c-f1a1-4d15-979f-920af8d12446
 *
 * Example output:
 * <code>
 * May 12, 2021 1:36:39 PM org.linuxforhealth.fhir.persistence.jdbc.test.erase.EraseTestMain createConnection
 * INFO: Opening connection to: jdbc:postgresql://localhost:5432/fhirdb?currentSchema=fhirdata&schemaName=fhirdata&ssl=false
 * Got a Connection
 * Starting the erase of 'Patient/1791f19a126-28b0239c-f1a1-4d15-979f-920af8d12446'
 * ResourceEraseRecord [total=-1, status=NOT_FOUND]
 * </code>
 */
public class EraseTestMain {

    private static final String CLASSNAME = EraseTestMain.class.getSimpleName();
    private static final Logger logger = Logger.getLogger(CLASSNAME);

    private IDatabaseTranslator translator = new PostgresTranslator();
    private DbType dbType = DbType.POSTGRESQL;
    private Properties inProperties = null;
    private Properties dbProperties = null;
    private String[] args = null;

    private String schemaName = "fhirdata";
    private String resourceType = null;
    private String logicalId = null;

    /**
     * Public Constructor
     *
     * @param properties
     *            the properties file with the database information and the type.
     * @param args
     *            from the inputs on the commandline
     */
    public EraseTestMain(Properties properties, String[] args) {
        this.args = args;
        this.inProperties = properties;
    }

    /**
     * erases the given resource type and logical id
     *
     * @throws Exception
     */
    protected void erase() throws Exception {
        try {
            try (Connection c = createConnection()) {
                System.out.println("Got a Connection");
                try {
                    FHIRDbFlavor flavor = new FHIRDbFlavorImpl(dbType, SchemaType.PLAIN);
                    EraseResourceDAO dao = new EraseResourceDAO(c, FhirSchemaConstants.FHIR_ADMIN, translator, schemaName, flavor, new MockLocalCache());

                    ResourceEraseRecord record = new ResourceEraseRecord();
                    EraseDTO eraseDto = new EraseDTO();
                    eraseDto.setResourceType(resourceType);
                    eraseDto.setLogicalId(logicalId);
                    System.out.println("Starting the erase of '" + resourceType + "/" + logicalId + "'");
                    dao.erase(record, eraseDto);
                    System.out.println(record);
                } catch (Exception x) {
                    System.out.println("Error Condition Encountered.");
                    c.rollback();
                    throw x;
                }
                c.commit();
            }
        } catch (SQLException x) {
            throw translator.translate(x);
        }
    }

    /**
     * determines the flavor and schema of the execution.
     */
    public void determineFlavorAndSchema() {
        if (inProperties.getProperty("postgres.schemaName") != null) {
            schemaName = inProperties.getProperty("postgres.schemaName");
        }
    }

    /**
     * process args
     */
    public void processArgs() {
        final String prefix = "postgres.";
        dbProperties = new Properties();
        for (Entry<Object, Object> prop : inProperties.entrySet()) {
            String key = (String) prop.getKey();
            if (key.startsWith(prefix)) {
                dbProperties.put(key.replace(prefix, ""), prop.getValue());
            }
        }
        resourceType = args[0];
        logicalId = args[1];
    }

    /**
     * Creates the Connection specific to this Erase Operation.
     *
     * @return
     */
    protected Connection createConnection() {
        Properties connectionProperties = new Properties();
        JdbcPropertyAdapter adapter = CommonUtil.getPropertyAdapter(dbType, dbProperties);
        adapter.getExtraProperties(connectionProperties);

        String url = translator.getUrl(dbProperties);
        logger.info("Opening connection to: " + url);
        Connection connection;
        try {
            connection = DriverManager.getConnection(url, connectionProperties);
            connection.setAutoCommit(false);
        } catch (SQLException x) {
            throw translator.translate(x);
        }
        return connection;
    }

    /**
     * Mocks the JDBC Cache
     */
    private static class MockLocalCache implements FHIRPersistenceJDBCCache {

        @Override
        public boolean needToPrefill() {
            return false;
        }

        @Override
        public void clearNeedToPrefill() {
            // TODO Auto-generated method stub
        }

        @Override
        public ICommonValuesCache getCommonValuesCache() {
            return null;
        }

        @Override
        public INameIdCache<Integer> getResourceTypeCache() {
            // Used to mock the resource_type_id in the db
            INameIdCache<Integer> cache = new INameIdCache<Integer>() {

                @Override
                public Integer getId(String resourceType) {
                    int count = 0;
                    for (Class<? extends Resource> resource : ModelSupport.getResourceTypes(false)) {
                        if (resource.getSimpleName().equals(resourceType)) {
                            break;
                        }
                        count++;
                    }
                    return count;
                }

                @Override
                public Collection<Integer> getAllIds() {
                    return Collections.emptyList();
                }

                @Override
                public void addEntry(String key, Integer id) {
                    // NOP
                }

                @Override
                public void updateSharedMaps() {
                    // NOP
                }

                @Override
                public void reset() {
                    // NOP
                }

                @Override
                public void clearLocalMaps() {
                    // NOP
                }

                @Override
                public void prefill(Map content) {
                    // NOP
                }
            };
            return cache;
        }

        @Override
        public IIdNameCache<Integer> getResourceTypeNameCache() {
            return null;
        }

        @Override
        public INameIdCache<Integer> getParameterNameCache() {
            return null;
        }

        @Override
        public void transactionCommitted() {
            // No Operation
        }

        @Override
        public void transactionRolledBack() {
            // No Operation
        }

        @Override
        public ILogicalResourceIdentCache getLogicalResourceIdentCache() {
            // TODO Auto-generated method stub
            return null;
        }
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
        properties.load(new FileInputStream("src/test/resources/test.erase.properties"));
        EraseTestMain main = new EraseTestMain(properties, args);
        main.determineFlavorAndSchema();
        main.processArgs();
        main.erase();
    }
}