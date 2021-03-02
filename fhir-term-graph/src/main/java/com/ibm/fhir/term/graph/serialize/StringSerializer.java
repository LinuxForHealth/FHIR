/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.graph.serialize;

import static com.ibm.fhir.model.type.String.string;

import org.janusgraph.core.attribute.AttributeSerializer;
import org.janusgraph.diskstorage.ScanBuffer;
import org.janusgraph.diskstorage.WriteBuffer;

import com.ibm.fhir.model.type.String;

public class StringSerializer implements AttributeSerializer<String> {
    private final org.janusgraph.graphdb.database.serialize.attribute.StringSerializer serializer;

    public StringSerializer() {
        serializer = new org.janusgraph.graphdb.database.serialize.attribute.StringSerializer();
    }

    @Override
    public String read(ScanBuffer buffer) {
        return string(serializer.read(buffer));
    }

    @Override
    public void write(WriteBuffer buffer, String attribute) {
        serializer.write(buffer, attribute.getValue());
    }
}
