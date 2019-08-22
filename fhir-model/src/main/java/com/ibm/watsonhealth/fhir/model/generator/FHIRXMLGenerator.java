/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.generator;

import static com.ibm.watsonhealth.fhir.model.type.Xhtml.xhtml;
import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.isPrimitiveType;
import static com.ibm.watsonhealth.fhir.model.util.XMLSupport.FHIR_NS_URI;
import static com.ibm.watsonhealth.fhir.model.util.XMLSupport.createStreamWriterDelegate;

import java.io.OutputStream;
import java.io.StringReader;
import java.io.Writer;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.UUID;

import javax.xml.XMLConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stax.StAXResult;
import javax.xml.transform.stream.StreamSource;

import com.ibm.watsonhealth.fhir.model.format.Format;
import com.ibm.watsonhealth.fhir.model.generator.exception.FHIRGeneratorException;
import com.ibm.watsonhealth.fhir.model.resource.Patient;
import com.ibm.watsonhealth.fhir.model.resource.Resource;
import com.ibm.watsonhealth.fhir.model.type.Base64Binary;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
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
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.NarrativeStatus;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Time;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.type.Xhtml;
import com.ibm.watsonhealth.fhir.model.util.XMLSupport.StreamWriterDelegate;


public class FHIRXMLGenerator implements FHIRGenerator {
    private final boolean prettyPrinting;
    
    protected FHIRXMLGenerator() {
        this(false);
    }
    
    protected FHIRXMLGenerator(boolean prettyPrinting) {
        this.prettyPrinting = prettyPrinting;
    }

    @Override
    public void generate(Resource resource, OutputStream out) throws FHIRGeneratorException {
        GeneratingVisitor visitor = null;
        try (StreamWriterDelegate delegate = createStreamWriterDelegate(out)) {
            visitor = new XMLGeneratingVisitor(delegate, prettyPrinting);
            delegate.setDefaultNamespace(FHIR_NS_URI);
            resource.accept(visitor);
            delegate.flush();
        } catch (Exception e) {
            throw new FHIRGeneratorException(e.getMessage(), (visitor != null) ? visitor.getPath() : null, e);
        }
    }

