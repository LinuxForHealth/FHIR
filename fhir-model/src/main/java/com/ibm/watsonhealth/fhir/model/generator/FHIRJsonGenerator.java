/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.generator;

import java.io.OutputStream;
import java.io.Writer;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;

import com.ibm.watsonhealth.fhir.model.format.Format;
import com.ibm.watsonhealth.fhir.model.generator.exception.FHIRGeneratorException;
import com.ibm.watsonhealth.fhir.model.resource.Patient;
import com.ibm.watsonhealth.fhir.model.resource.Resource;
import com.ibm.watsonhealth.fhir.model.type.Base64Binary;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.Date;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Decimal;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.HumanName;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Instant;
import com.ibm.watsonhealth.fhir.model.type.Integer;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.PositiveInt;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Time;
import com.ibm.watsonhealth.fhir.model.type.UnsignedInt;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.visitor.AbstractVisitor;
import com.ibm.watsonhealth.fhir.model.visitor.Visitable;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;


public class FHIRJsonGenerator implements FHIRGenerator {
    private static final JsonGeneratorFactory GENERATOR_FACTORY = Json.createGeneratorFactory(null);
    private static final JsonGeneratorFactory PRETTY_PRINTING_GENERATOR_FACTORY = createPrettyPrintingGeneratorFactory();
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss.SSSXXX");
    
    private final boolean prettyPrinting;
        
    protected FHIRJsonGenerator() {
        this(false);
    }

    protected FHIRJsonGenerator(boolean prettyPrinting) {
        this.prettyPrinting = prettyPrinting;
    }

    @Override
    public void generate(Resource resource, OutputStream out) throws FHIRGeneratorException {
        try (JsonGenerator generator = getGeneratorFactory().createGenerator(out)) {
            Visitor visitor = new GeneratingVisitor(generator);
            resource.accept(visitor);
            generator.flush();
        } catch (Exception e) {
            throw new FHIRGeneratorException(e.getMessage(), null, e);
        }
    }

    @Override
    public void generate(Resource resource, Writer writer) throws FHIRGeneratorException {
        try (JsonGenerator generator = getGeneratorFactory().createGenerator(writer)) {
            Visitor visitor = new GeneratingVisitor(generator);
            resource.accept(visitor);
            generator.flush();
        } catch (Exception e) {
            throw new FHIRGeneratorException(e.getMessage(), null, e);
        }
    }
    
    @Override
    public boolean isPrettyPrinting() {
        return prettyPrinting;
    }
    
    @Override
    public void reset() {
        // do nothing
    }

    private JsonGeneratorFactory getGeneratorFactory() {
        return prettyPrinting ? PRETTY_PRINTING_GENERATOR_FACTORY : GENERATOR_FACTORY;
    }
    
    private static JsonGeneratorFactory createPrettyPrintingGeneratorFactory() {
        Map<java.lang.String, Object> properties = new HashMap<>(1);
        properties.put(JsonGenerator.PRETTY_PRINTING, true);
        return Json.createGeneratorFactory(properties);
    }

    private static class GeneratingVisitor extends AbstractVisitor {
        private final JsonGenerator generator;
        
        public GeneratingVisitor(JsonGenerator generator) {
            this.generator = generator;
        }
        
        private void generate(Element element) {
            if (!element.getExtension().isEmpty()) {
                writeStartArray("extension");
                for (Extension extension : element.getExtension()) {
                    extension.accept(this);
                }
                generator.writeEnd();
            }
            if (element.getId() != null) {
                writeValue("id", element.getId());
            }
        }
        
        private boolean hasExtensionOrId(Element element) {
            return !element.getExtension().isEmpty() || element.getId() !=  null;
        }
    
        private boolean hasExtensionOrId(java.util.List<? extends Visitable> visitables) {
            for (Visitable visitable : visitables) {
                if (hasExtensionOrId((Element) visitable)) {
                    return true;
                }
            }
            return false;
        }
    
