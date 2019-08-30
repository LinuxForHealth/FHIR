/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.task.api;

import java.util.concurrent.ExecutorService;

/**
 * @author rarnold
 *
 */
public interface ITaskService {

    public ITaskCollector makeTaskCollector(ExecutorService pool);
}
