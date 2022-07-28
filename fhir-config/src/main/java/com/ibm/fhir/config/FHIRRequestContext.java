/*
 * (C) Copyright IBM Corp. 2017, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ibm.fhir.core.HTTPHandlingPreference;
import com.ibm.fhir.core.HTTPReturnPreference;
import com.ibm.fhir.exception.FHIRException;

/**
 * This class is used to hold FHIR REST API context information.
 * There are non-static methods for maintaining individual instances, as
 * well as static methods for setting instances on and getting instances from thread local.
 * The overall strategy is for the REST API servlet filter to retrieve the request context
 * information, create an instance of this class and set it on the current thread for use
 * by the FHIR Server as it processes the request.
 */
public class FHIRRequestContext {
    private static final Logger log = Logger.getLogger(FHIRRequestContext.class.getName());

    // The tenantId. Corresponding tenantKey is retrieved from the configuration on demand
    private String tenantId;

    // The datastore to be used for this request. Usually "default"
    private String dataStoreId;

    // An optional shard key passed with the request for use with distributed schemas
    private String requestShardKey;

    private String requestUniqueId;
    private String originalRequestUri;
    private Map<String, List<String>> httpHeaders;

    // Set to true if the REST layer determines the entire request only needs to read from a persistence layer, not write to it
    private boolean readOnly;

    // Set to false automatically, and override when appropriate.
    private boolean bulk = false;

    // Default to the "strict" handling which means the server will reject unrecognized search parameters and elements
    private HTTPHandlingPreference handlingPreference = HTTPHandlingPreference.STRICT;

    // Default to the "minimal" representation which means create/update responses won't return the resource body
    private HTTPReturnPreference returnPreference = HTTPReturnPreference.MINIMAL;

    // True if the returnPreference was not passed and is using the default value
    private boolean returnPreferenceDefault;

    private Map<String, Object> operationProperties = new HashMap<>();

    private Pattern validChars = Pattern.compile("[a-zA-Z0-9_\\-]+");
    private String errorMsg = "Only [a-z], [A-Z], [0-9], '_', and '-' characters are allowed.";

    // Map of all metrics currently collected (across multiple request threads)
    private static final Map<String, CallTimeMetric> metricMap = new ConcurrentHashMap<>();

    // the current metric handle on this request thread
    private MetricHandle currentMetricHandle;

    private static ThreadLocal<FHIRRequestContext> contexts = new ThreadLocal<FHIRRequestContext>() {
        @Override
        public FHIRRequestContext initialValue() {
            try {
                return new FHIRRequestContext(FHIRConfiguration.DEFAULT_TENANT_ID, FHIRConfiguration.DEFAULT_DATASTORE_ID);
            } catch (FHIRException e) {
                throw new IllegalStateException("Unexpected error while initializing FHIRRequestContext");
            }
        }
    };

    public FHIRRequestContext() {
        this.requestUniqueId = UUID.randomUUID().toString();
    }

    public FHIRRequestContext(String tenantId) throws FHIRException {
        this();
        setTenantId(tenantId);
    }

    public FHIRRequestContext(String tenantId, String dataStoreId) throws FHIRException {
        this(tenantId);
        setDataStoreId(dataStoreId);
    }

    public String getTenantId() {
        return tenantId;
    }

    /**
     * Returns true iff the entire request can be processed using only reads in the persistence layer
     * Permits persistence layer implementations to leverage read-only replicas if they are available
     * @return
     */
    public boolean isReadOnly() {
        return this.readOnly;
    }

    /**
     * Setter for the readOnly flag
     * @param flag
     */
    public void setReadOnly(boolean flag) {
        this.readOnly = flag;
    }

    /**
     * the status of the FHIR request
     * @return the status of the FHIRRequest indicating bulk
     */
    public boolean isBulk() {
        return this.bulk;
    }

    /**
     * set the bulk status
     * @param bulk
     */
    public void setBulk(boolean bulk) {
        this.bulk = bulk;
    }

    /**
     * @param tenantId An alphanumeric string (optionally with '-' or '_' chars); not null
     * @throws FHIRException
     */
    public void setTenantId(String tenantId) throws FHIRException {
        Matcher matcher = validChars.matcher(tenantId);
        if (matcher.matches()) {
            this.tenantId = tenantId;
        } else {
            throw new FHIRException("Invalid tenantId. " + errorMsg);
        }
    }

    public String getDataStoreId() {
        return dataStoreId;
    }

    public void setDataStoreId(String dataStoreId) throws FHIRException {
        Matcher matcher = validChars.matcher(dataStoreId);
        if (matcher.matches()) {
            this.dataStoreId = dataStoreId;
        } else {
            throw new FHIRException("Invalid dataStoreId. " + errorMsg);
        }
    }

    /**
     * Set the shard key string value provided by the request
     * @param k
     */
    public void setRequestShardKey(String k) {
        this.requestShardKey = k;
    }

    /**
     * Get the shard key string value provided by the request. This value is
     * not filtered in any way because the value eventually gets hashed into
     * a short (2 byte integer number) before being used.
     * @return
     */
    public String getRequestShardKey() {
        return this.requestShardKey;
    }

    /**
     * set an Operation Context property
     * @param name
     * @param value
     */
    public void setExtendedOperationProperties(String name, Object value) {
        operationProperties.put(name, value);
    }

    /**
     * get an extended Operation Context property
     * @param name
     * @return
     */
    public Object getExtendedOperationProperties(String name) {
        return operationProperties.get(name);
    }

    /**
     * Sets the specified FHIRRequestContext instance on the current thread,
     * so that it can be used by FHIR Server downstream processing.
     * This method is called when the FHIR Server starts processing a request.
     */
    public static void set(FHIRRequestContext context) {
        Objects.requireNonNull(context);
        contexts.set(context);
        if (log.isLoggable(Level.FINEST)) {
            log.finest("FHIRRequestContext.set: " + context.toString());
        }
    }

