/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.audit.configuration;

import static org.linuxforhealth.fhir.audit.AuditLogServiceConstants.DEFAULT_AUDIT_KAFKA_TOPIC;
import static org.linuxforhealth.fhir.audit.AuditLogServiceConstants.DEFAULT_MAPPER;
import static org.linuxforhealth.fhir.audit.AuditLogServiceConstants.FIELD_LOAD;
import static org.linuxforhealth.fhir.audit.AuditLogServiceConstants.KAFKA_USERNAME;
import static org.linuxforhealth.fhir.audit.AuditLogServiceConstants.PROPERTY_AUDIT_KAFKA_APIKEY;
import static org.linuxforhealth.fhir.audit.AuditLogServiceConstants.PROPERTY_AUDIT_KAFKA_BOOTSTRAPSERVERS;
import static org.linuxforhealth.fhir.audit.AuditLogServiceConstants.PROPERTY_AUDIT_KAFKA_TOPIC;
import static org.linuxforhealth.fhir.audit.AuditLogServiceConstants.PROPERTY_AUDIT_MAPPER;
import static org.linuxforhealth.fhir.audit.configuration.type.KafkaType.KAFKA_BOOTSTRAP_SERVERS;
import static org.linuxforhealth.fhir.audit.configuration.type.KafkaType.KAFKA_DEFAULT_SASL_MECHANISM;
import static org.linuxforhealth.fhir.audit.configuration.type.KafkaType.KAFKA_DEFAULT_SECURITY_PROTOCOL;
import static org.linuxforhealth.fhir.audit.configuration.type.KafkaType.KAFKA_DEFAULT_SERIALIZER;
import static org.linuxforhealth.fhir.audit.configuration.type.KafkaType.KAFKA_DEFAULT_SSL_ENDPOINT_IDENTIFICATION_ALGORITHM;
import static org.linuxforhealth.fhir.audit.configuration.type.KafkaType.KAFKA_DEFAULT_SSL_PROTOCOL;
import static org.linuxforhealth.fhir.audit.configuration.type.KafkaType.KAFKA_KEY_SERIALIZER;
import static org.linuxforhealth.fhir.audit.configuration.type.KafkaType.KAFKA_SASL_JAAS_CONFIG;
import static org.linuxforhealth.fhir.audit.configuration.type.KafkaType.KAFKA_SASL_MECHANISM;
import static org.linuxforhealth.fhir.audit.configuration.type.KafkaType.KAFKA_SECURITY_PROTOCOL;
import static org.linuxforhealth.fhir.audit.configuration.type.KafkaType.KAFKA_SSL_ENABLED_PROTOCOLS;
import static org.linuxforhealth.fhir.audit.configuration.type.KafkaType.KAFKA_SSL_ENDPOINT_IDENTIFICATION_ALGORITHM;
import static org.linuxforhealth.fhir.audit.configuration.type.KafkaType.KAFKA_SSL_PROTOCOLS;
import static org.linuxforhealth.fhir.audit.configuration.type.KafkaType.KAFKA_VALUE_SERIALIZER;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.audit.configuration.type.IBMEventStreamsType;
import org.linuxforhealth.fhir.audit.configuration.type.KafkaType;
import org.linuxforhealth.fhir.audit.mapper.MapperType;
import org.linuxforhealth.fhir.config.PropertyGroup;
import org.linuxforhealth.fhir.exception.FHIRException;

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
        mapped.put("org.linuxforhealth.fhir.audit.logging.impl.DisabledAuditLogService", "org.linuxforhealth.fhir.audit.impl.NopService");
        mapped.put("org.linuxforhealth.fhir.audit.logging.impl.WhcAuditCadfLogService", "org.linuxforhealth.fhir.audit.impl.KafkaService");
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
            org.linuxforhealth.fhir.audit.configuration.type.IBMEventStreamsType.EventStreamsCredentials credentials = IBMEventStreamsType.getEventStreamsCredentials();
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
                props.put(KAFKA_SASL_JAAS_CONFIG, String.format("org.apache.kafka.common.security.plain.PlainLoginModule required username=\"%s\" password=\"%s\";", KAFKA_USERNAME, apiKey));
        if (o != null) {
            logger.warning("Environmental properties are overriding fhir-server-config.json for - sasl.jaas.config");
        }
        o = props.put(KAFKA_BOOTSTRAP_SERVERS, bootstrapServers);
        if (o != null) {
            logger.warning("Environmental properties are overriding fhir-server-config.json for - bootstrap.servers");
        }
    }

    /*
     * check and load the default serialiers (for example)
     */
    private void checkAndLoadDefaults(Properties props, PropertyGroup auditLogProperties) {
        if(!props.containsKey(KAFKA_KEY_SERIALIZER)) {
            props.put(KAFKA_KEY_SERIALIZER, KAFKA_DEFAULT_SERIALIZER);
        }
        if(!props.containsKey(KAFKA_VALUE_SERIALIZER)) {
            props.put(KAFKA_VALUE_SERIALIZER, KAFKA_DEFAULT_SERIALIZER);
        }
        if(!props.containsKey(KAFKA_SASL_MECHANISM)) {
            props.put(KAFKA_SASL_MECHANISM, KAFKA_DEFAULT_SASL_MECHANISM);
        }
        if(!props.containsKey(KAFKA_SECURITY_PROTOCOL)) {
            props.put(KAFKA_SECURITY_PROTOCOL, KAFKA_DEFAULT_SECURITY_PROTOCOL);
        }
        if(!props.containsKey(KAFKA_SSL_PROTOCOLS)) {
            props.put(KAFKA_SSL_PROTOCOLS, KAFKA_DEFAULT_SSL_PROTOCOL);
        }
        if(!props.containsKey(KAFKA_SSL_ENABLED_PROTOCOLS)) {
            props.put(KAFKA_SSL_ENABLED_PROTOCOLS, KAFKA_DEFAULT_SSL_PROTOCOL);
        }
        if(!props.containsKey(KAFKA_SSL_ENDPOINT_IDENTIFICATION_ALGORITHM)) {
            props.put(KAFKA_SSL_ENDPOINT_IDENTIFICATION_ALGORITHM, KAFKA_DEFAULT_SSL_ENDPOINT_IDENTIFICATION_ALGORITHM);
        }
    }

    /**
     * gets the location to load the properties from:
     * <ul>
     *  <li>Environment Bindings with EventStreams Credentials format - "environment"</li>
     *  <li>fhir-server-config.json - "config"</li>
     * </ul>
     * @param auditLogProperties
     * @return
     */
    public ConfigurationType determineConfigurationType(PropertyGroup auditLogProperties) {
        checkAuditLogProperties(auditLogProperties);
        // Defaults to Environment.
        ConfigurationType type = ConfigurationType.ENVIRONMENT;
        try {
            type = ConfigurationType.from(auditLogProperties.getStringProperty(FIELD_LOAD, ConfigurationType.ENVIRONMENT.value()));
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