/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.smart.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import com.auth0.jwt.algorithms.Algorithm;
import com.ibm.fhir.smart.JWT;
import com.ibm.fhir.smart.JWT.DecodedJWT;

/**
 * Test for decoding JWTs and getting claims
 */
public class JWTTest {
    private String testString = "value";
    private List<String> testList = Arrays.asList(new String[] {"value1", "value2"});

    private String jwtNoSig = com.auth0.jwt.JWT.create()
            .withClaim("string", testString)
            .withClaim("array", testList)
            .withClaim("null", (Boolean)null)
            .sign(Algorithm.none());

    @Test
    public void testJWTnoSig() {
        DecodedJWT decodedJwt = JWT.decode(jwtNoSig);
        assertEquals(decodedJwt.getClaim("string").asString(), testString);
        assertEquals(decodedJwt.getClaim("string").asList(), null);
        assertEquals(decodedJwt.getClaim("array").asList(), testList);
        assertEquals(decodedJwt.getClaim("array").asString(), null);
        assertEquals(decodedJwt.getClaim("bogus").asString(), null);
        assertEquals(decodedJwt.getClaim("bogus").asList(), null);

        assertTrue(decodedJwt.getClaim("missing").isMissing());
        assertTrue(decodedJwt.getClaim("null").isNull());
        assertFalse(decodedJwt.getClaim("missing").isNull());
        assertFalse(decodedJwt.getClaim("null").isMissing());
    }

    @Test
    public void testJWTwithSig() throws NoSuchAlgorithmException {
        KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
        KeyPair keypair = keygen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keypair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keypair.getPrivate();

        String jwtWithSig = com.auth0.jwt.JWT.create()
                .withClaim("string", testString)
                .withClaim("array", testList)
                .withClaim("null", (Boolean)null)
                .sign(Algorithm.RSA256(publicKey, privateKey));

        DecodedJWT decodedJwt = JWT.decode(jwtWithSig);
        assertEquals(decodedJwt.getClaim("string").asString(), testString);
        assertEquals(decodedJwt.getClaim("string").asList(), null);
        assertEquals(decodedJwt.getClaim("array").asList(), testList);
        assertEquals(decodedJwt.getClaim("array").asString(), null);
        assertEquals(decodedJwt.getClaim("bogus").asString(), null);
        assertEquals(decodedJwt.getClaim("bogus").asList(), null);

        assertTrue(decodedJwt.getClaim("missing").isMissing());
        assertTrue(decodedJwt.getClaim("null").isNull());
        assertFalse(decodedJwt.getClaim("missing").isNull());
        assertFalse(decodedJwt.getClaim("null").isMissing());
    }

    /**
     * Utility for printing a JWT to sysout
     */
    public static void main(String[] args) {
        System.out.println(com.auth0.jwt.JWT.create()
                .withClaim("scope", "patient/*.*")
                .withClaim("patient_id", "Patient1")
                .sign(Algorithm.none()));
    }
}
