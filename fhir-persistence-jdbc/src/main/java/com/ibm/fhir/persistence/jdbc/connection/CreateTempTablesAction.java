/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.connection;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.config.FHIRConfigProvider;
import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.config.PropertyGroup;
import com.ibm.fhir.database.utils.api.BadTenantFrozenException;
import com.ibm.fhir.database.utils.api.BadTenantKeyException;
import com.ibm.fhir.database.utils.api.BadTenantNameException;
import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.JdbcTarget;
import com.ibm.fhir.database.utils.db2.Db2Adapter;
import com.ibm.fhir.database.utils.db2.Db2SetTenantVariable;
import com.ibm.fhir.database.utils.derby.DerbyAdapter;
import com.ibm.fhir.database.utils.derby.DerbyTranslator;
import com.ibm.fhir.database.utils.model.DbType;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.derby.CreateCodeSystemsTmp;
import com.ibm.fhir.persistence.jdbc.derby.CreateCommonTokenValuesTmp;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;

/**
 * Set the tenant variable in the session (part of Db2 multi-tenancy
 * support). This needs to be executed at the beginning of an
 * interaction with a database connection, before any other
 * DML/SQL.
 */
public class CreateTempTablesAction extends ChainedAction {
    private static final Logger log = Logger.getLogger(SetTenantAction.class.getName());
        
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
            JdbcTarget target = new JdbcTarget(connection);
            DerbyAdapter adapter = new DerbyAdapter(target);
    
            createCodeSystemsTmp(adapter);
            createCommonTokenValuesTmp(adapter);
        }
        
        // perform next action in the chain
        super.performOn(flavor, connection);
    }

    /**
     * Create the declared global temporary table COMMON_TOKEN_VALUES_TMP
     * @param connection
     * @throws FHIRPersistenceDBConnectException
     */
    public void createCommonTokenValuesTmp(DerbyAdapter adapter) throws FHIRPersistenceDBConnectException {
        IDatabaseStatement cmd = new CreateCommonTokenValuesTmp();
        adapter.runStatement(cmd);
    }

    /**
     * Create the declared global temporary table CODE_SYSTEMS_TMP
     * @param connection
     * @throws FHIRPersistenceDBConnectException
     */
    public void createCodeSystemsTmp(DerbyAdapter adapter) throws FHIRPersistenceDBConnectException {
        IDatabaseStatement cmd = new CreateCodeSystemsTmp();
        adapter.runStatement(cmd);
    }
}