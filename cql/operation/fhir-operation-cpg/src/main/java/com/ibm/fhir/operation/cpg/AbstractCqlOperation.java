/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.operation.cpg;

import static com.ibm.fhir.cql.helpers.ModelHelper.fhirstring;
import static com.ibm.fhir.cql.helpers.ModelHelper.javastring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.cqframework.cql.elm.execution.VersionedIdentifier;
import org.opencds.cqf.cql.engine.data.DataProvider;
import org.opencds.cqf.cql.engine.debug.DebugLibraryResultEntry;
import org.opencds.cqf.cql.engine.debug.DebugLocator;
import org.opencds.cqf.cql.engine.debug.DebugMap;
import org.opencds.cqf.cql.engine.debug.DebugResult;
import org.opencds.cqf.cql.engine.debug.DebugResultEntry;
import org.opencds.cqf.cql.engine.execution.CqlEngine;
import org.opencds.cqf.cql.engine.execution.EvaluationResult;
import org.opencds.cqf.cql.engine.execution.InMemoryLibraryLoader;
import org.opencds.cqf.cql.engine.execution.LibraryLoader;
import org.opencds.cqf.cql.engine.terminology.TerminologyProvider;

import com.ibm.fhir.core.FHIRConstants;
import com.ibm.fhir.cql.engine.converter.FHIRTypeConverter;
import com.ibm.fhir.cql.engine.converter.impl.FHIRTypeConverterImpl;
import com.ibm.fhir.cql.engine.searchparam.SearchParameterResolver;
import com.ibm.fhir.cql.engine.server.retrieve.ServerFHIRRetrieveProvider;
import com.ibm.fhir.cql.engine.server.terminology.ServerFHIRTerminologyProvider;
import com.ibm.fhir.cql.helpers.DataProviderFactory;
import com.ibm.fhir.cql.helpers.LibraryHelper;
import com.ibm.fhir.cql.helpers.ParameterMap;
import com.ibm.fhir.cql.translator.CqlTranslationProvider;
import com.ibm.fhir.cql.translator.FHIRLibraryLibrarySourceProvider;
import com.ibm.fhir.cql.translator.impl.InJVMCqlTranslationProvider;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.Library;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.search.util.SearchUtil;
import com.ibm.fhir.server.spi.operation.AbstractOperation;
import com.ibm.fhir.server.spi.operation.FHIRResourceHelpers;

public abstract class AbstractCqlOperation extends AbstractOperation {

    public static final String PARAM_IN_EXPRESSION = "expression";
    public static final String PARAM_IN_PARAMETERS = "parameters";
    public static final String PARAM_IN_SUBJECT = "subject";
    public static final String PARAM_IN_DEBUG= "debug";
    public static final String PARAM_OUT_RETURN = "return";
    public static final String PARAM_OUT_DEBUG_RESULT = "debugResult";

    public static final String PARAM_IN_USE_SERVER_DATA = "useServerData";
    public static final String PARAM_IN_DATA = "data";
    public static final String PARAM_IN_PREFETCH_DATA = "prefetchData";
    public static final String PARAM_IN_DATA_ENDPOINT = "dataEndpoint";
    public static final String PARAM_IN_CONTENT_ENDPOINT = "contentEndpoint";
    public static final String PARAM_IN_TERMINOLOGY_ENDPOINT = "terminologyEndpoint";