        private boolean isPrimitiveType(Class<?> elementType) {
            return Base64Binary.class.equals(elementType) ||
                    com.ibm.watsonhealth.fhir.model.type.Boolean.class.equals(elementType) ||
                    com.ibm.watsonhealth.fhir.model.type.String.class.isAssignableFrom(elementType) || 
                    Code.class.isAssignableFrom(elementType) ||
                    Uri.class.isAssignableFrom(elementType) ||
                    DateTime.class.equals(elementType) || 
                    Date.class.equals(elementType) ||
                    Time.class.equals(elementType) || 
                    Instant.class.equals(elementType) || 
                    com.ibm.watsonhealth.fhir.model.type.Integer.class.isAssignableFrom(elementType) || 
                    Decimal.class.equals(elementType);
        }
        
        @Override
        public boolean visit(java.lang.String elementName, Base64Binary base64Binary) {
            if (base64Binary.getValue() != null) {
                writeValue(elementName, Base64.getEncoder().encodeToString(base64Binary.getValue()));
            } else {
                writeNull(elementName, base64Binary);
            }
            return false;
        }
        
        @Override
        public boolean visit(java.lang.String elementName, Boolean _boolean) {
            if (_boolean.getValue() != null) {
                writeValue(elementName, _boolean.getValue());
            } else {
                writeNull(elementName, _boolean);
            }
            return false;
        }
        
        @Override
        public boolean visit(java.lang.String elementName, Date date) {
            if (date.getValue() != null) {
                writeValue(elementName, date.getValue().toString());
            } else {
                writeNull(elementName, date);
            }
            return false;
        }
        
        @Override
        public boolean visit(java.lang.String elementName, DateTime dateTime) {
            if (dateTime.getValue() != null) {
                if (!dateTime.isPartial()) {
                    writeValue(elementName, DATE_TIME_FORMATTER.format(dateTime.getValue()));
                } else {
                    writeValue(elementName, dateTime.getValue().toString());
                }
            } else {
                writeNull(elementName, dateTime);
            }
            return false;
        }
    
        @Override
        public boolean visit(java.lang.String elementName, Decimal decimal) {
            if (decimal.getValue() != null) {
                writeValue(elementName, decimal.getValue());
            } else {
                writeNull(elementName, decimal);
            }
            return false;
        }
        
        @Override
        public boolean visit(java.lang.String elementName, Instant instant) {
            if (instant.getValue() != null) {
                writeValue(elementName, DATE_TIME_FORMATTER.format(instant.getValue()));
            } else {
                writeNull(elementName, instant);
            }
            return false;
        }
        
        @Override
        public boolean visit(java.lang.String elementName, Integer integer) {
            if (integer.getValue() != null) {
                writeValue(elementName, integer.getValue());
            } else {
                writeNull(elementName, integer);
            }
            return false;
        }
    
        @Override
        public boolean visit(java.lang.String elementName, PositiveInt positiveInt) {
            if (positiveInt.getValue() != null) {
                writeValue(elementName, positiveInt.getValue());
            } else {
                writeNull(elementName, positiveInt);
            }
            return false;
        }
        
        @Override
        public boolean visit(java.lang.String elementName, String string) {
            if (string.getValue() != null) {
                writeValue(elementName, string.getValue());
            } else {
                writeNull(elementName, string);
            }
            return false;
        }
        
        @Override
        public void visit(java.lang.String elementName, java.lang.String value) {
            if (value != null) {
                writeValue(elementName, value);
            }
        }
    
        @Override
        public boolean visit(java.lang.String elementName, Time time) {
            if (time.getValue() != null) {
                writeValue(elementName, time.getValue().toString());
            } else {
                writeNull(elementName, time);
            }
            return false;
        }
        
        @Override
        public boolean visit(java.lang.String elementName, UnsignedInt unsignedInt) {
            if (unsignedInt.getValue() != null) {
                writeValue(elementName, unsignedInt.getValue());
            } else {
                writeNull(elementName, unsignedInt);
            }
            return false;
        }
        
        @Override
        public boolean visit(java.lang.String elementName, Uri uri) {
            if (uri.getValue() != null) {
                writeValue(elementName, uri.getValue());
            } else {
                writeNull(elementName, uri);
            }
            return false;
        }
        
