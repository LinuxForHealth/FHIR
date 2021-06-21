/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.audit.cadf;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonReaderFactory;
import jakarta.json.JsonValue;
import jakarta.json.stream.JsonGenerator;
import jakarta.json.stream.JsonGeneratorFactory;

import com.ibm.fhir.audit.cadf.enums.Action;
import com.ibm.fhir.audit.cadf.enums.EventType;
import com.ibm.fhir.audit.cadf.enums.Outcome;
import com.ibm.fhir.exception.FHIRException;

/**
 * This class represent a CADF audit event as described by the DMTF standard:
 * Cloud Auditing Data Federation (CADF) - Data Format and Interface Definitions
 * Specification.
 * A CADF event generally represents a "subject-verb-object" tuple to indicate
 * what action a particular subject performed over what object.
 * The class is intended to be serialized using JSON Binding (Jsonb). It is
 * implemented using the Builder pattern.
 */
public class CadfEvent {
    // required properties
    private String id;
    private EventType eventType;
    private String eventTime;
    private Action action;
    private Outcome outcome;

    // optional properties
    private String typeURI;
    private CadfReason reason;
    private String initiatorId;
    private CadfResource initiator;
    private String targetId;
    private CadfResource target;
    private String observerId;
    private CadfResource observer;
    // JSONB doesn't know how to serialize arrays
    private ArrayList<CadfMeasurement> measurements;
    private String name;
    private String severity;
    private String duration;
    private ArrayList<String> tags;
    private ArrayList<CadfAttachment> attachments;
    private ArrayList<CadfReporterStep> reporterchain;

    private CadfEvent(Builder builder) {
        this.id           = builder.id;
        this.eventType    = builder.eventType;
        this.eventTime    = builder.eventTime;
        this.action       = builder.action;
        this.outcome      = builder.outcome;
        this.typeURI      = builder.typeURI;
        this.reason       = builder.reason;
        this.initiatorId  = builder.initiatorId;
        this.initiator    = builder.initiator;
        this.targetId     = builder.targetId;
        this.target       = builder.target;
        this.observerId   = builder.observerId;
        this.observer     = builder.observer;
        this.measurements = builder.measurements;
        this.setName(builder.name);
        this.setSeverity(builder.severity);
        this.setDuration(builder.duration);
        this.setTags(builder.tags);
        this.setAttachments(builder.attachments);
        this.setReporterchain(builder.reporterchain);
    }

