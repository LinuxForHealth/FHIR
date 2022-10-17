/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.string.util;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

import org.linuxforhealth.fhir.exception.FHIROperationException;
import org.linuxforhealth.fhir.model.resource.OperationOutcome;
import org.linuxforhealth.fhir.model.string.util.strategy.StringSizeControlStrategy;
import org.linuxforhealth.fhir.model.type.code.IssueType;
import org.linuxforhealth.fhir.model.util.FHIRUtil;

/**
 * Controls the creation of the StringSizeControlStrategy objects using the ServiceLoader.
 */
public class StringSizeControlStrategyFactory {
    
    private static final StringSizeControlStrategyFactory FACTORY = new StringSizeControlStrategyFactory();
    
    private Map<String, StringSizeControlStrategy> keyToMemberMatchStrategy = new HashMap<>();

   /**
    * Private Constructor to load the StringSizeControlStrategy objects using the ServiceLoader
    */
    private StringSizeControlStrategyFactory() {
        ServiceLoader<StringSizeControlStrategy> strategies = ServiceLoader.load(StringSizeControlStrategy.class);
        for (StringSizeControlStrategy strategy : strategies) {
            keyToMemberMatchStrategy.put(strategy.getStrategyIdentifier(), strategy);
        }
    }

    /**
     * The various types of StringSizeControlStrategy.
     */
    public enum Strategy {
        MAX_BYTES("max_bytes");
        public String value;
        
        Strategy(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return this.value;
        }
    }
    
    /**
     * Gets the factory
     * @return
     */
    public static StringSizeControlStrategyFactory factory() {
        return FACTORY;
    }

    

    /**
     * Gets the strategy for this specific strategyIdentifier.
     * @param strategyIdentifier the unique StringSizeControlStrategy identifier.
     * @return the StringSizeControlStrategy
     * @throws FHIROperationException when the strategyIdentifier is invalid. 
     */
    public StringSizeControlStrategy getStrategy(String strategyIdentifier) throws FHIROperationException {
        StringSizeControlStrategy strategy = keyToMemberMatchStrategy.get(strategyIdentifier);
        if (strategy == null) {
            String message =  "The StringSizeControlStrategy is not found [" + strategyIdentifier + "]";
            OperationOutcome.Issue ooi = FHIRUtil.buildOperationOutcomeIssue(message, IssueType.EXCEPTION);
            throw new FHIROperationException(message, null).withIssue(ooi);
        }
        return strategy;
    }
}