/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query.node;

/**
 * Base for all equality type expressions
 */
public abstract class EqualityExpNode extends BinaryExpNode {

    @Override
    public int precedence() {
        return 4;
    }
}