/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Repeatable(Constraints.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Constraint {
    static final String LEVEL_RULE = "Rule";
    static final String LEVEL_WARNING = "Warning";
    static final String LOCATION_BASE = "(base)";
    static final String SOURCE_UNKNOWN = "(unknown)";

    String id();
    String level();
    String location();
    String description();
    String expression();
    String source();
    boolean modelChecked() default false;
    boolean generated() default false;
}
