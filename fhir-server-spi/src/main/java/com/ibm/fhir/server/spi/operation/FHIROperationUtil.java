/*
 * (C) Copyright IBM Corp. 2016, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.spi.operation;

import static com.ibm.fhir.model.type.String.string;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.OperationDefinition;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Oid;
import com.ibm.fhir.model.type.PositiveInt;
import com.ibm.fhir.model.type.Time;
import com.ibm.fhir.model.type.UnsignedInt;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.Url;
import com.ibm.fhir.model.type.Uuid;
import com.ibm.fhir.model.type.code.FHIRAllTypes;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.type.code.OperationParameterUse;
import com.ibm.fhir.model.util.FHIRUtil;

public final class FHIROperationUtil {
    public static final String ENV_DISABLED_OPERATIONS = "DISABLED_OPERATIONS";

    private static Set<String> DISABLED_OPERATIONS = new HashSet<>();

    private FHIROperationUtil() {
        // No Operation
    }

    /**
     * Initializes the FHIR Operation Utility so disallowedOperations are loaded one time.
     * First, the code checks the environment operations.
     * Second, the code checks the configuration.
     * This is initialized one time for the system.
     */
    public static void init() {
        String operationStr = System.getenv(ENV_DISABLED_OPERATIONS);
        if (operationStr != null) {
            DISABLED_OPERATIONS.addAll(Arrays.asList(operationStr.split(",")));
        }

        // Check the Configuration and also add to the Set
        operationStr = FHIRConfigHelper.getStringProperty(FHIRConfiguration.PROPERTY_DISABLED_OPERATIONS, null);
        if (operationStr != null) {
            DISABLED_OPERATIONS.addAll(Arrays.asList(operationStr.split(",")));
        }
    }

    /**
     * Construct a Parameters object from the input parameters passed via query parameters
     *
     * @param definition not null
     * @param queryParameters not null
     * @return
     * @throws FHIROperationException
     */
    public static Parameters getInputParameters(OperationDefinition definition,
            Map<String, List<String>> queryParameters) throws FHIROperationException {
        try {
            Parameters.Builder parametersBuilder = Parameters.builder();
            parametersBuilder.id("InputParameters");

            for (OperationDefinition.Parameter parameter : definition.getParameter()) {
                if (OperationParameterUse.IN.getValue().equals(parameter.getUse().getValue())) {
                    String paramName = parameter.getName().getValue();
                    if (!queryParameters.containsKey(paramName)) {
                        continue;
                    }

                    FHIRAllTypes type = parameter.getType();
                    if (type == null) {
                        continue;
                    }
                    String typeName = type.getValue();

                    for (String value : queryParameters.get(paramName)) {
                        parametersBuilder.parameter(parseParameter(typeName, paramName, value));
                    }
                }
            }
            return parametersBuilder.build();
        } catch (FHIROperationException e) {
            throw e;
        } catch (Exception e) {
            throw new FHIROperationException("Unable to process query parameters", e);
        }
    }

    private static Parameter parseParameter(String typeName, String paramName, String value) throws FHIROperationException {

        Parameter.Builder parameterBuilder = Parameter.builder().name(paramName);
        try {
            if ("boolean".equals(typeName)) {
                parameterBuilder.value(com.ibm.fhir.model.type.Boolean.of(value));
            } else if ("integer".equals(typeName)) {
                parameterBuilder.value(com.ibm.fhir.model.type.Integer.of(value));
            } else if ("string".equals(typeName)) {
                parameterBuilder.value(com.ibm.fhir.model.type.String.of(value));
            } else if ("decimal".equals(typeName)) {
                parameterBuilder.value(com.ibm.fhir.model.type.Decimal.of(value));
            } else if ("uri".equals(typeName)) {
                parameterBuilder.value(Uri.of(value));
            } else if ("url".equals(typeName)) {
                parameterBuilder.value(Url.of(value));
            } else if ("canonical".equals(typeName)) {
                parameterBuilder.value(Canonical.of(value));
            } else if ("instant".equals(typeName)) {
                parameterBuilder.value(Instant.of(value));
            } else if ("date".equals(typeName)) {
                parameterBuilder.value(Date.of(value));
            } else if ("dateTime".equals(typeName)) {
                parameterBuilder.value(DateTime.of(value));
            } else if ("time".equals(typeName)) {
                parameterBuilder.value(Time.of(value));
            } else if ("code".equals(typeName)) {
                parameterBuilder.value(Code.of(value));
            } else if ("oid".equals(typeName)) {
                parameterBuilder.value(Oid.of(value));
            } else if ("id".equals(typeName)) {
                parameterBuilder.value(Id.of(value));
            } else if ("unsignedInt".equals(typeName)) {
                parameterBuilder.value(UnsignedInt.of(value));
            } else if ("positiveInt".equals(typeName)) {
                parameterBuilder.value(PositiveInt.of(value));
            } else if ("uuid".equals(typeName)) {
                parameterBuilder.value(Uuid.of(value));
            } else {
                String msg = "Query parameter '" + paramName + "' is an invalid type: '" + typeName + "'";
                FHIROperationException operationException =
                        new FHIROperationException(msg);

                operationException.withIssue(Issue.builder()
                        .code(IssueType.INVALID)
                        .details(CodeableConcept.builder()
                                .text(msg)
                                .build())
                        .severity(IssueSeverity.ERROR)
                        .build());

                throw operationException;
            }
        } catch (NullPointerException | IllegalStateException e) {
            String msg = "Unable to parse query parameter '" + paramName + "' to its expected type: '" + typeName + "'";
            throw new FHIROperationException(msg, e)
                    .withIssue(Issue.builder()
                        .severity(IssueSeverity.ERROR)
                        .code(IssueType.INVALID)
                        .details(CodeableConcept.builder()
                                .text("Unable to process query parameters")
                                .build())
                        .build());
        }

        return parameterBuilder.build();
    }


    /**
     * Construct a Parameters object with a single parameter named resource
     *
     * @param definition
     * @param resource
     * @return
     * @throws Exception
     */
    public static Parameters getInputParameters(OperationDefinition definition, Resource resource) throws Exception {
        Parameters.Builder parametersBuilder = Parameters.builder();
        parametersBuilder.id("InputParameters");
        for (OperationDefinition.Parameter parameterDefinition : definition.getParameter()) {
            if (parameterDefinition.getType() == null) {
                continue;
            }
            String parameterTypeName = parameterDefinition.getType().getValue();
            String resourceTypeName = resource.getClass().getSimpleName();
            if ((resourceTypeName.equals(parameterTypeName) || "Resource".equals(parameterTypeName))
                    && OperationParameterUse.IN.getValue().equals(parameterDefinition.getUse().getValue())) {
                Parameter.Builder parameterBuilder =
                        Parameter.builder().name(string(parameterDefinition.getName().getValue()));
                parametersBuilder.parameter(parameterBuilder.resource(resource).build());
            }
        }
        return parametersBuilder.build();
    }

    /**
     * Generates an output Parameter resource
     * @param resource
     * @return
     */
    public static Parameters getOutputParameters(Resource resource) {
        return getOutputParameters("return", resource);
    }


    /**
     * generates an output parameter with a specific name.
     *
     * @param name
     * @param resource
     * @return
     */
    public static Parameters getOutputParameters(String name, Resource resource) {
        return Parameters.builder()
                .parameter(Parameter.builder()
                    .name(string(name))
                    .resource(resource)
                    .build())
                .build();
    }

    /**
     * Generates an output parameters, with a parameter for a specified element.
     * @param name the parameter name
     * @param element the element, or null
     * @return output parameters
     */
    public static Parameters getOutputParameters(String name, Element element) {
        Parameters.Builder builder = Parameters.builder();
        if (element != null) {
            builder.parameter(Parameter.builder()
                .name(string(name))
                .value(element)
                .build());
        }
        return builder.build();
    }

    public static boolean hasSingleResourceOutputParameter(Parameters parameters) {
        if (parameters == null) {
            return false;
        }
        return parameters.getParameter().size() == 1 && parameters.getParameter().get(0).getResource() != null;
    }

    public static Resource getSingleResourceOutputParameter(Parameters parameters) throws Exception {
        return parameters.getParameter().get(0).getResource();
    }

    /**
     * check and verify operation allowed
     * @param operationName
     * @throws FHIROperationException
     */
    public static void checkAndVerifyOperationAllowed(String operationName) throws FHIROperationException {
        if (DISABLED_OPERATIONS.contains(operationName)) {
            throw generateForbiddenOperationException(operationName);
        }
    }

    public static FHIROperationException generateForbiddenOperationException(String operationName) {
        FHIROperationException operationException =
                new FHIROperationException("Access to the operation is forbidden");

        List<Issue> issues = new ArrayList<>();
        Issue.Builder builder = Issue.builder();
        builder.code(IssueType.FORBIDDEN);
        builder.diagnostics(string("Access to the operation is forbidden - '$" + operationName +"'"));
        builder.severity(IssueSeverity.ERROR);
        issues.add(builder.build());

        operationException.setIssues(issues);
        return operationException;
    }

    /**
     * Helper method to generate a FHIROperationException with a fixed IssueType
     * @param msg the message to be packed in the exception
     * @param issueType the type of the issue
     * @return
     */
    public static FHIROperationException buildExceptionWithIssue(final String msg, IssueType issueType) {
        return buildExceptionWithIssue(msg, issueType, null);
    }

    /**
     * Helper method to generate a FHIROperationException with a fixed IssueType
     * @param msg the message to be packed in the exception
     * @param issueType the type of the issue
     * @param cause the throwable that causes this OperationOutcome/Exception
     * @return
     */
    public static FHIROperationException buildExceptionWithIssue(final String msg, IssueType issueType, final Throwable cause) {
        OperationOutcome.Issue ooi = FHIRUtil.buildOperationOutcomeIssue(msg, issueType);
        return new FHIROperationException(msg, cause).withIssue(ooi);
    }
}