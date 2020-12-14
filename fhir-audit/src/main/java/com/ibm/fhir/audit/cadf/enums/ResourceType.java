/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.audit.cadf.enums;

/**
 * This class represents the CADF Resource type taxonomy.
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

    public String value() {
        return uri;
    }

    public static ResourceType of(String value) {
        for (ResourceType entryType : ResourceType.values()) {
            if (entryType.value().equalsIgnoreCase(value)) {
                return entryType;
            }
        }
        throw new IllegalArgumentException("No constant with value " + value + " found.");
    }
}