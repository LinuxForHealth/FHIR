/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.patch;

import static com.ibm.fhir.schema.app.util.CommonUtil.configureLogger;
import static com.ibm.fhir.schema.app.util.CommonUtil.getDbAdapter;
import static com.ibm.fhir.schema.app.util.CommonUtil.getPropertyAdapter;
import static com.ibm.fhir.schema.app.util.CommonUtil.loadDriver;
import static com.ibm.fhir.schema.app.util.CommonUtil.logClasspath;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.core.FHIRVersionParam;
import com.ibm.fhir.core.util.ResourceTypeHelper;
import com.ibm.fhir.database.utils.api.DatabaseNotReadyException;
import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;
import com.ibm.fhir.database.utils.common.DropForeignKeyConstraint;
import com.ibm.fhir.database.utils.common.JdbcPropertyAdapter;
import com.ibm.fhir.database.utils.common.JdbcTarget;
import com.ibm.fhir.database.utils.db2.Db2Translator;
import com.ibm.fhir.database.utils.derby.DerbyTranslator;
import com.ibm.fhir.database.utils.model.DbType;
import com.ibm.fhir.database.utils.postgres.PostgresTranslator;
import com.ibm.fhir.schema.app.menu.Menu;

/**
 * Utility to patch an old schema for use-cases which fall outside the standard
 * automatic schema migration process
 */