    /**
     * Returns the FHIRRequestContext on the current thread.
     * If it doesn't exist yet, this method will create a default instance
     * and associate that with the current thread before returning it.
     */
    public static FHIRRequestContext get() {
        FHIRRequestContext result = contexts.get();
        if (log.isLoggable(Level.FINEST)) {
            log.finest("FHIRRequestContext.get: " + result.toString());
        }
        return result;
    }

    /**
     * Removes the FHIRRequestContext that's set on the current thread.
     * This method is called when the FHIR Server is finished processing a request.
     */
    public static void remove() {
        contexts.remove();
        log.finest("FHIRRequestContext.remove invoked.");
    }

    @Override
    public String toString() {
        return "FHIRRequestContext [tenantId=" + tenantId + ", dataStoreId=" + dataStoreId
                + ", this="+ objectHandle(this) + "]";
    }

    public String getRequestUniqueId() {
        return this.requestUniqueId;
    }

    private static String objectHandle(Object obj) {
        return '@' + Integer.toHexString(System.identityHashCode(obj));
    }

    /**
     * @return the handlingPreference
     */
    public HTTPHandlingPreference getHandlingPreference() {
        return handlingPreference;
    }

    /**
     * @param handlingPreference the handlingPreference to set
     */
    public void setHandlingPreference(HTTPHandlingPreference handlingPreference) {
        this.handlingPreference = handlingPreference;
    }

    /**
     * @return the returnPreference
     */
    public HTTPReturnPreference getReturnPreference() {
        return returnPreference;
    }

    /**
     * @param returnPreference the returnPreference to set
     */
    public void setReturnPreference(HTTPReturnPreference returnPreference) {
        this.returnPreference = returnPreference;
    }

    /**
     * @return the originalRequestUri
     */
    public String getOriginalRequestUri() {
        return originalRequestUri;
    }

    /**
     * @param originalRequestUri the originalRequestUri to set
     */
    public void setOriginalRequestUri(String originalRequestUri) {
        this.originalRequestUri = originalRequestUri;
    }

    /**
     * @return the httpHeaders
     */
    public Map<String, List<String>> getHttpHeaders() {
        return httpHeaders;
    }

    /**
     * @param httpHeaders the httpHeaders to set
     */
    public void setHttpHeaders(Map<String, List<String>> httpHeaders) {
        this.httpHeaders = httpHeaders;
    }

    /**
     * @return the returnPreferenceIsDefault
     */
    public boolean isReturnPreferenceDefault() {
        return returnPreferenceDefault;
    }

    /**
     * @param returnPreferenceIsDefault the returnPreferenceIsDefault to set
     */
    public void setReturnPreferenceDefault(boolean returnPreferenceIsDefault) {
        this.returnPreferenceDefault = returnPreferenceIsDefault;
    }

    /**
     * Get a handle to track a new instance of a call. Metric names must not include the
     * forward slash '/' character because it is used as a path separator
     * @param name
     * @return
     */
    public MetricHandle getMetricHandle(String name) {
        if (name.contains("/")) {
            throw new IllegalArgumentException("Metric names MUST NOT include the metric path separator '/'");
        }

        if (log.isLoggable(Level.FINEST)) {
            if (currentMetricHandle != null) {
                log.finest("getMetricHandle: '" + name + "', currentMetric is '" + currentMetricHandle.getPath() + "'");
            } else {
                log.finest("getMetricHandle: '" + name + "', currentMetric is '/'");
            }
        }

        final String parentPath = (currentMetricHandle == null) ? "/" : currentMetricHandle.getPath();
        final String fullMetricName = parentPath + name;

        // See if this fullMetricName already exists, if not create a new instance
        CallTimeMetric metric = metricMap.computeIfAbsent(fullMetricName, k -> new CallTimeMetric(fullMetricName, name));
        this.currentMetricHandle = new MetricHandle(this, metric, currentMetricHandle);
        return this.currentMetricHandle;
    }

    /**
     * Callback to indicate that the current MetricHandle has closed, so we should set the currentMetric
     * to be its parent (which may be null if the current metric is a root-level metric).
     * @param mh the metric handle being closed
     * @throws IllegalStateException if the given metric does not match the currentMetric value, indicating a programming error
     */
    protected void endMetric(MetricHandle mh) {
        if (log.isLoggable(Level.FINEST)) {
            log.finest("endMetric: '" + mh.getPath() + "', currentMetric is '" + currentMetricHandle.getPath() + "'");
        }

        if (mh == this.currentMetricHandle) {
            this.currentMetricHandle = mh.getParent();
        } else if (currentMetricHandle == null) {
            // programming error
            throw new IllegalStateException("Metric being closed is '" + mh.getPath() + "' but currentMetric is not set");
        } else {
            // programming error
            throw new IllegalStateException("Metric being closed is '" + mh.getPath() + "' but currentMetric is '" + currentMetricHandle.getPath() + "'");
        }
    }

    /**
     * Get a snapshot of the current collection of metric values and reset them.
     * @implNote this isn't atomic, so there's a very small chance a metric could
     * be reported after the result snapshot is calculated but before the clear()
     * call. But this tiny error is well worth the benefit of avoiding synchronization
     * here.
     * @return
     */
    public static List<CallTimeMetric> getAndResetMetrics() {
        List<CallTimeMetric> result = new ArrayList<CallTimeMetric>(metricMap.values());
        metricMap.clear();
        result.sort((a,b) -> a.getFullMetricName().compareTo(b.getFullMetricName()));
        return result;
    }
}