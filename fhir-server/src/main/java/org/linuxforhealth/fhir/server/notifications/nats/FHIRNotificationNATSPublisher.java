/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.notifications.nats;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.linuxforhealth.fhir.server.notification.FHIRNotificationEvent;
import org.linuxforhealth.fhir.server.notification.FHIRNotificationException;
import org.linuxforhealth.fhir.server.notification.FHIRNotificationService;
import org.linuxforhealth.fhir.server.notification.FHIRNotificationSubscriber;
import org.linuxforhealth.fhir.server.notification.FHIRNotificationUtil;

import io.nats.client.Connection;
import io.nats.client.Nats;
import io.nats.streaming.AckHandler;
import io.nats.streaming.NatsStreaming;
import io.nats.streaming.Options;
import io.nats.streaming.StreamingConnection;

/**
 * This class implements the FHIR server notification service via a NATS channel.
 */
public class FHIRNotificationNATSPublisher implements FHIRNotificationSubscriber {
    private static final Logger log = Logger.getLogger(FHIRNotificationNATSPublisher.class.getName());
    private static FHIRNotificationService service = FHIRNotificationService.getInstance();

    private StreamingConnection sc = null;
    private AckHandler acb = null;
    private String channelName = null;

    // "Hide" the default constructor.
    protected FHIRNotificationNATSPublisher() {
    }

    public FHIRNotificationNATSPublisher(String clusterId, String channelName, String clientId, String servers, Properties tlsProps) {
        log.entering(this.getClass().getName(), "constructor");
        try {
            init(clusterId, channelName, clientId, servers, tlsProps);
        } finally {
            log.exiting(this.getClass().getName(), "constructor");
        }
    }

    /**
     * Performs any required initialization to allow us to publish events to the channel.
     */
    private void init(String clusterId, String channelName, String clientId, String servers, Properties tlsProps) {
        log.entering(this.getClass().getName(), "init");

        SSLContext ctx = null;

        try {
            this.channelName = channelName;
            if (log.isLoggable(Level.FINER)) {
                log.finer("ClusterId: " + clusterId);
                log.finer("Channel name: " + channelName);
                log.finer("ClientId: " + clientId);
                log.finer("Servers: " + servers);
            }

            // Make sure that the properties file contains the expected properties.
            if (clusterId == null || channelName == null || clientId == null || servers == null || servers.length() == 0) {
                throw new IllegalStateException("Config property missing from the NATS connection properties.");
            }

            if (Boolean.parseBoolean(tlsProps.getProperty("useTLS"))) {
                // Make sure that the tls properties are set.
                if (tlsProps.getProperty("truststore") == null || tlsProps.getProperty("truststorePass") == null ||
                    tlsProps.getProperty("keystore") == null || tlsProps.getProperty("keystorePass") == null) {
                    throw new IllegalStateException("TLS config property missing from the NATS connection properties.");
                }

                ctx = createSSLContext(tlsProps);
            }

            // Create the NATS client connection options
            io.nats.client.Options.Builder builder = new io.nats.client.Options.Builder();
            builder.maxReconnects(-1);
            builder.connectionName(channelName);
            builder.servers(servers.split(","));
            if (ctx != null) {
                builder.sslContext(ctx);
            }
            io.nats.client.Options natsOptions = builder.build();

            // Create the NATS connection and the streaming connection
            Connection nc = Nats.connect(natsOptions);
            Options streamingOptions = new Options.Builder().natsConn(nc).build();
            sc = NatsStreaming.connect(clusterId, clientId, streamingOptions);

            // Create the publish callback
            acb = new AckHandler() {
                @Override
                public void onAck(String nuid, Exception ex) {
                    log.finer("Received ACK for guid: " + nuid);
                    if (ex != null && log.isLoggable(Level.SEVERE)) {
                        log.log(Level.SEVERE, "Error in server ack for guid " + nuid + ": " + ex.getMessage(), ex);
                    }
                }
            };

            // Register this NATS implementation as a "subscriber" with our Notification Service.
            // This means that our "notify" method will be called when the server publishes an event.
            service.subscribe(this);
            log.info("Initialized NATS publisher for channel '" + channelName + "' using servers: '" + servers + "'.");
        } catch (Throwable t) {
            String msg = "Caught exception while initializing NATS publisher.";
            log.log(Level.SEVERE, msg, t);
            throw new IllegalStateException(msg, t);
        } finally {
            log.exiting(this.getClass().getName(), "init");
        }
    }

