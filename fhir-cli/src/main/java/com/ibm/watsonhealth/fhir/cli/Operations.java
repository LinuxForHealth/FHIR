/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.cli;

/**
 * This enum defines the valid operations that can be invoked via the command-line tool.
 * 
 * @author padams
 */
public enum Operations {
    BATCH("batch"),
    CREATE("create"),
    HISTORY("history"),
    METADATA("metadata"),
    READ("read"),
    SEARCH("search"),
    SEARCHALL("search-all"),
    SEARCH_POST("search-post"),
    TRANSACTION("transaction"),
    UPDATE("update"),
    VALIDATE("validate"),
    VREAD("vread");

    private String opName;

    private Operations(String opName) {
        this.opName = opName;
    }

    public String getOpName() {
        return this.opName;
    }

    public static Operations fromString(String s) {
        for (Operations op : Operations.values()) {
            if (op.getOpName().equals(s)) {
                return op;
            }
        }
        return null;
    }

    public static String validOperations() {
        StringBuilder sb = new StringBuilder();
        boolean needSep = false;
        for (Operations op : Operations.values()) {
            if (needSep) {
                sb.append(" | ");
            }
            sb.append(op.getOpName());
            needSep = true;
        }
        return sb.toString();
    }

}
