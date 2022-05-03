/*
 * (C) Copyright IBM Corp. 2017, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.apply;

import static com.ibm.fhir.model.type.String.string;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import com.ibm.fhir.core.FHIRConstants;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.ActivityDefinition;
import com.ibm.fhir.model.resource.CarePlan;
import com.ibm.fhir.model.resource.CarePlan.Activity.Detail;
import com.ibm.fhir.model.resource.OperationDefinition;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.resource.PlanDefinition;
import com.ibm.fhir.model.resource.PlanDefinition.Action;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Annotation;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Dosage;
import com.ibm.fhir.model.type.Dosage.DoseAndRate;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Quantity;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.SimpleQuantity;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.CarePlanActivityKind;
import com.ibm.fhir.model.type.code.CarePlanActivityStatus;
import com.ibm.fhir.model.type.code.CarePlanIntent;
import com.ibm.fhir.model.type.code.CarePlanStatus;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.registry.FHIRRegistry;
import com.ibm.fhir.search.util.SearchHelper;
import com.ibm.fhir.server.spi.operation.AbstractOperation;
import com.ibm.fhir.server.spi.operation.FHIROperationContext;
import com.ibm.fhir.server.spi.operation.FHIROperationUtil;
import com.ibm.fhir.server.spi.operation.FHIRResourceHelpers;

/**
 * <code>$apply</code> is an operation specific to PlanDefinition.
 *
 * @link OperationDefinition http://hl7.org/fhir/operation-plandefinition-apply.json
 * @link $apply http://hl7.org/fhir/plandefinition-operation-apply.html
 */
public class ApplyOperation extends AbstractOperation {

    private static final String PARAM_PLAN_DEFINITION = "planDefinition";
    private static final String PARAM_SUBJECT = "subject";
    private static final String PARAM_ENCOUNTER = "encounter";
    private static final String PARAM_PRACTITIONER = "practitioner";
    private static final String PARAM_ORGANIZATION = "organization";
    private static final String PARAM_USER_TYPE = "userType";
    private static final String PARAM_USER_LANGUAGE = "userLanguage";
    private static final String PARAM_USER_TASK_CONTEXT = "userTaskContext";
    private static final String PARAM_SETTING = "setting";
    private static final String PARAM_SETTING_CONTEXT = "settingContext";

    private static final String EXTENSION_BASE_URL = FHIRConstants.EXT_BASE + "apply/";

    @Override
    protected OperationDefinition buildOperationDefinition() {
        return FHIRRegistry.getInstance().getResource("http://hl7.org/fhir/OperationDefinition/PlanDefinition-apply",
                OperationDefinition.class);
    }

    @Override
    protected Parameters doInvoke(FHIROperationContext operationContext, Class<? extends Resource> resourceType,
            String logicalId, String versionId, Parameters parameters, FHIRResourceHelpers resourceHelper,
            SearchHelper searchHelper) throws FHIROperationException {
        try {
            checkAndValidateResourceType(resourceType);

            javax.ws.rs.core.UriInfo uriInfo =
                    (javax.ws.rs.core.UriInfo) operationContext.getProperty(FHIROperationContext.PROPNAME_URI_INFO);
            MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();

            // Get the minimally required parameters (path/query)
            List<String> subjects = checkSubjects(queryParameters);
            String planDefintionId = checkPlanDefintionLogicalId(logicalId, queryParameters);

            // Retrieve the PlanDefinition
            PlanDefinition resource =
                    checkAndRetrievePlanDefinition(resourceHelper, planDefintionId);

            // Get the String from the QueryParameters
            String encounter =
                    checkAndProcessEncounter(resourceHelper, queryParameters, parameters);
            String practitioner =
                    checkAndProcessPractitioner(resourceHelper, queryParameters, parameters);
            String organization =
                    checkAndProcessOrganization(resourceHelper, queryParameters, parameters);

            CodeableConcept userType =
                    checkAndProcessUserType(operationContext, queryParameters, parameters);
            CodeableConcept userLanguage =
                    checkAndProcessUserLanguage(operationContext, queryParameters, parameters);
            CodeableConcept userTaskContext =
                    checkAndProcessUserTaskContext(operationContext, queryParameters, parameters);
            CodeableConcept setting =
                    checkAndProcessSetting(operationContext, queryParameters, parameters);
            CodeableConcept settingContext =
                    checkAndProcessSettingContext(operationContext, queryParameters, parameters);

            CarePlan carePlan =
                    transform(resource, subjects, encounter, practitioner, organization, userType, userLanguage, userTaskContext, setting, settingContext);
            return FHIROperationUtil.getOutputParameters(carePlan);
        } catch (FHIROperationException e) {
            throw e;
        } catch (Exception e) {
            throw new FHIROperationException("An error occurred during the '$apply' operation", e);
        }
    }

