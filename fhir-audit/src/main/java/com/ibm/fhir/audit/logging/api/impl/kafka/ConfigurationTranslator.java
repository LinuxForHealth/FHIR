/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.audit.logging.api.impl.kafka;

import java.util.Objects;
import java.util.Properties;
import java.util.logging.Logger;

import com.ibm.fhir.audit.logging.api.AuditLogServiceConstants;
import com.ibm.fhir.audit.logging.api.impl.kafka.environment.Kafka;
import com.ibm.fhir.audit.logging.api.impl.kafka.environment.IBMEventStreams;
import com.ibm.fhir.audit.logging.api.impl.kafka.environment.IBMEventStreams.EventStreamsCredentials;
import com.ibm.fhir.config.PropertyGroup;
import com.ibm.fhir.config.PropertyGroup.PropertyEntry;
import com.ibm.fhir.exception.FHIRException;

/**
 *
 */
public class ConfigurationTranslator {

    private static final Logger logger = java.util.logging.Logger.getLogger(ConfigurationTranslator.class.getName());
    private static final String CLASSNAME = ConfigurationTranslator.class.getName();

    public static final String PROPERTY_AUDIT_KAFKA_BOOTSTRAPSERVERS = "kafkaServers";
    public static final String PROPERTY_AUDIT_KAFKA_APIKEY = "kafkaApiKey";
    private static final String KAFKA_USERNAME = "token";

    /**
     * decides to load the Properties from the environment or the configuration.
     *
     * @param auditLogProperties
     * @return
     * @throws Exception
     */
    public Properties translate(PropertyGroup auditLogProperties) throws Exception {
        ConfigurationType type = loadFromEnvironment(auditLogProperties);
        Properties props = new Properties();
        switch (type) {
        case CONFIG:
            config(props, auditLogProperties);
            break;
        case ENVIRONMENT:
            environment(props, auditLogProperties);
            break;
        default:
            logger.warning("unable to determine where to load from");
            break;
        }
        return props;
    }

    /**
     * loads the details from the config values.
     *
     * @param props
     * @param auditLogProperties
     * @throws Exception
     */
    public void config(Properties props, PropertyGroup auditLogProperties) throws Exception {
        for (PropertyEntry entry : auditLogProperties.getPropertyGroup("kafka").getProperties()) {
            props.put(entry.getName(), entry.getValue());
        }

        // Every instance needs to be loaded:
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    }

    /**
     * loads the details from the environmental values.
     *
     * @param props
     * @param auditLogProperties
     * @throws Exception
     */
    public void environment(Properties props, PropertyGroup auditLogProperties) throws Exception {
        final String METHODNAME = "environment";
        logger.entering(CLASSNAME, METHODNAME);

        String bootstrapServers = null;
        String apiKey = null;
        // Check environment: EVENT_STREAMS_AUDIT_BINDING to obtain configuration parameters for
        // kafka (Kubernetes Container)
        if (System.getenv(IBMEventStreams.KUB_BINDING) != null) {
            logger.info("Using the environmental variable '" + IBMEventStreams.KUB_BINDING + "' to set the credentials.");
            EventStreamsCredentials credentials = IBMEventStreams.getEventStreamsCredentials();
            if (credentials != null) {
                bootstrapServers = IBMEventStreams.stringArrayToCSV(credentials.getKafkaBrokersSasl());
                apiKey = credentials.getApiKey();
            }
        } else if (System.getenv(Kafka.KUB_BINDING) != null) {

        }

        // If fails to get config from environment, then try to get them from FHIR config
        if (bootstrapServers == null || bootstrapServers.length() < 10 || apiKey == null || apiKey.length() < 10) {
            Objects.requireNonNull(auditLogProperties, "Audit log properties cannot be null.");
            logger.info("Using FHIR config to find credentials.");
            bootstrapServers = auditLogProperties.getStringProperty(PROPERTY_AUDIT_KAFKA_BOOTSTRAPSERVERS);
            apiKey = auditLogProperties.getStringProperty(PROPERTY_AUDIT_KAFKA_APIKEY);
        }

        // If still fails to get config for kafka producer, then throw <pre>exception</pre>
        if (bootstrapServers == null || bootstrapServers.length() < 10 || apiKey == null || apiKey.length() < 10) {
            throw new FHIRException("Can not get kafka settings!");
        }

        // We load the configuration.
        config(props, auditLogProperties);
        Object o =
                props.put("sasl.jaas.config", String.format("org.apache.kafka.common.security.plain.PlainLoginModule required username=\"%s\" password=\"%s\";", KAFKA_USERNAME, apiKey));
        if (o != null) {
            logger.warning("Environmental properties are overriding fhir-server-config.json for - sasl.jaas.config");
        }
        o = props.put("bootstrap.servers", bootstrapServers);
        if (o != null) {
            logger.warning("Environmental properties are overriding fhir-server-config.json for - bootstrap.servers");
        }
    }

    /*
     * gets the location to load the properties from:
     * 1 - Environment Bindings with EventStreams Credentials - "env"
     * 2 - fhir-server-config.json - "config"
     * @param auditLogProperties
     * @return
     */
    public ConfigurationType loadFromEnvironment(PropertyGroup auditLogProperties) {
        Objects.requireNonNull(auditLogProperties, "Expected Non Null audit log properties");
        ConfigurationType type = ConfigurationType.CONFIG;
        try {
            type = ConfigurationType.valueOf(auditLogProperties.getStringProperty(AuditLogServiceConstants.FIELD_LOAD, ConfigurationType.CONFIG.value()));
        } catch (Exception e) {
            logger.warning("Using FHIR config to find location to load environments.");
        }
        return type;
    }

    /**
     * gets the topic used in the logging of the audit messages.
     *
     * @param auditLogProperties
     * @return
     */
    public String getTopic(PropertyGroup auditLogProperties) {
        Objects.requireNonNull(auditLogProperties, "Expected Non Null audit log properties");
        try {
            return auditLogProperties.getStringProperty(AuditLogServiceConstants.FIELD_TOPIC_NAME, AuditLogServiceConstants.TOPIC);
        } catch (Exception e) {
            logger.warning("Using FHIR config to find credentials.");
        }
        return AuditLogServiceConstants.TOPIC;
    }
}