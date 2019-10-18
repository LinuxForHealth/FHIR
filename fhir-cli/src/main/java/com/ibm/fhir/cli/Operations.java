/*
 * (C) Copyright IBM Corp. 2016,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.cli;

/**
 * This enum defines the valid operations that can be invoked via the command-line tool.
 * 
 * @author padams
 */
public enum Operations {
    BATCH("batch", OptionNames.RESOURCE),
    CREATE("create", OptionNames.RESOURCE),
    COND_CREATE("conditional-create", OptionNames.RESOURCE),
    DELETE("delete", OptionNames.RESOURCETYPE, OptionNames.ID),
    COND_DELETE("conditional-delete", OptionNames.RESOURCETYPE),
    HISTORY("history", OptionNames.RESOURCETYPE, OptionNames.ID),
    METADATA("metadata"),
    READ("read", OptionNames.RESOURCETYPE, OptionNames.ID),
    SEARCH("search", OptionNames.RESOURCETYPE),
    SEARCHALL("search-all"),
    SEARCH_POST("search-post", OptionNames.RESOURCETYPE),
    TRANSACTION("transaction", OptionNames.RESOURCE),
    UPDATE("update", OptionNames.RESOURCE),
    COND_UPDATE("conditional-update", OptionNames.RESOURCE),
    VALIDATE("validate", OptionNames.RESOURCE),
    VREAD("vread", OptionNames.RESOURCETYPE, OptionNames.ID, OptionNames.VERSIONID);

    private String opName;
    private OptionNames[] requiredOptions;

    private Operations(String opName, OptionNames... requiredOptions) {
        this.opName = opName;
        this.requiredOptions = requiredOptions;
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
    
    public static String operationsAndRequiredOptions() {
        StringBuilder sb = new StringBuilder();
        for (Operations op : Operations.values()) {
            sb.append("    ");
            sb.append(op.getOpName() + ":  ");
            OptionNames [] reqOptions = op.getRequiredOptions();
            if (reqOptions != null && reqOptions.length != 0) {
                boolean needSep = false;
                for (OptionNames option : reqOptions) {
                    if (needSep) {
                        sb.append(", ");
                    }
                    needSep = true;
                    sb.append("--" + option.getLongName());
                }
                
            } else {
                sb.append("No options required");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public OptionNames[] getRequiredOptions() {
        return requiredOptions;
    }
}