    /**
     * Performs any necessary "shutdown" logic to disconnect from the channel.
     */
    public void shutdown() {
        log.entering(this.getClass().getName(), "shutdown");

        try {
            if (log.isLoggable(Level.FINE)) {
                log.fine("Shutting down NATS publisher for channel: '" + channelName + "'.");
            }
            if (sc != null) {
               sc.close();
            }
        } catch (Throwable t) {
            String msg = "Caught exception shutting down NATS publisher for channel: '" + channelName + "'.";
            log.log(Level.SEVERE, msg, t);
            throw new IllegalStateException(msg, t);
        } finally {
            log.exiting(this.getClass().getName(), "shutdown");
        }
    }

    /**
     * Publishes an event to NATS.
     */
    @Override
    public void notify(FHIRNotificationEvent event) throws FHIRNotificationException {
        log.entering(this.getClass().getName(), "notify");
        String jsonString = null;
        try {
            jsonString = FHIRNotificationUtil.toJsonString(event, true);

            if (log.isLoggable(Level.FINE)) {
                log.fine("Publishing NATS notification event to channel '" + channelName + "',\nmessage: '" + jsonString + "'.");
            }

            sc.publish("FHIRNotificationEvent", jsonString.getBytes(), acb);

            if (log.isLoggable(Level.FINE)) {
                log.fine("Published NATS notification event to channel '" + channelName + "'");
            }
        } catch (Throwable e) {
            String msg = buildNotificationErrorMessage(channelName, (jsonString == null ? "<null>" : jsonString));
            log.log(Level.SEVERE, msg , e);
            throw new FHIRNotificationException(msg, e);
        } finally {
            log.exiting(this.getClass().getName(), "notify");
        }
    }

    /**
     * Builds a formatted error message to indicate a notification publication failure.
     */
    private String buildNotificationErrorMessage(String channelName, String notificationEvent) {
        return String.format("NATS publication failure; channel '%s'\nNotification event: '%s'\n.", channelName, notificationEvent);
    }
    /*
     * Modified from original NATS documentation @ https://docs.nats.io/developing-with-nats/security/tls
     * openssl is used to generate a pkcs12 file (.p12) from client-cert.pem and client-key.pem.
     * The resulting file is then imported into a java keystore using keytool which is part of java jdk.
     * keytool is also used to import the CA certificate rootCA.pem into the truststore.
     */
    private static KeyStore loadKeystore(String path, String password) throws Exception {
        KeyStore store = KeyStore.getInstance("PKCS12");
        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(path));) {
            store.load(in, password.toCharArray());
        }
        return store;
    }

    private static KeyManager[] createKeyManagers(Properties tlsProps) throws Exception {
        KeyStore store = loadKeystore(tlsProps.getProperty("keystore"), tlsProps.getProperty("keystorePass"));
        KeyManagerFactory factory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        factory.init(store, tlsProps.getProperty("keystorePass").toCharArray());
        return factory.getKeyManagers();
    }

    private static TrustManager[] createTrustManagers(Properties tlsProps) throws Exception {
        KeyStore store = loadKeystore(tlsProps.getProperty("truststore"), tlsProps.getProperty("truststorePass"));
        TrustManagerFactory factory = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        factory.init(store);
        return factory.getTrustManagers();
    }

    private static SSLContext createSSLContext(Properties tlsProps) throws Exception {
        SSLContext ctx = SSLContext.getInstance("TLSv1.2");
        ctx.init(createKeyManagers(tlsProps), createTrustManagers(tlsProps), new SecureRandom());
        return ctx;
    }
}
