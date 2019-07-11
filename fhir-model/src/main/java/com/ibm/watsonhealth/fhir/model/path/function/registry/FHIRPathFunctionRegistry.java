/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.path.function.registry;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

import com.ibm.watsonhealth.fhir.model.path.function.FHIRPathFunction;

public class FHIRPathFunctionRegistry {
    private static final FHIRPathFunctionRegistry INSTANCE = new FHIRPathFunctionRegistry();
    private Map<String, FHIRPathFunction> functionMap = new ConcurrentHashMap<>();
    
    private FHIRPathFunctionRegistry() {
        loadRegistry();
    }
    
    private void loadRegistry() {
        ServiceLoader<FHIRPathFunction> loader = ServiceLoader.load(FHIRPathFunction.class);
        for (FHIRPathFunction function : loader) {
            register(function);    
        }
    }

    public static FHIRPathFunctionRegistry getInstance() {
        return INSTANCE;
    }
    
    public void register(FHIRPathFunction function) {
        functionMap.put(function.getName(), function);
    }
    
    public FHIRPathFunction getFunction(String functionName, int arity) {
        FHIRPathFunction function = functionMap.get(functionName);
        if (function == null) {
            throw new IllegalArgumentException("Function: '" + functionName + "' not found");
        }
        if (arity >= function.getMinArity() && arity <= function.getMaxArity()) {
            return function;
        }
        throw new IllegalArgumentException("Unexpected number of arguments: " + arity + " for function: '" + functionName + "'");
    }
}
