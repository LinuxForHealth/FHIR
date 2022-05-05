/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.jdbc.dao.impl;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.index.ParameterValueVisitorAdapter;
import com.ibm.fhir.persistence.jdbc.dto.CompositeParmVal;
import com.ibm.fhir.persistence.jdbc.dto.DateParmVal;
import com.ibm.fhir.persistence.jdbc.dto.ExtractedParameterValue;
import com.ibm.fhir.persistence.jdbc.dto.ExtractedParameterValueVisitor;
import com.ibm.fhir.persistence.jdbc.dto.LocationParmVal;
import com.ibm.fhir.persistence.jdbc.dto.NumberParmVal;
import com.ibm.fhir.persistence.jdbc.dto.QuantityParmVal;
import com.ibm.fhir.persistence.jdbc.dto.ReferenceParmVal;
import com.ibm.fhir.persistence.jdbc.dto.StringParmVal;
import com.ibm.fhir.persistence.jdbc.dto.TokenParmVal;


/**
 * A visitor to map parameters to a format suitable for transport to another
 * system (e.g. for remote indexing)
 */
public class ParameterTransportVisitor implements ExtractedParameterValueVisitor {
    private final ParameterValueVisitorAdapter adapter;

    // tracks the number of composites so we know what next composite_id to use
    private int compositeIdCounter = 0;

    // Tracks the name of the composite parameter currently being processed
    private String currentCompositeParameterName = null;

    /**
     * Public constructor
     * @param adapter
     */
    public ParameterTransportVisitor(ParameterValueVisitorAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void visit(StringParmVal stringParameter) throws FHIRPersistenceException {
        Integer compositeId = this.currentCompositeParameterName != null ? this.compositeIdCounter : null;
        adapter.stringValue(stringParameter.getName(), stringParameter.getValueString(), compositeId);
    }

    @Override
    public void visit(NumberParmVal numberParameter) throws FHIRPersistenceException {
        Integer compositeId = this.currentCompositeParameterName != null ? this.compositeIdCounter : null;
        adapter.numberValue(numberParameter.getName(), 
            numberParameter.getValueNumber(), 
            numberParameter.getValueNumberLow(), 
            numberParameter.getValueNumberHigh(),
            compositeId);
    }

    @Override
    public void visit(DateParmVal dateParameter) throws FHIRPersistenceException {
        Integer compositeId = this.currentCompositeParameterName != null ? this.compositeIdCounter : null;
        adapter.dateValue(dateParameter.getName(), dateParameter.getValueDateStart(), dateParameter.getValueDateEnd(), compositeId);
    }

    @Override
    public void visit(TokenParmVal tokenParameter) throws FHIRPersistenceException {
        Integer compositeId = this.currentCompositeParameterName != null ? this.compositeIdCounter : null;
        adapter.tokenValue(tokenParameter.getName(), tokenParameter.getValueSystem(), tokenParameter.getValueCode(), compositeId);
    }

    @Override
    public void visit(QuantityParmVal quantityParameter) throws FHIRPersistenceException {
        Integer compositeId = this.currentCompositeParameterName != null ? this.compositeIdCounter : null;
        adapter.quantityValue(quantityParameter.getName(), quantityParameter.getValueSystem(), quantityParameter.getValueCode(), quantityParameter.getValueNumber(),
            quantityParameter.getValueNumberLow(), quantityParameter.getValueNumberHigh(), compositeId);

    }

    @Override
    public void visit(LocationParmVal locationParameter) throws FHIRPersistenceException {
        Integer compositeId = this.currentCompositeParameterName != null ? this.compositeIdCounter : null;
        adapter.locationValue(locationParameter.getName(), locationParameter.getValueLatitude(), locationParameter.getValueLongitude(), compositeId);
    }

    @Override
    public void visit(ReferenceParmVal ref) throws FHIRPersistenceException {
        Integer compositeId = this.currentCompositeParameterName != null ? this.compositeIdCounter : null;
        adapter.referenceValue(ref.getName(), ref.getRefValue(), compositeId);
    }

    @Override
    public void visit(CompositeParmVal compositeParameter) throws FHIRPersistenceException {
        if (this.currentCompositeParameterName != null) {
            throw new FHIRPersistenceException("found nested composite parameter which isn't supported. "
                    + "current:[" + currentCompositeParameterName + "]"
                    + " nested:[" + compositeParameter.getName() + "]");
        }

        // Each parameter contained within this composite will be assigned the same
        // compositeIdCounter value
        this.compositeIdCounter++;
        this.currentCompositeParameterName = compositeParameter.getName();
        for (ExtractedParameterValue epv: compositeParameter.getComponent()) {
            epv.accept(this);
        }
        this.currentCompositeParameterName = null;
    }
}
