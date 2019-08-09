/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.patch;

import javax.json.JsonArray;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonPatch;
import javax.json.spi.JsonProvider;

import com.ibm.watsonhealth.fhir.model.format.Format;
import com.ibm.watsonhealth.fhir.model.parser.FHIRJsonParser;
import com.ibm.watsonhealth.fhir.model.parser.FHIRParser;
import com.ibm.watsonhealth.fhir.model.parser.exception.FHIRParserException;
import com.ibm.watsonhealth.fhir.model.patch.exception.FHIRPatchException;
import com.ibm.watsonhealth.fhir.model.resource.Resource;

public class FHIRJsonPatch {
    private static final JsonProvider PROVIDER = JsonProvider.provider();
    private final JsonPatch patch;
    
    public FHIRJsonPatch(JsonArray array) {
        patch = PROVIDER.createPatch(array);
    }

    public <T extends Resource> T apply(JsonObject object) throws FHIRPatchException {
        try {
            return FHIRParser.parser(Format.JSON)
                    .as(FHIRJsonParser.class)
                    .parse(patch.apply(object));
        } catch (JsonException e) {
            throw new FHIRPatchException(e.getMessage(), e);
        } catch (FHIRParserException e) {
            throw new FHIRPatchException(e.getMessage(), e.getPath(), e);
        }
    }
}
