/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.audit.logging.api.configuration;

import static com.ibm.fhir.audit.logging.api.AuditLogServiceConstants.DEFAULT_AUDIT_KAFKA_TOPIC;
import static com.ibm.fhir.audit.logging.api.AuditLogServiceConstants.DEFAULT_MAPPER;
import static com.ibm.fhir.audit.logging.api.AuditLogServiceConstants.FIELD_LOAD;
import static com.ibm.fhir.audit.logging.api.AuditLogServiceConstants.KAFKA_USERNAME;
import static com.ibm.fhir.audit.logging.api.AuditLogServiceConstants.PROPERTY_AUDIT_KAFKA_APIKEY;
import static com.ibm.fhir.audit.logging.api.AuditLogServiceConstants.PROPERTY_AUDIT_KAFKA_BOOTSTRAPSERVERS;
import static com.ibm.fhir.audit.logging.api.AuditLogServiceConstants.PROPERTY_AUDIT_KAFKA_TOPIC;
import static com.ibm.fhir.audit.logging.api.AuditLogServiceConstants.PROPERTY_AUDIT_MAPPER;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Logger;

import com.ibm.fhir.audit.logging.api.configuration.type.IBMEventStreamsType;
import com.ibm.fhir.audit.logging.api.configuration.type.KafkaType;
import com.ibm.fhir.audit.logging.mapper.MapperType;
import com.ibm.fhir.config.PropertyGroup;
import com.ibm.fhir.exception.FHIRException;

/**
 * Handles translation of configurations to useful configurations.
 */
public class ConfigurationTranslator {
    private static final String CLASSNAME = ConfigurationTranslator.class.getName();
    private static final Logger logger = java.util.logging.Logger.getLogger(CLASSNAME);


    private static final Map<String, String> PRIOR_VERSION_CLASSNAME = generateMap();

    public ConfigurationTranslator() {
        // No Operation
    }

    /*
     * generates the map from prior versions to the current naming scheme
     * and maintains backwards compatibility.
     */
    private static Map<String, String> generateMap() {
        Map<String, String> mapped = new HashMap<>(2);
        mapped.put("com.ibm.fhir.audit.logging.impl.DisabledAuditLogService", "com.ibm.fhir.audit.logging.api.impl.NopService");
        mapped.put("com.ibm.fhir.audit.logging.impl.WhcAuditCadfLogService", "com.ibm.fhir.audit.logging.api.impl.KafkaService");
        return mapped;
    }

    /*
     * used to remap from prior versions
     */
    public String remap(String inputName) {
        if (Objects.isNull(inputName)) {
            logger.warning("remapping a bad Audit Log Service, please check configuration");
        }
        if (PRIOR_VERSION_CLASSNAME.containsKey(inputName)) {
            return PRIOR_VERSION_CLASSNAME.get(inputName);
        }
        return inputName;
    }

    /**
     * decides to load the Properties from the environment or the configuration.
     *
     * @param auditLogProperties
     * @return
     * @throws Exception
     */
    public Properties translate(PropertyGroup auditLogProperties) throws Exception {
        ConfigurationType type = determineConfigurationType(auditLogProperties);
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
        props.putAll(KafkaType.getEnvironment(auditLogProperties));
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
        if (System.getenv(IBMEventStreamsType.KUB_BINDING) != null) {
            logger.info("Using the environmental variable '" + IBMEventStreamsType.KUB_BINDING + "' to set the credentials.");
            com.ibm.fhir.audit.logging.api.configuration.type.IBMEventStreamsType.EventStreamsCredentials credentials = IBMEventStreamsType.getEventStreamsCredentials();
            if (credentials != null) {
                bootstrapServers = IBMEventStreamsType.stringArrayToCSV(credentials.getKafkaBrokersSasl());
                apiKey = credentials.getApiKey();
            }
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
        checkAndLoadDefaults(props, auditLogProperties);

        // We override if we need to
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
     * check and load the default serialiers (for example)
     */
    private void checkAndLoadDefaults(Properties props, PropertyGroup auditLogProperties) {
        if(!props.containsKey("key.serializer")) {
            props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        }
        if(!props.containsKey("value.serializer")) {
            props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        }
        if(!props.containsKey("sasl.mechanism")) {
            props.put("sasl.mechanism", "PLAIN");
        }
        if(!props.containsKey("security.protocol")) {
            props.put("security.protocol", "SASL_SSL");
        }
        if(!props.containsKey("ssl.protocol")) {
            props.put("ssl.protocol", "TLSv1.2");
        }
        if(!props.containsKey("ssl.enabled.protocols")) {
            props.put("ssl.enabled.protocols", "TLSv1.2");
        }
        if(!props.containsKey("ssl.endpoint.identification.algorithm")) {
            props.put("ssl.endpoint.identification.algorithm", "HTTPS");
        }
    }

    /**
     * gets the location to load the properties from:
     * 1 - Environment Bindings with EventStreams Credentials format - "env"
     * 2 - fhir-server-config.json - "config"
     * @param auditLogProperties
     * @return
     */
    public ConfigurationType determineConfigurationType(PropertyGroup auditLogProperties) {
        checkAuditLogProperties(auditLogProperties);
        // Defaults to Environment.
        ConfigurationType type = ConfigurationType.ENVIRONMENT;
        try {
            type = ConfigurationType.valueOf(auditLogProperties.getStringProperty(FIELD_LOAD, ConfigurationType.ENVIRONMENT.value()));
        } catch (Exception e) {
            logger.warning("Using FHIR 'environment' to find location to load configuration.");
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
        checkAuditLogProperties(auditLogProperties);
        try {
            return auditLogProperties.getStringProperty(PROPERTY_AUDIT_KAFKA_TOPIC, DEFAULT_AUDIT_KAFKA_TOPIC);
        } catch (Exception e) {
            logger.warning("Defaulting to the default topic name.");
        }
        return DEFAULT_AUDIT_KAFKA_TOPIC;
    }


    /**
     * get the mapper from the auditLogProperties
     *
     * @param auditLogProperties
     * @return
     * @throws Exception
     */
    public MapperType getMapperType(PropertyGroup auditLogProperties) throws Exception {
        checkAuditLogProperties(auditLogProperties);
        String mapperType = auditLogProperties.getStringProperty(PROPERTY_AUDIT_MAPPER, DEFAULT_MAPPER);
        return MapperType.from(mapperType);
    }

    /*
     * check audit log properties
     */
    private void checkAuditLogProperties(PropertyGroup auditLogProperties) {
        if (Objects.isNull(auditLogProperties)) {
            throw new IllegalArgumentException("Expected Non Null audit log properties");
        }
    }
}