/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.connection;

import java.sql.Connection;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.database.utils.api.IDatabaseStatement;
import org.linuxforhealth.fhir.database.utils.common.JdbcTarget;
import org.linuxforhealth.fhir.database.utils.derby.DerbyAdapter;
import org.linuxforhealth.fhir.database.utils.model.DbType;
import org.linuxforhealth.fhir.persistence.jdbc.derby.CreateCanonicalValuesTmp;
import org.linuxforhealth.fhir.persistence.jdbc.derby.CreateCodeSystemsTmp;
import org.linuxforhealth.fhir.persistence.jdbc.derby.CreateCommonTokenValuesTmp;
import org.linuxforhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;

/**
 * Creates the declared global temp table used in Derby for handling
 * upserts into common_token_values and code_systems, avoiding
 * huge VALUES() statements which cause the Derby SQL parser to
 * generate a stack overflow.
 */
public class CreateTempTablesAction extends ChainedAction {
    private static final Logger log = Logger.getLogger(CreateTempTablesAction.class.getName());

    /**
     * Public constructor. No next action, so this will be the last action applied
     */
    public CreateTempTablesAction() {
        super();
    }

    /**
     * Public constructor
     * @param next the next action in the chain
     */
    public CreateTempTablesAction(Action next) {
        super(next);
    }

    @Override
    public void performOn(FHIRDbFlavor flavor, Connection connection) throws FHIRPersistenceDBConnectException {

        if (flavor.getType() == DbType.DERBY) {
            // This is only used for Derby databases
            log.fine("Adding declared global temp tables to this session");
            JdbcTarget target = new JdbcTarget(connection);
            DerbyAdapter adapter = new DerbyAdapter(target);

            createCodeSystemsTmp(adapter);
            createCommonTokenValuesTmp(adapter);
            createCanonicalValuesTmp(adapter);
        }

        // perform next action in the chain
        super.performOn(flavor, connection);
    }

    /**
     * Create the declared global temporary table COMMON_TOKEN_VALUES_TMP
     * @param adapter
     * @throws FHIRPersistenceDBConnectException
     */
    public void createCommonTokenValuesTmp(DerbyAdapter adapter) throws FHIRPersistenceDBConnectException {
        IDatabaseStatement cmd = new CreateCommonTokenValuesTmp();
        adapter.runStatement(cmd);
    }

    /**
     * Create the declared global temporary table CODE_SYSTEMS_TMP
     * @param adapter
     * @throws FHIRPersistenceDBConnectException
     */
    public void createCodeSystemsTmp(DerbyAdapter adapter) throws FHIRPersistenceDBConnectException {
        IDatabaseStatement cmd = new CreateCodeSystemsTmp();
        adapter.runStatement(cmd);
    }

    /**
     * Create the declared global temporary table COMMON_TOKEN_VALUES_TMP
     * @param adapter
     * @throws FHIRPersistenceDBConnectException
     */
    public void createCanonicalValuesTmp(DerbyAdapter adapter) throws FHIRPersistenceDBConnectException {
        IDatabaseStatement cmd = new CreateCanonicalValuesTmp();
        adapter.runStatement(cmd);
    }
}