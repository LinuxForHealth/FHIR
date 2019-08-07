/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.operation.document;

import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibm.watsonhealth.fhir.exception.FHIROperationException;
import com.ibm.watsonhealth.fhir.model.format.Format;
import com.ibm.watsonhealth.fhir.model.parser.FHIRParser;
import com.ibm.watsonhealth.fhir.model.resource.Bundle;
import com.ibm.watsonhealth.fhir.model.resource.Bundle.Entry;
import com.ibm.watsonhealth.fhir.model.resource.Composition;
import com.ibm.watsonhealth.fhir.model.resource.OperationDefinition;
import com.ibm.watsonhealth.fhir.model.resource.Parameters;
import com.ibm.watsonhealth.fhir.model.resource.Parameters.Parameter;
import com.ibm.watsonhealth.fhir.model.resource.Resource;
import com.ibm.watsonhealth.fhir.model.type.BundleType;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.operation.AbstractOperation;
import com.ibm.watsonhealth.fhir.operation.context.FHIROperationContext;
import com.ibm.watsonhealth.fhir.operation.util.FHIROperationUtil;
import com.ibm.watsonhealth.fhir.rest.FHIRResourceHelpers;
import com.ibm.watsonhealth.fhir.rest.FHIRRestOperationResponse;

public class DocumentOperation extends AbstractOperation {
    @Override
    protected OperationDefinition buildOperationDefinition() {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("document.json");){
            return FHIRParser.parser(Format.JSON).parse(in);            
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    @Override
    protected Parameters doInvoke(FHIROperationContext operationContext, Class<? extends Resource> resourceType, String logicalId, String versionId, Parameters parameters,
        FHIRResourceHelpers resourceHelper) throws FHIROperationException {
        try {
            Composition composition = null;
            
            Resource resource = resourceHelper.doRead("Composition", logicalId, false, false, null, null);
            if (resource == null) {
                throw new FHIROperationException("Could not find composition with id: " + logicalId);
            }
            
            composition = (Composition) resource;
            
            Bundle bundle = buildDocument(operationContext, composition, resourceHelper);            
            Parameter persistParameter = getParameter(parameters, "persist");
            
            if (persistParameter != null) {
                com.ibm.watsonhealth.fhir.model.type.Boolean persistParameterValue = persistParameter.getValue().as(com.ibm.watsonhealth.fhir.model.type.Boolean.class); //getValueBoolean();
                
                if (persistParameterValue != null) {
                    boolean persist = false;
                    
                    if (persistParameterValue.getValue() != null) {
                        persist = persistParameterValue.getValue();
                    }
                    
                    if (persist) {
                        // FHIRResourceHelper resourceHelper = (FHIRResourceHelper) operationContext.getProperty(FHIROperationContext.PROPNAME_RESOURCE_HELPER;
                        FHIRRestOperationResponse response = resourceHelper.doCreate("Bundle", bundle, null, null);
                        URI locationURI = response.getLocationURI();
                        operationContext.setProperty(FHIROperationContext.PROPNAME_LOCATION_URI, locationURI);
                    }
                }
            }
            
            return FHIROperationUtil.getOutputParameters(bundle);
        } catch (FHIROperationException e) {
            throw e;
        } catch (Exception e) {
            throw new FHIROperationException("An error occurred during the document operation", e);
        }
    }
    
    private Bundle buildDocument(FHIROperationContext operationContext, Composition composition, FHIRResourceHelpers resourceHelper) throws Exception {
        //Bundle document = factory.createBundle();
        //document.setType(factory.createBundleType().withValue(BundleTypeList.DOCUMENT));
        
        Bundle.Builder documentBuilder = Bundle.builder().type(BundleType.DOCUMENT);
        
        // the composition is the first bundle entry in the document
        Bundle.Entry.Builder entryBuilder = Entry.builder(); //createBundleEntry();
        //ResourceContainer container = factory.createResourceContainer();
        //container.setComposition(composition);
        //bundleEntry.setResource(container);

        //ResourceContainer container = factory.createResourceContainer();
        //FHIRUtil.setResourceContainerResource(container, resource);
        //bundleEntry.setResource(composition);
        entryBuilder.resource(composition);
       
        setFullUrl(operationContext, entryBuilder, "Composition/" + composition.getId().getValue());
        
        //document.getEntry().add(bundleEntry);
        documentBuilder.entry(entryBuilder.build());
        
        Map<String, Resource> resources = new HashMap<String, Resource>();
        
        // Composition.subject
        addBundleEntry(operationContext, documentBuilder, composition.getSubject(), resourceHelper, resources);
        
        // Composition.author
        for (Reference author : composition.getAuthor()) {
            addBundleEntry(operationContext, documentBuilder, author, resourceHelper, resources);
        }
        
        // Composition.attester.party
        for (Composition.Attester attester : composition.getAttester()) {
            addBundleEntry(operationContext, documentBuilder, attester.getParty(), resourceHelper, resources);
        }
        
        // Composition.custodian
        addBundleEntry(operationContext, documentBuilder, composition.getCustodian(), resourceHelper, resources);
        
        // Composition.event.detail
        for (Composition.Event event : composition.getEvent()) {
            for (Reference detail : event.getDetail()) {
                addBundleEntry(operationContext, documentBuilder, detail, resourceHelper, resources);
            }
        }
        
        // Composition.encounter
        addBundleEntry(operationContext, documentBuilder, composition.getEncounter(), resourceHelper, resources);
        
        // Composition.section.entry
        addBundleEntries(operationContext, documentBuilder, composition.getSection(), resourceHelper, resources);
        
        return documentBuilder.build();
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
    
    public static Uri uri(String uri) {
        return Uri.builder().value(uri).build();
    }
}
