/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.operation.davinci.hrex;

import org.linuxforhealth.fhir.exception.FHIROperationException;
import org.linuxforhealth.fhir.model.resource.OperationDefinition;
import org.linuxforhealth.fhir.model.resource.Parameters;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.type.code.IssueType;
import org.linuxforhealth.fhir.operation.davinci.hrex.configuration.ConfigurationAdapter;
import org.linuxforhealth.fhir.operation.davinci.hrex.configuration.ConfigurationFactory;
import org.linuxforhealth.fhir.operation.davinci.hrex.provider.MemberMatchFactory;
import org.linuxforhealth.fhir.operation.davinci.hrex.provider.strategy.MemberMatchStrategy;
import org.linuxforhealth.fhir.registry.FHIRRegistry;
import org.linuxforhealth.fhir.search.util.SearchHelper;
import org.linuxforhealth.fhir.server.spi.operation.AbstractOperation;
import org.linuxforhealth.fhir.server.spi.operation.FHIROperationContext;
import org.linuxforhealth.fhir.server.spi.operation.FHIROperationUtil;
import org.linuxforhealth.fhir.server.spi.operation.FHIRResourceHelpers;

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
            String logicalId, String versionId, Parameters parameters, FHIRResourceHelpers resourceHelper, SearchHelper searchHelper)
            throws FHIROperationException {
        ConfigurationAdapter config = ConfigurationFactory.factory().getConfigurationAdapter();

        if (!config.enabled()) {
            throw FHIROperationUtil.buildExceptionWithIssue("$member-match is not supported", IssueType.NOT_SUPPORTED);
        }

        MemberMatchStrategy strategy = MemberMatchFactory.factory().getStrategy(config);
        return strategy.execute(operationContext, parameters, resourceHelper);
    }
}