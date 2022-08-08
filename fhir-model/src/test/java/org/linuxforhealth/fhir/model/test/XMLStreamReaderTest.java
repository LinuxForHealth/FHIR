/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.test;

import static org.linuxforhealth.fhir.model.type.String.string;
import static org.linuxforhealth.fhir.model.type.Xhtml.xhtml;

import java.io.StringReader;
import java.io.StringWriter;
import java.time.ZoneOffset;
import java.util.UUID;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.generator.FHIRGenerator;
import org.linuxforhealth.fhir.model.resource.Patient;
import org.linuxforhealth.fhir.model.type.HumanName;
import org.linuxforhealth.fhir.model.type.Id;
import org.linuxforhealth.fhir.model.type.Instant;
import org.linuxforhealth.fhir.model.type.Meta;
import org.linuxforhealth.fhir.model.type.Narrative;
import org.linuxforhealth.fhir.model.type.code.NarrativeStatus;
import org.linuxforhealth.fhir.model.util.XMLSupport;

public class XMLStreamReaderTest {    
    public static void main(java.lang.String[] args) throws Exception {
        String div = "<div xmlns=\"http://www.w3.org/1999/xhtml\"><p><b>Generated Narrative</b></p></div>";
        
        String id = UUID.randomUUID().toString();
        
        Meta meta = Meta.builder().versionId(Id.of("1"))
                .lastUpdated(Instant.now(ZoneOffset.UTC))
                .build();
        
        HumanName name = HumanName.builder()
                .given(string("John"))
                .family(string("Doe"))
                .build();
        
        Narrative text = Narrative.builder().status(NarrativeStatus.GENERATED).div(xhtml(div)).build();
        
        Patient patient = Patient.builder()
                .id(id)
                .meta(meta)
                .text(text)
                .name(name)
                .build();
    
        StringWriter writer = new StringWriter();
        FHIRGenerator.generator(Format.XML, true).generate(patient, writer);
        java.lang.String result = writer.toString();
        System.out.println(result);
        
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(new StringReader(result));
        
        parse(reader);
    }

    private static void parse(XMLStreamReader reader) throws XMLStreamException {
        while (reader.hasNext()) {
            int eventType = reader.next();
            debug(reader);
            switch (eventType) {
            case XMLStreamReader.START_ELEMENT:
                parseResource(reader);
                return;
            }
        }
    }

    private static void parseResource(XMLStreamReader reader) throws XMLStreamException {
        while (reader.hasNext()) {
            debug(reader);
            switch (reader.getEventType()) {
            case XMLStreamReader.START_ELEMENT:
                java.lang.String localName = reader.getLocalName();
                System.out.println("localName: " + localName);
                switch (localName) {
                case "Patient":
                    parsePatient("Patient", reader);
                    break;
                }
                break;
            case XMLStreamReader.END_ELEMENT:
                return;
            }
        }
    }

    private static void parsePatient(java.lang.String elementName, XMLStreamReader reader) throws XMLStreamException {
        while (reader.hasNext()) {
            int eventType = reader.next();
            debug(reader);

            switch (eventType) {
            case XMLStreamReader.START_ELEMENT:
                java.lang.String localName = reader.getLocalName();
                System.out.println("localName: " + localName);
                switch (localName) {
                case "id":
                    parseId("id", reader);
                    break;
                case "meta":
                    parseMeta("meta", reader);
                    break;
                case "text":
                    parseNarrative("text", reader);
                    break;
                case "name":
                    parseHumanName("name", reader);
                    break;
                }
                break;
            case XMLStreamReader.END_ELEMENT:
                if (reader.getLocalName().equals(elementName)) {
                    return;
                }
                break;
            }
        }        
    }

    private static void parseId(java.lang.String elementName, XMLStreamReader reader) throws XMLStreamException {
        java.lang.String value = reader.getAttributeValue(null, "value");
        System.out.println("    value: " + value);
        
        while (reader.hasNext()) {
            int eventType = reader.next();
            debug(reader);

            switch (eventType) {
            case XMLStreamReader.START_ELEMENT:
                java.lang.String localName = reader.getLocalName();
                System.out.println("localName: " + localName);
                switch (localName) {
                    //
                }
                break;
            case XMLStreamReader.END_ELEMENT:
                if (reader.getLocalName().equals(elementName)) {
                    return;
                }
                break;
            }
        }
    }