    @Override
    public void generate(Resource resource, Writer writer) throws FHIRGeneratorException {
        GeneratingVisitor visitor = null;
        try (StreamWriterDelegate delegate = createStreamWriterDelegate(writer)) {
            visitor = new XMLGeneratingVisitor(delegate, prettyPrinting);
            delegate.setDefaultNamespace(FHIR_NS_URI);
            resource.accept(visitor);
            delegate.flush();
        } catch (Exception e) {
            throw new FHIRGeneratorException(e.getMessage(), (visitor != null) ? visitor.getPath() : null, e);
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
    
    private static class XMLGeneratingVisitor extends GeneratingVisitor {
        private final XMLStreamWriter writer;
        private final boolean prettyPrinting;
        
        private static final TransformerFactory TRANSFORMER_FACTORY = createTransformerFactory();
        private static final ThreadLocal<Transformer> THREAD_LOCAL_TRANSFORMER = new ThreadLocal<Transformer>() {
            public Transformer initialValue() {
                return createTransformer();
            }
        };
        
        private int indentLevel = 0;
                
        private XMLGeneratingVisitor(XMLStreamWriter writer, boolean prettyPrinting) {
            this.writer = writer;
            this.prettyPrinting = prettyPrinting;
        }
        
        private static Transformer createTransformer() {
            try {
                return TRANSFORMER_FACTORY.newTransformer();
            } catch (TransformerConfigurationException e) {
                throw new Error(e);
            }
        }

        private static TransformerFactory createTransformerFactory() {
            try {
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
                return transformerFactory;
            } catch (Exception e) {
                throw new Error(e);
            }
        }
        
        private void indent() {
            if (prettyPrinting) {
                for (int i = 0; i < indentLevel; i++) {
                    try {
                        writer.writeCharacters("  ");
                    } catch (XMLStreamException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        
        private void newLine() {
            if (prettyPrinting) {
                try {
                    writer.writeCharacters(System.lineSeparator());
                } catch (XMLStreamException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        private void writeAttributes(Element element) {
            try {
                Class<?> elementType = element.getClass();
                if (element.getId() != null) {
                    writer.writeAttribute("id", element.getId());
                }
                if (element instanceof Extension) {
                    Extension extension = (Extension) element;
                    if (extension.getUrl() != null) {
                        writer.writeAttribute("url", extension.getUrl());
                    }
                } else if (isPrimitiveType(elementType)) {
                    writeValue(element);
                }
            } catch (XMLStreamException e) {
                throw new RuntimeException(e);
            }
        }

        private void writeValue(Element element) throws XMLStreamException {
            java.lang.String value = null;
            if (element instanceof Base64Binary) {
                Base64Binary base64Binary  = (Base64Binary) element;
                if (base64Binary.getValue() != null) {
                    value = Base64.getEncoder().encodeToString(base64Binary.getValue());
                }
            } else if (element instanceof Boolean) {
                Boolean _boolean = (Boolean) element;
                if (_boolean.getValue() != null) {
                    value = _boolean.getValue().toString();
                }
            } else if (element instanceof Date) {
                Date date = (Date) element;
                if (date.getValue() != null) {
                    value = Date.PARSER_FORMATTER.format(date.getValue());
                }
            } else if (element instanceof DateTime) {
                DateTime dateTime = (DateTime) element;
                if (dateTime.getValue() != null) {
                    value = DateTime.PARSER_FORMATTER.format(dateTime.getValue());
                }
            } else if (element instanceof Decimal) {
                Decimal decimal = (Decimal) element;
                if (decimal.getValue() != null) {
                    value = decimal.getValue().toString();
                }
            } else if (element instanceof Instant) {
                Instant instant = (Instant) element;
                if (instant.getValue() != null) {
                    value = Instant.PARSER_FORMATTER.format(instant.getValue());
                }
            } else if (element instanceof Integer) {
                Integer integer = (Integer) element;
                if (integer.getValue() != null) {
                    value = integer.getValue().toString();
                }
            } else if (element instanceof String) {
                String string = (String) element;
                if (string.getValue() != null) {
                    value = string.getValue();
                }
            } else if (element instanceof Time) {
                Time time = (Time) element;
                if (time.getValue() != null) {
                    value = Time.PARSER_FORMATTER.format(time.getValue());
                }
            } else if (element instanceof Uri) {
                Uri uri = (Uri) element;
                if (uri.getValue() != null) {
                    value = uri.getValue();
                }
            }
            if (value != null) {
                writer.writeAttribute("value", value);
            }
        }
        
        private void writeXhtml(java.lang.String elementName, Xhtml xhtml) {
            try {
                Transformer transformer = THREAD_LOCAL_TRANSFORMER.get();
                transformer.reset();
                transformer.transform(new StreamSource(new StringReader(xhtml.getValue())), new StAXResult(writer));
            } catch (TransformerException e) {
                throw new RuntimeException(e);
            }
        }
        
        @Override
        public void doVisitEnd(java.lang.String elementName, int elementIndex, Element element) {
            try {
                indentLevel--;
                Class<?> elementType = element.getClass();
                if (!isPrimitiveType(elementType) || !element.getExtension().isEmpty()) {
                    indent();
                    writer.writeEndElement();
                    newLine();
                }
            } catch (XMLStreamException e) {
                throw new RuntimeException(e);
            }
        }
        
        @Override
        public void doVisitEnd(java.lang.String elementName, int elementIndex, Resource resource) {
            try {
                if (getDepth() > 1) {
                    indentLevel--;
                    indent();
                    writer.writeEndElement();
                    newLine();
                }
                indentLevel--;
                indent();
                writer.writeEndElement();
                if (getDepth() > 1) {
                    newLine();
                }
            } catch (XMLStreamException e) {
                throw new RuntimeException(e);
            }
        }
        
        @Override
        public void doVisitStart(java.lang.String elementName, int elementIndex, Element element) {
            try {
                indent();
                Class<?> elementType = element.getClass();
                if (isChoiceElement(elementName)) {
                    elementName = getChoiceElementName(elementName, element.getClass());
                }
                if (!isPrimitiveType(elementType) || !element.getExtension().isEmpty()) {
                    writer.writeStartElement(FHIR_NS_URI, elementName);
                } else if (Xhtml.class.equals(elementType)) {
                    writeXhtml(elementName, (Xhtml) element);
                } else {
                    writer.writeEmptyElement(FHIR_NS_URI, elementName);
                }
                writeAttributes(element);
                newLine();
                indentLevel++;
            } catch (XMLStreamException e) {
                throw new RuntimeException(e);
            }
        }
    
        @Override
        public void doVisitStart(java.lang.String elementName, int elementIndex, Resource resource) {
            try {
                int depth = getDepth();
                if (depth > 1) {
                    indent();
                    writer.writeStartElement(elementName);
                    newLine();
                    indentLevel++;
                }
                indent();
                Class<?> resourceType = resource.getClass();
                java.lang.String resourceTypeName = resourceType.getSimpleName();
                writer.writeStartElement(FHIR_NS_URI, resourceTypeName);
                if (depth == 1) {
                    writer.writeDefaultNamespace(FHIR_NS_URI);
                }
                newLine();
                indentLevel++;
            } catch (XMLStreamException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    public static void main(java.lang.String[] args) throws Exception {
        java.lang.String div = "<div xmlns=\"http://www.w3.org/1999/xhtml\"><p><b>Generated Narrative</b></p></div>";
                
        Id id = Id.builder().value(UUID.randomUUID().toString())
                .extension(Extension.builder()
                    .url("http://www.ibm.com/someExtension")
                    .value(String.of("Hello, World!"))
                    .build())
                .build();
        
        Meta meta = Meta.builder().versionId(Id.of("1"))
                .lastUpdated(Instant.now(ZoneOffset.UTC))
                .build();
        
        String given = String.builder().value("John")
                .extension(Extension.builder()
                    .url("http://www.ibm.com/someExtension")
                    .value(String.of("value and extension"))
                    .build())
                .build();
        
        String otherGiven = String.builder()
                .extension(Extension.builder()
                    .url("http://www.ibm.com/someExtension")
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
        
        Narrative text = Narrative.builder().status(NarrativeStatus.GENERATED).div(xhtml(div)).build();
        
        Patient patient = Patient.builder()
                .id(id)
                .text(text)
                .active(Boolean.TRUE)
                .multipleBirth(Integer.of(2))
                .meta(meta)
                .name(name)
                .birthDate(Date.of(LocalDate.now()))
                .build();
    
        FHIRGenerator.generator(Format.XML, true).generate(patient, System.out);
    }
}
