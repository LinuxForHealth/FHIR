/*
 * (C) Copyright IBM Corp. 2016, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.listener;

import static com.ibm.fhir.config.FHIRConfiguration.PROPERTY_CHECK_REFERENCE_TYPES;
import static com.ibm.fhir.config.FHIRConfiguration.PROPERTY_EXTENDED_CODEABLE_CONCEPT_VALIDATION;
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
import static com.ibm.fhir.config.FHIRConfiguration.PROPERTY_SERVER_RESOLVE_FUNCTION_ENABLED;
import static com.ibm.fhir.config.FHIRConfiguration.PROPERTY_WEBSOCKET_ENABLED;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.websocket.server.ServerContainer;

import org.apache.commons.configuration2.MapConfiguration;
import org.owasp.encoder.Encode;

import com.ibm.fhir.cache.CachingProxy;
import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.PropertyGroup;
import com.ibm.fhir.config.PropertyGroup.PropertyEntry;
import com.ibm.fhir.core.lifecycle.EventManagerImpl;
import com.ibm.fhir.model.config.FHIRModelConfig;
import com.ibm.fhir.model.lang.util.LanguageRegistryUtil;
import com.ibm.fhir.model.util.FHIRUtil;
import com.ibm.fhir.notification.websocket.impl.FHIRNotificationServiceEndpointConfig;
import com.ibm.fhir.notifications.kafka.impl.FHIRNotificationKafkaPublisher;
import com.ibm.fhir.notifications.nats.impl.FHIRNotificationNATSPublisher;
import com.ibm.fhir.path.function.registry.FHIRPathFunctionRegistry;
import com.ibm.fhir.persistence.helper.FHIRPersistenceHelper;
import com.ibm.fhir.registry.FHIRRegistry;
import com.ibm.fhir.search.util.SearchUtil;
import com.ibm.fhir.server.operation.FHIROperationRegistry;
import com.ibm.fhir.server.registry.ServerRegistryResourceProvider;
import com.ibm.fhir.server.resolve.ServerResolveFunction;
import com.ibm.fhir.server.util.FHIROperationUtil;
import com.ibm.fhir.term.config.FHIRTermConfig;
import com.ibm.fhir.term.graph.provider.GraphTermServiceProvider;
import com.ibm.fhir.term.remote.provider.RemoteTermServiceProvider;
import com.ibm.fhir.term.remote.provider.RemoteTermServiceProvider.Configuration;
import com.ibm.fhir.term.remote.provider.RemoteTermServiceProvider.Configuration.BasicAuth;
import com.ibm.fhir.term.remote.provider.RemoteTermServiceProvider.Configuration.Header;
import com.ibm.fhir.term.remote.provider.RemoteTermServiceProvider.Configuration.Supports;
import com.ibm.fhir.term.remote.provider.RemoteTermServiceProvider.Configuration.TrustStore;
import com.ibm.fhir.term.service.FHIRTermService;
import com.ibm.fhir.term.spi.FHIRTermServiceProvider;

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

    private List<GraphTermServiceProvider> graphTermServiceProviders = new ArrayList<>();
    private List<RemoteTermServiceProvider> remoteTermServiceProviders = new ArrayList<>();
    
    // Unique value known only to this class so that only we can initiate lifecycle events
    private static final Object serviceManagerId = new Object();

    @Override
    public void contextInitialized(ServletContextEvent event) {
        if (log.isLoggable(Level.FINER)) {
            log.entering(FHIRServletContextListener.class.getName(), "contextInitialized");
        }
        try {
            // Initialize our "initComplete" flag to false.
            event.getServletContext().setAttribute(FHIR_SERVER_INIT_COMPLETE, Boolean.FALSE);
            
            EventManagerImpl.registerServiceManagerId(serviceManagerId);

            FHIRConfiguration.setConfigHome(System.getenv("FHIR_CONFIG_HOME"));
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

            log.fine("Initializing FHIROperationUtil...");
            FHIROperationUtil.init();

            log.fine("Initializing LanguageRegistryUtil...");
            LanguageRegistryUtil.init();

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

            Boolean extendedCodeableConceptValidation = fhirConfig.getBooleanProperty(PROPERTY_EXTENDED_CODEABLE_CONCEPT_VALIDATION, Boolean.TRUE);
            FHIRModelConfig.setExtendedCodeableConceptValidation(extendedCodeableConceptValidation);

            log.fine("Initializing FHIRRegistry...");
            FHIRRegistry.getInstance();

            Boolean serverRegistryResourceProviderEnabled = fhirConfig.getBooleanProperty(PROPERTY_SERVER_REGISTRY_RESOURCE_PROVIDER_ENABLED, Boolean.FALSE);
            if (serverRegistryResourceProviderEnabled) {
                log.info("Registering ServerRegistryResourceProvider...");
                FHIRRegistry.getInstance().addProvider(new ServerRegistryResourceProvider(persistenceHelper));
            }

            Boolean serverResolveFunctionEnabled = fhirConfig.getBooleanProperty(PROPERTY_SERVER_RESOLVE_FUNCTION_ENABLED, Boolean.FALSE);
            if (serverResolveFunctionEnabled) {
                log.info("Registering ServerResolveFunction...");
                FHIRPathFunctionRegistry.getInstance().register(new ServerResolveFunction(persistenceHelper));
            }

            configureTermServiceCapabilities(fhirConfig);

            // Set our "initComplete" flag to true.
            event.getServletContext().setAttribute(FHIR_SERVER_INIT_COMPLETE, Boolean.TRUE);

            // Now init is complete, tell all registered callbacks
            EventManagerImpl.serverReady(serviceManagerId);
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

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        if (log.isLoggable(Level.FINER)) {
            log.entering(FHIRServletContextListener.class.getName(), "contextDestroyed");
        }
        try {
            // Set our "initComplete" flag back to false.
            event.getServletContext().setAttribute(FHIR_SERVER_INIT_COMPLETE, Boolean.FALSE);
            
            // Tell anyone who's interested that the server is being shut down. Should not block
            EventManagerImpl.startShutdown(serviceManagerId);

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

            for (GraphTermServiceProvider graphTermServiceProvider : graphTermServiceProviders) {
                graphTermServiceProvider.getGraph().close();
            }

            for (RemoteTermServiceProvider remoteTermServiceProvider : remoteTermServiceProviders) {
                remoteTermServiceProvider.close();
            }

            // Tell registered callbacks that their component shutdowns should be finished
            EventManagerImpl.finalShutdown(serviceManagerId);
        } catch (Exception e) {
            // Ignore it
        } finally {
            if (log.isLoggable(Level.FINER)) {
                log.exiting(FHIRServletContextListener.class.getName(), "contextDestroyed");
            }
        }
    }

    private void configureTermServiceCapabilities(PropertyGroup fhirConfig) throws Exception {
        // Configure terminology service capabilities
        PropertyGroup termPropertyGroup = fhirConfig.getPropertyGroup("fhirServer/term");
        if (termPropertyGroup != null) {
            Boolean cachingDisabled = fhirConfig.getBooleanProperty("cachingDisabled", Boolean.FALSE);
            FHIRTermConfig.setCachingDisabled(cachingDisabled);

            // Configure graph term service providers
            Object[] graphTermServiceProvidersArray = termPropertyGroup.getArrayProperty("graphTermServiceProviders");
            if (graphTermServiceProvidersArray != null) {
                for (Object graphTermServiceProviderObject : graphTermServiceProvidersArray) {
                    PropertyGroup graphTermServiceProviderPropertyGroup = (PropertyGroup) graphTermServiceProviderObject;
                    Boolean enabled = graphTermServiceProviderPropertyGroup.getBooleanProperty("enabled", Boolean.FALSE);
                    if (!enabled) {
                        continue;
                    }
                    try {
                        log.info("Adding GraphTermServiceProvider...");
                        PropertyGroup configurationPropertyGroup = graphTermServiceProviderPropertyGroup.getPropertyGroup("configuration");
                        if (configurationPropertyGroup == null) {
                            log.log(Level.WARNING, "GraphTermServiceProvider configuration not found");
                        } else {
                            Map<String, Object> map = new HashMap<>();
                            configurationPropertyGroup.getProperties().stream().forEach(entry -> map.put(entry.getName(), entry.getValue()));
                            int timeLimit = graphTermServiceProviderPropertyGroup.getIntProperty("timeLimit", GraphTermServiceProvider.DEFAULT_TIME_LIMIT);
                            GraphTermServiceProvider graphTermServiceProvider = new GraphTermServiceProvider(new MapConfiguration(map), timeLimit);
                            FHIRTermService.getInstance().addProvider(cachingDisabled ? graphTermServiceProvider : CachingProxy.newInstance(FHIRTermServiceProvider.class, graphTermServiceProvider));
                            graphTermServiceProviders.add(graphTermServiceProvider);
                        }
                    } catch (Exception e) {
                        log.log(Level.WARNING, "Unable to create GraphTermServiceProvider from configuration property group: " + graphTermServiceProviderPropertyGroup, e);
                    }
                }
            }

            // Configure remote term service providers
            Object[] remoteTermServiceProvidersArray = termPropertyGroup.getArrayProperty("remoteTermServiceProviders");
            if (remoteTermServiceProvidersArray != null) {
                for (Object remoteTermServiceProviderObject : remoteTermServiceProvidersArray) {
                    PropertyGroup remoteTermServiceProviderPropertyGroup = (PropertyGroup) remoteTermServiceProviderObject;
                    Boolean enabled = remoteTermServiceProviderPropertyGroup.getBooleanProperty("enabled", Boolean.FALSE);
                    if (!enabled) {
                        continue;
                    }
                    try {
                        Configuration.Builder builder = Configuration.builder();

                        builder.base(remoteTermServiceProviderPropertyGroup.getStringProperty("base"));

                        PropertyGroup trustStorePropertyGroup = remoteTermServiceProviderPropertyGroup.getPropertyGroup("trustStore");
                        if (trustStorePropertyGroup != null) {
                            builder.trustStore(TrustStore.builder()
                                .location(trustStorePropertyGroup.getStringProperty("location"))
                                .password(trustStorePropertyGroup.getStringProperty("password"))
                                .type(trustStorePropertyGroup.getStringProperty("type", TrustStore.DEFAULT_TYPE))
                                .build());
                        }

                        builder.hostnameVerificationEnabled(remoteTermServiceProviderPropertyGroup.getBooleanProperty("hostnameVerificationEnabled", Configuration.DEFAULT_HOSTNAME_VERIFICATION_ENABLED));

                        PropertyGroup basicAuthPropertyGroup = remoteTermServiceProviderPropertyGroup.getPropertyGroup("basicAuth");
                        if (basicAuthPropertyGroup != null) {
                            builder.basicAuth(BasicAuth.builder()
                                .username(basicAuthPropertyGroup.getStringProperty("username"))
                                .password(basicAuthPropertyGroup.getStringProperty("password"))
                                .build());
                        }

                        Object[] headersArray = remoteTermServiceProviderPropertyGroup.getArrayProperty("headers");
                        if (headersArray != null) {
                            for (Object headerObject : headersArray) {
                                PropertyGroup headerPropertyGroup = (PropertyGroup) headerObject;
                                builder.headers(Header.builder()
                                    .name(headerPropertyGroup.getStringProperty("name"))
                                    .value(headerPropertyGroup.getStringProperty("value"))
                                    .build());
                            }
                        }

                        builder.httpTimeout(remoteTermServiceProviderPropertyGroup.getIntProperty("httpTimeout", Configuration.DEFAULT_HTTP_TIMEOUT));

                        Object[] supportsArray = remoteTermServiceProviderPropertyGroup.getArrayProperty("supports");
                        if (supportsArray != null) {
                            for (Object supportsObject : supportsArray) {
                                PropertyGroup supportsPropertyGroup = (PropertyGroup) supportsObject;
                                builder.supports(Supports.builder()
                                    .system(supportsPropertyGroup.getStringProperty("system"))
                                    .version(supportsPropertyGroup.getStringProperty("version"))
                                    .build());
                            }
                        }

                        Configuration configuration = builder.build();

                        RemoteTermServiceProvider remoteTermServiceProvider = new RemoteTermServiceProvider(configuration);
                        FHIRTermService.getInstance().addProvider(cachingDisabled ? remoteTermServiceProvider : CachingProxy.newInstance(FHIRTermServiceProvider.class, remoteTermServiceProvider));
                        remoteTermServiceProviders.add(remoteTermServiceProvider);
                    } catch (Exception e) {
                        log.log(Level.WARNING, "Unable to create RemoteTermServiceProvider from configuration property group: " + remoteTermServiceProviderPropertyGroup, e);
                    }
                }
            }
        }
    }
}
