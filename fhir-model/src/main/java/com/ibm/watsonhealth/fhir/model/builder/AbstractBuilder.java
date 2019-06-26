/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.builder;

public abstract class AbstractBuilder<T> implements Builder<T> {
    @Override
    public abstract T build();
}
