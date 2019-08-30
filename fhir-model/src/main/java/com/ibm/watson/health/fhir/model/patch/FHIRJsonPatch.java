/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.model.patch;

import java.util.Objects;

import javax.json.JsonArray;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonPatch;
import javax.json.spi.JsonProvider;

import com.ibm.watson.health.fhir.model.format.Format;
import com.ibm.watson.health.fhir.model.generator.exception.FHIRGeneratorException;
import com.ibm.watson.health.fhir.model.parser.FHIRJsonParser;
import com.ibm.watson.health.fhir.model.parser.FHIRParser;
import com.ibm.watson.health.fhir.model.parser.exception.FHIRParserException;
import com.ibm.watson.health.fhir.model.patch.exception.FHIRPatchException;
import com.ibm.watson.health.fhir.model.resource.Resource;
import com.ibm.watson.health.fhir.model.util.JsonSupport;

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
