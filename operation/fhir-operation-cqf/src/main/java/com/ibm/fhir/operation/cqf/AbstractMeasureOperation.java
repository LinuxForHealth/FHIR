package com.ibm.fhir.operation.cqf;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAccessor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.opencds.cqf.cql.engine.data.CompositeDataProvider;
import org.opencds.cqf.cql.engine.data.DataProvider;
import org.opencds.cqf.cql.engine.execution.Context;
import org.opencds.cqf.cql.engine.execution.InMemoryLibraryLoader;
import org.opencds.cqf.cql.engine.execution.LibraryLoader;
import org.opencds.cqf.cql.engine.model.ModelResolver;
import org.opencds.cqf.cql.engine.retrieve.RetrieveProvider;
import org.opencds.cqf.cql.engine.runtime.DateTime;
import org.opencds.cqf.cql.engine.runtime.Interval;
import org.opencds.cqf.cql.engine.terminology.TerminologyProvider;

import com.ibm.fhir.core.FHIRConstants;
import com.ibm.fhir.cql.Constants;
import com.ibm.fhir.cql.engine.model.FhirModelResolver;
import com.ibm.fhir.cql.engine.searchparam.SearchParameterResolver;
import com.ibm.fhir.cql.engine.server.retrieve.ServerFhirRetrieveProvider;
import com.ibm.fhir.cql.engine.server.terminology.ServerFhirTerminologyProvider;
import com.ibm.fhir.cql.helpers.DateHelper;
import com.ibm.fhir.cql.helpers.LibraryHelper;
import com.ibm.fhir.cql.helpers.ParameterMap;
import com.ibm.fhir.cql.translator.CqlTranslationProvider;
import com.ibm.fhir.cql.translator.FhirLibraryLibrarySourceProvider;
import com.ibm.fhir.cql.translator.impl.InJVMCqlTranslationProvider;
import com.ibm.fhir.ecqm.common.MeasureReportType;
import com.ibm.fhir.ecqm.r4.R4MeasureEvaluation;
import com.ibm.fhir.model.resource.Library;
import com.ibm.fhir.model.resource.Measure;
import com.ibm.fhir.model.resource.MeasureReport;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.registry.FHIRRegistry;
import com.ibm.fhir.server.operation.spi.AbstractOperation;
import com.ibm.fhir.server.operation.spi.FHIRResourceHelpers;

public abstract class AbstractMeasureOperation extends AbstractOperation {

    public static final String CQL_PARAM_MEASUREMENT_PERIOD = "Measurement Period";

    public static final String PARAM_IN_PERIOD_END = "periodEnd";
    public static final String PARAM_IN_PERIOD_START = "periodStart";

    public TerminologyProvider getTerminologyProvider() {
        TerminologyProvider termProvider = new ServerFhirTerminologyProvider();
        return termProvider;
    }

    public ServerFhirRetrieveProvider getRetrieveProvider(FHIRResourceHelpers resourceHelper, TerminologyProvider termProvider) {
        SearchParameterResolver resolver = new SearchParameterResolver();
        ServerFhirRetrieveProvider retrieveProvider = new ServerFhirRetrieveProvider(resourceHelper, resolver);
        retrieveProvider.setExpandValueSets(false); // TODO - use server config settings
        retrieveProvider.setTerminologyProvider(termProvider);
        retrieveProvider.setPageSize(FHIRConstants.FHIR_PAGE_SIZE_DEFAULT_MAX); // TODO - use server config settings?
        return retrieveProvider;
    }

