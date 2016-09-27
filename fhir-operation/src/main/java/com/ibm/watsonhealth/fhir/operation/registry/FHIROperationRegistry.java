/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.operation.registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.TreeMap;

import com.ibm.watsonhealth.fhir.operation.FHIROperation;
import com.ibm.watsonhealth.fhir.operation.exception.FHIROperationException;
import com.ibm.watsonhealth.fhir.operation.exception.FHIROperationNotFoundException;

public class FHIROperationRegistry {
    private static final FHIROperationRegistry INSTANCE = new FHIROperationRegistry();
    
    private Map<String, FHIROperation> operationMap = null;
    
    private FHIROperationRegistry() {
        operationMap = new TreeMap<String, FHIROperation>();
        ServiceLoader<FHIROperation> operations = ServiceLoader.load(FHIROperation.class);
        for (FHIROperation operation : operations) {
            operationMap.put(operation.getName(), operation);
        }
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
