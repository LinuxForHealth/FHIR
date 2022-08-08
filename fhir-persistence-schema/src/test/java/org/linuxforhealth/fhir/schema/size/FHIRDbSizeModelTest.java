/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.schema.size;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.testng.annotations.Test;

/**
 * Unit test for {@link FHIRDbSizeModel}
 */
public class FHIRDbSizeModelTest {
    // can be anything for these tests
    private static final String SCHEMA_NAME = "FHIRDATA";

    @Test
    public void testAccumulation() {
        FHIRDbSizeModel model = new FHIRDbSizeModel(SCHEMA_NAME);
        model.accumulateTableSize("Resource", "LOGICAL_RESOURCES", false, 50, 100);
        model.accumulateIndexSize("Resource", "LOGICAL_RESOURCES", false, "PK_LOGICAL_RESOURCE", 128);
        model.accumulateTableSize("Patient", "PATIENT_LOGICAL_RESOURCES", false, 30, 10);
        model.accumulateIndexSize("Patient", "PATIENT_LOGICAL_RESOURCES", false, "PK_PATIENT_LOGICAL_RESOURCES", 60);
        model.accumulateTableSize("Patient", "PATIENT_STR_VALUES", true, 75, 300);
        model.accumulateIndexSize("Patient", "PATIENT_STR_VALUES", true, "PATIENT_STR_VALUES_VAL", 175);
        model.accumulateIndexSize("Patient", "PATIENT_STR_VALUES", true, "PATIENT_STR_VALUES_UCASE", 165);
        
        assertEquals(model.getTotalTableSize(), 155);
        assertEquals(model.getTotalIndexSize(), 528);
        assertEquals(model.getTotalSize(), 683);
        
        // traverse the model and check it was built as we expected
        final Set<String> resources = new HashSet<>();
        final Set<String> parameterResources = new HashSet<>();
        final AtomicLong checkResourceTableSize = new AtomicLong();
        final AtomicLong checkResourceIndexSize = new AtomicLong();
        final AtomicLong checkResourceRows = new AtomicLong();
        final AtomicLong checkParameterTableSize = new AtomicLong();
        final AtomicLong checkParameterIndexSize = new AtomicLong();
        final AtomicLong checkParameterRows = new AtomicLong();
        
        FHIRDbSizeModelVisitor visitor = new FHIRDbSizeModelVisitor() {
            @Override
            public void start() {
                // NOP
            }
            
            @Override
            public void resource(String resourceType, long logicalResourceRowEstimate, long resourceRowEstimate, long totalTableSize, long totalIndexSize,
                long rowEstimate, long resourceTableSize, long resourceIndexSize) {
                resources.add(resourceType);
                checkResourceTableSize.addAndGet(resourceTableSize);
                checkResourceIndexSize.addAndGet(resourceIndexSize);
                checkResourceRows.addAndGet(rowEstimate);
            }
            
            @Override
            public void table(String resourceType, String parameterTable, boolean isParameterTable, long rowEstimate, long paramTableSize, long paramIndexSize) {
                if (isParameterTable) {
                    parameterResources.add(resourceType);
                    checkParameterTableSize.addAndGet(paramTableSize);
                    checkParameterIndexSize.addAndGet(paramIndexSize);
                    checkParameterRows.addAndGet(rowEstimate);
                }
            }

            @Override
            public void index(String resourceType, String tableName, String indexName, long indexSize) {
                // NOP
            }
        };
        model.accept(visitor);
        assertTrue(resources.contains("Resource"));
        assertTrue(resources.contains("Patient"));
        assertTrue(parameterResources.contains("Patient"));
        assertEquals(checkResourceTableSize.get(), 155); // total of all table sizes including param tables
        assertEquals(checkResourceIndexSize.get(), 528); // total of all index sizes including param table indexes
        assertEquals(checkResourceRows.get(), 410); // total of rows for all tables
        assertEquals(checkParameterTableSize.get(), 75);
        assertEquals(checkParameterIndexSize.get(), 340);
        assertEquals(checkParameterRows.get(), 300);
    }
}
