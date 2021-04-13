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

import com.ibm.fhir.core.util.CacheKey;

@Retention(RUNTIME)
@Target(METHOD)
public @interface Cacheable {
    static int DEFAULT_MAX_ENTRIES = 128;
    int maxEntries() default DEFAULT_MAX_ENTRIES;
    Class<? extends CacheKey.Generator> keyGeneratorClass() default CacheKey.Generator.class;
}
