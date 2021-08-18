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
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonReaderFactory;
import jakarta.json.stream.JsonGenerator;
import jakarta.json.stream.JsonGeneratorFactory;

import com.ibm.fhir.audit.cadf.enums.ReporterRole;
import com.ibm.fhir.exception.FHIRException;

/**
 * CadfReporter Step
 */
public final class CadfReporterStep {

    private static final DateTimeFormatter DATE_TIME_PARSER_FORMATTER =
            new DateTimeFormatterBuilder()
                    .appendPattern("yyyy")
                    .optionalStart()
                    .appendPattern("-MM")
                    .optionalStart()
                    .appendPattern("-dd")
                    .appendLiteral("T")
                    .optionalStart()
                    .appendPattern("HH")
                    .optionalStart()
                    .appendPattern(":mm")
                    .optionalStart()
                    .appendPattern(":ss")
                    .optionalStart()
                    .appendFraction(ChronoField.MICRO_OF_SECOND, 0, 6, true)
                    .optionalEnd()
                    .optionalEnd()
                    .optionalEnd()
                    .optionalStart()
                    .appendPattern("XXX")
                    .optionalEnd()
                    .optionalEnd()
                    .optionalEnd()
                    .optionalEnd()
                    .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                    .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                    .toFormatter();

    protected final static DateTimeFormatter formatterFullTimestamp =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX").withZone(ZoneId.of("UTC"));

    private final ReporterRole role;
    private final CadfResource reporter;
    private final String reporterId;
    private final ZonedDateTime reporterTime;
    private final ArrayList<CadfAttachment> attachments;

    private CadfReporterStep(Builder builder) {
        this.role         = builder.role;
        this.reporter     = builder.reporter;
        this.reporterId   = builder.reporterId;
        this.reporterTime = builder.reporterTime;
        this.attachments  = builder.attachments;
    }

    public ReporterRole getRole() {
        return role;
    }

    public CadfResource getReporter() {
        return reporter;
    }

    public String getReporterId() {
        return reporterId;
    }

    /**
     * @return the reporterTime
     */
    public ZonedDateTime getReporterTime() {
        return reporterTime;
    }

    /**
     * @return the attachments
     */
    public ArrayList<CadfAttachment> getAttachments() {
        return attachments;
    }

    /**
     * Validate contents of the reporter step type.
     * The logic is determined by the CADF specification. In short, role and either
     * reporter resource or ID are required.
     *
     * @throws IllegalStateException when the properties do not meet the
     *                               specification.
     */
    private void validate() throws IllegalStateException {
        if (this.role == null) {
            throw new IllegalStateException("missing required role");
        }
        if (this.reporter == null && (this.reporterId == null || this.reporterId.isEmpty())) {
            throw new IllegalStateException("missing required reporter");
        }
    }

    public static class Builder {
        private ReporterRole role;
        private CadfResource reporter;
        private String reporterId;
        private ZonedDateTime reporterTime;
        private ArrayList<CadfAttachment> attachments;

        private Builder() {
            // NO Operation
        }

        /**
         * Construct a ReporterStep object. This type represents a step in the
         * REPORTERCHAIN that captures information about any notable REPORTER (in
         * addition to the OBSERVER) that modified or relayed the CADF Event Record and
         * any details regarding any modification it performed on the CADF Event Record
         * it is contained within.
         *
         * @param role - The role the REPORTER performed on the CADF Event Record
         *             (e.g., an "observer", "modifier" or "relay" role).
         * @see CadfEvent#getReporterchain()
         *      {@link ReporterRole}
         * @param rep   - This property defines the resource that acted as a REPORTER
         *              on a CADF Event Record. It is required if repId is not
         *              supplied.
         * @param repId - This property identifies a resource that acted as a REPORTER
         *              on a CADF Event Record by reference and whose definition
         *              exists outside the event record itself (e.g., within the same
         *              CADF Log or Report). Note: This property can be used instead
         *              of the "reporter" property to reference a valid CADF Resource
         *              definition, which is already defined and can be referenced by
         *              its identifier (e.g., a CADF Resource already defined within
         *              the same CADF Event record or at the CADF Log or Report level
         *              that also contains the referencing CADF Event record).
         * @param rTime - Optional. The time a REPORTER adds its Reporterstep entry
         *              into the REPORTERCHAIN (which follows completion of any
         *              updates to or handling of the corresponding CADF Event
         *              Record).
         */
        public Builder(ReporterRole role, CadfResource rep, String repId, ZonedDateTime rTime) {
            this.role       = role;
            this.reporter   = rep;
            this.reporterId = repId;
            if (rTime != null) {
                this.reporterTime = rTime;
            } else {
                this.reporterTime = Instant.now().atZone(ZoneId.of("UTC"));
            }
        }

