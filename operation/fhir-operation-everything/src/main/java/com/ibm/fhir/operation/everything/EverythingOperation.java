/*
 * (C) Copyright IBM Corp. 2021
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ibm.fhir.operation.everything;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import javax.ws.rs.core.MultivaluedHashMap;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.parser.exception.FHIRParserException;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.model.resource.OperationDefinition;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.UnsignedInt;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BundleType;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.search.compartment.CompartmentUtil;
import com.ibm.fhir.search.exception.FHIRSearchException;
import com.ibm.fhir.server.operation.spi.AbstractOperation;
import com.ibm.fhir.server.operation.spi.FHIROperationContext;
import com.ibm.fhir.server.operation.spi.FHIRResourceHelpers;
import com.ibm.fhir.server.util.FHIROperationUtil;


/**
 * This class implements the <a href="https://www.hl7.org/fhir/operation-patient-everything.html">$everything</a> operation
 * which is used to return all the information related to one or more patients described in the resource or context on 
 * which this operation is invoked.
 * 
 * @author Luis A. García
 */
public class EverythingOperation extends AbstractOperation {
    
    private static final Logger LOG = java.util.logging.Logger.getLogger(EverythingOperation.class.getName());

    private static final String PATIENT = "Patient";

    private static final String OPERATION_DEFINITION_FILE = "everything.json";
    
    private List<String> includedResourceTypes;
    
    /**
     * 
     */
    public EverythingOperation() {
        try {
            includedResourceTypes = getIncludedResources();
        } catch (FHIRSearchException e) {
            throw new Error("There has been an error retrieving the list of included resources of the $everything operation.", e);
        }
    }
    
    /* (non-Javadoc)
     * @see com.ibm.fhir.server.operation.spi.AbstractOperation#buildOperationDefinition()
     */
    @Override
    protected OperationDefinition buildOperationDefinition() {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(OPERATION_DEFINITION_FILE)) {
            return FHIRParser.parser(Format.JSON).parse(in);
        } catch (FHIRParserException e) {
            throw new Error("There has been a parsing error in the operation definition of the $everything operation.", e);
        } catch (IOException e) {
            throw new Error("There has been an IO error reading the operation definition of the $everything operation.", e);
        }
    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.server.operation.spi.AbstractOperation#doInvoke(com.ibm.fhir.server.operation.spi.FHIROperationContext, java.lang.Class, java.lang.String, java.lang.String, com.ibm.fhir.model.resource.Parameters, com.ibm.fhir.server.operation.spi.FHIRResourceHelpers)
     */
    @Override
    protected Parameters doInvoke(FHIROperationContext operationContext, Class<? extends Resource> resourceType, String logicalId, String versionId, Parameters parameters, FHIRResourceHelpers resourceHelper) throws FHIROperationException {
        LOG.entering(this.getClass().getName(), "doInvoke");

        Patient patient = null;
        try {
            patient = (Patient) resourceHelper.doRead(PATIENT, logicalId, false, false, null, null);
        } catch (Exception e) {
            FHIROperationException exceptionWithIssue = buildExceptionWithIssue("An unexpected error occurred while reading patient " + logicalId, IssueType.EXCEPTION);
            LOG.throwing(this.getClass().getName(), "doInvoke", exceptionWithIssue);
            throw exceptionWithIssue;
        }
        if (patient == null) {
            FHIROperationException exceptionWithIssue = buildExceptionWithIssue("Patient with ID " + logicalId + " does not exist.", IssueType.NOT_FOUND);
            LOG.throwing(this.getClass().getName(), "doInvoke", exceptionWithIssue);
            throw exceptionWithIssue;
        }

        Entry patientEntry = buildPatientEntry(operationContext, patient);
        List<Entry> allEntries = new ArrayList<>(1000);
        allEntries.add(patientEntry);
        MultivaluedHashMap<String, String> queryParameters = new MultivaluedHashMap<String, String>();
        queryParameters.add("_count", "500");
        for (String compartmentType : includedResourceTypes) {
            Bundle results = null;
            try {
                results = resourceHelper.doSearch(compartmentType, PATIENT, logicalId, queryParameters, null, null, null);
            } catch (Exception e) {
                e.printStackTrace();
                LOG.warning("Error retrieving $everything resources of type " + compartmentType + " for patient " + logicalId);
                continue;
            }
            allEntries.addAll(results.getEntry());
        }
        
        Bundle.Builder bundleBuilder = Bundle.builder()
                .type(BundleType.SEARCHSET)
                .id(UUID.randomUUID().toString())
                .entry(allEntries)
                .total(UnsignedInt.of(allEntries.size()));        
        
        Parameters outputParameters;
        try {
            outputParameters = FHIROperationUtil.getOutputParameters(bundleBuilder.build());
        } catch (Exception e) {
            FHIROperationException exceptionWithIssue = buildExceptionWithIssue("An unexpected error occurred while creating the operation output parameters for the resulting Bundle.", IssueType.EXCEPTION);
            LOG.throwing(this.getClass().getName(), "doInvoke", exceptionWithIssue);
            throw exceptionWithIssue;
        }
        LOG.exiting(this.getClass().getName(), "doInvoke", outputParameters);
        return outputParameters;
    }

    /**
     * @return the list of patient subresources that will be included in the $everything operaetion
     * @throws FHIRSearchException 
     */
    private List<String> getIncludedResources() throws FHIRSearchException {
        List<String> resourceTypes = new ArrayList<>(CompartmentUtil.getCompartmentResourceTypes(PATIENT));
        // TODO: Practitioner and Organization are not included in the getCompartmentReourceTypes() by default but it seems
        // like a couple of good additional resources to include and they are even mentioned as examples of resources
        // to include in the docs: https://www.hl7.org/fhir/operation-patient-everything.html 
        // resourceTypes.add(Practitioner.class.getSimpleName());
        // resourceTypes.add(Organization.class.getSimpleName());
        return resourceTypes;
    }

    /**
     * Builds an {@link Entry} out of the given {@link Patient} resource including its fullURL
     * 
     * @param operationContext the {@link FHIROperationContext} to get the base URI 
     * @param patient the patient to wrap
     * @return the entry with URL
     */
    private Entry buildPatientEntry(FHIROperationContext operationContext, Patient patient) {
        Uri patientURL = uri(operationContext, PATIENT + "/" + patient.getId());
        Entry patientEntry = Entry.builder()
                .resource(patient)
                .fullUrl(patientURL)
                .build();
        return patientEntry;
    }

    /**
     * Builds a URI with the base URI from the given {@link FHIROperationContext} and then provided URI path.
     * 
     * @param operationContext the {@link FHIROperationContext} to get the base URI 
     * @param uriPath the path to append to the base URI
     * @return the {@link Uri}
     */
    private static Uri uri(FHIROperationContext operationContext, String uriPath) {
        String requestBaseURI = (String) operationContext.getProperty(FHIROperationContext.PROPNAME_REQUEST_BASE_URI);
        return Uri.builder()
                .value(requestBaseURI + "/" + uriPath)
                .build();
    }
}
