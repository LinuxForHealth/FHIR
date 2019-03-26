/**
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.server.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.GregorianCalendar;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.codec.digest.DigestUtils;
import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.core.MediaType;
import com.ibm.watsonhealth.fhir.model.Attachment;
import com.ibm.watsonhealth.fhir.model.AuditEvent;
import com.ibm.watsonhealth.fhir.model.Binary;
import com.ibm.watsonhealth.fhir.model.Conformance;
import com.ibm.watsonhealth.fhir.model.ConformanceStatementKindList;
import com.ibm.watsonhealth.fhir.model.DocumentReference;
import com.ibm.watsonhealth.fhir.model.Parameters;
import com.ibm.watsonhealth.fhir.model.RestfulConformanceModeList;
import com.ibm.watsonhealth.fhir.model.StructureDefinition;
import com.ibm.watsonhealth.fhir.model.StructureDefinitionKindList;
import com.ibm.watsonhealth.fhir.model.TypeRestfulInteractionList;
import com.ibm.watsonhealth.fhir.model.UnknownContentCodeList;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil.Format;

/**
 * This class contains tests for deserializing Base64 encoded binary data.
 * This class was created for testing fixes to Defect 211585.
 */
public class Base64BinaryTest extends FHIRServerTestBase {
    public Base64BinaryTest() {
        DEBUG_JSON = false;
    }
    
