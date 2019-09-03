/**
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.server.test;

import static com.ibm.watson.health.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.digest.DigestUtils;
import org.testng.annotations.Test;

import com.ibm.watson.health.fhir.core.FHIRMediaType;
import com.ibm.watson.health.fhir.model.format.Format;
import com.ibm.watson.health.fhir.model.resource.AuditEvent;
import com.ibm.watson.health.fhir.model.resource.Binary;
import com.ibm.watson.health.fhir.model.resource.DocumentReference;
import com.ibm.watson.health.fhir.model.resource.Parameters;
import com.ibm.watson.health.fhir.model.resource.Person;
import com.ibm.watson.health.fhir.model.type.Attachment;
import com.ibm.watson.health.fhir.model.type.Base64Binary;
import com.ibm.watson.health.fhir.model.type.Code;
import com.ibm.watson.health.fhir.model.type.CodeableConcept;
import com.ibm.watson.health.fhir.model.type.Coding;
import com.ibm.watson.health.fhir.model.type.DocumentReferenceStatus;
import com.ibm.watson.health.fhir.model.type.Extension;
import com.ibm.watson.health.fhir.model.type.Identifier;
import com.ibm.watson.health.fhir.model.type.Instant;
import com.ibm.watson.health.fhir.model.type.Reference;
import com.ibm.watson.health.fhir.model.type.Signature;
import com.ibm.watson.health.fhir.model.type.Uri;
import com.ibm.watson.health.fhir.model.util.FHIRUtil;

/**
 * This class contains tests for deserializing Base64 encoded binary data. This
 * class was created for testing fixes to Defect 211585.
 */
public class Base64BinaryTest extends FHIRServerTestBase {
    public Base64BinaryTest() {
        DEBUG_JSON = false;
    }

