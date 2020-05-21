/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.omop.test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

public class GenerateObservationPeriods {
    public static void main(String[] args) throws Exception {
        Properties properties = getProperties();
        DataSource dataSource = createPoolingDataSource(properties);

        long sequence = 0;

        try (Connection connection = dataSource.getConnection();
                PreparedStatement selectStatement = connection.prepareStatement("SELECT person_id FROM person ORDER BY person_id")) {
            try (ResultSet resultSet = selectStatement.executeQuery()) {
                while (resultSet.next()) {
                    long personId = resultSet.getLong("person_id");
                    try (PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO observation_period VALUES (?, ?, '1900-01-01', '2020-01-01', 0)")) {
                        insertStatement.setLong(1, ++sequence);
                        insertStatement.setLong(2, personId);
                        insertStatement.execute();
                    }
                }
            }
        }
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
