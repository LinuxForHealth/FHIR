/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.test;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.generator.exception.FHIRGeneratorException;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.OperationDefinition;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.server.spi.operation.FHIROperationUtil;

/**
 * @author pbastide
 *
 */
public class FHIROperationUtilTest {

    @Test
    public void testFHIROperationUtilEmptyParameters() throws FHIROperationException {

        OperationDefinition definition = buildOperationDefinition();
        Map<java.lang.String, List<java.lang.String>> queryParameters = Collections.emptyMap();

        Parameters parameters = FHIROperationUtil.getInputParameters(definition, queryParameters);
        assertNotNull(parameters);
        assertTrue(parameters.getParameter().isEmpty());

    }

    protected OperationDefinition buildOperationDefinition() {
        try (InputStream in =
                getClass().getClassLoader().getResourceAsStream("document-util-test.json");) {
            return FHIRParser.parser(Format.JSON).parse(in);
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    public static void generateOutput(Resource resource) {

        try (StringWriter writer = new StringWriter();) {
            FHIRGenerator.generator(Format.JSON, true).generate(resource, System.out);
            System.out.println(writer.toString());
        } catch (FHIRGeneratorException e) {

            e.printStackTrace();
            fail("unable to generate the fhir resource to JSON");

        } catch (IOException e1) {
            e1.printStackTrace();
            fail("unable to generate the fhir resource to JSON (io problem) ");
        }

    }
}
