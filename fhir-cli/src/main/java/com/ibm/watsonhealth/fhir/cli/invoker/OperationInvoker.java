/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.cli.invoker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.ibm.watsonhealth.fhir.cli.invoker.InvocationContext.NameValuePair;
import com.ibm.watsonhealth.fhir.client.FHIRClient;
import com.ibm.watsonhealth.fhir.client.FHIRClientFactory;
import com.ibm.watsonhealth.fhir.client.FHIRParameters;
import com.ibm.watsonhealth.fhir.client.FHIRRequestHeader;
import com.ibm.watsonhealth.fhir.client.FHIRResponse;

/**
 * This is a base class for all of the various operation invoker classes.
 * 
 * @author padams
 */
public abstract class OperationInvoker {
    protected FHIRClient client;
    protected FHIRResponse response;
    protected FHIRParameters queryParameters;
    protected FHIRRequestHeader[] requestHeaders;
    
    public void invoke(InvocationContext ic) throws Exception {
        preInvoke(ic);
        doInvoke(ic);
        postInvoke(ic);
    }
    
    /**
     * Performs the necessary steps before the operation is invoked.
     */
    public void preInvoke(InvocationContext ic) throws Exception {
        response = null;
        queryParameters = null;
        Properties properties = loadClientProperties(ic.getPropertiesFile());
        client = getClient(properties);

        // If query parameters were specified on the command line, 
        // then create a FHIRParameters object and store them in it.
        List<NameValuePair> params = ic.getQueryParameters();
        if (params != null && !params.isEmpty()) {
            queryParameters = new FHIRParameters();
            for (NameValuePair param : params) {
                queryParameters.addMultivaluedParameter(param.getName(), param.getValue());
            }
        }
        
        List<NameValuePair> headers = ic.getHeaders();
        if (headers != null && !headers.isEmpty()) {
            requestHeaders = new FHIRRequestHeader[headers.size()];
            for (int i = 0; i < headers.size(); i++) {
                NameValuePair header = headers.get(i);
                requestHeaders[i] = new FHIRRequestHeader(header.getName(), header.getValue());
            }
        }
    }

    /**
     * Each subclass will implement this method to invoke the appropriate FHIR Client API,
     * based on the operation requested by the user.
     */
    public abstract void doInvoke(InvocationContext ic) throws Exception;
    
    /**
     * Saves off the relevant response information in the InvocationContext
     * after the operation has been invoked.
     */
    public void postInvoke(InvocationContext ic) throws Exception {
        ic.setResponse(response);
    }
    
    /**
     * Loads the FHIRClient properties from the specified file.
     */
    protected Properties loadClientProperties(String filename) throws Exception {
        Properties props = new Properties();
        
        // Open the properties file specified by the user.
        InputStream is = null;
        File f = new File(filename);
        if (f.exists()) {
            is = new FileInputStream(f);
        } else {
            throw new FileNotFoundException("Properties file '" + filename + "' not found.");
        }
        
        // Load the properties.
        props.load(is);
        is.close();
        
        return props;
    }
    
    /**
     * Retrieves a new FHIRClient instance using the specified properties.
     */
    protected FHIRClient getClient(Properties properties) throws Exception {
        return FHIRClientFactory.getClient(properties);
    }
}
