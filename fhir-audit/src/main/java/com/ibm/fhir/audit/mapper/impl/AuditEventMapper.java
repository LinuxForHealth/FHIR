/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.audit.mapper.impl;

import static com.ibm.fhir.model.type.Code.code;
import static com.ibm.fhir.model.type.String.string;
import static com.ibm.fhir.model.type.Uri.uri;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.ibm.fhir.audit.AuditLogEventType;
import com.ibm.fhir.audit.beans.AuditLogEntry;
import com.ibm.fhir.audit.beans.FHIRContext;
import com.ibm.fhir.audit.mapper.Mapper;
import com.ibm.fhir.config.PropertyGroup;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.resource.AuditEvent;
import com.ibm.fhir.model.resource.AuditEvent.Agent;
import com.ibm.fhir.model.resource.AuditEvent.Agent.Network;
import com.ibm.fhir.model.resource.AuditEvent.Entity;
import com.ibm.fhir.model.resource.AuditEvent.Entity.Detail;
import com.ibm.fhir.model.resource.AuditEvent.Source;
import com.ibm.fhir.model.type.Base64Binary;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.code.AuditEventAction;
import com.ibm.fhir.model.type.code.AuditEventAgentNetworkType;

/**
 * This class adds support for AuditEvent
 * https://www.hl7.org/fhir/r4/auditevent.html
 */
public class AuditEventMapper implements Mapper {
    private static final String CLASSNAME = AuditEventMapper.class.getName();
    private static final Logger logger = java.util.logging.Logger.getLogger(CLASSNAME);

    //@formatter:off
    private static final Coding TYPE = Coding.builder()
            .code(code("rest"))
            .display(string("Restful Operation"))
            .system(uri("http://terminology.hl7.org/CodeSystem/audit-event-type"))
            .build();
    //@formatter:on

    //@formatter:off
    private static final Coding SUBTYPE_READ = Coding.builder()
            .code(code("read"))
            .display(string("read"))
            .system(uri("http://hl7.org/fhir/restful-interaction"))
            .build();
    //@formatter:on

    //@formatter:off
    private static final Coding SUBTYPE_VREAD = Coding.builder()
            .code(code("vread"))
            .display(string("vread"))
            .system(uri("http://hl7.org/fhir/restful-interaction"))
            .build();
    //@formatter:on

    //@formatter:off
    private static final Coding SUBTYPE_UPDATE = Coding.builder()
            .code(code("update"))
            .display(string("update"))
            .system(uri("http://hl7.org/fhir/restful-interaction"))
            .build();
    //@formatter:on

    //@formatter:off
    private static final Coding SUBTYPE_PATCH = Coding.builder()
            .code(code("patch"))
            .display(string("patch"))
            .system(uri("http://hl7.org/fhir/restful-interaction"))
            .build();
    //@formatter:on

    //@formatter:off
    private static final Coding SUBTYPE_DELETE = Coding.builder()
            .code(code("delete"))
            .display(string("delete"))
            .system(uri("http://hl7.org/fhir/restful-interaction"))
            .build();
    //@formatter:on

    //@formatter:off
    private static final Coding SUBTYPE_HISTORY = Coding.builder()
            .code(code("history"))
            .display(string("history"))
            .system(uri("http://hl7.org/fhir/restful-interaction"))
            .build();
    //@formatter:on

    //@formatter:off
    @SuppressWarnings("unused")
    private static final Coding SUBTYPE_HISTORY_INSTANCE = Coding.builder()
            .code(code("history-instance"))
            .display(string("history-instance"))
            .system(uri("http://hl7.org/fhir/restful-interaction"))
            .build();
    //@formatter:on

    //@formatter:off
    @SuppressWarnings("unused")
    private static final Coding SUBTYPE_HISTORY_TYPE = Coding.builder()
            .code(code("history-type"))
            .display(string("history-type"))
            .system(uri("http://hl7.org/fhir/restful-interaction"))
            .build();
    //@formatter:on

