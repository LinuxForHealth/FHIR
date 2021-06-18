package com.ibm.fhir.ecqm.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;

import org.opencds.cqf.cql.engine.data.DataProvider;
import org.opencds.cqf.cql.engine.execution.Context;
import org.opencds.cqf.cql.engine.runtime.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unchecked")
public abstract class MeasureEvaluation<BaseT, MeasureT extends BaseT,  MeasureGroupComponentT extends BaseT, MeasureGroupPopulationComponentT extends BaseT, MeasureReportT extends BaseT, MeasureReportGroupComponentT extends BaseT, MeasureReportGroupPopulationComponentT extends BaseT, ResourceT, SubjectT extends ResourceT> {

    private static final Logger logger = LoggerFactory.getLogger(MeasureEvaluation.class);

    protected MeasureT measure;
    protected Context context;
    protected String subjectOrPractitionerId;
    protected Interval measurementPeriod;

    // TODO: Figure this out dynamically based on the ResourceType
    protected String packageName;

    protected Function<ResourceT, String> getId;

    protected abstract MeasureScoring getMeasureScoring();

    protected abstract String getCriteriaExpression(MeasureGroupPopulationComponentT mgpc);

    protected abstract void setGroupScore(MeasureReportGroupComponentT mrgc, Double score);

    protected abstract MeasurePopulationType getPopulationType(MeasureGroupPopulationComponentT mgpc);

    protected abstract Iterable<MeasureGroupComponentT> getGroup();

    protected abstract Iterable<MeasureGroupPopulationComponentT> getPopulation(MeasureGroupComponentT mgc);

    protected abstract void addPopulationReport(MeasureReportT report, MeasureReportGroupComponentT reportGroup,
            MeasureGroupPopulationComponentT populationCriteria, int populationCount,
            Iterable<SubjectT> subjectPopulation);

    protected abstract MeasureReportT createMeasureReport(String status, MeasureReportType type,
            Interval measurementPeriod, List<SubjectT> subjects);

    protected abstract MeasureReportGroupComponentT createReportGroup(String id);

    protected abstract String getGroupId(MeasureGroupComponentT group);

    protected abstract void addReportGroup(MeasureReportT report, MeasureReportGroupComponentT group);

    public MeasureEvaluation(Context context, MeasureT measure, Interval measurementPeriod, String packageName,
            Function<ResourceT, String> getId) {
        this(context, measure, measurementPeriod, packageName, getId, null);
    }

    public MeasureEvaluation(Context context, MeasureT measure, Interval measurementPeriod, String packageName,
            Function<ResourceT, String> getId, String patientOrPractitionerId) {
        this.measure = measure;
        this.context = context;
        this.subjectOrPractitionerId = patientOrPractitionerId;
        this.measurementPeriod = measurementPeriod;
        this.getId = getId;
        this.packageName = packageName;        
    }

    public MeasureReportT evaluate(MeasureReportType type) {
        switch (type) {
            case INDIVIDUAL:
                return this.evaluatePatientMeasure();
            case SUBJECTLIST:
                return this.evaluateSubjectListMeasure();
            case PATIENTLIST:
                return this.evaluatePatientListMeasure();
            default:
                return this.evaluatePatientListMeasure();
        }
    }

    protected MeasureReportT evaluatePatientMeasure() {
        logger.info("Generating individual report");

        if (this.subjectOrPractitionerId == null) {
            return evaluatePopulationMeasure();
        }
        
        String id = this.subjectOrPractitionerId;
        if (id.startsWith("Patient/") ) {
            id = id.substring("Patient/".length());
        }

        Iterable<Object> subjectRetrieve = this.getDataProvider().retrieve("Patient", "id",
                id, "Patient", null, null, null, null, null, null, null, null);
        SubjectT patient = null;
        if (subjectRetrieve.iterator().hasNext()) {
            patient = (SubjectT) subjectRetrieve.iterator().next();
        }

        return evaluate(patient == null ? Collections.emptyList() : Collections.singletonList(patient),
                MeasureReportType.INDIVIDUAL);
    }

    protected MeasureReportT evaluateSubjectListMeasure() {
        logger.info("Generating subject-list report");
        List<SubjectT> subjects = this.subjectOrPractitionerId == null ? getAllSubjects()
                : getPractitionerSubjects(this.subjectOrPractitionerId);
        return evaluate(subjects, MeasureReportType.SUBJECTLIST);
    }

