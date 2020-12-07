/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.audit.logging.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.ibm.fhir.audit.cadf.model.CadfAttachment;
import com.ibm.fhir.audit.cadf.model.CadfCredential;
import com.ibm.fhir.audit.cadf.model.CadfEndpoint;
import com.ibm.fhir.audit.cadf.model.CadfEvent;
import com.ibm.fhir.audit.cadf.model.CadfGeolocation;
import com.ibm.fhir.audit.cadf.model.CadfResource;
import com.ibm.fhir.audit.cadf.model.enums.Action;
import com.ibm.fhir.audit.cadf.model.enums.EventType;
import com.ibm.fhir.audit.cadf.model.enums.Outcome;
import com.ibm.fhir.audit.cadf.model.enums.ResourceType;
import com.ibm.fhir.audit.kafka.Environment;
import com.ibm.fhir.audit.kafka.EventStreamsCredentials;
import com.ibm.fhir.audit.logging.api.AuditLogEventType;
import com.ibm.fhir.audit.logging.api.AuditLogService;
import com.ibm.fhir.audit.logging.beans.AuditLogEntry;
import com.ibm.fhir.audit.logging.beans.impl.context.FHIRContext;
import com.ibm.fhir.config.PropertyGroup;
import com.ibm.fhir.exception.FHIRException;

/**
 * This class is a Cadf/EventStream/COS based implementation of the FHIR server
 * AuditLogService interface
 */
public class WhcAuditCadfLogService implements AuditLogService {
    private static final Logger logger = java.util.logging.Logger.getLogger(WhcAuditCadfLogService.class.getName());
    private static final String CLASSNAME = WhcAuditCadfLogService.class.getName();
    public static final String PROPERTY_AUDIT_KAFKA_BOOTSTRAPSERVERS = "kafkaServers";
    public static final String PROPERTY_AUDIT_KAFKA_APIKEY = "kafkaApiKey";
    private static final String PROPERTY_AUDIT_KAFKA_TOPIC = "auditTopic";
    private static final String PROPERTY_AUDIT_GEO_CITY = "geoCity";
    private static final String PROPERTY_AUDIT_GEO_STATE = "geoState";
    private static final String PROPERTY_AUDIT_GEO_COUNTRY = "geoCounty";
    private static final String HEALTHCHECKOP = "healthcheck";

    private static final String KAFKA_USERNAME = "token";
    private static final String DEFAULT_AUDIT_KAFKA_TOPIC = "FHIR_AUDIT";
    private static final String DEFAULT_AUDIT_GEO_CITY = "Dallas";
    private static final String DEFAULT_AUDIT_GEO_STATE = "TX";
    private static final String DEFAULT_AUDIT_GEO_COUNTRY = "US";

    private KafkaProducer<String, String> producer = null;
    private static String bootstrapServers = null;
    private static String apiKey = null;
    private static String auditTopic = DEFAULT_AUDIT_KAFKA_TOPIC;
    private static String geoCity = DEFAULT_AUDIT_GEO_CITY;
    private static String geoState = DEFAULT_AUDIT_GEO_STATE;
    private static String geoCountry = DEFAULT_AUDIT_GEO_COUNTRY;

    private static final Set<String> IGNORED_AUDIT_EVENT_TYPE = createIgnoredLogEventType();

    private boolean isEnabled = false;

    private static final Map<String, Action> fhir2CadfMap = new HashMap<String, Action>() {
        private static final long serialVersionUID = 1L;
        {
            put("C", Action.create);
            put("R", Action.read);
            put("U", Action.update);
            put("D", Action.delete);
        }
    };

    /*
     * creates an internal map of ignored Event Types.
     */
    private static Set<String> createIgnoredLogEventType() {
        Set<String> set = new HashSet<>();
        set.add(AuditLogEventType.FHIR_CONFIGDATA.value());
        set.add(AuditLogEventType.FHIR_METADATA.value());
        return set;
    }

    // Initialize CADF OBSERVER resource object once
    private static CadfResource observerRsrc =
            new CadfResource.Builder("fhir-server", ResourceType.compute_node)
                            .geolocation(new CadfGeolocation.Builder(geoCity, geoState, geoCountry, null).build())
                            .name("IBM FHIR Server - Audit")
                            .host(System.getenv("HOSTNAME"))
                            .build();

    public WhcAuditCadfLogService() {
        super();
    }

