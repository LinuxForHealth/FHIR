/**
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.cli.invoker;

import javax.json.JsonObject;

import com.ibm.watsonhealth.fhir.model.resource.Resource;

/**
 * This class is the OperationInvoker implementation for the 'conditional create' operation.
 * 
 * @author padams
 */
public class ConditionalCreateInvoker extends OperationInvoker {

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.cli.OperationInvoker#invoke(com.ibm.watsonhealth.fhir.cli.InvocationContext)
     */
    @Override
    public void doInvoke(InvocationContext ic) throws Exception {
        Object resource = ic.getRequestResourceWithExcp();
        if (resource instanceof Resource) {
            response = client.conditionalCreate((Resource)resource, queryParameters, requestHeaders);
        } else {
            response = client.conditionalCreate((JsonObject)resource, queryParameters, requestHeaders);
        }
    }
}
