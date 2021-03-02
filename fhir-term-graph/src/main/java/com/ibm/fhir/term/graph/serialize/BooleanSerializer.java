/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.graph.serialize;

import org.janusgraph.core.attribute.AttributeSerializer;
import org.janusgraph.diskstorage.ScanBuffer;
import org.janusgraph.diskstorage.WriteBuffer;

import com.ibm.fhir.model.type.Boolean;

public class BooleanSerializer implements AttributeSerializer<Boolean> {
    @Override
    public Boolean read(ScanBuffer buffer) {
        return Boolean.of(buffer.getBoolean());
    }

    @Override
    public void write(WriteBuffer buffer, Boolean attribute) {
        buffer.putBoolean(attribute.getValue());
    }
}