    @Override
    public void initialize(PropertyGroup auditLogProperties) throws Exception {
        final String METHODNAME = "initialize";
        logger.entering(CLASSNAME, METHODNAME);

        // Check environment: EVENT_STREAMS_AUDIT_BINDING to obtain configuration parameters for
        // kafka (Kubernetes Container)
        if (System.getenv(Environment.KUB_EVENTSTREAMS_BINDING) != null) {
            logger.log(Level.INFO, "Using env var " + Environment.KUB_EVENTSTREAMS_BINDING + " to find credentials.");
            EventStreamsCredentials credentials = Environment.getEventStreamsCredentials();
            if (credentials != null) {
                bootstrapServers = Environment.stringArrayToCSV(credentials.getKafkaBrokersSasl());
                apiKey           = credentials.getApiKey();
            }
        }

        // If fails to get config from environment, then try to get them from FHIR config
        if (bootstrapServers == null || bootstrapServers.length() < 10 || apiKey == null || apiKey.length() < 10) {
            Objects.requireNonNull(auditLogProperties, "Audit log properties cannot be null.");
            logger.log(Level.INFO, "Using FHIR config to find credentials.");
            bootstrapServers = auditLogProperties.getStringProperty(PROPERTY_AUDIT_KAFKA_BOOTSTRAPSERVERS);
            apiKey           = auditLogProperties.getStringProperty(PROPERTY_AUDIT_KAFKA_APIKEY);
        }

        // If still fails to get config for kafka producer, then throw <pre>exception</pre>
        if (bootstrapServers == null || bootstrapServers.length() < 10 || apiKey == null || apiKey.length() < 10) {
            throw new FHIRException("Can not get kafka settings!");
        }

        // Now, let's get the audit topic from FHIR config, if not found, then use the default topic
        if (auditLogProperties != null) {
            auditTopic = auditLogProperties.getStringProperty(PROPERTY_AUDIT_KAFKA_TOPIC, DEFAULT_AUDIT_KAFKA_TOPIC);
            geoCity    = auditLogProperties.getStringProperty(PROPERTY_AUDIT_GEO_CITY, DEFAULT_AUDIT_GEO_CITY);
            geoState   = auditLogProperties.getStringProperty(PROPERTY_AUDIT_GEO_STATE, DEFAULT_AUDIT_GEO_STATE);
            geoCountry = auditLogProperties.getStringProperty(PROPERTY_AUDIT_GEO_COUNTRY, DEFAULT_AUDIT_GEO_COUNTRY);
        }

        Properties kafkaProps = new Properties();
        kafkaProps.put("sasl.jaas.config", String.format(
                "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"%s\" password=\"%s\";",
                KAFKA_USERNAME, apiKey));
        kafkaProps.put("security.protocol", "SASL_SSL");
        kafkaProps.put("bootstrap.servers", bootstrapServers);
        kafkaProps.put("sasl.mechanism", "PLAIN");
        kafkaProps.put("ssl.protocol", "TLSv1.2");
        kafkaProps.put("ssl.enabled.protocols", "TLSv1.2");
        kafkaProps.put("ssl.endpoint.identification.algorithm", "HTTPS");
        kafkaProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        this.producer = new KafkaProducer<>(kafkaProps);

        if (this.producer == null) {
            throw new FHIRException("Failed to init kafka producer!");
        } else {
            logger.info("Initialized Audit logger.");
            this.isEnabled = true;
        }

        logger.exiting(CLASSNAME, METHODNAME);
    }

    @Override
    public void logEntry(AuditLogEntry logEntry) throws Exception {
        final String METHODNAME = "logEntry";
        logger.entering(CLASSNAME, METHODNAME);

        // skip healthcheck operation
        if (logEntry == null || logEntry.getContext() == null
                || (logEntry.getContext().getOperationName() != null
                && logEntry.getContext().getOperationName().equalsIgnoreCase(HEALTHCHECKOP))) {
            return;
        }

        CadfEvent eventObject = createCadfEvent(logEntry);

        if (eventObject != null) {
            String eventString = CadfEvent.Writer.generate(eventObject);
            ProducerRecord<String, String> record = new ProducerRecord<>(auditTopic, eventString);
            // Block till the message is sent to kafka server.
            this.producer.send(record).get();
        }

        logger.exiting(CLASSNAME, METHODNAME);
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }

    /**
     * @param logEntry
     * @return
     * @throws IllegalStateException
     * @throws IOException
     */
    public static CadfEvent createCadfEvent(AuditLogEntry logEntry)
            throws IllegalStateException, IOException {
        final String METHODNAME = "createCadfEvent";
        logger.entering(CLASSNAME, METHODNAME);

        CadfEvent event = null;
        Outcome cadfEventOutCome;

        // For CADF we don't log specific event types.
        if ( !IGNORED_AUDIT_EVENT_TYPE.contains(logEntry.getEventType())
                && logEntry.getContext() != null
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

            if (logEntry.getContext().getEndTime() == null ||
                    logEntry.getContext().getStartTime().equalsIgnoreCase(logEntry.getContext().getEndTime())) {
                cadfEventOutCome = Outcome.pending;
            } else if (logEntry.getContext().getApiParameters().getStatus() < 400) {
                cadfEventOutCome = Outcome.success;
            } else {
                cadfEventOutCome = Outcome.failure;
            }

            event = new CadfEvent.Builder(
                            logEntry.getContext().getRequestUniqueId() == null ? UUID.randomUUID().toString()
                                    : logEntry.getContext().getRequestUniqueId(),
                                    EventType.activity, logEntry.getTimestamp(),
                                    fhir2CadfMap.getOrDefault(logEntry.getContext().getAction(), Action.unknown),
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

    // release kafka resource for producer
    public void stop() {
        if (this.producer != null) {
            try {
                this.producer.close();
            } catch (Exception e) {
                logger.info(e.getMessage());
            }
        }
    }
}
