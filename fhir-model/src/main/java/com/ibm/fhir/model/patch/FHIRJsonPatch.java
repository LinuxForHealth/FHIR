/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.patch;

import java.util.Objects;

import jakarta.json.JsonArray;
import jakarta.json.JsonException;
import jakarta.json.JsonObject;
import jakarta.json.JsonPatch;
import jakarta.json.spi.JsonProvider;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.exception.FHIRGeneratorException;
import com.ibm.fhir.model.parser.FHIRJsonParser;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.parser.exception.FHIRParserException;
import com.ibm.fhir.model.patch.exception.FHIRPatchException;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.util.JsonSupport;

public class FHIRJsonPatch implements FHIRPatch {
    private static final JsonProvider PROVIDER = JsonProvider.provider();
    private final JsonPatch patch;
    
    FHIRJsonPatch(JsonArray array) {
        this(PROVIDER.createPatch(array));
    }
    
    FHIRJsonPatch(JsonPatch patch) {
        this.patch = Objects.requireNonNull(patch);
    }
    
    public JsonPatch getJsonPatch() {
        return patch;
    }

    @Override
    public <T extends Resource> T apply(T resource) throws FHIRPatchException {
        try {
            JsonObject object = JsonSupport.toJsonObject(resource);
            return FHIRParser.parser(Format.JSON)
                    .as(FHIRJsonParser.class)
                    .parse(patch.apply(object));
        } catch (JsonException e) {
            throw new FHIRPatchException(e.getMessage(), e);
        } catch (FHIRGeneratorException e) {
            throw new FHIRPatchException(e.getMessage(), e.getPath(), e);
        } catch (FHIRParserException e) {
            throw new FHIRPatchException(e.getMessage(), e.getPath(), e);
        }
    }
}
