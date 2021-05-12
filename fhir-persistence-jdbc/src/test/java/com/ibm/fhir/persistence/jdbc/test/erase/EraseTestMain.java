/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.test.erase;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IConnectionProvider;
import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.JdbcPropertyAdapter;
import com.ibm.fhir.database.utils.common.JdbcTarget;
import com.ibm.fhir.database.utils.db2.Db2Adapter;
import com.ibm.fhir.database.utils.db2.Db2PropertyAdapter;
import com.ibm.fhir.database.utils.derby.DerbyAdapter;
import com.ibm.fhir.database.utils.derby.DerbyPropertyAdapter;
import com.ibm.fhir.database.utils.model.DbType;
import com.ibm.fhir.database.utils.postgres.PostgresAdapter;
import com.ibm.fhir.database.utils.postgres.PostgresPropertyAdapter;
import com.ibm.fhir.database.utils.postgres.PostgresTranslator;
import com.ibm.fhir.persistence.ResourceEraseRecord;
import com.ibm.fhir.persistence.erase.EraseDTO;
import com.ibm.fhir.persistence.jdbc.connection.FHIRDbFlavor;
import com.ibm.fhir.persistence.jdbc.connection.FHIRDbFlavorImpl;
import com.ibm.fhir.persistence.jdbc.dao.EraseResourceDAO;

/**
 * EraseTestMain is a test driver for the EraseResourceDAO so it can be debugged during development.
 * input is ResourceType LogicalId
 * Main Patient 1791f19a126-28b0239c-f1a1-4d15-979f-920af8d12446
 *
 * Example output:
 * <code>
 * May 12, 2021 1:36:39 PM com.ibm.fhir.persistence.jdbc.test.erase.EraseTestMain createConnection
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


    public EraseTestMain(Properties properties, String[] args) {
        this.args = args;
        this.inProperties = properties;
    }

    protected void erase() throws Exception {
        try {
            try (Connection c = createConnection()) {
                System.out.println("Got a Connection");
                try {
                    FHIRDbFlavor flavor = new FHIRDbFlavorImpl(dbType, true);
                    EraseResourceDAO dao = new EraseResourceDAO(c, translator, schemaName, flavor, null, null);

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
    public void determineFlavorAnSchema() {
        if (inProperties.getProperty("type") != null && inProperties.getProperty("type").equals("db2")) {
            dbType = DbType.DB2;
            if (inProperties.getProperty("db2.schemaName") != null) {
                schemaName = inProperties.getProperty("db2.schemaName");
            }
        } else {
            if (inProperties.getProperty("postgres.schemaName") != null) {
                schemaName = inProperties.getProperty("db2.schemaName");
            }
        }
    }

    /**
     * process args
     */
    public void processArgs() {
        String prefix = "db2.";
        if (dbType == DbType.POSTGRESQL) {
            prefix = "postgres.";
        }
        dbProperties = new Properties();
        for(Entry<Object, Object> prop : inProperties.entrySet()) {
            String key = (String) prop.getKey();
            if (key.startsWith(prefix)) {
                dbProperties.put(key.replace(prefix, ""), prop.getValue());
            }
        }
        resourceType = args[0];
        logicalId = args[1];
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
        properties.load(new FileInputStream("src/test/resources/test.erase.properties"));
        EraseTestMain main = new EraseTestMain(properties, args);
        main.determineFlavorAnSchema();
        main.processArgs();
        main.erase();
    }

    protected Connection createConnection() {
        Properties connectionProperties = new Properties();
        JdbcPropertyAdapter adapter = getPropertyAdapter(dbType, dbProperties);
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
     * Load the driver class
     */
    public static void loadDriver(IDatabaseTranslator translator) {
        try {
            Class.forName(translator.getDriverClassName());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    public static JdbcPropertyAdapter getPropertyAdapter(DbType dbType, Properties props) {
        switch (dbType) {
        case DB2:
            return new Db2PropertyAdapter(props);
        case DERBY:
            return new DerbyPropertyAdapter(props);
        case POSTGRESQL:
            return new PostgresPropertyAdapter(props);
        default:
            throw new IllegalStateException("Unsupported db type: " + dbType);
        }
    }

    public static IDatabaseAdapter getDbAdapter(DbType dbType, JdbcTarget target) {
        switch (dbType) {
        case DB2:
            return new Db2Adapter(target);
        case DERBY:
            return new DerbyAdapter(target);
        case POSTGRESQL:
            return new PostgresAdapter(target);
        default:
            throw new IllegalStateException("Unsupported db type: " + dbType);
        }
    }

    public static IDatabaseAdapter getDbAdapter(DbType dbType, IConnectionProvider connectionProvider) {
        switch (dbType) {
        case DB2:
            return new Db2Adapter(connectionProvider);
        case DERBY:
            return new DerbyAdapter(connectionProvider);
        case POSTGRESQL:
            return new PostgresAdapter(connectionProvider);
        default:
            throw new IllegalStateException("Unsupported db type: " + dbType);
        }
    }
}