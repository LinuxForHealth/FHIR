/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.operation.cpg;

import static org.linuxforhealth.fhir.cql.helpers.ModelHelper.fhirstring;

import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.linuxforhealth.fhir.core.ResourceType;
import org.linuxforhealth.fhir.cql.helpers.ParameterMap;
import org.linuxforhealth.fhir.cql.translator.CqlTranslationException;
import org.linuxforhealth.fhir.exception.FHIROperationException;
import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.parser.FHIRParser;
import org.linuxforhealth.fhir.model.resource.Library;
import org.linuxforhealth.fhir.model.resource.OperationDefinition;
import org.linuxforhealth.fhir.model.resource.OperationOutcome.Issue;
import org.linuxforhealth.fhir.model.resource.Parameters;
import org.linuxforhealth.fhir.model.resource.Parameters.Parameter;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.type.CodeableConcept;
import org.linuxforhealth.fhir.model.type.code.IssueSeverity;
import org.linuxforhealth.fhir.model.type.code.IssueType;
import org.linuxforhealth.fhir.persistence.SingleResourceResult;
import org.linuxforhealth.fhir.registry.FHIRRegistry;
import org.linuxforhealth.fhir.search.util.SearchHelper;
import org.linuxforhealth.fhir.server.spi.operation.FHIROperationContext;
import org.linuxforhealth.fhir.server.spi.operation.FHIRResourceHelpers;

public class LibraryEvaluateOperation extends AbstractCqlOperation {

    private static final Logger logger = Logger.getLogger(LibraryEvaluateOperation.class.getName());

    @Override
    protected OperationDefinition buildOperationDefinition() {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("OperationDefinition-cpg-library-evaluate.json")) {
            return FHIRParser.parser(Format.JSON).parse(in);
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    @Override
    protected Parameters doInvoke(FHIROperationContext operationContext, Class<? extends Resource> resourceType,
            String logicalId, String versionId, Parameters parameters, FHIRResourceHelpers resourceHelper,
            SearchHelper searchHelper) throws FHIROperationException {

        Parameters result = null;

        ParameterMap paramMap = new ParameterMap(parameters);

        checkUnsupportedParameters(paramMap);

        try {
            Library primaryLibrary = null;
            if (operationContext.getType().equals(FHIROperationContext.Type.INSTANCE)) {
                SingleResourceResult<?> readResult = resourceHelper.doRead(ResourceType.LIBRARY.value(), logicalId);
                primaryLibrary = (Library) readResult.getResource();
                if (primaryLibrary == null) {
                    throw new IllegalArgumentException("failed to resolve library with resource id: " + logicalId);
                }
            } else if (operationContext.getType().equals(FHIROperationContext.Type.RESOURCE_TYPE)) {
                Parameter param = paramMap.getSingletonParameter("library");
                String canonicalURL = ((org.linuxforhealth.fhir.model.type.Uri) param.getValue()).getValue();
                primaryLibrary = FHIRRegistry.getInstance().getResource(canonicalURL, Library.class);
                if (primaryLibrary == null) {
                    throw new IllegalArgumentException("failed to resolve library with canonical URL: " + canonicalURL);
                }
            } else {
                throw new UnsupportedOperationException("This operation must be invoked in the context of the Library resource");
            }

            result = doEvaluation(resourceHelper, paramMap, searchHelper, primaryLibrary);

        } catch (FHIROperationException fex) {
            throw fex;
        } catch (IllegalArgumentException | CqlTranslationException iex) {
            logger.log(Level.SEVERE, "Bad Request", iex);
            throw new FHIROperationException(iex.getMessage(), iex)
                    .withIssue(Issue.builder()
                        .severity(IssueSeverity.ERROR)
                        .code(IssueType.INVALID)
                        .details(CodeableConcept.builder().text(fhirstring(iex.getMessage())).build())
                        .build());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Evaluation failed", ex);
            throwOperationException(ex);
        }

        return result;
    }

    @Override
    protected Set<String> getCqlExpressionsToEvaluate(ParameterMap paramMap) {
        Set<String> expressions = null;
        if ( paramMap.containsKey(PARAM_IN_EXPRESSION) ) {
            List<Parameter> expressionsParams = paramMap.getParameter(PARAM_IN_EXPRESSION);
            if (expressionsParams != null) {
                expressions = expressionsParams.stream().map(p -> ((org.linuxforhealth.fhir.model.type.String) p.getValue()).getValue()).collect(Collectors.toSet());
            }
        }
        return expressions;
    }

}
