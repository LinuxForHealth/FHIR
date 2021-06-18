package com.ibm.fhir.operation.cpg;

import static com.ibm.fhir.cql.helpers.ModelHelper.fhircode;
import static com.ibm.fhir.cql.helpers.ModelHelper.fhirstring;
import static com.ibm.fhir.cql.helpers.ModelHelper.javastring;
import static com.ibm.fhir.cql.helpers.ModelHelper.relatedArtifact;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.tuple.Pair;

import com.ibm.fhir.cql.Constants;
import com.ibm.fhir.cql.helpers.LibraryHelper;
import com.ibm.fhir.cql.helpers.ParameterMap;
import com.ibm.fhir.cql.translator.CqlTranslationException;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.Library;
import com.ibm.fhir.model.resource.OperationDefinition;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Attachment;
import com.ibm.fhir.model.type.Base64Binary;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.type.code.PublicationStatus;
import com.ibm.fhir.model.type.code.RelatedArtifactType;
import com.ibm.fhir.registry.FHIRRegistry;
import com.ibm.fhir.server.operation.spi.FHIROperationContext;
import com.ibm.fhir.server.operation.spi.FHIRResourceHelpers;

public class CqlOperation extends AbstractCqlOperation {

    public static final String PARAM_IN_EXPRESSION = "expression";
    public static final String PARAM_IN_LIBRARY = "library";
    public static final String PARAM_IN_LIBRARY_URL = "url";
    public static final String PARAM_IN_LIBRARY_NAME = "name";

    public static final String DEFAULT_LIBRARY_NAME = "InlineLibrary";
    public static final String DEFAULT_LIBRARY_VERSION = "1.0.0";
    public static final String DEFAULT_FHIR_VERSION = "4.0.1";
    public static final String DEFAULT_DEFINE_NAME = "InlineExpression";

    private static final Logger LOG = Logger.getLogger(CqlOperation.class.getName());

    public static class IncludeLibraryDetail {
        public IncludeLibraryDetail() {
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
        String logicalId, String versionId, Parameters parameters, FHIRResourceHelpers resourceHelper)
        throws FHIROperationException {

        Parameters result = null;

        ParameterMap paramMap = new ParameterMap(parameters);

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

            result = doEvaluation(resourceHelper, paramMap, primaryLibrary);
            
        } catch (IllegalArgumentException | CqlTranslationException iex) {
            throw new FHIROperationException(iex.getMessage(), iex).withIssue(Issue.builder().severity(IssueSeverity.ERROR).code(IssueType.INVALID).details(CodeableConcept.builder().text(fhirstring(iex.getMessage())).build()).build());
        } catch (Exception ex) {
            throw new FHIROperationException("Evaluation failed", ex);
        }

        return result;
    }

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

        StringBuilder content = new StringBuilder();
        content.append(String.format("library \"%s\" version '%s'\n", libraryName, libraryVersion));
        content.append(String.format("using \"FHIR\" version '%s'\n", fhirVersion));

        boolean userSpecifiedFhirHelpers = false;
        if (includes != null) {
            for( Parameter includeParameter : includes ) {
                IncludeLibraryDetail includeDetail = getIncludeDetail(includeParameter);
                
                // if we resolved a library add it as an include 
                if ( includeDetail.isValid() ) {
                    builder.relatedArtifact( relatedArtifact( RelatedArtifactType.DEPENDS_ON, includeDetail.url ) );
                    
                    content.append(String.format("include \"%s\"", includeDetail.name));
                    if (includeDetail.version != null) {
                        content.append(" version '");
                        content.append(includeDetail.version);
                        content.append("'");
                    }
                    if (includeDetail.alias != null) {
                        content.append(" called ");
                        content.append(includeDetail.alias);
                    }
                    content.append("\n");
                    
                    if( includeDetail.name.equals("FHIRHelpers") ) {
                        userSpecifiedFhirHelpers = true;
                    }
                } else {
                    throw new IllegalArgumentException(String.format("The library parameter must contain at minimum a url part"));
                }
            }
        }
        
        if( ! userSpecifiedFhirHelpers ) { 
            content.append(String.format("include \"FHIRHelpers\" version '%s'\n", fhirVersion));
            // Intentionally no related artifact here. It will get picked up automatically by the FhirLibrarySourceProvider
            // in the translator
        }

        if (context != null) {
            content.append("context ");
            content.append(context);
            content.append("\n");
        }

        content.append("define ");
        content.append(DEFAULT_DEFINE_NAME);
        content.append(":\n");
        content.append("\t");
        content.append(((com.ibm.fhir.model.type.String) expression.getValue()).getValue());
        content.append("\n");
        
        System.out.println(content.toString());
        if( LOG.isLoggable(Level.FINE) ) {
            LOG.fine("AdHoc CQL = " + content.toString());
        }

        builder.content(Attachment.builder().contentType(fhircode(Constants.MIME_TYPE_TEXT_CQL)).data(Base64Binary.of(content.toString().getBytes())).build());

        return builder.build();
    }

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
                includeDetail.alias = ((com.ibm.fhir.model.type.String) part.getValue()).getValue();
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