    public MeasureReport.Builder doMeasureEvaluation(Measure measure, Interval measurementPeriod, String subjectOrPractitionerId, MeasureReportType reportType,
        TerminologyProvider termProvider, Map<String, DataProvider> dataProviders) {

        int numLibraries = (measure.getLibrary() != null) ? measure.getLibrary().size() : 0;
        if (numLibraries != 1) {
            throw new IllegalArgumentException(String.format("Unexpected number of libraries '%d' referenced by measure '%s'", numLibraries, measure.getId()));
        }

        Library primaryLibrary = FHIRRegistry.getInstance().getResource(measure.getLibrary().get(0).getValue(), Library.class);
        List<Library> fhirLibraries = LibraryHelper.loadLibraries(primaryLibrary);

        List<org.cqframework.cql.elm.execution.Library> cqlLibraries = loadCqlLibraries(fhirLibraries);
        LibraryLoader ll = new InMemoryLibraryLoader(cqlLibraries);

        Context context = new Context(cqlLibraries.get(0));
        dataProviders.entrySet().stream().forEach(e -> context.registerDataProvider(e.getKey(), e.getValue()));
        context.registerTerminologyProvider(termProvider);
        context.registerLibraryLoader(ll);

        // By convention, measure authors such as CMS and HEDIS expect the measurement period to be available in the
        // CQL libraries under this well-known name.
        context.setParameter(null, CQL_PARAM_MEASUREMENT_PERIOD, measurementPeriod);

        R4MeasureEvaluation<Resource, Patient> evaluation =
                new R4MeasureEvaluation<>(context, measure, measurementPeriod, FhirModelResolver.RESOURCE_PACKAGE_NAME, r -> r.getId(), subjectOrPractitionerId);

        MeasureReport.Builder report = null;
        if (reportType != null) {
            report = evaluation.evaluate(reportType);
        } else {
            report = evaluation.evaluatePopulationMeasure();
        }
        return report;
    }

    public Interval getMeasurementPeriod(ParameterMap paramMap, ZoneOffset zoneOffset) {
        Parameter pStart = paramMap.getSingletonParameter(PARAM_IN_PERIOD_START);
        Parameter pEnd = paramMap.getSingletonParameter(PARAM_IN_PERIOD_END);

        TemporalAccessor ta;
        ta = ((Date) pStart.getValue()).getValue();
        LocalDateTime ldStart = DateHelper.dateFloor(ta);

        ta = ((Date) pEnd.getValue()).getValue();
        LocalDateTime ldEnd = DateHelper.dateCeiling(ta);
        
        OffsetDateTime odtStart = OffsetDateTime.of( ldStart, zoneOffset );
        OffsetDateTime odtEnd = OffsetDateTime.of( ldEnd, zoneOffset );

        DateTime dtStart = new DateTime(odtStart);
        DateTime dtEnd = new DateTime(odtEnd);

        return new Interval(dtStart, true, dtEnd, true);
    }

    protected Map<String, DataProvider> getDataProviders(RetrieveProvider retrieveProvider) {
        String suffix = "";

        // This is a hack to get around the fact that IBM FHIR types are in multiple packages
        // and the CQL engine expects there to be only a single package name
        Map<String, DataProvider> dataProviders = new HashMap<>();
        for (String packageName : FhirModelResolver.ALL_PACKAGES) {
            ModelResolver modelResolver = new FhirModelResolver();
            modelResolver.setPackageName(packageName);

            DataProvider provider = new CompositeDataProvider(modelResolver, retrieveProvider);
            dataProviders.put(Constants.FHIR_NS_URI + suffix, provider);
            suffix += "-";
        }
        return dataProviders;
    }

    protected LibraryLoader createLibraryLoader(List<Library> libraries) {
        List<org.cqframework.cql.elm.execution.Library> result = loadCqlLibraries(libraries);
        return new InMemoryLibraryLoader(result);
    }

    public List<org.cqframework.cql.elm.execution.Library> loadCqlLibraries(List<Library> libraries) {
        FhirLibraryLibrarySourceProvider sourceProvider = new FhirLibraryLibrarySourceProvider(libraries);
        CqlTranslationProvider translator = new InJVMCqlTranslationProvider(sourceProvider);

        List<org.cqframework.cql.elm.execution.Library> result =
                libraries.stream().flatMap(fl -> LibraryHelper.loadLibrary(translator, fl).stream()).filter(Objects::nonNull).collect(Collectors.toList());
        return result;
    }
}
