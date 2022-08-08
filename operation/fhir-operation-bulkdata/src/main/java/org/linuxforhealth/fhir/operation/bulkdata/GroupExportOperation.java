/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.operation.bulkdata;

import org.linuxforhealth.fhir.model.resource.OperationDefinition;
import org.linuxforhealth.fhir.registry.FHIRRegistry;

/**
 * Create a Group Export of FHIR Data to NDJSON format
 * @see https://hl7.org/Fhir/uv/bulkdata/OperationDefinition-group-export.json.html
 */
public class GroupExportOperation extends ExportOperation {
    public GroupExportOperation() {
        super();
    }

    @Override
    protected OperationDefinition buildOperationDefinition() {
        return FHIRRegistry.getInstance().getResource("http://hl7.org/fhir/uv/bulkdata/OperationDefinition/group-export", OperationDefinition.class);
    }
}