/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.operation.bulkdata;

import static org.testng.Assert.assertNotNull;

import java.lang.reflect.Method;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import org.linuxforhealth.fhir.config.FHIRConfiguration;
import org.linuxforhealth.fhir.config.FHIRRequestContext;
import org.linuxforhealth.fhir.exception.FHIRException;
import org.linuxforhealth.fhir.model.resource.OperationDefinition;
import org.linuxforhealth.fhir.operation.bulkdata.processor.BulkDataFactory;
import org.linuxforhealth.fhir.operation.bulkdata.processor.ExportImportBulkData;
import org.linuxforhealth.fhir.server.spi.operation.FHIROperationContext;

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