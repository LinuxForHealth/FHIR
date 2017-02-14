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
import com.ibm.watsonhealth.fhir.persistence.FHIRPersistence;
import com.ibm.watsonhealth.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.watsonhealth.fhir.persistence.context.FHIRPersistenceContextFactory;

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
        FHIRPersistence persistence) throws FHIROperationException {
        try {
            FHIRPersistenceContext context = FHIRPersistenceContextFactory.createPersistenceContext(null);
            Composition composition = (Composition) persistence.read(context, Composition.class, logicalId);            
            
            if (composition == null) {
                throw new FHIROperationException("Could not find composition with id: " + logicalId);
            }
            
            Bundle bundle = buildDocument(operationContext, composition, persistence);            
            ParametersParameter persistParameter = getParameter(parameters, "persist");
            
            if (persistParameter != null) {
                com.ibm.watsonhealth.fhir.model.Boolean persistParameterValue = persistParameter.getValueBoolean();
                
                if (persistParameterValue != null) {
                    boolean persist = false;
                    
                    if (persistParameterValue.isValue() != null) {
                        persist = persistParameterValue.isValue();
                    }
                    
                    if (persist) {
                        persistence.create(context, bundle);
                        URI locationURI = FHIRUtil.buildLocationURI(FHIRUtil.getResourceTypeName(bundle), bundle);
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
    
    private Bundle buildDocument(FHIROperationContext operationContext, Composition composition, FHIRPersistence persistence) throws Exception {
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
        addBundleEntry(operationContext, document, composition.getSubject(), persistence, resources);
        
        // Composition.author
        for (Reference author : composition.getAuthor()) {
            addBundleEntry(operationContext, document, author, persistence, resources);
        }
        
        // Composition.attester.party
        for (CompositionAttester attester : composition.getAttester()) {
            addBundleEntry(operationContext, document, attester.getParty(), persistence, resources);
        }
        
        // Composition.custodian
        addBundleEntry(operationContext, document, composition.getCustodian(), persistence, resources);
        
        // Composition.event.detail
        for (CompositionEvent event : composition.getEvent()) {
            for (Reference detail : event.getDetail()) {
                addBundleEntry(operationContext, document, detail, persistence, resources);
            }
        }
        
        // Composition.encounter
        addBundleEntry(operationContext, document, composition.getEncounter(), persistence, resources);
        
        // Composition.section.entry
        addBundleEntries(operationContext, document, composition.getSection(), persistence, resources);
        
        return document;
    }

    private void addBundleEntry(FHIROperationContext operationContext, Bundle document, Reference reference, FHIRPersistence persistence, Map<String, Resource> resources) throws Exception {;
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
            
            FHIRPersistenceContext context = FHIRPersistenceContextFactory.createPersistenceContext(null);
            resource = persistence.read(context, FHIRUtil.getResourceType(resourceTypeName), logicalId);
            
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

    private void addBundleEntries(FHIROperationContext operationContext, Bundle document, List<CompositionSection> sections, FHIRPersistence persistence, Map<String, Resource> resources) throws Exception {
        for (CompositionSection section : sections) {                
            // process entries for this section
            for (Reference entry : section.getEntry()) {
                addBundleEntry(operationContext, document, entry, persistence, resources);
            }
            
            // process subsections
            addBundleEntries(operationContext, document, section.getSection(), persistence, resources);
        }
    }
    
    private void setFullUrl(FHIROperationContext operationContext, BundleEntry bundleEntry, String referenceValue) {
        String requestBaseURI = (String) operationContext.getProperty(FHIROperationContext.PROPNAME_REQUEST_BASE_URI);
        if (requestBaseURI != null) {
            bundleEntry.setFullUrl(uri(requestBaseURI + "/" + referenceValue));
        }
    }
}
