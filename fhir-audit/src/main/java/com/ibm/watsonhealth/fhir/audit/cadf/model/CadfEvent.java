/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.audit.cadf.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import javax.json.bind.annotation.JsonbProperty;

/**
 * This class represent a CADF audit event as described by the DMTF standard:
 * Cloud Auditing Data Federation (CADF) - Data Format and Interface Definitions
 * Specification.
 * 
 * A CADF event generally represents a "subject-verb-object" tuple to indicate
 * what action a particular subject performed over what object.
 * 
 * The class is intended to be serialized using JSON Binding (Jsonb). It is
 * implemented using the Builder pattern.
 */
public class CadfEvent {

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

    /**
     * "Root" outcome classification.
     * 
     * "success" -- The attempted action completed successfully with the expected
     * results.
     * 
     * "failure" -- The attempted action failed due to some form of operational
     * system failure or because the action was denied, blocked, or refused in some
     * way.
     * 
     * "unknown" -- The outcome of the attempted action is unknown and it is not
     * expected that it will ever be known. Example: data sent to a third party via
     * an unreliable protocol without acknowledgment.
     * 
     * "pending" -- The outcome of the attempted action is unknown, but it is
     * expected that it will be known at some point in the future. A separate,
     * future, correlated event may provide additional detail. Example: a long-
     * running activity has started; the Observer will follow up with a nearly
     * identical event that includes the final outcome.
     */
    public enum Outcome {
        success, failure, unknown, pending
    }

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

    /**
     * This class represents the CADF Resource type taxonomy.
     * 
     * Properties have string values, because they must be serialized as URIs, per
     * CADF specifications.
     */
    public enum ResourceType {
        /**
         * compute/node
         */
        compute_node("compute/node"),
        /**
         * compute/cpu
         */
        compute_cpu("compute/cpu"),
        /**
         * compute/machine
         */
        compute_machine("compute/machine"),
        /**
         * compute/process
         */
        compute_process("compute/process"),
        /**
         * compute/thread
         */
        compute_thread("compute/thread"),
        /**
         * service/bss (business support services)
         */
        service_bss("service/bss"),
        /**
         * service/bss/metering
         */
        service_bss_metering("service/bss/metering"),
        /**
         * service/composition The logical classification grouping for services that
         * supports the compositing of independent services into a new service offering
         */
        service_composition("service/composition"),
        /**
         * service/compute: Infrastructure services for managing computing (fabric).
         */
        service_compute("service/compute"),
        /**
         * service/database (DBaaS)
         */
        service_database("service/database"),
        /**
         * service/image: Infrastructure services for managing virtual machine images
         * and associated metadata.
         */
        service_image("service/image"),
        /**
         * service/network: Infrastructure services for managing networking (fabric).
         */
        service_network("service/network"),
        /**
         * service/oss (Operational support services)
         */
        service_oss("service/oss"),
        /**
         * service/oss/monitoring
         */
        service_oss_monitoring("service/oss/monitoring"),
        /**
         * service/oss/logging
         */
        service_oss_logging("service/oss/logging"),
        /**
         * service/security: The logical classification grouping for security services
         * including Identity Mgmt., Policy Mgmt., Authentication, Authorization, Access
         * Mgmt., etc. (a.k.a. “Security- as-a-Service”)
         */
        service_security("service/security"),
        /**
         * service/storage
         */
        service_storage("service/storage"),
        /**
         * service/storage/block
         */
        service_storage_block("service/storage/block"),
        /**
         * service/storage/object
         */
        service_storage_object("service/storage/object"),
        /**
         * data/catalog
         */
        data_catalog("data/catalog"),
        /**
         * data/config
         */
        data_config("data/config"),
        /**
         * data/directory
         */
        data_directory("data/directory"),
        /**
         * data/file
         */
        data_file("data/file"),
        /**
         * data/image
         */
        data_image("data/image"),
        /**
         * data/log
         */
        data_log("data/log"),
        /**
         * data/message
         */
        data_message("data/message"),
        /**
         * data/message/stream
         */
        data_message_stream("data/message/stream"),
        /**
         * data/module
         */
        data_module("data/module"),
        /**
         * data/package
         */
        data_package("data/package"),
        /**
         * data/report
         */
        data_report("data/report"),
        /**
         * data/template
         */
        data_template("data/template"),
        /**
         * data/workload
         */
        data_workload("data/workload"),
        /**
         * data/database
         */
        data_database("data/database"),
        /**
         * data/security
         */
        data_security("data/security");

