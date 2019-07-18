/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.database.utils.common;

import com.ibm.watsonhealth.database.utils.api.IDatabaseStatement;
import com.ibm.watsonhealth.database.utils.api.IDatabaseSupplier;
import com.ibm.watsonhealth.database.utils.api.IDatabaseTarget;
import com.ibm.watsonhealth.database.utils.api.IDatabaseTranslator;

/**
 * Simple decorator to print out the DDL statement being run
 * @author rarnold
 *
 */
public class PrintTarget implements IDatabaseTarget {
    
    // The IDatabaseTarget implementation we are decorating
    private final IDatabaseTarget decorated;

    /**
     * Public constructor
     * @param decorated
     */
    public PrintTarget(IDatabaseTarget decorated) {
        this.decorated = decorated;
    }
    
    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.database.utils.api.IDatabaseTarget#runStatement(java.lang.String)
     */
    @Override
    public void runStatement(IDatabaseTranslator translator, String ddl) {
        System.out.println(ddl + ";");
        
        if (this.decorated != null) {
            this.decorated.runStatement(translator, ddl);
        }
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.database.utils.api.IDatabaseTarget#runStatementWithInt(com.ibm.watsonhealth.database.utils.api.IDatabaseTranslator, java.lang.String, int)
     */
    @Override
    public void runStatementWithInt(IDatabaseTranslator translator, String sql, int value) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.database.utils.api.IDatabaseTarget#runStatement(com.ibm.watsonhealth.database.utils.api.IDatabaseStatement)
     */
    @Override
    public void runStatement(IDatabaseTranslator translator, IDatabaseStatement statement) {
        // Print out the statement first before delegating the execution
        System.out.println(statement.toString() + ";");
        if (this.decorated != null) {
            this.decorated.runStatement(translator, statement);
        }
        
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.database.utils.api.IDatabaseTarget#runStatement(com.ibm.watsonhealth.database.utils.api.IDatabaseTranslator, com.ibm.watsonhealth.database.utils.api.IDatabaseSupplier)
     */
    @Override
    public <T> T runStatement(IDatabaseTranslator translator, IDatabaseSupplier<T> supplier) {
        System.out.println(supplier.toString() + ";");
        if (this.decorated != null) {
            return this.decorated.runStatement(translator, supplier);
        }
        else {
            return null;
        }
    }

}