    // Process the Parameters bodies for the contents
    private CodeableConcept checkAndProcessSettingContext(FHIROperationContext operationContext,
            MultivaluedMap<String, String> queryParameters, Parameters parameters)
            throws FHIROperationException {
        return internalCheckAndProcess(operationContext, queryParameters, parameters, PARAM_SETTING_CONTEXT);
    }

    private CodeableConcept checkAndProcessSetting(FHIROperationContext operationContext,
        MultivaluedMap<String, String> queryParameters, Parameters parameters) {
        return internalCheckAndProcess(operationContext, queryParameters, parameters, PARAM_SETTING);
    }

    private CodeableConcept checkAndProcessUserTaskContext(FHIROperationContext operationContext,
        MultivaluedMap<String, String> queryParameters, Parameters parameters) {
        return internalCheckAndProcess(operationContext, queryParameters, parameters, PARAM_USER_TASK_CONTEXT);
    }

    private CodeableConcept checkAndProcessUserLanguage(FHIROperationContext operationContext,
        MultivaluedMap<String, String> queryParameters, Parameters parameters) {
        return internalCheckAndProcess(operationContext, queryParameters, parameters, PARAM_USER_LANGUAGE);
    }

    private CodeableConcept checkAndProcessUserType(FHIROperationContext operationContext,
        MultivaluedMap<String, String> queryParameters, Parameters parameters) {
        return internalCheckAndProcess(operationContext, queryParameters, parameters, PARAM_USER_TYPE);
    }

    private CodeableConcept internalCheckAndProcess(FHIROperationContext operationContext,
        MultivaluedMap<String, String> queryParameters, Parameters parameters, String parameter) {
        for (Parameter p : parameters.getParameter()) {
            if (p.getName() != null && p.getName().getValue().compareTo(parameter) == 0) {
                return p.getValue().as(com.ibm.fhir.model.type.CodeableConcept.class);
            }
        }
        return null;
    }

    // Process Query Parameter Strings and/or Parameters bodies
    public String checkAndProcessOrganization(FHIRResourceHelpers resourceHelper,
        MultivaluedMap<String, String> queryParameters, Parameters parameters)
        throws FHIROperationException {
        return internalCheckAndProcess(resourceHelper, queryParameters, parameters, PARAM_ORGANIZATION);
    }

    public String checkAndProcessPractitioner(FHIRResourceHelpers resourceHelper,
        MultivaluedMap<String, String> queryParameters, Parameters parameters)
        throws FHIROperationException {
        return internalCheckAndProcess(resourceHelper, queryParameters, parameters, PARAM_PRACTITIONER);
    }

    public String checkAndProcessEncounter(FHIRResourceHelpers resourceHelper,
        MultivaluedMap<String, String> queryParameters, Parameters parameters)
        throws FHIROperationException {
        return internalCheckAndProcess(resourceHelper, queryParameters, parameters, PARAM_ENCOUNTER);
    }

