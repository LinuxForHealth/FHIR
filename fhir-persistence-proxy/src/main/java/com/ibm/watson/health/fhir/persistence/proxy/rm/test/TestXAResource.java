/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.persistence.proxy.rm.test;

import java.io.File;
import java.util.logging.Logger;

import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

import com.ibm.watson.health.fhir.persistence.proxy.rm.RMXAConnectionResource;

/**
 * @author padams
 *
 */
public class TestXAResource implements XAResource {
    private static final Logger log = Logger.getLogger(TestXAResource.class.getName());

    private XAResource delegate;
    private String failStep;
    private int failCount;
    private String dsLabel;

    @SuppressWarnings("unused")
    private TestXAResource() {
    }

    public TestXAResource(XAResource resource, String dsLabel, String failStep, int failCount) {
        log.entering(this.getClass().getName(), "TestXAResource ctor", new Object[] {
                "dsLabel", dsLabel, "failStep", failStep, "failCount", failCount
        });
        this.delegate = resource;
        this.failStep = failStep;
        this.failCount = failCount;
        this.dsLabel = dsLabel;
        log.exiting(this.getClass().getName(), "TestXAResource ctor");
    }

    public void commit(Xid xid, boolean onePhase) throws XAException {
        log.entering(this.getClass().getName(), methodLabel("commit"), new Object[] {
                "onePhase", onePhase
        });
        try {
            checkFailStep("commit");
            delegate.commit(xid, onePhase);
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("commit"));
        }
    }

    public void end(Xid xid, int flags) throws XAException {
        log.entering(this.getClass().getName(), methodLabel("end"), new Object[] {
                "flags", flags
        });
        try {
            checkFailStep("end");
            delegate.end(xid, flags);
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("end"));
        }
    }

    public void forget(Xid xid) throws XAException {
        log.entering(this.getClass().getName(), methodLabel("forget"));
        try {
            checkFailStep("forget");
            delegate.forget(xid);
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("forget"));
        }
    }

    public int getTransactionTimeout() throws XAException {
        log.entering(this.getClass().getName(), methodLabel("getTransactionTimeout"));
        try {
            return delegate.getTransactionTimeout();
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("getTransactionTimeout"));
        }
    }

    public boolean isSameRM(XAResource xares) throws XAException {
        log.entering(this.getClass().getName(), methodLabel("isSameRM"));
        try {
            return delegate.isSameRM(xares);
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("isSameRM"));
        }
    }

    public int prepare(Xid xid) throws XAException {
        log.entering(this.getClass().getName(), methodLabel("prepare"));
        try {
            checkFailStep("prepare");
            return delegate.prepare(xid);
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("prepare"));
        }
    }

    public Xid[] recover(int flag) throws XAException {
        log.entering(this.getClass().getName(), methodLabel("recover"));
        try {
            return delegate.recover(flag);
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("recover"));
        }
    }

    public void rollback(Xid xid) throws XAException {
        log.entering(this.getClass().getName(), methodLabel("rollback"));
        try {
            checkFailStep("rollback");
            delegate.rollback(xid);
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("rollback"));
        }
    }

    public boolean setTransactionTimeout(int seconds) throws XAException {
        log.entering(this.getClass().getName(), methodLabel("setTransactionTimeout"));
        try {
            return delegate.setTransactionTimeout(seconds);
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("setTransactionTimeout"));
        }
    }

    public void start(Xid xid, int flags) throws XAException {
        log.entering(this.getClass().getName(), methodLabel("start"), new Object[] {
                "flags", flags
        });
        try {
            checkFailStep("start");
            delegate.start(xid, flags);
        } finally {
            log.exiting(this.getClass().getName(), methodLabel("start"));
        }
    }

    private void checkFailStep(String step) throws XAException {
        if (failStep.equals(step)) {
            File f = new File("/tmp/skipFailures");
            if (f.exists()) {
                log.fine("Bypassing artificial failures due to presence of /tmp/skipFailures...");
                return;
            }
            if (RMXAConnectionResource.shouldBypassFailures()) {
                log.fine("Bypassing artifical failures while in recovery mode...");
                return;
            }
            if (failCount == 0) {
                log.fine("Bypassing artificial failure since failCount is zero");
                return;
            }
            failCount--;
            XAException e = new XAException("Simulating failure in '" + step + "'!");
            e.errorCode = XAException.XAER_RMFAIL;
            throw e;
        }
    }

    private String methodLabel(String method) {
        return method + "[" + dsLabel + ":" + failStep + ":" + failCount + "]";
    }
}
