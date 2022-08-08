/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.operation.cpg;

import static org.linuxforhealth.fhir.cql.helpers.ModelHelper.fhircode;
import static org.linuxforhealth.fhir.cql.helpers.ModelHelper.fhirstring;
import static org.linuxforhealth.fhir.cql.helpers.ModelHelper.javastring;
import static org.linuxforhealth.fhir.cql.helpers.ModelHelper.relatedArtifact;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.tuple.Pair;

import org.linuxforhealth.fhir.cql.Constants;
import org.linuxforhealth.fhir.cql.helpers.CqlBuilder;
import org.linuxforhealth.fhir.cql.helpers.LibraryHelper;
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
import org.linuxforhealth.fhir.model.type.Attachment;
import org.linuxforhealth.fhir.model.type.Base64Binary;
import org.linuxforhealth.fhir.model.type.Canonical;
import org.linuxforhealth.fhir.model.type.CodeableConcept;
import org.linuxforhealth.fhir.model.type.code.IssueSeverity;
import org.linuxforhealth.fhir.model.type.code.IssueType;
import org.linuxforhealth.fhir.model.type.code.PublicationStatus;
import org.linuxforhealth.fhir.model.type.code.RelatedArtifactType;
import org.linuxforhealth.fhir.registry.FHIRRegistry;
import org.linuxforhealth.fhir.search.util.SearchHelper;
import org.linuxforhealth.fhir.server.spi.operation.FHIROperationContext;
import org.linuxforhealth.fhir.server.spi.operation.FHIRResourceHelpers;

public class CqlOperation extends AbstractCqlOperation {

    public static final String PARAM_IN_LIBRARY = "library";
    public static final String PARAM_IN_LIBRARY_URL = "url";
    public static final String PARAM_IN_LIBRARY_NAME = "name";

    public static final String DEFAULT_LIBRARY_NAME = "InlineLibrary";
    public static final String DEFAULT_LIBRARY_VERSION = "1.0.0";
    public static final String DEFAULT_FHIR_VERSION = "4.0.1";
    public static final String DEFAULT_DEFINE_NAME = "InlineExpression";

    private static final Logger LOG = Logger.getLogger(CqlOperation.class.getName());

    /**
     * Data container for grouped input parameters library.url, library.name. The
     * version is parsed from the library.url if present. Alias is part of the
     * FHIR RelatedArtifact DataType, so it is included as an attribute, but is
     * not currently used.
     */
    public static class IncludeLibraryDetail {
        public IncludeLibraryDetail() {
            // No Operation
        }

        public IncludeLibraryDetail(String url, String name, String version, String alias) {
            this.url = url;
            this.name = name;
            this.version = version;
            this.alias = alias;
        }

        String url = null;
        String name = null;
        String version = null;
        String alias = null;

        public boolean isValid() {
            return (name != null);
        }
    }

    @Override
    protected OperationDefinition buildOperationDefinition() {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("OperationDefinition-cpg-cql.json")) {
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
            if (operationContext.getType().equals(FHIROperationContext.Type.SYSTEM)) {

                Parameter expression = paramMap.getSingletonParameter(PARAM_IN_EXPRESSION);
                List<Parameter> includes = paramMap.getParameter(PARAM_IN_LIBRARY);

                Pair<String, Object> context = getCqlContext(paramMap);

                primaryLibrary = createLibraryResource(context.getKey(), expression, includes);
            } else {
                throw new UnsupportedOperationException("This operation must be invoked in the system context");
            }

            result = doEvaluation(resourceHelper, paramMap, searchHelper, primaryLibrary);

        } catch (IllegalArgumentException | CqlTranslationException iex) {
            throw new FHIROperationException(iex.getMessage(), iex)
                .withIssue(Issue.builder()
                    .severity(IssueSeverity.ERROR)
                    .code(IssueType.INVALID)
                    .details(CodeableConcept.builder()
                        .text(fhirstring(iex.getMessage()))
                        .build())
                    .build());
        } catch (Exception ex) {
            throwOperationException(ex);
        }

