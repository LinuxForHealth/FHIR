/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.audit.logging.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibm.watsonhealth.fhir.audit.cadf.model.CadfAttachment;
import com.ibm.watsonhealth.fhir.audit.cadf.model.CadfCredential;
import com.ibm.watsonhealth.fhir.audit.cadf.model.CadfEndpoint;
import com.ibm.watsonhealth.fhir.audit.cadf.model.CadfEvent;
import com.ibm.watsonhealth.fhir.audit.cadf.model.CadfGeolocation;
import com.ibm.watsonhealth.fhir.audit.cadf.model.CadfParser;
import com.ibm.watsonhealth.fhir.audit.cadf.model.CadfResource;
import com.ibm.watsonhealth.fhir.audit.kafka.Environment;
import com.ibm.watsonhealth.fhir.audit.kafka.EventStreamsCredentials;
import com.ibm.watsonhealth.fhir.audit.logging.api.AuditLogEventType;
import com.ibm.watsonhealth.fhir.audit.logging.api.AuditLogService;
import com.ibm.watsonhealth.fhir.audit.logging.beans.AuditLogEntry;
import com.ibm.watsonhealth.fhir.audit.logging.beans.Context;
import com.ibm.watsonhealth.fhir.config.PropertyGroup;
import com.ibm.watsonhealth.fhir.exception.FHIRException;