        private String uri;

        ResourceType(String uri) {
            this.uri = uri;
        }

        @Override
        public String toString() {
            return uri;
        }
    }

    public class Metric {
        public String metricId;
        public String unit;
        public String name;
        public CadfMapItem[] annotations;
    }

    public class Measurement {
        public Object result;
        public String metricId;
        public Metric metric;
        public CadfResource calculatedBy;
        public String calculatedById;
    }

    public enum ReporterRole {
        observer, modifier, relay;
    }

    // required properties
    private String id;
    private EventType eventType;
    private String eventTime;
    private CadfEvent.Action action;
    private CadfEvent.Outcome outcome;

    // optional properties
    @JsonbProperty("typeURI")
    private String typeURI;
    private CadfReason reason;
    private String initiatorId;
    private CadfResource initiator;
    private String targetId;
    private CadfResource target;
    private String observerId;
    private CadfResource observer;
    // JSONB doesn't know how to serialize arrays
    private ArrayList<CadfEvent.Measurement> measurements;
    private String name;
    private String severity;
    private String duration;
    private ArrayList<String> tags;
    private ArrayList<CadfAttachment> attachments;
    private ArrayList<CadfReporterStep> reporterchain;

    /**
     * Return the event ID
     * 
     * @return String value of the event ID
     */
    public String getId() {
        return id;
    }

    private CadfEvent(CadfEventBuilder builder) {
        this.id = builder.id;
        this.eventType = builder.eventType;
        this.eventTime = builder.eventTime;
        this.action = builder.action;
        this.outcome = builder.outcome;
        this.typeURI = builder.typeURI;
        this.reason = builder.reason;
        this.initiatorId = builder.initiatorId;
        this.initiator = builder.initiator;
        this.targetId = builder.targetId;
        this.target = builder.target;
        this.observerId = builder.observerId;
        this.observer = builder.observer;
        this.measurements = builder.measurements;
        this.setName(builder.name);
        this.setSeverity(builder.severity);
        this.setDuration(builder.duration);
        this.setTags(builder.tags);
        this.setAttachments(builder.attachments);
        this.setReporterchain(builder.reporterchain);
    }

    /**
     * Format time intervals (durations) according to the XML Schema v2
     * specifications. https://www.w3.org/TR/xmlschema-2/#duration
     * 
     * @param duration in milliseconds
     * @return String representation of the duration according to the XML Schema 2
     *         specification.
     */
    public static String formatDuration(long duration) {
        long daysDiff = Double.valueOf(Math.floor(duration / 86400000.)).longValue();
        long hoursDiff = Double.valueOf(Math.floor((duration % 86400000) / 3600000.)).longValue();
        long minutesDiff = Double.valueOf(Math.floor(((duration % 86400000) % 3600000) / 60000.)).longValue();
        double secondsDiff = (((duration % 86400000) % 3600000) % 60000) / 1000.; // fractional
        return String.format("P%dDT%dH%dM%06.3fS", daysDiff, hoursDiff, minutesDiff, secondsDiff);
    }

