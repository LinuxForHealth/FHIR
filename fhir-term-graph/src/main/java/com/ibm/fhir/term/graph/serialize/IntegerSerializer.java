/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.graph.serialize;

import org.janusgraph.core.attribute.AttributeSerializer;
import org.janusgraph.diskstorage.ScanBuffer;
import org.janusgraph.diskstorage.WriteBuffer;

import com.ibm.fhir.model.type.Integer;

public class IntegerSerializer implements AttributeSerializer<Integer> {
    @Override
    public Integer read(ScanBuffer buffer) {
        return Integer.of(buffer.getInt());
    }

    @Override
    public void write(WriteBuffer buffer, Integer attribute) {
        buffer.putInt(attribute.getValue());
    }
}
