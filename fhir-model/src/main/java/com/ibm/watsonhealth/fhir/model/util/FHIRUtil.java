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
import java.util.Base64;
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
import javax.xml.transform.stream.StreamSource;

import org.eclipse.persistence.jaxb.JAXBContextProperties;
import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.w3c.dom.Node;

import com.ibm.watsonhealth.fhir.core.MediaType;
import com.ibm.watsonhealth.fhir.model.Attachment;
import com.ibm.watsonhealth.fhir.model.Code;
import com.ibm.watsonhealth.fhir.model.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.Coding;
import com.ibm.watsonhealth.fhir.model.ContactPoint;
import com.ibm.watsonhealth.fhir.model.ContactPointSystemList;
import com.ibm.watsonhealth.fhir.model.ContactPointUseList;
import com.ibm.watsonhealth.fhir.model.Date;
import com.ibm.watsonhealth.fhir.model.DateTime;
import com.ibm.watsonhealth.fhir.model.Decimal;
import com.ibm.watsonhealth.fhir.model.DomainResource;
import com.ibm.watsonhealth.fhir.model.HumanName;
import com.ibm.watsonhealth.fhir.model.Id;
import com.ibm.watsonhealth.fhir.model.Identifier;
import com.ibm.watsonhealth.fhir.model.IdentifierUse;
import com.ibm.watsonhealth.fhir.model.IdentifierUseList;
import com.ibm.watsonhealth.fhir.model.Instant;
import com.ibm.watsonhealth.fhir.model.Meta;
import com.ibm.watsonhealth.fhir.model.Narrative;
import com.ibm.watsonhealth.fhir.model.NarrativeStatus;
import com.ibm.watsonhealth.fhir.model.NarrativeStatusList;
import com.ibm.watsonhealth.fhir.model.ObjectFactory;
import com.ibm.watsonhealth.fhir.model.ObservationComponent;
import com.ibm.watsonhealth.fhir.model.ObservationStatus;
import com.ibm.watsonhealth.fhir.model.ObservationStatusList;
import com.ibm.watsonhealth.fhir.model.PatientLink;
import com.ibm.watsonhealth.fhir.model.Quantity;
import com.ibm.watsonhealth.fhir.model.Range;
import com.ibm.watsonhealth.fhir.model.Reference;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.model.RiskAssessmentPrediction;
import com.ibm.watsonhealth.fhir.model.SimpleQuantity;
import com.ibm.watsonhealth.fhir.model.Uri;
import com.ibm.watsonhealth.fhir.model.adapters.DivAdapter;
import com.ibm.watsonhealth.fhir.model.xhtml.Div;

