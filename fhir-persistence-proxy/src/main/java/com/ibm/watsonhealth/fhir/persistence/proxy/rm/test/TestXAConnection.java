/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.proxy.rm.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.sql.ConnectionEventListener;
import javax.sql.StatementEventListener;
import javax.sql.XAConnection;
import javax.transaction.xa.XAResource;

/**
 * This class acts as a wrapper class for an XAConnection instance, and is used to force failures during testing.
 * 
 * @author padams
 *
 */
public class TestXAConnection implements XAConnection {
    private static final Logger log = Logger.getLogger(TestXAConnection.class.getName());

    private XAConnection delegate;
    private String failStep;
    private String dsLabel;

    @SuppressWarnings("unused")
    private TestXAConnection() {
    }

    public TestXAConnection(XAConnection conn, String dsLabel, String failStep) {
        log.entering(this.getClass().getName(), "TestXAConnection ctor", new Object[] {
                "dsLabel", dsLabel
        });
        this.delegate = conn;
        this.failStep = failStep;
        this.dsLabel = dsLabel;
        log.exiting(this.getClass().getName(), "TestXAConnection ctor");
    }

    public XAResource getXAResource() throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("getXAResource"));
        try {
            XAResource resource = delegate.getXAResource();
            XAResource result = new TestXAResource(resource, dsLabel, failStep);
            return result;
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("getXAResource"));
        }
    }

    public Connection getConnection() throws SQLException {
        log.entering(this.getClass().getName(), methodLabel("getConnection"));
        try {
            Connection conn = delegate.getConnection();
            return new TestConnection(conn, dsLabel);
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("getConnection"));
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

    public void addConnectionEventListener(ConnectionEventListener listener) {
        log.entering(this.getClass().getName(), methodLabel("addConnectionEventListener"));
        try {
            delegate.addConnectionEventListener(listener);
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("addConnectionEventListener"));
        }
    }

    public void removeConnectionEventListener(ConnectionEventListener listener) {
        log.entering(this.getClass().getName(), methodLabel("removeConnectionEventListener"));
        try {
            delegate.removeConnectionEventListener(listener);
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("removeConnectionEventListener"));
        }
    }

    public void addStatementEventListener(StatementEventListener listener) {
        log.entering(this.getClass().getName(), methodLabel("addStatementEventListener"));
        try {
            delegate.addStatementEventListener(listener);
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("addStatementEventListener"));
        }
    }

    public void removeStatementEventListener(StatementEventListener listener) {
        log.entering(this.getClass().getName(), methodLabel("removeStatementEventListener"));
        try {
            delegate.removeStatementEventListener(listener);
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("removeStatementEventListener"));
        }
    }

    private String methodLabel(String method) {
        return method + "[" + dsLabel + ":" + failStep + "]";
    }
}
