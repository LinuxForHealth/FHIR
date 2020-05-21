/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.omop.test;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.parser.exception.FHIRParserException;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.omop.generator.Generator;
import com.ibm.fhir.omop.generator.factory.GeneratorFactory;
import com.ibm.fhir.omop.mapping.Mapping;
import com.ibm.fhir.omop.table.Table;
import com.ibm.fhir.omop.version.Version;
import com.ibm.fhir.omop.vocab.VocabService;

public class GeneratorTest {
    public static void main(String[] args) throws Exception {
        Properties properties = getProperties();
        DataSource dataSource = createPoolingDataSource(properties);
        VocabService vocabService = new VocabService(dataSource);

        Map<Class<? extends Resource>, List<Resource>> resourceMap = new HashMap<>();

        File dir = new File("src/test/resources/fhir/");
        for (File file : dir.listFiles()) {
            if (!file.isDirectory()) {
                System.out.println(file.getName());
                try (FileReader reader = new FileReader(file)) {
                    try {
                        Bundle bundle = FHIRParser.parser(Format.JSON).parse(reader);
                        for (Entry entry : bundle.getEntry()) {
                            Resource resource = entry.getResource();
                            resourceMap.computeIfAbsent(resource.getClass(), key -> new ArrayList<>()).add(resource);
                        }
                    } catch (FHIRParserException e) {
                        System.err.println("Unable to load: " + file.getName() + " due to exception: " + e.getMessage());
                    }
                }
            }
        }

        Generator generator = GeneratorFactory.generator(Version.OMOP_CDM_V6_0, vocabService);

        Map<Table, List<String>> tableDataMap = new HashMap<>();
        generator.generate(Mapping.CONDITION_TO_CONDITION_OCCURRENCE, resourceMap, tableDataMap);
        generator.generate(Mapping.ENCOUNTER_TO_VISIT_OCCURRENCE, resourceMap, tableDataMap);
        generator.generate(Mapping.ORGANIZATION_TO_CARE_SITE_AND_LOCATION, resourceMap, tableDataMap);
        generator.generate(Mapping.PATIENT_TO_PERSON_AND_LOCATION, resourceMap, tableDataMap);
        generator.generate(Mapping.PRACTITIONER_TO_PROVIDER, resourceMap, tableDataMap);

        for (Table table : tableDataMap.keySet()) {
            List<String> tableData = tableDataMap.get(table);
            List<String> data = new ArrayList<>(tableData.size() + 1);
            data.add(table.columnNames(Version.OMOP_CDM_V6_0).stream().collect(Collectors.joining(",")));
            data.addAll(tableData);
            Path path = Paths.get(table.tableName() + ".csv");
            Files.write(path, data);
        }

        generator.getReferenceMap().entrySet().stream()
            .map(entry -> entry.getKey() + " -> " + entry.getValue())
            .forEach(System.out::println);

        System.out.println("Processed reference count:   " + generator.getProcessedReferences().size());
        System.out.println("Unprocessed reference count: " + generator.getUnprocessedReferences().size());

        generator.getUnprocessedReferences().forEach(System.out::println);
    }

    public static Properties getProperties() throws Exception {
        try (InputStream in = GeneratorTest.class.getClassLoader().getResourceAsStream("omop.jdbc.properties")) {
            Properties properties = new Properties();
            properties.load(in);
            return properties;
        }
    }

    public static DataSource createPoolingDataSource(Properties properties) {
        String connectURI = String.format("jdbc:postgresql://%s:%s/%s", properties.getProperty("serverName"), properties.getProperty("portNumber"), properties.getProperty("databaseName"));
        ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(connectURI, null);
        PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, null);
        ObjectPool<PoolableConnection> connectionPool = new GenericObjectPool<>(poolableConnectionFactory);
        poolableConnectionFactory.setPool(connectionPool);
        PoolingDataSource<PoolableConnection> dataSource = new PoolingDataSource<>(connectionPool);
        return dataSource;
    }
}
