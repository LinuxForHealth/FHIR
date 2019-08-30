/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.cli.invoker;

import com.ibm.watson.health.fhir.model.resource.Bundle;

/**
 * This class is the OperationInvoker implementation for the 'batch' operation.
 * 
 * @author padams
 */
public class BatchInvoker extends OperationInvoker {

    /* (non-Javadoc)
     * @see com.ibm.watson.health.fhir.cli.OperationInvoker#invoke(com.ibm.watson.health.fhir.cli.InvocationContext)
     */
    @Override
    public void doInvoke(InvocationContext ic) throws Exception {
        Object resource = ic.getRequestResourceWithExcp();
        if (resource instanceof Bundle) {
            response = client.batch((Bundle)resource, requestHeaders);
        } else {
            throw new IllegalArgumentException("Input resource must be a Bundle.");
        }
    }
}
