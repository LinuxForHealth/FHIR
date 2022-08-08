/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.operation.cqf;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Map;

import org.opencds.cqf.cql.engine.data.DataProvider;
import org.opencds.cqf.cql.engine.execution.Context;
import org.opencds.cqf.cql.engine.execution.InMemoryLibraryLoader;
import org.opencds.cqf.cql.engine.execution.LibraryLoader;
import org.opencds.cqf.cql.engine.retrieve.RetrieveProvider;
import org.opencds.cqf.cql.engine.runtime.DateTime;
import org.opencds.cqf.cql.engine.runtime.Interval;
import org.opencds.cqf.cql.engine.terminology.TerminologyProvider;

import org.linuxforhealth.fhir.core.FHIRConstants;
import org.linuxforhealth.fhir.cql.engine.model.FHIRModelResolver;
import org.linuxforhealth.fhir.cql.engine.searchparam.SearchParameterResolver;
import org.linuxforhealth.fhir.cql.engine.server.retrieve.ServerFHIRRetrieveProvider;
import org.linuxforhealth.fhir.cql.engine.server.terminology.ServerFHIRTerminologyProvider;
import org.linuxforhealth.fhir.cql.helpers.DateHelper;
import org.linuxforhealth.fhir.cql.helpers.LibraryHelper;
import org.linuxforhealth.fhir.cql.helpers.ModelHelper;
import org.linuxforhealth.fhir.cql.helpers.ParameterMap;
import org.linuxforhealth.fhir.ecqm.common.MeasureReportType;
import org.linuxforhealth.fhir.ecqm.r4.MeasureHelper;
import org.linuxforhealth.fhir.ecqm.r4.R4MeasureEvaluation;
import org.linuxforhealth.fhir.exception.FHIROperationException;
import org.linuxforhealth.fhir.model.resource.Library;
import org.linuxforhealth.fhir.model.resource.Measure;
import org.linuxforhealth.fhir.model.resource.MeasureReport;
import org.linuxforhealth.fhir.model.resource.Parameters.Parameter;
import org.linuxforhealth.fhir.model.resource.Patient;
import org.linuxforhealth.fhir.model.type.Date;
import org.linuxforhealth.fhir.search.util.SearchHelper;
import org.linuxforhealth.fhir.server.spi.operation.AbstractOperation;
import org.linuxforhealth.fhir.server.spi.operation.FHIRResourceHelpers;

public abstract class AbstractMeasureOperation extends AbstractOperation {

    public static final String CQL_PARAM_MEASUREMENT_PERIOD = "Measurement Period";

    public static final String PARAM_IN_PERIOD_END = "periodEnd";
    public static final String PARAM_IN_PERIOD_START = "periodStart";

    /**
     * Create the terminology provider that will be used to access terminology data
     * during operation evaluation.
     *
     * @return Terminology Provider
     */
    public TerminologyProvider getTerminologyProvider(FHIRResourceHelpers resourceHelpers) {
        TerminologyProvider termProvider = new ServerFHIRTerminologyProvider(resourceHelpers);
        return termProvider;
    }

    /**
     * Create the retrieve provider that will be used to perform data retrieval during
     * the operation evaluation.
     *
     * @param resourceHelper FHIR Resource Helpers
     * @param termProvider Terminology Provider
     * @return Retrieve provider configured as appropriate for the environment
     */
    public RetrieveProvider getRetrieveProvider(FHIRResourceHelpers resourceHelper, TerminologyProvider termProvider, SearchHelper searchHelper) {
        SearchParameterResolver resolver = new SearchParameterResolver(searchHelper);
        ServerFHIRRetrieveProvider retrieveProvider = new ServerFHIRRetrieveProvider(resourceHelper, resolver);
        retrieveProvider.setExpandValueSets(false); // TODO - use server config settings
        retrieveProvider.setTerminologyProvider(termProvider);
        retrieveProvider.setPageSize(FHIRConstants.FHIR_PAGE_SIZE_DEFAULT_MAX); // TODO - use server config settings?
        return retrieveProvider;
    }

