/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.smart.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import com.auth0.jwt.algorithms.Algorithm;
import com.ibm.fhir.smart.JWT;
import com.ibm.fhir.smart.JWT.DecodedJWT;

/**
 *
 */
public class JWTTest {
    private String testString = "value";
    private List<String> testList = Arrays.asList(new String[] {"value1", "value2"});

    private String jwt = com.auth0.jwt.JWT.create()
            .withClaim("string", testString)
            .withClaim("array", testList)
            .sign(Algorithm.none());

    @Test
    public void test() {
        DecodedJWT decodedJwt = JWT.decode(jwt);
        assertEquals(decodedJwt.getClaim("string").asString(), testString);
        assertEquals(decodedJwt.getClaim("array").asList(), testList);
        assertTrue(decodedJwt.getClaim("bogus").isNull());
    }
}
