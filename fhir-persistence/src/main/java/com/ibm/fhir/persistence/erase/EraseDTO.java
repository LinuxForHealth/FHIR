/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.erase;

/**
 * The data transfer object with the necessary elements used to process
 * the Erase of a resource specified by <code>TYPE/LOGICAL_ID[/version]</code>.
 */
public class EraseDTO {

    // TYPE/LOGICAL_ID[/version]
    private String resourceType;
    private String logicalId;
    private Integer version;

    // The patient that the erase operation may be executed on.
    private String patient;

    // The reason that the erase operation is executed.
    private String reason;

    /**
     * @return the patient identifier
     */
    public String getPatient() {
        return patient;
    }

    /**
     * sets the identifier.
     *
     * @param patient
     *            the patient identifier
     */
    public void setPatient(String patient) {
        this.patient = patient;
    }

    /**
     * @return the reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * @param reason
     *            the reason to set
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * @return the resourceType
     */
    public String getResourceType() {
        return resourceType;
    }

    /**
     * @param resourceType
     *            the resourceType to set
     */
    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    /**
     * @return the logicalId
     */
    public String getLogicalId() {
        return logicalId;
    }

    /**
     * @param logicalId
     *            the logicalId to set
     */
    public void setLogicalId(String logicalId) {
        this.logicalId = logicalId;
    }

    /**
     * @return the version
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * @param version
     *            the version to set
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * Generates a reference from the input.
     * @return
     */
    public String generateReference() {
        // Reference to the specific Resource/[ID], or the version of that id that is deleted.
        StringBuilder reference = new StringBuilder();
        reference.append(getResourceType())
            .append("/")
            .append(getLogicalId());
        if (version != null) {
            // version specific at this point, such as version 54 of a specific resource
            // <code>Patient/17941bac037-ea376c66-69d4-4afe-a204-041bb25cc26e/_history/54</code>
            reference.append("/_history/")
                .append(getVersion());
        }
        return reference.toString();
    }

    @Override
    public String toString() {
        return "EraseDTO [patient=" + patient + ", reason=" + reason + ", resourceType=" + resourceType
                + ", logicalId=" + logicalId + ", version=" + version + "]";
    }
}
