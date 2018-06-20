/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.xml.XMLConstants;
import javax.xml.bind.Binder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.persistence.jaxb.JAXBContextProperties;
import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.w3c.dom.Node;

import com.ibm.watsonhealth.fhir.core.MediaType;
import com.ibm.watsonhealth.fhir.exception.FHIRException;
import com.ibm.watsonhealth.fhir.exception.FHIRInvalidResourceTypeException;
import com.ibm.watsonhealth.fhir.exception.FHIROperationException;
import com.ibm.watsonhealth.fhir.model.Address;
import com.ibm.watsonhealth.fhir.model.AddressUse;
import com.ibm.watsonhealth.fhir.model.AddressUseList;
import com.ibm.watsonhealth.fhir.model.Attachment;
import com.ibm.watsonhealth.fhir.model.Basic;
import com.ibm.watsonhealth.fhir.model.CarePlanParticipant;
import com.ibm.watsonhealth.fhir.model.CarePlanStatus;
import com.ibm.watsonhealth.fhir.model.CarePlanStatusList;
import com.ibm.watsonhealth.fhir.model.Code;
import com.ibm.watsonhealth.fhir.model.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.Coding;
import com.ibm.watsonhealth.fhir.model.ConditionVerificationStatus;
import com.ibm.watsonhealth.fhir.model.ConditionVerificationStatusList;
import com.ibm.watsonhealth.fhir.model.ContactPoint;
import com.ibm.watsonhealth.fhir.model.ContactPointSystemList;
import com.ibm.watsonhealth.fhir.model.ContactPointUseList;
import com.ibm.watsonhealth.fhir.model.Date;
import com.ibm.watsonhealth.fhir.model.DateTime;
import com.ibm.watsonhealth.fhir.model.Decimal;
import com.ibm.watsonhealth.fhir.model.DomainResource;
import com.ibm.watsonhealth.fhir.model.Element;
import com.ibm.watsonhealth.fhir.model.Extension;
import com.ibm.watsonhealth.fhir.model.GoalStatus;
import com.ibm.watsonhealth.fhir.model.GoalStatusList;
import com.ibm.watsonhealth.fhir.model.HumanName;
import com.ibm.watsonhealth.fhir.model.Id;
import com.ibm.watsonhealth.fhir.model.Identifier;
import com.ibm.watsonhealth.fhir.model.IdentifierUse;
import com.ibm.watsonhealth.fhir.model.IdentifierUseList;
import com.ibm.watsonhealth.fhir.model.Instant;
import com.ibm.watsonhealth.fhir.model.Integer;
import com.ibm.watsonhealth.fhir.model.IssueSeverityList;
import com.ibm.watsonhealth.fhir.model.IssueTypeList;
import com.ibm.watsonhealth.fhir.model.Meta;
import com.ibm.watsonhealth.fhir.model.NameUse;
import com.ibm.watsonhealth.fhir.model.NameUseList;
import com.ibm.watsonhealth.fhir.model.NarrativeStatus;
import com.ibm.watsonhealth.fhir.model.NarrativeStatusList;
import com.ibm.watsonhealth.fhir.model.ObjectFactory;
import com.ibm.watsonhealth.fhir.model.ObservationComponent;
import com.ibm.watsonhealth.fhir.model.ObservationStatus;
import com.ibm.watsonhealth.fhir.model.ObservationStatusList;
import com.ibm.watsonhealth.fhir.model.OperationOutcome;
import com.ibm.watsonhealth.fhir.model.OperationOutcomeIssue;
import com.ibm.watsonhealth.fhir.model.PatientAnimal;
import com.ibm.watsonhealth.fhir.model.PatientCommunication;
import com.ibm.watsonhealth.fhir.model.PatientContact;
import com.ibm.watsonhealth.fhir.model.PatientLink;
import com.ibm.watsonhealth.fhir.model.Period;
import com.ibm.watsonhealth.fhir.model.Quantity;
import com.ibm.watsonhealth.fhir.model.Range;
import com.ibm.watsonhealth.fhir.model.Reference;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.model.ResourceContainer;
import com.ibm.watsonhealth.fhir.model.RiskAssessmentPrediction;
import com.ibm.watsonhealth.fhir.model.SimpleQuantity;
import com.ibm.watsonhealth.fhir.model.Time;
import com.ibm.watsonhealth.fhir.model.TimingRepeat;
import com.ibm.watsonhealth.fhir.model.UnitsOfTime;
import com.ibm.watsonhealth.fhir.model.UnitsOfTimeList;
import com.ibm.watsonhealth.fhir.model.Uri;
import com.ibm.watsonhealth.fhir.model.adapters.DivAdapter;

public class FHIRUtil {
	private static final String HL7_FHIR_NS_URI = "http://hl7.org/fhir";	
	private static final String DEFAULT_NS_PREFIX = "";
	private static final String XHTML_NS_PREFIX = "xhtml";
	private static final String XHTML_NS_URI = "http://www.w3.org/1999/xhtml";
	private static final String XML_FHIR_METADATA_SOURCE = "com/ibm/watsonhealth/fhir/model/xml-fhir-metadata.xml";	
	private static final String JSON_FHIR_METADATA_SOURCE = "com/ibm/watsonhealth/fhir/model/json-fhir-metadata.xml";
    private static final String NL = System.getProperty("line.separator");
    private static final String BASIC_RESOURCE_TYPE_URL = "http://ibm.com/watsonhealth/fhir/basic-resource-type";
	private static final String P_MAX_ATTRIBUTE_SIZE = "com.ctc.wstx.maxAttributeSize";
    
