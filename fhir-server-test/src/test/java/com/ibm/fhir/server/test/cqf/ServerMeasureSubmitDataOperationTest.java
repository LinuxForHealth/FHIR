/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.server.test.cqf;

import static com.ibm.fhir.model.type.String.string;
import static org.testng.Assert.assertNotNull;

import java.io.InputStream;
import java.io.StringReader;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Encounter;
import com.ibm.fhir.model.resource.MeasureReport;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.code.AdministrativeGender;
import com.ibm.fhir.model.type.code.EncounterStatus;
import com.ibm.fhir.model.type.code.MeasureReportStatus;
import com.ibm.fhir.model.type.code.MeasureReportType;

public class ServerMeasureSubmitDataOperationTest extends BaseMeasureOperationTest {

    @Test
    public void testMeasureSubmitDataResourceType() throws Exception {
        MeasureReport measureReport = MeasureReport.builder()
                .id("submitdata-measure")
                .measure( Canonical.of(TEST_MEASURE_URL) )
                .status(MeasureReportStatus.COMPLETE)
                .type(MeasureReportType.INDIVIDUAL)
                .period(Period.builder().start(DateTime.of("2001-01-01")).end(DateTime.of("2001-01-01")).build())
                .build();

        Patient patient = Patient.builder()
                .id("submitdata-patient")
                .name(HumanName.builder().family(string("Machina")).given(string("Deus Ex")).build())
                .birthDate(Date.of("1970-01-01"))
                .gender(AdministrativeGender.OTHER)
                .build();

        Encounter encounter = Encounter.builder()
                .id("submitdata-encounter")
                .meta(Meta.builder().versionId(Id.of("1")).lastUpdated(Instant.now()).build())
                .status(EncounterStatus.FINISHED)
                .clazz(Coding.builder().code(Code.of("wellness")).build())
                .subject( Reference.builder().reference(string("Patient/" + patient.getId())).build() )
                .build();

        Parameters parameters = Parameters.builder()
                .parameter(Parameter.builder()
                    .name(string("measureReport"))
                    .resource(measureReport)
                    .build())
                .parameter(Parameter.builder()
                    .name(string("resource"))
                    .resource(patient).build())
                .parameter(Parameter.builder()
                    .name(string("resource"))
                    .resource(encounter).build())
                .build();

        Response response =
                getWebTarget().path("/Measure/{id}/$submit-data")
                    .resolveTemplate("id", TEST_MEASURE_ID)
                    .request()
                    .post(Entity.json(parameters));
        assertResponse(response, 200);

        String responseBody = response.readEntity(String.class);
        //System.out.println(responseBody);
        Bundle output = (Bundle) FHIRParser.parser(Format.JSON).parse(new StringReader(responseBody));
        assertNotNull(output);
    }

    @Test
    public void testMeasureSubmitDataResourceTypeDuplicateResourceID() throws Exception {
        MeasureReport measureReport = MeasureReport.builder()
                .id("submitdata-measure")
                .measure( Canonical.of(TEST_MEASURE_URL) )
                .status(MeasureReportStatus.COMPLETE)
                .type(MeasureReportType.INDIVIDUAL)
                .period(Period.builder().start(DateTime.of("2001-01-01")).end(DateTime.of("2001-01-01")).build())
                .build();

        Patient patient = Patient.builder()
                .id("submitdata-patient")
                .name(HumanName.builder().family(string("Machina")).given(string("Deus Ex")).build())
                .birthDate(Date.of("1970-01-01"))
                .gender(AdministrativeGender.OTHER)
                .build();

        Encounter encounter = Encounter.builder()
                .id("submitdata-encounter")
                .meta(Meta.builder().versionId(Id.of("1")).lastUpdated(Instant.now()).build())
                .status(EncounterStatus.FINISHED)
                .clazz(Coding.builder().code(Code.of("wellness")).build())
                .subject( Reference.builder().reference(string("Patient/" + patient.getId())).build() )
                .build();

        Encounter encounter2 = Encounter.builder()
                .id("submitdata-encounter")
                .meta(Meta.builder().versionId(Id.of("2")).lastUpdated(Instant.now()).build())
                .status(EncounterStatus.FINISHED)
                .clazz(Coding.builder().code(Code.of("wellness")).build())
                .subject( Reference.builder().reference(string("Patient/" + patient.getId())).build() )
                .build();

        Parameters parameters = Parameters.builder()
                .parameter(Parameter.builder()
                    .name(string("measureReport"))
                    .resource(measureReport)
                    .build())
                .parameter(Parameter.builder()
                    .name(string("resource"))
                    .resource(patient).build())
                .parameter(Parameter.builder()
                    .name(string("resource"))
                    .resource(encounter).build())
                .parameter(Parameter.builder()
                    .name(string("resource"))
                    .resource(encounter2).build())
                .build();

        Response response =
                getWebTarget().path("/Measure/{id}/$submit-data")
                    .resolveTemplate("id", TEST_MEASURE_ID)
                    .request()
                    .post(Entity.json(parameters));
        assertResponse(response, 409);

        String responseBody = response.readEntity(String.class);
        OperationOutcome output = (OperationOutcome) FHIRParser.parser(Format.JSON).parse(new StringReader(responseBody));
        assertNotNull(output);
    }

    @Test
    public void testMeasureSubmitDataOnlyMeasureReportTwice() throws Exception {
        MeasureReport measureReport = null;
        try( InputStream is = TestUtil.resolveFileLocation("testdata/MeasureReport-EXM104.json") ) {
            measureReport = FHIRParser.parser(Format.JSON).parse(is);
        }
        Parameters parameters = Parameters.builder()
                .parameter(Parameter.builder()
                    .name(string("measureReport"))
                    .resource(measureReport)
                    .build())
                .build();

        int numIterations = 2;
        for( int i=0; i<numIterations; i++) {
            Response response =
                    getWebTarget().path("/Measure/{id}/$submit-data")
                        .resolveTemplate("id", TEST_MEASURE_ID)
                        .request()
                        .post(Entity.json(parameters));
            assertResponse(response, 200);

            String responseBody = response.readEntity(String.class);
            Bundle output = (Bundle) FHIRParser.parser(Format.JSON).parse(new StringReader(responseBody));
            assertNotNull(output);
        }
    }
}
