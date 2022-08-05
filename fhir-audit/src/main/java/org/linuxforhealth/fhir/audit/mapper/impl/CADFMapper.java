/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.audit.mapper.impl;

import static org.linuxforhealth.fhir.audit.AuditLogServiceConstants.DEFAULT_AUDIT_GEO_CITY;
import static org.linuxforhealth.fhir.audit.AuditLogServiceConstants.DEFAULT_AUDIT_GEO_COUNTRY;
import static org.linuxforhealth.fhir.audit.AuditLogServiceConstants.DEFAULT_AUDIT_GEO_STATE;
import static org.linuxforhealth.fhir.audit.AuditLogServiceConstants.PROPERTY_AUDIT_GEO_CITY;
import static org.linuxforhealth.fhir.audit.AuditLogServiceConstants.PROPERTY_AUDIT_GEO_COUNTRY;
import static org.linuxforhealth.fhir.audit.AuditLogServiceConstants.PROPERTY_AUDIT_GEO_STATE;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.audit.beans.AuditLogEntry;
import org.linuxforhealth.fhir.audit.beans.FHIRContext;
import org.linuxforhealth.fhir.audit.cadf.CadfAttachment;
import org.linuxforhealth.fhir.audit.cadf.CadfCredential;
import org.linuxforhealth.fhir.audit.cadf.CadfEndpoint;
import org.linuxforhealth.fhir.audit.cadf.CadfEvent;
import org.linuxforhealth.fhir.audit.cadf.CadfGeolocation;
import org.linuxforhealth.fhir.audit.cadf.CadfResource;
import org.linuxforhealth.fhir.audit.cadf.enums.Action;
import org.linuxforhealth.fhir.audit.cadf.enums.EventType;
import org.linuxforhealth.fhir.audit.cadf.enums.Outcome;
import org.linuxforhealth.fhir.audit.cadf.enums.ResourceType;
import org.linuxforhealth.fhir.audit.mapper.Mapper;
import org.linuxforhealth.fhir.config.FHIRConfigHelper;
import org.linuxforhealth.fhir.config.FHIRConfiguration;
import org.linuxforhealth.fhir.config.PropertyGroup;
import org.linuxforhealth.fhir.core.util.handler.HostnameHandler;

/**
 * AuditLogEntry to CADF Mapper
 */
public class CADFMapper implements Mapper {
    private static final String CLASSNAME = CADFMapper.class.getName();
    private static final Logger logger = java.util.logging.Logger.getLogger(CLASSNAME);

    // FHIR Operation To CADF
    private static final Map<String, Action> FHIR_TO_CADF = new HashMap<String, Action>() {
        private static final long serialVersionUID = 1L;
        {
            put("C", Action.create);
            put("R", Action.read);
            put("U", Action.update);
            put("D", Action.delete);
        }
    };

    // Managers the access to the hostname
    private static final HostnameHandler handler = new HostnameHandler();

    private CadfEvent eventObject = null;

    private String hostname = null;
    private String geoCity = null;
    private String geoState = null;
    private String geoCountry = null;

    @Override
    public Mapper init(PropertyGroup auditLogProperties) throws Exception {
        String auditHostname = FHIRConfigHelper.getStringProperty(FHIRConfiguration.PROPERTY_AUDIT_HOSTNAME, null);
        hostname = auditHostname == null ? handler.getHostname() : auditHostname;

        geoCity = auditLogProperties.getStringProperty(PROPERTY_AUDIT_GEO_CITY, DEFAULT_AUDIT_GEO_CITY);
        geoState = auditLogProperties.getStringProperty(PROPERTY_AUDIT_GEO_STATE, DEFAULT_AUDIT_GEO_STATE);
        geoCountry = auditLogProperties.getStringProperty(PROPERTY_AUDIT_GEO_COUNTRY, DEFAULT_AUDIT_GEO_COUNTRY);
        return this;
    }

    @Override
    public Mapper map(AuditLogEntry entry) throws Exception {
        eventObject = createCadfEvent(entry);
        return this;
    }

    @Override
    public String serialize() throws Exception {
        return CadfEvent.Writer.generate(eventObject);
    }

    public CadfEvent createCadfEvent(AuditLogEntry logEntry) throws IllegalStateException, IOException {
        final String METHODNAME = "createCadfEvent";
        logger.entering(CLASSNAME, METHODNAME);

        CadfResource observerRsrc = new CadfResource.Builder("fhir-server", ResourceType.compute_node)
                .geolocation(new CadfGeolocation.Builder(geoCity, geoState, geoCountry, null).build())
                .name("IBM FHIR Server - Audit")
                .host(hostname)
                .build();

        CadfEvent event = null;
        Outcome cadfEventOutCome;

        // For CADF we don't log specific event types.
        if ( logEntry.getContext() != null
                && logEntry.getContext().getAction() != null
                && logEntry.getContext().getApiParameters() != null) {
            // Define resources
            CadfResource initiator =
                    new CadfResource.Builder(logEntry.getTenantId() + "@" + logEntry.getComponentId(),
                            ResourceType.compute_machine)
                                    .geolocation(
                                            new CadfGeolocation.Builder(geoCity, geoState, geoCountry, null).build())
                                    .credential(
                                            new CadfCredential.Builder("user-" + logEntry.getUserName()).build())
                                    .host(logEntry.getComponentIp()).build();
            CadfResource target =
                    new CadfResource.Builder(
                            logEntry.getContext().getData() == null || logEntry.getContext().getData().getId() == null
                                    ? UUID.randomUUID().toString()
                                    : logEntry.getContext().getData().getId(),
                            ResourceType.data_database)
                                    .geolocation(
                                            new CadfGeolocation.Builder(geoCity, geoState, geoCountry, null).build())
                                    .address(
                                            new CadfEndpoint(logEntry.getContext().getApiParameters().getRequest(), "",
                                                    ""))
                                    .build();

            FHIRContext fhirContext = new FHIRContext(logEntry.getContext());
            fhirContext.setClient_cert_cn(logEntry.getClientCertCn());
            fhirContext.setClient_cert_issuer_ou(logEntry.getClientCertIssuerOu());
            fhirContext.setEventType(logEntry.getEventType());
            fhirContext.setLocation(logEntry.getLocation());
            fhirContext.setDescription(logEntry.getDescription());

            /*
             * @implNote This applies to the next two methods: outcomeDesc, outcome
             * Previously, it had pending as an option, however it should now be success/failure in all cases.
             */
            if (logEntry.getContext().getApiParameters().getStatus() < 400) {
                cadfEventOutCome = Outcome.success;
            } else {
                cadfEventOutCome = Outcome.failure;
            }

            event = new CadfEvent.Builder(
                            logEntry.getContext().getRequestUniqueId() == null ? UUID.randomUUID().toString()
                                    : logEntry.getContext().getRequestUniqueId(),
                                    EventType.activity, logEntry.getTimestamp(),
                                    FHIR_TO_CADF.getOrDefault(logEntry.getContext().getAction(), Action.unknown),
                                    cadfEventOutCome)
                            .observer(observerRsrc)
                            .initiator(initiator)
                            .target(target)
                            .tag(logEntry.getCorrelationId())
                            .attachment(new CadfAttachment("application/json",
                                            FHIRContext.FHIRWriter.generate(fhirContext)))
                            .build();
        }

        logger.exiting(CLASSNAME, METHODNAME);
        return event;
    }
}