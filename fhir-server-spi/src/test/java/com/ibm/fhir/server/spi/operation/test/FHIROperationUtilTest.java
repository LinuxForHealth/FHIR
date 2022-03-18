 /*
 * (C) Copyright IBM Corp. 2017, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 package com.ibm.fhir.server.spi.operation.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.testng.annotations.Test;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.OperationDefinition;
import com.ibm.fhir.model.resource.OperationDefinition.Parameter;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.code.FHIRAllTypes;
import com.ibm.fhir.model.type.code.OperationKind;
import com.ibm.fhir.model.type.code.OperationParameterUse;
import com.ibm.fhir.model.type.code.PublicationStatus;
import com.ibm.fhir.server.spi.operation.FHIROperationUtil;

public class FHIROperationUtilTest {

    Set<FHIRAllTypes> PRIMITIVE_TYPES = Set.of(FHIRAllTypes.BOOLEAN, FHIRAllTypes.INTEGER, FHIRAllTypes.STRING, FHIRAllTypes.DECIMAL,
            FHIRAllTypes.URI, FHIRAllTypes.URL, FHIRAllTypes.CANONICAL, FHIRAllTypes.INSTANT, FHIRAllTypes.DATE,
            FHIRAllTypes.DATE_TIME, FHIRAllTypes.TIME, FHIRAllTypes.CODE, FHIRAllTypes.OID, FHIRAllTypes.ID,
            FHIRAllTypes.UNSIGNED_INT, FHIRAllTypes.POSITIVE_INT, FHIRAllTypes.UUID);

    OperationDefinition opDef = OperationDefinition.builder()
            .name("test")
            .status(PublicationStatus.DRAFT)
            .kind(OperationKind.OPERATION)
            .code(Code.of("test"))
            .system(false)
            .type(false)
            .instance(false)
            // add an input parameter for each primitive type
            .parameter(PRIMITIVE_TYPES.stream()
                    .map(t -> buildParameter(t))
                    .collect(Collectors.toList()))
            // add an input parameter that isn't a primitive type
            .parameter(buildParameter(FHIRAllTypes.CODEABLE_CONCEPT))
            // add an input parameter of type resource (abstract and concrete)
            .parameter(buildParameter(FHIRAllTypes.RESOURCE))
            .parameter(buildParameter(FHIRAllTypes.DOMAIN_RESOURCE))
            .parameter(buildParameter(FHIRAllTypes.PATIENT))
            // add an input parameter with no type
            .parameter(Parameter.builder()
                    .name(Code.of("unknown"))
                    .use(OperationParameterUse.IN)
                    .min(1).max("1")
                    .build())
            .build();

    Parameter buildParameter(FHIRAllTypes type) {
        return Parameter.builder()
                .name(type)
                .use(OperationParameterUse.IN)
                .min(1).max("1")
                .type(type)
                .build();
    }

    /**
     * FHIROperationUtil.getInputParameters constructs a Parameters object
     * from query parameters
     */
    @Test
    void testGetInputParametersFromQueryParameters() throws FHIROperationException {
        Map<String, List<String>> queryParams = new HashMap<>();
        queryParams.put("boolean", List.of("true"));                        // 1
        queryParams.put("integer", List.of("-1"));                          // 2
        queryParams.put("string", List.of("test"));                         // 3
        queryParams.put("decimal", List.of("0.123"));                       // 4
        queryParams.put("uri", List.of("uri"));                             // 5
        queryParams.put("url", List.of("http://example.com"));              // 6
        queryParams.put("canonical", List.of("http://example.com|1"));      // 7
        queryParams.put("instant", List.of("2022-03-18T03:58:00.123Z"));    // 8
        queryParams.put("date", List.of("2022-03-18"));                     // 9
        queryParams.put("dateTime", List.of("2022-03-18T22:00:00Z"));       // 10
        queryParams.put("time", List.of("12:30:00"));                       // 11
        queryParams.put("code", List.of("test"));                           // 12
        queryParams.put("oid", List.of("urn:oid:1.2.3"));                   // 13
        queryParams.put("id", List.of("test"));                             // 14
        queryParams.put("unsignedInt", List.of("100000000"));               // 15
        queryParams.put("positiveInt", List.of("1"));                       // 16
        queryParams.put("uuid", List.of("urn:uuid:" + UUID.randomUUID()));  // 17

        Parameters inputParameters = FHIROperationUtil.getInputParameters(opDef, queryParams);
        assertNotNull(inputParameters);
        assertEquals(inputParameters.getParameter().size(), 17);
    }

    /**
     * FHIROperationUtil.getInputParameters constructs a Parameters object
     * with entries for each query parameter, even when a single param is repeated
     */
    @Test
    void testGetInputParametersFromQueryParameters_multiple() throws FHIROperationException {
        Map<String, List<String>> queryParams = new HashMap<>();
        queryParams.put("boolean", List.of("true", "false"));               // 2
        queryParams.put("string", List.of("a,b,c"));                        // 3

        Parameters inputParameters = FHIROperationUtil.getInputParameters(opDef, queryParams);
        assertNotNull(inputParameters);
        assertEquals(inputParameters.getParameter().size(), 3);
    }

    /**
     * FHIROperationUtil.getInputParameters constructs a Parameters object
     * even when queryParams is empty
     */
    @Test
    void testGetInputParametersFromQueryParameters_empty() throws FHIROperationException {
        Map<String, List<String>> queryParams = new HashMap<>();
        Parameters inputParameters = FHIROperationUtil.getInputParameters(opDef, queryParams);

        assertNotNull(inputParameters);
        assertTrue(inputParameters.getParameter().isEmpty());
    }

    /**
     * FHIROperationUtil.getInputParameters skips input parameters with an unknown type
     */
    @Test
    void testGetInputParametersFromQueryParameters_unknownType() throws FHIROperationException {
        Map<String, List<String>> queryParams = new HashMap<>();
        queryParams.put("unknown", List.of("anything"));
        Parameters inputParameters = FHIROperationUtil.getInputParameters(opDef, queryParams);

        assertNotNull(inputParameters);
        assertTrue(inputParameters.getParameter().isEmpty());
    }

    /**
     * FHIROperationUtil.getInputParameters skips input parameters that are not defined
     */
    @Test
    void testGetInputParametersFromQueryParameters_unknownParam() throws FHIROperationException {
        Map<String, List<String>> queryParams = new HashMap<>();
        queryParams.put("bogus", List.of("anything"));
        Parameters inputParameters = FHIROperationUtil.getInputParameters(opDef, queryParams);

        assertNotNull(inputParameters);
        assertTrue(inputParameters.getParameter().isEmpty());
    }

    /**
     * FHIROperationUtil.getInputParameters fails to construct a Parameters object
     * for a parameter whose type is complex (i.e. not a primitive)
     */
    @Test(expectedExceptions = FHIROperationException.class)
    void testGetInputParametersFromQueryParameters_complex() throws FHIROperationException {
        Map<String, List<String>> queryParams = new HashMap<>();
        queryParams.put("CodeableConcept", List.of("http://example.com|bogus"));

        FHIROperationUtil.getInputParameters(opDef, queryParams);
        fail();
    }
}