    private static void parseMeta(java.lang.String elementName, XMLStreamReader reader) throws XMLStreamException {
        while (reader.hasNext()) {
            int eventType = reader.next();
            debug(reader);

            switch (eventType) {
            case XMLStreamReader.START_ELEMENT:
                java.lang.String localName = reader.getLocalName();
                System.out.println("localName: " + localName);
                switch (localName) {
                case "versionId":
                    parseString("versionId", reader);
                    break;
                case "lastUpdated":
                    parseInstant("lastUpdated", reader);
                    break;
                }
                break;
            case XMLStreamReader.END_ELEMENT:
                if (reader.getLocalName().equals(elementName)) {
                    return;
                }
                break;
            }
        }
    }

    private static void parseString(java.lang.String elementName, XMLStreamReader reader) throws XMLStreamException {
        java.lang.String value = reader.getAttributeValue(null, "value");
        System.out.println("    value: " + value);
        
        while (reader.hasNext()) {
            int eventType = reader.next();
            debug(reader);

            switch (eventType) {
            case XMLStreamReader.START_ELEMENT:
                java.lang.String localName = reader.getLocalName();
                System.out.println("localName: " + localName);
                switch (localName) {
                    //
                }
                break;
            case XMLStreamReader.END_ELEMENT:
                if (reader.getLocalName().equals(elementName)) {
                    return;
                }
                break;
            }
        }        
    }

    private static void parseInstant(java.lang.String elementName, XMLStreamReader reader) throws XMLStreamException {
        java.lang.String value = reader.getAttributeValue(null, "value");
        System.out.println("    value: " + value);
        
        while (reader.hasNext()) {
            int eventType = reader.next();
            debug(reader);

            switch (eventType) {
            case XMLStreamReader.START_ELEMENT:
                java.lang.String localName = reader.getLocalName();
                switch (localName) {
                    //
                }
                break;
            case XMLStreamReader.END_ELEMENT:
                if (reader.getLocalName().equals(elementName)) {
                    return;
                }
                break;
            }
        }        
    }

    private static void parseNarrative(java.lang.String elementName, XMLStreamReader reader) throws XMLStreamException {        
        while (reader.hasNext()) {
            int eventType = reader.next();
            debug(reader);

            switch (eventType) {
            case XMLStreamReader.START_ELEMENT:
                java.lang.String localName = reader.getLocalName();
                switch (localName) {
                case "status":
                    parseString("status", reader);
                    break;
                case "div":
                    java.lang.String div = XMLSupport.parseDiv(reader);
                    System.out.println("    div: " + div);
                    break;
                }
                break;
            case XMLStreamReader.END_ELEMENT:
                if (reader.getLocalName().equals(elementName)) {
                    return;
                }
                break;
            }
        }        
    }


    private static void parseHumanName(java.lang.String elementName, XMLStreamReader reader) throws XMLStreamException {
        java.lang.String value = reader.getAttributeValue(null, "value");
        System.out.println("    value: " + value);
        
        while (reader.hasNext()) {
            int eventType = reader.next();
            debug(reader);

            switch (eventType) {
            case XMLStreamReader.START_ELEMENT:
                java.lang.String localName = reader.getLocalName();
                System.out.println("localName: " + localName);
                switch (localName) {
                case "family":
                    parseString("family", reader);
                    break;
                case "given":
                    parseString("given", reader);
                    break;
                }
                break;
            case XMLStreamReader.END_ELEMENT:
                if (reader.getLocalName().equals(elementName)) {
                    return;
                }
                break;
            }
        }         
    }
    
    private static void debug(XMLStreamReader reader) throws XMLStreamException {
        int eventType = reader.getEventType();
        switch (eventType) {
        case XMLStreamReader.START_DOCUMENT:
            System.out.println("START_DOCUMENT");
            break;
        case XMLStreamReader.START_ELEMENT:
            System.out.println("START_ELEMENT: " + reader.getLocalName());
            break;
        case XMLStreamReader.SPACE:
            System.out.println("SPACE");
            break;
        case XMLStreamReader.CHARACTERS:
            System.out.println("CHARACTERS");
            break;
        case XMLStreamReader.END_ELEMENT:
            System.out.println("END_ELEMENT: " + reader.getLocalName());
            break;
        case XMLStreamReader.END_DOCUMENT:
            System.out.println("END_DOCUMENT");
            break;
        default:
            System.out.println("OTHER: " + eventType);
            break;
        }
    }
}