    /**
     * Check if the user provided any currently unsupported operation parameters.
     *
     * @param paramMap
     *            Operation input parameters
     * @throws FHIROperationException
     *             when one or more unsupported parameters are found
     */
    protected void checkUnsupportedParameters(ParameterMap paramMap) throws FHIROperationException {
        List<Issue> issues = new ArrayList<>();

        CollectionUtils.addIgnoreNull(issues, checkUnsupportedParameter(paramMap, PARAM_IN_USE_SERVER_DATA));
        CollectionUtils.addIgnoreNull(issues, checkUnsupportedParameter(paramMap, PARAM_IN_DATA));
        CollectionUtils.addIgnoreNull(issues, checkUnsupportedParameter(paramMap, PARAM_IN_PREFETCH_DATA));
        CollectionUtils.addIgnoreNull(issues, checkUnsupportedParameter(paramMap, PARAM_IN_DATA_ENDPOINT));
        CollectionUtils.addIgnoreNull(issues, checkUnsupportedParameter(paramMap, PARAM_IN_CONTENT_ENDPOINT));
        CollectionUtils.addIgnoreNull(issues, checkUnsupportedParameter(paramMap, PARAM_IN_TERMINOLOGY_ENDPOINT));

        if( ! issues.isEmpty() ) {
            FHIROperationException ex = new FHIROperationException("Request contains one or more unsupported parameters");
            ex.setIssues(issues);
            throw ex;
        }
    }

    /**
     * Check for a single unsupported parameter by name
     *
     * @param paramMap
     *            Operation input parameters
     * @param paramName
     *            Name of unsupported parameter
     * @return FHIR Issue data describing the error or null if no such parameter is found.
     */
    protected Issue checkUnsupportedParameter(ParameterMap paramMap, String paramName) {
        Issue issue = null;
        if( paramMap.containsKey(paramName) ) {
            String msg = "Parameter '" + paramName + "' is not currently supported";
            issue = OperationOutcome.Issue.builder()
                    .severity(IssueSeverity.FATAL)
                    .code(IssueType.NOT_SUPPORTED.toBuilder()
                        .extension(Extension.builder()
                            .url(FHIRConstants.EXT_BASE + "not-supported-detail")
                            .value(paramName)
                            .build())
                        .build())
                    .details(CodeableConcept.builder().text(fhirstring(msg)).build())
                    .build();
        }
        return issue;
    }

    /**
     * Evaluate the requested CQL from the provided Library resource.
     *
     * @param resourceHelper
     *            Resource operation provider for loading related Library resources
     * @param paramMap
     *            Parameters object that describes the operation to perform
     * @param primaryLibrary
     *            Library resource that is the entry point for the evaluation
     * @return Parameters describing the evaluation result
     */
    protected Parameters doEvaluation(FHIRResourceHelpers resourceHelper, ParameterMap paramMap, SearchUtil searchHelper, Library primaryLibrary) {
        List<Library> libraries = LibraryHelper.loadLibraries(primaryLibrary);
        return doEvaluation(resourceHelper, paramMap, searchHelper, libraries);
    }

    /**
     * Evaluate the requested CQL from the provided Library resource.
     *
     * @param resourceHelper
     *            Resource operation provider for loading related Library resources
     * @param paramMap
     *            Parameters object that describes the operation to perform
     * @param primaryLibrary
     *            Library resource that is the entry point for the evaluation
     * @param searchHelper
     *            A helper class for working with FHIR search parameters
     * @param libraries
     *            List of all necessary library resources. The first entry in the list is the primary library.
     * @return Parameters describing the evaluation result
     */
    protected Parameters doEvaluation(FHIRResourceHelpers resourceHelper, ParameterMap paramMap, SearchUtil searchHelper, List<Library> libraries) {
        Library primaryLibrary = libraries.get(0);
        LibraryLoader ll = createLibraryLoader(libraries);

        FHIRTypeConverter typeConverter = new FHIRTypeConverterImpl();
        ParameterConverter parameterConverter = new ParameterConverter(typeConverter);

        TerminologyProvider termProvider = new ServerFHIRTerminologyProvider(resourceHelper);

        SearchParameterResolver resolver = new SearchParameterResolver(searchHelper);
        ServerFHIRRetrieveProvider retrieveProvider = new ServerFHIRRetrieveProvider(resourceHelper, resolver);
        retrieveProvider.setExpandValueSets(false); // TODO - use server config settings
        retrieveProvider.setTerminologyProvider(termProvider);
        retrieveProvider.setPageSize(FHIRConstants.FHIR_PAGE_SIZE_DEFAULT_MAX); // TODO - use server config settings?

        Map<String, DataProvider> dataProviders = DataProviderFactory.createDataProviders(retrieveProvider);

        VersionedIdentifier vid = new VersionedIdentifier();
        vid.withId(javastring(primaryLibrary.getName()));
        vid.withVersion(javastring(primaryLibrary.getVersion()));

        Pair<String, Object> context = getCqlContext(paramMap);

        Map<String, Object> engineParameters = getCqlParameters(parameterConverter, paramMap);

        Set<String> expressions = getCqlExpressionsToEvaluate(paramMap);

        DebugMap debugMap = getDebugMap(paramMap);

        // TODO - add configuration support for the CQL engine's local time zone
        CqlEngine engine = new CqlEngine(ll, dataProviders, termProvider);
        EvaluationResult evaluationResult = engine.evaluate(vid, expressions, context, engineParameters, debugMap);

        Parameters.Builder output = Parameters.builder()
                .parameter(parameterConverter.toParameter(PARAM_OUT_RETURN, evaluationResult.expressionResults));

        if( debugMap != null) {
            // Need to experiment with how valuable this is right now. There are a couple of issues
            // that I noted. Most importantly, I don't think this information will be available when
            // the engine throws an exception (e.g. NPE). Beyond that, the information that is available
            // is not ordered, so it can't be used to track the actual path through the engine. Lastly,
            // the data captured is incomplete. The best path currently is to use the logging produced
            // in System.out
            Parameter debugResult = convertDebugResultToParameter(evaluationResult);
            output.parameter(debugResult);
        }

        return output.build();
    }

