/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.core.trace;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)

/**
 * Custom trace annotation to support additional instrumentation for tools such
 * as newrelic.
 * @implNote https://docs.newrelic.com/docs/apm/agents/java-agent/api-guides/java-agent-api-instrument-using-annotation/
 */
public @interface Trace {
    public static final String NULL = "";
    String metricName() default NULL;
    boolean dispatcher() default false;
    String tracerFactoryName() default NULL;
}