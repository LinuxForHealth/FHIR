/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.watson.health.fhir.operation.bullkdata.processor;

import com.ibm.watson.health.fhir.operation.bullkdata.processor.impl.DummyImportExportImpl;

/**
 * @author pbastide 
 *
 */
public class BulkDataFactory {
    
    public static ExportBulkData getExport() {
        return new DummyImportExportImpl();
    }
    
    public static ImportBulkData getImport() {
        return new DummyImportExportImpl();
    }
}
