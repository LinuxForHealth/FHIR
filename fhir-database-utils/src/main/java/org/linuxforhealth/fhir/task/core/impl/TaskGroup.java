/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.task.core.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.task.api.ITaskGroup;

/**
 * The TaskGroup to organize a set of ongoing or pending tasks. 
 */
public class TaskGroup implements ITaskGroup {
    private static final Logger logger = Logger.getLogger(TaskGroup.class.getName());

    // to internalize synchronization
    private final Object monitor = new Object();

    private final String taskId;

    // The task manager processing the tasks
    private final TaskManager taskManager;

    // the set of child tasks we're waiting to be completed before we can start
    private final Set<ITaskGroup> remainingTasks = new HashSet<>();

    // The set of task groups depending on this object
    private final Set<ITaskGroup> parents = new HashSet<>();

    // The task we will run run when it's our turn
    private final Runnable task;

    /**
     * Public constructor
     *
     * @param taskId
     * @param tmgr
     * @param task
     */
    public TaskGroup(String taskId, TaskManager tmgr, Runnable task) {
        this.taskId = taskId;
        this.taskManager = tmgr;
        this.task = task;
    }

    @Override
    public String getTaskId() {
        return this.taskId;
    }

    @Override
    public int hashCode() {
        return this.taskId.hashCode();
    }

    @Override
    public String toString() {
        return this.taskId;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            throw new IllegalArgumentException("Object other is null");
        }

        if (other instanceof TaskGroup) {
            TaskGroup that = (TaskGroup)other;
            return this.taskId.equals(that.taskId);
        }
        else {
            throw new IllegalArgumentException("Object other is not a " + this.getClass().getName());
        }
    }

    /**
     * Add the given {@link ITaskGroup} to the list of task groups
     * depending on this task group. These task groups will be notified
     * when we are completed.
     * @param tg
     */
    public void addDependsOnThis(ITaskGroup tg) {
        this.parents.add(tg);
    }

    @Override
    public void taskCompletionCallback(ITaskGroup tg) {
        boolean empty;
        synchronized(monitor) {
            if (!remainingTasks.remove(tg)) {
                throw new IllegalStateException("TaskGroup is not a child: " + tg.toString());
            }
            empty = remainingTasks.isEmpty();
        }

        // When our last child (dependency) completes, we can submit ourselves
        if (empty) {
            taskManager.submit(this);
        }
    }

    /**
     * Add the list of children to this task group, and make sure
     * each of those children know what we are its parent
     * @param children
     */
    public void addChildTaskGroups(List<ITaskGroup> children) {
        remainingTasks.addAll(children);

        // Make sure that each child knows that this is one of their parents
        for (ITaskGroup child: children) {
            child.addParent(this);
        }
    }

    /**
     * Submit this task to the thread-pool
     * @param pool
     */
    public void runTask(ExecutorService pool) {
        pool.submit(() -> runTask());
    }

    /**
     * Called when the ExecutorService executes this task
     */
    public void runTask() {
        try {
            this.task.run();

            // Tell all the tasks waiting on us that we're done. When the final
            // dependency is met, that task will then submit itself to the thread-pool
            // (via the task manager)
            synchronized(monitor) {
                for (ITaskGroup tg: this.parents) {
                    tg.taskCompletionCallback(this);
                }
            }

            // It's important we tell the taskManager we're done *after* we have told
            // our parent task groups. This lets them schedule themselves before we
            // are marked as done, making sure the runningTaskCount counter doesn't hit
            // zero before it should
            this.taskManager.taskComplete(this);
        }
        catch (Exception x) {
            // There was a problem running the task, so we obviously shouldn't let
            // anything that depends on this to start.
            logger.log(Level.SEVERE, this.getTaskId(), x);
            this.taskManager.taskFailed(this);
        }
    }

    @Override
    public void addParent(ITaskGroup parent) {
        this.parents.add(parent);
    }


}
