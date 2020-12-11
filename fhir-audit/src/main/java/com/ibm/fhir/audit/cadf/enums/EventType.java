/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.audit.cadf.enums;

/**
 * CADF event types:
 *
 * "monitor" -- events that provide information about the status of a resource
 * or of its attributes or properties. Such events typically report on
 * measurements or periodic probes on cloud resources, and may produce aggregate
 * data such as statistical or summary metrics.
 *
 * "activity" -- events that provide information about actions having occurred
 * or intended to occur, and initiated by some resource or done against some
 * resource.
 *
 * "control" -- events that reflect on or provide information about the
 * application of a policy or business rule, or more generally express the
 * outcome of a decision making process. Such events typically report on how
 * these policies or rules manifest in concrete situations such as attempted
 * resource access, evaluation of resource states, notifications, prioritization
 * of tasks, or other automated administrative action.
 */
public enum EventType {
    monitor, activity, control
}