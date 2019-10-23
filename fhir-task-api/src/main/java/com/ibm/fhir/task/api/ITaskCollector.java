/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.task.api;

import java.util.Collection;
import java.util.List;

/**
 * Manage the Tasks
 */
public interface ITaskCollector {

    /**
     * Add the given task to the task group
     * 
     * @param taskId the parent to tell when we're done. Can be null
     * @param r the runnable to call to process the task
     * @param children 
     * @return
     */
    public ITaskGroup makeTaskGroup(String taskId, Runnable r, List<ITaskGroup> children);

    /**
     * Process the tasks, starting with the childless leaf tasks, waiting for everything
     * to complete
     */
    public void startAndWait();

    /**
     * Return a list of all the {@link ITaskGroup} objects which ran but failed
     * This list does not contain any task groups which were not started (because
     * they have dependencies on task groups which failed).
     * @return
     */
    public Collection<ITaskGroup> getFailedTaskGroups();

}
