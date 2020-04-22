/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata;

import static org.testng.Assert.assertNotNull;

import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.OperationDefinition;
import com.ibm.fhir.operation.bulkdata.processor.BulkDataFactory;
import com.ibm.fhir.operation.bulkdata.processor.ExportImportBulkData;

public class BulkdataOperationTest {
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
        ExportImportBulkData eibd = BulkDataFactory.getTenantInstance();
        assertNotNull(eibd);
    }
}