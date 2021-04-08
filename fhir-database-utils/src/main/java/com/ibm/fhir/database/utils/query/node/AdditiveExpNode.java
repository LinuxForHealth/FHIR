/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query.node;

/**
 * Base for all additive type expressions
 */
public abstract class AdditiveExpNode extends BinaryExpNode {

    @Override
    public int precedence() {
        return 3;
    }
}