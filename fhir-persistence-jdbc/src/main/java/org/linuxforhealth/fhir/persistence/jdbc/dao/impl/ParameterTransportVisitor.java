/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.persistence.jdbc.dao.impl;

import java.time.Instant;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;
import org.linuxforhealth.fhir.persistence.index.CanonicalSupport;
import org.linuxforhealth.fhir.persistence.index.ParameterValueVisitorAdapter;
import org.linuxforhealth.fhir.persistence.index.ProfileParameter;
import org.linuxforhealth.fhir.persistence.jdbc.JDBCConstants;
import org.linuxforhealth.fhir.persistence.jdbc.dto.CompositeParmVal;
import org.linuxforhealth.fhir.persistence.jdbc.dto.DateParmVal;
import org.linuxforhealth.fhir.persistence.jdbc.dto.ExtractedParameterValue;
import org.linuxforhealth.fhir.persistence.jdbc.dto.ExtractedParameterValueVisitor;
import org.linuxforhealth.fhir.persistence.jdbc.dto.LocationParmVal;
import org.linuxforhealth.fhir.persistence.jdbc.dto.NumberParmVal;
import org.linuxforhealth.fhir.persistence.jdbc.dto.QuantityParmVal;
import org.linuxforhealth.fhir.persistence.jdbc.dto.ReferenceParmVal;
import org.linuxforhealth.fhir.persistence.jdbc.dto.StringParmVal;
import org.linuxforhealth.fhir.persistence.jdbc.dto.TokenParmVal;
import org.linuxforhealth.fhir.schema.control.FhirSchemaConstants;
import org.linuxforhealth.fhir.search.SearchConstants;
import org.linuxforhealth.fhir.search.util.ReferenceValue;
import org.linuxforhealth.fhir.search.util.ReferenceValue.ReferenceType;


/**
 * A visitor to map parameters to a format suitable for transport to another
 * system (e.g. for remote indexing)
 */
public class ParameterTransportVisitor implements ExtractedParameterValueVisitor {
    private static final Logger logger = Logger.getLogger(ParameterTransportVisitor.class.getName());
    private static final Boolean IS_WHOLE_SYSTEM = Boolean.TRUE;

    // The adapter to which we delegate each of our visit calls
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

        if (SearchConstants.PROFILE.equals(stringParameter.getName())) {
            // special case to store profile parameters in their own table
            ProfileParameter pp = CanonicalSupport.createProfileParameter(stringParameter.getName(), stringParameter.getValueString());
            adapter.profileValue(pp.getName(), pp.getUrl(), pp.getVersion(), pp.getFragment(), IS_WHOLE_SYSTEM);
        } else {
            Integer compositeId = this.currentCompositeParameterName != null ? this.compositeIdCounter : null;
            adapter.stringValue(stringParameter.getName(), stringParameter.getValueString(), compositeId, stringParameter.isWholeSystem(), FhirSchemaConstants.MAX_SEARCH_STRING_BYTES);
        }
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
        Instant dateStart = dateParameter.getValueDateStart().toInstant();
        Instant dateEnd = dateParameter.getValueDateEnd().toInstant();
        adapter.dateValue(dateParameter.getName(), dateStart, dateEnd, compositeId, dateParameter.isWholeSystem());
    }

    @Override
    public void visit(TokenParmVal tokenParameter) throws FHIRPersistenceException {
        Integer compositeId = this.currentCompositeParameterName != null ? this.compositeIdCounter : null;
        // tag and profile search params are often low-selectivity (many resources sharing the same value) so
        // we put them into their own tables to allow better cardinality estimation by the query
        // optimizer
        switch (tokenParameter.getName()) {
        case SearchConstants.TAG:
            adapter.tagValue(tokenParameter.getName(), tokenParameter.getValueSystem(), tokenParameter.getValueCode(), IS_WHOLE_SYSTEM);
            break;
        case SearchConstants.SECURITY:
            adapter.securityValue(tokenParameter.getName(), tokenParameter.getValueSystem(), tokenParameter.getValueCode(), IS_WHOLE_SYSTEM);
            break;
        default:
            adapter.tokenValue(tokenParameter.getName(), tokenParameter.getValueSystem(), tokenParameter.getValueCode(), compositeId, tokenParameter.isWholeSystem());
        }
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
    public void visit(ReferenceParmVal rpv) throws FHIRPersistenceException {
        Integer compositeId = this.currentCompositeParameterName != null ? this.compositeIdCounter : null;

        // The ReferenceValue has already been processed to convert the reference to
        // the required standard form, ready for insertion as a token value.
        ReferenceValue refValue = rpv.getRefValue();
        String resourceType = rpv.getResourceType();
        String refResourceType = refValue.getTargetResourceType();
        String refLogicalId = refValue.getValue();
        Integer refVersion = refValue.getVersion();
        if (refValue.getType() == ReferenceType.DISPLAY_ONLY || refValue.getType() == ReferenceType.INVALID) {
            // protect against code regression. Invalid/improper references should be
            // filtered out already.
            logger.warning("Invalid reference parameter type: '" + resourceType + "." + rpv.getName() + "' type=" + refValue.getType().name());
            throw new IllegalArgumentException("Invalid reference parameter value. See server log for details.");
        }

        if (refResourceType == null) {
            // Prior to V0027, references without a target resource type would be assigned the
            // DEFAULT_TOKEN_SYSTEM (having a valid system makes queries faster). For V0027,
            // all reference values get an entry in logical_resource_ident so in order to use
            // a valid resource type we use "Resource" instead.
            refResourceType = JDBCConstants.RESOURCE;
        }
        adapter.referenceValue(rpv.getName(), refResourceType, refLogicalId, refVersion, compositeId);
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
