/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.params.model;


/**
 * Holder for the code_system_id obtained from the database. This is
 * modelled as a mutable record so that we can reference this record
 * many times, and resolve it once (either from cache lookup, database
 * lookup, or a database create).
 */
public class CodeSystemValue implements Comparable<CodeSystemValue> {
    private final String codeSystem;

    // the id gets set after we find/create it in the database
    private Integer codeSystemId;

    /**
     * Public constructor
     * @param codeSystem
     */
    public CodeSystemValue(String codeSystem) {
        this.codeSystem = codeSystem;
    }

    @Override
    public int hashCode() {
        return codeSystem.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CodeSystemValue) {
            CodeSystemValue that = (CodeSystemValue)obj;
            return 0 == compareTo(that);
        }
        return false;
    }

    @Override
    public int compareTo(CodeSystemValue that) {
        return this.codeSystem.compareTo(that.codeSystem);
    }

    /**
     * @return the codeSystemId or null if it is current unknown
     */
    public Integer getCodeSystemId() {
        return codeSystemId;
    }

    /**
     * @param codeSystemId the codeSystemId to set
     */
    public void setCodeSystemId(int codeSystemId) {
        this.codeSystemId = codeSystemId;
    }

    /**
     * @return the codeSystem
     */
    public String getCodeSystem() {
        return codeSystem;
    }

}