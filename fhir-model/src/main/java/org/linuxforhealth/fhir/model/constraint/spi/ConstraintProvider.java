/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.constraint.spi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ServiceLoader;
import java.util.function.Predicate;

import org.linuxforhealth.fhir.model.annotation.Constraint;

/**
 * An interface used to remove and/or add to the constraints used in a specific context
 */
public interface ConstraintProvider {
    /**
     * Get the list of removal predicates for this constraint provider.
     *
     * @return
     *     the list of removal predicates for this constraint provider
     */
    List<Predicate<Constraint>> getRemovalPredicates();

    /**
     * Get the list of constraints for this constraint provider.
     *
     * @return
     *     the list of constraints for this constraint provider
     */
    List<Constraint> getConstraints();

    /**
     * Get the list of constraint provider instances of the given type from the ServiceLoader.
     *
     * @param <T>
     *     the type of provider
     * @param providerClass
     *     the provider class
     * @return
     *     the list of constraint provider instances of the given type from the ServiceLoader
     */
    static <T extends ConstraintProvider> List<T> providers(Class<T> providerClass) {
        List<T> providers = new ArrayList<>();
        for (T provider : ServiceLoader.load(providerClass)) {
            providers.add(provider);
        }
        return Collections.unmodifiableList(providers);
    }
}
