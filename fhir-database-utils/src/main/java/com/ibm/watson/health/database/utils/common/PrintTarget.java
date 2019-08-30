/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.database.utils.common;

import com.ibm.watson.health.database.utils.api.IDatabaseStatement;
import com.ibm.watson.health.database.utils.api.IDatabaseSupplier;
import com.ibm.watson.health.database.utils.api.IDatabaseTarget;
import com.ibm.watson.health.database.utils.api.IDatabaseTranslator;

/**
 * Simple decorator to print out the DDL statement being run
 * @author rarnold
 *
 */
public class PrintTarget implements IDatabaseTarget {
    
    // The IDatabaseTarget implementation we are decorating
    private final IDatabaseTarget decorated;
    
    // to print, or not to print
    private final boolean printFlag;

    /**
     * Public constructor
     * @param decorated
     */
    public PrintTarget(IDatabaseTarget decorated, boolean printFlag) {
        this.decorated = decorated;
        this.printFlag = printFlag;
    }
    
    /* (non-Javadoc)
     * @see com.ibm.watson.health.database.utils.api.IDatabaseTarget#runStatement(java.lang.String)
     */
    @Override
    public void runStatement(IDatabaseTranslator translator, String ddl) {
        if (printFlag) {
            System.out.println(ddl + ";");
        }
        
        if (this.decorated != null) {
            this.decorated.runStatement(translator, ddl);
        }
    }

    /* (non-Javadoc)
     * @see com.ibm.watson.health.database.utils.api.IDatabaseTarget#runStatementWithInt(com.ibm.watson.health.database.utils.api.IDatabaseTranslator, java.lang.String, int)
     */
    @Override
    public void runStatementWithInt(IDatabaseTranslator translator, String sql, int value) {
        if (this.printFlag) {
            System.out.println(sql + "; [value=" + value + "]");
        }
        
        if (this.decorated != null) {
            this.decorated.runStatementWithInt(translator, sql, value);
        }
    }

    /* (non-Javadoc)
     * @see com.ibm.watson.health.database.utils.api.IDatabaseTarget#runStatement(com.ibm.watson.health.database.utils.api.IDatabaseStatement)
     */
    @Override
    public void runStatement(IDatabaseTranslator translator, IDatabaseStatement statement) {
        // Print out the statement first before delegating the execution
        if (this.printFlag) {
            System.out.println(statement.toString() + ";");
        }
        
        if (this.decorated != null) {
            this.decorated.runStatement(translator, statement);
        }
        
    }

    /* (non-Javadoc)
     * @see com.ibm.watson.health.database.utils.api.IDatabaseTarget#runStatement(com.ibm.watson.health.database.utils.api.IDatabaseTranslator, com.ibm.watson.health.database.utils.api.IDatabaseSupplier)
     */
    @Override
    public <T> T runStatement(IDatabaseTranslator translator, IDatabaseSupplier<T> supplier) {
        if (this.printFlag) {
            System.out.println(supplier.toString() + ";");
        }
        
        if (this.decorated != null) {
            return this.decorated.runStatement(translator, supplier);
        }
        else {
            return null;
        }
    }

}
