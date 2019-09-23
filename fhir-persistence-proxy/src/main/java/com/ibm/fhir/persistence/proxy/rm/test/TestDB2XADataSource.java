/*
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.proxy.rm.test;

import java.sql.SQLException;
import java.util.logging.Logger;

import javax.sql.XAConnection;

import com.ibm.db2.jcc.DB2XADataSource;

/**
 * This class is used in the testing of the XA resource recovery function within the proxy datasource class. It serves
 * as a wrapper class for the DB2XADataSource class and provides additional function to artificially trigger failures
 * during the two-phase commit process.
 * 
 * @author padams
 *
 */
public class TestDB2XADataSource extends DB2XADataSource {
    private static final long serialVersionUID = -8393190316903569196L;
    private static final Logger log = Logger.getLogger(TestDB2XADataSource.class.getName());

    private String failStep = "none";
    private int failCount = 1;

    public TestDB2XADataSource() {
        super();
        log.fine(this.getClass().getSimpleName() + " ctor invoked.");
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
     * We'll override this method so that we can insert our own class as a wrapper
     * around the XAConnection served up by the super class.
     */
    @Override
    public XAConnection getXAConnection() throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("getXAConnection"));
        try {
            XAConnection conn = super.getXAConnection();
            XAConnection result = new TestXAConnection(conn, getDataSourceLabel(), failStep, failCount);
            return result;
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("getXAConnection"));
        }
    }
    
    /**
     * Returns a label suitable for trace logging to identify a particular instance
     * of this class.
     * @return
     */
    private String getDataSourceLabel() {
        return this.getClass().getSimpleName() + ":" + getDatabaseName();
    }

    private String methodLabel(String method) {
        return method + "[" + getDataSourceLabel() + ":" + failStep + "]";
    }
    
    @Override
    public String toString() {
        return "[" + getDataSourceLabel() + "]";
    }
}
