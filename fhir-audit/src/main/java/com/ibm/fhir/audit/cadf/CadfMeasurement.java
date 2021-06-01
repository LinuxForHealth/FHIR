/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.audit.cadf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonReaderFactory;
import jakarta.json.stream.JsonGenerator;
import jakarta.json.stream.JsonGeneratorFactory;

import com.ibm.fhir.exception.FHIRException;

public class CadfMeasurement {
    private Object result;
    private String metricId;
    private CadfMetric metric;
    private CadfResource calculatedBy;
    private String calculatedById;

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getMetricId() {
        return metricId;
    }

    public void setMetricId(String metricId) {
        this.metricId = metricId;
    }

    public CadfMetric getMetric() {
        return metric;
    }

    public void setMetric(CadfMetric metric) {
        this.metric = metric;
    }

    public CadfResource getCalculatedBy() {
        return calculatedBy;
    }

    public void setCalculatedBy(CadfResource calculatedBy) {
        this.calculatedBy = calculatedBy;
    }

    public String getCalculatedById() {
        return calculatedById;
    }

    public void setCalculatedById(String calculatedById) {
        this.calculatedById = calculatedById;
    }

    public static class Builder {
        private CadfMeasurement measurement = new CadfMeasurement();

        private Builder() {
            // No Operation
        }

        public Builder result(Object result) {
            measurement.setResult(result);
            return this;
        }

        public Builder metricId(String metricId) {
            measurement.setMetricId(metricId);
            return this;
        }

        public Builder metric(CadfMetric metric) {
            measurement.setMetric(metric);
            return this;
        }

        public Builder calculatedBy(CadfResource calculatedBy) {
            measurement.setCalculatedBy(calculatedBy);
            return this;
        }

        public Builder calculatedById(String calculatedById) {
            measurement.setCalculatedById(calculatedById);
            return this;
        }

        public CadfMeasurement build() {
            return measurement;
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
        public static String generate(CadfMeasurement obj)
                throws IOException {
            String o = "{}";
            try (StringWriter writer = new StringWriter();) {
                try (JsonGenerator generator =
                        PRETTY_PRINTING_GENERATOR_FACTORY.createGenerator(writer);) {
                    generate(obj, generator);
                }
                o = writer.toString();
            }
            return o;
        }

        public static void generate(CadfMeasurement obj, JsonGenerator generator)
                throws IOException {
            if (obj == null) {
                return;
            }
            generator.writeStartObject();

            if (obj.getResult() != null) {
                Object tmpObj = obj.getResult();
                try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ObjectOutputStream objStream = new ObjectOutputStream(baos);) {
                    objStream.writeObject(tmpObj);
                    generator.write("result",
                            java.util.Base64.getEncoder().encodeToString(baos.toByteArray()));
                }
            }

            if (obj.getMetricId() != null) {
                generator.write("metricId", obj.getMetricId());
            }

            if (obj.getMetric() != null) {
                generator.writeStartObject("metric");
                CadfMetric.Writer.generate(obj.getMetric(), generator);
                generator.writeEnd();
            }

            if (obj.getCalculatedBy() != null) {
                generator.writeStartObject("calculatedBy");
                CadfResource.Writer.generate(obj.getCalculatedBy(), generator);
                generator.writeEnd();
            }

            if (obj.getCalculatedById() != null) {
                generator.write("calculatedById", obj.getCalculatedById());
            }

            generator.writeEnd();
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

        public static CadfMeasurement parse(InputStream in)
                throws FHIRException {
            try (JsonReader jsonReader =
                    JSON_READER_FACTORY.createReader(in, StandardCharsets.UTF_8)) {
                JsonObject jsonObject = jsonReader.readObject();
                return parse(jsonObject);
            } catch (Exception e) {
                throw new FHIRException("Problem parsing the CadfMeasurement", e);
            }
        }

        public static CadfMeasurement parse(JsonObject jsonObject)
                throws FHIRException, IOException {
            CadfMeasurement.Builder builder =
                    CadfMeasurement.builder();

            String calculatedById = jsonObject.getString("calculatedById");
            if (calculatedById != null) {
                builder.calculatedById(calculatedById);
            }

            String metricId = jsonObject.getString("metricId");
            if (metricId != null) {
                builder.metricId(metricId);
            }

            String result = jsonObject.getString("result");
            if (result != null) {
                try {
                    byte[] contents = java.util.Base64.getDecoder().decode(result);
                    ByteArrayInputStream bis = new ByteArrayInputStream(contents);
                    try (ObjectInput input = new ObjectInputStream(bis);) {
                        Object tmpValue = input.readObject();
                        builder.result(tmpValue);
                    }
                } catch (Exception e) {
                    throw new FHIRException("Issue converting from base64 to jsonObject value");
                }
            }

            JsonObject jsonObjectTmp = jsonObject.getJsonObject("calculatedBy");
            if (jsonObjectTmp != null) {
                try {
                    CadfResource r = CadfResource.Parser.parse(jsonObjectTmp);
                    builder.calculatedBy(r);
                } catch (Exception e) {
                    throw new FHIRException("Issue converting from base64 to jsonObject value for resource");
                }
            }

            jsonObjectTmp = jsonObject.getJsonObject("metric");
            if (jsonObjectTmp != null) {
                CadfMetric r = CadfMetric.Parser.parse(jsonObjectTmp);
                builder.metric(r);
            }

            return builder.build();
        }
    }
}