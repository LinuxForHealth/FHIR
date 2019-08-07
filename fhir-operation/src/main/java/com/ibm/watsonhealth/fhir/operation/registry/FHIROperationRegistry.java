/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.operation.registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

import com.ibm.watsonhealth.fhir.exception.FHIROperationException;
import com.ibm.watsonhealth.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.watsonhealth.fhir.model.type.IssueSeverity;
import com.ibm.watsonhealth.fhir.operation.FHIROperation;
import com.ibm.watsonhealth.fhir.operation.exception.FHIROperationNotFoundException;
import com.ibm.watsonhealth.fhir.model.validation.FHIRValidator;
import com.ibm.watsonhealth.fhir.model.validation.exception.FHIRValidationException;

public class FHIROperationRegistry {
    private final Logger log = Logger.getLogger(FHIROperationRegistry.class.getName());
    private static final FHIROperationRegistry INSTANCE = new FHIROperationRegistry();

    private Map<String, FHIROperation> operationMap = null;

    private FHIROperationRegistry() {
        operationMap = new TreeMap<String, FHIROperation>();
        ServiceLoader<FHIROperation> operations = ServiceLoader.load(FHIROperation.class);
        // https://docs.oracle.com/javase/8/docs/api/java/util/ServiceLoader.html#iterator--
        Iterator<FHIROperation> iterator = operations.iterator();
        while (iterator.hasNext()) {
            String operationName = "unknown name";
            try {
                FHIROperation operation = iterator.next();
                log.fine("Found FHIROperation implementation class: " + operation.getClass().getName());
                operationName = operation.getName();
                if (!isValid(operation)) {
                    log.severe("Operation $" + operationName + " has failed validation and will be skipped.");
                    continue;
                }
                operationMap.put(operation.getName(), operation);
            } catch (ServiceConfigurationError | FHIRValidationException | JAXBException e) {
                log.log(Level.SEVERE, "Unable to validate operation $" + operationName + ". This operation will be skipped.", e);
            }
        }

        if (log.isLoggable(Level.FINE)) {
            log.fine("Discovered " + operationMap.size() + " custom operation implementations:");
            log.fine(operationMap.toString());
        }
    }

    private boolean isValid(FHIROperation operation) throws FHIRValidationException, JAXBException, FHIRValidationException {
        List<Issue> issues = FHIRValidator.validator(operation.getDefinition()).validate();
        if (!issues.isEmpty()) {
            for (Issue issue : issues) {
                log.info("Issue: " + issue.getCode().getValue() + ":" 
            + issue.getSeverity().getValue() + ":" + issue.getDiagnostics());
                if (issue.getSeverity().equals(IssueSeverity.ERROR) 
                        || issue.getSeverity().equals(IssueSeverity.FATAL)) {
                    return false;
                }
            }

        }
        if (operation.getName() == null) {
            return false;
        }
        return true;
    }

    public List<String> getOperationNames() {
        return Collections.unmodifiableList(new ArrayList<String>(operationMap.keySet()));
    }

    public static FHIROperationRegistry getInstance() {
        return INSTANCE;
    }

    public FHIROperation getOperation(String name) throws FHIROperationException {
        FHIROperation operation = operationMap.get(name);
        if (operation == null) {
            throw new FHIROperationNotFoundException("Operation with name: '" + name + "' was not found");
        }
        return operation;
    }
}
