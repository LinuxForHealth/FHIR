/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app.processor.action;

import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.api.IDatabaseTarget;
import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.database.utils.model.PhysicalDataModel;
import com.ibm.fhir.schema.app.processor.action.bean.ActionBean;
import com.ibm.fhir.schema.app.processor.action.exceptions.SchemaActionException;
import com.ibm.fhir.schema.control.FhirSchemaConstants;
import com.ibm.fhir.schema.control.FhirSchemaGenerator;

public class GrantPrivilegesAction implements ISchemaAction {
    public GrantPrivilegesAction() {
        // No Operation 
    }

    @Override
    public void run(ActionBean actionBean, IDatabaseTarget target, IDatabaseAdapter adapter,
            ITransactionProvider transactionProvider) throws SchemaActionException {
        // Build/update the tables as well as the stored procedures
        FhirSchemaGenerator gen = new FhirSchemaGenerator(actionBean.getAdminSchemaName(), actionBean.getSchemaName());
        PhysicalDataModel pdm = new PhysicalDataModel();
        gen.buildSchema(pdm);
        pdm.applyGrants(adapter, FhirSchemaConstants.FHIR_USER_GRANT_GROUP, actionBean.getGrantTo());
    }
}