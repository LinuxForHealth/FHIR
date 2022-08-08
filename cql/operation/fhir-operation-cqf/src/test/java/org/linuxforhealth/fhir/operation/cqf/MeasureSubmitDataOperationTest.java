/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.operation.cqf;

import static org.linuxforhealth.fhir.cql.helpers.ModelHelper.coding;
import static org.linuxforhealth.fhir.cql.helpers.ModelHelper.fhirstring;
import static org.linuxforhealth.fhir.cql.helpers.ModelHelper.reference;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.mockito.MockedStatic;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import org.linuxforhealth.fhir.exception.FHIROperationException;
import org.linuxforhealth.fhir.model.resource.Bundle;
import org.linuxforhealth.fhir.model.resource.Encounter;
import org.linuxforhealth.fhir.model.resource.MeasureReport;
import org.linuxforhealth.fhir.model.resource.Parameters;
import org.linuxforhealth.fhir.model.resource.Parameters.Parameter;
import org.linuxforhealth.fhir.model.resource.Patient;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.type.HumanName;
import org.linuxforhealth.fhir.model.type.Id;
import org.linuxforhealth.fhir.model.type.Instant;
import org.linuxforhealth.fhir.model.type.Meta;
import org.linuxforhealth.fhir.model.type.UnsignedInt;
import org.linuxforhealth.fhir.model.type.code.BundleType;
import org.linuxforhealth.fhir.model.type.code.EncounterStatus;
import org.linuxforhealth.fhir.registry.FHIRRegistry;
import org.linuxforhealth.fhir.search.util.SearchHelper;
import org.linuxforhealth.fhir.server.spi.operation.FHIROperationContext;
import org.linuxforhealth.fhir.server.spi.operation.FHIRResourceHelpers;

public class MeasureSubmitDataOperationTest {
    MeasureSubmitDataOperation op;
    SearchHelper searchHelper;

    @BeforeClass
    public void initializeSearchUtil() {
        searchHelper = new SearchHelper();
    }

    @BeforeMethod
    public void setup() {
        op = new MeasureSubmitDataOperation();
    }

    @Test
    public void testInstanceExecutionWithResources() throws Exception {

        MeasureReport.Builder builder = MeasureReport.builder()
                .id("measure-report-1");
        builder.setValidating(false);

        Patient p = Patient.builder()
                .id(UUID.randomUUID().toString())
                .name(HumanName.builder().family(fhirstring("Doe")).given(fhirstring("John")).build())
                .build();

        Encounter e = Encounter.builder()
                .id(UUID.randomUUID().toString())
                .meta(Meta.builder().versionId(Id.of("1")).lastUpdated(Instant.now()).build())
                .status(EncounterStatus.FINISHED)
                .subject( reference( p ) )
                .clazz( coding("wellness") )
                .build();

        Encounter e2 = Encounter.builder()
                .id(e.getId())
                .meta(Meta.builder().versionId(Id.of("2")).lastUpdated(Instant.now()).build())
                .status(EncounterStatus.FINISHED)
                .subject( reference( p ) )
                .clazz( coding("wellness") )
                .build();

        MeasureReport report = builder.build();
        List<Resource> resources = Arrays.asList(p,e,e2);

        runTest(report, resources);
    }

    @Test
    public void testInstanceExecutionWithoutResources() throws Exception {

        MeasureReport.Builder builder = MeasureReport.builder()
                .id("measure-report-1");
        builder.setValidating(false);

        MeasureReport report = builder.build();
        List<Resource> resources = Collections.emptyList();

        runTest(report, resources);
    }

    protected Parameters runTest(MeasureReport report, List<Resource> resources) throws Exception, FHIROperationException {
        Parameters inParams = createInParameters(report, resources);
        Bundle responseBundle = Bundle.builder()
                .type(BundleType.COLLECTION)
                .total(UnsignedInt.of(0))
                .build();

        try (MockedStatic<FHIRRegistry> staticRegistry = mockStatic(FHIRRegistry.class)) {
            FHIRRegistry mockRegistry = spy(FHIRRegistry.class);
            staticRegistry.when(FHIRRegistry::getInstance).thenReturn(mockRegistry);

            FHIRResourceHelpers resourceHelper = mock(FHIRResourceHelpers.class);
            when( resourceHelper.doBundle(any(Bundle.class), anyBoolean())).thenReturn(responseBundle);

            Parameters outParams = op.doInvoke(FHIROperationContext.createInstanceOperationContext("submit-data"),
                    null, null, null, inParams, resourceHelper, searchHelper);
            assertNotNull(outParams);
            return outParams;
        }
    }

    public Parameters createInParameters(MeasureReport report, List<Resource> resources) {
        Parameters.Builder inParams = Parameters.builder()
                .parameter(Parameter.builder()
                    .name(fhirstring(MeasureSubmitDataOperation.PARAM_IN_MEASURE_REPORT))
                    .resource(report)
                    .build());
        if( resources != null ) {
            resources.forEach( r -> {
                inParams.parameter(Parameter.builder()
                        .name(fhirstring(MeasureSubmitDataOperation.PARAM_IN_RESOURCE))
                        .resource(r)
                        .build());
            });
        }
        return inParams.build();
    }
}