    @Test(enabled=true, groups = { "server-binary" })
    public void testCreateBinary() throws Exception {
        WebTarget target = getWebTarget();
        
        byte[] value = "Hello, World!".getBytes();
        Binary binary = f.createBinary();
        binary.setContentType(f.createCode().withValue("text/plain"));
        binary.setContent(f.createBase64Binary().withValue(value));
            
        Entity<Binary> entity = Entity.entity(binary, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.path("Binary").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        
        if (DEBUG_JSON) {
            FHIRUtil.write(binary, Format.JSON, System.out);
        }
        
        String binaryId = getLocationLogicalId(response);
        
        response = target.path("Binary/" + binaryId).request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Binary responseBinary = response.readEntity(Binary.class);
        
        if (DEBUG_JSON) {
            FHIRUtil.write(responseBinary, Format.JSON, System.out);
        }
        
        String valueString = new String(responseBinary.getContent().getValue());
        
        assertEquals(valueString, "Hello, World!");
    }
    
    @SuppressWarnings("deprecation")
    @Test(enabled=true, groups = { "server-binary" })
    public void testCreateDocumentReference() throws Exception {
        WebTarget target = getWebTarget();
        
        // Create an Attachment element
        String valueString =  "Hello, World! attachment test.";
        byte[] value = valueString.getBytes();
        byte[] valueHash = DigestUtils.sha(valueString);
        Attachment attachment = f.createAttachment();
        attachment.setContentType(f.createCode().withValue("text/plain"));
        attachment.setLanguage(f.createCode().withValue("en-US"));
        attachment.setData(f.createBase64Binary().withValue(value));
        attachment.setHash(f.createBase64Binary().withValue(valueHash));
        
        XMLGregorianCalendar indexedDateTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar());
        
        // Create a DocumentReference resource and connect the attachment to it.
        DocumentReference docRef = f.createDocumentReference()
                                    .withContent(f.createDocumentReferenceContent()
                                            .withAttachment(attachment))
                                    .withType(f.createCodeableConcept()
                                            .withCoding(f.createCoding()
                                                    .withCode(f.createCode().withValue("attachmentTest"))))
                                    .withIndexed(f.createInstant().withValue(indexedDateTime))
                                    .withStatus(f.createCode().withValue("current"));
                                            
        // Persist the DocumentReference
        Entity<DocumentReference> entity = Entity.entity(docRef, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.path("DocumentReference").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        
        if (DEBUG_JSON) {
            FHIRUtil.write(docRef, Format.JSON, System.out);
        }
        
        // Retrieve the DocumentReference
        String docRefId = getLocationLogicalId(response);
        response = target.path("DocumentReference/" + docRefId).request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        DocumentReference responseDocRef = response.readEntity(DocumentReference.class);
        
        if (DEBUG_JSON) {
            FHIRUtil.write(responseDocRef, Format.JSON, System.out);
        }
        
        // Compare original and retrieved values.
        assertEquals(new String(responseDocRef.getContent().get(0).getAttachment().getData().getValue()), valueString);
        assertTrue(Arrays.equals(responseDocRef.getContent().get(0).getAttachment().getHash().getValue(), valueHash));
    }
    
    @Test(enabled=true, groups = { "server-binary" })
    public void testCreateAuditEvent() throws Exception {
        WebTarget target = getWebTarget();
        
        String valueString =  "Hello, World! audit event test.";
        byte[] value = valueString.getBytes();
        XMLGregorianCalendar instantDateTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()); 
        
        // Create an AuditEvent with an AuditEventObject that has an AuditEventDetail with a base64 encoded value.
        AuditEvent auditEvent = f.createAuditEvent()
                                .withEvent(f.createAuditEventEvent()
                                        .withType(f.createCoding()
                                                .withCode(f.createCode()
                                                        .withValue("99")))
                                        .withDateTime(f.createInstant()
                                                .withValue(instantDateTime)))
                                .withParticipant(f.createAuditEventParticipant()
                                        .withRequestor(f.createBoolean()
                                                .withValue(new Boolean(false))))
                                .withSource(f.createAuditEventSource()
                                        .withIdentifier(f.createIdentifier().withValue(f.createString().withValue("Device/1"))))
                                .withObject(f.createAuditEventObject()
                                        .withDetail(f.createAuditEventDetail()
                                                .withType(f.createString().withValue("Base64Binary test"))
                                                .withValue(f.createBase64Binary()
                                                        .withValue(value))));
                                        
        // Persist the AuditEvent
        Entity<AuditEvent> entity = Entity.entity(auditEvent, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.path("AuditEvent").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        
        if (DEBUG_JSON) {
            FHIRUtil.write(auditEvent, Format.JSON, System.out);
        }
         
        // Retrieve the AuditEvent
        String auditEventId = getLocationLogicalId(response);
        response = target.path("AuditEvent/" + auditEventId).request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        AuditEvent responseAuditEvent = response.readEntity(AuditEvent.class);
        
        if (DEBUG_JSON) {
            FHIRUtil.write(responseAuditEvent, Format.JSON, System.out);
        }
        
        // Compare original and retrieved values.
        assertEquals(new String(responseAuditEvent.getObject().get(0).getDetail().get(0).getValue().getValue()), valueString);
    }
    
    @Test(enabled=true, groups = { "server-binary" })
    public void testCreateConformance() throws Exception {
        WebTarget target = getWebTarget();
       
        String valueString =  "Hello, World! conformance test.";
        byte[] value = valueString.getBytes();
                
        // Create an AuditEvent with an AuditEventObject that has an AuditEventDetail with a base64 encoded value.
        Conformance conformance = f.createConformance()
                                   .withDescription(f.createString().withValue("conformanceTest"))
                                   .withDate(f.createDateTime().withValue("2016-09-22T02:57:46.941Z"))
                                   .withKind(f.createConformanceStatementKind().withValue(ConformanceStatementKindList.CAPABILITY))
                                   .withFhirVersion(f.createId().withValue("0.6"))
                                   .withAcceptUnknown(f.createUnknownContentCode()
                                           .withValue(UnknownContentCodeList.ELEMENTS))
                                   .withFormat(f.createCode().withValue("application/json+fhir"))
                                   .withRest(f.createConformanceRest()
                                           .withMode(f.createRestfulConformanceMode().withValue(RestfulConformanceModeList.SERVER))
                                           .withResource(f.createConformanceResource()
                                                   .withType(f.createCode().withValue("StructureDefinition"))
                                                   .withInteraction(f.createConformanceInteraction()
                                                           .withCode(f.createTypeRestfulInteraction().withValue(TypeRestfulInteractionList.CREATE))))
                                           .withSecurity(f.createConformanceSecurity()
                                                   .withCertificate(f.createConformanceCertificate()
                                                           .withBlob(f.createBase64Binary().withValue(value)))));
                                        
        // Persist the Conformance resource
        Entity<Conformance> entity = Entity.entity(conformance, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.path("Conformance").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        
        if (DEBUG_JSON) {
            FHIRUtil.write(conformance, Format.JSON, System.out);
        }
                
        // Retrieve the Conformance
        String conformanceId = getLocationLogicalId(response);
        response = target.path("Conformance/" + conformanceId).request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Conformance responseConformance = response.readEntity(Conformance.class);
        
        if (DEBUG_JSON) {
            FHIRUtil.write(responseConformance, Format.JSON, System.out);
        }
        
        // Compare original and retrieved values.
        assertEquals(new String(responseConformance.getRest().get(0).getSecurity().getCertificate().get(0).getBlob().getValue()), valueString);
    }
    
    @Test(enabled=true, groups = { "server-binary" })
    public void testCreateParameters() throws Exception {
        WebTarget target = getWebTarget();
       
        String valueParameterString =  "Hello, World! parameters test.";
        byte[] valueParameter = valueParameterString.getBytes();
        String valueSignatureString = "Dr. Vinny Boombotz";
        byte[] valueSignature = valueSignatureString.getBytes();
        XMLGregorianCalendar instantDateTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar());
                
        // Create a Parameters resource containing a Base64 encoded value and signature.
        Parameters parameters = f.createParameters()
                                 .withParameter(f.createParametersParameter()
                                     .withName(f.createString().withValue("base64BinaryParameterTest"))
                                     .withValueBase64Binary(f.createBase64Binary().withValue(valueParameter)))
                                 
                                .withParameter(f.createParametersParameter()
                                    .withName(f.createString().withValue("resourceParameterTest"))
                                    .withResource(f.createResourceContainer()
                                        .withPerson(f.createPerson()
                                            .withActive(f.createBoolean().withValue(new Boolean(false))))))
                                
                                .withParameter(f.createParametersParameter()
                                    .withName(f.createString().withValue("signatureParameterTest"))
                                    .withValueSignature(f.createSignature()
                                        .withWhoReference(f.createReference()
                                                .withReference(f.createString().withValue("Patient/777")))
                                                .withType(f.createCoding()
                                                .withCode(f.createCode().withValue("1.2.840.10065.1.12.1.1")))
                                        .withWhen(f.createInstant()
                                                .withValue(instantDateTime))
                                        .withContentType(f.createCode().withValue("application/json+fhir"))
                                        .withBlob(f.createBase64Binary()
                                                .withValue(valueSignature))));
        
        if (DEBUG_JSON) {
            FHIRUtil.write(parameters, Format.JSON, System.out);
        }
        
        
        // Persist the Parameters resource
        Entity<Parameters> entity = Entity.entity(parameters, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.path("Parameters").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        
        // Retrieve the Parameters
        String parametersId = getLocationLogicalId(response);
        response = target.path("Parameters/" + parametersId).request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Parameters responseParameters = response.readEntity(Parameters.class);
        
        if (DEBUG_JSON) {
            FHIRUtil.write(responseParameters, Format.JSON, System.out);
        }
        
        // Compare original and retrieved values.
        assertEquals(new String(responseParameters.getParameter().get(0).getValueBase64Binary().getValue()), valueParameterString);
        assertEquals(new String(responseParameters.getParameter().get(2).getValueSignature().getBlob().getValue()), valueSignatureString);
    }
    
    @Test(enabled=true, groups = { "server-binary" })
    public void testCreateStructureDefn() throws Exception {
        WebTarget target = getWebTarget();
       
        String valueSnapshotString =  "Hello, World! structure definition test.";
        byte[] valueSnapshot = valueSnapshotString.getBytes();
               
        StructureDefinition struct = f.createStructureDefinition()
                                      .withBase(f.createUri().withValue("http://hl7.org/fhir/test"))
                                      .withUrl(f.createUri()
                                              .withValue("http://hl7.org/fhir/test"))
                                      .withConstrainedType(f.createCode().withValue("/Patient"))
                                      .withName(f.createString().withValue("createStructureDefnTest"))
                                      .withStatus(f.createCode().withValue("draft"))
                                      .withAbstract(f.createBoolean().withValue(new Boolean(true)))
                                      .withKind(f.createStructureDefinitionKind()
                                              .withValue(StructureDefinitionKindList.LOGICAL))
                                      .withSnapshot(f.createStructureDefinitionSnapshot()
                                              .withElement(f.createElementDefinition()
                                                      .withDefinition(f.createMarkdown().withValue("y"))
                                                      .withMin(f.createInteger().withValue(new Integer(1)))
                                                      .withMax(f.createString().withValue("1"))
                                                      .withType(f.createElementDefinitionType()
                                                              .withCode(f.createCode().withValue("x")))
                                                      .withBase(f.createElementDefinitionBase()
                                                              .withPath(f.createString().withValue("Patient/1"))
                                                              .withMin(f.createInteger().withValue(new Integer(1)))
                                                              .withMax(f.createString().withValue("1")))
                                                      .withPath(f.createString().withValue("/Patient"))
                                                      .withDefaultValueBase64Binary(f.createBase64Binary().withValue(valueSnapshot))
                                                      .withExampleBase64Binary(f.createBase64Binary().withValue(valueSnapshot))
                                                      .withFixedBase64Binary(f.createBase64Binary().withValue(valueSnapshot))
                                                      .withMaxValueBase64Binary(f.createBase64Binary().withValue(valueSnapshot))
                                                      .withMinValueBase64Binary(f.createBase64Binary().withValue(valueSnapshot))
                                                      .withPatternBase64Binary(f.createBase64Binary().withValue(valueSnapshot))));
        
        if (DEBUG_JSON) {
            FHIRUtil.write(struct, Format.JSON, System.out);
        }
                
        // Persist the StructureDefinition resource
        Entity<StructureDefinition> entity = Entity.entity(struct, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.path("StructureDefinition").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
                      
        // Retrieve the StructureDefinition
        String parametersId = getLocationLogicalId(response);
        response = target.path("StructureDefinition/" + parametersId).request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        StructureDefinition responseStruct = response.readEntity(StructureDefinition.class);
        
        if (DEBUG_JSON) {
            FHIRUtil.write(responseStruct, Format.JSON, System.out);
        }
        
        // Compare original and retrieved values.
        assertEquals(new String(responseStruct.getSnapshot().getElement().get(0).getDefaultValueBase64Binary().getValue()), valueSnapshotString);
        assertEquals(new String(responseStruct.getSnapshot().getElement().get(0).getExampleBase64Binary().getValue()), valueSnapshotString);
        assertEquals(new String(responseStruct.getSnapshot().getElement().get(0).getFixedBase64Binary().getValue()), valueSnapshotString);
        assertEquals(new String(responseStruct.getSnapshot().getElement().get(0).getMaxValueBase64Binary().getValue()), valueSnapshotString);
        assertEquals(new String(responseStruct.getSnapshot().getElement().get(0).getMinValueBase64Binary().getValue()), valueSnapshotString);
        assertEquals(new String(responseStruct.getSnapshot().getElement().get(0).getPatternBase64Binary().getValue()), valueSnapshotString);
         
    }
    
    @Test(enabled=true, groups = { "server-binary" })
    public void testCreateAuditEventWithExtension() throws Exception {
        WebTarget target = getWebTarget();
        
        String valueString =  "Hello, World! audit event with extension test.";
        byte[] value = valueString.getBytes();
        XMLGregorianCalendar instantDateTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()); 
        
        // Create an AuditEvent with an AuditEventObject that has an AuditEventDetail with a base64 encoded value.
        AuditEvent auditEvent = f.createAuditEvent()
                                .withEvent(f.createAuditEventEvent()
                                        .withType(f.createCoding()
                                                .withCode(f.createCode()
                                                        .withValue("99")))
                                        .withDateTime(f.createInstant()
                                                .withValue(instantDateTime)))
                                .withParticipant(f.createAuditEventParticipant()
                                        .withRequestor(f.createBoolean()
                                                .withValue(new Boolean(false))))
                                .withSource(f.createAuditEventSource()
                                        .withIdentifier(f.createIdentifier().withValue(f.createString().withValue("Device/1"))))
                                .withExtension(f.createExtension()
                                        .withUrl("http://ibm.com/watsonhealth/fhir/AuditEvent/testExtension")
                                        .withValueBase64Binary(f.createBase64Binary().withValue(value)));
                                        
        // Persist the AuditEvent
        Entity<AuditEvent> entity = Entity.entity(auditEvent, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.path("AuditEvent").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        
        if (DEBUG_JSON) {
            FHIRUtil.write(auditEvent, Format.JSON, System.out);
        }
         
        // Retrieve the AuditEvent
        String auditEventId = getLocationLogicalId(response);
        response = target.path("AuditEvent/" + auditEventId).request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        AuditEvent responseAuditEvent = response.readEntity(AuditEvent.class);
        
        if (DEBUG_JSON) {
            FHIRUtil.write(responseAuditEvent, Format.JSON, System.out);
        }
        
        // Compare original and retrieved values.
        assertEquals(new String(responseAuditEvent.getExtension().get(0).getValueBase64Binary().getValue()), valueString);
    }
}