	public static enum Format {
		XML,
		JSON
	}
	
	private static final JAXBContext xmlContext = createContext(Format.XML);
	private static final JAXBContext jsonContext = createContext(Format.JSON);
	private static final ObjectFactory objectFactory = new ObjectFactory();
	private static final DatatypeFactory datatypeFactory = createDatatypeFactory();
	private static final DocumentBuilderFactory documentBuilderFactory = createDocumentBuilderFactory();
	private static final XMLInputFactory inputFactory = createInputFactory();
	
	private static final ThreadLocal<FHIRJsonParser> threadLocalFHIRJsonParser = new ThreadLocal<FHIRJsonParser>() {
	    @Override
	    protected FHIRJsonParser initialValue() {
	        return FHIRJsonParser.createLenientParser();
	    }
	};
	
	private static final ThreadLocal<FHIRJsonGenerator> threadLocalFHIRJsonGenerator = new ThreadLocal<FHIRJsonGenerator>() {
	    @Override
	    protected FHIRJsonGenerator initialValue() {
	        return new FHIRJsonGenerator();
	    }
	};
	
	private FHIRUtil() { }
	
	public static void init() {
	    // allows us to initialize this class during startup
	}
	
	private static DocumentBuilderFactory createDocumentBuilderFactory() {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
			return factory;
		} catch (ParserConfigurationException e) {
			throw new Error(e);
		}
	}
	
	private static DatatypeFactory createDatatypeFactory() {
		try {
			return DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			throw new Error(e);
		}
	}
	
	private static XMLInputFactory createInputFactory() {
	    try {
	        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
	        inputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
	        inputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
	        if (inputFactory.isPropertySupported(P_MAX_ATTRIBUTE_SIZE)) {
	            inputFactory.setProperty(P_MAX_ATTRIBUTE_SIZE, "10000000");
	        }
	        return inputFactory;
	    } catch (Exception e) {
	        throw new Error(e);
	    }
	}

	private static JAXBContext createContext(Format format) {
		try {		
			Map<String, Object> properties = new HashMap<String, Object>();
			String metadataSource = null;
			if (Format.XML.equals(format)) {
				// XML-specific configuration
				properties.put(JAXBContextProperties.MEDIA_TYPE, MediaType.APPLICATION_XML);
				metadataSource = XML_FHIR_METADATA_SOURCE;
			} else {
				// JSON-specific configuration
				properties.put(JAXBContextProperties.MEDIA_TYPE, MediaType.APPLICATION_JSON);
				properties.put(JAXBContextProperties.JSON_INCLUDE_ROOT, false);
				metadataSource = JSON_FHIR_METADATA_SOURCE;
			}
			// common configuration
			properties.put(JAXBContextProperties.OXM_METADATA_SOURCE, metadataSource);
			return JAXBContext.newInstance("com.ibm.watsonhealth.fhir.model", ObjectFactory.class.getClassLoader(), properties);
		} catch (JAXBException e) {
			throw new Error(e);
		}
	}
	
	private static JAXBContext getContext(Format format) {
		return Format.XML.equals(format) ? xmlContext : jsonContext;
	}
	
	public static <T extends Resource> Binder<Node> createBinder(T resource) {
		Binder<Node> binder = getContext(Format.XML).createBinder();
		try {
		    DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			binder.marshal(wrap(resource), documentBuilder.newDocument());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return binder;
	}
	
	@SuppressWarnings("unchecked")
	private static <T extends Resource> JAXBElement<T> wrap(T resource) {
		try {
			Class<? extends Resource> resourceType = resource.getClass();
			Method method = objectFactory.getClass().getDeclaredMethod("create" + resourceType.getSimpleName(), resourceType);
			return (JAXBElement<T>) method.invoke(objectFactory, resource);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Unmarshaller createUnmarshaller(Format format) throws JAXBException {
		JAXBContext context = getContext(format);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		configureUnmarshaller(unmarshaller, format);
		unmarshaller.setEventHandler(new FHIRSerializationEventHandler());
		return unmarshaller;
	}
	
	private static void configureUnmarshaller(Unmarshaller unmarshaller, Format format) throws JAXBException {
		unmarshaller.setEventHandler(new ValidationEventHandler() {
			@Override
			public boolean handleEvent(ValidationEvent event) {
				return false;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Resource> T read(Class<T> resourceType, Format format, InputStream stream) throws JAXBException {
		if (Format.XML.equals(format)) {
	        try {
				Unmarshaller unmarshaller = createUnmarshaller(format);
                return (T) unmarshaller.unmarshal(inputFactory.createXMLStreamReader(stream));
	        } catch (JAXBException e) {
	            throw e;
            } catch (Exception e) {
                throw new JAXBException(e);
            }
		} else {
		    // Format.JSON.equals(format)
		    try {
		        FHIRJsonParser parser = threadLocalFHIRJsonParser.get();
		        parser.reset();
		        return (T) parser.parse(stream);
		    } catch (FHIRException e) {
		        throw new JAXBException(e);
		    }
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Resource> T read(Class<T> resourceType, Format format, Reader reader) throws JAXBException {
		if (Format.XML.equals(format)) {
		    try {
		    		Unmarshaller unmarshaller = createUnmarshaller(format);
                return (T) unmarshaller.unmarshal(inputFactory.createXMLStreamReader(reader));
		    } catch (JAXBException e) {
		        throw e;
            } catch (Exception e) {
                throw new JAXBException(e);
            }
		} else {
		    // Format.JSON.equals(format)
            try {
                FHIRJsonParser parser = threadLocalFHIRJsonParser.get();
                parser.reset();
                return (T) parser.parse(reader);
            } catch (FHIRException e) {
                throw new JAXBException(e);
            }
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Resource> T read(Node node) throws JAXBException {
		Unmarshaller unmarshaller = createUnmarshaller(Format.XML);
		return (T) unmarshaller.unmarshal(node);
	}
	
	public static <T extends Resource> T toResource(Class<T> resourceType, JsonObject jsonObject) throws JAXBException {
        // write JsonObject to String
	    StringWriter writer = new StringWriter();
        Json.createWriter(writer).writeObject(jsonObject);
        String jsonString = writer.toString();
        
        // read Resource from String
        Unmarshaller unmarshaller = createUnmarshaller(Format.JSON);
        JAXBElement<T> jaxbElement = unmarshaller.unmarshal(new StreamSource(new StringReader(jsonString)), resourceType);
        return jaxbElement.getValue();
    }

    public static <T extends Element> T toElement(Class<T> elementType, JsonObject jsonObject) throws JAXBException {
        // write JsonObject to String
        StringWriter writer = new StringWriter();
        Json.createWriter(writer).writeObject(jsonObject);
        String jsonString = writer.toString();
        
        // read Element from String
        Unmarshaller unmarshaller = createUnmarshaller(Format.JSON);
        JAXBElement<T> jaxbElement = unmarshaller.unmarshal(new StreamSource(new StringReader(jsonString)), elementType);
        return jaxbElement.getValue();
    }

    private static Marshaller createMarshaller(Format format, boolean formatted) throws JAXBException {
		JAXBContext context = getContext(format);
		Marshaller marshaller = context.createMarshaller();
		configureMarshaller(marshaller, format, formatted);
		marshaller.setEventHandler(new FHIRSerializationEventHandler());
		return marshaller;
	}
	
	public static ConditionVerificationStatus conditionVerificationStatus(String s) {
		return objectFactory.createConditionVerificationStatus().withValue(ConditionVerificationStatusList.fromValue("confirmed"));
	}
	
	private static void configureMarshaller(Marshaller marshaller, Format format, boolean formatted) throws PropertyException   {
		// common configuration
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, formatted);
		if (formatted) {
		    marshaller.setProperty(MarshallerProperties.INDENT_STRING, "    ");
		}
		if (Format.XML.equals(format)) {
			// XML-specific configuration
			Map<String, String> namespacePrefixMap = new HashMap<String, String>();
			namespacePrefixMap.put(HL7_FHIR_NS_URI, DEFAULT_NS_PREFIX);	// default namespace
			namespacePrefixMap.put(XHTML_NS_URI, XHTML_NS_PREFIX);
			marshaller.setProperty(MarshallerProperties.NAMESPACE_PREFIX_MAPPER, namespacePrefixMap);
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
		} else {
		    // JSON-specific configuration
			marshaller.setProperty(MarshallerProperties.JSON_MARSHAL_EMPTY_COLLECTIONS, false);
		}
	}
	
	public static <T extends Resource> void write(T resource, Format format, OutputStream stream) throws JAXBException {
		write(resource, format, stream, true);
	}
	
    public static <T extends Resource> void write(T resource, Format format, OutputStream stream, boolean formatted) throws JAXBException {
        if (Format.XML.equals(format)) {
    			Marshaller marshaller = createMarshaller(format, formatted);
    			marshaller.marshal(resource, stream);
        } else {
        		// Format.JSON.equals(format)
        		try {
        			FHIRJsonGenerator generator = threadLocalFHIRJsonGenerator.get();
        			generator.setPrettyPrinting(formatted);
        			generator.generate(resource, stream);
        		} catch (FHIRException e) {
        			throw new JAXBException(e);
        		}
        }
    }
	
	public static <T extends Resource> void write(T resource, Format format, Writer writer) throws JAXBException {
		write(resource, format, writer, true);
	}
	
    public static <T extends Resource> void write(T resource, Format format, Writer writer, boolean formatted) throws JAXBException {
        if (Format.XML.equals(format)) {
    			Marshaller marshaller = createMarshaller(format, formatted);
    			marshaller.marshal(resource, writer);
        } else {
        		// Format.JSON.equals(format)
        		try {
        			FHIRJsonGenerator generator = threadLocalFHIRJsonGenerator.get();
        			generator.setPrettyPrinting(formatted);
        			generator.generate(resource, writer);
        		} catch (FHIRException e) {
        			throw new JAXBException(e);
        		}
        }
    }
	
	public static <T extends Resource> void write(T resource, Node node) throws JAXBException {
		write(resource, node, true);
	}
	
    public static <T extends Resource> void write(T resource, Node node, boolean formatted) throws JAXBException {
        Marshaller marshaller = createMarshaller(Format.XML, formatted);
        marshaller.marshal(resource, node);
    }
	
    public static JsonObject toJsonObject(Resource resource) throws JAXBException {
        // write Resource to String
        StringWriter writer = new StringWriter();
        write(resource, Format.JSON, writer);
        String jsonString = writer.toString();
        
        // read JsonObject from String
        return Json.createReader(new StringReader(jsonString)).readObject();
    }
    
    public static JsonObjectBuilder toJsonObjectBuilder(Resource resource) throws JAXBException {
        return toJsonObjectBuilder(toJsonObject(resource));
    }

    public static <T extends Element> JsonObject toJsonObject(Class<T> elementType, T element) throws JAXBException {
        // write Element to String
        StringWriter writer = new StringWriter();
        Marshaller marshaller = createMarshaller(Format.JSON, false);
        
        // wrap "element" in a JAXBElement to omit "type" field from output
        JAXBElement<T> jaxbElement = new JAXBElement<T>(new QName(""), elementType, element);
        marshaller.marshal(jaxbElement, writer);
        String jsonString = writer.toString();
        
        // read JsonObject from String
        return Json.createReader(new StringReader(jsonString)).readObject();
    }
    
    public static <T extends Element> JsonObjectBuilder toJsonObjectBuilder(Class<T> elementType, T element) throws JAXBException {
        return toJsonObjectBuilder(toJsonObject(elementType, element));
    }

    // copy an immutable JsonObject into a mutable JsonObjectBuilder
    public static JsonObjectBuilder toJsonObjectBuilder(JsonObject jsonObject) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        // JsonObject is a Map<String, JsonValue>
        for (String key : jsonObject.keySet()) {
            JsonValue value = jsonObject.get(key);
            builder.add(key, value);
        }
        return builder;
    }

    public static Attachment attachment(String contentType) {
        return objectFactory.createAttachment().withContentType(code(contentType));
    }

    public static com.ibm.watsonhealth.fhir.model.Base64Binary base64Binary(String b64Binary) {
        return objectFactory.createBase64Binary().withValue(Base64.getDecoder().decode(b64Binary));
    }

    public static com.ibm.watsonhealth.fhir.model.Boolean bool(boolean b) {
        return objectFactory.createBoolean().withValue(b);
    }
    
    public static CarePlanParticipant carePlanParticipant(CodeableConcept c, Reference r) {
    	return objectFactory.createCarePlanParticipant().withRole(c).withMember(r);
    }
    
    public static CarePlanStatus carePlanStatus(String s) {
    	return objectFactory.createCarePlanStatus().withValue(CarePlanStatusList.fromValue(s));
    }

    public static Code code(String code) {
        return objectFactory.createCode().withValue(code);
    }

    public static CodeableConcept codeableConcept(Coding... coding) {
        return objectFactory.createCodeableConcept().withCoding(coding);
    }

    public static CodeableConcept codeableConcept(String text) {
        return objectFactory.createCodeableConcept().withText(string(text));
    }

    public static CodeableConcept codeableConcept(String system, String code) {
        return codeableConcept(coding(system, code));
    }

    public static CodeableConcept codeableConceptWithText(String system, String code, String text) {
        return codeableConcept(coding(system, code)).withText(string(text));
    }

    public static CodeableConcept codeableConcept(String system, String code, String display) {
        return codeableConcept(coding(system, code, display));
    }

    public static Coding coding(String code) {
        return objectFactory.createCoding().withCode(code(code));
    }

    public static Coding coding(String system, String code) {
        return objectFactory.createCoding().withSystem(uri(system)).withCode(code(code));
    }

    public static Coding coding(String system, String code, String display) {
        return objectFactory.createCoding().withSystem(uri(system)).withCode(code(code)).withDisplay(string(display));
    }

    public static ContactPoint contactPoint(ContactPointSystemList system, String value) {
        return objectFactory.createContactPoint().withSystem(objectFactory.createContactPointSystem().withValue(system)).withValue(string(value)).withUse(objectFactory.createContactPointUse());
    }

    public static ContactPoint contactPoint(ContactPointSystemList system, String value, ContactPointUseList use) {
        return objectFactory.createContactPoint().withSystem(objectFactory.createContactPointSystem().withValue(system)).withValue(string(value)).withUse(objectFactory.createContactPointUse().withValue(use));
    }
    
    public static Address address(String city, String country, String line, String postalCode, String use) {
        return objectFactory.createAddress().withCity(string(city))
        								.withCountry(string(country))
        								.withLine(string(line))
        								.withPostalCode(string(postalCode))
        								.withUse(addressUse(use));
    }
    
    public static Address address(String city, String state, String line, String postalCode, String use, Extension e) {
        return objectFactory.createAddress().withCity(string(city))
        								.withState(string(state))
        								.withLine(string(line))
        								.withPostalCode(string(postalCode))
        								.withUse(addressUse(use))
        								.withExtension(e);
    }    
    
    public static PatientCommunication patientCommunication(CodeableConcept c, Boolean b) {
        return objectFactory.createPatientCommunication().withLanguage(c).withPreferred(bool(b));
    }    
    
    public static AddressUse addressUse(String a) {
        return objectFactory.createAddressUse().withValue(AddressUseList.fromValue(a));
    }

    public static Date date(String date) {
        return objectFactory.createDate().withValue(date);
    }

    public static DateTime dateTime(String dateTime) {
        return objectFactory.createDateTime().withValue(dateTime);
    }

    public static Time time(int hours, int minutes, int seconds) throws DatatypeConfigurationException {
        XMLGregorianCalendar time = datatypeFactory.newXMLGregorianCalendarTime(hours, minutes, seconds, DatatypeConstants.FIELD_UNDEFINED);
        return objectFactory.createTime().withValue(time);
    }

    public static Decimal decimal(Number value) {
        return objectFactory.createDecimal().withValue(new BigDecimal(value.toString()));
    }

    public static org.w3c.dom.Element div(String s) {
        try {
            return new DivAdapter().unmarshal(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static Extension extension(String url) {
        return objectFactory.createExtension().withUrl(url);
    }
    
    public static Extension extension(String url, CodeableConcept vc) {
        return objectFactory.createExtension().withUrl(url).withValueCodeableConcept(vc);
    }
    
    public static GoalStatus goalStatus(String s) {
        return objectFactory.createGoalStatus().withValue(GoalStatusList.fromValue(s));
    }

    public static HumanName humanName(String name) {
        return objectFactory.createHumanName().withText(string(name));
    }

    public static HumanName humanName(String given, String family) {
        return objectFactory.createHumanName().withGiven(string(given)).withFamily(string(family));
    }

    public static HumanName humanName(String given1, String given2, String family) {
        return objectFactory.createHumanName().withGiven(string(given1)).withGiven(string(given2)).withFamily(string(family));
    }
    
    public static HumanName humanName(String given1, String given2, String family, String prefix, String suffix, String text, String use) {
        return objectFactory.createHumanName().withGiven(string(given1))
        									.withGiven(string(given2))
        									.withFamily(string(family))
        									.withPrefix(string(prefix))
        									.withSuffix(string(suffix))
        									.withText(string(text))
        									.withUse(nameUse(use));
    }
    
    public static HumanName humanName(String given, String family, String prefix, String suffix, String text, String use) {
        return objectFactory.createHumanName().withGiven(string(given))
        									.withFamily(string(family))
        									.withPrefix(string(prefix))
        									.withSuffix(string(suffix))
        									.withText(string(text))
        									.withUse(nameUse(use));
    }
    
    public static NameUse nameUse(String use) {
        return objectFactory.createNameUse().withValue(NameUseList.fromValue(use));
    }
    
    public static Period period(String d) {
        return objectFactory.createPeriod().withStart(dateTime(d));
    }
    
    public static PatientAnimal patientAnimal(CodeableConcept c) {
        return objectFactory.createPatientAnimal().withBreed(c);
    }

    public static Id id(String s) {
        return objectFactory.createId().withValue(s);
    }

    public static Identifier identifier(String value) {
        return objectFactory.createIdentifier().withValue(string(value));
    }

    public static Identifier identifier(String value, String system) {
        return objectFactory.createIdentifier().withValue(string(value)).withSystem(uri(system));
    }
    
    public static Identifier identifier(String value, String system, String type, String use) {
        return objectFactory.createIdentifier().withValue(string(value)).withSystem(uri(system)).withType(codeableConcept(type)).withUse(identifierUse(use));
    }

    public static IdentifierUse identifierUse(String identifierUse) {
        return objectFactory.createIdentifierUse().withValue(IdentifierUseList.fromValue(identifierUse));
    }

    public static Instant instant(long time) {
        return instant(time, true);
    }
    
    public static Instant instant(long time, boolean normalize) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(time);
        XMLGregorianCalendar xmlCalendar = datatypeFactory.newXMLGregorianCalendar(calendar);
        if (normalize) {
            xmlCalendar = xmlCalendar.normalize();
        }
        return objectFactory.createInstant().withValue(xmlCalendar);
    }
    
    public static Instant instant(String time) {
        return instant(time, true);
    }
    
    public static Instant instant(String time, boolean normalize) {
        XMLGregorianCalendar xmlCalendar = datatypeFactory.newXMLGregorianCalendar(time);
        if (normalize) {
            xmlCalendar = xmlCalendar.normalize();
        }
        return objectFactory.createInstant().withValue(xmlCalendar);
    }
    
    public static Integer integer(int i) {
        return objectFactory.createInteger().withValue(i);
    }

    public static CodeableConcept interpretation(String system, String code, String display, String text) {
        return codeableConcept(coding(system, code, display)).withText(string(text));
    }

    public static Meta meta(long lastUpdated) {
        return meta(lastUpdated, true);
    }
    
    public static Meta meta(long lastUpdated, boolean normalize) {
        return objectFactory.createMeta().withLastUpdated(instant(lastUpdated, normalize));
    }
    
    public static Meta meta(String lastUpdated) {
        return meta(lastUpdated, true);
    }
    
    public static Meta meta(String lastUpdated, boolean normalize) {
        return objectFactory.createMeta().withLastUpdated(instant(lastUpdated, normalize));
    }

    public static NarrativeStatus narrativeStatus(String narStatus) {
        return objectFactory.createNarrativeStatus().withValue(NarrativeStatusList.fromValue(narStatus));
    }

    public static ObservationComponent observationComponent(CodeableConcept c, Quantity q) {
        return objectFactory.createObservationComponent().withCode(c).withValueQuantity(q);
    }

    public static ObservationStatus observationStatus(ObservationStatusList status) {
        return objectFactory.createObservationStatus().withValue(status);
    }

    public static PatientLink patientLink(String otherReference) {
        return objectFactory.createPatientLink().withOther(reference(otherReference));
    }

    public static Quantity quantity(Number value, String unit) {
        return objectFactory.createQuantity().withValue(objectFactory.createDecimal().withValue(new BigDecimal(value.toString()))).withUnit(string(unit));
    }

    public static Quantity quantity(Number value, String system, String code) {
        return objectFactory.createQuantity().withValue(objectFactory.createDecimal().withValue(new BigDecimal(value.toString()))).withSystem(uri(system)).withCode(code(code));
    }

    public static Quantity quantity(Number value, String unit, String system, String code) {
        return objectFactory.createQuantity().withValue(objectFactory.createDecimal().withValue(new BigDecimal(value.toString()))).withUnit(string(unit)).withSystem(uri(system)).withCode(code(code));
    }

    public static Range range(String code, String unit, String system, Number value) {
        return objectFactory.createRange().withHigh(simpleQty(code, unit, system, value));
    }

    public static Reference reference(String reference) {
        return objectFactory.createReference().withReference(string(reference));
    }

    public static Reference reference(String reference, String display) {
        return objectFactory.createReference().withReference(string(reference)).withDisplay(string(display));
    }
    
    public static PatientContact patientContact(CodeableConcept r, ContactPoint pc, HumanName h) {
        return objectFactory.createPatientContact().withRelationship(r).withTelecom(pc).withName(h);
    }

    public static RiskAssessmentPrediction riskAssmtPred(String outcomeText, Number d, String c, String unit, String system, Number v) {
        return objectFactory.createRiskAssessmentPrediction().withOutcome(codeableConcept(outcomeText)).withProbabilityDecimal(decimal(d)).withWhenRange(range(c, unit, system, v));
    }

    public static RiskAssessmentPrediction riskAssmtPred(String outcomeText, Number d, String highC, String highUnit, String highSystem, Number highV, String lowC, String lowUnit, String lowSystem, Number lowV) {
        Range r = range(highC, highUnit, highSystem, highV);
        r.setLow(simpleQty(lowC, lowUnit, lowSystem, lowV));
        return objectFactory.createRiskAssessmentPrediction().withOutcome(codeableConcept(outcomeText)).withProbabilityDecimal(decimal(d)).withWhenRange(r);
    }

    public static RiskAssessmentPrediction riskAssmtPred(String outcomeText, String c, String system, String pCode, String pDisplay, String pSystem) {
        return objectFactory.createRiskAssessmentPrediction().withOutcome(codeableConceptWithText(system, c, outcomeText)).withProbabilityCodeableConcept(codeableConcept(pSystem, pCode, pDisplay));
    }

    public static SimpleQuantity simpleQty(String code, String unit, String system, Number value) {
        return objectFactory.createSimpleQuantity().withCode(code(code)).withUnit(string(unit)).withSystem(uri(system)).withValue(decimal(value));
    }

    public static com.ibm.watsonhealth.fhir.model.String string(String s) {
        return objectFactory.createString().withValue(s);
    }
    
    public static TimingRepeat timingRepeat(int n1, int n2, String s) {
    	return objectFactory.createTimingRepeat()
				.withFrequency(integer(n1))
				.withPeriod(decimal(n2))
				.withPeriodUnits(unitsOfTime(s));
    }

    public static Uri uri(String uri) {
        return objectFactory.createUri().withValue(uri);
    }
    
    public static UnitsOfTime unitsOfTime(String s) {
    	return objectFactory.createUnitsOfTime().withValue(UnitsOfTimeList.fromValue(s));
    }
    
    public static boolean isStandardResourceType(String name) {
        return resourceTypeNames.contains(name);
    }
    
    public static List<String> getResourceTypeNames() {
        return resourceTypeNames;
    }
	
	@SuppressWarnings("unchecked")
	public static Class<? extends Resource> getResourceType(String name) throws FHIRException {
		try {
			return (Class<? extends Resource>) Class.forName("com.ibm.watsonhealth.fhir.model." + name);
		} catch (ClassNotFoundException e) {
			throw new FHIRInvalidResourceTypeException("'" + name + "' is not a valid resource type.");
		}
	}
	
	/**
	 * Returns the resource type (as a String) of the specified resource.   For a virtual resource,
	 * this will be the actual virtual resource type (not Basic).
	 * @param resource the resource 
	 * @return the name of the resource type associated with the resource
	 */
	public static String getResourceTypeName(Resource resource) {
	    if (resource instanceof Basic) {
	        Basic basic = (Basic) resource;
	        CodeableConcept cc = basic.getCode();
	        if (cc != null) {
	            List<Coding> codingList = cc.getCoding();
	            if (codingList != null) {
	                for (Coding coding : codingList) {
	                    if (coding.getSystem() != null) {
	                        String system = coding.getSystem().getValue();
	                        if (BASIC_RESOURCE_TYPE_URL.equals(system)) {
	                            return coding.getCode().getValue();
	                        }
	                    }
	                }
	            }
	        }
	    }
	    
	    return resource.getClass().getSimpleName();
	}
	
	private static final List<String> resourceTypeNames = Arrays.asList(
		"Account",
		"AllergyIntolerance",
		"Appointment",
		"AppointmentResponse",
		"AuditEvent",
		"Basic",
		"Binary",
		"BodySite",
		"Bundle",
		"CarePlan",
		"Claim",
		"ClaimResponse",
		"ClinicalImpression",
		"Communication",
		"CommunicationRequest",
		"Composition",
		"ConceptMap",
		"Condition",
		"Conformance",
		"Contract",
		"Coverage",
		"DataElement",
		"DetectedIssue",
		"Device",
		"DeviceComponent",
		"DeviceMetric",
		"DeviceUseRequest",
		"DeviceUseStatement",
		"DiagnosticOrder",
		"DiagnosticReport",
		"DocumentManifest",
		"DocumentReference",
		"DomainResource",
		"EligibilityRequest",
		"EligibilityResponse",
		"Encounter",
		"EnrollmentRequest",
		"EnrollmentResponse",
		"EpisodeOfCare",
		"ExplanationOfBenefit",
		"FamilyMemberHistory",
		"Flag",
		"Goal",
		"Group",
		"HealthcareService",
		"ImagingObjectSelection",
		"ImagingStudy",
		"Immunization",
		"ImmunizationRecommendation",
		"ImplementationGuide",
		"List",
		"Location",
		"Media",
		"Medication",
		"MedicationAdministration",
		"MedicationDispense",
		"MedicationOrder",
		"MedicationStatement",
		"MessageHeader",
		"NamingSystem",
		"NutritionOrder",
		"Observation",
		"OperationDefinition",
		"OperationOutcome",
		"Order",
		"OrderResponse",
		"Organization",
		"Parameters",
		"Patient",
		"PaymentNotice",
		"PaymentReconciliation",
		"Person",
		"Practitioner",
		"Procedure",
		"ProcedureRequest",
		"ProcessRequest",
		"ProcessResponse",
		"Provenance",
		"Questionnaire",
		"QuestionnaireResponse",
		"ReferralRequest",
		"RelatedPerson",
		"Resource",
		"RiskAssessment",
		"Schedule",
		"SearchParameter",
		"Slot",
		"Specimen",
		"StructureDefinition",
		"Subscription",
		"Substance",
		"SupplyDelivery",
		"SupplyRequest",
		"TestScript",
		"ValueSet",
		"VisionPrescription"
	);
	
    /**
     * Retrieves the resource contained in the specified ResourceContainer.
     * 
     * @param container
     *            the ResourceContainer containing the resource
     * @return
     * @throws Exception
     */
    public static Resource getResourceContainerResource(ResourceContainer container) throws Exception {
        if (container != null) {
            // Visit each of the ResourceContainer.getXXX() methods until we see a non-null value.
            for (Method method : RESOURCE_CONTAINER_DECLARED_GET_METHODS) {
                Resource resource = (Resource) method.invoke(container);
                if (resource != null) {
                    return resource;
                }
            }
        }
        return null;
    }
    
    private static final List<Method> RESOURCE_CONTAINER_DECLARED_GET_METHODS = buildResourceContainerDeclaredGetMethods();

    private static List<Method> buildResourceContainerDeclaredGetMethods() {
        List<Method> result = new ArrayList<>();
        for (Method method : ResourceContainer.class.getDeclaredMethods()) {
            if (method.getName().startsWith("get")) {
                result.add(method);
            }
        }
        return result;
    }

    /**
     * Sets the specified Resource within the specified ResourceContainer.
     * @param container the ResourceContainer that will hold the Resource
     * @param resource the Resource to store in the container
     * @throws Exception 
     */
    public static void setResourceContainerResource(ResourceContainer container, Resource resource) throws Exception {
        // Using reflection, call the appropriate ResourceContainer.setXXX() method, 
        // depending on the resource type.
        Class<? extends Resource> resourceType = resource.getClass();
        Method method = ResourceContainer.class.getMethod("set" + resourceType.getSimpleName(), resourceType);
        method.invoke(container, resource);
    }
    
    public static OperationOutcomeIssue buildOperationOutcomeIssue(String msg, IssueTypeList code) {
        return buildOperationOutcomeIssue(IssueSeverityList.FATAL, code, msg, null);
    }
    
    public static OperationOutcomeIssue buildOperationOutcomeIssue(IssueSeverityList severity, IssueTypeList code, String diagnostics, String location) {
        OperationOutcomeIssue issue = objectFactory.createOperationOutcomeIssue()
                .withSeverity(objectFactory.createIssueSeverity().withValue(severity))
                .withCode(objectFactory.createIssueType().withValue(code))
                .withDiagnostics(string(diagnostics))
                .withLocation(string(location));
        return issue;
    }

    /**
     * Build an OperationOutcome that contains the specified list of operation outcome issues.
     */
    public static OperationOutcome buildOperationOutcome(List<OperationOutcomeIssue> issues) {
        // Build an OperationOutcome and stuff the issues into it.
        OperationOutcome oo = objectFactory.createOperationOutcome().withIssue(issues);
        return oo;
    }
    
    /**
     * Build an OperationOutcome with an id and a list of issues from exception e.
     */
    public static OperationOutcome buildOperationOutcome(FHIROperationException e, boolean includeCausedByClauses) {
        if (e.getIssues() != null && e.getIssues().size() > 0) {
            Id id = id(e.getUniqueId());
            return buildOperationOutcome(e.getIssues()).withId(id);
        } else {
            return buildOperationOutcome((FHIRException) e, includeCausedByClauses);
        }
    }
    
    /**
     * Build an OperationOutcome with an id from exception e and a single issue of type 'exception' and severity 'fatal'.
     */
    public static OperationOutcome buildOperationOutcome(FHIRException e, boolean includeCausedByClauses) {
        Id id = id(e.getUniqueId());
        return buildOperationOutcome((Exception) e, includeCausedByClauses).withId(id);
    }
    
    /**
     * Build an OperationOutcome for the specified exception with a single issue of type 'exception' and severity 'fatal'.
     */
    public static OperationOutcome buildOperationOutcome(Exception exception, boolean includeCausedByClauses) {
        return buildOperationOutcome(exception, null, null, includeCausedByClauses);
    }
    
    /**
     * Build an OperationOutcome for the specified exception.
     */
    public static OperationOutcome buildOperationOutcome(Exception exception, IssueTypeList issueType, IssueSeverityList severity, boolean includeCausedByClauses) {
        // First, build a set of exception messages to be included in the OperationOutcome.
        // We'll include the exception message from each exception in the hierarchy, 
        // following the "causedBy" exceptions.
        StringBuilder msgs = new StringBuilder();
        Throwable e = exception;
        String causedBy = "";
        while (e != null) {
            msgs.append(causedBy + e.getClass().getSimpleName() + ": " + (e.getMessage() != null ? e.getMessage() : "<null message>"));
            e = e.getCause();
            causedBy = NL + "Caused by: ";
            
            // Force an exit from the loop if the caller doesn't want the caused-by clauses added.
            if (!includeCausedByClauses) {
                e = null;
            }
        }
        
        return buildOperationOutcome(msgs.toString(), issueType, severity);
    }
    
    /**
     * Build an OperationOutcome for the specified exception.
     * @param issueType defaults to IssueTypeList.EXCEPTION
     * @param severity defaults to IssueSeverityList.FATAL
     */
    public static OperationOutcome buildOperationOutcome(String message, IssueTypeList issueType, IssueSeverityList severity) {
        if (issueType == null) {
            issueType = IssueTypeList.EXCEPTION;
        }
        if (severity == null) {
            severity = IssueSeverityList.FATAL;
        }
        
        // Build an OperationOutcomeIssue that contains the exception messages.
        OperationOutcomeIssue ooi = objectFactory.createOperationOutcomeIssue()
                .withCode(objectFactory.createIssueType().withValue(issueType))
                .withSeverity(objectFactory.createIssueSeverity().withValue(severity))
                .withDiagnostics(objectFactory.createString().withValue(message));
        
        // Next, build the OperationOutcome.
        OperationOutcome oo = objectFactory.createOperationOutcome().withIssue(ooi);
        return oo;
    }
    
    
    /**
     * Builds a relative "Location" header value for the specified resource. This will be a string of the form
     * "<resource-type>/<id>/_history/<version>". Note that the server will turn this into an absolute URL prior to
     * returning it to the client.
     * 
     * @param resource
     *            the resource for which the location header value should be returned
     */
    public static URI buildLocationURI(String type, Resource resource) {
        String resourceTypeName = resource.getClass().getSimpleName();
        if (!resourceTypeName.equals(type)) {
            resourceTypeName = type;
        }
        return URI.create(resourceTypeName + "/" + resource.getId().getValue() 
            + "/_history/" + resource.getMeta().getVersionId().getValue());
    }
    
    /**
     * Creates a FHIR Resource object of the specified type.
     * @param resourceType The simple name of the type of resource instance to be created.
     * @return Resource - A Resource instance of the specified type.
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static <T extends Resource> T createResource(Class<T> resourceType) throws Exception {
        String createMethodName = "create" + resourceType.getSimpleName();
        Method createMethod = objectFactory.getClass().getMethod(createMethodName);
        return (T) createMethod.invoke(objectFactory);
    }

	/**
	 * Returns the string value of the specified extension element within
	 * the specified resource.
	 * @param resource
	 * @param extensionUrl
	 * @return the value of the first such extension with a valueString or null if the resource has no such extensions
	 */
	public static String getExtensionStringValue(Resource resource, String extensionUrl) {
		if (Objects.nonNull(resource) && Objects.nonNull(extensionUrl)) {
		    if (DomainResource.class.isAssignableFrom(resource.getClass())) {
		        DomainResource dr = (DomainResource) resource;
		        for (Extension ext : dr.getExtension()) {
		            if (ext.getUrl() != null && ext.getValueString() != null 
		                    && ext.getUrl().equals(extensionUrl)) {
		                return ext.getValueString().getValue();
		            }
		        }
		    }
		}   
		    	    
	    return null;
	}

}
