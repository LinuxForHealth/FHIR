/**
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.operation.context;

import java.util.HashMap;
import java.util.Map;

public class FHIROperationContext {
    public static enum Type { SYSTEM, RESOURCE_TYPE, INSTANCE }

    public static final String PROPNAME_REQUEST_BASE_URI = "REQUEST_BASE_URI";
    public static final String PROPNAME_LOCATION_URI = "LOCATION_URI";

    private Type type = null;
    private Map<String, Object> properties = null;
    
    private FHIROperationContext(Type type) {
        if (type == null) {
            throw new IllegalArgumentException("Context type cannot be null");
        }
        this.type = type;
        properties = new HashMap<String, Object>();
    }
    
    public Type getType() {
        return type;
    }
    
    public void setProperty(String name, Object value) {
        properties.put(name, value);
    }
    
    public Object getProperty(String name) {
        return properties.get(name);
    }
    
    public static FHIROperationContext createSystemOperationContext() {
        return new FHIROperationContext(Type.SYSTEM);
    }
    
    public static FHIROperationContext createResourceTypeOperationContext() {
        return new FHIROperationContext(Type.RESOURCE_TYPE);
    }
    
    public static FHIROperationContext createInstanceOperationContext() {
        return new FHIROperationContext(Type.INSTANCE);
    }
}
