/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.connection;

import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.util.FHIRUtil;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDBCleanupException;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;

/**
 * Helper functions used for managing FHIR database interactions
 */
public class FHIRDbHelper {
    private static final Logger log = Logger.getLogger(FHIRDbHelper.class.getName());
    
    /**
     * Convenience function to log the cause of an exception about to be thrown. This
     * is useful when avoiding chaining the cause with the persistence exception, which
     * could inadvertently leak sensitive information (details of the schema, for example)
     *
     * @param logger
     * @param fx
     * @param cause
     * @return
     */
    public static <XT extends FHIRPersistenceException> XT severe(Logger logger, XT fx, Throwable cause) {
        logger.log(Level.SEVERE, fx.getMessage(), cause);
        return fx;
    }
    
    /**
     * Log the exception message here along with the cause stack. Return the
     * exception fx to the caller so that it can be thrown easily.
     *
     * @param logger
     * @param fx
     * @param errorMessage
     * @param cause
     * @return
     */
    public static <XT extends FHIRPersistenceException> XT severe(Logger logger, XT fx, String errorMessage,
            Throwable cause) {
        if (cause != null) {
            logger.log(Level.SEVERE, fx.addProbeId(errorMessage), cause);
        } else {
            logger.log(Level.SEVERE, fx.addProbeId(errorMessage));
        }
        return fx;
    }
    
    public static FHIRPersistenceDataAccessException buildExceptionWithIssue(String msg, IssueType issueType)
            throws FHIRPersistenceDataAccessException {
        Issue ooi = FHIRUtil.buildOperationOutcomeIssue(msg, issueType);
        return new FHIRPersistenceDataAccessException(msg).withIssue(ooi);
    }

    public static FHIRPersistenceDBConnectException buildFHIRPersistenceDBConnectException(String msg, IssueType issueType)
            throws FHIRPersistenceDBConnectException {
        Issue ooi = FHIRUtil.buildOperationOutcomeIssue(msg, issueType);
        return new FHIRPersistenceDBConnectException(msg).withIssue(ooi);
    }

    /**
     * Close the connection if not null.
     * @implNote This connection object is just a wrapper. If we're in a 
     *           transaction, then closing it doesn't do much, other than tell
     *           the transaction manager that the connection is no longer in
     *           use. The transaction manager still holds the underlying 
     *           database connection open, and will use that connection the 
     *           next time getConnection() is called for the same datasource 
     *           within this thread. Only when the transaction commits 
     *           will the connection be returned to the pool (or closed). 
     *           If connections remain open when commit() is called, the 
     *           transaction will fail.
     * @param connection
     */
    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (Throwable e) {
                // log the failure, but suppress the exception
                FHIRPersistenceDBCleanupException ce = new FHIRPersistenceDBCleanupException("Failure closing Connection.", e);
                log.log(Level.SEVERE, ce.getMessage(), ce);
            }
        }
    }
}
