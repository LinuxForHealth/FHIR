/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app.processor.action;

import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.api.IDatabaseTarget;
import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.database.utils.db2.Db2Adapter;
import com.ibm.fhir.schema.app.processor.action.bean.ActionBean;

public class CheckCompatibilityAction implements ISchemaAction {

    public CheckCompatibilityAction() {
        // No Operation
    }

    @Override
    public void run(ActionBean actionBean, IDatabaseTarget target, IDatabaseAdapter adapter,
            ITransactionProvider transactionProvider) {
        boolean compatible = false;
        if (adapter instanceof Db2Adapter) {
            compatible = ((Db2Adapter) adapter).checkCompatibility(actionBean.getAdminSchemaName());
        }
        actionBean.setCompatible(compatible);
    }

    @Override
    public void dryRun(ActionBean actionBean, IDatabaseTarget target, IDatabaseAdapter adapter,
            ITransactionProvider transactionProvider) {
        run(actionBean, target, adapter, transactionProvider);
    }
}