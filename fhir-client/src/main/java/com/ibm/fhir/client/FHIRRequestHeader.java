/*
 * (C) Copyright IBM Corp. 2016,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.client;

/**
 * This class represents an HTTP request header that will be used as part of 
 * a FHIRClient operation invocation.
 * 
 * @author padams
 */
public class FHIRRequestHeader {
    private String name;
    private Object value;
    
    public FHIRRequestHeader() {
    }
    
    public FHIRRequestHeader(String name, Object value) {
        setName(name);
        setValue(value);
    }

    /**
     * Returns the name of the request header.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the request header.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the value of the request header.
     */
    public Object getValue() {
        return value;
    }

    /**
     * Sets the value of the request header.
     */
    public void setValue(Object value) {
        this.value = value;
    }
    
    /**
     * This static method can be used as a shortcut for instantiating a new FHIRRequestHeader.
     * Example:
     * <pre>
     *      import static com.ibm.fhir.client.FHIRRequestHeader.header;
     *      
     *      ...
     *      
     *      response = client.create(myPatient, header("Header1","value1"), header("Header2","value2"));
     * </pre> 
     * 
     * @param name the name of the request header 
     * @param value the value of the request header
     * @return a new FHIRRequestHeader instance
     */
    public static FHIRRequestHeader header(String name, Object value) {
        return new FHIRRequestHeader(name, value);
    }
}
