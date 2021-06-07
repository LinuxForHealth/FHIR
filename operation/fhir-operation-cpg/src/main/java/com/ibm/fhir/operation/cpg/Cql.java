package com.ibm.fhir.operation.cpg;

import static com.ibm.fhir.cql.engine.model.ModelUtil.fhircode;
import static com.ibm.fhir.cql.engine.model.ModelUtil.fhirstring;
import static com.ibm.fhir.cql.engine.model.ModelUtil.javastring;

import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.Library;
import com.ibm.fhir.model.resource.OperationDefinition;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.type.Attachment;
import com.ibm.fhir.model.type.Base64Binary;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.registry.FHIRRegistry;
import com.ibm.fhir.server.operation.spi.FHIROperationContext;
import com.ibm.fhir.server.operation.spi.FHIRResourceHelpers;

public class Cql extends AbstractCqlOperation {

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

        Map<String, Parameter> paramMap = indexParametersByName(parameters);

        try {
            Library primaryLibrary = null;
            if (operationContext.getType().equals(FHIROperationContext.Type.SYSTEM)) {

                Parameter expression = getRequiredParameter(paramMap, "expression");
                Parameter includes = getParameter(paramMap, "includes");

                Pair<String, Object> context = getCqlContext(paramMap);

                primaryLibrary = createLibraryResource(context.getKey(), expression, includes);
            } else {
                throw new UnsupportedOperationException("This operation must be invoked in the system context");
            }

            result = doEvaluation(resourceHelper, paramMap, primaryLibrary);

        } catch (FHIROperationException fex) {
            throw fex;
        } catch (IllegalArgumentException iex) {
            throw new FHIROperationException(iex.getMessage(), iex).withIssue(Issue.builder().severity(IssueSeverity.ERROR).code(IssueType.INVALID).details(CodeableConcept.builder().text(fhirstring(iex.getMessage())).build()).build());
        } catch (Exception ex) {
            throw new FHIROperationException("Evaluation failed", ex);
        }

        return result;
    }

    protected Library createLibraryResource(String context, Parameter expression, Parameter includes) {
        String libraryName = "inline";
        String libraryVersion = "1.0.0";
        String fhirVersion = "4.0.1";

        Library.Builder builder = Library.builder().id(libraryName).name(fhirstring(libraryName)).version(fhirstring(libraryVersion));

        StringBuilder content = new StringBuilder();
        content.append(String.format("library \"%s\" version '%s'\n", libraryName, libraryVersion));
        content.append(String.format("using \"FHIR\" version '%s'\n", fhirVersion));
        content.append(String.format("include \"FHIRHelpers\" version '%s'"));

        if (includes != null) {
            Parameters includeParameters = (Parameters) includes.getResource();
            for (Parameter includeParameter : includeParameters.getParameter()) {
                String url = null;
                String name = null;
                String version = null;
                String alias = null;

                for (Parameter nestedParam : includeParameter.getPart()) {
                    String paramName = javastring(nestedParam.getName());
                    if (paramName.equals("url")) {
                        url = ((Canonical) nestedParam.getValue()).getValue();
                        Library related = FHIRRegistry.getInstance().getResource(url, Library.class);
                        if (related != null) {
                            name = javastring(related.getName());
                            version = javastring(related.getVersion());
                        } else {
                            throw new IllegalArgumentException(String.format("No Library resource matching %s found in registry", url));
                        }

                    } else if (paramName.equals("name")) {
                        alias = ((com.ibm.fhir.model.type.String) nestedParam.getValue()).getValue();
                    } else {
                        throw new IllegalArgumentException(String.format("Invalid parameter content for parameter %s", includeParameter.getName()));
                    }
                }
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
        content.append("\n");
        content.append("\t");
        content.append(((com.ibm.fhir.model.type.String) expression.getValue()).getValue());
        content.append("\n");

        builder.content(Attachment.builder().contentType(fhircode(Constants.MIME_TYPE_TEXT_CQL)).data(Base64Binary.of(content.toString())).build());

        return builder.build();
    }

    @Override
    protected Set<String> getCqlExpressionsToEvaluate(Map<String, Parameter> paramMap) {
        return Collections.singleton(DEFAULT_DEFINE_NAME);
    }
}
