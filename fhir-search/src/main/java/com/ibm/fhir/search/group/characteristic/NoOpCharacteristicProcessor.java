/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.group.characteristic;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.MultivaluedMap;

import com.ibm.fhir.model.resource.Group.Characteristic;

/**
 * Nothing is added to the QueryParams
 */
public class NoOpCharacteristicProcessor implements CharacteristicProcessor {
    private static final Logger logger = Logger.getLogger(NoOpCharacteristicProcessor.class.getName());

    @Override
    public void process(Characteristic characteristic, String target, MultivaluedMap<String, String> queryParams) {
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Activated the NullCharacteristicProcessor");
        }
    }
}