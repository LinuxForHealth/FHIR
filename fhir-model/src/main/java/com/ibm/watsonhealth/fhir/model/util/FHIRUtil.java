/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.Binder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.persistence.jaxb.JAXBContextProperties;
import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.w3c.dom.Node;

import com.ibm.watsonhealth.fhir.model.Code;
import com.ibm.watsonhealth.fhir.model.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.Coding;
import com.ibm.watsonhealth.fhir.model.ContactPoint;
import com.ibm.watsonhealth.fhir.model.ContactPointSystemList;
import com.ibm.watsonhealth.fhir.model.ContactPointUseList;
import com.ibm.watsonhealth.fhir.model.Date;
import com.ibm.watsonhealth.fhir.model.DomainResource;
import com.ibm.watsonhealth.fhir.model.HumanName;
import com.ibm.watsonhealth.fhir.model.Id;
import com.ibm.watsonhealth.fhir.model.Instant;
import com.ibm.watsonhealth.fhir.model.Narrative;
import com.ibm.watsonhealth.fhir.model.ObjectFactory;
import com.ibm.watsonhealth.fhir.model.ObservationStatus;
import com.ibm.watsonhealth.fhir.model.ObservationStatusList;
import com.ibm.watsonhealth.fhir.model.Quantity;
import com.ibm.watsonhealth.fhir.model.Reference;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.model.Uri;

public class FHIRUtil {
	private static final String HL7_FHIR_NS_URI = "http://hl7.org/fhir";	
	private static final String DEFAULT_NS_PREFIX = "";
	private static final String XHTML_NS_PREFIX = "xhtml";
	private static final String XHTML_NS_URI = "http://www.w3.org/1999/xhtml";
	private static final String APPLICATION_XML = "application/xml";
	private static final String XML_FHIR_METADATA_SOURCE = "com/ibm/watsonhealth/fhir/model/xml-fhir-metadata.xml";	
	private static final String APPLICATION_JSON = "application/json";
	private static final String JSON_FHIR_METADATA_SOURCE = "com/ibm/watsonhealth/fhir/model/json-fhir-metadata.xml";
	
	public static enum Format {
		XML,
		JSON
	}
	
	private static final JAXBContext xmlContext = createContext(Format.XML);
	private static final JAXBContext jsonContext = createContext(Format.JSON);
	private static final ObjectFactory objectFactory = new ObjectFactory();
	private static final DatatypeFactory datatypeFactory = createDatatypeFactory();
	private static final DocumentBuilder documentBuilder = createDocumentBuilder();
	
	private FHIRUtil() { }
	
	private static DocumentBuilder createDocumentBuilder() {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);			
			return factory.newDocumentBuilder();
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

	private static JAXBContext createContext(Format format) {
		try {		
			Map<String, Object> properties = new HashMap<String, Object>();
			String metadataSource = null;
			if (Format.XML.equals(format)) {
				// XML-specific configuration
				properties.put(JAXBContextProperties.MEDIA_TYPE, APPLICATION_XML);
				metadataSource = XML_FHIR_METADATA_SOURCE;
			} else {
				// JSON-specific configuration
				properties.put(JAXBContextProperties.MEDIA_TYPE, APPLICATION_JSON);
				properties.put(JAXBContextProperties.JSON_INCLUDE_ROOT, false);
				metadataSource = JSON_FHIR_METADATA_SOURCE;
			}
			// common configuration
			properties.put(JAXBContextProperties.OXM_METADATA_SOURCE, metadataSource);
			return JAXBContext.newInstance(new Class[] { Resource.class }, properties);
		} catch (JAXBException e) {
			throw new Error(e);
		}
	}
	
	private static JAXBContext getContext(Format format) {
		return Format.XML.equals(format) ? xmlContext : jsonContext;
	}
	