    //@formatter:off
    @SuppressWarnings("unused")
    private static final Coding SUBTYPE_HISTORY_SYSTEM = Coding.builder()
            .code(code("history-system"))
            .display(string("history-system"))
            .system(uri("http://hl7.org/fhir/restful-interaction"))
            .build();
    //@formatter:on

    //@formatter:off
    private static final Coding SUBTYPE_CREATE = Coding.builder()
            .code(code("create"))
            .display(string("create"))
            .system(uri("http://hl7.org/fhir/restful-interaction"))
            .build();
    //@formatter:on

    //@formatter:off
    private static final Coding SUBTYPE_SEARCH = Coding.builder()
            .code(code("search"))
            .display(string("search"))
            .system(uri("http://hl7.org/fhir/restful-interaction"))
            .build();
    //@formatter:on

    //@formatter:off
    @SuppressWarnings("unused")
    private static final Coding SUBTYPE_SEARCH_TYPE = Coding.builder()
            .code(code("search-type"))
            .display(string("search-type"))
            .system(uri("http://hl7.org/fhir/restful-interaction"))
            .build();
    //@formatter:on

    //@formatter:off
    @SuppressWarnings("unused")
    private static final Coding SUBTYPE_SEARCH_SYSTEM = Coding.builder()
            .code(code("search-system"))
            .display(string("search-system"))
            .system(uri("http://hl7.org/fhir/restful-interaction"))
            .build();
    //@formatter:on

    //@formatter:off
    private static final Coding SUBTYPE_CAPABILITIES = Coding.builder()
            .code(code("capabilities"))
            .display(string("capabilities"))
            .system(uri("http://hl7.org/fhir/restful-interaction"))
            .build();
    //@formatter:on

    //@formatter:off
    @SuppressWarnings("unused")
    private static final Coding SUBTYPE_TRANSACTION = Coding.builder()
            .code(code("transaction"))
            .display(string("transaction"))
            .system(uri("http://hl7.org/fhir/restful-interaction"))
            .build();
    //@formatter:on

    //@formatter:off
    private static final Coding SUBTYPE_BATCH = Coding.builder()
            .code(code("batch"))
            .display(string("batch"))
            .system(uri("http://hl7.org/fhir/restful-interaction"))
            .build();
    //@formatter:on

    //@formatter:off
    private static final Coding SUBTYPE_OPERATION = Coding.builder()
            .code(code("operation"))
            .display(string("operation"))
            .system(uri("http://hl7.org/fhir/restful-interaction"))
            .build();
    //@formatter:on

    //@formatter:off
    private static final Coding SOURCE_AUDIT = Coding.builder()
            .code(code("4"))
            .display(string("Application Server"))
            .system(uri("http://terminology.hl7.org/CodeSystem/security-source-type"))
            .build();
    //@formatter:on

    //@formatter:off
    private static final CodeableConcept ROLE_DATA_COLLECTOR = CodeableConcept.builder()
            .coding(Coding.builder()
                .code(code("datacollector"))
                .display(string("datacollector"))
                .system(uri("http://terminology.hl7.org/CodeSystem/extra-security-role-type"))
                .build())
            .build();
    //@formatter:on

    // These are loaded one time when the JVM starts up and this class is activated.
    private static final Map<String, Coding> MAP_TO_SUBTYPE = buildMapToSubtype();
    private static final Map<String, AuditEventAction> MAP_TO_ACTION = buildMapToAction();

