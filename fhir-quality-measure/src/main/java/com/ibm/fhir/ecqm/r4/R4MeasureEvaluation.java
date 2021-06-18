package com.ibm.fhir.ecqm.r4;

import static com.ibm.fhir.model.type.String.string;
import static com.ibm.fhir.cql.helpers.ModelHelper.canonical;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import org.opencds.cqf.cql.engine.execution.Context;
import org.opencds.cqf.cql.engine.runtime.Interval;


import com.ibm.fhir.ecqm.common.MeasureEvaluation;
import com.ibm.fhir.ecqm.common.MeasurePopulationType;
import com.ibm.fhir.ecqm.common.MeasureReportType;
import com.ibm.fhir.ecqm.common.MeasureScoring;
import com.ibm.fhir.model.resource.Measure;
import com.ibm.fhir.model.resource.MeasureReport;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Decimal;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.Quantity;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.code.MeasureReportStatus;

public class R4MeasureEvaluation<RT, ST extends RT> extends
        MeasureEvaluation<Object, Measure, Measure.Group, Measure.Group.Population, MeasureReport.Builder, MeasureReport.Group.Builder, MeasureReport.Group.Population.Builder, RT, ST> {

    public R4MeasureEvaluation(Context context, Measure measure, Interval measurementPeriod, String packageName,
            Function<RT, String> getId, String patientOrPractitionerId) {
        super(context, measure, measurementPeriod, packageName, getId, patientOrPractitionerId);
    }

    public R4MeasureEvaluation(Context context, Measure measure, Interval measurementPeriod, String packageName,
            Function<RT, String> getId) {
        super(context, measure, measurementPeriod, packageName, getId);
    }

    @Override
    protected void addPopulationReport(MeasureReport.Builder report, MeasureReport.Group.Builder group,
        Measure.Group.Population populationCriteria, int populationCount, Iterable<ST> subjectPopulation) {
        MeasureReport.Group.Population.Builder populationReport = MeasureReport.Group.Population.builder();
        populationReport.count(com.ibm.fhir.model.type.Integer.of(populationCount));
        populationReport.code(populationCriteria.getCode());

        MeasureReportType type = MeasureReportType.fromCode(report.build().getType().getValue());
        if ((type == MeasureReportType.SUBJECTLIST || type == MeasureReportType.PATIENTLIST) && subjectPopulation != null) {
            com.ibm.fhir.model.resource.List.Builder SUBJECTLIST = com.ibm.fhir.model.resource.List.builder();
            String listId = UUID.randomUUID().toString();
            SUBJECTLIST.id( listId );
            populationReport.subjectResults(Reference.builder().reference(string("#" + listId)).build());
            for( ST patient : subjectPopulation ) {
                com.ibm.fhir.model.resource.List.Entry.Builder entry = com.ibm.fhir.model.resource.List.Entry.builder();
                
                String patientId = this.getId.apply( patient );
                
                entry.item(Reference.builder().reference(string(patientId.startsWith("Patient/") ? patientId : String.format("Patient/%s", patientId))).build());
                SUBJECTLIST.entry(entry.build());
            }
            report.contained(SUBJECTLIST.build());
        }

        group.population(populationReport.build());
    }

    @Override
    protected void addReportGroup(MeasureReport.Builder report, MeasureReport.Group.Builder group) {
        report.group(group.build());
    }

    @Override
    protected MeasureReport.Builder createMeasureReport(String status, MeasureReportType type, Interval measurementPeriod,
        List<ST> subjects) {
        MeasureReport.Builder report = MeasureReport.builder();
        report.status(MeasureReportStatus.of(status));
        report.type(com.ibm.fhir.model.type.code.MeasureReportType.of(type.toCode()));
        report.measure(canonical(measure.getUrl(), measure.getVersion()));

        if (type == MeasureReportType.INDIVIDUAL && !subjects.isEmpty()) {
            ST subject = subjects.get(0);
            report.subject(Reference.builder().reference(string(subject.getClass().getSimpleName() + "/" + this.getId.apply(subject))).build());
        }
        
        OffsetDateTime start =  ((org.opencds.cqf.cql.engine.runtime.DateTime) measurementPeriod.getStart()).getDateTime();
        OffsetDateTime end = ((org.opencds.cqf.cql.engine.runtime.DateTime) measurementPeriod.getEnd()).getDateTime();
        
        report.period(Period.builder().start(DateTime.of(start.toZonedDateTime())).end(DateTime.of(end.toZonedDateTime())).build());

        return report;
    }

    @Override
    protected MeasureReport.Group.Builder createReportGroup(String id) {
        MeasureReport.Group.Builder builder = MeasureReport.Group.builder().id(id);
        builder.setValidating(false);
        return builder;
    }

    @Override
    protected String getCriteriaExpression(Measure.Group.Population population) {
        return population.getCriteria().getExpression().getValue();
    }

    @Override
    protected Iterable<Measure.Group> getGroup() {
        return this.measure.getGroup();
    }

    @Override
    protected String getGroupId(Measure.Group group) {
        return group.getId();
    }

    @Override
    protected MeasureScoring getMeasureScoring() {
        return MeasureScoring.fromCode(this.measure.getScoring().getCoding().get(0).getCode().getValue());
    }

    @Override
    protected Iterable<Measure.Group.Population> getPopulation(Measure.Group mgc) {
        return mgc.getPopulation();
    }

    @Override
    protected MeasurePopulationType getPopulationType(Measure.Group.Population population) {
        return MeasurePopulationType.fromCode(population.getCode().getCoding().get(0).getCode().getValue());
    }

    @Override
    protected void setGroupScore(MeasureReport.Group.Builder group, Double score) {
        group.measureScore(Quantity.builder().value(Decimal.of(score)).build());
    }
}
