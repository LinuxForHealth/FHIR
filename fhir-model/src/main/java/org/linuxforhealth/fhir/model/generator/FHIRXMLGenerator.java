/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.generator;

import static org.linuxforhealth.fhir.model.util.ModelSupport.isPrimitiveType;
import static org.linuxforhealth.fhir.model.util.XMLSupport.FHIR_NS_URI;
import static org.linuxforhealth.fhir.model.util.XMLSupport.createNonClosingStreamWriterDelegate;
import static org.linuxforhealth.fhir.model.util.XMLSupport.createStreamWriterDelegate;
import static org.linuxforhealth.fhir.model.util.XMLSupport.createTransformer;

import java.io.OutputStream;
import java.io.StringReader;
import java.io.Writer;
import java.util.Base64;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stax.StAXResult;
import javax.xml.transform.stream.StreamSource;

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
import org.linuxforhealth.fhir.model.util.XMLSupport.StreamWriterDelegate;
import org.linuxforhealth.fhir.model.visitor.Visitable;

public class FHIRXMLGenerator extends FHIRAbstractGenerator {
    private static final int DEFAULT_INDENT_AMOUNT = 2;

    protected FHIRXMLGenerator(boolean prettyPrinting) {
        super(prettyPrinting);
    }

    @Override
    public void generate(Visitable visitable, OutputStream out) throws FHIRGeneratorException {
        GeneratingVisitor visitor = null;
        try (StreamWriterDelegate delegate = createStreamWriterDelegate(out)) {
            int indentAmount = getPropertyOrDefault(FHIRGenerator.PROPERTY_INDENT_AMOUNT, DEFAULT_INDENT_AMOUNT, java.lang.Integer.class);
            visitor = new XMLGeneratingVisitor(delegate, prettyPrinting, indentAmount);
            delegate.setDefaultNamespace(FHIR_NS_URI);
            visitable.accept(visitor);
            delegate.flush();
        } catch (Exception e) {
            throw new FHIRGeneratorException(e.getMessage(), (visitor != null) ? visitor.getPath() : null, e);
        }
    }

    @Override
    public void generate(Visitable visitable, Writer writer) throws FHIRGeneratorException {
        GeneratingVisitor visitor = null;
        try (StreamWriterDelegate delegate = createStreamWriterDelegate(writer)) {
            int indentAmount = getPropertyOrDefault(FHIRGenerator.PROPERTY_INDENT_AMOUNT, DEFAULT_INDENT_AMOUNT, java.lang.Integer.class);
            visitor = new XMLGeneratingVisitor(delegate, prettyPrinting, indentAmount);
            delegate.setDefaultNamespace(FHIR_NS_URI);
            visitable.accept(visitor);
            delegate.getWriter().writeEndDocument();
            delegate.flush();
        } catch (Exception e) {
            throw new FHIRGeneratorException(e.getMessage(), (visitor != null) ? visitor.getPath() : null, e);
        }
    }

    @Override
    public boolean isPropertySupported(java.lang.String name) {
        if (FHIRGenerator.PROPERTY_INDENT_AMOUNT.equals(name)) {
            return true;
        }
        return false;
    }

    private static class XMLGeneratingVisitor extends GeneratingVisitor {
        private final XMLStreamWriter writer;
        private final boolean prettyPrinting;
        private final int indentAmount;

        private int indentLevel = 0;

        private XMLGeneratingVisitor(XMLStreamWriter writer, boolean prettyPrinting, int indentAmount) {
            this.writer = writer;
            this.prettyPrinting = prettyPrinting;
            this.indentAmount = indentAmount;
            if (this.indentAmount < 0) {
                throw new IllegalStateException("Indent amount must be a non-negative number");
            }
        }

        private void indent() {
            if (prettyPrinting) {
                for (int i = 0; i < indentLevel; i++) {
                    try {
                        for (int j = 0; j < indentAmount; j++) {
                            writer.writeCharacters(" ");
                        }
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
                Transformer transformer = createTransformer();
                transformer.reset();
                transformer.transform(new StreamSource(new StringReader(xhtml.getValue())), new StAXResult(createNonClosingStreamWriterDelegate(writer)));
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
                if (resource.getId() != null) {
                    indent();
                    writer.writeEmptyElement("id");
                    writer.writeAttribute("value", resource.getId());
                    newLine();
                }
            } catch (XMLStreamException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
