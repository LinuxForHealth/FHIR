/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.davinci.hrex;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.OperationDefinition;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.operation.davinci.hrex.configuration.ConfigurationAdapter;
import com.ibm.fhir.operation.davinci.hrex.configuration.ConfigurationFactory;
import com.ibm.fhir.operation.davinci.hrex.provider.MemberMatchFactory;
import com.ibm.fhir.operation.davinci.hrex.provider.strategy.MemberMatchStrategy;
import com.ibm.fhir.registry.FHIRRegistry;
import com.ibm.fhir.server.operation.spi.AbstractOperation;
import com.ibm.fhir.server.operation.spi.FHIROperationContext;
import com.ibm.fhir.server.operation.spi.FHIRResourceHelpers;

/**
 * Implements the $MemberMatch Operation
 */
public class MemberMatchOperation extends AbstractOperation {

    public MemberMatchOperation() {
        super();
    }

    @Override
    protected OperationDefinition buildOperationDefinition() {
        return FHIRRegistry.getInstance()
                .getResource("http://hl7.org/fhir/us/davinci-hrex/OperationDefinition/member-match",
                    OperationDefinition.class);
    }

    @Override
    protected Parameters doInvoke(FHIROperationContext operationContext, Class<? extends Resource> resourceType,
            String logicalId, String versionId, Parameters parameters, FHIRResourceHelpers resourceHelper) throws FHIROperationException {
        ConfigurationAdapter config = ConfigurationFactory.factory().getConfigurationAdapter();
        MemberMatchStrategy strategy = MemberMatchFactory.factory().getStrategy(config);
        return strategy.execute(operationContext, parameters, resourceHelper);
    }
}