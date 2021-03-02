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
import com.ibm.fhir.model.type.DateTime;

public class DateTimeSerializer implements AttributeSerializer<DateTime> {
    private final StringSerializer serializer;
    
    public DateTimeSerializer() {
        serializer = new StringSerializer();
    }

    @Override
    public DateTime read(ScanBuffer buffer) {
        return DateTime.of(serializer.read(buffer));
    }

    @Override
    public void write(WriteBuffer buffer, DateTime attribute) {
        serializer.write(buffer, DateTime.PARSER_FORMATTER.format(attribute.getValue()));
    }
}