public class FHIRUtil {
	private static final String HL7_FHIR_NS_URI = "http://hl7.org/fhir";	
	private static final String DEFAULT_NS_PREFIX = "";
	private static final String XHTML_NS_PREFIX = "xhtml";
	private static final String XHTML_NS_URI = "http://www.w3.org/1999/xhtml";
	private static final String XML_FHIR_METADATA_SOURCE = "com/ibm/watsonhealth/fhir/model/xml-fhir-metadata.xml";	
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
//			return JAXBContext.newInstance(new Class[] { Resource.class }, properties);
			return JAXBContext.newInstance("com.ibm.watsonhealth.fhir.model", ObjectFactory.class.getClassLoader(), properties);
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
		return unmarshaller;
	}
	
	private static void configureUnmarshaller(Unmarshaller unmarshaller, Format format) throws PropertyException {
		// TODO: add format specific configuration here
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Resource> T read(Class<T> resourceType, Format format, InputStream stream) throws JAXBException {
		Unmarshaller unmarshaller = createUnmarshaller(format);
		if (Format.XML.equals(format)) {
			return (T) unmarshaller.unmarshal(stream);
		} else {
			JAXBElement<T> element = unmarshaller.unmarshal(new StreamSource(stream), resourceType);
			return element.getValue();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Resource> T read(Class<T> resourceType, Format format, Reader reader) throws JAXBException {
		Unmarshaller unmarshaller = createUnmarshaller(format);
		if (Format.XML.equals(format)) {
			return (T) unmarshaller.unmarshal(reader);
		} else {
			JAXBElement<T> element = unmarshaller.unmarshal(new StreamSource(reader), resourceType);
			return element.getValue();
		}
	}
	
	/*
	public static <T extends Resource> T read(Class<T> resourceType, Node node) throws JAXBException {
		Unmarshaller unmarshaller = createUnmarshaller(Format.XML);
		JAXBElement<T> element = unmarshaller.unmarshal(new DOMSource(node), resourceType);
		return element.getValue();
	}
	*/
	
	@SuppressWarnings("unchecked")
	public static <T extends Resource> T read(Node node) throws JAXBException {
		Unmarshaller unmarshaller = createUnmarshaller(Format.XML);
		return (T) unmarshaller.unmarshal(node);
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
	
    public static Attachment attachment(String contentType) {
        return objectFactory.createAttachment().withContentType(code(contentType));
    }

    public static com.ibm.watsonhealth.fhir.model.Base64Binary base64Binary(String b64Binary) {
        return objectFactory.createBase64Binary().withValue(Base64.getDecoder().decode(b64Binary));
    }

    public static com.ibm.watsonhealth.fhir.model.Boolean bool(boolean b) {
        return objectFactory.createBoolean().withValue(b);
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

    public static CodeableConcept codeableConceptWithCoding_Text(String system, String code, String text) {
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

    public static Date date(String date) {
        return objectFactory.createDate().withValue(date);
    }

    public static DateTime dateTime(String dateTime) {
        return objectFactory.createDateTime().withValue(dateTime);
    }

    public static Decimal decimal(double v) {
        return objectFactory.createDecimal().withValue(BigDecimal.valueOf(v).stripTrailingZeros());
    }

    public static Div div(String s) {
        try {
            return new DivAdapter().unmarshal(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

    public static Id id(String s) {
        return objectFactory.createId().withValue(s);
    }

    public static Identifier identifier(String value) {
        return objectFactory.createIdentifier().withValue(string(value));
    }

    public static Identifier identifier(String value, String system) {
        return objectFactory.createIdentifier().withValue(string(value)).withSystem(uri(system));
    }

    public static IdentifierUse identifierUse(String identifierUse) {
        return objectFactory.createIdentifierUse().withValue(IdentifierUseList.fromValue(identifierUse));
    }

    public static Instant instant(long time) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(time);
        XMLGregorianCalendar xmlCalendar = datatypeFactory.newXMLGregorianCalendar(calendar);
        return objectFactory.createInstant().withValue(xmlCalendar);
    }

    public static CodeableConcept interpretation(String system, String code, String display, String text) {
        return codeableConcept(coding(system, code, display)).withText(string(text));
    }

    public static Meta meta(long lastUpdated) {
        return objectFactory.createMeta().withLastUpdated(instant(lastUpdated));
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

    public static Quantity quantity(double value, String unit) {
        return objectFactory.createQuantity().withValue(objectFactory.createDecimal().withValue(BigDecimal.valueOf(value).stripTrailingZeros())).withUnit(string(unit));
    }

    public static Quantity quantity(double value, String system, String code) {
        return objectFactory.createQuantity().withValue(objectFactory.createDecimal().withValue(BigDecimal.valueOf(value).stripTrailingZeros())).withSystem(uri(system)).withCode(code(code));
    }

    public static Quantity quantity(double value, String unit, String system, String code) {
        return objectFactory.createQuantity().withValue(objectFactory.createDecimal().withValue(BigDecimal.valueOf(value).stripTrailingZeros())).withUnit(string(unit)).withSystem(uri(system)).withCode(code(code));
    }

    public static Range range(String c, String unit, String system, double v) {
        return objectFactory.createRange().withHigh(simpleQty(c, unit, system, v));
    }

    public static Reference reference(String reference) {
        return objectFactory.createReference().withReference(string(reference));
    }

    public static Reference reference(String reference, String display) {
        return objectFactory.createReference().withReference(string(reference)).withDisplay(string(display));
    }

    public static RiskAssessmentPrediction riskAssmtPred(String outcomeText, double d, String c, String unit, String system, double v) {
        return objectFactory.createRiskAssessmentPrediction().withOutcome(codeableConcept(outcomeText)).withProbabilityDecimal(decimal(d)).withWhenRange(range(c, unit, system, v));
    }

    public static RiskAssessmentPrediction riskAssmtPred(String outcomeText, double d, String highC, String highUnit, String highSystem, double highV,
        String lowC, String lowUnit, String lowSystem, double lowV) {
        Range r = range(highC, highUnit, highSystem, highV);
        r.setLow(simpleQty(lowC, lowUnit, lowSystem, lowV));
        return objectFactory.createRiskAssessmentPrediction().withOutcome(codeableConcept(outcomeText)).withProbabilityDecimal(decimal(d)).withWhenRange(r);
    }

    public static RiskAssessmentPrediction riskAssmtPred(String outcomeText, String c, String system, String pCode, String pDisplay, String pSystem) {
        return objectFactory.createRiskAssessmentPrediction().withOutcome(codeableConceptWithCoding_Text(system, c, outcomeText)).withProbabilityCodeableConcept(codeableConcept(pSystem, pCode, pDisplay));
    }

    public static SimpleQuantity simpleQty(String c, String unit, String system, double v) {
        return objectFactory.createSimpleQuantity().withCode(code(c)).withUnit(string(unit)).withSystem(uri(system)).withValue(decimal(v));
    }

    public static com.ibm.watsonhealth.fhir.model.String string(String s) {
        return objectFactory.createString().withValue(s);
    }

    public static Uri uri(String uri) {
        return objectFactory.createUri().withValue(uri);
    }

    public static boolean isValidResourceTypeName(String name) {
        return resourceTypeNames.contains(name);
    }
	
	@SuppressWarnings("unchecked")
	public static Class<? extends Resource> getResourceType(String name) {
		try {
			return (Class<? extends Resource>) Class.forName("com.ibm.watsonhealth.fhir.model." + name);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("Resource type for name: " + name + " not found.");
		}
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
