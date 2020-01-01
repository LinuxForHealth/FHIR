/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app.processor.action;

import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.api.IDatabaseTarget;
import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.schema.app.processor.action.bean.ActionBean;

/**
 * Definition for stateless Actions
 */
public interface ISchemaAction {

    /**
     * runs the schema action
     * <br>
     * @param actionBean
     * @param target
     * @param adapter
     * @param transactionProvider
     */
    public void run(ActionBean actionBean, IDatabaseTarget target, IDatabaseAdapter adapter, ITransactionProvider transactionProvider);

    /**
     * dry runs the actions 
     * <br>
     * @param actionBean
     * @param target
     * @param adapter
     * @param transactionProvider
     */
    public void dryRun(ActionBean actionBean, IDatabaseTarget target, IDatabaseAdapter adapter, ITransactionProvider transactionProvider);
}