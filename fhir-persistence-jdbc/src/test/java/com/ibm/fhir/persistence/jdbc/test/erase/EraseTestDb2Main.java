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
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IConnectionProvider;
import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.JdbcPropertyAdapter;
import com.ibm.fhir.database.utils.common.JdbcTarget;
import com.ibm.fhir.database.utils.db2.Db2Adapter;
import com.ibm.fhir.database.utils.db2.Db2PropertyAdapter;
import com.ibm.fhir.database.utils.db2.Db2Translator;
import com.ibm.fhir.database.utils.derby.DerbyAdapter;
import com.ibm.fhir.database.utils.derby.DerbyPropertyAdapter;
import com.ibm.fhir.database.utils.model.DbType;
import com.ibm.fhir.database.utils.postgres.PostgresAdapter;
import com.ibm.fhir.database.utils.postgres.PostgresPropertyAdapter;
import com.ibm.fhir.persistence.ResourceEraseRecord;
import com.ibm.fhir.persistence.erase.EraseDTO;
import com.ibm.fhir.persistence.jdbc.connection.FHIRDbFlavor;
import com.ibm.fhir.persistence.jdbc.connection.FHIRDbFlavorImpl;
import com.ibm.fhir.persistence.jdbc.connection.SchemaNameFromProps;
import com.ibm.fhir.persistence.jdbc.connection.SchemaNameImpl;
import com.ibm.fhir.persistence.jdbc.connection.SchemaNameSupplier;
import com.ibm.fhir.persistence.jdbc.dao.EraseResourceDAO;

/**
 * EraseTestMain is a test driver for the EraseResourceDAO
 * so it can be debugged during development.
 */
public class EraseTestDb2Main {
    private static final String CLASSNAME = EraseTestDb2Main.class.getSimpleName();
    private static final Logger logger = Logger.getLogger(CLASSNAME);

    private IDatabaseTranslator translator = new Db2Translator();
    private DbType dbType = DbType.DB2;
    private Properties properties = null;

    public EraseTestDb2Main(Properties properties) {
        this.properties = properties;
    }

    protected void erase() throws Exception {
        try {
            try (Connection c = createConnection()) {
                try (Statement stmt = c.createStatement()){
                    // Set the Tenant Id
                    stmt.execute("SET FHIR_ADMIN.SV_TENANT_ID = 1");

                    FHIRDbFlavor flavor = new FHIRDbFlavorImpl(DbType.DB2, true);
                    EraseResourceDAO dao = new EraseResourceDAO(c, translator, "FHIRDATA", flavor, null, null);

                    SchemaNameSupplier schemaNameSupplier = new SchemaNameImpl(new SchemaNameFromProps(properties));
                    schemaNameSupplier.getSchemaForRequestContext(c);


                    ResourceEraseRecord record = new ResourceEraseRecord(true);
                    EraseDTO eraseDto = new EraseDTO();
                    eraseDto.setResourceType("Patient");

                    eraseDto.setLogicalId("178f70bd344-f0a13e93-88b9-4964-86ce-6376759bab55");
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
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
        properties.load(new FileInputStream("src/test/resources/test.erase.db2.properties"));
        EraseTestDb2Main main = new EraseTestDb2Main(properties);
        main.erase();
    }

    protected Connection createConnection() {
        Properties connectionProperties = new Properties();
        JdbcPropertyAdapter adapter = getPropertyAdapter(dbType, properties);
        adapter.getExtraProperties(connectionProperties);

        String url = translator.getUrl(properties);
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
