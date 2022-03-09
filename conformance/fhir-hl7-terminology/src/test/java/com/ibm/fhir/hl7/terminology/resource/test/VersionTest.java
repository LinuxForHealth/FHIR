/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.hl7.terminology.resource.test;

import static com.ibm.fhir.registry.resource.FHIRRegistryResource.Version;

import org.testng.Assert;
import org.testng.annotations.Test;

public class VersionTest {
    @Test
    public void testVersion1() {
        Version v = Version.from("1.2.3");
        Assert.assertEquals(v.major(), 1);
        Assert.assertEquals(v.minor(), 2);
        Assert.assertEquals(v.patch(), 3);
        Assert.assertEquals(v.toString(), "1.2.3");
    }
    
    @Test
    public void testVersion2() {
        Version v = Version.from("1.2");
        Assert.assertEquals(v.major(), 1);
        Assert.assertEquals(v.minor(), 2);
        Assert.assertEquals(v.toString(), "1.2");
    }
    
    @Test
    public void testVersion3() {
        Version v = Version.from("1");
        Assert.assertEquals(v.major(), 1);
        Assert.assertEquals(v.toString(), "1");
    }
    
    @Test
    public void testVersion4() {
        Assert.assertEquals(Version.from("1.0.0"), Version.from("1.0"));
    }
    
    @Test
    public void testVersion5() {
        Assert.assertTrue(lessThan(Version.from("1.0"), Version.from("2.0")));
        Assert.assertTrue(lessThan(Version.from("1.0"), Version.from("10.0")));
        Assert.assertTrue(lessThan(Version.from("1.0.1"), Version.from("1.0.2")));
    }
    
    @Test
    public void testVersion6() {
        Assert.assertTrue(greaterThan(Version.from("1.0.1"), Version.from("1.0.0")));
        Assert.assertTrue(greaterThan(Version.from("40.0.1"), Version.from("10.0")));
        Assert.assertTrue(greaterThan(Version.from("3"), Version.from("2")));
    }
    
    public boolean greaterThan(Version v1, Version v2) {
        return v1.compareTo(v2) > 0;
    }
    
    public boolean lessThan(Version v1, Version v2) {
        return v1.compareTo(v2) < 0;
    }
}