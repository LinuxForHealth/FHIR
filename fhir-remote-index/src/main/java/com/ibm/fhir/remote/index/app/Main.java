/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.remote.index.app;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;

import com.ibm.fhir.database.utils.api.IConnectionProvider;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.JdbcConnectionProvider;
import com.ibm.fhir.database.utils.postgres.PostgresPropertyAdapter;
import com.ibm.fhir.database.utils.postgres.PostgresTranslator;
import com.ibm.fhir.database.utils.thread.ThreadHandler;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.remote.index.api.IMessageHandler;
import com.ibm.fhir.remote.index.cache.IdentityCacheImpl;
import com.ibm.fhir.remote.index.database.CacheLoader;
import com.ibm.fhir.remote.index.database.DistributedPostgresMessageHandler;
import com.ibm.fhir.remote.index.kafka.RemoteIndexConsumer;

/**
 * Main class for the FHIR remote index service Kafka consumer
 */
public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    // Properties holding the Kafka connection information
    private final Properties kafkaProperties = new Properties();
    // Properties holding the JDBC connection information
    private final Properties databaseProperties = new Properties();

    private String topicName;

    // The standard consumer group which all the remote index consumers should use
    private String consumerGroup = "remote-index-service-cg";

    private int consumerCount = 1;

    private Duration pollDuration = Duration.ofSeconds(10);
    private long maxBatchCollectTimeMs = 5000;

    // the list of consumers
    private final List<RemoteIndexConsumer> consumers = new ArrayList<>();

    // track the number of consumers that are still running
    private AtomicInteger stillRunningCounter;

    private volatile boolean running = true;

    // Exit if we drop below this number of running consumers
    private float minRunningConsumerRatio = 0.5f;
    private int minRunningConsumerThreshold;
    private IdentityCacheImpl identityCache;

    // Database Configuration
    private IDatabaseTranslator translator;
    private IConnectionProvider connectionProvider;
    
    /**
     * Parse the given command line arguments
     * @param args
     */
    public void parseArgs(String[] args) {
        int a = 0;
        while (a < args.length) {
            final String arg = args[a++];
            switch (arg) {
            case "--kafka-properties":
                if (a < args.length && !args[a].startsWith("--")) {
                    loadKafkaProperties(args[a++]);
                } else {
                    throw new IllegalArgumentException("Missing value for --kafka-properties");
                }
                break;
            case "--database-properties":
                if (a < args.length && !args[a].startsWith("--")) {
                    loadDatabaseProperties(args[a++]);
                } else {
                    throw new IllegalArgumentException("Missing value for --database-properties");
                }
                break;
            case "--topic-name":
                if (a < args.length && !args[a].startsWith("--")) {
                    topicName = args[a++];
                } else {
                    throw new IllegalArgumentException("Missing value for --topic-name");
                }
                break;
            case "--consumer-group":
                if (a < args.length && !args[a].startsWith("--")) {
                    consumerGroup = args[a++];
                } else {
                    throw new IllegalArgumentException("Missing value for --consumer-group");
                }
                break;
            case "--consumer-count":
                if (a < args.length && !args[a].startsWith("--")) {
                    consumerCount = Integer.parseInt(args[a++]);
                } else {
                    throw new IllegalArgumentException("Missing value for --consumer-count");
                }
                break;
            default:
                throw new IllegalArgumentException("Bad arg: '" + arg + "'");
            }
        }
    }

    /**
     * Read kafka properties from the given file
     * @param filename
     */
    protected void loadKafkaProperties(String filename) {
        try (InputStream is = new FileInputStream(filename)) {
            kafkaProperties.load(is);
        } catch (IOException x) {
            throw new IllegalArgumentException(x);
        }
    }

    /**
     * Read database properties from the given file
     * @param filename
     */
    protected void loadDatabaseProperties(String filename) {
        try (InputStream is = new FileInputStream(filename)) {
            databaseProperties.load(is);
        } catch (IOException x) {
            throw new IllegalArgumentException(x);
        }
    }

    /**
     * Get the configured schema name for where we need to use it explicitly
     * @return
     * @throws FHIRPersistenceException
     */
    private String getSchemaName() throws FHIRPersistenceException {
        String result = databaseProperties.getProperty("currentSchema");
        if (result == null) {
            throw new FHIRPersistenceException("currentSchema value missing in database properties");
        }
        return result;
    }

    /**
     * Keep consuming from Kafka forever...or until we see too many
     * consumers fail
     */
    public void run() throws FHIRPersistenceException {
        dumpProperties("kafka", kafkaProperties);
        dumpProperties("database", databaseProperties);
        configureForPostgres();
        initIdentityCache();

        // Keep track of how many consumers are still running. If too many fail,
        // we stop everything and exit which allows our operating environment
        // to handle things perhaps by restarting us somewhere else
        stillRunningCounter = new AtomicInteger(this.consumerCount);
        this.minRunningConsumerThreshold = Math.max(1, Math.round(this.consumerCount * minRunningConsumerRatio));

        // One thread per consumer
        ExecutorService pool = Executors.newCachedThreadPool();
        for (int i=0; i<this.consumerCount; i++) {
            KafkaConsumer<String, String> kc = buildConsumer();
            if (i == 0) {
                // use the first consumer to check we have partitions for the configured topic
                doPartitionCheck(kc);
            }
            IMessageHandler handler = buildHandler();
            RemoteIndexConsumer consumer = new RemoteIndexConsumer(kc, handler, () -> failedConsumerCallback(), topicName, maxBatchCollectTimeMs, pollDuration);
            pool.submit(consumer);
            // Keep track of the consumer, so that we can signal a shutdown if we need to
            consumers.add(consumer);
        }

        // Hold in a slow poll loop, ideally forever. We only exit if too
        // many consumers fail
        while (running) {
            ThreadHandler.safeSleep(1000);
        }

        // Make sure anything still running is stopped
        logger.warning("Too many consumers have failed, so stopping everything");
        for (RemoteIndexConsumer consumer: consumers) {
            consumer.shutdown();
        }

        // Try to make the exit as clean as possible, although this may not happen
        // because we're likely in some sort of failure scenario here (e.g. network
        // partition, brokers failed, database down etc).
        int waitForTerminationSeconds = 30;
        logger.info("Waiting " + waitForTerminationSeconds + " seconds for consumers to stop");
        pool.shutdown();
        try {
            pool.awaitTermination(waitForTerminationSeconds, TimeUnit.SECONDS);
        } catch (InterruptedException x) {
            logger.warning("Interrupted waiting for consumer pool to terminate");
        }
        logger.info("All consumers stopped");
    }

    /**
     * Set up the identity cache and preload it with all the parameter_names
     * currently in the database
     * @throws FHIRPersistenceException
     */
    private void initIdentityCache() throws FHIRPersistenceException {
        logger.info("Initializing identity cache");
        identityCache = new IdentityCacheImpl(
            1000, Duration.ofSeconds(3600), 
            10000, Duration.ofSeconds(3600),
            1000, Duration.ofSeconds(3600));
        CacheLoader loader = new CacheLoader(identityCache);
        try (Connection connection = connectionProvider.getConnection()) {
            loader.apply(connection);
            connection.commit();
        } catch (SQLException x) {
            throw new FHIRPersistenceException("cache init failed", x);
        }
    }
    /**
     * Create a new consumer
     * @return
     */
    private KafkaConsumer<String,String> buildConsumer() {
            
        Properties kp = new Properties();
        kp.putAll(kafkaProperties);

        // Inject the properties we want to force here
        kp.put("enable.auto.commit", "false");
        kp.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        kp.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        kp.put("group.id", this.consumerGroup);
        kp.put("auto.offset.reset", "earliest");

        KafkaConsumer<String,String> consumer = new KafkaConsumer<>(kp);
        return consumer;
    }

    private void configureForPostgres() {
        this.translator = new PostgresTranslator();
        try {
            Class.forName(translator.getDriverClassName());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }

        PostgresPropertyAdapter propertyAdapter = new PostgresPropertyAdapter(databaseProperties);
        connectionProvider = new JdbcConnectionProvider(translator, propertyAdapter);
    }

    /**
     * Instantiate a new message handler for use by a consumer thread. Each handler gets
     * its own database connection.
     * @return
     * @throws FHIRPersistenceException
     */
    private IMessageHandler buildHandler() throws FHIRPersistenceException {
        Objects.requireNonNull(identityCache, "must set up identityCache first");
        try {
            // Each handler gets a dedicated database connection so we don't have
            // to deal with contention when grabbing connections from a pool
            return new DistributedPostgresMessageHandler(connectionProvider.getConnection(), getSchemaName(), identityCache);
        } catch (SQLException x) {
            throw new FHIRPersistenceException("get connection failed", x);
        }
    }

    /**
     * Get the partitions for the named topic to check if the topic actually exists
     */
    private void doPartitionCheck(KafkaConsumer<String,String> consumer) {
        // Checking for topic existence before subscribing
        List<PartitionInfo> partitions = consumer.partitionsFor(topicName);
        if (partitions == null || partitions.isEmpty()) {
            logger.severe("Topic not found: '" + topicName + "'");
            throw new IllegalStateException("topic not found");
        } else {
            // dump the list of partitions configured for this topic
            for (PartitionInfo pi: partitions) {
                logger.info("Topic '" + topicName + "' has partition " + pi.toString());
            }
        }
    }

    
    /**
     * Log the properties which can help with debugging deployment issues.
     * Hides secrets
     * @param which the type of properties
     * @param p the properties to dump
     */
    protected void dumpProperties(String which, Properties p) {

        if (which != null && p != null) {
            StringBuilder buffer = new StringBuilder();
            buffer.append("{");
            Iterator<Object> keys = p.keySet().iterator();
            boolean first = true;
            while (keys.hasNext()) {
                String key = (String) keys.next();
                String value = p.getProperty(key);
                if (key.toLowerCase().contains("password")) {
                    value = "[*******]";
                }
                if (first) {
                    first = false;
                } else {
                    buffer.append(", ");
                }
                buffer.append("\"").append(key).append("\"");
                buffer.append(": ");
                buffer.append("\"").append(value).append("\"");
            }
            buffer.append("}");
            logger.info(which + ": " + buffer.toString());
        }
    }

    /**
     * Called from a consumer thread when it is about to exit
     */
    private void failedConsumerCallback() {
        final int remainingConsumersStillRunning = stillRunningCounter.decrementAndGet();
        if (remainingConsumersStillRunning < minRunningConsumerThreshold) {
            // Signal termination of the entire program
            logger.severe("Too many consumers have failed. Terminating");
            this.running = false;
        } else {
            logger.info("Remaining consumer count: " + remainingConsumersStillRunning);
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        Main m = new Main();
        try {
            m.parseArgs(args);
            m.run();
        } catch (Throwable t) {
            logger.log(Level.SEVERE, "terminating", t);
        } finally {
            // Any exit means something failed, so we call this an error so a container
            // environment can react accordingly
            System.exit(1);
        }
    }
}
