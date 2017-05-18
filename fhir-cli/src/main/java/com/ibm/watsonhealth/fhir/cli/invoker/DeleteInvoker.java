/**
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.cli.invoker;

/**
 * This class is the OperationInvoker implementation for the 'delete' operation.
 * 
 * @author padams
 */
public class DeleteInvoker extends OperationInvoker {

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.cli.OperationInvoker#invoke(com.ibm.watsonhealth.fhir.cli.InvocationContext)
     */
    @Override
    public void doInvoke(InvocationContext ic) throws Exception {
        String resourceType = ic.getResourceTypeWithExcp();
        String resourceId = ic.getResourceIdWithExcp();
        
        response = client.read(resourceType, resourceId, requestHeaders);
    }
}
