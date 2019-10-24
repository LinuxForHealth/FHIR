/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.streams;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Simple implementation taking a JDBC ResultSet and rendering it using
 * the Java 8 Streams API.
 * <br>
 * The JDBC API is really starting to show its age here
 */
public class Streamer {
    private static final Logger logger = Logger.getLogger(Streamer.class.getName());

    /**
     * Inner class representing each row of the {@link ResultSet} as
     * it flows down the stream. This implementation assumes you know
     * what type each of the columns should be, and their order

     *
     */
    public static class Row {

        // For making a copy of the object values
        private final String[] columnNames;
        private final Object[] cols;
        public Row(ResultSet rs, String[] columnNames) throws SQLException {
            this.columnNames = columnNames;
            this.cols = new Object[columnNames.length];
            for (int i=0; i<cols.length; i++) {
                cols[i] = rs.getObject(i+1);
            }
        }
        
        public String[] getColumnNames() {
            return this.columnNames;
        }
        
        public Object[] getCols() {
            return this.cols;
        }
    }
    

    /**
     * Wrap the JDBC {@link ResultSet} as a stream object. Note that the stream has to be consumed within
     * the boundaries of the statement execution
     * @param rs
     * @return
     */
    public Stream<Row> wrap(final ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        final String[] columnNames = new String[md.getColumnCount()];
        for (int i=0; i<columnNames.length; i++) {
            columnNames[i] = md.getColumnName(i+1);
        }
        
        Spliterator<Row> s = new Spliterators.AbstractSpliterator<Row>(Long.MAX_VALUE, Spliterator.NONNULL | Spliterator.IMMUTABLE) {

            @Override
            public boolean tryAdvance(Consumer<? super Row> action) {
                try {
                    if (!rs.next()) {
                        return false;
                    }
                    
                    // wrap the current result record as a Row which is then handed
                    // off to the stream
                    action.accept(new Row(rs, columnNames));
                    return true;
                }
                catch (SQLException x) {
                    // ouch
                    logger.log(Level.SEVERE, x.getMessage(), x);
                    return false;
                }
            }
        };
        
        // Create a sequential stream of the rows from the result set
        return StreamSupport.stream(s, false);
    }
    
}