    @Test(enabled = true, groups = { "server-binary" })
    public void testCreateBinary() throws Exception {
        WebTarget target = getWebTarget();

        byte[] value = "Hello, World!".getBytes();
        Binary binary = Binary.builder()
                .contentType(Code.of("text/plain")).data(Base64Binary.builder().value(value).build()).build();

        Entity<Binary> entity = Entity.entity(binary, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Binary").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        if (DEBUG_JSON) {
            FHIRUtil.write(binary, Format.JSON, System.out);
        }

        String binaryId = getLocationLogicalId(response);

        response = target.path("Binary/" + binaryId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Binary responseBinary = response.readEntity(Binary.class);

        if (DEBUG_JSON) {
            FHIRUtil.write(responseBinary, Format.JSON, System.out);
        }

        String valueString = new String(responseBinary.getData().getValue());

        assertEquals(valueString, "Hello, World!");
    }

    @SuppressWarnings("deprecation")
    @Test(enabled = true, groups = { "server-binary" })
    public void testCreateDocumentReference() throws Exception {
        WebTarget target = getWebTarget();

        // Create an Attachment element
        String valueString = "Hello, World! attachment test.";
        byte[] value = valueString.getBytes();
        byte[] valueHash = DigestUtils.sha(valueString);
        Attachment attachment = Attachment.builder().contentType(Code.of("text/plain")).language(Code.of("en-US"))
                .data(Base64Binary.builder().value(value).build()).hash(Base64Binary.builder().value(valueHash).build())
                .build();

        // Create a DocumentReference resource and connect the attachment to it.
        List<DocumentReference.Content> listDocRef = new ArrayList<DocumentReference.Content>();
        listDocRef.add(DocumentReference.Content.builder().attachment(attachment).build());

        DocumentReference docRef = DocumentReference.builder()
                .status(DocumentReferenceStatus.CURRENT)
                .content(listDocRef)
                .type(CodeableConcept.builder()
                        .coding(Coding.builder().code(Code.of("attachmentTest")).build()).build())
                .date(Instant.of(ZonedDateTime.now())).build();

        // Persist the DocumentReference
        Entity<DocumentReference> entity = Entity.entity(docRef, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("DocumentReference").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        if (DEBUG_JSON) {
            FHIRUtil.write(docRef, Format.JSON, System.out);
        }

        // Retrieve the DocumentReference
        String docRefId = getLocationLogicalId(response);
        response = target.path("DocumentReference/" + docRefId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        DocumentReference responseDocRef = response.readEntity(DocumentReference.class);

        if (DEBUG_JSON) {
            FHIRUtil.write(responseDocRef, Format.JSON, System.out);
        }

        // Compare original and retrieved values.
        assertEquals(new String(responseDocRef.getContent().get(0).getAttachment().getData().getValue()), valueString);
        assertTrue(Arrays.equals(responseDocRef.getContent().get(0).getAttachment().getHash().getValue(), valueHash));
    }

    @Test(enabled = true, groups = { "server-binary" })
    public void testCreateAuditEvent() throws Exception {
        WebTarget target = getWebTarget();

        String valueString = "Hello, World! audit event test.";
        byte[] value = valueString.getBytes();

        // Create an AuditEvent with an AuditEventObject that has an AuditEventDetail
        // with a base64 encoded value.
        List<AuditEvent.Agent> listAgents = new ArrayList<AuditEvent.Agent>();
        listAgents.add(AuditEvent.Agent.builder()
                .requestor(com.ibm.watson.health.fhir.model.type.Boolean.TRUE).build());
        AuditEvent auditEvent = AuditEvent.builder()
                .type(Coding.builder().code(Code.of("99")).build())
                .recorded(Instant.of(ZonedDateTime.now()))
                .agent(listAgents)
                .source(AuditEvent.Source.builder()
                        .observer(Reference.builder()
                                .identifier(Identifier.builder().value(string("Device/1")).build()).build())
                                .build())
                .entity(AuditEvent.Entity.builder()
                        .detail(AuditEvent.Entity.Detail.builder()
                                .type(string("Base64Binary test"))
                                .value(Base64Binary.builder().value(value).build()).build())
                        .build())
                .build();

        // Persist the AuditEvent
        Entity<AuditEvent> entity = Entity.entity(auditEvent, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("AuditEvent").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        if (DEBUG_JSON) {
            FHIRUtil.write(auditEvent, Format.JSON, System.out);
        }

        // Retrieve the AuditEvent
        String auditEventId = getLocationLogicalId(response);
        response = target.path("AuditEvent/" + auditEventId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        AuditEvent responseAuditEvent = response.readEntity(AuditEvent.class);

        if (DEBUG_JSON) {
            FHIRUtil.write(responseAuditEvent, Format.JSON, System.out);
        }

        Base64Binary base64Binary = (Base64Binary) responseAuditEvent.getEntity().get(0).getDetail().get(0).getValue();

        // Compare original and retrieved values.
        assertEquals(new String(base64Binary.getValue()), valueString);
    }


    @Test(enabled = true, groups = { "server-binary" })
    public void testCreateParameters() throws Exception {
        WebTarget target = getWebTarget();

        String valueParameterString = "Hello, World! parameters test.";
        byte[] valueParameter = valueParameterString.getBytes();
        String valueSignatureString = "Dr. Vinny Boombotz";
        byte[] valueSignature = valueSignatureString.getBytes();

        // Create a Parameters resource containing a Base64 encoded value and signature.
        List<Coding> listCoding = new ArrayList<Coding>();
        listCoding.add(Coding.builder().code(Code.of(FHIRMediaType.APPLICATION_FHIR_JSON)).build());
        Parameters parameters = Parameters.builder()
                .parameter(Parameters.Parameter.builder()
                        .name(string("base64BinaryParameterTest"))
                        .value(Base64Binary.builder().value(valueParameter).build()).build())
                .parameter(Parameters.Parameter.builder()
                        .name(string("resourceParameterTest"))
                        .resource(Person.builder().active(com.ibm.watson.health.fhir.model.type.Boolean.FALSE)
                                .build())
                        .build())
                .parameter(Parameters.Parameter.builder()
                        .name(string("signatureParameterTest"))
                        .value(Signature.builder()
                                .type(listCoding)
                                .when(Instant.of(ZonedDateTime.now()))
                                .who(Reference.builder().type(Uri.of("1.2.840.10065.1.12.1.1"))
                                        .reference(string("Patient/777")).build())
                                .data(Base64Binary.builder().value(valueSignature).build()).build())
                        .build())
                .build();

        if (DEBUG_JSON) {
            FHIRUtil.write(parameters, Format.JSON, System.out);
        }

        // Persist the Parameters resource
        Entity<Parameters> entity = Entity.entity(parameters, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Parameters").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Retrieve the Parameters
        String parametersId = getLocationLogicalId(response);
        response = target.path("Parameters/" + parametersId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Parameters responseParameters = response.readEntity(Parameters.class);

        if (DEBUG_JSON) {
            FHIRUtil.write(responseParameters, Format.JSON, System.out);
        }

        Base64Binary base64Binary = (Base64Binary) responseParameters.getParameter().get(0).getValue();
        Signature signature = (Signature) responseParameters.getParameter().get(2).getValue();

        // Compare original and retrieved values.
        assertEquals(new String(base64Binary.getValue()), valueParameterString);
        assertEquals(new String(signature.getData().getValue()), valueSignatureString);
    }


    @Test(enabled = true, groups = { "server-binary" })
    public void testCreateAuditEventWithExtension() throws Exception {
        WebTarget target = getWebTarget();

        String valueString = "Hello, World! audit event with extension test.";
        byte[] value = valueString.getBytes();

        // Create an AuditEvent with an AuditEventObject that has an AuditEventDetail
        // with a base64 encoded value.
        List<AuditEvent.Agent> listAgents = new ArrayList<AuditEvent.Agent>();
        listAgents.add(AuditEvent.Agent.builder()
                .requestor(com.ibm.watson.health.fhir.model.type.Boolean.FALSE)
                .build());
        
        AuditEvent auditEvent = AuditEvent.builder()
                .type(Coding.builder().code(Code.of("99")).build())
                .recorded(Instant.of(ZonedDateTime.now()))
                .agent(listAgents)
                .source(AuditEvent.Source.builder()
                        .observer(Reference.builder()
                                .identifier(Identifier.builder().value(string("Device/1")).build()).build())
                                .build())
                .extension(Extension.builder()
                        .url("http://ibm.com/watson/health/fhir/AuditEvent/testExtension")
                        .value(Base64Binary.builder().value(value).build()).build())
                .build();

        // Persist the AuditEvent
        Entity<AuditEvent> entity = Entity.entity(auditEvent, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("AuditEvent").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        if (DEBUG_JSON) {
            FHIRUtil.write(auditEvent, Format.JSON, System.out);
        }

        // Retrieve the AuditEvent
        String auditEventId = getLocationLogicalId(response);
        response = target.path("AuditEvent/" + auditEventId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        AuditEvent responseAuditEvent = response.readEntity(AuditEvent.class);

        if (DEBUG_JSON) {
            FHIRUtil.write(responseAuditEvent, Format.JSON, System.out);
        }

        // Compare original and retrieved values.
        assertEquals(new String(((Base64Binary) responseAuditEvent.getExtension().get(0).getValue()).getValue()),
                valueString);
    }
}
