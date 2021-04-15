/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.core.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

import com.ibm.fhir.core.util.CachingProxy.KeyGenerator;

@Retention(RUNTIME)
@Target(METHOD)
public @interface Cacheable {
    int maximumSize() default 128;
    int duration() default 1;
    TimeUnit unit() default TimeUnit.HOURS;
    Class<? extends KeyGenerator> keyGeneratorClass() default KeyGenerator.class;
}
