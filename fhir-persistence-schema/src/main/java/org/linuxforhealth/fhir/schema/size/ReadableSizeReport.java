/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.schema.size;

import java.io.IOException;
import java.io.Writer;
import java.time.Instant;

import org.linuxforhealth.fhir.database.utils.api.DataAccessException;


/**
 * A human readable report showing the breakdown of size/usage in the FHIR data
 * schema.
 */
public class ReadableSizeReport implements ISizeReport, FHIRDbSizeModelVisitor {
    private static final String NL = System.lineSeparator();

    // The output we use to render the report
    private final Writer output;
    
    // Should the report output include detailed table and index info
    private boolean includeDetail;

    /**
     * Public constructor
     * @param output
     */
    public ReadableSizeReport(Writer output, boolean includeDetail) {
        this.output = output;
        this.includeDetail = includeDetail;
    }

    @Override
    public void render(FHIRDbSizeModel model) {
        try {
            writeln("Database Size Report");
            writeln("====================");
            writeln(String.format("%12s: %s", "Date", Instant.now()));
            writeln(String.format("%12s: %s", "Schema", model.getSchemaName()));
            writeln(String.format("%12s: %12d %s", "Db Size", model.getTotalSize(), "bytes"));
            writeln(String.format("%12s: %12d %s", "Table Size", model.getTotalTableSize(), "bytes"));
            writeln(String.format("%12s: %12d %s", "Index Size", model.getTotalIndexSize(), "bytes"));
            writeln("");
            writeln("Summary by Resource Type");
            writeln("------------------------");
            model.accept(this);

            // Collect parameter summary and render
            writeln("");
            writeln("Summary by Parameter Table Type");
            writeln("-------------------------------");
            FHIRDbParameterSummary ps = new FHIRDbParameterSummary(this);
            model.accept(ps);
            ps.render(model.getTotalTableSize(), model.getTotalIndexSize());

            if (this.includeDetail) {
                writeln("");
                writeln("Table Detail");
                writeln("------------");
                FHIRDbTableDetailWriter tableDetail = new FHIRDbTableDetailWriter(output);
                model.accept(tableDetail);
    
                writeln("");
                writeln("Index Detail");
                writeln("------------");
                FHIRDbIndexDetailWriter indexDetail = new FHIRDbIndexDetailWriter(output);
                model.accept(indexDetail);
            }

        } catch (IOException x) {
            throw new DataAccessException(x);
        }
    }
    
    /**
     * Get the system line separator character
     * @return
     */
    private String nl() {
        return NL;
    }

    /**
     * Convenience function to make write statements more fluid
     * @param str
     * @return
     * @throws IOException
     */
    private Writer write(String str) throws IOException {
        output.write(str);
        return output;
    }

    /**
     * Convenience function to write the line followed by a newline
     * @param line
     * @return
     * @throws IOException
     */
    private Writer writeln(String line) throws IOException {
        output.write(line);
        output.write(nl());
        return output;
    }

    @Override
    public void start() {
        // NOP
    }

    @Override
    public void resource(String resourceType, long logicalResourceRowEstimate, long resourceRowEstimate, long totalTableSize, long totalIndexSize, long rowEstimate, long resourceTableSize, long resourceIndexSize) {
        // render an info line for the given resource type - but only if the row count is greater than zero
        // because we don't care about the empty tables (although they take up some space, this is already
        // included in the database-level totals
        final long totalDbSize = totalTableSize + totalIndexSize;
        try {
            if (rowEstimate > 0) {
                // we are interested in how much each thing contributes to the overal size of the database
                double tablePercent = 100.0 * resourceTableSize / totalDbSize;
                double indexPercent = 100.0 * resourceIndexSize / totalDbSize;
                writeln(String.format("%34s  Resources/Versions: %9d/%9d All Rows: %9d Table Bytes: %12d [%4.1f%%] Index Bytes: %12d [%4.1f%%]", 
                        resourceType, logicalResourceRowEstimate, resourceRowEstimate, rowEstimate, resourceTableSize, tablePercent, resourceIndexSize, indexPercent));
            }
        } catch (IOException x) {
            throw new DataAccessException(x);
        }
    }

    @Override
    public void table(String resourceType, String parameterTable, boolean isParameterTable, long rowEstimate, long paramTableSize, long paramIndexSize) {
        // NOP
    }

    @Override
    public void index(String resourceType, String tableName, String indexName, long indexSize) {
        // NOP
    }

    @Override
    public void renderParameterSummary(String parameterTable, long rowEstimate, long totalTableSize, long tableSize, long totalIndexSize, long indexSize) {
        final long totalDbSize = totalTableSize + totalIndexSize;
        try {
            double tablePercent = 100.0 * tableSize / totalDbSize;
            double indexPercent = 100.0 * indexSize / totalDbSize;
            writeln(String.format("%24s  Rows: %9d Table: %12d [%4.1f%%] Index: %12d [%4.1f%%]", 
                    parameterTable, rowEstimate, tableSize, tablePercent, indexSize, indexPercent));
        } catch (IOException x) {
            throw new DataAccessException(x);
        }
        
    }
}
