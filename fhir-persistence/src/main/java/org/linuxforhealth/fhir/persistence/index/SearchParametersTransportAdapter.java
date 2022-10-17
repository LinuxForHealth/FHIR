/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.persistence.index;

import java.math.BigDecimal;
import java.time.Instant;

import org.linuxforhealth.fhir.exception.FHIROperationException;
import org.linuxforhealth.fhir.model.string.util.StringSizeControlStrategyFactory;
import org.linuxforhealth.fhir.model.string.util.StringSizeControlStrategyFactory.Strategy;
import org.linuxforhealth.fhir.model.string.util.strategy.StringSizeControlStrategy;
import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;



/**
 * Visitor adapter implementation to build an instance of {@link SearchParametersTransport} to
 * provide support for shipping a set of search parameter values off to a remote
 * index service. This allows the parameters to be stored in the database in a
 * separate transaction, and allows the inserts to be batched together, providing
 * improved throughput.
 */
public class SearchParametersTransportAdapter implements ParameterValueVisitorAdapter {

    // The builder we use to collect all the visited parameter values
    private final SearchParametersTransport.Builder builder;
    
    private StringSizeControlStrategy stringSizeControlStrategy;
    /**
     * Public constructor
     * @param resourceType
     * @param logicalId
     * @param logicalResourceId
     * @param versionId
     * @param lastUpdated
     * @param requestShard
     * @param parameterHash
     */
    public SearchParametersTransportAdapter(String resourceType, String logicalId, long logicalResourceId, 
            int versionId, Instant lastUpdated, String requestShard, String parameterHash) {
        builder = SearchParametersTransport.builder()
            .withResourceType(resourceType)
            .withLogicalId(logicalId)
            .withLogicalResourceId(logicalResourceId)
            .withVersionId(versionId)
            .withLastUpdated(lastUpdated)
            .withRequestShard(requestShard)
            .withParameterHash(parameterHash);
    }

    /**
     * Build the SearchParametersTransport instance from the current state of builder
     * @return
     */
    public SearchParametersTransport build() {
        return builder.build();
    }

    @Override
    public void stringValue(String name, String valueString, Integer compositeId, boolean wholeSystem, int maxBytes) throws FHIRPersistenceException {
        StringParameter value = new StringParameter();
        value.setName(name);
        stringSizeControlStrategy = getStringSizeControlStrategy();
        value.setValue(valueString != null ? stringSizeControlStrategy.truncateString(valueString, maxBytes) : valueString);
        value.setCompositeId(compositeId);
        value.setWholeSystem(wholeSystem);
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
    public void dateValue(String name, Instant valueDateStart, Instant valueDateEnd, Integer compositeId, boolean wholeSystem) {
        DateParameter value = new DateParameter();
        value.setName(name);
        value.setValueDateStart(valueDateStart);
        value.setValueDateEnd(valueDateEnd);
        value.setCompositeId(compositeId);
        value.setWholeSystem(wholeSystem);
        builder.addDateValue(value);
    }

    @Override
    public void tokenValue(String name, String valueSystem, String valueCode, Integer compositeId, boolean wholeSystem) {
        TokenParameter value = new TokenParameter();
        value.setName(name);
        value.setValueSystem(valueSystem);
        value.setValueCode(valueCode);
        value.setCompositeId(compositeId);
        value.setWholeSystem(wholeSystem);
        builder.addTokenValue(value);
    }

    @Override
    public void tagValue(String name, String valueSystem, String valueCode, boolean wholeSystem) {
        TagParameter value = new TagParameter();
        value.setName(name);
        value.setValueSystem(valueSystem);
        value.setValueCode(valueCode);
        value.setWholeSystem(wholeSystem);
        builder.addTagValue(value);
    }

    @Override
    public void profileValue(String name, String url, String version, String fragment, boolean wholeSystem) {
        ProfileParameter value = new ProfileParameter();
        value.setName(name);
        value.setUrl(url);
        value.setVersion(version);
        value.setFragment(fragment);
        value.setWholeSystem(wholeSystem);
        builder.addProfileValue(value);
    }

    @Override
    public void securityValue(String name, String valueSystem, String valueCode, boolean wholeSystem) {
        SecurityParameter value = new SecurityParameter();
        value.setName(name);
        value.setValueSystem(valueSystem);
        value.setValueCode(valueCode);
        value.setWholeSystem(wholeSystem);
        builder.addSecurityValue(value);
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
    public void referenceValue(String name, String refResourceType, String refLogicalId, Integer refVersion, Integer compositeId) {
        ReferenceParameter value = new ReferenceParameter();
        value.setName(name);
        value.setResourceType(refResourceType);
        value.setLogicalId(refLogicalId);
        value.setRefVersionId(refVersion);
        value.setCompositeId(compositeId);
        builder.addReferenceValue(value);
    }
    
    /**
     * 
     * @return
     * @throws FHIRPersistenceException
     */
    private StringSizeControlStrategy getStringSizeControlStrategy() throws FHIRPersistenceException {
        try {
            return StringSizeControlStrategyFactory.factory().
                    getStrategy(Strategy.MAX_BYTES.getValue());
        } catch (FHIROperationException foe) {
            FHIRPersistenceException fpe = new FHIRPersistenceException(foe.getMessage());
            if(foe.getIssues() != null) {
                fpe.withIssue(foe.getIssues());
            }
            throw fpe;
        }
    }
}
