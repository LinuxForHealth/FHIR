/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.persistence.proxy.rm.test;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.logging.Logger;

/**
 * @author padams
 *
 */
public class TestConnection implements Connection {
    private static final Logger log = Logger.getLogger(TestConnection.class.getName());

    private Connection delegate;
    private String dsLabel;

    public TestConnection(Connection connection, String dsLabel) {
        log.entering(this.getClass().getName(), "TestConnection ctor");
        this.delegate = connection;
        this.dsLabel = dsLabel;
        log.exiting(this.getClass().getName(), methodLabel("TestConnection ctor"));
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("unwrap"), new Object[] {
                "iface", iface.getName()
        });
        try {
            return delegate.unwrap(iface);
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("unwrap"));
        }
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("isWrapperFor"), new Object[] {
                "iface", iface.getName()
        });
        try {
            return delegate.isWrapperFor(iface);
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("isWrapperFor"));
        }
    }

    public Statement createStatement() throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("createStatement"));
        try {
            return delegate.createStatement();
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("createStatement"));
        }
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("prepareStatement"));
        log.fine("SQL statement: " + sql);
        try {
            return delegate.prepareStatement(sql);
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("prepareStatement"));
        }
    }

    public CallableStatement prepareCall(String sql) throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("prepareStatement"));
        log.fine("SQL statement: " + sql);
        try {
            return delegate.prepareCall(sql);
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("prepareStatement"));
        }
    }

    public String nativeSQL(String sql) throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("nativeSQL"));
        log.fine("SQL statement: " + sql);
        try {
            return delegate.nativeSQL(sql);
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("nativeSQL"));
        }
    }

    public void setAutoCommit(boolean autoCommit) throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("setAutoCommit"), new Object[] {
                "autoCommit", autoCommit
        });
        try {
            delegate.setAutoCommit(autoCommit);
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("setAutoCommit"));
        }
    }

    public boolean getAutoCommit() throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("getAutoCommit"));
        try {
            return delegate.getAutoCommit();
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("getAutoCommit"));
        }
    }

    public void commit() throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("commit"));
        try {
            delegate.commit();
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("commit"));
        }
    }

    public void rollback() throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("rollback"));
        try {
            delegate.rollback();
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("rollback"));
        }
    }

    public void close() throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("close"));
        try {
            delegate.close();
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("close"));
        }
    }

    public boolean isClosed() throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("isClosed"));
        try {
            return delegate.isClosed();
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("isClosed"));
        }
    }

    public DatabaseMetaData getMetaData() throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("getMetaData"));
        try {
            return delegate.getMetaData();
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("getMetaData"));
        }
    }

    public void setReadOnly(boolean readOnly) throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("setReadOnly"), new Object[] {
                "readOnly", readOnly
        });
        try {
            delegate.setReadOnly(readOnly);
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("setReadOnly"));
        }
    }

    public boolean isReadOnly() throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("isReadOnly"));
        try {
            return delegate.isReadOnly();
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("isReadOnly"));
        }
    }

    public void setCatalog(String catalog) throws SQLException {
        delegate.setCatalog(catalog);
    }

    public String getCatalog() throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("getCatalog"));
        try {
            return delegate.getCatalog();
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("getCatalog"));
        }
    }

    public void setTransactionIsolation(int level) throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("setTransactionIsolation"), new Object[] {
                "level", level
        });
        try {
            delegate.setTransactionIsolation(level);
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("setTransactionIsolation"));
        }
    }

    public int getTransactionIsolation() throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("getTransactionIsolation"));
        try {
            return delegate.getTransactionIsolation();
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("getTransactionIsolation"));
        }
    }

    public SQLWarning getWarnings() throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("getWarnings"));
        try {
            return delegate.getWarnings();
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("getWarnings"));
        }
    }

    public void clearWarnings() throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("clearWarnings"));
        try {
            delegate.clearWarnings();
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("clearWarnings"));
        }
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("createStatement"), new Object[] {
                "resultSetType", resultSetType, "resultSetConcurrency", resultSetConcurrency
        });
        try {
            return delegate.createStatement(resultSetType, resultSetConcurrency);
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("createStatement"));
        }
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("prepareStatement"), new Object[] {
                "resultSetType", resultSetType, "resultSetConcurrency", resultSetConcurrency
        });
        log.fine("SQL statement: " + sql);
        try {
            return delegate.prepareStatement(sql, resultSetType, resultSetConcurrency);
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("prepareStatement"));
        }
    }

    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("prepareCall"), new Object[] {
                "resultSetType", resultSetType, "resultSetConcurrency", resultSetConcurrency
        });
        log.fine("SQL statement: " + sql);
        try {
            return delegate.prepareCall(sql, resultSetType, resultSetConcurrency);
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("prepareCall"));
        }
    }

    public Map<String, Class<?>> getTypeMap() throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("getTypeMap"));
        try {
            return delegate.getTypeMap();
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("getTypeMap"));
        }
    }

    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("setTypeMap"));
        try {
            delegate.setTypeMap(map);
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("setTypeMap"));
        }
    }

    public void setHoldability(int holdability) throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("setHoldability"), new Object[] {
                "holdability", holdability
        });
        try {
            delegate.setHoldability(holdability);
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("setHoldability"));
        }
    }

    public int getHoldability() throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("getHoldability"));
        try {
            return delegate.getHoldability();
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("getHoldability"));
        }
    }

    public Savepoint setSavepoint() throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("setSavepoint"));
        try {
            return delegate.setSavepoint();
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("setSavepoint"));
        }
    }

    public Savepoint setSavepoint(String name) throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("setSavepoint(String)"), new Object[] {
                "name", name
        });
        try {
            return delegate.setSavepoint(name);
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("setSavepoint(String)"));
        }
    }

    public void rollback(Savepoint savepoint) throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("rollback"));
        try {
            delegate.rollback(savepoint);
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("rollback"));
        }
    }

    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("releaseSavepoint"));
        try {
            delegate.releaseSavepoint(savepoint);
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("releaseSavepoint"));
        }
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("createStatement"), new Object[] {
                "resultSetType", resultSetType, "resultSetConcurrency", resultSetConcurrency, "resultSetHoldability", resultSetHoldability
        });
        try {
            return delegate.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("createStatement"));
        }
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("prepareStatement"), new Object[] {
                "resultSetType", resultSetType, "resultSetConcurrency", resultSetConcurrency, "resultSetHoldability", resultSetHoldability
        });
        log.fine("SQL statement: " + sql);
        try {
            return delegate.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("prepareStatement"));
        }
    }

    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("prepareCall"), new Object[] {
                "resultSetType", resultSetType, "resultSetConcurrency", resultSetConcurrency, "resultSetHoldability", resultSetHoldability
        });
        log.fine("SQL statement: " + sql);
        try {
            return delegate.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("prepareCall"));
        }
    }

    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("prepareStatement"), new Object[]{"autoGeneratedKeys", autoGeneratedKeys});
        log.fine("SQL statement: " + sql);
        try {
            return delegate.prepareStatement(sql, autoGeneratedKeys);
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("prepareStatement"));
        }
    }

    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("prepareStatement"), new Object[]{"columnIndexes", columnIndexes});
        log.fine("SQL statement: " + sql);
        try {
            return delegate.prepareStatement(sql, columnIndexes);
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("prepareStatement"));
        }
    }

    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("prepareStatement"), new Object[]{"columnNames", columnNames});
        log.fine("SQL statement: " + sql);
        try {
            return delegate.prepareStatement(sql, columnNames);
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("prepareStatement"));
        }
    }

    public Clob createClob() throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("createClob"));
        try {
            return delegate.createClob();
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("createClob"));
        }
    }

    public Blob createBlob() throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("createBlob"));
        try {
            return delegate.createBlob();
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("createBlob"));
        }
    }

    public NClob createNClob() throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("createNClob"));
        try {
            return delegate.createNClob();
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("createNClob"));
        }
    }

    public SQLXML createSQLXML() throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("createSQLXML"));
        try {
            return delegate.createSQLXML();
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("createSQLXML"));
        }
    }

    public boolean isValid(int timeout) throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("isValid"), new Object[]{"timeout", timeout});
        try {
            return delegate.isValid(timeout);
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("isValid"));
        }
    }

    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        log.entering(this.getClass().getName(), methodLabel("setClientInfo"), new Object[]{"name", name, "value", value});
        try {
            delegate.setClientInfo(name, value);
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("setClientInfo"));
        }
    }

    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        log.entering(this.getClass().getName(), methodLabel("setClientInfo"));
        log.fine("Properties: " + properties.toString());
        try {
            delegate.setClientInfo(properties);
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("setClientInfo"));
        }
    }

    public String getClientInfo(String name) throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("getClientInfo"), new Object[]{"name", name});
        try {
            return delegate.getClientInfo(name);
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("getClientInfo"));
        }
    }

    public Properties getClientInfo() throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("getClientInfo"));
        try {
            return delegate.getClientInfo();
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("getClientInfo"));
        }
    }

    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("createArrayOf"), new Object[]{"typeName", typeName});
        try {
            return delegate.createArrayOf(typeName, elements);
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("createArrayOf"));
        }
    }

    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("createStruct"), new Object[]{"typeName", typeName});
        try {
            return delegate.createStruct(typeName, attributes);
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("createStruct"));
        }
    }

    public void setSchema(String schema) throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("setSchema"), new Object[]{"schema", schema});
        try {
            delegate.setSchema(schema);
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("setSchema"));
        }
    }

    public String getSchema() throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("getSchema"));
        try {
            return delegate.getSchema();
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("getSchema"));
        }
    }

    public void abort(Executor executor) throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("abort"));
        try {
            delegate.abort(executor);
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("abort"));
        }
    }

    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("setNetworkTimeout"), new Object[]{"milliseconds", milliseconds});
        try {
            delegate.setNetworkTimeout(executor, milliseconds);
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("setNetworkTimeout"));
        }
    }

    public int getNetworkTimeout() throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("getNetworkTimeout"));
        try {
            return delegate.getNetworkTimeout();
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("getNetworkTimeout"));
        }
    }

    private String methodLabel(String method) {
        return method + "[" + dsLabel + "]";
    }
}
