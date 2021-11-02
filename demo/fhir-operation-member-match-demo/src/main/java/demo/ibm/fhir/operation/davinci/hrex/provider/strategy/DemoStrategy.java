/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package demo.ibm.fhir.operation.davinci.hrex.provider.strategy;

import java.util.logging.Logger;

import com.ibm.fhir.config.PropertyGroup;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.operation.davinci.hrex.configuration.ConfigurationFactory;
import com.ibm.fhir.operation.davinci.hrex.provider.strategy.DefaultMemberMatchStrategy;
import com.ibm.fhir.operation.davinci.hrex.provider.strategy.MemberMatchResult;

/**
 * Used to Demonstrate a custom Strategy
 */
public class DemoStrategy extends DefaultMemberMatchStrategy {

    private static final Logger LOG = Logger.getLogger(DemoStrategy.class.getSimpleName());

    @Override
    public String getMemberMatchIdentifier() {
        return "demo";
    }

    @Override
    public void validate(Parameters input) throws FHIROperationException {
        LOG.info("Validating Content for strategy - " + this.getMemberMatchIdentifier());
        super.validate(input);
    }

    @Override
    public MemberMatchResult executeMemberMatch() throws FHIROperationException {
        LOG.info("executeMemberMatch for strategy - " + this.getMemberMatchIdentifier());
        PropertyGroup group = ConfigurationFactory.factory().getConfigurationAdapter().getExtendedStrategyPropertyGroup();
        if (group != null) {
            LOG.info("executeMemberMatch Extend Strategy Config is " + group.getJsonObj());
        }
        return super.executeMemberMatch();
    }
}