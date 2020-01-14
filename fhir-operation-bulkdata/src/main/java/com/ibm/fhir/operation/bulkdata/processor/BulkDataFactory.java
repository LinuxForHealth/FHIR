/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.operation.bulkdata.processor;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.operation.bulkdata.config.BulkDataConfigUtil;
import com.ibm.fhir.operation.bulkdata.config.cache.BulkDataTenantSpecificCache;
import com.ibm.fhir.operation.bulkdata.processor.impl.CosExportImpl;
import com.ibm.fhir.operation.bulkdata.processor.impl.DummyImportExportImpl;

/**
 * The BulkDataFactory enables the tenant specific lookup of the 'dummy' or 'cos' implementations.
 * By default the 'dummy' is available, unless otherwise specified in the bulkdata.json.
 *
 * <pre>"implementation_type" : "cos"  </pre>
 *
 *
 */
public class BulkDataFactory {
    private static final String CLASSNAME = BulkDataFactory.class.getName();
    private static final Logger log = Logger.getLogger(CLASSNAME);


    private static final Map<String, ExportBulkData> EXPORT_IMPLS =
            new HashMap<String, ExportBulkData>() {
                private static final long serialVersionUID = 1L;

                {
                    put("dummy", new DummyImportExportImpl());
                    put("cos", new CosExportImpl());
                }
            };

    private static final Map<String, ImportBulkData> IMPORT_IMPLS =
            new HashMap<String, ImportBulkData>() {
                private static final long serialVersionUID = 1L;

                {
                    put("dummy", new DummyImportExportImpl());
                }
            };

    public static ExportBulkData getExport(BulkDataTenantSpecificCache cache) {
        // Eventually, this needs to be added...
        // <code>String tenantId = FHIRRequestContext.get().getTenantId();</code>
        try {

            // The file is loaded ONLY from config/default
            Map<String, String> properties = cache.getCachedObjectForTenant(FHIRConfiguration.DEFAULT_TENANT_ID);
            String impl = properties.get(BulkDataConfigUtil.IMPLEMENTATION_TYPE);

            if("cos".compareTo(impl)==0) {
                return EXPORT_IMPLS.get(impl);
            }

        } catch (Exception e) {
            log.log(Level.WARNING, "found no tenant specific bulkdata.json", e);
        }

        return new DummyImportExportImpl();
    }

    /**
     * gets the tenant specified implementation of the ImportBulkData.
     * If none are found, the code returns the 'dummy' implementation.
     */
    public static ImportBulkData getImport(BulkDataTenantSpecificCache cache) {
        return IMPORT_IMPLS.get("dummy");
    }

}
