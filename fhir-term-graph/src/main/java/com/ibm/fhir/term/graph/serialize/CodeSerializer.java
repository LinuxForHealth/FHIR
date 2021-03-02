/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.graph.serialize;

import org.janusgraph.core.attribute.AttributeSerializer;
import org.janusgraph.diskstorage.ScanBuffer;
import org.janusgraph.diskstorage.WriteBuffer;
import org.janusgraph.graphdb.database.serialize.attribute.StringSerializer;

import com.ibm.fhir.model.type.Code;

public class CodeSerializer implements AttributeSerializer<Code> {
    private final StringSerializer serializer;

    public CodeSerializer() {
        serializer = new StringSerializer();
    }

    @Override
    public Code read(ScanBuffer buffer) {
        return Code.of(serializer.read(buffer));
    }

    @Override
    public void write(WriteBuffer buffer, Code attribute) {
        serializer.write(buffer, attribute.getValue());
    }
}