    private String internalCheckAndProcess(FHIRResourceHelpers resourceHelper,
        MultivaluedMap<String, String> queryParameters, Parameters parameters, String parameter)
        throws FHIROperationException {
        String result = null;

        List<String> qps = queryParameters.get(parameter);
        if (qps != null) {
            if (qps.isEmpty() || qps.size() != 1) {
                throw buildOperationException("Cardinality expectation for $apply operation parameter is 0..1 ");
            }
            result = qps.get(0);
        }

        if (result == null) {
            for (Parameter p : parameters.getParameter()) {

                if (p.getName() != null && p.getName().getValue().compareTo(parameter) == 0) {
                    result = p.getValue().as(com.ibm.fhir.model.type.String.class).getValue();
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Transforms the CarePlan resource.
     * @param planDefinition
     * @param subjects
     * @param encounter
     * @param practitioner
     * @param organization
     * @param userType
     * @param userLanguage
     * @param userTaskContext
     * @param setting
     * @param settingContext
     * @return
     */
    private CarePlan transform(PlanDefinition planDefinition, List<String> subjects,
            String encounter, String practitioner, String organization, CodeableConcept userType,
            CodeableConcept userLanguage, CodeableConcept userTaskContext, CodeableConcept setting,
            CodeableConcept settingContext) {
        CarePlan.Builder builder = CarePlan.builder();

        if (planDefinition.getUrl() != null) {
            Uri url = planDefinition.getUrl();
            if (url.is(Canonical.class)) {
                builder.instantiatesCanonical(url.as(Canonical.class));
            } else {
                builder.instantiatesUri(url);
            }
        }

        // Subjects
        if (subjects != null) {
            Reference subjectRef = null;
            List<Reference> altSubjectsRefs = new ArrayList<>();

            if (planDefinition.getSubject() != null) {
                CodeableConcept code = planDefinition.getSubject().as(CodeableConcept.class);
                String codeType = code.getText().getValue();

                for (String subject : subjects) {
                    if (subject.contains(codeType)) {
                        subjectRef = Reference.builder().reference(string(subject)).build();
                    } else {
                        altSubjectsRefs.add(Reference.builder().reference(string(subject)).build());
                    }

                }
            }

            // Didn't find the right prefix... default to first.
            if (subjectRef == null) {
                subjectRef = Reference.builder().reference(string(subjects.get(0))).build();
            }

            builder.subject(subjectRef);

            // If there are more than 1, then 2..* is shoved into supporting info
            if (!altSubjectsRefs.isEmpty()) {
                builder.supportingInfo(altSubjectsRefs);
            }
        }

        // Encounter
        if (encounter != null) {
            builder.encounter(Reference.builder().reference(string(encounter)).build());
        }

        // Practitioner maps to a contributor
        if (practitioner != null) {
            builder.contributor(Reference.builder().reference(string(practitioner)).build());
        }

        // Organization
        if (organization != null) {
            builder.contributor(Reference.builder().reference(string(organization)).build());
        }

        // userTaskContext
        if (userTaskContext != null) {
            builder.description(userTaskContext.as(com.ibm.fhir.model.type.String.class));
        }

        // Setting
        if (setting != null) {
            builder.extension(Extension.builder().value(setting).url(EXTENSION_BASE_URL + "setting").build());
        }

        // SettingContext
        if (settingContext != null) {
            builder.extension(Extension.builder().value(settingContext).url(EXTENSION_BASE_URL + "settingContext").build());
        }

        // User Type
        if (userType != null) {
            builder.extension(Extension.builder().value(userType).url(EXTENSION_BASE_URL + "userType").build());
        }

        // User Language
        if (userLanguage != null) {
            com.ibm.fhir.model.type.String lang = userLanguage.getText();
            Code.Builder b = Code.builder();
            builder.language(b.value(lang.getValue()).build());
        }

        convertFromPlanDefinitionToCarePlan(planDefinition, builder);
        builder.status(CarePlanStatus.DRAFT);
        return builder.build();
    }

    /**
     * @param planDefinition
     * @param builder
     */
    private void convertFromPlanDefinitionToCarePlan(PlanDefinition planDefinition, CarePlan.Builder builder) {
        List<CarePlan.Activity> activities = new ArrayList<>();
        for (Action action : getActions(planDefinition.getAction())) {
            CarePlan.Activity.Builder cab = CarePlan.Activity.builder();

            ActivityDefinition activityDefinition = null;
            Element definition = action.getDefinition();

            if (definition != null) {
                activityDefinition = FHIRRegistry.getInstance().getResource(definition.as(Uri.class).getValue(), ActivityDefinition.class);
            }

            if (activityDefinition != null) {
                CarePlan.Activity.Detail.Builder cadb = CarePlan.Activity.Detail.builder();
                cadb.kind(CarePlanActivityKind.of(activityDefinition.getKind().getValue()));
                cadb.status(CarePlanActivityStatus.NOT_STARTED);
                cadb.code(activityDefinition.getCode());
                cadb.description(activityDefinition.getDescription());
                cadb.quantity(activityDefinition.getQuantity());
                cadb.scheduled(activityDefinition.getTiming());
                if (!activityDefinition.getDosage().isEmpty()) {
                    Dosage dosage = activityDefinition.getDosage().get(0);
                    if (!dosage.getDoseAndRate().isEmpty()) {
                        DoseAndRate doseAndRate = dosage.getDoseAndRate().get(0);
                        Element dose = doseAndRate.getDose();
                        if (dose.is(Quantity.class)) {
                            SimpleQuantity quantity = SimpleQuantity.builder()
                                    .value(dose.as(Quantity.class).getValue())
                                    .system(dose.as(Quantity.class).getSystem())
                                    .code(dose.as(Quantity.class).getCode())
                                    .unit(dose.as(Quantity.class).getUnit())
                                    .build();
                            cadb.dailyAmount(quantity);
                        }
                    }
                }
                if (definition.is(Canonical.class)) {
                    cadb.instantiatesCanonical(definition.as(Canonical.class));
                } else {
                    cadb.instantiatesUri(definition.as(Uri.class));
                }
                cab.detail(cadb.build());
            }

            List<Annotation> progress = Collections.emptyList();
            cab.progress(progress);

            List<CodeableConcept> outcomeCodeableConcept = Collections.emptyList();
            cab.outcomeCodeableConcept(outcomeCodeableConcept);

            List<Reference> outcomeReference = Collections.emptyList();
            cab.outcomeReference(outcomeReference);
            Detail.Builder detailBuilder = Detail.builder();
            detailBuilder.description(action.getDescription());
            detailBuilder.status(CarePlanActivityStatus.NOT_STARTED);
            cab.detail(detailBuilder.build());
            activities.add(cab.build());

        }
        builder.activity(activities);
        builder.intent(CarePlanIntent.PLAN);
    }

    private List<Action> getActions(List<Action> actions) {
        List<Action> result = new ArrayList<>();
        for (Action action : actions) {
            if (action.getDefinition() != null) {
                result.add(action);
            }
            result.addAll(getActions(action.getAction()));
        }
        return result;
    }

    /**
     * reads the plan definition and converts to PlanDefinition resource.
     * @param resourceHelper
     * @param planDefinitionId
     * @return
     */
    private PlanDefinition checkAndRetrievePlanDefinition(FHIRResourceHelpers resourceHelper,
            String planDefinitionId) throws Exception {
        Resource resource =
                resourceHelper.doRead("PlanDefinition", planDefinitionId).getResource();
        if (resource == null) {
            throw buildOperationExceptionNotFound("Could not find 'PlanDefinition' with id: ["
                    + planDefinitionId + "]");
        }
        return (PlanDefinition) resource;
    }

    /**
     * validates the resource is of the PlanDefinition type.
     * @param resourceType
     */
    private void checkAndValidateResourceType(Class<? extends Resource> resourceType)
            throws FHIROperationException {
        if ("PlanDefinition".compareTo(resourceType.getSimpleName()) != 0) {
            throw buildOperationException("$apply operation is available for PlanDefinition only");
        }
    }

    /**
     * checks the plan definition logical Id.
     * @param logicalId
     * @param queryParameters
     * @return
     * @throws FHIROperationException
     */
    private String checkPlanDefintionLogicalId(String logicalId,
            MultivaluedMap<String, String> queryParameters) throws FHIROperationException {
        String checkedLogicalId = logicalId;

        // Instance
        if (logicalId != null) {
            // Operation invoked at Instance level, then the parameter is not allowed.
            if (queryParameters.containsKey(PARAM_PLAN_DEFINITION)) {
                throw buildOperationException("Instance level execution of $apply with query parameter planDefinition");
            }
        }

        // Type
        if (logicalId == null) {
            List<String> planDefinitionIds = queryParameters.get(PARAM_PLAN_DEFINITION);
            if (planDefinitionIds != null && planDefinitionIds.size() == 1) {
                // Must be exactly one...
                checkedLogicalId = queryParameters.getFirst(PARAM_PLAN_DEFINITION);
            } else {
                throw buildOperationException("type level execution of $apply needs exactly one parameter 'planDefinition'");
            }
        }

        if (checkedLogicalId == null || checkedLogicalId.isEmpty()) {
            throw buildOperationException("Execution of $apply needs a valid 'planDefinition' parameter");
        }

        return checkedLogicalId;
    }

    /**
     * Checks each of the subjects to see if they are valid. If the subject is invalid, the code throws and exception.
     * @param queryParameters
     * @return
     * @throws FHIROperationException
     */
    private List<String> checkSubjects(MultivaluedMap<String, String> queryParameters)
            throws FHIROperationException {
        List<String> subjects = queryParameters.get(PARAM_SUBJECT);

        // Null and Empty Check
        if (subjects == null || subjects.isEmpty()) {
            throw buildOperationException("Could not find 'subject' ");
        }

        for (String subject : subjects) {
            if (subject == null || subjects.isEmpty()) {
                throw buildOperationException("Found an invalid 'subject' ");
            }
        }

        return subjects;
    }

    /*
     * builds a exception with issue to make it type 400.
     * @param errMsg
     * @return
     */
    private FHIROperationException buildOperationException(String errMsg) {
        FHIROperationException operationException =
                new FHIROperationException(errMsg);

        List<Issue> issues = new ArrayList<>();
        Issue.Builder builder = Issue.builder();
        builder.code(IssueType.INVALID);
        builder.diagnostics(string(errMsg));
        builder.severity(IssueSeverity.ERROR);
        issues.add(builder.build());

        operationException.setIssues(issues);
        return operationException;
    }

    /*
     * builds a exception with issue to make it type 400.
     * @param errMsg
     * @return
     */
    private FHIROperationException buildOperationExceptionNotFound(String errMsg) {
        FHIROperationException operationException =
                new FHIROperationException(errMsg);

        List<Issue> issues = new ArrayList<>();
        Issue.Builder builder = Issue.builder();
        builder.code(IssueType.NOT_FOUND);
        builder.diagnostics(string(errMsg));
        builder.severity(IssueSeverity.ERROR);
        issues.add(builder.build());

        operationException.setIssues(issues);
        return operationException;
    }

}
