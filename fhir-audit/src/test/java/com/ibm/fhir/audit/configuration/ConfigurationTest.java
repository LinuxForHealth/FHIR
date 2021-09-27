/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.audit.configuration;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.audit.mapper.MapperType;
import com.ibm.fhir.config.PropertyGroup;

import jakarta.json.Json;
import jakarta.json.JsonBuilderFactory;
import jakarta.json.JsonObject;

/**
 * Configuration Tests
 */
public class ConfigurationTest {

    private static final JsonBuilderFactory BUILDER_FACTORY = Json.createBuilderFactory(null);

    private JsonObject jsonObjKafka = null;
    private JsonObject jsonObjKafkaDefault = null;
    private JsonObject jsonObjKafkaMapper = null;
    private JsonObject jsonObjKafkaBadMapper = null;

    @BeforeClass
    public void setup() {
        //@formatter:off
        jsonObjKafka = BUILDER_FACTORY.createObjectBuilder()
                            .add("auditTopic","auditTopicValue2")
                            .add("mapper","auditevent")
                            .add("kafka",
                                BUILDER_FACTORY.createObjectBuilder()
                                    .build())
                        .build();
        //@formatter:on

        //@formatter:off
        jsonObjKafkaMapper = BUILDER_FACTORY.createObjectBuilder()
                            .add("auditTopic","auditTopicValue")
                            .add("mapper","cadf")
                            .add("kafka",
                                BUILDER_FACTORY.createObjectBuilder()
                                    .build())
                        .build();
        //@formatter:on

        //@formatter:off
        jsonObjKafkaBadMapper = BUILDER_FACTORY.createObjectBuilder()
                            .add("auditTopic","auditTopicValue")
                            .add("mapper","bad")
                            .add("kafka",
                                BUILDER_FACTORY.createObjectBuilder()
                                    .build())
                        .build();
        //@formatter:on

        //@formatter:off
        jsonObjKafkaDefault = BUILDER_FACTORY.createObjectBuilder()
                            .add("level2b","level2bprop")
                            .add("kafka",
                                BUILDER_FACTORY.createObjectBuilder()
                                    .build())
                        .build();
        //@formatter:on
    }

    final ConfigurationTranslator translator = new ConfigurationTranslator();

    @Test
    public void testConfigurationTestConfig() {
        ConfigurationType type = ConfigurationType.from("config");
        assertNotNull(type);
        assertEquals(type.name(), "CONFIG");
    }

    @Test
    public void testConfigurationTestEnvironment() {
        ConfigurationType type = ConfigurationType.from("environment");
        assertNotNull(type);
        assertEquals(type.name(), "ENVIRONMENT");
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testConfigurationTestNoMatch() {
        ConfigurationType.from("notexists");
    }

    @Test
    public void testRemapping() {
        assertEquals(translator.remap("test"), "test");
        assertEquals(translator.remap("com.ibm.fhir.audit.logging.impl.DisabledAuditLogService"), "com.ibm.fhir.audit.impl.NopService");
        assertEquals(translator.remap("com.ibm.fhir.audit.logging.impl.WhcAuditCadfLogService"), "com.ibm.fhir.audit.impl.KafkaService");
    }

    @Test
    public void testRemappingNull() {
        assertEquals("null", translator.remap("null"));
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testGetTopicNull() throws Exception {
        translator.getTopic(null);
    }

    @Test
    public void testGetTopicWithTopicDefault() {
        String topic = translator.getTopic(new PropertyGroup(jsonObjKafkaDefault));
        assertEquals(topic, "FHIR_AUDIT");
    }

    @Test
    public void testGetTopicWithTopic() {
        String topic = translator.getTopic(new PropertyGroup(jsonObjKafkaMapper));
        assertEquals(topic, "auditTopicValue");
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testGetMapperTypeNull() throws Exception {
        translator.getMapperType(null);
    }

    @Test
    public void testGetMapperTypeAuditEvent() throws Exception {
        MapperType mapper = translator.getMapperType(new PropertyGroup(jsonObjKafka));
        assertEquals(mapper.name().toLowerCase(), "auditevent");
    }

    @Test
    public void testGetMapperTypeCadf() throws Exception {
        MapperType mapper = translator.getMapperType(new PropertyGroup(jsonObjKafkaMapper));
        assertEquals(mapper.name().toLowerCase(), "cadf");
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testGetMapperTypeCadfBad() throws Exception {
        translator.getMapperType(new PropertyGroup(jsonObjKafkaBadMapper));
    }

    @Test
    public void testGetMapperTypeDefault() throws Exception {
        MapperType mapper = translator.getMapperType(new PropertyGroup(jsonObjKafkaDefault));
        assertEquals(mapper.name().toLowerCase(), "cadf");
    }
}