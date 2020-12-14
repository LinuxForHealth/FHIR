/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.audit.mapper;

import static org.junit.Assert.assertNotNull;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.ibm.fhir.audit.mapper.Mapper;
import com.ibm.fhir.audit.mapper.MapperFactory;
import com.ibm.fhir.audit.mapper.MapperType;

/**
 * Mapper Related Tests for MapperType, MapperFactory
 */
public class MapperTest {

    @Test
    public void testMapperTypeCADF() {
        MapperType mt = MapperType.from("cadf");
        assertNotNull(mt);
        assertEquals(mt.name(),"CADF");
    }

    @Test
    public void testMapperTypeAuditEvent() {
        MapperType mt = MapperType.from("auditevent");
        assertNotNull(mt);
        assertEquals(mt.name(),"AUDITEVENT");
    }

    @Test(expectedExceptions= {IllegalArgumentException.class})
    public void testMapperTypeNoMatch() {
        MapperType.from("NOMATCH");
    }

    @Test
    public void testMapperFactoryAuditEvent() {
        Mapper mapper = MapperFactory.getMapper(MapperType.AUDITEVENT);
        assertNotNull(mapper);
    }

    @Test
    public void testMapperFactoryCADF() {
        Mapper mapper = MapperFactory.getMapper(MapperType.CADF);
        assertNotNull(mapper);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testMapperFactoryNull() {
        MapperFactory.getMapper(null);
    }
}