    /**
     * Validate contents of the CADF event so far.
     * 
     * The logic is determined by the CADF specification.
     * 
     * @throws IllegalStateException when the event does not meet the specification.
     */
    private void validate() throws IllegalStateException {
        // any event type must have these components:
        if (this.id == null || this.eventType == null || this.eventTime == null || this.action == null
                || this.outcome == null) {
            throw new IllegalStateException("mandatory properties are missing");
        }
        if ((this.initiator == null && this.initiatorId == null) || (this.target == null && this.targetId == null)
                || (this.observer == null && this.observerId == null)) {
            throw new IllegalStateException("at least one of the required resource references is missing");
        }
        if (this.typeURI == null) {
            throw new IllegalStateException("typeURI is required for JSON serialization");
        }

        // monitor events require measurements
        if (this.eventType == CadfEvent.EventType.monitor && measurements == null) {
            throw new IllegalStateException("mandatory measurement component is missing missing for event type");
        }

        // Reason is required for control events
        if (this.eventType == EventType.control && this.reason == null) {
            throw new IllegalStateException("reason is required for control events");
        }
        if (this.reason != null) {
            this.reason.validate();
        }

        // if we are here, everything seems to be ok
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the severity
     */
    public String getSeverity() {
        return severity;
    }

    /**
     * @param severity the severity to set
     */
    public void setSeverity(String severity) {
        this.severity = severity;
    }

    /**
     * @return the duration
     */
    public String getDuration() {
        return duration;
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration(String duration) {
        this.duration = duration;
    }

    /**
     * @return the tags
     */
    public ArrayList<String> getTags() {
        return tags;
    }

    /**
     * @param tags the tags to set
     */
    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    /**
     * @return the attachments
     */
    public ArrayList<CadfAttachment> getAttachments() {
        return attachments;
    }

    /**
     * @param attachments the attachments to set
     */
    public void setAttachments(ArrayList<CadfAttachment> attachments) {
        this.attachments = attachments;
    }

    /**
     * @return the reporterchain
     */
    public ArrayList<CadfReporterStep> getReporterchain() {
        return reporterchain;
    }

    /**
     * @param reporterchain the reporterchain to set
     */
    public void setReporterchain(ArrayList<CadfReporterStep> reporterchain) {
        this.reporterchain = reporterchain;
    }

    /**
     * Builder of the CadfEvent objects
     * 
     */
    public static class CadfEventBuilder {

        // required properties
        private String id;
        private CadfEvent.EventType eventType;
        private String eventTime;
        private CadfEvent.Action action;
        private CadfEvent.Outcome outcome;

        // optional properties
        private String typeURI = "http://schemas.dmtf.org/cloud/audit/1.0/event";
        private CadfReason reason;
        private String initiatorId;
        private CadfResource initiator;
        private String targetId;
        private CadfResource target;
        private String observerId;
        private CadfResource observer;
        private ArrayList<CadfEvent.Measurement> measurements;
        private String name;
        private String severity;
        private String duration;
        private ArrayList<String> tags;
        private ArrayList<CadfAttachment> attachments;
        private ArrayList<CadfReporterStep> reporterchain;

        /**
         * CadfEvent builder constructor. Supply initial values for the event
         * definition.
         * 
         * @param id        -- String. Globally unique identifier for the event. If null
         *                  value is provided, the identifier is generated
         *                  automatically.
         * @param eventType -- CadfEvent.EventType. See {@link EventType} for guidance.
         * @param eventTime -- Date. Timestamp of the event occurrence. If null value is
         *                  provided, this property is set to the current timestamp.
         * @param action    -- CadfEvent.Action. Indicates the action that created this
         *                  event.
         * @param outcome   -- CadfEvent.Outcome. Indicate the action's outcome.
         */
        public CadfEventBuilder(String id, CadfEvent.EventType eventType, String eventTime, CadfEvent.Action action,
                CadfEvent.Outcome outcome) {
            if (id != null) {
                this.id = id;
            } else {
                this.id = UUID.randomUUID().toString();
            }
            this.eventType = eventType;
            if (eventTime != null) {
                this.eventTime = eventTime;
            } else {
                this.eventTime = new Date().toString();
            }
            this.action = action;
            this.outcome = outcome;
        }

        /**
         * Set the typeURI property of the CADF event entity. Defaults to
         * "http://schemas.dmtf.org/cloud/audit/1.0/event" Although it is formally *
         * optional, it is required in the JSON representation of the event.
         * Subsequently, if it is not set, {@link #build()} will throw an
         * {@link IllegalStateException}
         * 
         * @param typeUri
         * @return CadfEventBuilder
         */
        public CadfEventBuilder withTypeUri(String typeUri) {
            this.typeURI = typeUri;
            return this;
        }

        /**
         * A convenience method to set the event reason. Instantiates a new
         * {@link CadfReason} object using the supplied values. Reason is a required
         * property if the event type is "control", otherwise it is optional.
         * 
         * @param reason
         * @return CadfEventBuilder
         * @see CadfReason
         */
        public CadfEventBuilder withReason(String reasonType, String reasonCode, String policyType, String policyId) {

            this.reason = new CadfReason(reasonCode, reasonType, policyId, policyType);
            return this;
        }

        /**
         * Sets the event reason. This property contains domain-specific reason code and
         * policy data that provides an additional level of detail to the outcome value.
         * Reason is a required property if the event type is "control", otherwise it is
         * optional.
         * 
         * @param reason
         * @return CadfEventBuilder
         * @see CadfReason
         */
        public CadfEventBuilder withReason(CadfReason reason) {
            this.reason = reason;
            return this;
        }

        /**
         * Sets a descriptive name for the event. Optional.
         * 
         * @param name -- event descriptive name.
         * @return CadfEventBuilder
         */
        public CadfEventBuilder withName(String name) {
            this.name = name;
            return this;
        }

        /**
         * This optional property describes domain-relative severity assigned to the
         * event by OBSERVER.
         * 
         * @param severity -- Severity descriptor.
         * @return CadfEventBuilder
         */
        public CadfEventBuilder withSeverity(String severity) {
            this.severity = severity;
            return this;
        }

        /**
         * This optional property describes the duration of activity for long-running
         * activities. It is typically used in the second of a pair of events marking
         * the start and end of such activity. Value as defined by xs:duration per
         * XMLSchema2
         * 
         * @param duration -- String in the format [-]PnYnMnD[TnHnMnS]
         * @return CadfEventBuilder
         */
        public CadfEventBuilder withDuration(String duration) {
            this.duration = duration;
            return this;
        }

        /**
         * An optional array of Tags that MAY be used to further qualify or categorize
         * the CADF Event Record. Each tag is represented by a URI consisting of an
         * optional namespace, a required tag name, and an optional value, e.g.
         * //GRC20.gov/cloud/security/pci-dss?value=enabled In this example
         * "//GRC20.gov/cloud/security/" is the namespace, "pci-dss" is the tag name and
         * "enabled" is the value.
         * 
         * @param tags -- Array of strings, each in the format
         *             [//namespace/]tag-name[?value=tag-value]
         * @return CadfEventBuilder
         */
        public CadfEventBuilder withTags(String[] tags) {
            this.tags = new ArrayList<String>(Arrays.asList(tags));
            return this;
        }

        /**
         * A convenience method allowing to add one tag at a time as opposed to an
         * array.
         * 
         * @see withTags
         * @param tags -- A String in the format
         *             [//namespace/]tag-name[?value=tag-value]
         * @return CadfEventBuilder
         */
        public CadfEventBuilder withTag(String tag) {
            if (this.tags == null) {
                tags = new ArrayList<String>();
            }
            this.tags.add(tag);
            return this;
        }

        /**
         * Property that represents the event INITIATOR. It is required when initiatorId
         * is not supplied.
         */
        public CadfEventBuilder withInitiator(CadfResource initiator) {
            this.initiator = initiator;
            return this;
        }

        /**
         * Property that identifies the event INITIATOR. This property can be used
         * instead of the "initiator" property if the CADF Event data is contained
         * within the same CADF Log or Report that also contains a valid CADF Resource
         * definition for the resource being referenced as the INITIATOR.
         */
        public CadfEventBuilder withInitiatorId(String initiatorId) {
            this.initiatorId = initiatorId;
            return this;
        }

        /**
         * Property that represents the event TARGET. It is required when targetId is
         * not supplied.
         */
        public CadfEventBuilder withTarget(CadfResource target) {
            this.target = target;
            return this;
        }

        /**
         * Property that identifies the event TARGET. This property can be used instead
         * of the "target" property if the CADF Event data is contained within the same
         * CADF Log or Report that also contains a valid CADF Resource definition for
         * the resource being referenced as the TARGET.
         */
        public CadfEventBuilder withTargetId(String targetId) {
            this.targetId = targetId;
            return this;
        }

        /**
         * Property that represents the event OBSERVER. It is required when observerId
         * is not supplied.
         */
        public CadfEventBuilder withObserver(CadfResource observer) {
            this.observer = observer;
            return this;
        }

        /**
         * Property that identifies the event OBSERVER. This property can be used
         * instead of the "observer" property if the CADF Event data is contained within
         * the same CADF Log or Report that also contains a valid CADF Resource
         * definition for the resource being referenced as the OBSERVER.
         */
        public CadfEventBuilder withObserverId(String observerId) {
            this.observerId = observerId;
            return this;
        }

        /**
         * Property representing measurements associated with the event. Reguired if the
         * event type is "monitor".
         */
        public CadfEventBuilder withMeasurements(CadfEvent.Measurement[] measurements) {
            this.measurements = new ArrayList<CadfEvent.Measurement>(Arrays.asList(measurements));
            return this;
        }

        /**
         * A convenience method to add one measurement at a time.
         * 
         * @see withMeasurements()
         */
        public CadfEventBuilder withMeasurement(CadfEvent.Measurement measurement) {
            if (this.measurements == null) {
                measurements = new ArrayList<CadfEvent.Measurement>();
            }
            this.measurements.add(measurement);
            return this;
        }

        /**
         * An optional array of extended or domain-specific information about the event
         * or its context.
         */
        public CadfEventBuilder withAttachments(CadfAttachment[] attachments) {
            this.attachments = new ArrayList<CadfAttachment>(Arrays.asList(attachments));
            return this;
        }

        /**
         * A convenience method to add one attachment at a time.
         * 
         * @see withAttachments()
         */
        public CadfEventBuilder withAttachment(CadfAttachment attachment) {
            if (this.attachments == null) {
                attachments = new ArrayList<CadfAttachment>();
            }
            this.attachments.add(attachment);
            return this;
        }

        /**
         * An array of Reporterstep typed data that contains information about the
         * sequenced handling of or change to the associated CADF Event Record by any
         * REPORTER. Does not include the OBSERVER.
         */
        public CadfEventBuilder withReporterChain(CadfReporterStep[] chain) {
            this.reporterchain = new ArrayList<CadfReporterStep>(Arrays.asList(chain));
            return this;
        }

        /**
         * 
         * @return {@link CadfEvent}
         * @throws IllegalStateException when the combination of event properties is not
         *                               valid.
         */
        public CadfEvent build() throws IllegalStateException {
            CadfEvent evt = new CadfEvent(this);
            evt.validate();
            return evt;
        }
    }

    public CadfResource getInitiator() {
        return initiator;
    }

    public void setInitiator(CadfResource initiator) {
        this.initiator = initiator;
    }

    public CadfResource getTarget() {
        return target;
    }

    public void setTarget(CadfResource target) {
        this.target = target;
    }

    public CadfResource getObserver() {
        return observer;
    }

    public void setObserver(CadfResource observer) {
        this.observer = observer;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public CadfEvent.Action getAction() {
        return action;
    }

    public void setAction(CadfEvent.Action action) {
        this.action = action;
    }

    public CadfEvent.Outcome getOutcome() {
        return outcome;
    }

    public void setOutcome(CadfEvent.Outcome outcome) {
        this.outcome = outcome;
    }
}