    // Map String to Code SubTypes
    private static Map<String, Coding> buildMapToSubtype() {
        Map<String, Coding> map = new HashMap<>();
        map.put(AuditLogEventType.FHIR_READ.value(), SUBTYPE_READ);
        map.put(AuditLogEventType.FHIR_VREAD.value(), SUBTYPE_VREAD);
        map.put(AuditLogEventType.FHIR_UPDATE.value(), SUBTYPE_UPDATE);
        map.put(AuditLogEventType.FHIR_PATCH.value(), SUBTYPE_PATCH);
        map.put(AuditLogEventType.FHIR_DELETE.value(), SUBTYPE_DELETE);
        map.put(AuditLogEventType.FHIR_HISTORY.value(), SUBTYPE_HISTORY);
        map.put(AuditLogEventType.FHIR_CREATE.value(), SUBTYPE_CREATE);
        map.put(AuditLogEventType.FHIR_SEARCH.value(), SUBTYPE_SEARCH);
        map.put(AuditLogEventType.FHIR_METADATA.value(), SUBTYPE_CAPABILITIES);
        map.put(AuditLogEventType.FHIR_BUNDLE.value(), SUBTYPE_BATCH);
        map.put(AuditLogEventType.FHIR_OPERATION.value(), SUBTYPE_OPERATION);
        map.put(AuditLogEventType.FHIR_VALIDATE.value(), SUBTYPE_OPERATION);

        // Audit Log Event Type that don't exist yet, probably need to be mapped at some point.
        // map.put(AuditLogEventType.FHIR_HISTORY_INSTANCE.value(), SUBTYPE_HISTORY_INSTANCE);
        // map.put(AuditLogEventType.FHIR_HISTORY_TYPE.value(), SUBTYPE_HISTORY_TYPE);
        // map.put(AuditLogEventType.FHIR_HISTORY_SYSTEM.value(), SUBTYPE_HISTORY_SYSTEM);
        // map.put(AuditLogEventType.FHIR_SEARCH_SYSTEM_TYPE.value(), SUBTYPE_SEARCH_TYPE);
        // map.put(AuditLogEventType.FHIR_SEARCH_SYSTEM.value(), SUBTYPE_SEARCH_SYSTEM);
        // map.put(AuditLogEventType.FHIR_TRANSACTION.value(), SUBTYPE_TRANSACTION);
        // map.put(AuditLogEventType.FHIR_BATCH.value(), SUBTYPE_BATCH);
        // map.put(AuditLogEventType.FHIR_OPERATION_BULKDATA_IMPORT.value(), SUBTYPE_OPERATION);
        // map.put(AuditLogEventType.FHIR_OPERATION_BULKDATA_IMPORT.value(), SUBTYPE_OPERATION);
        // map.put(AuditLogEventType.FHIR_OPERATION_BULKDATA_SEARCH.value(), SUBTYPE_OPERATION);
        return map;
    }

    // Map to AuditEventAction
    private static Map<String, com.ibm.fhir.model.type.code.AuditEventAction> buildMapToAction() {
        Map<String, com.ibm.fhir.model.type.code.AuditEventAction> map = new HashMap<>();
        map.put(AuditLogEventType.FHIR_READ.value(), AuditEventAction.R);
        map.put(AuditLogEventType.FHIR_VREAD.value(), AuditEventAction.R);
        map.put(AuditLogEventType.FHIR_UPDATE.value(), AuditEventAction.U);
        map.put(AuditLogEventType.FHIR_PATCH.value(), AuditEventAction.U);
        map.put(AuditLogEventType.FHIR_DELETE.value(), AuditEventAction.D);
        map.put(AuditLogEventType.FHIR_HISTORY.value(), AuditEventAction.R);
        map.put(AuditLogEventType.FHIR_CREATE.value(), AuditEventAction.C);
        map.put(AuditLogEventType.FHIR_SEARCH.value(), AuditEventAction.E);
        map.put(AuditLogEventType.FHIR_METADATA.value(), AuditEventAction.E);
        map.put(AuditLogEventType.FHIR_BUNDLE.value(), AuditEventAction.E);
        map.put(AuditLogEventType.FHIR_OPERATION.value(), AuditEventAction.E);
        map.put(AuditLogEventType.FHIR_VALIDATE.value(), AuditEventAction.E);

        // Audit Log Event Type that don't exist yet, probably need to be mapped at some point.
        // map.put(AuditLogEventType.FHIR_HISTORY_INSTANCE.value(), AuditEventAction.R);
        // map.put(AuditLogEventType.FHIR_HISTORY_TYPE.value(), AuditEventAction.R);
        // map.put(AuditLogEventType.FHIR_HISTORY_SYSTEM.value(), AuditEventAction.R);
        // map.put(AuditLogEventType.FHIR_SEARCH_SYSTEM_TYPE.value(), AuditEventAction.E);
        // map.put(AuditLogEventType.FHIR_SEARCH_SYSTEM.value(), AuditEventAction.E);
        // map.put(AuditLogEventType.FHIR_TRANSACTION.value(), AuditEventAction.E);
        // map.put(AuditLogEventType.FHIR_BATCH.value(), AuditEventAction.E);
        // map.put(AuditLogEventType.FHIR_OPERATION_BULKDATA_IMPORT.value(), AuditEventAction.E);
        // map.put(AuditLogEventType.FHIR_OPERATION_BULKDATA_EXPORT.value(), AuditEventAction.E);
        // map.put(AuditLogEventType.FHIR_OPERATION_BULKDATA_SEARCH.value(), AuditEventAction.E);
        return map;
    }