    private Parameter convertDebugResultToParameter(EvaluationResult evaluationResult) {
        Parameter.Builder debugResultBuilder = Parameter.builder().name(fhirstring(PARAM_OUT_DEBUG_RESULT));

        Parameter debugResultParameter = null;
        DebugResult debugResult = evaluationResult.getDebugResult();
        if( debugResult != null ) {
            Parameters.Builder debugResultEntries = Parameters.builder();
            for( Map.Entry<String,DebugLibraryResultEntry> libraryResult : debugResult.getLibraryResults().entrySet() ) {
                String libraryId = libraryResult.getKey();
                DebugLibraryResultEntry librayResultEntry = libraryResult.getValue();

                for( Map.Entry<DebugLocator,List<DebugResultEntry>> e : librayResultEntry.getResults().entrySet() ) {
                    DebugLocator dl = e.getKey();
                    //The DebugResultEntry class captures no object state right now, so it is useless
                    debugResultEntries.parameter( Parameter.builder().name(fhirstring(libraryId)).value(fhirstring(dl.getLocatorType().toString() + "|" + dl.getLocator())).build() );
                }
                debugResultBuilder.resource(debugResultEntries.build());
            }
            debugResultParameter = debugResultBuilder.build();
        }
        return debugResultParameter;
    }

    /**
     * Retrieve subject parameter from the operation input
     *
     * @param paramMap
     *            operation input
     * @return Pair of context name and value
     */
    protected Pair<String, Object> getCqlContext(ParameterMap paramMap) {
        Pair<String, Object> context = null;
        Parameter subjectParam = paramMap.getSingletonParameter(PARAM_IN_SUBJECT);
        if (subjectParam != null) {
            context = getCqlContext(subjectParam);
        }
        return context;
    }

    /**
     * Retrieve subject parameter from the operation input
     *
     * @param paramMap
     *            operation input
     * @return Pair of context name and value
     */
    protected Pair<String, Object> getCqlContext(Parameter subjectParam) {
        Pair<String, Object> context = null;

        String ref = javastring(((com.ibm.fhir.model.type.String) subjectParam.getValue()));
        String[] parts = ref.split("/");
        if (parts.length >= 2) {
            context = Pair.of(parts[parts.length - 2], parts[parts.length - 1]);
        } else {
            throw new IllegalArgumentException(String.format("Invalid format for subject parameter '%s'. Value must contain a resource type and ID.", ref));
        }
        return context;
    }

