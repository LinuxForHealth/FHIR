/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ibm.fhir.model.type.code.BindingStrength;

/**
 * Used to annotate coded elements that are bound to a value set.
 */
@Target({ ElementType.FIELD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Binding {
    /**
     * A name that can be used for code generation when generating named enumerations for the binding.
     *
     * @return the binding name
     */
    String bindingName() default "";

    /**
     * Indicates the degree of conformance expectations associated with this binding - that is, the degree to which the
     * provided value set must be adhered to in the instances.
     *
     * @return the binding strength
     */
    BindingStrength.Value strength();

    /**
     * Describes the intended use of this particular set of codes.
     *
     * @return the description
     */
    String description() default "";

    /**
     * Refers to the value set that identifies the set of codes the binding refers to.
     *
     * @return the value set
     */
    String valueSet() default "";

    /**
     * A reference to an extensible value set specified in a parent profile in order to allow a conformance checking tool
     * to validate that a code not in the extensible value set of the profile is not violating rules defined by parent
     * profile bindings.
     *
     * @return the inherited extensible value set
     */
    String inheritedExtensibleValueSet() default "";

    /**
     * The minimum allowable value set, for use when the binding strength is 'required' or 'extensible'. This value set
     * is the minimum value set that any conformant system SHALL support.
     *
     * @return the minimum allowable value set
     */
    String minValueSet() default "";

    /**
     * The maximum allowable value set, for use when the binding strength is 'extensible' or 'preferred'. This value
     * set is the value set from which additional codes can be taken from. This defines a 'required' binding over the
     * top of the extensible binding.
     *
     * @return the maximum allowable value set
     */
    String maxValueSet() default "";
}