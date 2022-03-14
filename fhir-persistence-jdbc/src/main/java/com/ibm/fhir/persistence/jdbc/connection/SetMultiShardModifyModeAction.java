/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.connection;

import java.sql.Connection;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.citus.CitusTranslator;
import com.ibm.fhir.database.utils.citus.ConfigureConnectionDAO;
import com.ibm.fhir.database.utils.derby.DerbyAdapter;
import com.ibm.fhir.database.utils.model.DbType;
import com.ibm.fhir.persistence.jdbc.derby.CreateCanonicalValuesTmp;
import com.ibm.fhir.persistence.jdbc.derby.CreateCodeSystemsTmp;
import com.ibm.fhir.persistence.jdbc.derby.CreateCommonTokenValuesTmp;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;

/**
 * For Citus connections, SET LOCAL citus.multi_shard_modify_mode TO 'sequential'
 */
public class SetMultiShardModifyModeAction extends ChainedAction {
    private static final Logger log = Logger.getLogger(CreateTempTablesAction.class.getName());

    /**
     * Public constructor. No next action, so this will be the last action applied
     */
    public SetMultiShardModifyModeAction() {
        super();
    }

    /**
     * Public constructor
     * @param next the next action in the chain
     */
    public SetMultiShardModifyModeAction(Action next) {
        super(next);
    }

    @Override
    public void performOn(FHIRDbFlavor flavor, Connection connection) throws FHIRPersistenceDBConnectException {

        if (flavor.getType() == DbType.CITUS) {
            // This is only used for Citus databases
            log.fine("SET LOCAL citus.multi_shard_modify_mode TO 'sequential'");
            ConfigureConnectionDAO dao = new ConfigureConnectionDAO();
            dao.run(new CitusTranslator(), connection);
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