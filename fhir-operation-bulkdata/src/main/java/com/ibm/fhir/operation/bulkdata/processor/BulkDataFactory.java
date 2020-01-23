/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.operation.bulkdata.processor;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.operation.bulkdata.config.BulkDataConfigUtil;
import com.ibm.fhir.operation.bulkdata.config.cache.BulkDataTenantSpecificCache;
import com.ibm.fhir.operation.bulkdata.processor.impl.CosExportImpl;
import com.ibm.fhir.operation.bulkdata.processor.impl.DummyImportExportImpl;

/**
 * The BulkDataFactory enables the tenant specific lookup of the 'dummy' or 'cos' implementations.
 * By default the 'dummy' is available, unless otherwise specified in the bulkdata.json.
 * <br>
 * Dummy is only really handy in debugging or testing the operation feature.
 * <br>
 * 
 * <pre>
 * "implementation_type" : "cos"
 * </pre>
 */
public class BulkDataFactory {
    private static final String CLASSNAME = BulkDataFactory.class.getName();
    private static final Logger log = Logger.getLogger(CLASSNAME);

    private BulkDataFactory() {
        // No Op
    }

    public static ExportImportBulkData getTenantInstance(BulkDataTenantSpecificCache cache) {
        try {
            String tenantId = FHIRRequestContext.get().getTenantId();
            Map<String, String> properties = cache.getCachedObjectForTenant(tenantId);
            String impl = properties.get(BulkDataConfigUtil.IMPLEMENTATION_TYPE);

            if ("cos".compareTo(impl) == 0) {
                return new CosExportImpl(cache);
            }
        } catch (Exception e) {
            log.log(Level.WARNING, "found no tenant specific bulkdata.json", e);
        }
        return new DummyImportExportImpl(cache);
    }
}