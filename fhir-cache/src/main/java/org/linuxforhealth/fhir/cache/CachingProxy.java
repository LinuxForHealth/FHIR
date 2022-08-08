/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.cache;

import static org.linuxforhealth.fhir.cache.CacheKey.key;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.linuxforhealth.fhir.cache.annotation.Cacheable;
import org.linuxforhealth.fhir.cache.util.CacheSupport;

/**
 * A Java dynamic proxy that caches method results based on the presence of a @Cacheable annotation
 */
public final class CachingProxy {
    private CachingProxy() { }

    /**
     * Create a new proxy instance that implements the provided interface and delegates to a caching
     * invocation handler.
     *
     * @param <T>
     *     the type of the proxy instance
     * @param interfaceClass
     *     the interface class
     * @param target
     *     the target
     * @return
     *     a new proxy instance that implements the provided interface
     */
    public static <T> T newInstance(Class<T> interfaceClass, T target) {
        Objects.requireNonNull(interfaceClass, "interfaceClass");
        Objects.requireNonNull(target, "target");
        return interfaceClass.cast(Proxy.newProxyInstance(
            interfaceClass.getClassLoader(),
            new Class<?>[] { interfaceClass },
            new CachingInvocationHandler(target)));
    }

    /**
     * Indicates whether the provided class has at least one method with the @Cacheable annotation
     *
     * @param targetClass
     *     the target class
     * @return
     *     true if the provided class has at least one method with the @Cacheable annotation, false otherwise
     */
    public static boolean hasCacheableMethod(Class<?> targetClass) {
        Objects.requireNonNull(targetClass, "targetClass");
        for (Method method : targetClass.getMethods()) {
            if (isCacheable(method)) {
                return true;
            }
        }
        return false;
    }

    /**
     * An interface for generating cache keys used by the caching invocation handler to cache method results
     */
    public interface KeyGenerator {
        /**
         * Default implementation uses the method and argument parameters to generate a cache key
         */
        static final KeyGenerator DEFAULT = new KeyGenerator() {
            @Override
            public CacheKey generate(Object target, Method method, Object[] args) {
                return key(method, args);
            }
        };

        /**
         * Generate a cache key for the provided target object, method, and arguments.
         *
         * @param target
         *     the target object
         * @param method
         *     the target method
         * @param args
         *     the arguments passed to the target method
         * @return
         *     a cache key instance
         */
        CacheKey generate(Object target, Method method, Object[] args);
    }

    private static boolean isCacheable(Method method) {
        return method.isAnnotationPresent(Cacheable.class);
    }

    private static class CachingInvocationHandler implements InvocationHandler {
        private static final Object NULL = new Object();

        private final Object target;
        private final Class<?> targetClass;
        private final Map<Method, Method> targetMethodCache = new ConcurrentHashMap<>();
        private final Map<Class<? extends KeyGenerator>, KeyGenerator> keyGeneratorCache = new ConcurrentHashMap<>();
        private final Map<Method, Map<CacheKey, Object>> resultCacheMap = new ConcurrentHashMap<>();

        public CachingInvocationHandler(Object target) {
            this.target = Objects.requireNonNull(target, "target");
            targetClass = target.getClass();
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            try {
                Method targetMethod = targetMethodCache.computeIfAbsent(method, k -> computeTargetMethod(method));
                if (isCacheable(targetMethod)) {
                    Cacheable cacheable = targetMethod.getAnnotation(Cacheable.class);

                    KeyGenerator keyGenerator = getKeyGenerator(cacheable.keyGeneratorClass());
                    CacheKey key = keyGenerator.generate(target, targetMethod, args);

                    Map<CacheKey, Object> resultCache = resultCacheMap.computeIfAbsent(targetMethod, k -> createCacheAsMap(cacheable));
                    Object result = resultCache.computeIfAbsent(key, k -> computeResult(targetMethod, args));

                    return (result != NULL) ? result : null;
                }
                return targetMethod.invoke(target, args);
            } catch (WrappedException e) {
                Exception unwrapped = unwrap(e);
                if (unwrapped instanceof InvocationTargetException && unwrapped.getCause() instanceof Exception) {
                    throw unwrapped.getCause();
                }
                throw unwrapped;
            } catch (InvocationTargetException e) {
                if (e.getCause() instanceof Exception) {
                    throw e.getCause();
                }
                throw e;
            }
        }

        private Method computeTargetMethod(Method method) {
            try {
                return targetClass.getMethod(method.getName(), method.getParameterTypes());
            } catch (Exception e) {
                throw wrap(e);
            }
        }

        private KeyGenerator getKeyGenerator(Class<? extends KeyGenerator> keyGeneratorClass) {
            if (KeyGenerator.class.equals(keyGeneratorClass)) {
                return KeyGenerator.DEFAULT;
            }
            return keyGeneratorCache.computeIfAbsent(keyGeneratorClass, k -> computeKeyGenerator(keyGeneratorClass));
        }

        private KeyGenerator computeKeyGenerator(Class<? extends KeyGenerator> keyGeneratorClass) {
            try {
                return keyGeneratorClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw wrap(e);
            }
        }

        private <K, V> Map<K, V> createCacheAsMap(Cacheable cacheable) {
            return CacheSupport.createCacheAsMap(cacheable.maximumSize(), Duration.of(cacheable.duration(), cacheable.unit()));
        }

        private Object computeResult(Method targetMethod, Object[] args) {
            try {
                Object result = targetMethod.invoke(target, args);
                return (result != null) ? result : NULL;
            } catch (Exception e) {
                throw wrap(e);
            }
        }

        private static class WrappedException extends RuntimeException {
            private static final long serialVersionUID = 1L;

            public WrappedException(Exception e) {
                super(e);
            }
        }

        private WrappedException wrap(Exception e) {
            return new WrappedException(e);
        }

        private Exception unwrap(WrappedException e) {
            return (Exception) e.getCause();
        }
    }
}
