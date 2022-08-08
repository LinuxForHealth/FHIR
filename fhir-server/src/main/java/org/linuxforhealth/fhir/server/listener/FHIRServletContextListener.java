/*
 * (C) Copyright IBM Corp. 2016, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.listener;

import static org.linuxforhealth.fhir.config.FHIRConfiguration.PROPERTY_CHECK_CONTROL_CHARS;
import static org.linuxforhealth.fhir.config.FHIRConfiguration.PROPERTY_CHECK_REFERENCE_TYPES;
import static org.linuxforhealth.fhir.config.FHIRConfiguration.PROPERTY_DATASOURCES;
import static org.linuxforhealth.fhir.config.FHIRConfiguration.PROPERTY_EXTENDED_CODEABLE_CONCEPT_VALIDATION;
import static org.linuxforhealth.fhir.config.FHIRConfiguration.PROPERTY_KAFKA_CONNECTIONPROPS;
import static org.linuxforhealth.fhir.config.FHIRConfiguration.PROPERTY_KAFKA_ENABLED;
import static org.linuxforhealth.fhir.config.FHIRConfiguration.PROPERTY_KAFKA_INDEX_SERVICE_CONNECTIONPROPS;
import static org.linuxforhealth.fhir.config.FHIRConfiguration.PROPERTY_KAFKA_INDEX_SERVICE_MODE;
import static org.linuxforhealth.fhir.config.FHIRConfiguration.PROPERTY_KAFKA_INDEX_SERVICE_TOPICNAME;
import static org.linuxforhealth.fhir.config.FHIRConfiguration.PROPERTY_KAFKA_TOPICNAME;
import static org.linuxforhealth.fhir.config.FHIRConfiguration.PROPERTY_NATS_CHANNEL;
import static org.linuxforhealth.fhir.config.FHIRConfiguration.PROPERTY_NATS_CLIENT;
import static org.linuxforhealth.fhir.config.FHIRConfiguration.PROPERTY_NATS_CLUSTER;
import static org.linuxforhealth.fhir.config.FHIRConfiguration.PROPERTY_NATS_ENABLED;
import static org.linuxforhealth.fhir.config.FHIRConfiguration.PROPERTY_NATS_KEYSTORE;
import static org.linuxforhealth.fhir.config.FHIRConfiguration.PROPERTY_NATS_KEYSTORE_PW;
import static org.linuxforhealth.fhir.config.FHIRConfiguration.PROPERTY_NATS_SERVERS;
import static org.linuxforhealth.fhir.config.FHIRConfiguration.PROPERTY_NATS_TLS_ENABLED;
import static org.linuxforhealth.fhir.config.FHIRConfiguration.PROPERTY_NATS_TRUSTSTORE;
import static org.linuxforhealth.fhir.config.FHIRConfiguration.PROPERTY_NATS_TRUSTSTORE_PW;
import static org.linuxforhealth.fhir.config.FHIRConfiguration.PROPERTY_REMOTE_INDEX_SERVICE_INSTANCEIDENTIFIER;
import static org.linuxforhealth.fhir.config.FHIRConfiguration.PROPERTY_REMOTE_INDEX_SERVICE_TYPE;
import static org.linuxforhealth.fhir.config.FHIRConfiguration.PROPERTY_SERVER_REGISTRY_RESOURCE_PROVIDER_ENABLED;
import static org.linuxforhealth.fhir.config.FHIRConfiguration.PROPERTY_SERVER_RESOLVE_FUNCTION_ENABLED;
import static org.linuxforhealth.fhir.config.FHIRConfiguration.PROPERTY_WEBSOCKET_ENABLED;

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

import org.linuxforhealth.fhir.cache.CachingProxy;
import org.linuxforhealth.fhir.config.CallTimeMetric;
import org.linuxforhealth.fhir.config.FHIRConfiguration;
import org.linuxforhealth.fhir.config.FHIRRequestContext;
import org.linuxforhealth.fhir.config.PropertyGroup;
import org.linuxforhealth.fhir.config.PropertyGroup.PropertyEntry;
import org.linuxforhealth.fhir.core.lifecycle.EventManager;
import org.linuxforhealth.fhir.database.utils.derby.DerbyServerPropertiesMgr;
import org.linuxforhealth.fhir.database.utils.thread.ThreadHandler;
import org.linuxforhealth.fhir.model.config.FHIRModelConfig;
import org.linuxforhealth.fhir.model.lang.util.LanguageRegistryUtil;
import org.linuxforhealth.fhir.model.util.FHIRUtil;
import org.linuxforhealth.fhir.model.util.ModelSupport;
import org.linuxforhealth.fhir.path.function.registry.FHIRPathFunctionRegistry;
import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;
import org.linuxforhealth.fhir.persistence.helper.FHIRPersistenceHelper;
import org.linuxforhealth.fhir.persistence.index.FHIRRemoteIndexService;
import org.linuxforhealth.fhir.registry.FHIRRegistry;
import org.linuxforhealth.fhir.search.util.SearchHelper;
import org.linuxforhealth.fhir.server.index.kafka.FHIRRemoteIndexKafkaService;
import org.linuxforhealth.fhir.server.index.kafka.KafkaPropertyAdapter;
import org.linuxforhealth.fhir.server.notification.kafka.FHIRNotificationKafkaPublisher;
import org.linuxforhealth.fhir.server.notification.websocket.FHIRNotificationServiceEndpointConfig;
import org.linuxforhealth.fhir.server.notifications.nats.FHIRNotificationNATSPublisher;
import org.linuxforhealth.fhir.server.operation.FHIROperationRegistry;
import org.linuxforhealth.fhir.server.registry.ServerRegistryResourceProvider;
import org.linuxforhealth.fhir.server.resolve.ServerResolveFunction;
import org.linuxforhealth.fhir.server.spi.operation.FHIROperationUtil;
import org.linuxforhealth.fhir.term.config.FHIRTermConfig;
import org.linuxforhealth.fhir.term.graph.provider.GraphTermServiceProvider;
import org.linuxforhealth.fhir.term.remote.provider.RemoteTermServiceProvider;
import org.linuxforhealth.fhir.term.remote.provider.RemoteTermServiceProvider.Configuration;
import org.linuxforhealth.fhir.term.remote.provider.RemoteTermServiceProvider.Configuration.BasicAuth;
import org.linuxforhealth.fhir.term.remote.provider.RemoteTermServiceProvider.Configuration.Header;
import org.linuxforhealth.fhir.term.remote.provider.RemoteTermServiceProvider.Configuration.Supports;
import org.linuxforhealth.fhir.term.remote.provider.RemoteTermServiceProvider.Configuration.TrustStore;
import org.linuxforhealth.fhir.term.service.FHIRTermService;
import org.linuxforhealth.fhir.term.spi.FHIRTermServiceProvider;

@WebListener("IBM FHIR Server Servlet Context Listener")
public class FHIRServletContextListener implements ServletContextListener {
    private static final Logger log = Logger.getLogger(FHIRServletContextListener.class.getName());
    private static final Logger metricLogger = Logger.getLogger("org.linuxforhealth.fhir.MetricReport");

    private static final String ATTRNAME_WEBSOCKET_SERVERCONTAINER = "javax.websocket.server.ServerContainer";
    private static final String DEFAULT_KAFKA_TOPICNAME = "fhirNotifications";
    private static final String DEFAULT_KAFKA_INDEX_SERVICE_TOPICNAME = "fhirIndex";
    private static final String DEFAULT_NATS_CHANNEL = "fhirNotifications";
    private static final String DEFAULT_NATS_CLUSTER = "nats-streaming";
    private static final String DEFAULT_NATS_CLIENT = "fhir-server";
    public static final String FHIR_SERVER_INIT_COMPLETE = "org.linuxforhealth.fhir.webappInitComplete";
    private static FHIRNotificationKafkaPublisher kafkaPublisher = null;
    private static FHIRNotificationNATSPublisher natsPublisher = null;
    private static FHIRRemoteIndexService remoteIndexService = null;
    private static volatile boolean running = true;
    private static Thread metricReportThread = null;

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

            EventManager.registerServiceManagerId(serviceManagerId);

            FHIRConfiguration.setConfigHome(System.getenv("FHIR_CONFIG_HOME"));
            PropertyGroup fhirConfig = FHIRConfiguration.getInstance().loadConfiguration();
            if (fhirConfig == null) {
                throw new IllegalStateException("No FHIRConfiguration was found");
            }

            log.fine("Current working directory: " + Encode.forHtml(System.getProperty("user.dir")));

            /*
             * The following inits are intended to load the Support / Util classes into the classloader.
             * Subsequently, the code activates the static values (and maps).
             *
             * @see JobControlContextListener to see register these settings for Bulk Data.
             */

            log.fine("Initializing ModelSupport...");
            ModelSupport.init();

            log.fine("Initializing FHIRUtil...");
            FHIRUtil.init();

            log.fine("Initializing FHIRRegistry...");
            FHIRRegistry.getInstance();
            FHIRRegistry.init();

            log.fine("Initializing SearchUtil...");
            // Set the shared SearchUtil
            SearchHelper searchHelper = new SearchHelper();
            event.getServletContext().setAttribute(SearchHelper.class.getName(), searchHelper);
            log.fine("Set shared search helper on servlet context.");

            log.fine("Initializing FHIROperationRegistry...");
            FHIROperationRegistry.getInstance();

            log.fine("Initializing FHIROperationUtil...");
            FHIROperationUtil.init();

            log.fine("Initializing LanguageRegistryUtil...");
            LanguageRegistryUtil.init();

            setDerbyProperties(fhirConfig);

            // For any singleton resources that need to be shared among our resource class instances,
            // we'll add them to our servlet context so that the resource class can easily retrieve them.

            // Set the shared FHIRPersistenceHelper.
            FHIRPersistenceHelper persistenceHelper = new FHIRPersistenceHelper(searchHelper);
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

            // If the Kafka async indexing service is enabled, set it up so that it's ready to go
            // before we starting processing any requests.
            String remoteIndexServiceType = fhirConfig.getStringProperty(PROPERTY_REMOTE_INDEX_SERVICE_TYPE, null);
            if (remoteIndexServiceType != null) {
                if ("kafka".equals(remoteIndexServiceType)) {
                    String topicName = fhirConfig.getStringProperty(PROPERTY_KAFKA_INDEX_SERVICE_TOPICNAME, DEFAULT_KAFKA_INDEX_SERVICE_TOPICNAME);
                    String instanceIdentifier = fhirConfig.getStringProperty(PROPERTY_REMOTE_INDEX_SERVICE_INSTANCEIDENTIFIER);
                    String mode = fhirConfig.getStringProperty(PROPERTY_KAFKA_INDEX_SERVICE_MODE, "active");
    
                    // Gather up the Kafka connection properties for the async index service
                    Properties kafkaProps = new Properties();
                    PropertyGroup pg = fhirConfig.getPropertyGroup(PROPERTY_KAFKA_INDEX_SERVICE_CONNECTIONPROPS);
                    if (pg != null) {
                        List<PropertyEntry> connectionProps = pg.getProperties();
                        if (connectionProps != null) {
                            for (PropertyEntry entry : connectionProps) {
                                kafkaProps.setProperty(entry.getName(), entry.getValue().toString());
                            }
                        }
                    }
    
                    log.info("Initializing Kafka async indexing service.");
                    FHIRRemoteIndexKafkaService s = new FHIRRemoteIndexKafkaService();
                    s.init(new KafkaPropertyAdapter(instanceIdentifier, topicName, kafkaProps, KafkaPropertyAdapter.Mode.valueOf(mode)));
                    // Now the service is ready, we can publish it
                    remoteIndexService = s;
                    FHIRRemoteIndexService.setServiceInstance(remoteIndexService);
                } else {
                    throw new FHIRPersistenceException("Invalid value for remote index service property '" + PROPERTY_REMOTE_INDEX_SERVICE_TYPE + "'");
                }
            } else {
                log.info("Bypassing Kafka async indexing service configuration.");
            }

            Boolean checkReferenceTypes = fhirConfig.getBooleanProperty(PROPERTY_CHECK_REFERENCE_TYPES, Boolean.TRUE);
            FHIRModelConfig.setCheckReferenceTypes(checkReferenceTypes);

            Boolean extendedCodeableConceptValidation = fhirConfig.getBooleanProperty(PROPERTY_EXTENDED_CODEABLE_CONCEPT_VALIDATION, Boolean.TRUE);
            FHIRModelConfig.setExtendedCodeableConceptValidation(extendedCodeableConceptValidation);

            Boolean checkUnicodeChars = fhirConfig.getBooleanProperty(PROPERTY_CHECK_CONTROL_CHARS, Boolean.TRUE);
            FHIRModelConfig.setCheckForControlChars(checkUnicodeChars);

            Boolean serverRegistryResourceProviderEnabled = fhirConfig.getBooleanProperty(PROPERTY_SERVER_REGISTRY_RESOURCE_PROVIDER_ENABLED, Boolean.FALSE);
            if (serverRegistryResourceProviderEnabled) {
                log.info("Registering ServerRegistryResourceProvider...");
                FHIRRegistry.getInstance().addProvider(new ServerRegistryResourceProvider(persistenceHelper, searchHelper));
            }

            Boolean serverResolveFunctionEnabled = fhirConfig.getBooleanProperty(PROPERTY_SERVER_RESOLVE_FUNCTION_ENABLED, Boolean.FALSE);
            if (serverResolveFunctionEnabled) {
                log.info("Registering ServerResolveFunction...");
                FHIRPathFunctionRegistry.getInstance().register(new ServerResolveFunction(persistenceHelper));
            }

            configureTermServiceCapabilities(fhirConfig);

            if (metricLogger.isLoggable(Level.FINE)) {
                this.metricReportThread = new Thread(() -> metricReportLoop());
                this.metricReportThread.start();
            }

            // Set our "initComplete" flag to true.
            event.getServletContext().setAttribute(FHIR_SERVER_INIT_COMPLETE, Boolean.TRUE);

            // Now init is complete, tell all registered callbacks
            EventManager.serverReady(serviceManagerId);
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
     * Continuous loop to periodically write out any metrics we've collected
     */
    private void metricReportLoop() {
        do {
            // report metrics every minute
            ThreadHandler.safeSleep(60000);
            if (metricLogger.isLoggable(Level.FINE)) {
                CallTimeMetric.render(metricLogger, FHIRRequestContext.getAndResetMetrics());
            }
        } while (running);
    }

    /**
     * If the default datasource is configured to use Derby then set some internal
     * Derby properties to make things run a little more smoothly.
     * @param fhirConfig
     * @throws Exception
     */
    private void setDerbyProperties(PropertyGroup fhirConfig) throws Exception {
        // Check the default datasource to see if it is using Derby
        PropertyGroup defaultDatasource = fhirConfig.getPropertyGroup(PROPERTY_DATASOURCES + "/default");
        String datasourceType = defaultDatasource.getStringProperty("type", "unknown");
        if ("derby".equalsIgnoreCase(datasourceType)) {
            log.info("Detected Derby datasource so configuring Derby properties.");
            DerbyServerPropertiesMgr.setServerProperties(false);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        if (log.isLoggable(Level.FINER)) {
            log.entering(FHIRServletContextListener.class.getName(), "contextDestroyed");
        }
        try {
            running = false;
            if (metricReportThread != null) {
                metricReportThread.interrupt();
                metricReportThread.join();
            }

            // Set our "initComplete" flag back to false.
            event.getServletContext().setAttribute(FHIR_SERVER_INIT_COMPLETE, Boolean.FALSE);

            // Tell anyone who's interested that the server is being shut down. Should not block
            EventManager.startShutdown(serviceManagerId);

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
            EventManager.finalShutdown(serviceManagerId);
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