/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.examples;

import java.io.IOException;

import com.ibm.watsonhealth.fhir.model.builder.Builder;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.examples.DataCreatorBase;

public class MinimalDataCreator extends DataCreatorBase {

    public MinimalDataCreator() throws IOException {
        super();
    }

    @Override
    protected Builder<?> addData(Builder<?> builder, int choiceIndicator) {
        if (builder instanceof Element.Builder) {
            setDataAbsentReason((Element.Builder) builder);
        }
        return builder;
    }
}
