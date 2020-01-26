/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app.processor.action;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.api.IDatabaseTarget;
import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.database.utils.common.JdbcTarget;
import com.ibm.fhir.database.utils.model.PhysicalDataModel;
import com.ibm.fhir.schema.app.processor.action.bean.ActionBean;
import com.ibm.fhir.schema.app.processor.action.exceptions.SchemaActionException;
import com.ibm.fhir.schema.control.FhirSchemaGenerator;

/**
 * Drops the Objects in the Schema and does not remove the Schema itself.
 * <br>
 * Configuration to drop tenant schema:
 * 
 * <pre>
 * --prop-file PATH/to/db2.properties
 * --schema-name FHIRDATA
 * --pool-size 2
 * --drop-schema
 * --confirm-drop
 * </pre>
 * 
 * Configuration to drop admin schema:
 * 
 * <pre>
 * --prop-file PATH/to/db2.properties
 * --schema-name FHIRDATA
 * --pool-size 2
 * --confirm-drop
 * --drop-admin
 * </pre>
 * 
 * <br>
 * To drop the schemas execute the following:
 * 
 * <pre>
 * $ db2 "drop schema YOUR_SCHEMA_NAME RESTRICT"
 *   DB20000I  The SQL command completed successfully.
 * $ db2 "drop schema YOUR_ADMIN_SCHEMA_NAME RESTRICT"
 *   DB20000I  The SQL command completed successfully.
 * </pre>
 */
public class DropSchemaAction implements ISchemaAction {
    private static final Logger logger = Logger.getLogger(DropSchemaAction.class.getName());

    public DropSchemaAction() {
        // No Operation
    }

    @Override
    public void run(ActionBean actionBean, IDatabaseTarget target, IDatabaseAdapter adapter,
            ITransactionProvider transactionProvider) throws SchemaActionException {
        // Build/update the tables as well as the stored procedures
        FhirSchemaGenerator gen = new FhirSchemaGenerator(actionBean.getAdminSchemaName(), actionBean.getSchemaName());
        PhysicalDataModel pdm = new PhysicalDataModel();
        gen.buildSchema(pdm);

        if (actionBean.isDropSchema()) {
            // Just drop the objects associated with the FHIRDATA schema group
            if (checkSchema(actionBean, target, FhirSchemaGenerator.FHIRDATA_GROUP)) {
                pdm.drop(adapter, FhirSchemaGenerator.SCHEMA_GROUP_TAG, FhirSchemaGenerator.FHIRDATA_GROUP);
            } else {
                logger.warning("The schema does not exist - [" + FhirSchemaGenerator.FHIRDATA_GROUP + "]");
                logger.warning("No action taken to drop the schema [" + FhirSchemaGenerator.FHIRDATA_GROUP + "]");
            }
        }

        if (actionBean.isDropAdmin()) {
            // Just drop the objects associated with the ADMIN schema group
            if (checkSchema(actionBean, target, FhirSchemaGenerator.FHIRDATA_GROUP)) {
                pdm.drop(adapter, FhirSchemaGenerator.SCHEMA_GROUP_TAG, FhirSchemaGenerator.ADMIN_GROUP);
            } else {
                logger.warning("The schema does not exist - [" + FhirSchemaGenerator.ADMIN_GROUP + "]");
                logger.warning("No action taken to drop the schema [" + FhirSchemaGenerator.ADMIN_GROUP + "]");
            }
        }
    }

    /**
     * checks that the schema exists.
     */
    public static boolean checkSchema(ActionBean actionBean, IDatabaseTarget target, String schemaName) {
        boolean result = false;
        if (target instanceof JdbcTarget) {
            JdbcTarget jdbcTarget = (JdbcTarget) target;
            final String statement = "select schemaname from syscat.schemata WHERE schemaname = ?";
            try (PreparedStatement ps = jdbcTarget.getConnection().prepareStatement(statement)) {
                int i = 1;
                ps.setString(i++, schemaName);
                boolean ex = ps.execute();
                if (ex) {
                    try (ResultSet resultSet = ps.getResultSet();) {
                        result = resultSet.next();
                    }
                }
            } catch (SQLException x) {
                throw actionBean.getTranslator().translate(x);
            }
        }
        return result;
    }
}