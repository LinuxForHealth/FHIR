/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.audit.cadf;

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

import org.linuxforhealth.fhir.exception.FHIRException;

/**
 * The CADF Attachment type is used as one means to add domain-specific
 * information to certain CADF entities or data types.
 */
public class CadfAttachment {
    /**
     * URI that identifies the data type of the content property. Required.
     */
    private String contentType;
    /**
     * A container with attachment data. Required.
     */
    private Object content;
    /**
     * An optional name that can be used to identify content (e.g. file name)
     */
    private String name;

    /**
     * Create an attachment object with a user-friendly name.
     *
     * @param contentType URI that identifies the data type of the content property.
     *                    Required. For example, an attachment that includes a
     *                    standard MIME types (such as "application/pdf") can be
     *                    included by setting this property to
     *                    "http://www.iana.org/assignments/media-types/application/pdf"
     * @param content     A container with attachment data. Required.
     * @param name        An optional name that can be used to identify content
     *                    (e.g. file name)
     */
    public CadfAttachment(String contentType, Object content, String name) {
        this.setContent(content);
        this.setContentType(contentType);
        this.setName(name);
    }

    /**
     * Create an attachment object.
     *
     * @param contentType URI that identifies the data type of the content property.
     *                    Required. For example, an attachment that includes a
     *                    standard MIME types (such as "application/pdf") can be
     *                    included by setting this property to
     *                    "http://www.iana.org/assignments/media-types/application/pdf"
     * @param content     A container with attachment data. Required.
     */
    public CadfAttachment(String contentType, Object content) {
        this.setContent(content);
        this.setContentType(contentType);
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Generates JSON from this object.
     */
    public static class Writer {

        private Writer() {
            // No Operation
        }

        private static final Map<java.lang.String, Object> properties =
                Collections.singletonMap(JsonGenerator.PRETTY_PRINTING, true);
        private static final JsonGeneratorFactory PRETTY_PRINTING_GENERATOR_FACTORY =
                Json.createGeneratorFactory(properties);

        /**
         * @param obj
         * @return
         * @throws IOException
         */
        public static String generate(CadfAttachment obj)
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

        public static void generate(CadfAttachment obj, JsonGenerator generator)
                throws IOException {
            // If the CADF attachment is null, then simply skip it.
            if (obj == null) {
                return;
            }
            generator.writeStartObject();

            if (obj.getName() != null) {
                generator.write("name", obj.getName());
            }

            if (obj.getContentType() != null) {
                generator.write("contentType", obj.getContentType());
            }

            if (obj.getContent() != null) {
                Object tmpObj = obj.getContent();
                try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ObjectOutputStream objStream = new ObjectOutputStream(baos);) {
                    objStream.writeObject(tmpObj);
                    generator.write("content",
                            java.util.Base64.getEncoder().encodeToString(baos.toByteArray()));
                }
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

        public static CadfAttachment parse(InputStream in)
                throws FHIRException {
            try (JsonReader jsonReader =
                    JSON_READER_FACTORY.createReader(in, StandardCharsets.UTF_8)) {
                return parse(jsonReader.readObject());
            } catch (Exception e) {
                throw new FHIRException("Problem parsing the CadfAttachment", e);
            }
        }

        public static CadfAttachment parse(JsonObject jsonObject) throws IOException, FHIRException {
            CadfAttachment.Builder builder =
                    CadfAttachment.builder();

            if (jsonObject.get("name") != null) {
                String name = jsonObject.getString("name");
                builder.name(name);
            }

            if (jsonObject.get("contentType") != null) {
                String contentType = jsonObject.getString("contentType");
                builder.contentType(contentType);
            }

            // Get the value and switch over to Object
            if (jsonObject.get("content") != null) {
                String content = jsonObject.getString("content");
                try {
                    byte[] values = java.util.Base64.getDecoder().decode(content);
                    try (ByteArrayInputStream bis = new ByteArrayInputStream(values);
                            ObjectInput input = new ObjectInputStream(bis);) {
                        Object tmpValue = input.readObject();
                        builder.content(tmpValue);
                    }
                } catch (Exception e) {
                    throw new FHIRException("Class not found", e);
                }
            }

            return builder.build();
        }
    }

    /**
     * Builder is a convenience pattern to assemble to Java Object
     */
    public static class Builder {
        private String name = null;
        private Object content = null;
        private String contentType = null;

        private Builder() {
            // Intentionally hiding from external callers.
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder content(Object content) {
            this.content = content;
            return this;
        }

        public CadfAttachment build() {
            if (name != null) {
                return new CadfAttachment(contentType, content, name);
            } else {
                return new CadfAttachment(contentType, content);
            }
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
