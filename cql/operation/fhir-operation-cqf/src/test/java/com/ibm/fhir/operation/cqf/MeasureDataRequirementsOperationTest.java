/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.operation.cqf;

import static com.ibm.fhir.cql.helpers.ModelHelper.canonical;
import static com.ibm.fhir.cql.helpers.ModelHelper.concept;
import static com.ibm.fhir.cql.helpers.ModelHelper.fhircode;
import static com.ibm.fhir.cql.helpers.ModelHelper.fhirstring;
import static com.ibm.fhir.cql.helpers.ModelHelper.fhiruri;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.Library;
import com.ibm.fhir.model.resource.Measure;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.Expression;
import com.ibm.fhir.model.type.code.PublicationStatus;
import com.ibm.fhir.registry.FHIRRegistry;
import com.ibm.fhir.server.spi.operation.FHIROperationContext;
import com.ibm.fhir.server.spi.operation.FHIRResourceHelpers;

public class MeasureDataRequirementsOperationTest extends BaseDataRequirementsOperationTest {

    private Measure measure;
    private boolean primaryLibraryExists;

    @Override
    public AbstractDataRequirementsOperation getOperation() {
        return new MeasureDataRequirementsOperation();
    }

    @BeforeMethod
    public void setup() {

        measure = Measure.builder()
                .id("Test-1.0.0")
                .name(fhirstring("Test"))
                .version(fhirstring("1.0.0"))
                .url(fhiruri(URL_BASE + "Measure/Test"))
                .status(PublicationStatus.UNKNOWN)
                .scoring(concept("cohort"))
                .group( Measure.Group.builder().population(
                    Measure.Group.Population.builder()
                        .code(concept("initial-population"))
                        .criteria(
                            Expression.builder()
                                .expression(fhirstring("Initial Population"))
                                .language(fhircode("text/cql"))
                                .build()).build())
                    .build())
                .build();
        
        primaryLibraryExists = true;
    }

    @Test
    public void testInstanceExecution() throws Exception {
        String measureId = measure.getId();

        Parameters inParams = Parameters.builder()
                .parameter(Parameters.Parameter.builder().name(fhirstring("periodStart")).value(Date.of("2000-01-01")).build())
                .parameter(Parameters.Parameter.builder().name(fhirstring("periodEnd")).value(Date.of("2001-01-01")).build())
                .build();

        Parameters outParams = runTest(FHIROperationContext.createInstanceOperationContext("data-requirements"),
                Measure.class, primaryLibrary -> measureId, primaryLibrary -> inParams);
        assertNotNull(outParams);

        Library moduleDefinition = (Library) outParams.getParameter().get(0).getResource();
        assertEquals(moduleDefinition.getRelatedArtifact().stream().filter( ra -> ra.getResource().getValue().equals(measure.getLibrary().get(0).getValue()) ).count(), 1);
    }
    
    @Test(expectedExceptions = { FHIROperationException.class } , expectedExceptionsMessageRegExp  = "Failed to resolve Library resource.*")
    public void testInstanceExecutionMissingPrimaryLibrary() throws Exception {
        primaryLibraryExists = false;
        String measureId = measure.getId();

        Parameters inParams = Parameters.builder()
                .parameter(Parameters.Parameter.builder().name(fhirstring("periodStart")).value(Date.of("2000-01-01")).build())
                .parameter(Parameters.Parameter.builder().name(fhirstring("periodEnd")).value(Date.of("2001-01-01")).build())
                .build();
 
        runTest(FHIROperationContext.createInstanceOperationContext("data-requirements"),
            Measure.class, primaryLibrary -> measureId, primaryLibrary -> inParams);
    }

    @Override
    protected Library initializeLibraries(FHIRRegistry mockRegistry, FHIRResourceHelpers resourceHelper) throws Exception {
        Library primaryLibrary = super.initializeLibraries(mockRegistry, resourceHelper, primaryLibraryExists);

        measure = measure.toBuilder().library( canonical(primaryLibrary) ).build();

        when(resourceHelper.doRead(eq("Measure"), eq(measure.getId()), anyBoolean(), anyBoolean(), any())).thenAnswer(x -> TestHelper.asResult(measure));
        when(mockRegistry.getResource( canonical(measure.getUrl(), measure.getVersion()).getValue(), Measure.class )).thenReturn(measure);

        return primaryLibrary;
    }
}
