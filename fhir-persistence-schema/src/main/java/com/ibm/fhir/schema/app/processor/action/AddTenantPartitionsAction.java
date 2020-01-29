/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app.processor.action;

import static com.ibm.fhir.schema.control.FhirSchemaConstants.FHIR_TS_EXTENT_KB;

import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.api.IDatabaseTarget;
import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.database.utils.model.PhysicalDataModel;
import com.ibm.fhir.schema.app.processor.action.bean.ActionBean;
import com.ibm.fhir.schema.app.processor.action.exceptions.SchemaActionException;
import com.ibm.fhir.schema.control.FhirSchemaGenerator;

public class AddTenantPartitionsAction implements ISchemaAction {
    public AddTenantPartitionsAction() {
        // No Operation
    }

    @Override
    public void run(ActionBean actionBean, IDatabaseTarget target, IDatabaseAdapter adapter,
            ITransactionProvider transactionProvider) throws SchemaActionException {
        // Build/update the tables as well as the stored procedures
        FhirSchemaGenerator gen = new FhirSchemaGenerator(actionBean.getAdminSchemaName(), actionBean.getSchemaName());
        PhysicalDataModel pdm = new PhysicalDataModel();
        gen.buildSchema(pdm);

        // Get the data model to create the table partitions. This is threaded, so transactions are
        // handled within each thread by the adapter. This means we should probably pull some of
        // that logic out of the adapter and handle it at a higher level. Note...the extent size used
        // for the partitions needs to match the extent size of the original table tablespace (FHIR_TS)
        // so this must be constant.
        pdm.addTenantPartitions(adapter, actionBean.getSchemaName(), actionBean.getTenantId(), FHIR_TS_EXTENT_KB);
    }
}