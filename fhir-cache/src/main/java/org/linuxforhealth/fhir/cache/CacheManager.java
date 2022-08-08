/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.cache;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import org.linuxforhealth.fhir.cache.util.CacheSupport;
import org.linuxforhealth.fhir.core.TenantIdProvider;

/**
 * A class used to create and manage cache instances on a per tenant basis
 */
public final class CacheManager {
    private static final Logger LOG = Logger.getLogger(CacheManager.class.getName());
    private static final Map<String, Map<String, Cache<?, ?>>> TENANT_CACHE_MAPS = new ConcurrentHashMap<>();
    private static final TenantIdProvider TENANT_ID_PROVIDER = TenantIdProvider.provider();

    private CacheManager() { }

    /**
     * Get the cache names for the current tenant.
     *
     * @return
     *     the cache names
     */
    public Set<String> getCacheNames() {
        String tenantId = TENANT_ID_PROVIDER.getTenantId();
        return TENANT_CACHE_MAPS.getOrDefault(tenantId, Collections.emptyMap()).keySet();
    }

    /**
     * Get the managed cache with the given name for the current tenant.
     *
     * @param <K>
     *     the key type
     * @param <V>
     *     the value type
     * @param cacheName
     *     the cache name
     * @return
     *     the managed cache instance with the given name, or null if not exists
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Cache<K, V> getCache(String cacheName) {
        Objects.requireNonNull(cacheName, "cacheName");
        String tenantId = TENANT_ID_PROVIDER.getTenantId();
        Map<String, Cache<?, ?>> tenantCacheMap = TENANT_CACHE_MAPS.getOrDefault(tenantId, Collections.emptyMap());
        return (Cache<K, V>) tenantCacheMap.get(cacheName);
    }

    /**
     * Get or create the managed cache with the given name for the current tenant.
     *
     * @param <K>
     *     the key type
     * @param <V>
     *     the value type
     * @param cacheName
     *     the cache name
     * @param configuration
     *     the configuration
     * @return
     *     a managed cache with the given name for the current tenant
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Cache<K, V> getCache(String cacheName, Configuration configuration) {
        Objects.requireNonNull(cacheName, "cacheName");
        Objects.requireNonNull(configuration, "configuration");
        String tenantId = TENANT_ID_PROVIDER.getTenantId();
        Map<String, Cache<?, ?>> tenantCacheMap = TENANT_CACHE_MAPS.computeIfAbsent(tenantId, k -> new ConcurrentHashMap<>());
        return (Cache<K, V>) tenantCacheMap.computeIfAbsent(cacheName, k -> createCache(configuration));
    }

    /**
     * Get the managed cache with the given name for the current tenant as a thread-safe map.
     *
     * @param <K>
     *     the key type
     * @param <V>
     *     the value type
     * @param cacheName
     *     the cache name
     * @return
     *     the managed cache instance with the given name as a thread-safe map, or null if not exists
     */
    public static <K, V> Map<K, V> getCacheAsMap(String cacheName) {
        Objects.requireNonNull(cacheName, "cacheName");
        Cache<K, V> cache = getCache(cacheName);
        return (cache != null) ? cache.asMap() : null;
    }

    /**
     * Get or create the managed cache with the given name for the current tenant as a thread-safe map.
     *
     * @param <K>
     *     the key type
     * @param <V>
     *     the value type
     * @param cacheName
     *     the cache name
     * @param configuration
     *     the configuration
     * @return
     *     a managed cache with the given name for the current tenant as a thread-safe map
     */
    public static <K, V> Map<K, V> getCacheAsMap(String cacheName, Configuration configuration) {
        Objects.requireNonNull(cacheName, "cacheName");
        Objects.requireNonNull(configuration, "configuration");
        Cache<K, V> cache = getCache(cacheName, configuration);
        return cache.asMap();
    }

    /**
     * Get a snapshot of the cumulative statistics for the cache with the given name.
     *
     * @param cacheName
     *     the cache name
     * @return
     *     a snapshot of the cumulative statistics for the cache with the given name, or null if not exists
     */
    public static <K, V> CacheStats getCacheStats(String cacheName) {
        Cache<K, V> cache = getCache(cacheName);
        return (cache != null) ? cache.stats() : null;
    }

    /**
     * Invalidate the entry with the provided key in the cache with the given name for the current tenant.
     *
     * @param cacheName
     *     the cache name
     * @param key
     *     the key
     */
    public static <K, V> void invalidate(String cacheName, K key) {
        Cache<K, V> cache = getCache(cacheName);
        if (cache != null) {
            cache.invalidate(key);
        }
    }

    /**
     * Invalidate all entries for the cache with the given name for the current tenant.
     *
     * @param cacheName
     *     the cache name
     */
    public static <K, V> void invalidateAll(String cacheName) {
        Cache<K, V> cache = getCache(cacheName);
        if (cache != null) {
            cache.invalidateAll();
        }
    }

