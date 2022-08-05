/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.audit.mapper;

import java.util.Objects;

import org.linuxforhealth.fhir.audit.mapper.impl.AuditEventMapper;
import org.linuxforhealth.fhir.audit.mapper.impl.CADFMapper;

/**
 * Mapper Factory selects the desired mapper and returns a new instance
 * for each call.
 */
public final class MapperFactory {

    private MapperFactory() {
        // No Operation
    }

    /**
     * gets the appropriate mapper
     *
     * @param mt
     * @return
     * @throws IllegalArgumentException
     */
    public static Mapper getMapper(MapperType mt) {
        if (Objects.isNull(mt)) {
            throw new IllegalArgumentException("Expected non-null MapperType");
        }
        switch (mt) {
        case AUDITEVENT:
            return new AuditEventMapper();
        default:
            // The default is CADF
            return new CADFMapper();
        }
    }
}