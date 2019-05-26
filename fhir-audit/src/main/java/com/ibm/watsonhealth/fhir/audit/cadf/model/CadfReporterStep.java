/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.audit.cadf.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public final class CadfReporterStep {
    private final CadfEvent.ReporterRole role;
    private final CadfResource reporter;
    private final String reporterId;
    private final Date reporterTime;
    private final ArrayList<CadfAttachment> attachments;

    private CadfReporterStep(Builder builder) {
        this.role = builder.role;
        this.reporter = builder.reporter;
        this.reporterId = builder.reporterId;
        this.reporterTime = builder.reporterTime;
        this.attachments = builder.attachments;
    }

    /**
     * Validate contents of the reporter step type.
     * 
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

    /**
     * @return the reporterTime
     */
    public Date getReporterTime() {
        return reporterTime;
    }

    /**
     * @return the attachments
     */
    public ArrayList<CadfAttachment> getAttachments() {
        return attachments;
    }

    public static class Builder {
        private CadfEvent.ReporterRole role;
        private CadfResource reporter;
        private String reporterId;
        private Date reporterTime;
        private ArrayList<CadfAttachment> attachments;

        /**
         * Construct a ReporterStep object. This type represents a step in the
         * REPORTERCHAIN that captures information about any notable REPORTER (in
         * addition to the OBSERVER) that modified or relayed the CADF Event Record and
         * any details regarding any modification it performed on the CADF Event Record
         * it is contained within.
         * 
         * @param role    - The role the REPORTER performed on the CADF Event Record
         *                (e.g., an "observer", "modifier" or "relay" role). @see
         *                {@link CadfEvent#ReporterRole}
         * @param rep     - This property defines the resource that acted as a REPORTER
         *                on a CADF Event Record. It is required if repId is not
         *                supplied.
         * @param repId   - This property identifies a resource that acted as a REPORTER
         *                on a CADF Event Record by reference and whose definition
         *                exists outside the event record itself (e.g., within the same
         *                CADF Log or Report). Note: This property can be used instead
         *                of the "reporter" property to reference a valid CADF Resource
         *                definition, which is already defined and can be referenced by
         *                its identifier (e.g., a CADF Resource already defined within
         *                the same CADF Event record or at the CADF Log or Report level
         *                that also contains the referencing CADF Event record).
         * @param repTime - Optional. The time a REPORTER adds its Reporterstep entry
         *                into the REPORTERCHAIN (which follows completion of any
         *                updates to or handling of the corresponding CADF Event
         *                Record).
         */
        public Builder(CadfEvent.ReporterRole role, CadfResource rep, String repId, Date repTime) {
            this.role = role;
            this.reporter = rep;
            this.reporterId = repId;
            if (repTime != null) {
                this.reporterTime = repTime;
            } else {
                this.reporterTime = new Date();
            }
        }

        /**
         * An optional array of additional data containing information about the
         * reporter or any action it performed that affected the CADF Event Record
         * contents.
         */
        public Builder withAttachments(CadfAttachment[] attachments) {
            this.attachments = new ArrayList<CadfAttachment>(Arrays.asList(attachments));
            return this;
        }

        /**
         * An optional array of additional data containing information about the
         * reporter or any action it performed that affected the CADF Event Record
         * contents.
         */
        public Builder withAttachments(ArrayList<CadfAttachment> attachments) {
            this.attachments = attachments;
            return this;
        }

        /**
         * A convenience method to add one attachment at a time.
         * 
         * @see withAttachments()
         */
        public Builder withAttachment(CadfAttachment attachment) {
            if (this.attachments == null) {
                attachments = new ArrayList<CadfAttachment>();
            }
            this.attachments.add(attachment);
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
}