        return result;
    }

    /**
     * Create a Library resource matching the provided input parameters.
     *
     * @param context
     *            CQL execution context (e.g. Patient)
     * @param expression
     *            FHIR Parameter resource containg the CQL code to evaluate
     * @param includes
     *            List of FHIR parameter resources describing any helper libraries
     *            that need to be included in order to evaluate the provided expression.
     * @return generated Library resource
     */
    protected Library createLibraryResource(String context, Parameter expression, List<Parameter> includes) {
        String libraryName = DEFAULT_LIBRARY_NAME;
        String libraryVersion = DEFAULT_LIBRARY_VERSION;
        String fhirVersion = DEFAULT_FHIR_VERSION;

        Library.Builder builder = Library.builder()
                .id(libraryName)
                .name(fhirstring(libraryName))
                .version(fhirstring(libraryVersion))
                .status(PublicationStatus.ACTIVE)
                .type(LibraryHelper.getLogicLibraryConcept());

        CqlBuilder content = CqlBuilder.builder();
        content.library(libraryName, libraryVersion);
        content.using("FHIR", fhirVersion);

        boolean userSpecifiedFhirHelpers = false;
        if (includes != null) {
            for( Parameter includeParameter : includes ) {
                IncludeLibraryDetail includeDetail = getIncludeDetail(includeParameter);

                // if we resolved a library add it as an include
                if ( includeDetail.isValid() ) {
                    builder.relatedArtifact( relatedArtifact( RelatedArtifactType.DEPENDS_ON, includeDetail.url ) );

                    content.includes(includeDetail.name, includeDetail.version, includeDetail.alias);

                    if( includeDetail.name.equals("FHIRHelpers") ) {
                        userSpecifiedFhirHelpers = true;
                    }
                } else {
                    throw new IllegalArgumentException(String.format("The library parameter must contain at minimum a url part"));
                }
            }
        }

        if( !userSpecifiedFhirHelpers ) {
            content.include("FHIRHelpers", fhirVersion);
            //content.append(String.format("include \"FHIRHelpers\" version '%s'\n", fhirVersion));
            // Intentionally no related artifact here. It will get picked up automatically by the FhirLibrarySourceProvider
            // in the translator
        }

        if (context != null) {
            content.context(context);
        }

        content.expression(DEFAULT_DEFINE_NAME, ((org.linuxforhealth.fhir.model.type.String) expression.getValue()).getValue());

        String contentCql = content.build();
        System.out.println(contentCql);
        if( LOG.isLoggable(Level.FINE) ) {
            LOG.fine("AdHoc CQL = " + contentCql);
        }

        builder.content(Attachment.builder().contentType(fhircode(Constants.MIME_TYPE_TEXT_CQL)).data(Base64Binary.of(contentCql.getBytes())).build());

        return builder.build();
    }

    /**
     * Convert FHIR Parameter resource into corresponding IncludeLibraryDetail DTO
     *
     * @param includeParameter FHIR Parameter resource
     * @return IncludeLibraryDetail
     */
    protected IncludeLibraryDetail getIncludeDetail(Parameter includeParameter) {
        IncludeLibraryDetail includeDetail = new IncludeLibraryDetail();

        // each element should consist of a url and an optional name to use as the alias within the library
        for (Parameter part : includeParameter.getPart()) {
            String paramName = javastring(part.getName());
            if (paramName.equals(PARAM_IN_LIBRARY_URL)) {
                includeDetail.url = ((Canonical) part.getValue()).getValue();
                Library related = FHIRRegistry.getInstance().getResource(includeDetail.url, Library.class);
                if (related != null) {
                    includeDetail.name = javastring(related.getName());
                    includeDetail.version = javastring(related.getVersion());
                } else {
                    throw new IllegalArgumentException(String.format("No Library resource matching %s found in registry", includeDetail.url));
                }

            } else if (paramName.equals(PARAM_IN_LIBRARY_NAME)) {
                includeDetail.alias = ((org.linuxforhealth.fhir.model.type.String) part.getValue()).getValue();
            } else {
                throw new IllegalArgumentException(String.format("Invalid parameter part %s", paramName));
            }
        }
        return includeDetail;
    }

    @Override
    protected Set<String> getCqlExpressionsToEvaluate(ParameterMap paramMap) {
        return Collections.singleton(DEFAULT_DEFINE_NAME);
    }
}
