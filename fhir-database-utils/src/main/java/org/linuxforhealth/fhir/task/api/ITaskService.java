/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.task.api;

import java.util.concurrent.ExecutorService;

/**
 * Task Service to manage and wrap access to the ExecutorService
 */
public interface ITaskService {

    public ITaskCollector makeTaskCollector(ExecutorService pool);
}
