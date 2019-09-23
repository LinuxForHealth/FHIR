/*
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.cli.invoker;

import javax.json.JsonObject;

import com.ibm.fhir.model.resource.Resource;

/**
 * This class is the OperationInvoker implementation for the 'create' operation.
 * 
 * @author padams
 */
public class CreateInvoker extends OperationInvoker {

    /* (non-Javadoc)
     * @see com.ibm.fhir.cli.OperationInvoker#invoke(com.ibm.fhir.cli.InvocationContext)
     */
    @Override
    public void doInvoke(InvocationContext ic) throws Exception {
        Object resource = ic.getRequestResourceWithExcp();
        if (resource instanceof Resource) {
            response = client.create((Resource)resource, requestHeaders);
        } else {
            response = client.create((JsonObject)resource, requestHeaders);
        }
    }
}
