/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.operation.document;

import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.uri;

import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibm.watsonhealth.fhir.model.Bundle;
import com.ibm.watsonhealth.fhir.model.BundleEntry;
import com.ibm.watsonhealth.fhir.model.BundleTypeList;
import com.ibm.watsonhealth.fhir.model.Composition;
import com.ibm.watsonhealth.fhir.model.CompositionAttester;
import com.ibm.watsonhealth.fhir.model.CompositionEvent;
import com.ibm.watsonhealth.fhir.model.CompositionSection;
import com.ibm.watsonhealth.fhir.model.OperationDefinition;
import com.ibm.watsonhealth.fhir.model.Parameters;
import com.ibm.watsonhealth.fhir.model.ParametersParameter;
import com.ibm.watsonhealth.fhir.model.Reference;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.model.ResourceContainer;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil.Format;
import com.ibm.watsonhealth.fhir.operation.AbstractOperation;
import com.ibm.watsonhealth.fhir.operation.context.FHIROperationContext;
import com.ibm.watsonhealth.fhir.operation.exception.FHIROperationException;
import com.ibm.watsonhealth.fhir.operation.util.FHIROperationUtil;
import com.ibm.watsonhealth.fhir.rest.FHIRResourceHelpers;
import com.ibm.watsonhealth.fhir.rest.FHIRRestOperationResponse;

public class DocumentOperation extends AbstractOperation {
    @Override
    protected OperationDefinition buildOperationDefinition() {
        try {
            InputStream in = getClass().getClassLoader().getResourceAsStream("document.json");
            return FHIRUtil.read(OperationDefinition.class, Format.JSON, in);            
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
            ParametersParameter persistParameter = getParameter(parameters, "persist");
            
            if (persistParameter != null) {
                com.ibm.watsonhealth.fhir.model.Boolean persistParameterValue = persistParameter.getValueBoolean();
                
                if (persistParameterValue != null) {
                    boolean persist = false;
                    
                    if (persistParameterValue.isValue() != null) {
                        persist = persistParameterValue.isValue();
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
        Bundle document = factory.createBundle();
        document.setType(factory.createBundleType().withValue(BundleTypeList.DOCUMENT));
        
        // the composition is the first bundle entry in the document
        BundleEntry bundleEntry = factory.createBundleEntry();
        ResourceContainer container = factory.createResourceContainer();
        
        container.setComposition(composition);
        
        bundleEntry.setResource(container);
        setFullUrl(operationContext, bundleEntry, "Composition/" + composition.getId().getValue());
        
        document.getEntry().add(bundleEntry);
        
        Map<String, Resource> resources = new HashMap<String, Resource>();
        
        // Composition.subject
        addBundleEntry(operationContext, document, composition.getSubject(), resourceHelper, resources);
        
        // Composition.author
        for (Reference author : composition.getAuthor()) {
            addBundleEntry(operationContext, document, author, resourceHelper, resources);
        }
        
        // Composition.attester.party
        for (CompositionAttester attester : composition.getAttester()) {
            addBundleEntry(operationContext, document, attester.getParty(), resourceHelper, resources);
        }
        
        // Composition.custodian
        addBundleEntry(operationContext, document, composition.getCustodian(), resourceHelper, resources);
        
        // Composition.event.detail
        for (CompositionEvent event : composition.getEvent()) {
            for (Reference detail : event.getDetail()) {
                addBundleEntry(operationContext, document, detail, resourceHelper, resources);
            }
        }
        
        // Composition.encounter
        addBundleEntry(operationContext, document, composition.getEncounter(), resourceHelper, resources);
        
        // Composition.section.entry
        addBundleEntries(operationContext, document, composition.getSection(), resourceHelper, resources);
        
        return document;
    }

    private void addBundleEntry(FHIROperationContext operationContext, Bundle document, Reference reference, FHIRResourceHelpers resourceHelper, Map<String, Resource> resources) throws Exception {;
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
            BundleEntry bundleEntry = factory.createBundleEntry();
            ResourceContainer container = factory.createResourceContainer();
            
            FHIRUtil.setResourceContainerResource(container, resource);
            
            bundleEntry.setResource(container);
            setFullUrl(operationContext, bundleEntry, referenceValue);
            
            document.getEntry().add(bundleEntry);
        }
    }

    private void addBundleEntries(FHIROperationContext operationContext, Bundle document, List<CompositionSection> sections, FHIRResourceHelpers resourceHelper, Map<String, Resource> resources) throws Exception {
        for (CompositionSection section : sections) {                
            // process entries for this section
            for (Reference entry : section.getEntry()) {
                addBundleEntry(operationContext, document, entry, resourceHelper, resources);
            }
            
            // process subsections
            addBundleEntries(operationContext, document, section.getSection(), resourceHelper, resources);
        }
    }
    
    private void setFullUrl(FHIROperationContext operationContext, BundleEntry bundleEntry, String referenceValue) {
        String requestBaseURI = (String) operationContext.getProperty(FHIROperationContext.PROPNAME_REQUEST_BASE_URI);
        if (requestBaseURI != null) {
            bundleEntry.setFullUrl(uri(requestBaseURI + "/" + referenceValue));
        }
    }
}
