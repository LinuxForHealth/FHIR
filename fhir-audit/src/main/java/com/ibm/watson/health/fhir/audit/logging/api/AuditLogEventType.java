/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.audit.logging.api;

/**
 * This enum defines allowable types of audit log events that can be logged using the FHIR server Audit Log Service.
 * @author markd
 *
 */
public enum AuditLogEventType {
    
    FHIR_CREATE("fhir-create"), 
    
    FHIR_UPDATE("fhir-update"), 
    
    FHIR_PATCH("fhir-patch"), 

    FHIR_DELETE("fhir-delete"), 
    
    FHIR_READ("fhir-read"), 
    
    FHIR_VREAD("fhir-version-read"),
    
    FHIR_HISTORY("fhir-history"),
    
    FHIR_SEARCH("fhir-search"),
    
    FHIR_BUNDLE("fhir-bundle"),
    
    FHIR_VALIDATE("fhir-validate"),
    
    FHIR_METADATA("fhir-metadata"),
    
    FHIR_CONFIGDATA("fhir-configdata"), 
    
    FHIR_OPERATION("fhir-operation");
        
    
        
    private String value = null;
    
    AuditLogEventType(String value) {
        this.value = value;
    }
    
    public String value() {
        return value;
    }
    
    public static AuditLogEventType fromValue(String value) {
        for (AuditLogEventType entryType : AuditLogEventType.values()) {
            if (entryType.value.equalsIgnoreCase(value)) {
                return entryType;
            }
        }
        throw new IllegalArgumentException("No constant with value " + value + " found.");
    }
}
