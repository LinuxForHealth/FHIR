/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.client.action;

import javax.ws.rs.core.Response;

import org.apache.http.impl.client.CloseableHttpClient;

import com.ibm.fhir.exception.FHIROperationException;

/**
 * Bulk Data Client Action
 */
public interface BulkDataClientAction extends AutoCloseable {

    /**
     * prepares the action using configuration.
     *
     * @param cli
     * @param batchurl
     * @param batchUser
     * @param batchPass
     *
     * @throws FHIROperationException indicating a failure to close the action's dependent resources
     */
    void prepare(CloseableHttpClient cli, String batchUrl, String batchUser, String batchPass) throws FHIROperationException;

    /**
     * Runs the given client action
     *
     * @param tenant
     * @param job the identifier uniquely identifying the logical id of the job
     *
     *
     * @throws FHIROperationException indicating a failure to run the action
     */
    void run(String tenant, String job) throws FHIROperationException;

    /**
     * get the result
     * @return
     */
    Response.Status getResult();
}