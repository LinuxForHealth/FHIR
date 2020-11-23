/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.util;


/**
 * Fixed set of compartment names. The id is used in the database. Do not change it!
 * Must also be unique, which we have to enforce in a unit-test
 * {@link https://www.hl7.org/fhir/compartmentdefinition.html}
 */
public enum CompartmentNames {
    Patient(0),
    Encounter(1),
    RelatedPerson(2),
    Practitioner(3),
    Device(4);
    
    // The id we use for the compartment in the database
    private final int id;

    /**
     * Public constructor
     * @param id
     */
    private CompartmentNames(int id) {
        this.id = id;
    }

    /**
     * Getter for the id associated with this compartment name
     * @return
     */
    public int getId() {
        return this.id;
    }
}
