/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.operation.validate;

import static com.ibm.watsonhealth.fhir.config.FHIRConfiguration.PROPERTY_USER_DEFINED_SCHEMATRON_ENABLED;
import static com.ibm.watsonhealth.fhir.model.type.String.string;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.ibm.watsonhealth.fhir.config.FHIRConfigHelper;
import com.ibm.watsonhealth.fhir.exception.FHIROperationException;
import com.ibm.watsonhealth.fhir.model.format.Format;
import com.ibm.watsonhealth.fhir.model.resource.OperationDefinition;
import com.ibm.watsonhealth.fhir.model.resource.OperationOutcome;
import com.ibm.watsonhealth.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.watsonhealth.fhir.model.resource.Parameters;
import com.ibm.watsonhealth.fhir.model.resource.Parameters.Parameter;
import com.ibm.watsonhealth.fhir.model.resource.Resource;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.IssueSeverity;
import com.ibm.watsonhealth.fhir.model.type.IssueType;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.NarrativeStatus;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;
import com.ibm.watsonhealth.fhir.operation.AbstractOperation;
import com.ibm.watsonhealth.fhir.operation.context.FHIROperationContext;
import com.ibm.watsonhealth.fhir.operation.util.FHIROperationUtil;
import com.ibm.watsonhealth.fhir.rest.FHIRResourceHelpers;
import com.ibm.watsonhealth.fhir.model.validation.FHIRValidator;

public class ValidateOperation extends AbstractOperation {
    
    public ValidateOperation() {
        super();
    }

    @Override
    protected OperationDefinition buildOperationDefinition() {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("validate.json");){
            return FHIRUtil.read(OperationDefinition.class, Format.JSON, in);            
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    @Override
    protected Parameters doInvoke(FHIROperationContext operationContext, Class<? extends Resource> resourceType, String logicalId, String versionId, Parameters parameters,
        FHIRResourceHelpers resourceHelper) throws FHIROperationException {
        try {
            Parameter parameter = getParameter(parameters, "resource");
            if (parameter == null) {
                throw buildExceptionWithIssue("Input parameter 'resource' is required for the $validate operation", IssueType.ValueSet.INVALID);
            }
            Resource resource = parameter.getResource() ;
            List<Issue> issues = FHIRValidator.validator(resource).validate();
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
        
        List <Issue> issueList = new ArrayList <Issue> ();
        Issue newIssue = Issue.builder(IssueSeverity.INFORMATION, IssueType.INFORMATIONAL)
                .details(CodeableConcept.builder().text(string("All OK")).build())
                .build();
        
        issueList.add(newIssue);
                
        OperationOutcome operationOutcome = OperationOutcome.builder(issueList)
                .id(Id.of("allok"))
                .text(Narrative.builder(NarrativeStatus.ADDITIONAL, "<div><p>All OK</p></div>").build())
                .build();
                
        return operationOutcome;
    }
}
