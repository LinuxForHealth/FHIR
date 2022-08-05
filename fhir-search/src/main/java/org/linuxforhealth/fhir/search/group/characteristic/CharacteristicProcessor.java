/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.search.group.characteristic;

import javax.ws.rs.core.MultivaluedMap;

import org.linuxforhealth.fhir.model.resource.Group;

/**
 * The Group.Charactersitic to QueryParameter string.
 */
public interface CharacteristicProcessor {
    /**
     * Converts the characteristic to a query parameter string.
     * It adds to an existing query string.
     *
     * @param characteristic the group characteristic that is passed in
     * @param target the resource target
     * @param queryParams the updated query parameters
     */
    public void process(Group.Characteristic characteristic, String target, MultivaluedMap<String, String> queryParams);
}