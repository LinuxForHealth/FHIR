/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.task.core.service;

import java.util.concurrent.ExecutorService;

import com.ibm.fhir.task.api.ITaskCollector;
import com.ibm.fhir.task.api.ITaskService;
import com.ibm.fhir.task.core.impl.TaskManager;

/**
 * A simple service to support creation of task collectors

 *
 */
public class TaskService implements ITaskService {

    /* (non-Javadoc)
     * @see com.ibm.fhir.task.api.ITaskService#makeTaskCollection(java.util.concurrent.ExecutorService)
     */
    @Override
    public ITaskCollector makeTaskCollector(ExecutorService pool) {
        return new TaskManager(pool);
    }
}
