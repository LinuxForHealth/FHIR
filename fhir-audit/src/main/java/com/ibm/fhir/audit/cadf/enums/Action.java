/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.audit.cadf.enums;

/**
 * This class represents the CADF ACTION taxonomy.
 *
 * Properties have string values, because they must be serialized as URIs, per
 * CADF specifications.
 */
public enum Action {
    /**
     * Event type: monitor. The only allowed action for this event type.
     */
    monitor("monitor"),
    /**
     * Event type: control. Indicates that the initiating resource has denied access
     * to the target resource.
     */
    deny("deny"),
    /**
     * Event type: control. Indicates that the initiating resource has allowed
     * access to the target resource.
     */
    allow("allow"),
    /**
     * Event type: control. Indicates the evaluation or application of a policy,
     * rule, or algorithm to a set of inputs.
     */
    evaluate("evaluate"),
    /**
     * Event type: control. Indicates that the initiating resource has sent a
     * notification based on some policy or algorithm application – perhaps it has
     * generated an alert to indicate a system problem.
     */
    notify("notify"),
    /**
     * Event type: activity. Signifies an attempt (successful or not) to create the
     * target resource.
     */
    create("create"),
    /**
     * Event type: activity. Attempt to read from the target resource.
     */
    read("read"),
    /**
     * Event type: activity. Attempt to modify (change) one or more properties of
     * the target resource.
     */
    update("update"),
    /**
     * Event type: activity. Attempt to delete the target resource.
     */
    delete("delete"),
    /**
     * Event type: activity. The target resource is being persisted to long-term
     * storage. No environment, context, or resource state is saved.
     */
    backup("backup"),
    /**
     * Event type: activity. The target resource is being persisted to long-term
     * storage including relevant environment, context, and state information
     * (snapshot).
     */
    capture("capture"),
    /**
     * Event type: activity. Target resource configuration is being set up in a
     * particular environment or for a particular purpose.
     */
    configure("configure"),
    /**
     * Event type: activity. The target resource is being provisioned for use by the
     * initiator, but not yet ready.
     */
    deploy("deploy"),
    /**
     * Event type: activity. The initiator causes some or all functions of the
     * target resource to be stopped or blocked.
     */
    disable("disable"),
    /**
     * Event type: activity. The initiator causes some or all functions of the
     * target resource to be allowed or permitted.
     */
    enable("enable"),
    /**
     * Event type: activity. The target resource is being re-created from persistent
     * storage.
     */
    restore("restore"),
    /**
     *
     */
    start("start"),
    /**
     *
     */
    stop("stop"),
    /**
     *
     */
    undeploy("undeploy"),
    /**
     *
     */
    receive("receive"),
    /**
     *
     */
    send("send"),
    /**
     *
     */
    authenticate("authenticate"),
    /**
     *
     */
    login("authenticate/login"),
    /**
     * Logon is a specialized authentication action, typically used to establish a
     * resource’s identity or credentials for the resource to be authorized to
     * perform subsequent actions.
     *
     * This is an extension for the "authenticate" action.
     */
    renew("renew"),
    /**
     *
     */
    revoke("revoke"),
    /**
     * Any event type. Observer is unable to classify the action.
     */
    unknown("unknown");

    private String uri;

    Action(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return uri;
    }
}