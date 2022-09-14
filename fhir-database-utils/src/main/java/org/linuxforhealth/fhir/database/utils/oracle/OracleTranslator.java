/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.oracle;

import java.sql.SQLException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.linuxforhealth.fhir.database.utils.api.ConnectionDetails;
import org.linuxforhealth.fhir.database.utils.api.ConnectionException;
import org.linuxforhealth.fhir.database.utils.api.DataAccessException;
import org.linuxforhealth.fhir.database.utils.api.DuplicateSchemaException;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseTranslator;
import org.linuxforhealth.fhir.database.utils.api.LockException;
import org.linuxforhealth.fhir.database.utils.api.UndefinedNameException;
import org.linuxforhealth.fhir.database.utils.api.UniqueConstraintViolationException;
import org.linuxforhealth.fhir.database.utils.common.DataDefinitionUtil;
import org.linuxforhealth.fhir.database.utils.model.DbType;
import org.linuxforhealth.fhir.database.utils.postgres.PostgresPropertyAdapter;

/**
 * translates database access to Oracle supported access.
 */
public class OracleTranslator implements IDatabaseTranslator {
    private static final Logger logger = Logger.getLogger(OracleTranslator.class.getName());

    @Override
    public String addForUpdate(String sql) {
        return sql + " FOR UPDATE";
    }

    @Override
    public boolean isDerby() {
        return false;
    }

    @Override
    public String globalTempTableName(String tableName) {
        return "SYS." + tableName;
    }

    @Override
    public String createGlobalTempTable(String ddl) {
        return "DECLARE " + ddl;
    }

    @Override
    public boolean isDuplicate(SQLException x) {
        // Class Code 23: Constraint Violation
        // Refer to https://www.postgresql.org/docs/12/errcodes-appendix.html for more detail
        return "23505".equals(x.getSQLState());
    }

    @Override
    public boolean isAlreadyExists(SQLException x) {
        return "42710".equals(x.getSQLState());
    }

    @Override
    public boolean isLockTimeout(SQLException x) {
        return false;
    }

    @Override
    public boolean isIndexUseSchemaPrefix() {
        return true;
    }

    @Override
    public boolean isDeadlock(SQLException x) {
        final String sqlState = x.getSQLState();
        return "04020".equals(sqlState);
    }

    @Override
    public boolean isConnectionError(SQLException x) {
        String sqlState = x.getSQLState();
        return sqlState != null && sqlState.startsWith("08");
    }

    @Override
    public DataAccessException translate(SQLException x) {
        if (isDeadlock(x)) {
            return new LockException(x, true);
        } else if (isLockTimeout(x)) {
            return new LockException(x, false);
        } else if (isConnectionError(x)) {
            return new ConnectionException(x);
        } else if (isDuplicate(x)) {
            return new UniqueConstraintViolationException(x);
        } else if (isUndefinedName(x)) {
            return new UndefinedNameException(x);
        } else if(isDuplicateSchema(x)) {
            return new DuplicateSchemaException(x);
        } else {
            return new DataAccessException(x);
        }
    }

    /**
     * @implNote sometimes this is wrapped in a cause by.
     * @see https://www.postgresql.org/docs/9.4/errcodes-appendix.html
     */
    public boolean isDuplicateSchema(SQLException x) {
        Throwable inter = x.getCause();
        SQLException temp = null;
        if (inter instanceof SQLException) {
            temp = ((SQLException) inter);
        }
        return "42P06".equals(x.getSQLState()) || (temp != null && "42P06".equals(temp.getSQLState()));
    }

    @Override
    public boolean isUndefinedName(SQLException x) {
        // TODO
        String sqlState = x.getSQLState();
        return "42704".equals(sqlState) ||
               "42883".equals(sqlState) ||
               "42P01".equals(sqlState) ||
               "42P02".equals(sqlState);
    }