public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static final int EXIT_OK = 0; // validation was successful
    private static final int EXIT_BAD_ARGS = 1; // invalid CLI arguments
    private static final int EXIT_RUNTIME_ERROR = 2; // programming error or service issue

    private static final Menu menu = new Menu();

    private static final double NANOS = 1e9;

    // Properties accumulated as we parse args and read configuration files
    private final Properties properties = new Properties();

    // Default Values for schema names
    public static final String ADMIN_SCHEMANAME = "FHIR_ADMIN";
    public static final String DATA_SCHEMANAME = "FHIRDATA";

    // The schema we will use for all the FHIR data tables
    private String schemaName = DATA_SCHEMANAME;

    // Arguments requesting we drop the objects from the schema
    private boolean dropOldConstraints = true;

    // The database type being populated (default: Db2)
    private DbType dbType = DbType.DB2;
    private IDatabaseTranslator translator = new Db2Translator();

    // Composite properties
    private static final String COMP = "COMP";
    private static final int MAX_COMP = 3;
    private int maxComposites = MAX_COMP;

    // What status to leave with
    private int exitStatus = EXIT_OK;

    //-----------------------------------------------------------------------------------------------------------------
    // The following method is related to the common methods and functions
    /**
     * @return a created connection to the selected database
     */
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
     * Create the schemas
     */
    protected void patchSchema() {
        try {
            try (Connection c = createConnection()) {
                try {
                    JdbcTarget target = new JdbcTarget(c);
                    IDatabaseAdapter adapter = getDbAdapter(dbType, target);

                    if (this.dropOldConstraints) {
                        dropOldConstraints(adapter);
                    }
                } catch (Exception x) {
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
     * Drop constraints which were created as part of an old schema design and are no
     * longer tracked as part of the model (breaking the automatic schema migration
     * handling).
     * @param adapter the adapter wrapping the target database connection
     */
    private void dropOldConstraints(IDatabaseAdapter adapter) {
        Set<String> resourceTypes = ResourceTypeHelper.getR4bResourceTypesFor(FHIRVersionParam.VERSION_43);

        final List<String> params = Arrays.asList("STR", "NUMBER", "DATE", "TOKEN", "QUANTITY", "LATLNG");

        for (String resourceType: resourceTypes) {
            final String tableName = resourceType + "_COMPOSITES";
            for (String param: params) {
                for (int i = 1; i <= maxComposites; i++) {
                    String comp = COMP + i;
                    final String constraintName = "FK_" + tableName + "_" + comp + "_" + param;
                    dropFKConstraint(adapter, tableName, constraintName);
                }
            }
        }
    }

    /**
     * Safely drop the named constraint, catching any exceptions
     * @param adapter
     * @param tableName
     * @param constraintName
     */
    private void dropFKConstraint(IDatabaseAdapter adapter, String tableName, String constraintName) {
        logger.info("DROP " + schemaName + "." + tableName + " FK CONSTRAINT " + constraintName + "...");
        try {
            DropForeignKeyConstraint dropper = new DropForeignKeyConstraint(schemaName, tableName, constraintName);
            adapter.runStatement(dropper);
            logger.info("DROP " + schemaName + "." + tableName + " FK CONSTRAINT " + constraintName + ": DONE");
        } catch (Exception x) {
            logger.info("DROP " + schemaName + "." + tableName + " FK CONSTRAINT " + constraintName + ": " + x.getMessage());
        }
    }

    //-----------------------------------------------------------------------------------------------------------------
    // The following methods are related to parsing arguments and action selection
    /**
     * Parse the command-line arguments, building up the environment and
     * establishing
     * the run-list
     *
     * @param args
     */
    protected void parseArgs(String[] args) {
        // Arguments are pretty simple, so we go with a basic switch instead of having
        // yet another dependency (e.g. commons-cli).
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            switch (arg) {
            case "--prop-file":
                if (++i < args.length) {
                    loadPropertyFile(args[i]);
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--schema-name":
                if (++i < args.length) {
                    DataDefinitionUtil.assertValidName(args[i]);

                    // Force upper-case to avoid tricky-to-catch errors related to quoting names
                    this.schemaName = args[i];
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--prop":
                if (++i < args.length) {
                    // properties are given as name=value
                    addProperty(args[i]);
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--db-type":
                if (++i < args.length) {
                    this.dbType = DbType.from(args[i]);
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                switch (dbType) {
                case DERBY:
                    translator = new DerbyTranslator();
                    break;
                case POSTGRESQL:
                    translator = new PostgresTranslator();
                    break;
                case DB2:
                default:
                    break;
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid argument: " + arg);
            }
        }
    }

    /**
     * Read the properties from the given file
     *
     * @param filename
     */
    public void loadPropertyFile(String filename) {
        try (InputStream is = new FileInputStream(filename)) {
            properties.load(is);
            // Trim leading and trailing whitespace from property values (except password)
            for (Entry<Object, Object> entry : properties.entrySet()) {
                if (!"password".equals(entry.getKey())) {
                    String trimmedValue = entry.getValue().toString().trim();
                    if (!trimmedValue.equals(entry.getValue().toString())) {
                        logger.warning("Whitespace trimmed from value of property '" + entry.getKey() + "'");
                        entry.setValue(trimmedValue);
                    }
                }
            }
        } catch (IOException x) {
            throw new IllegalArgumentException(x);
        }
    }

    /**
     * Parse the given key=value string and add to the properties being collected
     *
     * @param pair
     */
    public void addProperty(String pair) {
        String[] kv = pair.split("=");
        if (kv.length == 2) {
            // Trim leading and trailing whitespace from property value (except password)
            if (!"password".equals(kv[0])) {
                String trimmedValue = kv[1].trim();
                if (!trimmedValue.equals(kv[1])) {
                    logger.warning("Whitespace trimmed from value of property '" + kv[0] + "'");
                }
                properties.put(kv[0], trimmedValue);
            } else {
                properties.put(kv[0], kv[1]);
            }
        } else {
            throw new IllegalArgumentException("Property must be defined as key=value, not: " + pair);
        }
    }

    /**
     * Process the requested operation
     */
    protected void process() {
        long start = System.nanoTime();
        loadDriver(translator);
        patchSchema();

        long elapsed = System.nanoTime() - start;
        logger.info(String.format("Processing took: %7.3f s", elapsed / NANOS));
    }

    /**
     * Get the program exit status from the environment
     *
     * @return
     */
    protected int getExitStatus() {
        return this.exitStatus;
    }

    /**
     * Write a final status message - useful for QA to review when checking the
     * output
     */
    protected void logStatusMessage(int status) {
        switch (status) {
        case EXIT_OK:
            logger.info("SCHEMA CHANGE: OK");
            break;
        case EXIT_BAD_ARGS:
            logger.severe("SCHEMA CHANGE: BAD ARGS");
            break;
        case EXIT_RUNTIME_ERROR:
            logger.severe("SCHEMA CHANGE: RUNTIME ERROR");
            break;
        default:
            logger.severe("SCHEMA CHANGE: RUNTIME ERROR");
            break;
        }
    }

    /**
     * Main entry point
     *
     * @param args
     */
    public static void main(String[] args) {
        logClasspath(logger);

        int exitStatus;
        Main m = new Main();
        try {
            configureLogger();
            m.parseArgs(args);
            m.process();
            exitStatus = m.getExitStatus();
        } catch(DatabaseNotReadyException x) {
            logger.log(Level.SEVERE, "The database is not yet available. Please re-try.", x);
            exitStatus = EXIT_RUNTIME_ERROR;
        } catch (IllegalArgumentException x) {
            logger.log(Level.SEVERE, "bad argument", x);
            menu.generateHelpMenu();
            exitStatus = EXIT_BAD_ARGS;
        } catch (Exception x) {
            logger.log(Level.SEVERE, "schema tool failed", x);
            exitStatus = EXIT_RUNTIME_ERROR;
        }

        // Write out a final status message to make it easy to see validation success/failure
        m.logStatusMessage(exitStatus);

        // almost certainly will get flagged during code-scan, but this is intentional,
        // as we genuinely want to exit with the correct status here. The code-scan tool
        // really ought to be able to see that this is a main function in a J2SE environment
        System.exit(exitStatus);
    }
}
