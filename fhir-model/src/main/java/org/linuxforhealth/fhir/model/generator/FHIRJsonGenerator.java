/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.generator;

import static org.linuxforhealth.fhir.model.util.JsonSupport.nonClosingOutputStream;
import static org.linuxforhealth.fhir.model.util.JsonSupport.nonClosingWriter;
import static org.linuxforhealth.fhir.model.util.ModelSupport.isPrimitiveType;

import java.io.OutputStream;
import java.io.Writer;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import org.linuxforhealth.fhir.model.generator.exception.FHIRGeneratorException;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.type.Base64Binary;
import org.linuxforhealth.fhir.model.type.Boolean;
import org.linuxforhealth.fhir.model.type.Date;
import org.linuxforhealth.fhir.model.type.DateTime;
import org.linuxforhealth.fhir.model.type.Decimal;
import org.linuxforhealth.fhir.model.type.Element;
import org.linuxforhealth.fhir.model.type.Extension;
import org.linuxforhealth.fhir.model.type.Instant;
import org.linuxforhealth.fhir.model.type.Integer;
import org.linuxforhealth.fhir.model.type.String;
import org.linuxforhealth.fhir.model.type.Time;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.type.Xhtml;
import org.linuxforhealth.fhir.model.visitor.Visitable;

import jakarta.json.Json;
import jakarta.json.stream.JsonGenerator;
import jakarta.json.stream.JsonGeneratorFactory;

public class FHIRJsonGenerator extends FHIRAbstractGenerator {
    private static final JsonGeneratorFactory GENERATOR_FACTORY = Json.createGeneratorFactory(null);
    private static final JsonGeneratorFactory PRETTY_PRINTING_GENERATOR_FACTORY = Json.createGeneratorFactory(Collections.singletonMap(JsonGenerator.PRETTY_PRINTING, true));

    protected FHIRJsonGenerator(boolean prettyPrinting) {
        super(prettyPrinting);
    }

    @Override
    public void generate(Visitable visitable, OutputStream out) throws FHIRGeneratorException {
        GeneratingVisitor visitor = null;
        try (JsonGenerator generator = getGeneratorFactory().createGenerator(nonClosingOutputStream(out), StandardCharsets.UTF_8)) {
            visitor = new JsonGeneratingVisitor(generator);
            visitable.accept(visitor);
            generator.flush();
        } catch (Exception e) {
            throw new FHIRGeneratorException(e.getMessage(), (visitor != null) ? visitor.getPath() : null, e);
        }
    }

    @Override
    public void generate(Visitable visitable, Writer writer) throws FHIRGeneratorException {
        GeneratingVisitor visitor = null;
        try (JsonGenerator generator = getGeneratorFactory().createGenerator(nonClosingWriter(writer))) {
            visitor = new JsonGeneratingVisitor(generator);
            visitable.accept(visitor);
            generator.flush();
        } catch (Exception e) {
            throw new FHIRGeneratorException(e.getMessage(), (visitor != null) ? visitor.getPath() : null, e);
        }
    }

    @Override
    public boolean isPrettyPrinting() {
        return prettyPrinting;
    }

    private static class JsonGeneratingVisitor extends GeneratingVisitor {
        private final JsonGenerator generator;

        private JsonGeneratingVisitor(JsonGenerator generator) {
            this.generator = generator;
        }

        private void generate(Element element) {
            if (element.getId() != null) {
                // visit id
                visit("id", element.getId());
            }
            if (!element.getExtension().isEmpty()) {
                // visit extension
                visitStart("extension", element.getExtension(), Extension.class);
                int elementIndex = 0;
                for (Extension extension : element.getExtension()) {
                    extension.accept("extension", elementIndex++, this);
                }
                visitEnd("extension", element.getExtension(), Extension.class);
            }
        }

        private boolean hasIdOrExtension(Element element) {
            return element.getId() != null || !element.getExtension().isEmpty();
        }

