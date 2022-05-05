/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.jdbc.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.logging.Logger;

import com.ibm.fhir.persistence.index.DateParameter;
import com.ibm.fhir.persistence.index.LocationParameter;
import com.ibm.fhir.persistence.index.NumberParameter;
import com.ibm.fhir.persistence.index.ParameterValueVisitorAdapter;
import com.ibm.fhir.persistence.index.QuantityParameter;
import com.ibm.fhir.persistence.index.SearchParametersTransport;
import com.ibm.fhir.persistence.index.StringParameter;
import com.ibm.fhir.persistence.index.TokenParameter;
import com.ibm.fhir.search.util.ReferenceValue;
import com.ibm.fhir.search.util.ReferenceValue.ReferenceType;


/**
 * Visitor implementation to build an instance of {@link SearchParametersTransport} to
 * provide support for shipping a set of search parameter values off to a remote
 * index service. This allows the parameters to be stored in the database in a
 * separate transaction, and allows the inserts to be batched together, providing
 * improved throughput.
 */
public class SearchParametersTransportAdapter implements ParameterValueVisitorAdapter {
    private static final Logger logger = Logger.getLogger(SearchParametersTransportAdapter.class.getName());

    // The builder we use to collect all the visited parameter values
    private final SearchParametersTransport.Builder builder;

    /**
     * Public constructor
     * @param resourceType
     * @param logicalId
     * @param logicalResourceId
     * @param shardKey
     */
    public SearchParametersTransportAdapter(String resourceType, String logicalId, long logicalResourceId, Short shardKey) {
        builder = SearchParametersTransport.builder()
            .withResourceType(resourceType)
            .withLogicalId(logicalId)
            .withLogicalResourceId(logicalResourceId)
            .withShardKey(shardKey);
    }

    /**
     * Build the SearchParametersTransport instance from the current state of builder
     * @return
     */
    public SearchParametersTransport build() {
        return builder.build();
    }

    @Override
    public void stringValue(String name, String valueString, Integer compositeId) {
        StringParameter value = new StringParameter();
        value.setName(name);
        value.setValue(valueString);
        value.setCompositeId(compositeId);
        builder.addStringValue(value);
    }

    @Override
    public void numberValue(String name, BigDecimal valueNumber, BigDecimal valueNumberLow, BigDecimal valueNumberHigh, Integer compositeId) {
        NumberParameter value = new NumberParameter();
        value.setName(name);
        value.setValue(valueNumber);
        value.setLowValue(valueNumberLow);
        value.setHighValue(valueNumberHigh);
        value.setCompositeId(compositeId);
        builder.addNumberValue(value);
    }

    @Override
    public void dateValue(String name, Timestamp valueDateStart, Timestamp valueDateEnd, Integer compositeId) {
        DateParameter value = new DateParameter();
        value.setName(name);
        value.setValueDateStart(valueDateStart);
        value.setValueDateEnd(valueDateEnd);
        value.setCompositeId(compositeId);
        builder.addDateValue(value);
    }

    @Override
    public void tokenValue(String name, String valueSystem, String valueCode, Integer compositeId) {
        TokenParameter value = new TokenParameter();
        value.setName(name);
        value.setValueSystem(valueSystem);
        value.setValueCode(valueCode);
        value.setCompositeId(compositeId);
        builder.addTokenValue(value);
    }

    @Override
    public void quantityValue(String name, String valueSystem, String valueCode, BigDecimal valueNumber, BigDecimal valueNumberLow, BigDecimal valueNumberHigh,
        Integer compositeId) {
        QuantityParameter value = new QuantityParameter();
        value.setName(name);
        value.setValueSystem(valueSystem);
        value.setValueCode(valueCode);
        value.setValueNumber(valueNumber);
        value.setValueNumberLow(valueNumberLow);
        value.setValueNumberHigh(valueNumberHigh);
        value.setCompositeId(compositeId);
        builder.addQuantityValue(value);
    }

    @Override
    public void locationValue(String name, Double valueLatitude, Double valueLongitude, Integer compositeId) {
        LocationParameter value = new LocationParameter();
        value.setName(name);
        value.setValueLatitude(valueLatitude);
        value.setValueLongitude(valueLongitude);
        value.setCompositeId(compositeId);
        builder.addLocationValue(value);
    }

    @Override
    public void referenceValue(String name, ReferenceValue refValue, Integer compositeId) {
        if (refValue == null) {
            return;
        }

        // The ReferenceValue has already been processed to convert the reference to
        // the required standard form, ready for insertion as a token value.

        String refResourceType = refValue.getTargetResourceType();
        String refLogicalId = refValue.getValue();
        Integer refVersion = refValue.getVersion();

        // Ignore references containing only a "display" element (apparently supported by the spec,
        // but contains nothing useful to store because there's no searchable value).
        // See ParameterVisitorBatchDAO
        if (refValue.getType() == ReferenceType.DISPLAY_ONLY || refValue.getType() == ReferenceType.INVALID) {
            // protect against code regression. Invalid/improper references should be
            // filtered out already.
            logger.warning("Invalid reference parameter type: name='" + name + "' type=" + refValue.getType().name());
            throw new IllegalArgumentException("Invalid reference parameter value. See server log for details.");
        }
        
        TokenParameter value = new TokenParameter();
        value.setName(name);
        value.setValueSystem(refResourceType);
        value.setValueCode(refLogicalId);
        value.setRefVersionId(refVersion);
        value.setCompositeId(compositeId);
        builder.addTokenValue(value);
    }
}
