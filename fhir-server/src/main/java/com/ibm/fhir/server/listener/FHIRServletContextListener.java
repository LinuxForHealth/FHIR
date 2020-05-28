/*
 * (C) Copyright IBM Corp. 2016, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.listener;

import static com.ibm.fhir.config.FHIRConfiguration.PROPERTY_CHECK_REFERENCE_TYPES;
import static com.ibm.fhir.config.FHIRConfiguration.PROPERTY_JDBC_BOOTSTRAP_DB;
import static com.ibm.fhir.config.FHIRConfiguration.PROPERTY_KAFKA_CONNECTIONPROPS;
import static com.ibm.fhir.config.FHIRConfiguration.PROPERTY_KAFKA_ENABLED;
import static com.ibm.fhir.config.FHIRConfiguration.PROPERTY_KAFKA_TOPICNAME;
import static com.ibm.fhir.config.FHIRConfiguration.PROPERTY_NATS_CHANNEL;
import static com.ibm.fhir.config.FHIRConfiguration.PROPERTY_NATS_CLIENT;
import static com.ibm.fhir.config.FHIRConfiguration.PROPERTY_NATS_CLUSTER;
import static com.ibm.fhir.config.FHIRConfiguration.PROPERTY_NATS_ENABLED;
import static com.ibm.fhir.config.FHIRConfiguration.PROPERTY_NATS_KEYSTORE;
import static com.ibm.fhir.config.FHIRConfiguration.PROPERTY_NATS_KEYSTORE_PW;
import static com.ibm.fhir.config.FHIRConfiguration.PROPERTY_NATS_SERVERS;
import static com.ibm.fhir.config.FHIRConfiguration.PROPERTY_NATS_TLS_ENABLED;
import static com.ibm.fhir.config.FHIRConfiguration.PROPERTY_NATS_TRUSTSTORE;
import static com.ibm.fhir.config.FHIRConfiguration.PROPERTY_NATS_TRUSTSTORE_PW;
import static com.ibm.fhir.config.FHIRConfiguration.PROPERTY_SERVER_REGISTRY_RESOURCE_PROVIDER_ENABLED;
import static com.ibm.fhir.config.FHIRConfiguration.PROPERTY_WEBSOCKET_ENABLED;

import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import javax.websocket.server.ServerContainer;

import org.owasp.encoder.Encode;

import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.config.PropertyGroup;
import com.ibm.fhir.config.PropertyGroup.PropertyEntry;
import com.ibm.fhir.model.config.FHIRModelConfig;
import com.ibm.fhir.model.util.FHIRUtil;
import com.ibm.fhir.notification.websocket.impl.FHIRNotificationServiceEndpointConfig;
import com.ibm.fhir.notifications.kafka.impl.FHIRNotificationKafkaPublisher;
import com.ibm.fhir.notifications.nats.impl.FHIRNotificationNATSPublisher;
import com.ibm.fhir.operation.registry.FHIROperationRegistry;
import com.ibm.fhir.persistence.helper.FHIRPersistenceHelper;
import com.ibm.fhir.persistence.interceptor.impl.FHIRPersistenceInterceptorMgr;
import com.ibm.fhir.persistence.jdbc.util.DerbyBootstrapper;
import com.ibm.fhir.registry.FHIRRegistry;
import com.ibm.fhir.search.util.SearchUtil;
import com.ibm.fhir.server.registry.ServerRegistryResourceProvider;

@WebListener("IBM FHIR Server Servlet Context Listener")
public class FHIRServletContextListener implements ServletContextListener {
    private static final Logger log = Logger.getLogger(FHIRServletContextListener.class.getName());

    private static final String ATTRNAME_WEBSOCKET_SERVERCONTAINER = "javax.websocket.server.ServerContainer";
    private static final String DEFAULT_KAFKA_TOPICNAME = "fhirNotifications";
    private static final String DEFAULT_NATS_CHANNEL = "fhirNotifications";
    private static final String DEFAULT_NATS_CLUSTER = "nats-streaming";
    private static final String DEFAULT_NATS_CLIENT = "fhir-server";
    public static final String FHIR_SERVER_INIT_COMPLETE = "com.ibm.fhir.webappInitComplete";
    private static FHIRNotificationKafkaPublisher kafkaPublisher = null;
    private static FHIRNotificationNATSPublisher natsPublisher = null;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        if (log.isLoggable(Level.FINER)) {
            log.entering(FHIRServletContextListener.class.getName(), "contextInitialized");
        }
        try {
            // Initialize our "initComplete" flag to false.
            event.getServletContext().setAttribute(FHIR_SERVER_INIT_COMPLETE, Boolean.FALSE);

            PropertyGroup fhirConfig = FHIRConfiguration.getInstance().loadConfiguration();
            if (fhirConfig == null) {
                throw new IllegalStateException("No FHIRConfiguration was found");
            }

            log.fine("Current working directory: " + Encode.forHtml(System.getProperty("user.dir")));

            /*
             * The following inits are intended to load the FHIRUtil and SearchUtil into the classloader.
             * Subsequently, the code activates the static values (and maps).
             */
            log.fine("Initializing FHIRUtil...");
            FHIRUtil.init();

            log.fine("Initializing SearchUtil...");
            SearchUtil.init();

            log.fine("Initializing FHIROperationRegistry...");
            FHIROperationRegistry.getInstance();

            // For any singleton resources that need to be shared among our resource class instances,
            // we'll add them to our servlet context so that the resource class can easily retrieve them.

            // Set the shared FHIRPersistenceHelper.
            FHIRPersistenceHelper persistenceHelper = new FHIRPersistenceHelper();
            event.getServletContext().setAttribute(FHIRPersistenceHelper.class.getName(), persistenceHelper);
            log.fine("Set shared persistence helper on servlet context.");

            // If websocket notifications are enabled, then initialize the endpoint.
            Boolean websocketEnabled = fhirConfig.getBooleanProperty(PROPERTY_WEBSOCKET_ENABLED, Boolean.FALSE);
            if (websocketEnabled) {
                log.info("Initializing WebSocket notification publisher.");
                ServerContainer container = (ServerContainer) event.getServletContext().getAttribute(ATTRNAME_WEBSOCKET_SERVERCONTAINER);
                container.addEndpoint(new FHIRNotificationServiceEndpointConfig());
            } else {
                log.info("Bypassing WebSocket notification init.");
            }

            // If Kafka notifications are enabled, start up our Kafka notification publisher.
            Boolean kafkaEnabled = fhirConfig.getBooleanProperty(PROPERTY_KAFKA_ENABLED, Boolean.FALSE);
            if (kafkaEnabled) {
                // Retrieve the topic name.
                String topicName = fhirConfig.getStringProperty(PROPERTY_KAFKA_TOPICNAME, DEFAULT_KAFKA_TOPICNAME);

                // Gather up the Kafka connection properties.
                Properties kafkaProps = new Properties();
                PropertyGroup pg = fhirConfig.getPropertyGroup(PROPERTY_KAFKA_CONNECTIONPROPS);
                if (pg != null) {
                    List<PropertyEntry> connectionProps = pg.getProperties();
                    if (connectionProps != null) {
                        for (PropertyEntry entry : connectionProps) {
                            kafkaProps.setProperty(entry.getName(), entry.getValue().toString());
                        }
                    }
                }

                log.info("Initializing Kafka notification publisher.");
                kafkaPublisher = new FHIRNotificationKafkaPublisher(topicName, kafkaProps);
            } else {
                log.info("Bypassing Kafka notification init.");
            }

            // If NATS notifications are enabled, start up our NATS notification publisher.
            Boolean natsEnabled = fhirConfig.getBooleanProperty(PROPERTY_NATS_ENABLED, Boolean.FALSE);
            if (natsEnabled) {
                // Retrieve the cluster ID.
                String clusterId = fhirConfig.getStringProperty(PROPERTY_NATS_CLUSTER, DEFAULT_NATS_CLUSTER);
                // Retrieve the channel name.
                String channelName = fhirConfig.getStringProperty(PROPERTY_NATS_CHANNEL, DEFAULT_NATS_CHANNEL);
                // Retrieve the NATS client ID.
                String clientId = fhirConfig.getStringProperty(PROPERTY_NATS_CLIENT, DEFAULT_NATS_CLIENT);
                // Retrieve the server URL.
                String servers = fhirConfig.getStringProperty(PROPERTY_NATS_SERVERS);

                // Gather up the NATS TLS properties.
                Properties tlsProps = new Properties();
                tlsProps.setProperty("useTLS", fhirConfig.getBooleanProperty(PROPERTY_NATS_TLS_ENABLED, Boolean.TRUE).toString());
                tlsProps.setProperty("truststore", fhirConfig.getStringProperty(PROPERTY_NATS_TRUSTSTORE));
                tlsProps.setProperty("truststorePass", fhirConfig.getStringProperty(PROPERTY_NATS_TRUSTSTORE_PW));
                tlsProps.setProperty("keystore", fhirConfig.getStringProperty(PROPERTY_NATS_KEYSTORE));
                tlsProps.setProperty("keystorePass", fhirConfig.getStringProperty(PROPERTY_NATS_KEYSTORE_PW));

                log.info("Initializing NATS notification publisher.");
                natsPublisher = new FHIRNotificationNATSPublisher(clusterId, channelName, clientId, servers, tlsProps);
            } else {
                log.info("Bypassing NATS notification init.");
            }

            Boolean checkReferenceTypes = fhirConfig.getBooleanProperty(PROPERTY_CHECK_REFERENCE_TYPES, Boolean.TRUE);
            FHIRModelConfig.setCheckReferenceTypes(checkReferenceTypes);

            bootstrapDerbyDatabases(fhirConfig);

            log.fine("Initializing FHIRRegistry...");
            FHIRRegistry.getInstance();

            Boolean serverRegistryResourceProviderEnabled = fhirConfig.getBooleanProperty(PROPERTY_SERVER_REGISTRY_RESOURCE_PROVIDER_ENABLED, Boolean.FALSE);
            if (serverRegistryResourceProviderEnabled) {
                log.info("Registering ServerRegistryResourceProvider...");
                ServerRegistryResourceProvider provider = new ServerRegistryResourceProvider(persistenceHelper);
                FHIRRegistry.getInstance().register(provider);
                FHIRPersistenceInterceptorMgr.getInstance().addInterceptor(provider);
            }

            // Finally, set our "initComplete" flag to true.
            event.getServletContext().setAttribute(FHIR_SERVER_INIT_COMPLETE, Boolean.TRUE);
        } catch(Throwable t) {
            String msg = "Encountered an exception while initializing the servlet context.";
            log.log(Level.SEVERE, msg, t);
            throw new RuntimeException(msg, t);
        } finally {
            if (log.isLoggable(Level.FINER)) {
                log.exiting(FHIRServletContextListener.class.getName(), "contextInitialized");
            }
        }
    }

    /**
     * Bootstraps derby databases during server startup if requested.
     */
    private void bootstrapDerbyDatabases(PropertyGroup fhirConfig) throws Exception {
        Boolean performDbBootstrap = fhirConfig.getBooleanProperty(PROPERTY_JDBC_BOOTSTRAP_DB, Boolean.FALSE);
        if (performDbBootstrap) {
            log.info("Performing Derby database bootstrapping...");

            String datasourceJndiName = fhirConfig.getStringProperty(FHIRConfiguration.PROPERTY_JDBC_DATASOURCE_JNDINAME, "jdbc/fhirDB");
            InitialContext ctxt = new InitialContext();
            DataSource ds = (DataSource) ctxt.lookup(datasourceJndiName);

            bootstrapFhirDb("default", "default", ds);
            bootstrapFhirDb("tenant1", "profile", ds);
            bootstrapFhirDb("tenant1", "reference", ds);
            bootstrapFhirDb("tenant1", "study1", ds);

            datasourceJndiName = "jdbc/OAuth2DB";
            try {
                ds = (DataSource) ctxt.lookup(datasourceJndiName);
                if (ds != null) {
                    log.info("Found '" + datasourceJndiName + "'; bootstrapping the OAuth client tables");
                    DerbyBootstrapper.bootstrapOauthDb(ds);
                }
            } catch (NameNotFoundException e) {
                log.info("No '" + datasourceJndiName + "' dataSource found; skipping OAuth client table bootstrapping");
            }

            datasourceJndiName = "jdbc/fhirbatchDB";
            try {
                // Check the batch database, if the batch database configuration is there, and available.
                // Note, in the boostrap code we conditionally bootstrap if and only if it's targeting derby.
                ds = (DataSource) ctxt.lookup(datasourceJndiName);
                if (ds != null) {
                    log.info("Found '" + datasourceJndiName + "'; bootstrapping the Java Batch tables");
                    DerbyBootstrapper.bootstrapBatchDb(ds);
                }
            } catch (NameNotFoundException e) {
                log.info("No '" + datasourceJndiName + "' dataSource found; skipping Java Batch table bootstrapping");
            }

            log.info("Finished Derby database bootstrapping...");
        } else {
            log.info("Derby database bootstrapping is disabled.");
        }
    }

    /**
     * Bootstraps the database specified by tenantId and dsId, assuming the specified datastore definition can be
     * retrieved from the configuration.
     */
    private void bootstrapFhirDb(String tenantId, String dsId, DataSource ds) throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext(tenantId, dsId));
        PropertyGroup pg = FHIRConfigHelper.getPropertyGroup(FHIRConfiguration.PROPERTY_DATASOURCES + "/" + dsId);
        if (pg != null) {
            String type = pg.getStringProperty("type");
            if (type != null && !type.isEmpty() && (type.toLowerCase().equals("derby") || type.toLowerCase().equals("derby_network_server"))) {
                log.info("Bootstrapping database for tenantId/dsId: " + tenantId + "/" + dsId);
                DerbyBootstrapper.bootstrapDb(ds);
                log.info("Finished bootstrapping database for tenantId/dsId: " + tenantId + "/" + dsId);
            }
        }

        FHIRRequestContext.remove();
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        if (log.isLoggable(Level.FINER)) {
            log.entering(FHIRServletContextListener.class.getName(), "contextDestroyed");
        }
        try {
            // Set our "initComplete" flag back to false.
            event.getServletContext().setAttribute(FHIR_SERVER_INIT_COMPLETE, Boolean.FALSE);

            // If we previously initialized the Kafka publisher, then shut it down now.
            if (kafkaPublisher != null) {
                kafkaPublisher.shutdown();
                kafkaPublisher = null;
            }

            // If we previously initialized the NATS publisher, then shut it down now.
            if (natsPublisher != null) {
                natsPublisher.shutdown();
                natsPublisher = null;
            }
        } catch (Exception e) {
        } finally {
            if (log.isLoggable(Level.FINER)) {
                log.exiting(FHIRServletContextListener.class.getName(), "contextDestroyed");
            }
        }
    }
}