        private boolean hasIdOrExtension(java.util.List<? extends Visitable> visitables) {
            for (Visitable visitable : visitables) {
                if (hasIdOrExtension((Element) visitable)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean visit(java.lang.String elementName, int elementIndex, Base64Binary base64Binary) {
            if (isChoiceElement(elementName)) {
                elementName = getChoiceElementName(elementName, Base64Binary.class);
            }
            if (base64Binary.getValue() != null) {
                writeValue(elementName, elementIndex, Base64.getEncoder().encodeToString(base64Binary.getValue()));
            } else {
                writeNull(elementName, elementIndex, base64Binary);
            }
            return false;
        }

        @Override
        public boolean visit(java.lang.String elementName, int elementIndex, Boolean _boolean) {
            if (isChoiceElement(elementName)) {
                elementName = getChoiceElementName(elementName, Boolean.class);
            }
            if (_boolean.getValue() != null) {
                writeValue(elementName, elementIndex, _boolean.getValue());
            } else {
                writeNull(elementName, elementIndex, _boolean);
            }
            return false;
        }

        @Override
        public boolean visit(java.lang.String elementName, int elementIndex, Date date) {
            if (isChoiceElement(elementName)) {
                elementName = getChoiceElementName(elementName, Date.class);
            }
            if (date.getValue() != null) {
                writeValue(elementName, elementIndex, Date.PARSER_FORMATTER.format(date.getValue()));
            } else {
                writeNull(elementName, elementIndex, date);
            }
            return false;
        }

        @Override
        public boolean visit(java.lang.String elementName, int elementIndex, DateTime dateTime) {
            if (isChoiceElement(elementName)) {
                elementName = getChoiceElementName(elementName, DateTime.class);
            }
            if (dateTime.getValue() != null) {
                writeValue(elementName, elementIndex, DateTime.PARSER_FORMATTER.format(dateTime.getValue()));
            } else {
                writeNull(elementName, elementIndex, dateTime);
            }
            return false;
        }

        @Override
        public boolean visit(java.lang.String elementName, int elementIndex, Decimal decimal) {
            if (isChoiceElement(elementName)) {
                elementName = getChoiceElementName(elementName, Decimal.class);
            }
            if (decimal.getValue() != null) {
                writeValue(elementName, elementIndex, decimal.getValue());
            } else {
                writeNull(elementName, elementIndex, decimal);
            }
            return false;
        }

        @Override
        public boolean visit(java.lang.String elementName, int elementIndex, Instant instant) {
            if (isChoiceElement(elementName)) {
                elementName = getChoiceElementName(elementName, Instant.class);
            }
            if (instant.getValue() != null) {
                writeValue(elementName, elementIndex, Instant.PARSER_FORMATTER.format(instant.getValue()));
            } else {
                writeNull(elementName, elementIndex, instant);
            }
            return false;
        }

        @Override
        public boolean visit(java.lang.String elementName, int elementIndex, Integer integer) {
            if (isChoiceElement(elementName)) {
                elementName = getChoiceElementName(elementName, integer.getClass());
            }
            if (integer.getValue() != null) {
                writeValue(elementName, elementIndex, integer.getValue());
            } else {
                writeNull(elementName, elementIndex, integer);
            }
            return false;
        }

        @Override
        public void doVisit(java.lang.String elementName, java.lang.String value) {
            writeValue(elementName, -1, value);
        }

        @Override
        public boolean visit(java.lang.String elementName, int elementIndex, String string) {
            if (isChoiceElement(elementName)) {
                elementName = getChoiceElementName(elementName, string.getClass());
            }
            if (string.getValue() != null) {
                writeValue(elementName, elementIndex, string.getValue());
            } else {
                writeNull(elementName, elementIndex, string);
            }
            return false;
        }

        @Override
        public boolean visit(java.lang.String elementName, int elementIndex, Time time) {
            if (isChoiceElement(elementName)) {
                elementName = getChoiceElementName(elementName, Time.class);
            }
            if (time.getValue() != null) {
                writeValue(elementName, elementIndex, Time.PARSER_FORMATTER.format(time.getValue()));
            } else {
                writeNull(elementName, elementIndex, time);
            }
            return false;
        }

        @Override
        public boolean visit(java.lang.String elementName, int elementIndex, Uri uri) {
            if (isChoiceElement(elementName)) {
                elementName = getChoiceElementName(elementName, uri.getClass());
            }
            if (uri.getValue() != null) {
                writeValue(elementName, elementIndex, uri.getValue());
            } else {
                writeNull(elementName, elementIndex, uri);
            }
            return false;
        }

        @Override
        public boolean visit(java.lang.String elementName, int elementIndex, Xhtml xhtml) {
            writeValue(elementName, elementIndex, xhtml.getValue());
            return false;
        }

        @Override
        public void doVisitEnd(java.lang.String elementName, int elementIndex, Element element) {
            Class<?> elementType = element.getClass();
            if (isPrimitiveType(elementType)) {
                if (isChoiceElement(elementName)) {
                    elementName = getChoiceElementName(elementName, elementType);
                }
                if (elementIndex == -1 && hasIdOrExtension(element)) {
                    generator.writeStartObject("_" + elementName);
                    generate(element);
                    generator.writeEnd();
                }
                if (getDepth() == 1) {
                    generator.writeEnd();
                }
            } else {
                generator.writeEnd();
            }
        }

        @Override
        public void visitEnd(java.lang.String elementName, List<? extends Visitable> visitables, Class<?> type) {
            if (!visitables.isEmpty()) {
                generator.writeEnd();
                if (isPrimitiveType(type) && hasIdOrExtension(visitables)) {
                    generator.writeStartArray("_" + elementName);
                    for (Visitable visitable : visitables) {
                        if (hasIdOrExtension((Element) visitable)) {
                            generator.writeStartObject();
                            generate((Element) visitable);
                            generator.writeEnd();
                        } else {
                            generator.writeNull();
                        }
                    }
                    generator.writeEnd();
                }
            }
        }

        @Override
        public void doVisitEnd(java.lang.String elementName, int elementIndex, Resource resource) {
            generator.writeEnd();
        }

        @Override
        public void doVisitStart(java.lang.String elementName, int elementIndex, Element element) {
            Class<?> elementType = element.getClass();
            if (!isPrimitiveType(elementType)) {
                if (isChoiceElement(elementName)) {
                    elementName = getChoiceElementName(elementName, element.getClass());
                }
                writeStartObject(elementName, elementIndex);
            } else if (getDepth() == 1) {
                generator.writeStartObject();
            }
        }

        @Override
        public void visitStart(java.lang.String elementName, List<? extends Visitable> visitables, Class<?> type) {
            if (!visitables.isEmpty()) {
                writeStartArray(elementName);
            }
        }

        @Override
        public void doVisitStart(java.lang.String elementName, int elementIndex, Resource resource) {
            writeStartObject(elementName, elementIndex);
            Class<?> resourceType = resource.getClass();
            java.lang.String resourceTypeName = resourceType.getSimpleName();
            generator.write("resourceType", resourceTypeName);
        }

        private void writeNull(java.lang.String elementName, int elementIndex, Element element) {
            if (elementIndex != -1 && hasIdOrExtension(element)) {
                generator.writeNull();
            }
        }

        private void writeStartArray(java.lang.String elementName) {
            generator.writeStartArray(elementName);
        }

        private void writeStartObject(java.lang.String elementName, int elementIndex) {
            if (getDepth() > 1 && elementIndex == -1) {
                generator.writeStartObject(elementName);
            } else {
                generator.writeStartObject();
            }
        }

        private void writeValue(java.lang.String elementName, int elementIndex, BigDecimal value) {
            if (elementIndex == -1) {
                generator.write(elementName, value);
            } else {
                generator.write(value);
            }
        }

        private void writeValue(java.lang.String elementName, int elementIndex, java.lang.Boolean value) {
            if (elementIndex == -1) {
                generator.write(elementName, value);
            } else {
                generator.write(value);
            }
        }

        private void writeValue(java.lang.String elementName, int elementIndex, java.lang.Integer value) {
            if (elementIndex == -1) {
                generator.write(elementName, value);
            } else {
                generator.write(value);
            }
        }

        private void writeValue(java.lang.String elementName, int elementIndex, java.lang.String value) {
            if (elementIndex == -1) {
                generator.write(elementName, value);
            } else {
                generator.write(value);
            }
        }
    }

    private JsonGeneratorFactory getGeneratorFactory() {
        return prettyPrinting ? PRETTY_PRINTING_GENERATOR_FACTORY : GENERATOR_FACTORY;
    }
}
