/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.core.util;

import static com.ibm.fhir.core.util.CacheSupport.createCache;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import com.ibm.fhir.core.annotation.Cacheable;
import com.ibm.fhir.core.util.CacheKey.Generator;

public class CachingProxy {
    private static final Logger log = Logger.getLogger(CachingProxy.class.getName());

    public static <T> T newInstance(Class<T> interfaceClass, T target) {
        Objects.requireNonNull(interfaceClass, "interfaceClass");
        Objects.requireNonNull(target, "target");
        return interfaceClass.cast(Proxy.newProxyInstance(
            interfaceClass.getClassLoader(),
            new Class<?>[] { interfaceClass },
            new CachingInvocationHandler(target)));
    }

    public static boolean hasCacheableMethod(Class<?> targetClass) {
        Objects.requireNonNull(targetClass, "targetClass");
        for (Method method : targetClass.getMethods()) {
            if (isCacheable(method)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isCacheable(Method method) {
        return method.isAnnotationPresent(Cacheable.class);
    }

    private static class CachingInvocationHandler implements InvocationHandler {
        private static final Object NULL = new Object();

        private final Object target;
        private final Class<?> targetClass;
        private final Map<Method, Method> targetMethodCache = new ConcurrentHashMap<>();
        private final Map<Class<? extends CacheKey.Generator>, CacheKey.Generator> keyGeneratorCache = new ConcurrentHashMap<>();
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
                    Class<? extends CacheKey.Generator> keyGeneratorClass = cacheable.keyGeneratorClass();

                    CacheKey.Generator keyGenerator = getKeyGenerator(keyGeneratorClass, target, targetMethod, args);
                    CacheKey key = keyGenerator.generate(target, targetMethod, args);

                    Map<CacheKey, Object> resultCache = resultCacheMap.computeIfAbsent(targetMethod, k -> createCache(cacheable));
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

        private CacheKey.Generator getKeyGenerator(Class<? extends CacheKey.Generator> keyGeneratorClass, Object target, Method targetMethod, Object[] args) {
            if (CacheKey.Generator.class.equals(keyGeneratorClass)) {
                return CacheKey.Generator.DEFAULT;
            }
            return keyGeneratorCache.computeIfAbsent(keyGeneratorClass, k -> computeKeyGenerator(keyGeneratorClass));
        }

        private CacheKey.Generator computeKeyGenerator(Class<? extends Generator> keyGeneratorClass) {
            try {
                return keyGeneratorClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw wrap(e);
            }
        }

        private Object computeResult(Method targetMethod, Object[] args) {
            try {
                log.finest(() -> "Result cache miss for target method: " + targetMethod);
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