    private AuditEvent.Builder builder = AuditEvent.builder();

    @Override
    public Mapper init(PropertyGroup auditLogProperties) throws Exception {
        // Eventually we'll populate
        return this;
    }

    @Override
    public Mapper map(AuditLogEntry entry) throws Exception {
        final String METHODNAME = "map";
        logger.entering(CLASSNAME, METHODNAME);

        // Everything on this server is an initiated RESTFUL Operation
        // @link https://www.hl7.org/fhir/valueset-audit-event-type.html
        // @formatter:off
        builder.type(TYPE)
            // We map to a single sub-type, however, in the future we may use multiple.
            // @link https://www.hl7.org/fhir/valueset-audit-event-sub-type.html#expansion
            .subtype(Arrays.asList(MAP_TO_SUBTYPE.get(entry.getEventType())))
            // Mapping our AuditEventType to a specific C-R-U-D-E
            // @link https://www.hl7.org/fhir/valueset-audit-event-action.html
            .action(MAP_TO_ACTION.get(entry.getEventType()))
            // Period involves start/end
            .period(period(entry))
            // Now, when we are creating this AuditEvent
            .recorded(Instant.now())
            // Agent is the Actor involved in the event
            .agent(agent(entry))
            // Whether the event succeeded or failed
            .outcome(outcome(entry))
            // Description of the event outcome
            .outcomeDesc(outcomeDesc(entry))
            // The purposeOfUse of the event
            .purposeOfEvent(purposeOfEvent(entry))
            // source is the 'Audit Event Reporter' this server
            .source(source(entry))
            // Data or objects used in the audit
            .entity(entity(entry));
        // @formatter:on
        logger.exiting(CLASSNAME, METHODNAME);
        return this;
    }

