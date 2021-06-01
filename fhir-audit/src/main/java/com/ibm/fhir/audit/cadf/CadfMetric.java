/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.audit.cadf;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonReaderFactory;
import jakarta.json.stream.JsonGenerator;
import jakarta.json.stream.JsonGeneratorFactory;

import com.ibm.fhir.exception.FHIRException;

public class CadfMetric {
    private String metricId;
    private String unit;
    private String name;
    private CadfMapItem[] annotations;

    public String getMetricId() {
        return metricId;
    }

    public void setMetricId(String metricId) {
        this.metricId = metricId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CadfMapItem[] getAnnotations() {
        return annotations;
    }

    public void setAnnotations(CadfMapItem[] annotations) {
        this.annotations = annotations;
    }

    public static class Builder {
        private CadfMetric metric = new CadfMetric();
        private List<CadfMapItem> tmpAnnotations = new ArrayList<>();

        private Builder() {
            // No Operation
        }

        public Builder metricId(String metricId) {
            metric.setMetricId(metricId);
            return this;
        }

        public Builder unit(String unit) {
            metric.setUnit(unit);
            return this;
        }

        public Builder name(String name) {
            metric.setName(name);
            return this;
        }

        public Builder annotation(CadfMapItem annotation) {
            tmpAnnotations.add(annotation);
            return this;
        }

        public CadfMetric build() {
            metric.setAnnotations(tmpAnnotations.toArray(new CadfMapItem[0]));
            return metric;
        }

    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Generates JSON from this object.
     */
    public static class Writer {
        private static final Map<java.lang.String, Object> properties =
                Collections.singletonMap(JsonGenerator.PRETTY_PRINTING, true);
        private static final JsonGeneratorFactory PRETTY_PRINTING_GENERATOR_FACTORY =
                Json.createGeneratorFactory(properties);

        private Writer() {
            // No Operation
        }

        /**
         * @param obj
         * @return
         * @throws IOException
         */
        public static String generate(CadfMetric obj)
                throws IOException {
            String o = "{}";
            try (StringWriter writer = new StringWriter();) {
                try (JsonGenerator generator =
                        PRETTY_PRINTING_GENERATOR_FACTORY.createGenerator(writer);) {
                    generator.writeStartObject();
                    generate(obj, generator);
                    generator.writeEnd();
                }
                o = writer.toString();
            }
            return o;
        }

        public static void generate(CadfMetric obj, JsonGenerator generator)
                throws IOException {
            if (obj.getName() != null) {
                generator.write("name", obj.getName());
            }

            if (obj.getUnit() != null) {
                generator.write("unit", obj.getUnit());
            }

            if (obj.getMetricId() != null) {
                generator.write("metricId", obj.getMetricId());
            }

            //Annotations
            if (obj.getAnnotations() != null) {
                generator.writeStartArray("annotations");
                for (CadfMapItem item : obj.getAnnotations()) {
                    CadfMapItem.Writer.generate(item, generator);
                }
                generator.writeEnd();
            }
        }
    }

    /**
     * Parser
     */
    public static class Parser {
        private static final JsonReaderFactory JSON_READER_FACTORY = Json.createReaderFactory(null);

        private Parser() {
            // No Impl
        }

        public static CadfMetric parse(InputStream in)
                throws FHIRException {
            try (JsonReader jsonReader =
                    JSON_READER_FACTORY.createReader(in, StandardCharsets.UTF_8)) {
                JsonObject jsonObject = jsonReader.readObject();
                return parse(jsonObject);
            } catch (Exception e) {
                throw new FHIRException("Problem parsing the CadfCredential", e);
            }
        }

        public static CadfMetric parse(JsonObject jsonObject)
                throws FHIRException, IOException {
            CadfMetric.Builder builder =
                    CadfMetric.builder();

            String name = jsonObject.getString("name");
            if (name != null) {
                builder.name(name);
            }

            String unit = jsonObject.getString("unit");
            if (unit != null) {
                builder.unit(unit);
            }

            String metricId = jsonObject.getString("metricId");
            if (metricId != null) {
                builder.metricId(metricId);
            }

            JsonArray annotations = jsonObject.getJsonArray("annotations");
            if (annotations != null) {
                for (int i = 0; i < annotations.size(); i++) {
                    JsonObject obj = (JsonObject) annotations.get(0);
                    CadfMapItem mapItem = CadfMapItem.Parser.parse(obj);
                    builder.annotation(mapItem);
                }
            }
            return builder.build();
        }
    }
}
