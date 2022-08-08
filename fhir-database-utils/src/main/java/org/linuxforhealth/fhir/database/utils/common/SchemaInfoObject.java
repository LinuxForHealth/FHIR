/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.database.utils.common;


/**
 * A database object (table, index, view etc) existing within a schema
 */
public class SchemaInfoObject {
    public enum Type {
        FUNCTION,
        INDEX,
        PROCEDURE,
        SEQUENCE,
        TABLE,
        VIEW
    }

    // The object type
    private final Type type;
    
    // The object name
    private final String name;
    
    public SchemaInfoObject(Type type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public String toString() {
        return type.name() + ":" + name;
    }
    /**
     * @return the type
     */
    public Type getType() {
        return type;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
}