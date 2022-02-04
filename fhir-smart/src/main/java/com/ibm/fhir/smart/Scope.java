/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.smart;

import java.util.Objects;

import com.ibm.fhir.core.ResourceType;

/**
 * @see <a href="http://www.hl7.org/fhir/smart-app-launch/scopes-and-launch-context/index.html">
 *      SMART App Launch: Scopes and Launch Context</a>
 */
public class Scope {
    public static final String SCOPE_STRING_REGEX = "(user|patient|system)/" + "([a-zA-Z]+|\\*)" + "\\." + "(read|write|\\*)";

    private final ContextType contextType;
    private final ResourceType resourceType;
    private final Permission permission;

    /**
     * @param scopeString a string of the form {@code (user|patient)/:resourceType.(read|write|*)}
     */
    public Scope(String scopeString) {
        Objects.requireNonNull(scopeString, "scope string");
        if (!scopeString.matches(SCOPE_STRING_REGEX)) {
            throw new IllegalArgumentException("Invalid scope string: '" + scopeString + "'");
        }

        String[] split1 = scopeString.split("/");
        this.contextType = ContextType.from(split1[0]);
        String[] split2 = split1[1].split("\\.");

        this.resourceType = split2[0].charAt(0) == '*' ?
                ResourceType.RESOURCE : ResourceType.from(split2[0]);

        this.permission = Permission.from(split2[1]);
    }

    /**
     * @param contextType
     * @param resourceType "Resource" for all resource types
     * @param permission
     */
    public Scope(ContextType contextType, ResourceType resourceType, Permission permission) {
        this.contextType = contextType;
        this.resourceType = resourceType;
        this.permission = permission;
    }

    /**
     * @return the contextType
     */
    public ContextType getContextType() {
        return contextType;
    }

    /**
     * @return the resourceType; "Resource" for all resource types
     */
    public ResourceType getResourceType() {
        return resourceType;
    }

    /**
     * @return the permission
     */
    public Permission getPermission() {
        return permission;
    }

    /**
     * @return a scopeString of the form {@code (user|patient|system)/:resourceType.(read|write|*)}
     */
    @Override
    public String toString() {
        String resourceTypeString =
                resourceType == ResourceType.RESOURCE ? "*" : resourceType.value();
        return contextType.value + "/" + resourceTypeString + "." + permission.value;
    }

    public static enum ContextType {
        PATIENT("patient"),
        USER("user"),
        SYSTEM("system");

        private final String value;

        /**
         * @param string
         */
        ContextType(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }

        public static ContextType from(String value) {
            for (ContextType c : ContextType.values()) {
                if (c.value.equals(value)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(value);
        }
    }

    public static enum Permission {
        READ("read"),
        WRITE("write"),
        ALL("*");

        private final String value;

        /**
         * @param string
         */
        Permission(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }

        public static Permission from(String value) {
            for (Permission p : Permission.values()) {
                if (p.value.equals(value)) {
                    return p;
                }
            }
            throw new IllegalArgumentException(value);
        }
    }
}