    /**
     * Retrieve CQL parameters data from the operation input. FHIR data types are
     * automatically converted to CQL System data types.
     *
     * @param converter
     *            Converter logic for transforming FHIR data types to CQL
     *            input parameters.
     * @param paramMap
     *            operation input
     * @return Map of parameter name to parameter value where value objects are
     *         types in the CQL typesystem.
     */
    protected Map<String, Object> getCqlParameters(ParameterConverter converter, ParameterMap paramMap) {
        Map<String, Object> engineParameters = null;
        Parameter parametersParam = paramMap.getOptionalSingletonParameter(PARAM_IN_PARAMETERS);
        if (parametersParam != null) {
            engineParameters = getCqlEngineParameters(converter, parametersParam);
        }
        return engineParameters;
    }

    /**
     * Retrieve CQL parameters data from the operation input. FHIR data types are
     * automatically converted to CQL System data types.
     *
     * @param converter
     *            Converter logic for transforming FHIR data types to CQL
     *            input parameters.
     * @param parametersParam
     *            operation input
     * @return Map of parameter name to parameter value where value objects are
     *         types in the CQL typesystem.
     */
    protected Map<String, Object> getCqlEngineParameters(ParameterConverter converter, Parameter parametersParam) {
        Map<String, Object> engineParameters;
        engineParameters = new HashMap<>();

        Parameters nested = (Parameters) parametersParam.getResource();
        for (Parameter engineParam : nested.getParameter()) {
            Object converted = converter.toCql(engineParam);
            engineParameters.put(javastring(engineParam.getName()), converted);
        }
        return engineParameters;
    }

    /**
     * Get the expression names to evaluate in the primary library
     *
     * @param paramMap
     *            operation input
     * @return expression names to evaluate
     */
    protected abstract Set<String> getCqlExpressionsToEvaluate(ParameterMap paramMap);

    /**
     * Create a CQL Library loader for the content of the provided FHIR Library resources
     *
     * @param libraries
     *            FHIR Library resources containing CQL and/or ELM content
     * @return CQL Engine LibraryLoader
     */
    protected LibraryLoader createLibraryLoader(List<Library> libraries) {
        FHIRLibraryLibrarySourceProvider sourceProvider = new FHIRLibraryLibrarySourceProvider(libraries);
        CqlTranslationProvider translator = new InJVMCqlTranslationProvider(sourceProvider);

        Collection<org.cqframework.cql.elm.execution.Library> result = libraries.stream()
                .flatMap( fl -> LibraryHelper.loadLibrary(translator, fl).stream() )
                .filter( Objects::nonNull )
                .collect(Collectors.toCollection(
                    () -> new TreeSet<>( CqlLibraryComparator.INSTANCE ) ));
        return new InMemoryLibraryLoader(result);
    }

    /**
     * Create a CQL DebugMap object based on the configuation in the operation input
     *
     * @param paramMap
     *            operation input
     * @return DebugMap
     */
    protected DebugMap getDebugMap(ParameterMap paramMap) {
        DebugMap debugMap = null;
        Parameter pDebug = paramMap.getOptionalSingletonParameter(PARAM_IN_DEBUG);
        if( pDebug != null && ((com.ibm.fhir.model.type.Boolean) pDebug.getValue()).getValue().equals(Boolean.TRUE) ) {
            debugMap = new DebugMap();
            debugMap.setIsLoggingEnabled(true);
            //debugMap.setIsCoverageEnabled(true);
        }
        return debugMap;
    }

    /**
     * Construct a FHIROperationExcepiton from the provided exception. This allows commonality
     * of error handling between related operations.
     *
     * @throws FHIROperationException
     */
    protected void throwOperationException(Exception ex) throws FHIROperationException {
        throw new FHIROperationException("Evaluation failed", ex)
            .withIssue(Issue.builder()
                .code(IssueType.EXCEPTION)
                .severity(IssueSeverity.FATAL)
                .details(CodeableConcept.builder()
                    .text(ex.getMessage())
                    .build())
                .build());
    }
}
