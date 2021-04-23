/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.config.preflight.impl;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.operation.bulkdata.OperationConstants;
import com.ibm.fhir.operation.bulkdata.config.preflight.Preflight;
import com.ibm.fhir.operation.bulkdata.model.type.Input;
import com.ibm.fhir.operation.bulkdata.model.type.StorageDetail;
import com.ibm.fhir.operation.bulkdata.util.CommonUtil;

/**
 * For the legacy configurations, we don't want to run the preflight checks.
 * Or the storage type is invalid.
 */
public class NopPreflight implements Preflight {

    private static final String CLASSNAME = NopPreflight.class.getName();
    private static final Logger logger = Logger.getLogger(CLASSNAME);

    private String source = null;
    private String outcome = null;
    private List<Input> inputs = null;
    private OperationConstants.ExportType exportType = null;
    private String format = null;

    protected CommonUtil util = new CommonUtil();

    public NopPreflight(String source, String outcome, List<Input> inputs, OperationConstants.ExportType exportType, String format) {
        this.source = source;
        this.outcome = outcome;
        this.inputs = inputs;
        this.exportType = exportType;
        this.format = format;
    }

    @Override
    public void preflight() throws FHIROperationException {
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Preflight: source -> '" + source + "' outcome -> '" + outcome + "'");
        }
    }

    /**
     * The storage provider that provides the source.
     * @return
     */
    protected String getSource() {
        return source;
    }

    /**
     * The storage provider that provides the destination of the operational outcomes.
     * @return
     */
    protected String getOutcome() {
        return outcome;
    }

    protected List<Input> getInputs() {
        return inputs;
    }

    protected OperationConstants.ExportType getExportType(){
        return exportType;
    }

    protected String getFormat(){
        return format;
    }

    @Override
    public void checkStorageAllowed(StorageDetail storageDetail) throws FHIROperationException {
        // No Operation
    }
}