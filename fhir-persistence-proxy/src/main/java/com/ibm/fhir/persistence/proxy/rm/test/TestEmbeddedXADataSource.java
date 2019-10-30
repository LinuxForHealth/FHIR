/*
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.proxy.rm.test;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Hashtable;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.sql.XAConnection;

import org.apache.derby.iapi.jdbc.ResourceAdapter;
import org.apache.derby.jdbc.EmbeddedXADataSource;
import org.apache.derby.jdbc.EmbeddedXADataSourceInterface;

/**
 * This class is used in the testing of the XA resource recovery function within the proxy datasource class. It serves
 * as a wrapper class for the derby EmbeddedXADataSource class and provides additional function to artificially trigger
 * failures during the two-phase commit process.
 * 
 * @author padams
 *
 */
public class TestEmbeddedXADataSource implements EmbeddedXADataSourceInterface {
    private static final Logger log = Logger.getLogger(TestEmbeddedXADataSource.class.getName());

    private EmbeddedXADataSource delegate;

    private String failStep = "none";
    private int failCount = 1;

    public TestEmbeddedXADataSource() {
        log.entering(this.getClass().getName(), "TestEmbeddedXADataSource ctor");
        this.delegate = new EmbeddedXADataSource();
        log.exiting(this.getClass().getName(), "TestEmbeddedXADataSource ctor");
    }

    public String getFailStep() {
        return failStep;
    }

    public void setFailStep(String failStep) {
        this.failStep = failStep;
    }

    public int getFailCount() {
        return failCount;
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }

    /**
     * Returns a label suitable for trace logging to identify a particular instance of this class.
     * 
     * @return
     */
    private String getDataSourceLabel() {
        return this.getClass().getSimpleName() + ":" + getDatabaseName();
    }
    
    private String methodLabel(String method) {
        return method + "[" + getDataSourceLabel() + ":" + failStep + "]";
    }
    
    /**
     * We'll override this method so that we can insert our own class as a wrapper around the XAConnection served up by
     * the super class.
     */
    @Override
    public XAConnection getXAConnection() throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("getXAConnection"));
        try {
            XAConnection conn = delegate.getXAConnection();
            String dsLabel = getDataSourceLabel();
            XAConnection result = new TestXAConnection(conn, dsLabel, failStep, failCount);
            return result;
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("getXAConnection"));
        }
    }

    public final Reference getReference() throws NamingException {
        return delegate.getReference();
    }

    public final XAConnection getXAConnection(String paramString1, String paramString2) throws SQLException {
        return delegate.getXAConnection(paramString1, paramString2);
    }

    public ResourceAdapter getResourceAdapter() {
        return delegate.getResourceAdapter();
    }

    public void setDatabaseName(String paramString) {
        delegate.setDatabaseName(paramString);
    }

    public String getDatabaseName() {
        return delegate.getDatabaseName();
    }

    public void setDataSourceName(String paramString) {
        delegate.setDataSourceName(paramString);
    }

    public Object getObjectInstance(Object paramObject, Name paramName, Context paramContext, Hashtable<?, ?> paramHashtable) throws Exception {
        return delegate.getObjectInstance(paramObject, paramName, paramContext, paramHashtable);
    }

    public String getDataSourceName() {
        return delegate.getDataSourceName();
    }

    public void setDescription(String paramString) {
        delegate.setDescription(paramString);
    }

    public String getDescription() {
        return delegate.getDescription();
    }

    public void setUser(String paramString) {
        delegate.setUser(paramString);
    }

    public String getUser() {
        return delegate.getUser();
    }

    public void setPassword(String paramString) {
        delegate.setPassword(paramString);
    }

    public String getPassword() {
        return delegate.getPassword();
    }

    public int getLoginTimeout() throws SQLException {
        return delegate.getLoginTimeout();
    }

    public void setLoginTimeout(int paramInt) throws SQLException {
        delegate.setLoginTimeout(paramInt);
    }

    public PrintWriter getLogWriter() throws SQLException {
        return delegate.getLogWriter();
    }

    public void setLogWriter(PrintWriter paramPrintWriter) throws SQLException {
        delegate.setLogWriter(paramPrintWriter);
    }

    public void setCreateDatabase(String paramString) {
        delegate.setCreateDatabase(paramString);
    }

    public String getCreateDatabase() {
        return delegate.getCreateDatabase();
    }

    public void setConnectionAttributes(String paramString) {
        delegate.setConnectionAttributes(paramString);
    }

    public String getConnectionAttributes() {
        return delegate.getConnectionAttributes();
    }

    public void setShutdownDatabase(String paramString) {
        delegate.setShutdownDatabase(paramString);
    }

    public String getShutdownDatabase() {
        return delegate.getShutdownDatabase();
    }

    public void setAttributesAsPassword(boolean paramBoolean) {
        delegate.setAttributesAsPassword(paramBoolean);
    }

    public boolean getAttributesAsPassword() {
        return delegate.getAttributesAsPassword();
    }

    public boolean equals(Object paramObject) {
        return delegate.equals(paramObject);
    }

    public int hashCode() {
        return delegate.hashCode();
    }

    public Connection getConnection() throws SQLException {
        return delegate.getConnection();
    }

    public Connection getConnection(String paramString1, String paramString2) throws SQLException {
        return delegate.getConnection(paramString1, paramString2);
    }

    public boolean isWrapperFor(Class<?> paramClass) throws SQLException {
        return delegate.isWrapperFor(paramClass);
    }

    public <T> T unwrap(Class<T> paramClass) throws SQLException {
        return delegate.unwrap(paramClass);
    }

    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return delegate.getParentLogger();
    }
    
    @Override
    public String toString() {
        return "[" + getDataSourceLabel() + "]";
    }

//    public String toString() {
//        return delegate.toString();
//    }
}
