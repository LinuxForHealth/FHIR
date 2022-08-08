/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.profile.constraint.spi;

import org.linuxforhealth.fhir.model.constraint.spi.ConstraintProvider;

/**
 * An interface that extends {@link ConstraintProvider} with a method that determines whether this constraint provider applies to a specific profile
 */
public interface ProfileConstraintProvider extends ConstraintProvider {
    /**
     * Indicates whether this constraint provider applies to a profile with the given url and version
     *
     * @param url
     *     the url
     * @param version
     *     the version
     * @return
     *     true if this constraint provider applies to a profile with the given url and version, false otherwise
     */
    boolean appliesTo(String url, String version);
}
