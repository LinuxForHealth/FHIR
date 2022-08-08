/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.task.api;

/**
 * Manage access to the tasks
 */
public interface ITaskGroup {

    // Get the unique identifier for this task group
    String getTaskId();

    /**
     * Add the given parent as one of the parents of this task group
     * @param parent
     */
    void addParent(ITaskGroup parent);

    /**
     * Called when a child task completes.
     * @param taskGroup
     */
    void taskCompletionCallback(ITaskGroup taskGroup);

}
