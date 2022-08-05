/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.task.core.service;

import java.util.concurrent.ExecutorService;

import org.linuxforhealth.fhir.task.api.ITaskCollector;
import org.linuxforhealth.fhir.task.api.ITaskService;
import org.linuxforhealth.fhir.task.core.impl.TaskManager;

/**
 * A simple service to support creation of task collectors

 *
 */
public class TaskService implements ITaskService {

    /* (non-Javadoc)
     * @see org.linuxforhealth.fhir.task.api.ITaskService#makeTaskCollection(java.util.concurrent.ExecutorService)
     */
    @Override
    public ITaskCollector makeTaskCollector(ExecutorService pool) {
        return new TaskManager(pool);
    }
}
