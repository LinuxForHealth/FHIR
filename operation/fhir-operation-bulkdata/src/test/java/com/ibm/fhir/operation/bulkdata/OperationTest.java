/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata;

import static org.testng.Assert.assertNotNull;

import java.lang.reflect.Method;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.model.resource.OperationDefinition;
import com.ibm.fhir.operation.bulkdata.processor.BulkDataFactory;
import com.ibm.fhir.operation.bulkdata.processor.ExportImportBulkData;
import com.ibm.fhir.server.spi.operation.FHIROperationContext;

public class OperationTest {
    @BeforeClass
    public void setup() {
        FHIRConfiguration.setConfigHome("target/test-classes");
    }

    @BeforeMethod
    public void startMethod(Method method) throws FHIRException {

        // Configure the request context for our search tests
        FHIRRequestContext context = FHIRRequestContext.get();
        context.setTenantId("default");
    }

    @AfterMethod
    public void clearThreadLocal() {
        FHIRRequestContext.remove();
    }

    @Test
    public void testExportOperation() {
        ExportOperation exportOperation = new ExportOperation();
        OperationDefinition operationDefinition = exportOperation.buildOperationDefinition();
        assertNotNull(operationDefinition);
    }

    @Test
    public void testExportStatusOperation() {
        StatusOperation exportStatusOperation = new StatusOperation();
        OperationDefinition operationDefinition = exportStatusOperation.buildOperationDefinition();
        assertNotNull(operationDefinition);
    }

    @Test
    public void testGroupExportOperation() {
        GroupExportOperation groupExportOperation = new GroupExportOperation();
        OperationDefinition operationDefinition = groupExportOperation.buildOperationDefinition();
        assertNotNull(operationDefinition);
    }

    @Test
    public void testImportOperation() {
        ImportOperation importOperation = new ImportOperation();
        OperationDefinition operationDefinition = importOperation.buildOperationDefinition();
        assertNotNull(operationDefinition);
    }

    @Test
    public void testPatientExportOperation() {
        PatientExportOperation patientExportOperation = new PatientExportOperation();
        OperationDefinition operationDefinition = patientExportOperation.buildOperationDefinition();
        assertNotNull(operationDefinition);
    }

    @Test
    public void testBulkDataFactory() {
        FHIROperationContext operationContext = FHIROperationContext.createInstanceOperationContext("test");
        ExportImportBulkData eibd = BulkDataFactory.getInstance(operationContext);
        assertNotNull(eibd);
    }
}