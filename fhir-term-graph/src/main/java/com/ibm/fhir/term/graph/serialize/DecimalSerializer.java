/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.graph.serialize;

import org.janusgraph.core.attribute.AttributeSerializer;
import org.janusgraph.diskstorage.ScanBuffer;
import org.janusgraph.diskstorage.WriteBuffer;

import com.ibm.fhir.model.type.Decimal;

public class DecimalSerializer implements AttributeSerializer<Decimal> {
    @Override
    public Decimal read(ScanBuffer buffer) {
        return Decimal.of(buffer.getDouble());
    }

    @Override
    public void write(WriteBuffer buffer, Decimal attribute) {
        buffer.putDouble(attribute.getValue().doubleValue());
    }
}