    @Override
    public void fillProperties(Properties p, ConnectionDetails cd) {
        p.put("user", cd.getUser());
        p.put("password", cd.getPassword());

        // TODO inject TLS connection properties
    }

    @Override
    public String timestampDiff(String left, String right, String alias) {
        // diff is left - right, e.g. current - start time
        if (alias == null || alias.isEmpty()) {
            return String.format("(%s - %s)", left, right);
        }
        else {
            return String.format("(%s - %s) AS %s", left, right, alias);
        }
    }

    @Override
    public String reorgTableCommand(String tableName) {
        // REORG TABLE not supported by Oracle
        throw new UnsupportedOperationException("reorg table is not supported!");
    }

    @Override
    public String getDriverClassName() {
        return "oracle.jdbc.driver.OracleDriver";
    }

    @Override
    public String getUrl(Properties connectionProperties) {
        PostgresPropertyAdapter adapter = new PostgresPropertyAdapter(connectionProperties);
        StringBuilder jdbcUrl = new StringBuilder();
        jdbcUrl.append("jdbc:oracle:thin:@//");
        jdbcUrl.append(adapter.getHost());
        jdbcUrl.append(':');
        jdbcUrl.append(adapter.getPort());
        jdbcUrl.append('/');
        jdbcUrl.append(adapter.getDatabase());

        // Filter out comments and db specific values
        Map<Object, Object> entries = connectionProperties.entrySet()
                .stream().filter(p -> !p.getKey().toString().startsWith("db.")
                    && !p.getKey().toString().startsWith("#")
                    && !p.getKey().toString().equals("user")
                    && !p.getKey().toString().equals("password"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        boolean first = true;
        if(entries != null && !entries.isEmpty()) {
            jdbcUrl.append('?');
            // The following is really all strings and casted to such an object
            for(Entry<Object, Object> entry : entries.entrySet()) {
                if (!first) {
                    jdbcUrl.append('&');
                } else {
                    first = false;
                }
                jdbcUrl.append(entry.getKey());
                jdbcUrl.append('=');
                jdbcUrl.append(entry.getValue());
            }
        }
        return jdbcUrl.toString();
    }

    @Override
    public boolean clobSupportsInline() {
        return false;
    }

    @Override
    public DbType getType() {
        return DbType.ORACLE;
    }

    @Override
    public String dualTableName() {
        return "DUAL";
    }

    @Override
    public String currentTimestampString() {
        return "CURRENT_TIMESTAMP";
    }

    @Override
    public String limit(String arg) {
        return "LIMIT " + arg;
    }

    @Override
    public String pagination(int offset, int rowsPerPage) {
        StringBuilder result = new StringBuilder();
        if (offset == 0) {
            result.append("LIMIT ");
            result.append(rowsPerPage);
        } else {
            result.append("OFFSET ");
            result.append(offset);
            result.append(" LIMIT ");
            result.append(rowsPerPage);
        }
        return result.toString();
    }

    @Override
    public Optional<Integer> maximumQueryParameters() {
        return Optional.of(Integer.valueOf(32767));
    }

    @Override
    public boolean isFamilyPostgreSQL() {
        return false;
    }

    @Override
    public String selectSequenceNextValue(String schemaName, String sequenceName) {
        final String qname = DataDefinitionUtil.getQualifiedName(schemaName, sequenceName);
        return "SELECT " + qname + ".NEXTVAL FROM DUAL";
    }

    @Override
    public String nextValue(String schemaName, String sequenceName) {
        return sequenceName + ".NEXTVAL" ;
    }

    @Override
    public String createSequence(String name, int cache) {
        return "CREATE SEQUENCE " + name + " CACHE " + cache;
    }

    @Override
    public String dropForeignKeyConstraint(String qualifiedTableName, String constraintName) {
        return "ALTER TABLE " + qualifiedTableName + " DROP FOREIGN KEY " + constraintName;
    }

    @Override
    public String dropView(String qualifiedViewName) {
        return "DROP VIEW " + qualifiedViewName;
    }
}