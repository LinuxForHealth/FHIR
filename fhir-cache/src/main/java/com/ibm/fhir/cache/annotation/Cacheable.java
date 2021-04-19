/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.cache.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.time.temporal.ChronoUnit;

import com.ibm.fhir.cache.CachingProxy;
import com.ibm.fhir.cache.CachingProxy.KeyGenerator;

/**
 * This annotation is applied to methods that may have their results cached using a Java dynamic proxy instance.
 *
 * <p>@see {@link CachingProxy}
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface Cacheable {
    /**
     * The maximum size of the result cache before entries are evicted
     *
     * @return
     *     the maximum size
     */
    int maximumSize() default 128;

    /**
     * The duration amount of the entries after they are written to the result cache
     *
     * @return
     *     the duration
     */
    int duration() default 1;

    /**
     * The duration unit of the entries after they are written to to the result cache
     *
     * @return
     *     the duration unit
     */
    ChronoUnit unit() default ChronoUnit.HOURS;

    /**
     * The class used to generate keys for entries in the result cache
     *
     * @return
     *     the key generator class
     */
    Class<? extends KeyGenerator> keyGeneratorClass() default KeyGenerator.class;
}
