/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.bucket.scanner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import com.ibm.fhir.bucket.api.CosItem;
import com.ibm.fhir.bucket.api.FileType;
import com.ibm.fhir.bucket.cos.CosClient;

/**
 * Active object to periodically scan COS buckets looking for new
 * objects to load
 */
public class CosScanner {
    private static final Logger logger = Logger.getLogger(CosScanner.class.getName());

    // COS connection
    private final CosClient client;
    
    // the list of buckets to scan
    private final List<String> buckets;
    
    // main thread control flag
    private volatile boolean running = true;
    
    // Scan COS every 60 seconds by default
    private long scanIntervalMs = 60000;

    // active object thread
    private Thread mainLoopThread;
    
    // Access to our data layer for persistence
    private final DataAccess dataAccess;
    
    // Only process files matching these types
    private final Set<FileType> fileTypes;

    // optional prefix to narrow the scan inside the bucket
    private final String pathPrefix;
    
    // time tracker for regular heartbeats
    public static final long HEARTBEAT_INTERVAL_MS = 5000;
    private long lastHeartbeatTime = -1;
    
    // number of nanos per ms
    private static final long NANO_MS = 1000000;

    /**
     * Public constructor
     * @param client
     * @param buckets
     */
    public CosScanner(CosClient client, Collection<String> buckets, DataAccess dataAccess, Set<FileType> fileTypes, String pathPrefix) {
        this.client = client;
        this.buckets = new ArrayList<>(buckets);
        this.dataAccess = dataAccess;
        this.fileTypes = fileTypes;
        this.pathPrefix = pathPrefix;
    }
    
    /**
     * Run the scanner thread
     */
    public void init() {
        mainLoopThread = new Thread(new Runnable() {
            
            @Override
            public void run() {
                mainLoop();
            }
            
        });
        mainLoopThread.start();
    }

    /**
     * Tell the main loop thread to stop
     */
    public void stop() {
        logger.info("Stopping CosScanner");
        this.running = false;
        
        if (mainLoopThread != null) {
            this.mainLoopThread.interrupt();
            try {
                // give it a few seconds to respond
                mainLoopThread.join(5000);
            } catch (InterruptedException x) {
                logger.warning("Main loop thread did not terminate in 5000ms");
            }
        }
        logger.info("CosScanner stopped");
    }

    /**
     * The main loop running inside this active object's thread
     */
    public void mainLoop() {
        long nextScanTime = -1;
        
        while (this.running) {
            long start = System.nanoTime();
            heartbeat();
            
            if (nextScanTime == -1 || start >= nextScanTime) {
                scan();
                
                double elapsed = (System.nanoTime() - start) / 1e9;
                logger.info(String.format("Scan complete [took %4.1f s]", elapsed));
                
                // roughly schedule the next scan
                nextScanTime = start + scanIntervalMs * NANO_MS;
            }
            
            // Heartbeat is supposed to be a fraction of the scan interval (e.g. 5s vs. 30s)
            // so we don't need to bother with calculating different sleep schedules to wake
            // up exactly on time for the next heartbeat/scan.
            safeSleep(HEARTBEAT_INTERVAL_MS);
        }
    }

    /**
     * Sleep this thread for the given milliseconds
     * @param millis
     */
    protected void safeSleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException x) {
            // NOP
        }
    }
    
    /**
     * Perform a scan for each of the configured buckets
     */
    protected void scan() {
        for (String bucket: this.buckets) {
            client.scan(bucket, this.pathPrefix, CosScanner::fileTyper, ci -> handle(ci));
        }
    }
    
    /**
     * Determine the type of the file based on the suffix
     * @param itemName
     * @return
     */
    protected static FileType fileTyper(String itemName) {
        if (itemName.endsWith(".ndjson") || itemName.endsWith(".NDJSON")) {
            return FileType.NDJSON;
        } else if (itemName.endsWith(".json") || itemName.endsWith(".JSON")) {
            return FileType.JSON;
        } else {
            return FileType.UNKNOWN;
        }
    }
    
    /**
     * Process the item returned by the scan
     * @param item
     */
    protected void handle(CosItem item) {
        // Only process items we recognize and want
        if (fileTypes.contains(item.getFileType())) {
            dataAccess.registerBucketItem(item);
        }
        
        // keep the heartbeat going within the scan just in case a scan
        // takes a really long time
        heartbeat();
    }

    /**
     * Update the heartbeat on a (reasonably) regular basis to
     * demonstrate this loader instance is still alive
     */
    protected void heartbeat() {
        long now = System.nanoTime();
        long gap = (now - this.lastHeartbeatTime) / NANO_MS;
        if (this.lastHeartbeatTime < 0 || gap > HEARTBEAT_INTERVAL_MS) {
            this.lastHeartbeatTime = now;
            dataAccess.heartbeat();
        }
    }
}
