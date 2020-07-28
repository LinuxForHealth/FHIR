/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.processor;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.operation.bulkdata.config.BulkDataConfigUtil;
import com.ibm.fhir.operation.bulkdata.processor.impl.CosExportImpl;
import com.ibm.fhir.operation.bulkdata.processor.impl.DummyImportExportImpl;

/**
 * The BulkDataFactory enables the tenant specific lookup of the 'dummy' or 'cos' implementations.
 * By default the 'dummy' is available, unless otherwise specified in the fhir-server-config
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

    public static ExportImportBulkData getTenantInstance() {
        try {
            Map<String, String> properties = BulkDataConfigUtil.getBatchJobConfig();
            String impl = properties.get(BulkDataConfigUtil.IMPLEMENTATION_TYPE);

            if ("cos".equals(impl)) {
                return new CosExportImpl(properties);
            }
        } catch (Exception e) {
            log.log(Level.WARNING, "problem reading bulkdata property from tenant config", e);
        }
        return new DummyImportExportImpl();
    }
}
