/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import com.ibm.fhir.persistence.jdbc.TransactionData;
import com.ibm.fhir.persistence.jdbc.dao.impl.ResourceProfileRec;
import com.ibm.fhir.persistence.jdbc.dao.impl.ResourceTokenValueRec;

/**
 * Holds all the parameter data for a given datasource. This data will be persisted at the end
 * of the current transaction
 */
public class ParameterTransactionDataImpl implements TransactionData {
    private static final Logger logger = Logger.getLogger(ParameterTransactionDataImpl.class.getName());

    // The id/name of the datasource to which this data belongs
    private final String datasourceId;

    // remember the impl which was used to create us. This impl should simplify
    // access to the datasource we're associated with
    private final FHIRPersistenceJDBCImpl impl;

    // The transaction object, needed to mark rollback if persist fails
    private final UserTransaction userTransaction;

    // Collect all the token values so we can submit once per transaction
    private final List<ResourceTokenValueRec> tokenValueRecs = new ArrayList<>();

    // Collect all the profile values so we can submit once per transaction
    private final List<ResourceProfileRec> profileRecs = new ArrayList<>();

    // Collect all the tag values so we can submit once per transaction
    private final List<ResourceTokenValueRec> tagRecs = new ArrayList<>();

    // Collect all the security values so we can submit once per transaction
    private final List<ResourceTokenValueRec> securityRecs = new ArrayList<>();

    /**
     * Public constructor
     * @param datasourceId
     * @param impl
     * @param userTransaction
     */
    public ParameterTransactionDataImpl(String datasourceId, FHIRPersistenceJDBCImpl impl, UserTransaction userTransaction) {
        this.datasourceId = datasourceId;
        this.impl = impl;
        this.userTransaction = userTransaction;
    }

    @Override
    public void persist() {

        try {
            impl.onCommit(tokenValueRecs, profileRecs, tagRecs, securityRecs);
        } catch (Throwable t) {
            logger.log(Level.SEVERE, "Failed persisting parameter transaction data. Marking transaction for rollback", t);
            try {
                this.userTransaction.setRollbackOnly();
            } catch (SystemException x) {
                logger.log(Level.SEVERE, "Failed marking transaction for rollback", t);
            }
        }
    }

    /**
     * Add this record to the list of records being accumulated in this transaction
     * @param rec
     */
    public void addValue(ResourceTokenValueRec rec) {
        tokenValueRecs.add(rec);
    }

    /**
     * Add the given profile parameter record to the list of records being accumulated in
     * this transaction data. The records will be inserted to the database together at the
     * end, just prior to the commit (see {@link #persist()}
     * @param rec
     */
    public void addValue(ResourceProfileRec rec) {
        profileRecs.add(rec);
    }

    /**
     * Add the given tag parameter record to the list of records being accumulated in
     * this transaction data. The records will be inserted to the database together at the
     * end, just prior to the commit (see {@link #persist()}
     * @param rec
     */
    public void addTagValue(ResourceTokenValueRec rec) {
        tagRecs.add(rec);
    }

    /**
     * Add the given security parameter record to the list of records being accumulated in
     * the transaction data. The records will be inserted to the database together at the end,
     * just prior to the commit (see {@link #persist()}
     * @param rec
     */
    public void addSecurityValue(ResourceTokenValueRec rec) {
        securityRecs.add(rec);
    }
}