/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.query.node;

/**
 * An expression node which is an operator (as opposed to an operand).
 * Used to facilitate expression parsing
 */
public abstract class OperatorNode implements ExpNode {

    @Override
    public boolean isOperator() {
        return true;
    }

    @Override
    public String toString() {
        // Just need the empty string, because the expression will include the classname
        return "";
    }
}