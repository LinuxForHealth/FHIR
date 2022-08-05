/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.connection;

import java.sql.Connection;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.database.utils.citus.CitusTranslator;
import org.linuxforhealth.fhir.database.utils.citus.ConfigureConnectionDAO;
import org.linuxforhealth.fhir.database.utils.model.DbType;
import org.linuxforhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;

/**
 * For Citus connections, SET LOCAL citus.multi_shard_modify_mode TO 'sequential'
 */
public class SetMultiShardModifyModeAction extends ChainedAction {
    private static final Logger log = Logger.getLogger(CreateTempTablesAction.class.getName());
    // By eliminating FK constraints from DISTRIBUTED tables to REFERENCE tables, it appears
    // we will no longer need to serialize shard modifications
    private static final boolean USE_SEQUENTIAL = false;

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

        if (flavor.getType() == DbType.CITUS && USE_SEQUENTIAL) {
            // This is only used for Citus databases
            ConfigureConnectionDAO dao = new ConfigureConnectionDAO();
            dao.run(new CitusTranslator(), connection);
        }

        // perform next action in the chain
        super.performOn(flavor, connection);
    }
}