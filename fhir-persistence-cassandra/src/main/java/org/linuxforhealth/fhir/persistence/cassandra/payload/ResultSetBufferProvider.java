/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.persistence.cassandra.payload;

import java.nio.ByteBuffer;

import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;

/**
 * Provides buffers read from a CQL query result set
 */
public class ResultSetBufferProvider implements IBufferProvider {

    private final ResultSet resultSet;
    private final int columnIndex;
    
    /**
     * Public constructor
     * @param rs
     * @param columnIndex
     */
    public ResultSetBufferProvider(ResultSet rs, int columnIndex) {
        this.resultSet = rs;
        this.columnIndex = columnIndex;
    }

    @Override
    public ByteBuffer nextBuffer() {
        Row row = this.resultSet.one();
        if (row != null) {
            return row.getByteBuffer(columnIndex);
        } else {
            return null;
        }
    }
}