/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.audit.logging.mapper;

import com.ibm.fhir.audit.logging.mapper.impl.CADFMapper;
import com.ibm.fhir.audit.logging.mapper.impl.FHIRAuditEventMapper;
import com.ibm.fhir.config.PropertyGroup;

/**
 *
 */
public class MapperFactory {

    /**
     * gets the appropriate mapper
     *
     * @param mt
     * @return
     */
    public static Mapper getMapper(MapperType mt) {
        switch (mt) {
        case CADF:
            return new CADFMapper();
        case FHIR:
            return new FHIRAuditEventMapper();
        default:
            throw new IllegalArgumentException("the mapper registed does not exist");
        }
    }

    /**
     * @param auditLogProperties
     * @return
     */
    public static MapperType getMapperType(PropertyGroup auditLogProperties) {
        // TODO Auto-generated method stub
        return null;
    }
}