    protected MeasureReportT evaluatePatientListMeasure() {
        logger.info("Generating patient-list report");
        List<SubjectT> subjects = this.subjectOrPractitionerId == null ? getAllSubjects()
                : getPractitionerSubjects(this.subjectOrPractitionerId);
        return evaluate(subjects, MeasureReportType.PATIENTLIST);
    }

    private List<SubjectT> getPractitionerSubjects(String practitionerRef) {
        List<SubjectT> subjects = new ArrayList<>();
        Iterable<Object> subjectRetrieve = this.getDataProvider().retrieve("Practitioner", "generalPractitioner",
                practitionerRef, "Patient", null, null, null, null, null, null, null, null);
        subjectRetrieve.forEach(x -> subjects.add((SubjectT) x));
        return subjects;
    }

    private DataProvider getDataProvider() {
        return this.context.resolveDataProvider(this.packageName);
    }

    private List<SubjectT> getAllSubjects() {
        List<SubjectT> patients = new ArrayList<>();
        Iterable<Object> patientRetrieve = this.getDataProvider().retrieve(null, null, null, "Patient", null, null,
                null, null, null, null, null, null);
        patientRetrieve.forEach(x -> patients.add((SubjectT) x));
        return patients;
    }

    public MeasureReportT evaluatePopulationMeasure() {
        logger.info("Generating summary report");

        return evaluate(getAllSubjects(), MeasureReportType.SUMMARY);
    }

    private Iterable<ResourceT> evaluateCriteria(SubjectT subject, MeasureGroupPopulationComponentT pop) {
        String criteriaExpression = this.getCriteriaExpression(pop);
        if (criteriaExpression == null || criteriaExpression.isEmpty()) {
            return Collections.emptyList();
        }

        context.setContextValue("Patient", this.getId.apply(subject));
        Object result = context.resolveExpressionRef(criteriaExpression).evaluate(context);
        if (result == null) {
            Collections.emptyList();
        }

        if (result instanceof Boolean) {
            if (((Boolean) result)) {
                return Collections.singletonList(subject);
            } else {
                return Collections.emptyList();
            }
        }

        return (Iterable<ResourceT>)result;
    }

    private boolean evaluatePopulationCriteria(SubjectT subject, MeasureGroupPopulationComponentT criteria,
            HashMap<String, ResourceT> population, HashMap<String, SubjectT> populationSubjects,
            MeasureGroupPopulationComponentT exclusionCriteria, HashMap<String, ResourceT> exclusionPopulation,
            HashMap<String, SubjectT> exclusionSubjects) {
        boolean inPopulation = false;
        if (criteria != null) {
            for (ResourceT resource : evaluateCriteria(subject, criteria)) {
                inPopulation = true;
                population.put(this.getId.apply(resource), resource);
            }
        }

        if (inPopulation) {
            // Are they in the exclusion?
            if (exclusionCriteria != null) {
                for (ResourceT resource : evaluateCriteria(subject, exclusionCriteria)) {
                    inPopulation = false;
                    exclusionPopulation.put(this.getId.apply(resource), resource);
                    population.remove(this.getId.apply(resource));
                }
            }
        }

        if (inPopulation && populationSubjects != null) {
            populationSubjects.put(this.getId.apply(subject), subject);
        }
        if (!inPopulation && exclusionSubjects != null) {
            exclusionSubjects.put(this.getId.apply(subject), subject);
        }

        return inPopulation;
    }

    private void addPopulationCriteriaReport(MeasureReportT report, MeasureReportGroupComponentT reportGroup,
            MeasureGroupPopulationComponentT populationCriteria, int populationCount,
            Iterable<SubjectT> patientPopulation) {
        if (populationCriteria != null) {
            this.addPopulationReport(report, reportGroup, populationCriteria, populationCount, patientPopulation);
        }
    }

