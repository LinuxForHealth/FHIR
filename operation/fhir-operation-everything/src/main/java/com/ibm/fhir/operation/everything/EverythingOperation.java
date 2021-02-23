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

import static com.ibm.fhir.model.type.String.string;

import java.io.IOException;
import java.io.InputStream;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.parser.exception.FHIRParserException;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.model.resource.Composition;
import com.ibm.fhir.model.resource.OperationDefinition;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BundleType;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.search.compartment.CompartmentUtil;
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

    private static final String OPERATION_DEFINITION = "everything.json";

    /* (non-Javadoc)
     * @see com.ibm.fhir.server.operation.spi.AbstractOperation#buildOperationDefinition()
     */
    @Override
    protected OperationDefinition buildOperationDefinition() {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(OPERATION_DEFINITION)) {
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
        List<String> resourceTypes = CompartmentUtil.getCompartmentResourceTypes("Patient");
        System.out.println("***********: " + resourceTypes);
        Patient patient = null;
        try {
            patient = (Patient) resourceHelper.doRead("Patient", logicalId, false, false, null, null);
        } catch (Exception e) {
            throw buildExceptionWithIssue("An unexpected error occurred while reading patient " + logicalId, IssueType.EXCEPTION);
        }
        if (patient == null) {
            throw buildExceptionWithIssue("Patient with ID " + logicalId + " does not exist.", IssueType.NOT_FOUND);
        }

        Bundle.Builder bundleBuilder = Bundle.builder().type(BundleType.SEARCHSET);
        Bundle.Entry.Builder entryBuilder = Entry.builder();
        entryBuilder.resource(patient);
        setFullUrl(operationContext, entryBuilder, "Patient/" + patient.getId());
        bundleBuilder.entry(entryBuilder.build());
        
        Map<String, Resource> resources = new HashMap<String, Resource>();
        
//        // Composition.subject
//        addBundleEntry(operationContext, documentBuilder, composition.getSubject(), resourceHelper, resources);
//        
//        // Composition.author
//        for (Reference author : composition.getAuthor()) {
//            addBundleEntry(operationContext, documentBuilder, author, resourceHelper, resources);
//        }
//        
//        // Composition.attester.party
//        for (Composition.Attester attester : composition.getAttester()) {
//            addBundleEntry(operationContext, documentBuilder, attester.getParty(), resourceHelper, resources);
//        }
//        
//        // Composition.custodian
//        addBundleEntry(operationContext, documentBuilder, composition.getCustodian(), resourceHelper, resources);
//        
//        // Composition.event.detail
//        for (Composition.Event event : composition.getEvent()) {
//            for (Reference detail : event.getDetail()) {
//                addBundleEntry(operationContext, documentBuilder, detail, resourceHelper, resources);
//            }
//        }
//        
//        // Composition.encounter
//        addBundleEntry(operationContext, documentBuilder, composition.getEncounter(), resourceHelper, resources);
//        
//        // Composition.section.entry
//        addBundleEntries(operationContext, documentBuilder, composition.getSection(), resourceHelper, resources);
        
        
        Bundle bundle = bundleBuilder.timestamp(Instant.now(ZoneOffset.UTC))
                .identifier(Identifier.builder()
                        .system(Uri.of("http://hl7.org/fhir/OperationDefinition/Composition-document")).value(string("urn:uuid:" + UUID.randomUUID().toString()))
                        .build())
                .meta(Meta.builder().lastUpdated(Instant.now(ZoneOffset.UTC)).build())
                .build();
        
//
//        URI locationURI = response.getLocationURI();
//        operationContext.setProperty(FHIROperationContext.PROPNAME_LOCATION_URI, locationURI);
//        FHIRRestOperationResponse response = resourceHelper.doCreate("Bundle", bundle, null, null, false);
//        
        Parameters outputParameters;
        try {
            outputParameters = FHIROperationUtil.getOutputParameters(bundle);
        } catch (Exception e) {
            throw buildExceptionWithIssue("An unexpected error occurred while creating the operation output parameters for the resulting Bundle.", IssueType.EXCEPTION);
        }
        return outputParameters;
    }
    

    private void addBundleEntry(FHIROperationContext operationContext, Bundle.Builder documentBuilder, Reference reference, FHIRResourceHelpers resourceHelper, Map<String, Resource> resources) throws Exception {;
        if (reference == null) {
            return;
        }
        
        if (reference.getReference() == null) {
            throw new FHIROperationException("Empty reference object is not allowed");
        }
    
        String referenceValue = reference.getReference().getValue();
        if (referenceValue == null) {
            throw new FHIROperationException("Empty reference value is not allowed");
        }
        
        Resource resource = resources.get(referenceValue);
        
        if (resource == null) {
            String[] referenceTokens = referenceValue.split("/");
            
            // assumption: references will be relative {resourceTypeName}/{logicalId}
            if (referenceTokens.length != 2) {
                throw new FHIROperationException("Could not parse reference value: " + referenceValue);
            }
            
            String resourceTypeName = referenceTokens[0];
            String logicalId = referenceTokens[1];
            
            resource = resourceHelper.doRead(resourceTypeName, logicalId, false, false, null, null);
            
            if (resource == null) {
                throw new FHIROperationException("Could not find resource for reference value: " + referenceValue);
            }
            
            resources.put(referenceValue, resource);
            
            // create a bundle entry for the resource
            //BundleEntry bundleEntry = factory.createBundleEntry();
            
            //ResourceContainer container = factory.createResourceContainer();
            //FHIRUtil.setResourceContainerResource(container, resource);
            
            Bundle.Entry.Builder entryBuilder = Entry.builder(); //createBundleEntry();
            entryBuilder.resource(resource);
            
            setFullUrl(operationContext, entryBuilder, referenceValue);
            
            //document.getEntry().add(bundleEntry);
            documentBuilder.entry(entryBuilder.build());
        }
    }

    private void addBundleEntries(FHIROperationContext operationContext, Bundle.Builder documentBuilder, List<Composition.Section> sections, FHIRResourceHelpers resourceHelper, Map<String, Resource> resources) throws Exception {
        for (Composition.Section section : sections) {                
            // process entries for this section
            for (Reference entry : section.getEntry()) {
                addBundleEntry(operationContext, documentBuilder, entry, resourceHelper, resources);
            }
            
            // process subsections
            addBundleEntries(operationContext, documentBuilder, section.getSection(), resourceHelper, resources);
        }
    }
    
    private void setFullUrl(FHIROperationContext operationContext, Bundle.Entry.Builder entryBuilder, String referenceValue) {
        String requestBaseURI = (String) operationContext.getProperty(FHIROperationContext.PROPNAME_REQUEST_BASE_URI);
        if (requestBaseURI != null) {
            entryBuilder.fullUrl(uri(requestBaseURI + "/" + referenceValue));
        }
    }
    
    private static Uri uri(String uri) {
        return Uri.builder().value(uri).build();
    }

}
