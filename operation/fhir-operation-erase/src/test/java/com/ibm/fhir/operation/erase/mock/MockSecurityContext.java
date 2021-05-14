/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.operation.erase.mock;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

/**
 * Mocks the java security context for testing of the rest layer
 */
public class MockSecurityContext implements SecurityContext {
    private String checkRole;

    public MockSecurityContext(String checkRole) {
        this.checkRole = checkRole;
    }

    @Override
    public Principal getUserPrincipal() {
        return null;
    }
    @Override
    public boolean isUserInRole(String role) {
        return checkRole.equals(role);
    }
    @Override
    public boolean isSecure() {
        return false;
    }
    @Override
    public String getAuthenticationScheme() {
        return null;
    }
}