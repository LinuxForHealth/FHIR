/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.test.util;

import static com.ibm.fhir.schema.app.util.CommonUtil.getDbAdapter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.core.FHIRVersionParam;
import com.ibm.fhir.core.util.ResourceTypeHelper;
import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.JdbcPropertyAdapter;
import com.ibm.fhir.database.utils.common.JdbcTarget;
import com.ibm.fhir.database.utils.db2.Db2Translator;
import com.ibm.fhir.database.utils.derby.DerbyTranslator;
import com.ibm.fhir.database.utils.model.DbType;
import com.ibm.fhir.database.utils.postgres.PostgresTranslator;
import com.ibm.fhir.schema.app.util.CommonUtil;

/**
 * Utility to count the number of parameter values stored in
 * a database
 */
public class ParameterCounter {
    private static final Logger logger = Logger.getLogger(ParameterCounter.class.getName());
    private final Properties databaseProperties = new Properties();
    private String databasePropertiesFile = "db.properties";
    private DbType dbType;
    private IDatabaseTranslator translator;
    private String schemaName;

    public void parseArgs(String[] args) {
        for (int i=0; i<args.length; i++) {
            String arg = args[i];
            switch (arg) {
            case "--db-type":
                if (i < args.length + 1) {
                    this.dbType = DbType.from(args[++i]);
                } else {
                    throw new IllegalArgumentException("missing value for --db-type");
                }
                break;
            case "--schema-name":
                if (i < args.length + 1) {
                    this.schemaName = args[++i];
                } else {
                    throw new IllegalArgumentException("missing value for --schema-name");
                }
                break;
            case "--db-properties":
                if (i < args.length + 1) {
                    this.databasePropertiesFile = args[++i];
                } else {
                    throw new IllegalArgumentException("missing value for --db-properties");
                }
                break;
            default:
                throw new IllegalArgumentException("Bad arg: " + arg);
            }
        }
    }

    /**
     * Load the database properties file and set up the database configuration
     */
    public void configure() {
        switch (dbType) {
        case DERBY:
            translator = new DerbyTranslator();
            if (schemaName == null) {
                schemaName = "APP";
            }
            break;
        case POSTGRESQL:
            translator = new PostgresTranslator();
            if (schemaName == null) {
                schemaName = "FHIRDATA";
            }
            break;
        case DB2:
        default:
            translator = new Db2Translator();
            if (schemaName == null) {
                schemaName = "FHIRDATA";
            }
            break;
        }

        if (databasePropertiesFile == null) {
            throw new IllegalArgumentException("Must give --db-properties");
        }

        try (InputStream is = new FileInputStream(databasePropertiesFile)) {
            databaseProperties.load(is);
        } catch (IOException x) {
            throw new IllegalArgumentException(x);
        }
    }

    /**
     * Run the counts and print the results
     */
    public void process() {
        try (Connection c = createConnection()) {
            try {
                JdbcTarget target = new JdbcTarget(c);
                IDatabaseAdapter adapter = getDbAdapter(dbType, target);
                process(adapter);
            } catch (Exception x) {
                c.rollback();
                throw x;
            }
            c.commit();
        } catch (SQLException x) {
            throw translator.translate(x);
        }

    }

    protected void process(IDatabaseAdapter adapter) {
        Set<String> resourceTypes = ResourceTypeHelper.getR4bResourceTypesFor(FHIRVersionParam.VERSION_43);

        final List<String> paramTables = Arrays.asList("STR_VALUES", "NUMBER_VALUES", "DATE_VALUES", "RESOURCE_TOKEN_REFS", "QUANTITY_VALUES", "LATLNG_VALUES");

        for (String pt: paramTables) {
            int parameterCount = 0;
            for (String resourceType: resourceTypes) {
                logger.fine(() -> "Counting " + resourceType + "_" + pt);
                ParameterCounterDAO dao = new ParameterCounterDAO(schemaName, resourceType, pt);
                parameterCount += adapter.runStatement(dao);
            }

            System.out.println(String.format("COUNT [%20s] = %d", pt, parameterCount));
        }
    }

    /**
     * Get a connection to the database configured in the db.properties file
     * @return
     */
    protected Connection createConnection() {
        Properties connectionProperties = new Properties();
        JdbcPropertyAdapter adapter = CommonUtil.getPropertyAdapter(dbType, this.databaseProperties);
        adapter.getExtraProperties(connectionProperties);

        String url = translator.getUrl(this.databaseProperties);
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
     * Main entry point
     * @param args
     */
   public static void main(String[] args) {
        ParameterCounter m = new ParameterCounter();
        try {
            m.parseArgs(args);
            m.configure();
            m.process();
        } catch (Exception x) {
            logger.log(Level.SEVERE, x.getMessage(), x);
        }
    }
}