/**
 * This class is a Cadf/EventStream/COS based implementation of the FHIR server
 * AuditLogService interface
 * 
 * @author Albert Wang
 *
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

    private static final String KAKA_USERNAME = "token";
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

    private boolean isEnabled = false;

    private static Map<String, CadfEvent.Action> fhir2CadfMap = new HashMap<String, CadfEvent.Action>() {
        /**
        * 
        */
        private static final long serialVersionUID = 1L;

        {
            put("C", CadfEvent.Action.create);
            put("D", CadfEvent.Action.delete);
            put("U", CadfEvent.Action.update);
            put("R", CadfEvent.Action.read);
        }
    };

    // Initialize CADF OBSERVER resource object once
    private static CadfResource observerRsrc = new CadfResource.Builder("fhir-server",
            CadfEvent.ResourceType.compute_node)
                    .withGeolocation(new CadfGeolocation.Builder(geoCity, geoState, geoCountry, null).build())
                    .withName("Fhir Audit").withHost(System.getenv("HOSTNAME")).build();

    public WhcAuditCadfLogService() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ibm.watsonhealth.fhir.audit.logging.api.AuditLogService#initialize(com.
     * ibm.watsonhealth.fhir.config.PropertyGroup)
     */
    @Override
    public void initialize(PropertyGroup auditLogProperties) throws Exception {

        final String METHODNAME = "initialize";
        logger.entering(CLASSNAME, METHODNAME);

        // Check environment: VCAP_SERVICES and binding-eventstreams-fhir1 to obtain
        // configuration parameters for
        // kafka (for Cloud Foundry and Kub Container)
        if (System.getenv(Environment.VCAP_SERVICES) != null
                || System.getenv(Environment.KUB_EVENTSTREAMS_BINDING) != null) {
            logger.log(Level.INFO, "Using VCAP_SERVICES or binding-eventstreams-fhir1 to find credentials.");
            EventStreamsCredentials credentials = Environment.getEventStreamsCredentials();
            if (credentials != null) {
                bootstrapServers = Environment.stringArrayToCSV(credentials.getKafkaBrokersSasl());
                apiKey = credentials.getApiKey();
            }
        }

        // If fails to get config from environment, then try to get them from FHIR
        // config
        if (bootstrapServers == null || bootstrapServers.length() < 10 || apiKey == null || apiKey.length() < 10) {
            Objects.requireNonNull(auditLogProperties, "Audit log properties cannot be null.");
            logger.log(Level.INFO, "Using FHIR config to find credentials.");
            bootstrapServers = auditLogProperties.getStringProperty(PROPERTY_AUDIT_KAFKA_BOOTSTRAPSERVERS);
            apiKey = auditLogProperties.getStringProperty(PROPERTY_AUDIT_KAFKA_APIKEY);
        }

        // If still fails to get config for kafka producer, then throw exception;
        if (bootstrapServers == null || bootstrapServers.length() < 10 || apiKey == null || apiKey.length() < 10) {
            throw new FHIRException("Can not get kafka settings!");
        }

        // Now, let's get the audit topic from FHIR config, if not found, then use the
        // default topic
        if (auditLogProperties != null) {
            auditTopic = auditLogProperties.getStringProperty(PROPERTY_AUDIT_KAFKA_TOPIC, DEFAULT_AUDIT_KAFKA_TOPIC);
            geoCity = auditLogProperties.getStringProperty(PROPERTY_AUDIT_GEO_CITY, DEFAULT_AUDIT_GEO_CITY);
            geoState = auditLogProperties.getStringProperty(PROPERTY_AUDIT_GEO_STATE, DEFAULT_AUDIT_GEO_STATE);
            geoCountry = auditLogProperties.getStringProperty(PROPERTY_AUDIT_GEO_COUNTRY, DEFAULT_AUDIT_GEO_COUNTRY);
        }

        Properties kafkaProps = new Properties();
        kafkaProps.put("sasl.jaas.config", String.format(
                "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"%s\" password=\"%s\";",
                KAKA_USERNAME, apiKey));
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
CODE_REMOVED
            this.isEnabled = true;
        }

        logger.exiting(CLASSNAME, METHODNAME);

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ibm.watsonhealth.fhir.audit.logging.api.AuditLogService#logEntry(com.ibm.
     * watsonhealth.fhir.audit.logging.beans.AuditLogEntry)
     */
    @Override
    public void logEntry(AuditLogEntry logEntry) throws Exception {
        final String METHODNAME = "logEntry";
        logger.entering(CLASSNAME, METHODNAME);

        CadfEvent eventObject = createCadfEvent(logEntry);

        if (eventObject != null) {
            CadfParser parser = new CadfParser();
            String eventString = parser.cadf2Json(eventObject);
            ProducerRecord<String, String> record = new ProducerRecord<String, String>(auditTopic, eventString);
            // Block till the message is sent to kafka server.
            this.producer.send(record).get();
        }

        logger.exiting(CLASSNAME, METHODNAME);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ibm.watsonhealth.fhir.audit.logging.api.AuditLogService#isEnabled()
     */
    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }

    /**
     * @param logEntry
     * @return
     * @throws IllegalStateException
     * @throws JsonProcessingException
     */
    public static CadfEvent createCadfEvent(AuditLogEntry logEntry)
            throws IllegalStateException, JsonProcessingException {
        final String METHODNAME = "createCadfEvent";
        logger.entering(CLASSNAME, METHODNAME);

        CadfEvent event = null;

        // Cadf does't log config, so skip
        if ((logEntry.getEventType() != AuditLogEventType.FHIR_CONFIGDATA.value()) && logEntry.getContext() != null
                && logEntry.getContext().getAction() != null && logEntry.getContext().getApiParameters() != null) {
            // Define resources
            CadfResource initiator = new CadfResource.Builder(logEntry.getTenantId() + "@" + logEntry.getComponentId(),
                    CadfEvent.ResourceType.compute_machine)
                            .withGeolocation(new CadfGeolocation.Builder(geoCity, geoState, geoCountry, null).build())
                            .withCredential(new CadfCredential.Builder("user-" + logEntry.getUserName()).build())
                            .withHost(logEntry.getComponentIp()).build();
            CadfResource target = new CadfResource.Builder(
                    logEntry.getContext().getData() == null || logEntry.getContext().getData().getId() == null ? UUID.randomUUID().toString()
                            : logEntry.getContext().getData().getId(),
                    CadfEvent.ResourceType.data_database)
                            .withGeolocation(new CadfGeolocation.Builder("Dallas", "TX", "US", null).build())
                            .withAddress(
                                    new CadfEndpoint(logEntry.getContext().getApiParameters().getRequest(), "", ""))
                            .build();

            CadfFhirContext fhirContext = new CadfFhirContext(logEntry.getContext());
            fhirContext.setClient_cert_cn(logEntry.getClientCertCn());
            fhirContext.setClient_cert_issuer_ou(logEntry.getClientCertIssuerOu());
            fhirContext.setEvent_type(logEntry.getEventType());
            fhirContext.setLocation(logEntry.getLocation());
            fhirContext.setDescription(logEntry.getDescription());

            event = new CadfEvent.CadfEventBuilder(
                    logEntry.getContext().getRequestUniqueId() == null ? UUID.randomUUID().toString()
                            : logEntry.getContext().getRequestUniqueId(),
                    CadfEvent.EventType.activity, logEntry.getTimestamp(),
                    fhir2CadfMap.getOrDefault(logEntry.getContext().getAction(), CadfEvent.Action.unknown),
                    logEntry.getContext().getApiParameters().getStatus() < 400 ? CadfEvent.Outcome.success
                            : CadfEvent.Outcome.failure).withObserver(observerRsrc).withInitiator(initiator)
                                    .withTarget(target).withTag(logEntry.getCorrelationId())
                                    .withAttachment(new CadfAttachment("application/json",
                                            new CadfParser().fhirContext2Json(fhirContext)))
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

class CadfFhirContext extends Context {
    private String event_type;
    private String description;
    private String client_cert_cn;
    private String client_cert_issuer_ou;
    private String location;

    public CadfFhirContext(Context fromObj) {
        super(fromObj);
    }

    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClient_cert_cn() {
        return client_cert_cn;
    }

    public void setClient_cert_cn(String client_cert_cn) {
        this.client_cert_cn = client_cert_cn;
    }

    public String getClient_cert_issuer_ou() {
        return client_cert_issuer_ou;
    }

    public void setClient_cert_issuer_ou(String client_cert_issuer_ou) {
        this.client_cert_issuer_ou = client_cert_issuer_ou;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