        @Override
        public void visitEnd(java.lang.String elementName, Element element) {
            Class<?> elementType = element.getClass();
            if (isPrimitiveType(elementType)) {
                if (elementName != null && hasExtensionOrId(element)) {
                    generator.writeStartObject("_" + elementName);
                    generate(element);
                    generator.writeEnd();
                }
            } else {
                generator.writeEnd();
            }
        }
        
        @Override
        public void visitEnd(java.lang.String elementName, List<? extends Visitable> visitables, Class<?> type) {
            generator.writeEnd();
            if (isPrimitiveType(type) && hasExtensionOrId(visitables)) {
                generator.writeStartArray("_" + elementName);
                for (Visitable visitable : visitables) {
                    if (hasExtensionOrId((Element) visitable)) {
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
        
        @Override
        public void visitEnd(java.lang.String elementName, Resource resource) {
            generator.writeEnd();
        }
        
        @Override
        public void visitStart(java.lang.String elementName, Element element) {
            Class<?> elementType = element.getClass();
            if (!isPrimitiveType(elementType)) {
                writeStartObject(elementName);
            }
        }
        
        @Override
        public void visitStart(java.lang.String elementName, List<? extends Visitable> visitables, Class<?> type) {
            writeStartArray(elementName);
        }
    
        @Override
        public void visitStart(java.lang.String elementName, Resource resource) {
            writeStartObject(elementName);
            Class<?> resourceType = resource.getClass();
            java.lang.String resourceTypeName = resourceType.getSimpleName();
            generator.write("resourceType", resourceTypeName);
        }
    
        private void writeNull(java.lang.String elementName, Element element) {
            if (elementName == null && hasExtensionOrId(element)) {
                generator.writeNull();
            }
        }
    
        private void writeStartArray(java.lang.String elementName) {
            if (elementName != null) {
                generator.writeStartArray(elementName);
            } else {
                generator.writeStartArray();
            }
        }
    
        private void writeStartObject(java.lang.String elementName) {
            if (elementName != null) {
                generator.writeStartObject(elementName);
            } else {
                generator.writeStartObject();
            }
        }
    
        private void writeValue(java.lang.String elementName, BigDecimal value) {
            if (elementName != null) {
                generator.write(elementName, value);
            } else {
                generator.write(value);
            }
        }
    
        private void writeValue(java.lang.String elementName, java.lang.Boolean value) {
            if (elementName != null) {
                generator.write(elementName, value);
            } else {
                generator.write(value);
            }
        }
    
        private void writeValue(java.lang.String elementName, java.lang.Integer value) {
            if (elementName != null) {
                generator.write(elementName, value);
            } else {
                generator.write(value);
            }
        }
        
        private void writeValue(java.lang.String elementName, java.lang.String value) {
            if (elementName != null) {
                generator.write(elementName, value);
            } else {
                generator.write(value);
            }
        }
    }

    public static void main(java.lang.String[] args) throws Exception {
        Id id = Id.builder().value(UUID.randomUUID().toString())
                .extension(Extension.builder("http://www.ibm.com/someExtension")
                    .value(String.of("Hello, World!"))
                    .build())
                .build();
        
        Meta meta = Meta.builder().versionId(Id.of("1"))
                .lastUpdated(Instant.now(true))
                .build();
        
        String given = String.builder().value("John")
                .extension(Extension.builder("http://www.ibm.com/someExtension")
                    .value(String.of("value and extension"))
                    .build())
                .build();
        
        String otherGiven = String.builder()
                .extension(Extension.builder("http://www.ibm.com/someExtension")
                    .value(String.of("extension only"))
                    .build())
                .build();
        
        HumanName name = HumanName.builder()
                .id("someId")
                .given(given)
                .given(otherGiven)
                .given(String.of("value no extension"))
                .family(String.of("Doe"))
                .build();
                
        Patient patient = Patient.builder()
                .id(id)
                .active(Boolean.TRUE)
                .multipleBirth(Integer.of(2))
                .meta(meta)
                .name(name)
                .birthDate(Date.of(LocalDate.now()))
                .build();
    
        FHIRGenerator.generator(Format.JSON, true).generate(patient, System.out);
    }
}
