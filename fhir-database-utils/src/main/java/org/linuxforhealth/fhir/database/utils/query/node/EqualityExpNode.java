/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.query.node;

/**
 * Base for all equality type expressions
 */
public abstract class EqualityExpNode extends BinaryExpNode {

    @Override
    public int precedence() {
        return 4;
    }
}