    /**
     * Given a FHIR Measure resource, evaluate the measure and return
     * a report of the results.
     *
     * @param resourceHelpers
     *            Resource helpers for data access operations
     * @param measure
     *            Measure resource to be evaluated
     * @param zoneOffset
     *            Zone offset of the timezone that will be used for Now
     *            or when partial dates are specified in the CQL.
     * @param measurementPeriod
     *            Measurement period that will be provided as input
     *            to the Measure calculation.
     * @param subjectOrPractitionerId
     *            Subject or Practitioner ID for which the measure will be calculated.
     * @param reportType
     *            The type of measure report: subject, subject-list, or population. If not specified, a default value of
     *            subject will be used if the subject parameter is supplied, otherwise, population will be used
     * @param termProvider
     *            terminology provider
     * @param dataProviders
     *            data provider
     * @return MeasureReport.Builder that contains the calculated report data
     * @throws FHIROperationException
     *             Measure evaluation fails
     */
    public MeasureReport.Builder doMeasureEvaluation(FHIRResourceHelpers resourceHelpers, Measure measure, ZoneOffset zoneOffset, Interval measurementPeriod, String subjectOrPractitionerId,
        MeasureReportType reportType,
        TerminologyProvider termProvider, Map<String, DataProvider> dataProviders) throws FHIROperationException {

        String primaryLibraryId = MeasureHelper.getPrimaryLibraryId(measure);

        Library primaryLibrary = OperationHelper.loadLibraryByReference(resourceHelpers, primaryLibraryId);
        List<Library> fhirLibraries = LibraryHelper.loadLibraries(primaryLibrary);

        List<org.cqframework.cql.elm.execution.Library> cqlLibraries = OperationHelper.loadCqlLibraries(fhirLibraries);
        LibraryLoader ll = new InMemoryLibraryLoader(cqlLibraries);

        ZonedDateTime zdt = ZonedDateTime.now(zoneOffset);

        Context context = new Context(cqlLibraries.get(0), zdt);
        dataProviders.entrySet().stream().forEach(e -> context.registerDataProvider(e.getKey(), e.getValue()));
        context.registerTerminologyProvider(termProvider);
        context.registerLibraryLoader(ll);

        // By convention, measure authors such as CMS and HEDIS expect the measurement period to be available in the
        // CQL libraries under this well-known name.
        context.setParameter(null, CQL_PARAM_MEASUREMENT_PERIOD, measurementPeriod);

        R4MeasureEvaluation<Patient> evaluation =
                new R4MeasureEvaluation<>(context, measure, measurementPeriod, FHIRModelResolver.RESOURCE_PACKAGE_NAME, r -> r.getId(), subjectOrPractitionerId);

        MeasureReport.Builder report = null;
        if (reportType != null) {
            report = evaluation.evaluate(reportType);
        } else {
            report = evaluation.evaluatePopulationMeasure();
        }
        return report;
    }

    /**
     * Return the <code>ZoneOffset</code> which will be used
     * to interpret dates that do not have associated time zone information.
     * This includes the periodStart, periodEnd, and those specified in the
     * CQL that is evaluated, . The default implementation uses the zone
     * offset of the server performing the evaluation.
     *
     * @param paramMap
     *            Operation input parameters
     * @return timezone offset that will be applied to dates without TimeZone specified
     */
    public ZoneOffset getZoneOffset(ParameterMap paramMap) {
        ZoneOffset zoneOffset = null;

        Parameter pStart = paramMap.getSingletonParameter(PARAM_IN_PERIOD_START);
        if (pStart != null) {
            zoneOffset = ModelHelper.getZoneOffset(pStart.getValue());
        }

        if (zoneOffset == null) {
            zoneOffset = OffsetDateTime.now().getOffset();
        }

        return zoneOffset;
    }

    /**
     * Construct the Measurement Period interval that will be
     * passed to the CQL Engine and Measurement Report builder.
     * The input parameters are FHIR date objects that do not
     * contain timezone information. The start and end DateTime
     * objects will be calculated based on the <code>zoneOffset</code>
     * that is provided.
     *
     * @param paramMap
     *            Operation input parameters
     * @param zoneOffset
     * @return Interval of DateTime values representing the measurement period.
     */
    public Interval getMeasurementPeriod(ParameterMap paramMap, ZoneOffset zoneOffset) {
        Parameter pStart = paramMap.getSingletonParameter(PARAM_IN_PERIOD_START);
        Parameter pEnd = paramMap.getSingletonParameter(PARAM_IN_PERIOD_END);

        TemporalAccessor ta;
        ta = ((Date) pStart.getValue()).getValue();
        LocalDateTime ldStart = DateHelper.dateFloor(ta);

        ta = ((Date) pEnd.getValue()).getValue();
        LocalDateTime ldEnd = DateHelper.dateCeiling(ta);

        OffsetDateTime odtStart = OffsetDateTime.of(ldStart, zoneOffset);
        OffsetDateTime odtEnd = OffsetDateTime.of(ldEnd, zoneOffset);

        DateTime dtStart = new DateTime(odtStart);
        DateTime dtEnd = new DateTime(odtEnd);

        return new Interval(dtStart, true, dtEnd, true);
    }
}
