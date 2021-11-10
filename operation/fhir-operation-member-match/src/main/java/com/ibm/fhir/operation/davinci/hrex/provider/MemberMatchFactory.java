/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.davinci.hrex.provider;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.operation.davinci.hrex.configuration.ConfigurationAdapter;
import com.ibm.fhir.operation.davinci.hrex.provider.strategy.DefaultMemberMatchStrategy;
import com.ibm.fhir.operation.davinci.hrex.provider.strategy.MemberMatchStrategy;
import com.ibm.fhir.server.spi.operation.FHIROperationUtil;

/**
 * Controls the creation of the MemberMatchProviderStrategy objects using the ServiceLoader.
 */
public class MemberMatchFactory {
    private static final MemberMatchFactory FACTORY = new MemberMatchFactory();

    private static final String DEFAULT_STRATEGY = "default";

    private Map<String, MemberMatchStrategy> keyToMemberMatchStrategy = new HashMap<>();

    /**
     * private to the class so there is a minimal amount of strategies created and stored memory.
     */
    private MemberMatchFactory() {
        ServiceLoader<MemberMatchStrategy> strategies = ServiceLoader.load(MemberMatchStrategy.class);
        for (MemberMatchStrategy strategy : strategies) {
            keyToMemberMatchStrategy.put(strategy.getMemberMatchIdentifier(), strategy);
        }
        // Last so we don't let it get overridden by an aggressive implementor.
        keyToMemberMatchStrategy.put(DEFAULT_STRATEGY, new DefaultMemberMatchStrategy());
    }

    /**
     * Gets the factory
     * @return
     */
    public static MemberMatchFactory factory() {
        return FACTORY;
    }

    /**
     * Gets the strategy for this specific configuration.
     * @param config
     * @return the MemberMatchStrategy or the DefaultMemberMatchStrategy
     * @throws FHIROperationException
     */
    public MemberMatchStrategy getStrategy(ConfigurationAdapter config) throws FHIROperationException {
        String key = config.getStrategyKey();
        MemberMatchStrategy strategy = keyToMemberMatchStrategy.get(key);
        if (strategy == null) {
            throw FHIROperationUtil.buildExceptionWithIssue("The MemberMatchStrategy is not found [" + key + "]", IssueType.EXCEPTION);
        }
        return strategy;
    }
}
