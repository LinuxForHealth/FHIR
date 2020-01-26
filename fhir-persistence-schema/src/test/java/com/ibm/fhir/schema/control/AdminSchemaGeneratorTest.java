/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.control;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import org.testng.annotations.Test;

import com.ibm.fhir.database.utils.model.PhysicalDataModel;

/**
 * 
 */
public class AdminSchemaGeneratorTest {
    @Test
    public void testAdminSchemaGenerator() {
        AdminSchemaGenerator adminSchemaGenerator = new AdminSchemaGenerator("FHIR_ADMIN_SCHEMA");
        assertNull(adminSchemaGenerator.getFhirTablespace());
        assertNull(adminSchemaGenerator.getSessionVariable());
    }

    @Test
    public void testAdminSchemaGeneratorPhysicalDataModelIndividualMethods() {
        AdminSchemaGenerator adminSchemaGenerator = new AdminSchemaGenerator("FHIR_ADMIN_SCHEMA");
        PhysicalDataModel pdm = new PhysicalDataModel();
        adminSchemaGenerator.addTenantSequence(pdm);
        adminSchemaGenerator.addTenantTable(pdm);
        adminSchemaGenerator.addTenantKeysTable(pdm);

        adminSchemaGenerator.addVariable(pdm);
        assertNotNull(adminSchemaGenerator.getSessionVariable());
    }

    @Test
    public void testAdminSchemaGeneratorPhysicalDataModel() {
        AdminSchemaGenerator adminSchemaGenerator = new AdminSchemaGenerator("FHIR_ADMIN_SCHEMA");
        PhysicalDataModel pdm = new PhysicalDataModel();
        adminSchemaGenerator.buildSchema(pdm);
        assertNotNull(adminSchemaGenerator.getSessionVariable());
    }
}