    @Override
    public String serialize() throws Exception {
        AuditEvent auditEvent = builder.build();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            FHIRGenerator.generator(Format.JSON).generate(auditEvent, out);
            return out.toString();
        } catch (Exception e) {
            throw new Exception("Failed to serialize the Audit Event", e);
        }
    }

    /*
     * generate a period from the audit log entry
     * for now we just hard code the now time.
     */
    private com.ibm.fhir.model.type.Period period(AuditLogEntry entry) {
        // @formatter:off
        return com.ibm.fhir.model.type.Period.builder()
                .start(com.ibm.fhir.model.type.DateTime.of(
                    ZonedDateTime.of(Timestamp.valueOf(entry.getContext().getStartTime()).toLocalDateTime(), ZoneId.of("UTC"))
                    ))
                .end(com.ibm.fhir.model.type.DateTime.of(
                    ZonedDateTime.of(Timestamp.valueOf(entry.getContext().getEndTime()).toLocalDateTime(), ZoneId.of("UTC"))
                    ))
            .build();
        // @formatter:on
    }

    // Agent is loaded, but may end up being more than one agent
    // Data Collector, Data Producer, Data Subject et cetra.
    private List<Agent> agent(AuditLogEntry entry) {
        Agent collector = Agent.builder()
                .requestor(com.ibm.fhir.model.type.Boolean.TRUE)
                .role(ROLE_DATA_COLLECTOR)
                .name(string(entry.getComponentId()))
                .network(Network.builder()
                    .type(AuditEventAgentNetworkType.TYPE_1)
                    .address(string(entry.getComponentIp()))
                    .build())
                .build();
        return Arrays.asList(collector);
    }

    /*
     * entity may need refactoring to accommodate various usecases.
     */
    private Entity entity(AuditLogEntry entry) throws IOException {
        // @formatter:off
        Entity.Builder builder = Entity.builder()
                .securityLabel(Coding.builder()
                                .code(code("N"))
                                .display(string("normal"))
                                .system(uri("http://terminology.hl7.org/CodeSystem/v3-Confidentiality"))
                                .build())
                .description(string(entry.getDescription()));

        if (entry.getContext() != null) {
            FHIRContext fhirContext = new FHIRContext(entry.getContext());
            fhirContext.setClient_cert_cn(entry.getClientCertCn());
            fhirContext.setClient_cert_issuer_ou(entry.getClientCertIssuerOu());
            fhirContext.setEventType(entry.getEventType());
            fhirContext.setLocation(entry.getLocation());
            fhirContext.setDescription(entry.getDescription());
            String value = FHIRContext.FHIRWriter.generate(fhirContext);
            builder.detail(
                Detail.builder()
                    .type(string("FHIR Context"))
                    .value(Base64Binary.builder()
                            .value(value.getBytes())
                            .build())
                    .build());
        }

        // @formatter:on
        if (AuditLogEventType.FHIR_SEARCH.value().equals(entry.getEventType())) {
            String value = entry.getLocation() + "/" + entry.getContext().getQueryParameters();
            builder.query(Base64Binary.builder()
                .value(value.getBytes())
                .build());
        }
        return builder.build();
    }

    /*
     * uses the location from the audit properties as passed in to AuditLogEntrys
     */
    private Source source(AuditLogEntry entry) {
        // @formatter:off
        return Source.builder()
                .type(SOURCE_AUDIT)
                .site(string(entry.getLocation()))
                .observer(Reference.builder()
                            .reference(string("Device/"+ entry.getComponentId()))
                            .build())
                .build();
        // @formatter:on
    }

    private List<CodeableConcept> purposeOfEvent(AuditLogEntry entry) {
        /*
         * this may be something to make more extensible in the future and is here
         * as a method to facilitate.
         *
         * @link https://www.hl7.org/fhir/v3/PurposeOfUse/vs.html
         */
        // @formatter:off
        return Arrays.asList(CodeableConcept.builder()
                    .coding(Coding.builder()
                            .code(code("PurposeOfUse"))
                            .display(string("PurposeOfUse"))
                            .system(uri("http://terminology.hl7.org/CodeSystem/v3-ActReason"))
                            .build())
                    .build());
        // @formatter:on
    }

    /*
     * in the future, we could
     * enhance this part to log the alternative outcomes.
     * This applies to the next two methods: outcomeDesc, outcome
     */
    private com.ibm.fhir.model.type.String outcomeDesc(AuditLogEntry entry) {
        if (entry.getContext().getEndTime() == null ||
                entry.getContext().getStartTime().equalsIgnoreCase(entry.getContext().getEndTime())) {
            return string("pending");
        } else if (entry.getContext().getApiParameters().getStatus() < 400) {
            return string("success");
        } else if (entry.getContext().getApiParameters().getStatus() < 500) {
            return string("minor failure");
        } else {
            return string("major failure");
        }
    }

    // Specifies the outcome as a success, minor or major
    private com.ibm.fhir.model.type.code.AuditEventOutcome outcome(AuditLogEntry entry) {
        if (entry.getContext().getApiParameters().getStatus() < 400) {
            return com.ibm.fhir.model.type.code.AuditEventOutcome.OUTCOME_0;
        } else if (entry.getContext().getApiParameters().getStatus() < 500) {
            return com.ibm.fhir.model.type.code.AuditEventOutcome.OUTCOME_4;
        } else {
            return com.ibm.fhir.model.type.code.AuditEventOutcome.OUTCOME_12;
        }
    }
}