	public static <T extends Resource> Binder<Node> createBinder(T resource) {
		// FIXME: Workaround for bug: https://bugs.eclipse.org/bugs/show_bug.cgi?id=455133
		Narrative text = null;
		DomainResource domainResource = null;
		if (resource instanceof DomainResource) {
			domainResource = (DomainResource) resource;
			if (domainResource.getText() != null) {
				text = domainResource.getText();
				domainResource.setText(null);
			}
		}
		Binder<Node> binder = getContext(Format.XML).createBinder();
		try {
			binder.marshal(wrap(resource), documentBuilder.newDocument());
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		if (text != null) {
			domainResource.setText(text);
		}
		return binder;
	}
	
	private static Unmarshaller createUnmarshaller(Format format) throws JAXBException {
		JAXBContext context = getContext(format);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		configureUnmarshaller(unmarshaller, format);
		return unmarshaller;
	}
	
	private static void configureUnmarshaller(Unmarshaller unmarshaller, Format format) throws PropertyException {
		// TODO: add format specific configuration here
	}
	
	public static <T extends Resource> T read(Class<T> resourceType, Format format, InputStream stream) throws JAXBException {
		Unmarshaller unmarshaller = createUnmarshaller(format);
		JAXBElement<T> element = unmarshaller.unmarshal(new StreamSource(stream), resourceType);
		return element.getValue();
	}
	
	public static <T extends Resource> T read(Class<T> resourceType, Format format, Reader reader) throws JAXBException {
		Unmarshaller unmarshaller = createUnmarshaller(format);
		JAXBElement<T> element = unmarshaller.unmarshal(new StreamSource(reader), resourceType);
		return element.getValue();
	}
	
	public static <T extends Resource> T read(Class<T> resourceType, Node node) throws JAXBException {
		Unmarshaller unmarshaller = createUnmarshaller(Format.XML);
		JAXBElement<T> element = unmarshaller.unmarshal(new DOMSource(node), resourceType);
		return element.getValue();
	}
	
	private static Marshaller createMarshaller(Format format) throws JAXBException {
		JAXBContext context = getContext(format);
		Marshaller marshaller = context.createMarshaller();
		configureMarshaller(marshaller, format);
		return marshaller;
	}
	
	private static void configureMarshaller(Marshaller marshaller, Format format) throws PropertyException {
		// common configuration
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setProperty(MarshallerProperties.INDENT_STRING, "    ");
		if (Format.XML.equals(format)) {
			// XML-specific configuration
			Map<String, String> namespacePrefixMap = new HashMap<String, String>();
			namespacePrefixMap.put(HL7_FHIR_NS_URI, DEFAULT_NS_PREFIX);	// default namespace
			namespacePrefixMap.put(XHTML_NS_URI, XHTML_NS_PREFIX);
			marshaller.setProperty(MarshallerProperties.NAMESPACE_PREFIX_MAPPER, namespacePrefixMap);
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
		}
	}
	
	public static <T extends Resource> void write(T resource, Format format, OutputStream stream) throws JAXBException {
		Marshaller marshaller = createMarshaller(format);
		marshaller.marshal(resource, stream);
	}
	
	public static <T extends Resource> void write(T resource, Format format, Writer writer) throws JAXBException {
		Marshaller marshaller = createMarshaller(format);
		marshaller.marshal(resource, writer);
	}
	
	public static <T extends Resource> void write(T resource, Node node) throws JAXBException {
		Marshaller marshaller = createMarshaller(Format.XML);
		marshaller.marshal(resource, node);
	}
	
	public static Code code(String code) {
		return objectFactory.createCode().withValue(code);
	}

	public static CodeableConcept codeableConcept(Coding...coding) {
		return objectFactory.createCodeableConcept().withCoding(coding);
	}
	
	public static CodeableConcept codeableConcept(String system, String code) {
		return codeableConcept(coding(system, code));
	}

	public static CodeableConcept codeableConcept(String system, String code, String display) {
		return codeableConcept(coding(system, code, display));
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
	
	public static Date date(String date) {
		return objectFactory.createDate().withValue(date);
	}
	
	public static HumanName humanName(String name) {
		return objectFactory.createHumanName().withText(string(name));
	}
	
	public static HumanName humanName(String family, String... given) {
		HumanName humanName = objectFactory.createHumanName();
		humanName.getFamily().add(string(family));
		for (String g : given) {
			humanName.getGiven().add(string(g));
		}
		return humanName;
	}

	public static Id id(String s) {
		return objectFactory.createId().withValue(s);
	}
	
	public static Instant instant(long time) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(time);
		XMLGregorianCalendar xmlCalendar = datatypeFactory.newXMLGregorianCalendar(calendar);
		return objectFactory.createInstant().withValue(xmlCalendar);
	}
	
	public static ObservationStatus observationStatus(ObservationStatusList status) {
		return objectFactory.createObservationStatus().withValue(status);
	}

	public static Quantity quantity(double value, String unit) {
		return objectFactory.createQuantity().withValue(objectFactory.createDecimal().withValue(new BigDecimal(value))).withUnit(string(unit));
	}

	public static Quantity quantity(double value, String system, String code) {
		return objectFactory.createQuantity().withValue(objectFactory.createDecimal().withValue(new BigDecimal(value))).withSystem(uri(system)).withCode(code(code));
	}

	public static Quantity quantity(double value, String unit, String system, String code) {
		return objectFactory.createQuantity().withValue(objectFactory.createDecimal().withValue(new BigDecimal(value))).withUnit(string(unit)).withSystem(uri(system)).withCode(code(code));
	}

	public static Reference reference(String reference) {
		return objectFactory.createReference().withReference(string(reference));
	}

	public static com.ibm.watsonhealth.fhir.model.String string(String s) {
		return objectFactory.createString().withValue(s);
	}
	
	public static Uri uri(String uri) {
		return objectFactory.createUri().withValue(uri);
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
	
	public static boolean isValidResourceTypeName(String name) {
		return resourceTypeNames.contains(name);
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
}