    private MeasureReportT evaluate(List<SubjectT> patients, MeasureReportType type) {
        MeasureReportT report = this.createMeasureReport("complete", type, this.measurementPeriod, patients);
        HashMap<String, ResourceT> resources = new HashMap<>();
        HashMap<String, HashSet<String>> codeToResourceMap = new HashMap<>();

        MeasureScoring measureScoring = this.getMeasureScoring();
        if (measureScoring == null) {
            throw new RuntimeException("MeasureType scoring is required in order to calculate.");
        }

        for (MeasureGroupComponentT group : this.getGroup()) {
            MeasureReportGroupComponentT reportGroup = this.createReportGroup(this.getGroupId(group));
            this.addReportGroup(report, reportGroup);

            // Declare variables to avoid a hash lookup on every patient
            // TODO: Isn't quite right, there may be multiple initial populations for a
            // ratio MeasureType...
            MeasureGroupPopulationComponentT initialPopulationCriteria = null;
            MeasureGroupPopulationComponentT numeratorCriteria = null;
            MeasureGroupPopulationComponentT numeratorExclusionCriteria = null;
            MeasureGroupPopulationComponentT denominatorCriteria = null;
            MeasureGroupPopulationComponentT denominatorExclusionCriteria = null;
            MeasureGroupPopulationComponentT denominatorExceptionCriteria = null;
            MeasureGroupPopulationComponentT measurePopulationCriteria = null;
            MeasureGroupPopulationComponentT measurePopulationExclusionCriteria = null;
            // TODO: Isn't quite right, there may be multiple MeasureType observations...
            MeasureGroupPopulationComponentT measureObservationCriteria = null;

            HashMap<String, ResourceT> initialPopulation = null;
            HashMap<String, ResourceT> numerator = null;
            HashMap<String, ResourceT> numeratorExclusion = null;
            HashMap<String, ResourceT> denominator = null;
            HashMap<String, ResourceT> denominatorExclusion = null;
            HashMap<String, ResourceT> denominatorException = null;
            HashMap<String, ResourceT> measurePopulation = null;
            HashMap<String, ResourceT> measurePopulationExclusion = null;
            HashMap<String, ResourceT> measureObservation = null;

            HashMap<String, SubjectT> initialPopulationPatients = null;
            HashMap<String, SubjectT> numeratorPatients = null;
            HashMap<String, SubjectT> numeratorExclusionPatients = null;
            HashMap<String, SubjectT> denominatorPatients = null;
            HashMap<String, SubjectT> denominatorExclusionPatients = null;
            HashMap<String, SubjectT> denominatorExceptionPatients = null;
            HashMap<String, SubjectT> measurePopulationPatients = null;
            HashMap<String, SubjectT> measurePopulationExclusionPatients = null;

            for (MeasureGroupPopulationComponentT pop : this.getPopulation(group)) {
                MeasurePopulationType populationType = this.getPopulationType(pop);
                if (populationType != null) {
                    switch (populationType) {
                        case INITIALPOPULATION:
                            initialPopulationCriteria = pop;
                            initialPopulation = new HashMap<>();
                            if (type == MeasureReportType.SUBJECTLIST || type == MeasureReportType.PATIENTLIST) {
                                initialPopulationPatients = new HashMap<>();
                            }
                            break;
                        case NUMERATOR:
                            numeratorCriteria = pop;
                            numerator = new HashMap<>();
                            if (type == MeasureReportType.SUBJECTLIST || type == MeasureReportType.PATIENTLIST) {
                                numeratorPatients = new HashMap<>();
                            }
                            break;
                        case NUMERATOREXCLUSION:
                            numeratorExclusionCriteria = pop;
                            numeratorExclusion = new HashMap<>();
                            if (type == MeasureReportType.SUBJECTLIST || type == MeasureReportType.PATIENTLIST) {
                                numeratorExclusionPatients = new HashMap<>();
                            }
                            break;
                        case DENOMINATOR:
                            denominatorCriteria = pop;
                            denominator = new HashMap<>();
                            if (type == MeasureReportType.SUBJECTLIST || type == MeasureReportType.PATIENTLIST) {
                                denominatorPatients = new HashMap<>();
                            }
                            break;
                        case DENOMINATOREXCLUSION:
                            denominatorExclusionCriteria = pop;
                            denominatorExclusion = new HashMap<>();
                            if (type == MeasureReportType.SUBJECTLIST || type == MeasureReportType.PATIENTLIST) {
                                denominatorExclusionPatients = new HashMap<>();
                            }
                            break;
                        case DENOMINATOREXCEPTION:
                            denominatorExceptionCriteria = pop;
                            denominatorException = new HashMap<>();
                            if (type == MeasureReportType.SUBJECTLIST || type == MeasureReportType.PATIENTLIST) {
                                denominatorExceptionPatients = new HashMap<>();
                            }
                            break;
                        case MEASUREPOPULATION:
                            measurePopulationCriteria = pop;
                            measurePopulation = new HashMap<>();
                            if (type == MeasureReportType.SUBJECTLIST || type == MeasureReportType.PATIENTLIST) {
                                measurePopulationPatients = new HashMap<>();
                            }
                            break;
                        case MEASUREPOPULATIONEXCLUSION:
                            measurePopulationExclusionCriteria = pop;
                            measurePopulationExclusion = new HashMap<>();
                            if (type == MeasureReportType.SUBJECTLIST || type == MeasureReportType.PATIENTLIST) {
                                measurePopulationExclusionPatients = new HashMap<>();
                            }
                            break;
                        case MEASUREOBSERVATION:
                            measureObservation = new HashMap<>();
                            break;
                    }
                }
            }

            switch (measureScoring) {
                case PROPORTION:
                case RATIO: {

                    // For each patient in the initial population
                    for (SubjectT patient : patients) {

                        // Are they in the initial population?
                        boolean inInitialPopulation = evaluatePopulationCriteria(patient, initialPopulationCriteria,
                                initialPopulation, initialPopulationPatients, null, null, null);
                        populateResourceMap(MeasurePopulationType.INITIALPOPULATION, resources, codeToResourceMap);

                        if (inInitialPopulation) {
                            // Are they in the denominator?
                            boolean inDenominator = evaluatePopulationCriteria(patient, denominatorCriteria,
                                    denominator, denominatorPatients, denominatorExclusionCriteria,
                                    denominatorExclusion, denominatorExclusionPatients);
                            populateResourceMap(MeasurePopulationType.DENOMINATOR, resources, codeToResourceMap);

                            if (inDenominator) {
                                // Are they in the numerator?
                                boolean inNumerator = evaluatePopulationCriteria(patient, numeratorCriteria, numerator,
                                        numeratorPatients, numeratorExclusionCriteria, numeratorExclusion,
                                        numeratorExclusionPatients);
                                populateResourceMap(MeasurePopulationType.NUMERATOR, resources, codeToResourceMap);

                                if (!inNumerator && inDenominator && (denominatorExceptionCriteria != null)) {
                                    // Are they in the denominator exception?
                                    boolean inException = false;
                                    for (ResourceT resource : evaluateCriteria(patient, denominatorExceptionCriteria)) {
                                        inException = true;
                                        denominatorException.put(this.getId.apply(resource), resource);
                                        denominator.remove(this.getId.apply(resource));
                                        populateResourceMap(MeasurePopulationType.DENOMINATOREXCEPTION, resources,
                                                codeToResourceMap);
                                    }
                                    if (inException) {
                                        if (denominatorExceptionPatients != null) {
                                            denominatorExceptionPatients.put(this.getId.apply(patient), patient);
                                        }
                                        if (denominatorPatients != null) {
                                            denominatorPatients.remove(this.getId.apply(patient));
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Calculate actual MeasureType score, Count(numerator) / Count(denominator)
                    if (denominator != null && numerator != null && denominator.size() > 0) {
                        this.setGroupScore(reportGroup, numerator.size() / (double) denominator.size());
                    }

                    break;
                }
                case CONTINUOUSVARIABLE: {

                    // For each patient in the PatientType list
                    for (SubjectT patient : patients) {

                        // Are they in the initial population?
                        boolean inInitialPopulation = evaluatePopulationCriteria(patient, initialPopulationCriteria,
                                initialPopulation, initialPopulationPatients, null, null, null);
                        populateResourceMap(MeasurePopulationType.INITIALPOPULATION, resources, codeToResourceMap);

                        if (inInitialPopulation) {
                            // Are they in the MeasureType population?
                            boolean inMeasurePopulation = evaluatePopulationCriteria(patient, measurePopulationCriteria,
                                    measurePopulation, measurePopulationPatients, measurePopulationExclusionCriteria,
                                    measurePopulationExclusion, measurePopulationExclusionPatients);

                            if (inMeasurePopulation) {
                                // TODO: Evaluate MeasureType observations
                                for (ResourceT resource : evaluateCriteria(patient, measureObservationCriteria)) {
                                    measureObservation.put(this.getId.apply(resource), resource);
                                }
                            }
                        }
                    }

                    break;
                }

                case COHORT: {

                    // For each patient in the PatientType list
                    for (SubjectT patient : patients) {
                        // Are they in the initial population?
                        evaluatePopulationCriteria(patient, initialPopulationCriteria,
                                initialPopulation, initialPopulationPatients, null, null, null);
                        populateResourceMap(MeasurePopulationType.INITIALPOPULATION, resources, codeToResourceMap);
                    }

                    break;
                }
            }

            // Add population reports for each group
            addPopulationCriteriaReport(report, reportGroup, initialPopulationCriteria,
                    initialPopulation != null ? initialPopulation.size() : 0,
                    initialPopulationPatients != null ? initialPopulationPatients.values() : null);
            addPopulationCriteriaReport(report, reportGroup, numeratorCriteria,
                    numerator != null ? numerator.size() : 0,
                    numeratorPatients != null ? numeratorPatients.values() : null);
            addPopulationCriteriaReport(report, reportGroup, numeratorExclusionCriteria,
                    numeratorExclusion != null ? numeratorExclusion.size() : 0,
                    numeratorExclusionPatients != null ? numeratorExclusionPatients.values() : null);
            addPopulationCriteriaReport(report, reportGroup, denominatorCriteria,
                    denominator != null ? denominator.size() : 0,
                    denominatorPatients != null ? denominatorPatients.values() : null);
            addPopulationCriteriaReport(report, reportGroup, denominatorExclusionCriteria,
                    denominatorExclusion != null ? denominatorExclusion.size() : 0,
                    denominatorExclusionPatients != null ? denominatorExclusionPatients.values() : null);
            addPopulationCriteriaReport(report, reportGroup, denominatorExceptionCriteria,
                    denominatorException != null ? denominatorException.size() : 0,
                    denominatorExceptionPatients != null ? denominatorExceptionPatients.values() : null);
            addPopulationCriteriaReport(report, reportGroup, measurePopulationCriteria,
                    measurePopulation != null ? measurePopulation.size() : 0,
                    measurePopulationPatients != null ? measurePopulationPatients.values() : null);
            addPopulationCriteriaReport(report, reportGroup, measurePopulationExclusionCriteria,
                    measurePopulationExclusion != null ? measurePopulationExclusion.size() : 0,
                    measurePopulationExclusionPatients != null ? measurePopulationExclusionPatients.values() : null);
            // TODO: MeasureType Observations...
        }

        // for (String key : codeToResourceMap.keySet()) {
        // list = new ListResource();
        // for (String element : codeToResourceMap.get(key)) {
        // ListResource.ListEntryComponent comp = new ListEntryComponent();
        // comp.setItem(new Reference('#' + element));
        // list.addEntry(comp);
        // }

        // if (!list.isEmpty()) {
        // list.setId(UUID.randomUUID().toString());
        // list.setTitle(key);
        // resources.put(list.getId(), list);
        // }
        // }

        // if (!resources.isEmpty()) {
        // FhirMeasureBundler bundler = new FhirMeasureBundler();
        // Bundle evaluatedResources = bundler.bundle(resources.values());
        // evaluatedResources.setId(UUID.randomUUID().toString());
        // report.setEvaluatedResource(Collections.singletonList(new Reference('#' +
        // evaluatedResources.getId())));
        // report.addContained(evaluatedResources);
        // }

        return report;
    }

    private void populateResourceMap(MeasurePopulationType type, HashMap<String, ResourceT> resources,
            HashMap<String, HashSet<String>> codeToResourceMap) {
        if (this.context.getEvaluatedResources().isEmpty()) {
            return;
        }

        if (!codeToResourceMap.containsKey(type.toCode())) {
            codeToResourceMap.put(type.toCode(), new HashSet<>());
        }

        HashSet<String> codeHashSet = codeToResourceMap.get((type.toCode()));

        for (Object o : this.context.getEvaluatedResources()) {
            try {
                ResourceT r = (ResourceT) o;
                String id = this.getId.apply(r);
                if (!codeHashSet.contains(id)) {
                    codeHashSet.add(id);
                }

                if (!resources.containsKey(id)) {
                    resources.put(id, r);
                }
            } catch (Exception e) {
            }
        }

        this.context.clearEvaluatedResources();
    }
}
