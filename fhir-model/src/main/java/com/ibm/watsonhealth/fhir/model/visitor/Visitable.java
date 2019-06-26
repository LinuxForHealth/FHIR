/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.visitor;

public interface Visitable {
    void accept(java.lang.String elementName, Visitor visitor);

    default void accept(Visitor visitor) {
        accept(null, visitor);
    }
}