    /**
     * Return the event ID
     *
     * @return String value of the event ID
     */
    public String getId() {
        return id;
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
        if (this.eventType == EventType.monitor && measurements == null) {
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

    public String getTypeURI() {
        return typeURI;
    }

    public void setTypeURI(String typeURI) {
        this.typeURI = typeURI;
    }

    public CadfReason getReason() {
        return reason;
    }

    public void setReason(CadfReason reason) {
        this.reason = reason;
    }

    public String getInitiatorId() {
        return initiatorId;
    }

    public void setInitiatorId(String initiatorId) {
        this.initiatorId = initiatorId;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getObserverId() {
        return observerId;
    }

    public void setObserverId(String observerId) {
        this.observerId = observerId;
    }

    public ArrayList<CadfMeasurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(ArrayList<CadfMeasurement> measurements) {
        this.measurements = measurements;
    }

    public void setId(String id) {
        this.id = id;
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

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Outcome getOutcome() {
        return outcome;
    }

    public void setOutcome(Outcome outcome) {
        this.outcome = outcome;
    }

    /**
     * Builder of the CadfEvent objects
     */
    public static class Builder {

        // required properties
        private String id;
        private EventType eventType;
        private String eventTime;
        private Action action;
        private Outcome outcome;

        // optional properties
        private String typeURI = "http://schemas.dmtf.org/cloud/audit/1.0/event";
        private CadfReason reason;
        private String initiatorId;
        private CadfResource initiator;
        private String targetId;
        private CadfResource target;
        private String observerId;
        private CadfResource observer;
        private ArrayList<CadfMeasurement> measurements;
        private String name;
        private String severity;
        private String duration;
        private ArrayList<String> tags;
        private ArrayList<CadfAttachment> attachments;
        private ArrayList<CadfReporterStep> reporterchain;

        public Builder() {
            // No Operation
        }

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
        public Builder(String id, EventType eventType, String eventTime, Action action,
                Outcome outcome) {
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
            this.action  = action;
            this.outcome = outcome;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder eventType(EventType eventType) {
            this.eventType = eventType;
            return this;
        }

        public Builder eventTime(String eventTime) {
            this.eventTime = eventTime;
            return this;
        }

        public Builder action(Action action) {
            this.action = action;
            return this;
        }

        public Builder outcome(Outcome outcome) {
            this.outcome = outcome;
            return this;
        }

        /**
         * Set the typeURI property of the CADF event entity. Defaults to
         * "http://schemas.dmtf.org/cloud/audit/1.0/event" Although it is formally *
         * optional, it is required in the JSON representation of the event.
         * Subsequently, if it is not set, {@link #build()} will throw an
         * {@link IllegalStateException}
         *
         * @param typeUri
         * @return Builder
         */
        public Builder typeUri(String typeUri) {
            this.typeURI = typeUri;
            return this;
        }

        /**
         * A convenience method to set the event reason. Instantiates a new
         * {@link CadfReason} object using the supplied values. Reason is a required
         * property if the event type is "control", otherwise it is optional.
         *
         * @param reasonType
         * @param reasonCode
         * @param policyType
         * @param policyId
         * @return Builder
         * @see CadfReason
         */
        public Builder reason(String reasonType, String reasonCode, String policyType, String policyId) {
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
         * @return Builder
         * @see CadfReason
         */
        public Builder reason(CadfReason reason) {
            this.reason = reason;
            return this;
        }

        /**
         * Sets a descriptive name for the event. Optional.
         *
         * @param name -- event descriptive name.
         * @return Builder
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * This optional property describes domain-relative severity assigned to the
         * event by OBSERVER.
         *
         * @param severity -- Severity descriptor.
         * @return Builder
         */
        public Builder severity(String severity) {
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
         * @return Builder
         */
        public Builder duration(String duration) {
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
         * @return Builder
         */
        public Builder tags(String[] tags) {
            this.tags = new ArrayList<>(Arrays.asList(tags));
            return this;
        }

        /**
         * A convenience method allowing to add one tag at a time as opposed to an
         * array.
         *
         * @see #tags(String[])
         * @param tag -- A String in the format
         *            [//namespace/]tag-name[?value=tag-value]
         * @return Builder
         */
        public Builder tag(String tag) {
            if (this.tags == null) {
                tags = new ArrayList<>();
            }
            this.tags.add(tag);
            return this;
        }

        /**
         * Property that represents the event INITIATOR. It is required when initiatorId
         * is not supplied.
         */
        public Builder initiator(CadfResource initiator) {
            this.initiator = initiator;
            return this;
        }

        /**
         * Property that identifies the event INITIATOR. This property can be used
         * instead of the "initiator" property if the CADF Event data is contained
         * within the same CADF Log or Report that also contains a valid CADF Resource
         * definition for the resource being referenced as the INITIATOR.
         */
        public Builder initiatorId(String initiatorId) {
            this.initiatorId = initiatorId;
            return this;
        }

        /**
         * Property that represents the event TARGET. It is required when targetId is
         * not supplied.
         */
        public Builder target(CadfResource target) {
            this.target = target;
            return this;
        }

        /**
         * Property that identifies the event TARGET. This property can be used instead
         * of the "target" property if the CADF Event data is contained within the same
         * CADF Log or Report that also contains a valid CADF Resource definition for
         * the resource being referenced as the TARGET.
         */
        public Builder targetId(String targetId) {
            this.targetId = targetId;
            return this;
        }

        /**
         * Property that represents the event OBSERVER. It is required when observerId
         * is not supplied.
         */
        public Builder observer(CadfResource observer) {
            this.observer = observer;
            return this;
        }

        /**
         * Property that identifies the event OBSERVER. This property can be used
         * instead of the "observer" property if the CADF Event data is contained within
         * the same CADF Log or Report that also contains a valid CADF Resource
         * definition for the resource being referenced as the OBSERVER.
         */
        public Builder observerId(String observerId) {
            this.observerId = observerId;
            return this;
        }

        /**
         * Property representing measurements associated with the event. Reguired if the
         * event type is "monitor".
         */
        public Builder measurements(CadfMeasurement[] measurements) {
            this.measurements = new ArrayList<>(Arrays.asList(measurements));
            return this;
        }

        /**
         * A convenience method to add one measurement at a time.
         *
         * @see #measurements(CadfMeasurement[])
         */
        public Builder measurement(CadfMeasurement measurement) {
            if (this.measurements == null) {
                measurements = new ArrayList<>();
            }
            this.measurements.add(measurement);
            return this;
        }

        /**
         * An optional array of extended or domain-specific information about the event
         * or its context.
         */
        public Builder attachments(CadfAttachment[] attachments) {
            this.attachments = new ArrayList<>(Arrays.asList(attachments));
            return this;
        }

        /**
         * A convenience method to add one attachment at a time.
         *
         * @see #attachments(CadfAttachment[])
         */
        public Builder attachment(CadfAttachment attachment) {
            if (this.attachments == null) {
                attachments = new ArrayList<>();
            }
            this.attachments.add(attachment);
            return this;
        }

        /**
         * An array of Reporterstep typed data that contains information about the
         * sequenced handling of or change to the associated CADF Event Record by any
         * REPORTER. Does not include the OBSERVER.
         */
        public Builder reporterChain(CadfReporterStep[] chain) {
            this.reporterchain = new ArrayList<>(Arrays.asList(chain));
            return this;
        }

        /**
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

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Generates JSON from this object.
     */
    public static class Writer {
        private static final Map<java.lang.String, Object> properties =
                Collections.singletonMap(JsonGenerator.PRETTY_PRINTING, true);
        private static final JsonGeneratorFactory PRETTY_PRINTING_GENERATOR_FACTORY =
                Json.createGeneratorFactory(properties);

        private Writer() {
            // No Operation
        }

        /**
         * @param obj
         * @return
         * @throws IOException
         */
        public static String generate(CadfEvent obj)
                throws IOException {
            String o = "{}";
            try (StringWriter writer = new StringWriter();) {
                try (JsonGenerator generator =
                        PRETTY_PRINTING_GENERATOR_FACTORY.createGenerator(writer);) {
                    generate(obj, generator);
                }
                o = writer.toString();
            }
            return o;
        }

        public static void generate(CadfEvent obj, JsonGenerator generator)
                throws IOException {
            generator.writeStartObject();

            if (obj.getAction() != null) {
                generator.write("action", obj.getAction().toString());
            }

            if (obj.getDuration() != null) {
                generator.write("duration", obj.getDuration());
            }

            if (obj.getEventTime() != null) {
                generator.write("eventTime", obj.getEventTime());
            }

            if (obj.getEventType() != null) {
                generator.write("eventType", obj.getEventType().name());
            }

            if (obj.getId() != null) {
                generator.write("id", obj.getId());
            }

            if (obj.getInitiatorId() != null) {
                generator.write("initiatorId", obj.getInitiatorId());
            }

            if (obj.getName() != null) {
                generator.write("name", obj.getName());
            }

            if (obj.getObserverId() != null) {
                generator.write("observerId", obj.getObserverId());
            }

            if (obj.getOutcome() != null) {
                generator.write("outcome", obj.getOutcome().name());
            }

            if (obj.getSeverity() != null) {
                generator.write("severity", obj.getSeverity());
            }

            if (obj.getTarget() != null) {
                generator.writeStartObject("target");
                CadfResource.Writer.generate(obj.getTarget(), generator);
                generator.writeEnd();
            }

            if (obj.getTargetId() != null) {
                generator.write("targetId", obj.getTargetId());
            }

            if (obj.getTypeURI() != null) {
                generator.write("typeURI", obj.getTypeURI());
            }

            if (obj.getTags() != null) {
                generator.writeStartArray("tags");
                for (String tag : obj.getTags()) {
                    if (tag != null) {
                        generator.write(tag);
                    }
                }
                generator.writeEnd();
            }

            if (obj.getAttachments() != null) {
                generator.writeStartArray("attachments");
                for (CadfAttachment attachment : obj.getAttachments()) {
                    if (attachment != null) {
                        CadfAttachment.Writer.generate(attachment, generator);
                    }
                }
                generator.writeEnd();
            }

            if (obj.getMeasurements() != null) {
                generator.writeStartArray("measurements");
                for (CadfMeasurement measurement : obj.getMeasurements()) {
                    if (measurement != null) {
                        CadfMeasurement.Writer.generate(measurement, generator);
                    }
                }
                generator.writeEnd();
            }

            if (obj.getReporterchain() != null) {
                generator.writeStartArray("reporterchain");
                for (CadfReporterStep reportStep : obj.getReporterchain()) {
                    if (reportStep  != null) {
                        CadfReporterStep.Writer.generate(reportStep, generator);
                    }
                }
                generator.writeEnd();
            }

            if (obj.getInitiator() != null) {
                generator.writeStartObject("initiator");
                if (obj.getInitiator() !=null) {
                    CadfResource.Writer.generate(obj.getInitiator(), generator);
                }
                generator.writeEnd();
            }

            if (obj.getObserver() != null) {
                generator.writeStartObject("observer");
                if (obj.getObserver() != null) {
                    CadfResource.Writer.generate(obj.getObserver(), generator);
                }
                generator.writeEnd();
            }

            if (obj.getReason() != null) {
                generator.writeStartObject("reason");
                if (obj.getReason() != null) {
                    CadfReason.Writer.generate(obj.getReason(), generator);
                }
                generator.writeEnd();
            }

            generator.writeEnd();
        }

    }

    public static class Parser {
        private static final JsonReaderFactory JSON_READER_FACTORY = Json.createReaderFactory(null);

        private Parser() {
            // No Impl
        }

        public static CadfEvent parse(InputStream in)
                throws FHIRException {
            try (JsonReader jsonReader =
                    JSON_READER_FACTORY.createReader(in, StandardCharsets.UTF_8)) {
                JsonObject jsonObject = jsonReader.readObject();
                return parse(jsonObject);
            } catch (Exception e) {
                throw new FHIRException("Problem parsing the CadfEvent", e);
            }
        }

        public static CadfEvent parse(JsonObject jsonObject)
                throws FHIRException, ClassNotFoundException, IOException {
            CadfEvent.Builder builder =
                    CadfEvent.builder();

            if (jsonObject.get("action") != null) {
                String action = jsonObject.getString("action");
                builder.action(Action.valueOf(action));
            }

            if (jsonObject.get("duration") != null) {
                String duration = jsonObject.getString("duration");
                builder.duration(duration);
            }

            if (jsonObject.get("eventTime") != null) {
                String eventTime = jsonObject.getString("eventTime");
                builder.eventTime(eventTime);
            }

            if (jsonObject.get("eventType") != null) {
                String eventType = jsonObject.getString("eventType");
                builder.eventType(EventType.valueOf(eventType));
            }

            if (jsonObject.get("id") != null) {
                String id = jsonObject.getString("id");
                builder.id(id);
            }

            if (jsonObject.get("initiatorId") != null) {
                String initiatorId = jsonObject.getString("initiatorId");
                builder.initiatorId(initiatorId);
            }

            if (jsonObject.get("name") != null) {
                String name = jsonObject.getString("name");
                builder.name(name);
            }

            if (jsonObject.get("observerId") != null) {
                String observerId = jsonObject.getString("observerId");
                builder.observerId(observerId);
            }

            if (jsonObject.get("outcome") != null) {
                String outcome = jsonObject.getString("outcome");
                builder.outcome(Outcome.valueOf(outcome));
            }

            if (jsonObject.get("severity") != null) {
                String severity = jsonObject.getString("severity");
                builder.severity(severity);
            }

            if (jsonObject.get("targetId") != null) {
                String targetId = jsonObject.getString("targetId");
                builder.targetId(targetId);
            }

            if (jsonObject.get("typeURI") != null) {
                String typeURI = jsonObject.getString("typeURI");
                builder.typeUri(typeURI);
            }

            if (jsonObject.get("tags") != null) {
                JsonArray arr = jsonObject.getJsonArray("tags");
                Iterator<JsonValue> iter = arr.iterator();
                while(iter.hasNext()) {
                    builder.tag(iter.next().toString());
                }
            }

            if (jsonObject.get("attachments") != null) {
                JsonArray arr = jsonObject.getJsonArray("attachments");
                Iterator<JsonValue> iter = arr.iterator();
                while(iter.hasNext()) {
                    builder.attachment(
                            CadfAttachment.Parser.parse(iter.next().asJsonObject()));
                }
            }

            if (jsonObject.get("measurements") != null) {
                JsonArray arr = jsonObject.getJsonArray("measurements");
                Iterator<JsonValue> iter = arr.iterator();
                while(iter.hasNext()) {
                    builder.measurement(
                            CadfMeasurement.Parser.parse(iter.next().asJsonObject()));
                }
            }

            if (jsonObject.get("reporterchain") != null) {
                JsonArray arr = jsonObject.getJsonArray("reporterchain");
                Iterator<JsonValue> iter = arr.iterator();
                CadfReporterStep[] chain = new CadfReporterStep[arr.size()];
                int i = 0;
                while(iter.hasNext()) {
                    chain[i++] = CadfReporterStep.Parser.parse(iter.next().asJsonObject());
                }
                builder.reporterChain(chain);
            }

            if (jsonObject.get("initiator") != null) {
                JsonObject tmp = jsonObject.getJsonObject("initiator");
                builder.initiator(CadfResource.Parser.parse(tmp));
            }

            if (jsonObject.get("observer") != null) {
                JsonObject tmp = jsonObject.getJsonObject("observer");
                builder.observer(CadfResource.Parser.parse(tmp));
            }

            if (jsonObject.get("reason") != null) {
                JsonObject tmp = jsonObject.getJsonObject("reason");
                builder.reason(CadfReason.Parser.parse(tmp));
            }

            return builder.build();
        }
    }

}
