package com.ibm.fhir.operation.cpg;

import static com.ibm.fhir.cql.helpers.ModelHelper.fhircode;
import static com.ibm.fhir.cql.helpers.ModelHelper.fhirstring;
import static com.ibm.fhir.cql.helpers.ModelHelper.javastring;
import static com.ibm.fhir.cql.helpers.ModelHelper.relatedArtifact;

import java.io.InputStream;
import java.util.Collections;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.tuple.Pair;

import com.ibm.fhir.cql.Constants;
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
    
    private static final Logger LOG = Logger.getLogger(CqlOperation.class.getName());

    private static final String DEFAULT_LIBRARY_NAME = "InlineLibrary";
    private static final String DEFUALT_LIBRARY_VERSION = "1.0.0";
    private static final String DEFAULT_FHIR_VERSION = "4.0.1";
    private static final String DEFAULT_DEFINE_NAME = "InlineExpression";

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

                Parameter expression = paramMap.getSingletonParameter("expression");
                Parameter includes = paramMap.getOptionalSingletonParameter("includes");

                Pair<String, Object> context = getCqlContext(paramMap);

                primaryLibrary = createLibraryResource(context.getKey(), expression, includes);
            } else {
                throw new UnsupportedOperationException("This operation must be invoked in the system context");
            }

            result = doEvaluation(resourceHelper, paramMap, primaryLibrary);
            
        } catch (IllegalArgumentException iex) {
            throw new FHIROperationException(iex.getMessage(), iex).withIssue(Issue.builder().severity(IssueSeverity.ERROR).code(IssueType.INVALID).details(CodeableConcept.builder().text(fhirstring(iex.getMessage())).build()).build());
        } catch (Exception ex) {
            throw new FHIROperationException("Evaluation failed", ex);
        }

        return result;
    }

    protected Library createLibraryResource(String context, Parameter expression, Parameter includes) {
        String libraryName = DEFAULT_LIBRARY_NAME;
        String libraryVersion = DEFUALT_LIBRARY_VERSION;
        String fhirVersion = DEFAULT_FHIR_VERSION;

        Library.Builder builder = Library.builder().id(libraryName).name(fhirstring(libraryName)).version(fhirstring(libraryVersion)).status(PublicationStatus.ACTIVE).type(LibraryHelper.getLogicLibraryConcept());

        StringBuilder content = new StringBuilder();
        content.append(String.format("library \"%s\" version '%s'\n", libraryName, libraryVersion));
        content.append(String.format("using \"FHIR\" version '%s'\n", fhirVersion));
        content.append(String.format("include \"FHIRHelpers\" version '%s'\n", fhirVersion));
        
        // The assumed Canonical url for FHIR Helpers is "http://hl7.org/fhir/Library/FHIRHelpers"
        // but that isn't guaranteed. Not sure if we should just link to it by magic or require that the
        // client provide it. The safest answer is to make the client do it for now.  
        

        if (includes != null) {
            Parameters includeParameters = (Parameters) includes.getResource();
            for (Parameter includeParameter : includeParameters.getParameter()) {
                String url = null;
                String name = null;
                String version = null;
                String alias = null;

                // each element should consist of a url and an optional name to use as the alias within the library
                for (Parameter nestedParam : includeParameter.getPart()) {
                    String paramName = javastring(nestedParam.getName());
                    if (paramName.equals("url")) {
                        url = ((Canonical) nestedParam.getValue()).getValue();
                        Library related = FHIRRegistry.getInstance().getResource(url, Library.class);
                        if (related != null) {
                            name = javastring(related.getName());
                            version = javastring(related.getVersion());

                            builder.relatedArtifact( relatedArtifact( RelatedArtifactType.DEPENDS_ON, related.getUrl(), related.getVersion() ) );
                        } else {
                            throw new IllegalArgumentException(String.format("No Library resource matching %s found in registry", url));
                        }

                    } else if (paramName.equals("name")) {
                        alias = ((com.ibm.fhir.model.type.String) nestedParam.getValue()).getValue();
                    } else {
                        throw new IllegalArgumentException(String.format("Invalid parameter content for parameter %s", includeParameter.getName()));
                    }
                }
                
                // if we resolved a library add it as an include 
                if (name != null) {
                    content.append(String.format("include \"%s\"", name));
                    if (version != null) {
                        content.append(" version '");
                        content.append(version);
                        content.append("'");
                    }
                    if (alias != null) {
                        content.append(" called ");
                        content.append(alias);
                    }
                    content.append("\n");
                } else {
                    throw new IllegalArgumentException(String.format("Missing required url value for parameter %s", includeParameter.getName()));
                }
            }
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

    @Override
    protected Set<String> getCqlExpressionsToEvaluate(ParameterMap paramMap) {
        return Collections.singleton(DEFAULT_DEFINE_NAME);
    }
}
