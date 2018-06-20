/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.operation.validate;

import static com.ibm.watsonhealth.fhir.config.FHIRConfiguration.PROPERTY_USER_DEFINED_SCHEMATRON_ENABLED;
import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.id;
import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.string;

import java.io.InputStream;
import java.util.List;

import com.ibm.watsonhealth.fhir.config.FHIRConfigHelper;
import com.ibm.watsonhealth.fhir.exception.FHIROperationException;
import com.ibm.watsonhealth.fhir.model.IssueSeverityList;
import com.ibm.watsonhealth.fhir.model.IssueTypeList;
import com.ibm.watsonhealth.fhir.model.NarrativeStatusList;
import com.ibm.watsonhealth.fhir.model.OperationDefinition;
import com.ibm.watsonhealth.fhir.model.OperationOutcome;
import com.ibm.watsonhealth.fhir.model.OperationOutcomeIssue;
import com.ibm.watsonhealth.fhir.model.Parameters;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.model.ResourceContainer;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil.Format;
import com.ibm.watsonhealth.fhir.operation.AbstractOperation;
import com.ibm.watsonhealth.fhir.operation.context.FHIROperationContext;
import com.ibm.watsonhealth.fhir.operation.util.FHIROperationUtil;
import com.ibm.watsonhealth.fhir.rest.FHIRResourceHelpers;
import com.ibm.watsonhealth.fhir.validation.FHIRValidator;

public class ValidateOperation extends AbstractOperation {
    
    public ValidateOperation() {
        super();
    }

    @Override
    protected OperationDefinition buildOperationDefinition() {
        try {
            InputStream in = getClass().getClassLoader().getResourceAsStream("validate.json");
            return FHIRUtil.read(OperationDefinition.class, Format.JSON, in);            
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    @Override
    protected Parameters doInvoke(FHIROperationContext operationContext, Class<? extends Resource> resourceType, String logicalId, String versionId, Parameters parameters,
        FHIRResourceHelpers resourceHelper) throws FHIROperationException {
        try {
            ResourceContainer container = getParameter(parameters, "resource").getResource();
            Resource resource = FHIRUtil.getResourceContainerResource(container);
            List<OperationOutcomeIssue> issues = FHIRValidator.getInstance().validate(resource, isUserDefinedSchematronEnabled());
            if (!issues.isEmpty()) {
                throw new FHIROperationException("Input resource failed validation.").withIssue(issues);
            }
            return FHIROperationUtil.getOutputParameters(buildResourceValidOperationOutcome());
        } catch (FHIROperationException e) {
            throw e;
        } catch (Exception e) {
            throw new FHIROperationException("An error occurred during validation", e);
        }
    }
    
    private boolean isUserDefinedSchematronEnabled() {
        return FHIRConfigHelper.getBooleanProperty(PROPERTY_USER_DEFINED_SCHEMATRON_ENABLED, Boolean.FALSE).booleanValue();
    }

    private OperationOutcome buildResourceValidOperationOutcome() {
        OperationOutcome operationOutcome = factory.createOperationOutcome()
                .withId(id("allok"))
                .withText(factory.createNarrative()
                    .withStatus(factory.createNarrativeStatus().withValue(NarrativeStatusList.ADDITIONAL))
                    .withDiv(FHIRUtil.div("<div><p>All OK</p></div>")))
                .withIssue(factory.createOperationOutcomeIssue()
                    .withSeverity(factory.createIssueSeverity().withValue(IssueSeverityList.INFORMATION))
                    .withCode(factory.createIssueType().withValue(IssueTypeList.INFORMATIONAL))
                    .withDetails(factory.createCodeableConcept().withText(string("All OK"))));
        return operationOutcome;
    }
}
