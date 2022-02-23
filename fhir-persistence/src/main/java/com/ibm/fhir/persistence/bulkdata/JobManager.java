/* 
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.bulkdata;

import java.util.List;

/**
 * 
 */
public interface JobManager {

    /**
     * Creates the Export Job
     * 
     * @param tenant
     * @param datastore
     * @param source
     * @param outcome
     * @param incomingUrl
     * @param extJobId
     * @param since
     * @param types
     * @param exportType
     * @param outputFormatStr
     * @param typeFiltersStr
     * @param groupId
     */
    void createExportJob(String tenant, String datastore, String source, String outcome, String incomingUrl,
            String extJobId, String since, String types, String exportType, String outputFormatStr,
            String typeFiltersStr, String groupId);

    /**
     * creates the Import job
     * 
     * @param tenant
     * @param datastore
     * @param source
     * @param outcome
     * @param incomingUrl
     * @param extJobId
     * @param inputs
     */
    void createImportJob(String tenant, String datastore, String source, String outcome, String incomingUrl,
            String extJobId, List<InputDTO> inputs);

    /**
     * Updates the job with the internal job id based on the ext job id.
     * 
     * @param extJobId
     * @param intJobId
     */
    void updateInternalJobId(String extJobId, String intJobId);

    /**
     * Gets the job based on the Job Id
     * 
     * @param extJobId
     * @return
     */
    String getInternalJobId(String extJobId);
    
    
    JobStatusDTO getStatus(String extJobId);
    
    void updateJobStatus(String extJobId);
}