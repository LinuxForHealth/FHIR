/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.server.listener;

import static com.ibm.watsonhealth.fhir.config.FHIRConfiguration.PROPERTY_JDBC_BOOTSTRAP_DB;
import static com.ibm.watsonhealth.fhir.config.FHIRConfiguration.PROPERTY_JDBC_SCHEMA_TYPE;
import static com.ibm.watsonhealth.fhir.config.FHIRConfiguration.PROPERTY_KAFKA_CONNECTIONPROPS;
import static com.ibm.watsonhealth.fhir.config.FHIRConfiguration.PROPERTY_KAFKA_ENABLED;
import static com.ibm.watsonhealth.fhir.config.FHIRConfiguration.PROPERTY_KAFKA_TOPICNAME;
import static com.ibm.watsonhealth.fhir.config.FHIRConfiguration.PROPERTY_WEBSOCKET_ENABLED;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import javax.websocket.server.ServerContainer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import com.ibm.watsonhealth.fhir.config.FHIRConfigHelper;
import com.ibm.watsonhealth.fhir.config.FHIRConfiguration;
import com.ibm.watsonhealth.fhir.config.FHIRRequestContext;
import com.ibm.watsonhealth.fhir.config.PropertyGroup;
import com.ibm.watsonhealth.fhir.config.PropertyGroup.PropertyEntry;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;
import com.ibm.watsonhealth.fhir.notification.websocket.impl.FHIRNotificationServiceEndpointConfig;
import com.ibm.watsonhealth.fhir.notifications.kafka.impl.FHIRNotificationKafkaPublisher;
import com.ibm.watsonhealth.fhir.operation.registry.FHIROperationRegistry;
import com.ibm.watsonhealth.fhir.persistence.helper.FHIRPersistenceHelper;
import com.ibm.watsonhealth.fhir.persistence.jdbc.SchemaType;
import com.ibm.watsonhealth.fhir.persistence.jdbc.util.DerbyBootstrapper;
import com.ibm.watsonhealth.fhir.search.util.SearchUtil;

@WebListener("IBM Watson Health Cloud FHIR Server Servlet Context Listener")
public class FHIRServletContextListener implements ServletContextListener {
    private static final Logger log = Logger.getLogger(FHIRServletContextListener.class.getName());
	
	private static final String ATTRNAME_WEBSOCKET_SERVERCONTAINER = "javax.websocket.server.ServerContainer";
	private static final String DEFAULT_KAFKA_TOPICNAME = "fhirNotifications";
    public static final String FHIR_SERVER_INIT_COMPLETE = "com.ibm.watsonhealth.fhir.webappInitComplete";
    private static FHIRNotificationKafkaPublisher kafkaPublisher = null;

	@Override
	public void contextInitialized(ServletContextEvent event) {
		if (log.isLoggable(Level.FINER)) {
			log.entering(FHIRServletContextListener.class.getName(), "contextInitialized");
		}
		try {
            // Initialize our "initComplete" flag to false.
            event.getServletContext().setAttribute(FHIR_SERVER_INIT_COMPLETE, Boolean.FALSE);
            
		    PropertyGroup fhirConfig = FHIRConfiguration.getInstance().loadConfiguration();
		    
		    log.fine("Current working directory: " + System.getProperty("user.dir"));
		    
		    log.fine("Initializing FHIRUtil...");
		    FHIRUtil.init();
		    
		    log.fine("Initializing SearchUtil...");
		    SearchUtil.init();
		    
		    log.fine("Initializing FHIROperationRegistry...");
		    FHIROperationRegistry.getInstance();
		    
			// For any singleton resources that need to be shared among our resource class instances,
		    // we'll add them to our servlet context so that the resource class can easily retrieve them.
		    
			// Set the shared FHIRPersistenceHelper.
            event.getServletContext().setAttribute(FHIRPersistenceHelper.class.getName(), new FHIRPersistenceHelper());
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
            
            bootstrapDerbyDatabases(fhirConfig);
            
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
            SchemaType schemaType = SchemaType.fromValue(fhirConfig.getStringProperty(PROPERTY_JDBC_SCHEMA_TYPE));
            String datasourceJndiName = fhirConfig.getStringProperty(FHIRConfiguration.PROPERTY_JDBC_DATASOURCE_JNDINAME, "jdbc/fhirDB");
            InitialContext ctxt = new InitialContext();
            DataSource ds = (DataSource) ctxt.lookup(datasourceJndiName);

            String derbySprocJarDir = "userlib";
		    Collection<File> derbyJarFiles = FileUtils.listFiles(new File(derbySprocJarDir), new WildcardFileFilter("fhir-derby-sproc*.jar"), null);
            
		    String derbySProcJarLocation = null;
            if(!derbyJarFiles.isEmpty() && schemaType == SchemaType.NORMALIZED) {
            	derbySProcJarLocation = derbySprocJarDir + "/" + derbyJarFiles.iterator().next().getName();
            } else {
            	derbySProcJarLocation = "";	//The JAR file is not used while bootstrapping Derby for BASIC schema type
            }
            bootstrapDb("default", "default", ds, schemaType, derbySProcJarLocation);
            bootstrapDb("tenant1", "profile", ds, schemaType, derbySProcJarLocation);
            bootstrapDb("tenant1", "reference", ds, schemaType, derbySProcJarLocation);
            bootstrapDb("tenant1", "study1", ds, schemaType, derbySProcJarLocation);
            log.info("Finished Derby database bootstrapping...");
        } else {
            log.info("Derby database bootstrapping is disabled.");
        }
    }
    
    /**
     * Bootstraps the database specified by tenantId and dsId, assuming the specified datastore definition can be
     * retrieved from the configuration.
     */
    private void bootstrapDb(String tenantId, String dsId, DataSource ds, SchemaType schemaType, String derbyJar) throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext(tenantId, dsId));
        PropertyGroup pg = FHIRConfigHelper.getPropertyGroup(FHIRConfiguration.PROPERTY_DATASOURCES + "/" + dsId);
        if (pg != null) {
            String type = pg.getStringProperty("type");
            if (type != null && !type.isEmpty() && (type.toLowerCase().equals("derby") || type.toLowerCase().equals("derby_network_server"))) {
                log.info("Bootstrapping database for tenantId/dsId: " + tenantId + "/" + dsId);
                DerbyBootstrapper.bootstrapDb(ds, schemaType, derbyJar);
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

            // If we previously intialized the Kafka publisher, then shut it down now.
            if (kafkaPublisher != null) {
                kafkaPublisher.shutdown();
                kafkaPublisher = null;
            }
        } catch (Exception e) {
        } finally {
            if (log.isLoggable(Level.FINER)) {
                log.exiting(FHIRServletContextListener.class.getName(), "contextDestroyed");
            }
        }
    }
}
