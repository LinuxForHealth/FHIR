/*
 * (C) Copyright IBM Corp. 2017, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.operation.test;

import java.io.InputStream;

import org.linuxforhealth.fhir.exception.FHIROperationException;
import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.parser.FHIRParser;
import org.linuxforhealth.fhir.model.resource.OperationDefinition;
import org.linuxforhealth.fhir.model.resource.Parameters;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.search.util.SearchHelper;
import org.linuxforhealth.fhir.server.spi.operation.AbstractOperation;
import org.linuxforhealth.fhir.server.spi.operation.FHIROperationContext;
import org.linuxforhealth.fhir.server.spi.operation.FHIRResourceHelpers;

/**
 * This class will test what happens if there is a bad OperationDefinition defined for a custom operation.<br/>
 * There is no corresponding testcase as the Java ServiceLoader (SPI) mechanism <br/>
 * will automatically load this service if it is configured as a service provider and available on the classpath.<br/>
 * The expected result is:<br/>
 * 1. to see an error/message explaining why this service was not loaded<br/>
 * 2. for other operations to continue working<br/>
 * @author lmsurpre
 */
public class BadOperation extends AbstractOperation {

    @Override
    protected OperationDefinition buildOperationDefinition() {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("operationdefinition-bad.json");){
            return FHIRParser.parser(Format.JSON).parse(in);
        } catch (Exception e) {
            throw new RuntimeException("Unable to read operationdefinition-bad.json", e);
        }
    }

    @Override
    protected Parameters doInvoke(FHIROperationContext operationContext, Class<? extends Resource> resourceType, String logicalId, String versionId,
            Parameters parameters, FHIRResourceHelpers resourceHelper, SearchHelper searchHelper) throws FHIROperationException {
        // do nothing
        return null;
    }
}
