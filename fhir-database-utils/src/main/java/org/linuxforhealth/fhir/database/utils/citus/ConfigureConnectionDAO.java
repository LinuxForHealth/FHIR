/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.citus;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.linuxforhealth.fhir.database.utils.api.IDatabaseStatement;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseTranslator;

/**
 * DAO to configure the Citus database connection when performing schema build
 * activities. This must be performed before any of the following UDFs are called:
 * <ul>
 * <li>create_distributed_table
 * <li>create_reference_table
 * </ul>
 * to avoid the following error:
 * <pre>
 * org.postgresql.util.PSQLException: ERROR: cannot modify table "common_token_values" because there was a parallel operation on a distributed table in the transaction
 * Detail: When there is a foreign key to a reference table, Citus needs to perform all operations over a single connection per node to ensure consistency.
 * Hint: Try re-running the transaction with "SET LOCAL citus.multi_shard_modify_mode TO 'sequential';"
 * </pre>
 */
public class ConfigureConnectionDAO implements IDatabaseStatement {

    /**
     * Public constructor
     */
    public ConfigureConnectionDAO() {
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {
        // we need this behavior for all transactions on this connection, so
        // we use SET SESSION instead of SET LOCAL
        final String SQL = "SET SESSION citus.multi_shard_modify_mode TO 'sequential'";

        try (Statement s = c.createStatement()) {
            s.executeUpdate(SQL);
        } catch (SQLException x) {
            // Translate the exception into something a little more meaningful
            // for this database type and application
            throw translator.translate(x);
        }
    }
}