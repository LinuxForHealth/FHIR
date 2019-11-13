/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.benchmark;

import java.lang.instrument.Instrumentation;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;

import com.ibm.fhir.model.visitor.DefaultVisitor;
import com.ibm.fhir.model.visitor.Visitable;

/**
 * Uses {@link java.lang.instrument.Instrumentation} to estimate the size of a fhir-model Object.
 * This only works when you configure the enclosing jar file as a java agent. 
 */
public class ObjectSizeEstimator {
    private static ObjectSizeVisitor visitor = new ObjectSizeVisitor();
    private static Instrumentation instrumentation;

    public static void premain(String args, Instrumentation inst) {
        instrumentation = inst;
    }
    
    /**
     * Compute the estimated size of the Visitable by traversing the structure and adding 
     * the estimated size of all the objects in the tree
     */
    public static long getObjectSize(Visitable o) {
        o.accept(visitor);
        return visitor.getResult();
    }
    
    
    private static class ObjectSizeVisitor extends DefaultVisitor {
        long size = 0;
        
        public ObjectSizeVisitor() {
            super(true);
        }
        
        public long getResult() {
            return size;
        }
        
        @Override
        public boolean visit(String elementName, int elementIndex, Visitable visitable) {
            size += instrumentation.getObjectSize(visitable);
            return true;
        }

        @Override
        public void visit(java.lang.String elementName, BigDecimal value) {
            size += instrumentation.getObjectSize(value);
        }

        @Override
        public void visit(java.lang.String elementName, java.lang.Boolean value) {
            size += instrumentation.getObjectSize(value);
        }

        @Override
        public void visit(java.lang.String elementName, java.lang.Integer value) {
            size += instrumentation.getObjectSize(value);
        }

        @Override
        public void visit(java.lang.String elementName, LocalDate value) {
            size += instrumentation.getObjectSize(value);
        }

        @Override
        public void visit(java.lang.String elementName, LocalTime value) {
            size += instrumentation.getObjectSize(value);
        }

        @Override
        public void visit(java.lang.String elementName, java.lang.String value) {
            size += instrumentation.getObjectSize(value);
        }

        @Override
        public void visit(java.lang.String elementName, Year value) {
            size += instrumentation.getObjectSize(value);
        }

        @Override
        public void visit(java.lang.String elementName, YearMonth value) {
            size += instrumentation.getObjectSize(value);
        }

        @Override
        public void visit(java.lang.String elementName, ZonedDateTime value) {
            size += instrumentation.getObjectSize(value);
        }
    }
}
