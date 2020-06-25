/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.group;

/**
 * signals to the upstream an issue with Group Search Compilation
 */
public class GroupSearchCompilerException extends Exception {
    private static final long serialVersionUID = 4849453894300769348L;

    public GroupSearchCompilerException(String msg) {
        super(msg);
    }
}