        /**
         * An optional array of additional data containing information about the
         * reporter or any action it performed that affected the CADF Event Record
         * contents.
         */
        public Builder attachments(CadfAttachment[] attachments) {
            this.attachments = new ArrayList<>(Arrays.asList(attachments));
            return this;
        }

        /**
         * An optional array of additional data containing information about the
         * reporter or any action it performed that affected the CADF Event Record
         * contents.
         */
        public Builder attachments(ArrayList<CadfAttachment> attachments) {
            this.attachments = attachments;
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

        public Builder reporter(CadfResource reporter) {
            this.reporter = reporter;
            return this;
        }

        public Builder reporterId(String reporterId) {
            this.reporterId = reporterId;
            return this;
        }

        public Builder reporterTime(ZonedDateTime reporterTime) {
            this.reporterTime = reporterTime;
            return this;
        }

        public Builder role(ReporterRole role) {
            this.role = role;
            return this;
        }

        /**
         * Build an immutable ReporterStep instance.
         *
         * @return ReporterStep
         * @throws IllegalStateException when the properties do not meet the
         *                               specification.
         */
        public CadfReporterStep build() {
            CadfReporterStep step = new CadfReporterStep(this);
            step.validate();
            return step;
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
        public static String generate(CadfReporterStep obj)
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

        public static void generate(CadfReporterStep obj, JsonGenerator generator)
                throws IOException {
            // If the CADF reporter step is null, then simply skip it.
            if (obj == null) {
                return;
            }
            generator.writeStartObject();

            if (obj.getReporter() != null) {
                generator.writeStartObject("reporter");
                CadfResource.Writer.generate(obj.getReporter(), generator);
                generator.writeEnd();
            }

            if (obj.getReporterId() != null) {
                generator.write("reporterId", obj.getReporterId());
            }

            if (obj.getReporterTime() != null) {
                generator.write("reporterTime", formatterFullTimestamp.format(obj.getReporterTime()));
            }

            if (obj.getRole() != null) {
                generator.write("role", obj.getRole().toString());
            }

            //Attachments
            if (obj.getAttachments() != null) {
                generator.writeStartArray("attachments");
                for (CadfAttachment item : obj.getAttachments()) {
                    CadfAttachment.Writer.generate(item, generator);
                }
                generator.writeEnd();
            }
            generator.writeEnd();
        }
    }

    /**
     * Parser
     */
    public static class Parser {
        private static final JsonReaderFactory JSON_READER_FACTORY = Json.createReaderFactory(null);

        private Parser() {
            // No Impl
        }

        public static CadfReporterStep parse(InputStream in)
                throws FHIRException {
            try (JsonReader jsonReader =
                    JSON_READER_FACTORY.createReader(in, StandardCharsets.UTF_8)) {
                JsonObject jsonObject = jsonReader.readObject();
                return parse(jsonObject);
            } catch (Exception e) {
                throw new FHIRException("Problem parsing the CadfReporterStep", e);
            }
        }

        public static CadfReporterStep parse(JsonObject jsonObject)
                throws FHIRException, IOException, ClassNotFoundException {
            CadfReporterStep.Builder builder =
                    CadfReporterStep.builder();

            if (jsonObject.get("reporter") != null) {
                JsonObject reporter = jsonObject.getJsonObject("reporter");
                CadfResource resource = CadfResource.Parser.parse(reporter);
                builder.reporter(resource);
            }

            if (jsonObject.get("reporterId") != null) {
                String reporterId = jsonObject.getString("reporterId");
                builder.reporterId(reporterId);
            }

            if (jsonObject.get("reporterTime") != null) {
                String rTime = jsonObject.getString("reporterTime");
                TemporalAccessor reporterTime = DATE_TIME_PARSER_FORMATTER.parse(rTime);
                builder.reporterTime(ZonedDateTime.from(reporterTime));
            }

            if (jsonObject.get("role") != null) {
                String role = jsonObject.getString("role");
                builder.role(ReporterRole.valueOf(role));
            }

            if (jsonObject.get("attachments") != null) {
                JsonArray annotations = jsonObject.getJsonArray("attachments");
                for (int i = 0; i < annotations.size(); i++) {
                    JsonObject obj = (JsonObject) annotations.get(0);
                    CadfAttachment item = CadfAttachment.Parser.parse(obj);
                    builder.attachment(item);
                }
            }

            return builder.build();
        }
    }
}