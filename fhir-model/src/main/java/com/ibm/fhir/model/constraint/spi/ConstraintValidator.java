/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.constraint.spi;

import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Element;

/**
 * An interface for programmatically evaluating constraints against a validation target {@link Element} or {@link Resource}
 */
public interface ConstraintValidator {
    /**
     * Indicates whether an element is valid with respect to the given constraint
     *
     * @param element
     *     the element
     * @param constraint
     *     the constraint
     * @return
     *     true if the element is valid with respect to the given constraint, false otherwise
     */
    default boolean isValid(Element element, Constraint constraint) {
        return true;
    }

    /**
     * Indicates whether a resource is valid with respect to the given constraint
     *
     * @param resource
     *     the resource
     * @param constraint
     *     the constraint
     * @return
     *     true if the resource is valid with respect to the given constraint
     */
    default boolean isValid(Resource resource, Constraint constraint) {
        return true;
    }
}
