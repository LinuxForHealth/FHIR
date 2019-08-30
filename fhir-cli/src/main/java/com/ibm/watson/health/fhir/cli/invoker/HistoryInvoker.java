/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.cli.invoker;

/**
 * This class is the OperationInvoker implementation for the 'history' operation.
 * 
 * @author padams
 */
public class HistoryInvoker extends OperationInvoker {

    /* (non-Javadoc)
     * @see com.ibm.watson.health.fhir.cli.OperationInvoker#invoke(com.ibm.watson.health.fhir.cli.InvocationContext)
     */
    @Override
    public void doInvoke(InvocationContext ic) throws Exception {
        String resourceType = ic.getResourceTypeWithExcp();
        String resourceId = ic.getResourceIdWithExcp();
        
        response = client.history(resourceType, resourceId, queryParameters, requestHeaders);
    }
}
