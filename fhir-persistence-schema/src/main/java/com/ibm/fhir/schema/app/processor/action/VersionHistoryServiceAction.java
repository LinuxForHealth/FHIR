/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app.processor.action;

import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.api.IDatabaseTarget;
import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.database.utils.version.VersionHistoryService;
import com.ibm.fhir.schema.app.processor.action.bean.ActionBean;

public class VersionHistoryServiceAction implements ISchemaAction {

    public VersionHistoryServiceAction() {
        // No Operation
    }

    @Override
    public void run(ActionBean actionBean, IDatabaseTarget target, IDatabaseAdapter adapter,
            ITransactionProvider transactionProvider) {
        VersionHistoryService vhs =
                new VersionHistoryService(actionBean.getAdminSchemaName(), actionBean.getSchemaName());
        vhs.setTransactionProvider(transactionProvider);
        vhs.setTarget(adapter);
        vhs.init();
        actionBean.setVersionHistoryService(vhs);
    }

    @Override
    public void dryRun(ActionBean actionBean, IDatabaseTarget target, IDatabaseAdapter adapter,
            ITransactionProvider transactionProvider) {
        run(actionBean, target, adapter, transactionProvider);
    }
}