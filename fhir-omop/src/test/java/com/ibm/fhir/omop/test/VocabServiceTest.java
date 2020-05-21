/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.omop.test;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.postgresql.ds.PGSimpleDataSource;

import com.ibm.fhir.omop.model.Concept;
import com.ibm.fhir.omop.vocab.VocabService;

public class VocabServiceTest {
    public static final int LIMIT = 100000;

    public static void main(String[] args) throws Exception {
        try (InputStream in = VocabServiceTest.class.getClassLoader().getResourceAsStream("omop.jdbc.properties")) {
            Properties properties = new Properties();
            properties.load(in);

//          DataSource dataSource = createSimpleDataSource(properties);
            DataSource dataSource = createPoolingDataSource(properties);
            VocabService vocabService = new VocabService(dataSource);

            int offset = 0;
            int count = 0;

            List<Concept> concepts = vocabService.getConcepts("SNOMED", LIMIT, offset);
            while (!concepts.isEmpty()) {
                count += concepts.size();
                concepts.forEach(System.out::println);
                offset += LIMIT;
                concepts = vocabService.getConcepts("SNOMED", LIMIT, offset);
            }

            System.out.println("count: " + count);
        }
    }

    public static DataSource createSimpleDataSource(Properties properties) throws Exception {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setServerNames(new String[] { properties.getProperty("serverName") });
        dataSource.setPortNumbers(new int[] { Integer.parseInt(properties.getProperty("portNumber")) });
        dataSource.setDatabaseName(properties.getProperty("databaseName"));
        return dataSource;
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