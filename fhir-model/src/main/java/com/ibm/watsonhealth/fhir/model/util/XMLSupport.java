/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.util;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public final class XMLSupport {
    public static final XMLInputFactory XML_INPUT_FACTORY = createXMLInputFactory();
    public static final XMLOutputFactory XML_OUTPUT_FACTORY = XMLOutputFactory.newInstance();
    public static final java.lang.String FHIR_NS_URI = "http://hl7.org/fhir";
    public static final String XHTML_NS_URI = "http://www.w3.org/1999/xhtml";

    private XMLSupport() { }
    
    private static XMLInputFactory createXMLInputFactory() {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        return factory;
    }
    
    public static void requireNamespace(XMLStreamReader reader, String namespaceURI) throws XMLStreamException {
        if (reader instanceof StreamReaderDelegate) {
            StreamReaderDelegate delegate = (StreamReaderDelegate) reader;
            delegate.require(XMLStreamReader.START_ELEMENT, namespaceURI, null);
        }
    }

    public static String parseDiv(XMLStreamReader reader) throws XMLStreamException {
        StringWriter sw = new StringWriter();
        XMLStreamWriter writer = createStreamWriterDelegate(XML_OUTPUT_FACTORY.createXMLStreamWriter(sw));
        writeStartElement(reader, writer);
        while (reader.hasNext()) {
            int eventType = reader.next();
            switch (eventType) {
            case XMLStreamReader.START_ELEMENT:
                writeStartElement(reader, writer);
                break;
            case XMLStreamReader.SPACE:
            case XMLStreamReader.CHARACTERS:
                writer.writeCharacters(reader.getTextCharacters(), reader.getTextStart(), reader.getTextLength());
                break;             
            case XMLStreamReader.END_ELEMENT:
                writer.writeEndElement();
                if ("div".equals(reader.getLocalName())) {
                    writer.flush();
                    writer.close();
                    return sw.toString();
                }
                break;
            }
        }
        throw new XMLStreamException("Unexpected end of stream");
    }

    private static void writeStartElement(XMLStreamReader reader, XMLStreamWriter writer) throws XMLStreamException {
        String prefix = reader.getPrefix();
        String namespaceURI = reader.getNamespaceURI();
        String localName = reader.getLocalName();
        if (namespaceURI != null) {
            if (prefix != null) {
                writer.writeStartElement(prefix, localName, namespaceURI);
            } else {
                writer.setDefaultNamespace(namespaceURI);
                writer.writeStartElement(namespaceURI, localName);
            }
        } else {
            writer.writeStartElement(localName);
        }
        writeNamespaces(reader, writer);
        writeAttributes(reader, writer);
    }

    private static void writeNamespaces(XMLStreamReader reader, XMLStreamWriter writer) throws XMLStreamException {
        for (int i = 0; i < reader.getNamespaceCount(); i++) {
            writer.writeNamespace(reader.getNamespacePrefix(i), reader.getNamespaceURI(i));
        }
    }

    private static void writeAttributes(XMLStreamReader reader, XMLStreamWriter writer) throws XMLStreamException {
        for (int i = 0; i < reader.getAttributeCount(); i++) {
            String prefix = reader.getAttributePrefix(i);
            String namespaceURI = reader.getAttributeNamespace(i);
            String localName = reader.getAttributeLocalName(i);
            String value = reader.getAttributeValue(i);
            if (namespaceURI != null) {
                if (prefix != null) {
                    writer.writeAttribute(prefix, namespaceURI, localName, value);
                } else {
                    writer.writeAttribute(namespaceURI, localName, value);
                }
            } else {
                writer.writeAttribute(localName, value);
            }
        }
    }

    public static StreamReaderDelegate createStreamReaderDelegate(XMLStreamReader reader) {
        return new StreamReaderDelegate(reader);
    }

    public static StreamWriterDelegate createStreamWriterDelegate(XMLStreamWriter writer) {
        return new StreamWriterDelegate(writer) {
            @Override
            public void writeStartDocument() throws XMLStreamException {
                // do nothing
            }
    
            @Override
            public void writeStartDocument(java.lang.String version) throws XMLStreamException {
                // do nothing
            }
    
            @Override
            public void writeStartDocument(java.lang.String encoding, java.lang.String version) throws XMLStreamException {
                // do nothing
            }
            
            @Override
            public void writeEndDocument() {
                // do nothing
            }
        };
    }

    public static class StreamReaderDelegate extends javax.xml.stream.util.StreamReaderDelegate implements AutoCloseable {
        public StreamReaderDelegate(XMLStreamReader reader) {
            super(reader);
        }
    }

    public static class StreamWriterDelegate implements XMLStreamWriter, AutoCloseable {
        protected final XMLStreamWriter writer;
                
        public StreamWriterDelegate(XMLStreamWriter writer) {
            this.writer = writer;
        }
        
        @Override
        public void writeStartElement(java.lang.String localName) throws XMLStreamException {
            writer.writeStartElement(localName);
        }
    
        @Override
        public void writeStartElement(java.lang.String namespaceURI, java.lang.String localName) throws XMLStreamException {
            writer.writeStartElement(namespaceURI, localName);
        }
    
        @Override
        public void writeStartElement(java.lang.String prefix, java.lang.String localName, java.lang.String namespaceURI) throws XMLStreamException {
            writer.writeStartElement(prefix, localName, namespaceURI);
        }
    
        @Override
        public void writeEmptyElement(java.lang.String namespaceURI, java.lang.String localName) throws XMLStreamException {
            writer.writeEmptyElement(namespaceURI, localName);
        }
    
        @Override
        public void writeEmptyElement(java.lang.String prefix, java.lang.String localName, java.lang.String namespaceURI) throws XMLStreamException {
            writer.writeEmptyElement(prefix, localName, namespaceURI);
        }
    
        @Override
        public void writeEmptyElement(java.lang.String localName) throws XMLStreamException {
            writer.writeEmptyElement(localName);
        }
    
        @Override
        public void writeEndElement() throws XMLStreamException {
            writer.writeEndElement();
        }
    
        @Override
        public void writeEndDocument() throws XMLStreamException {
            writer.writeEndDocument();
        }
    
        @Override
        public void close() throws XMLStreamException {
            writer.close();
        }
    
        @Override
        public void flush() throws XMLStreamException {
            writer.flush();
        }
    
        @Override
        public void writeAttribute(java.lang.String localName, java.lang.String value) throws XMLStreamException {
            writer.writeAttribute(localName, value);
        }
    
        @Override
        public void writeAttribute(java.lang.String prefix, java.lang.String namespaceURI, java.lang.String localName, java.lang.String value) throws XMLStreamException {
            writer.writeAttribute(prefix, namespaceURI, localName, value);
        }
    
        @Override
        public void writeAttribute(java.lang.String namespaceURI, java.lang.String localName, java.lang.String value) throws XMLStreamException {
            writer.writeAttribute(namespaceURI, localName, value);
        }
    
        @Override
        public void writeNamespace(java.lang.String prefix, java.lang.String namespaceURI) throws XMLStreamException {
            writer.writeNamespace(prefix, namespaceURI);
        }
    
        @Override
        public void writeDefaultNamespace(java.lang.String namespaceURI) throws XMLStreamException {
            writer.writeDefaultNamespace(namespaceURI);
        }
    
        @Override
        public void writeComment(java.lang.String data) throws XMLStreamException {
            writer.writeComment(data);
        }
    
        @Override
        public void writeProcessingInstruction(java.lang.String target) throws XMLStreamException {
            writer.writeProcessingInstruction(target);
        }
    
        @Override
        public void writeProcessingInstruction(java.lang.String target, java.lang.String data) throws XMLStreamException {
            writer.writeProcessingInstruction(target, data);
        }
    
        @Override
        public void writeCData(java.lang.String data) throws XMLStreamException {
            writer.writeCData(data);
        }
    
        @Override
        public void writeDTD(java.lang.String dtd) throws XMLStreamException {
            writer.writeDTD(dtd);
        }
    
        @Override
        public void writeEntityRef(java.lang.String name) throws XMLStreamException {
            writer.writeEntityRef(name);
        }
    
        @Override
        public void writeStartDocument() throws XMLStreamException {
            writer.writeStartDocument();
        }
    
        @Override
        public void writeStartDocument(java.lang.String version) throws XMLStreamException {
            writer.writeStartDocument(version);
        }
    
        @Override
        public void writeStartDocument(java.lang.String encoding, java.lang.String version) throws XMLStreamException {
            writer.writeStartDocument(encoding, version);
        }
    
        @Override
        public void writeCharacters(java.lang.String text) throws XMLStreamException {
            writer.writeCharacters(text);
        }
    
        @Override
        public void writeCharacters(char[] text, int start, int len) throws XMLStreamException {
            writer.writeCharacters(text, start, len);
        }
    
        @Override
        public java.lang.String getPrefix(java.lang.String uri) throws XMLStreamException {
            return writer.getPrefix(uri);
        }
    
        @Override
        public void setPrefix(java.lang.String prefix, java.lang.String uri) throws XMLStreamException {
            writer.setPrefix(prefix, uri);
        }
    
        @Override
        public void setDefaultNamespace(java.lang.String uri) throws XMLStreamException {
            writer.setDefaultNamespace(uri);
        }
    
        @Override
        public void setNamespaceContext(NamespaceContext context) throws XMLStreamException {
            writer.setNamespaceContext(context);
        }
    
        @Override
        public NamespaceContext getNamespaceContext() {
            return writer.getNamespaceContext();
        }
    
        @Override
        public Object getProperty(java.lang.String name) throws IllegalArgumentException {
            return writer.getProperty(name);
        }
    }
    
    public static void main(String[] args) throws Exception {
        String div = "<div xmlns=\"http://www.w3.org/1999/xhtml\"><p><b>Generated Narrative</b></p></div>";
        XMLStreamReader reader = XML_INPUT_FACTORY.createXMLStreamReader(new StringReader(div));
        reader.next();
        System.out.println(parseDiv(reader));
        
        div = "<h:div xmlns:h=\"http://www.w3.org/1999/xhtml\"><h:p><h:b>Generated Narrative</h:b></h:p></h:div>";
        reader = XML_INPUT_FACTORY.createXMLStreamReader(new StringReader(div));
        reader.next();
        System.out.println(parseDiv(reader));
    }
}
