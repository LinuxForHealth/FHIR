/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.operation.test;

import java.io.InputStream;

import com.ibm.watsonhealth.fhir.model.OperationDefinition;
import com.ibm.watsonhealth.fhir.model.Parameters;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil.Format;
import com.ibm.watsonhealth.fhir.operation.AbstractOperation;
import com.ibm.watsonhealth.fhir.operation.context.FHIROperationContext;
import com.ibm.watsonhealth.fhir.operation.exception.FHIROperationException;
import com.ibm.watsonhealth.fhir.rest.FHIRResourceHelpers;

/**
 * This class will test what happens if there is a bad OperationDefinition defined for a custom operation.
 * There is no corresponding testcase as the Java ServiceLoader (SPI) mechanism 
 * will automatically load this service if it is configured as a service provider and available on the classpath.
 * The expected result is:
 * 1. to see an error/message explaining why this service was not loaded
 * 2. for other operations to continue working
 * @author lmsurpre
 */
public class BadOperation extends AbstractOperation {
    @Override
    protected OperationDefinition buildOperationDefinition() {
        try {
            InputStream in = getClass().getClassLoader().getResourceAsStream("operationdefinition-bad.json");
            return FHIRUtil.read(OperationDefinition.class, Format.JSON, in);            
        } catch (Exception e) {
            throw new RuntimeException("Unable to read operationdefinition-bad.json", e);
        }
    }

    @Override
    protected Parameters doInvoke(FHIROperationContext operationContext, Class<? extends Resource> resourceType, String logicalId, String versionId,
        Parameters parameters, FHIRResourceHelpers resourceHelper) throws FHIROperationException {
        // do nothing
        return null;
    }
}