    /**
     * Indicates whether a cache with the given name is managed for the current tenant.
     *
     * @param cacheName
     *     the cache name
     * @return
     *     true if a cache with the given name is managed for the current tenant, false otherwise
     */
    public static boolean isManaged(String cacheName) {
        if (cacheName != null) {
            String tenantId = TENANT_ID_PROVIDER.getTenantId();
            return TENANT_CACHE_MAPS.getOrDefault(tenantId, Collections.emptyMap()).containsKey(cacheName);
        }
        return false;
    }

    /**
     * Remove the cache with the given name from the cache manager for the current tenant.
     *
     * @param cacheName
     *     the cache name
     */
    public static void removeCache(String cacheName) {
        if (cacheName != null) {
            String tenantId = TENANT_ID_PROVIDER.getTenantId();
            Map<String, Cache<?, ?>> tenantCacheMap = TENANT_CACHE_MAPS.get(tenantId);
            if (tenantCacheMap != null) {
                tenantCacheMap.remove(cacheName);
            }
        }
    }

    /**
     * A configuration class used by the cache manager to create managed cache instances with size and/or time-based eviction policies
     */
    public static class Configuration {
        private final Integer maximumSize;
        private final Duration duration;

        private Configuration(int maximumSize) {
            this(maximumSize, null);
        }

        private Configuration(Duration duration) {
            this(null, duration);
        }

        private Configuration(Integer maximumSize, Duration duration) {
            this.maximumSize = maximumSize;
            this.duration = duration;
        }

        /**
         * The maximum size of the cache before entries are evicted
         *
         * @return
         *     the maximum size or null
         */
        public Integer getMaximumSize() {
            return maximumSize;
        }

        /**
         * The duration of entries after they are written to the cache
         *
         * @return
         *     the duration or null
         */
        public Duration getDuration() {
            return duration;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            Configuration other = (Configuration) obj;
            return Objects.equals(maximumSize, other.maximumSize) && Objects.equals(duration, other.duration);
        }

        @Override
        public int hashCode() {
            return Objects.hash(maximumSize, duration);
        }

        /**
         * A factory method for creating the configuration of a cache with a time-based eviction policy
         *
         * @param maximumSize
         *     the maximum size
         * @return
         *     a configuration instance
         */
        public static Configuration of(int maximumSize) {
            return new Configuration(maximumSize);
        }

        /**
         * A factory method for creating the configuration of a cache with both size and time-based eviction policies
         *
         * @param maximumSize
         *     the maximum size
         * @param duration
         *     the duration
         * @return
         *     a configuration instance
         */
        public static Configuration of(int maximumSize, Duration duration) {
            Objects.requireNonNull(duration, "duration");
            return new Configuration(maximumSize, duration);
        }

        /**
         * A factory method for creating the configuration of a cache with a time-based eviction policy
         *
         * @param duration
         *     the duration
         * @return
         *     a configuration instance
         */
        public static Configuration of(Duration duration) {
            Objects.requireNonNull(duration, "duration");
            return new Configuration(duration);
        }
    }

    private static <K, V> Cache<K, V> createCache(Configuration configuration) {
        if (Objects.nonNull(configuration.getMaximumSize()) && Objects.nonNull(configuration.getDuration())) {
            return CacheSupport.createCache(configuration.getMaximumSize(), configuration.getDuration(), true);
        }
        if (Objects.nonNull(configuration.getMaximumSize())) {
            return CacheSupport.createCache(configuration.getMaximumSize(), true);
        }
        return CacheSupport.createCache(configuration.getDuration(), true);
    }

    /**
     * Reports the cache statistics in the caffeine cache.
     * @param cacheClientLogger used to report the statistics
     * @param cacheName the name of the cache to check
     */
    public static void reportCacheStats(Logger cacheClientLogger, String cacheName) {
        Objects.requireNonNull(cacheClientLogger, "The logger must exist");
        Objects.requireNonNull(cacheName, "The cacheName must exist");
        if (LOG.isLoggable(Level.FINE) && cacheClientLogger.isLoggable(Level.FINEST)) {
            CacheStats stats = CacheManager.getCacheStats(cacheName);
            if (stats == null) {
                cacheClientLogger.warning("No cachestats for " + cacheName);
            } else {
                cacheClientLogger.fine("CacheStats for '" + cacheName + "' "
                        + "averageLoadPenalty=[" + stats.averageLoadPenalty() + "],"
                        + "evictionCount=[" + stats.evictionCount() + "],"
                        + "hitCount=[" + stats.hitCount() + "],"
                        + "hitRate=[" + stats.hitRate() + "],"
                        + "loadCount=[" + stats.loadCount() + "],"
                        + "missCount=[" + stats.missCount() + "],"
                        + "requestCount=[" + stats.requestCount() + "],"
                        + "missRate=[" + stats.missRate() + "],"
                        + "loadFailureRate=[" + stats.loadFailureRate() + "]");

            }
        }
    }
}
