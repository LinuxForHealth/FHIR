/**
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.server.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static com.ibm.watsonhealth.fhir.model.type.String.string;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.digest.DigestUtils;
import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.core.MediaType;
import com.ibm.watsonhealth.fhir.model.type.Attachment;
import com.ibm.watsonhealth.fhir.model.type.Base64Binary;
import com.ibm.watsonhealth.fhir.model.resource.AuditEvent;
import com.ibm.watsonhealth.fhir.model.resource.Binary;
import com.ibm.watsonhealth.fhir.model.resource.CapabilityStatement;
import com.ibm.watsonhealth.fhir.model.resource.CapabilityStatement.Rest;
import com.ibm.watsonhealth.fhir.model.resource.CapabilityStatement.Rest.Resource.Interaction;
import com.ibm.watsonhealth.fhir.model.type.CapabilityStatementKind;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.Coding;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.DocumentReferenceStatus;
import com.ibm.watsonhealth.fhir.model.type.ElementDefinition;
import com.ibm.watsonhealth.fhir.model.type.ElementDefinition.Base;
import com.ibm.watsonhealth.fhir.model.type.ElementDefinition.Example;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.FHIRVersion;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.resource.DocumentReference;
import com.ibm.watsonhealth.fhir.model.resource.Parameters;
import com.ibm.watsonhealth.fhir.model.type.RestfulCapabilityMode;
import com.ibm.watsonhealth.fhir.model.type.Signature;
import com.ibm.watsonhealth.fhir.model.resource.StructureDefinition;
import com.ibm.watsonhealth.fhir.model.resource.StructureDefinition.Snapshot;
import com.ibm.watsonhealth.fhir.model.resource.Person;
import com.ibm.watsonhealth.fhir.model.type.StructureDefinitionKind;
import com.ibm.watsonhealth.fhir.model.type.TypeRestfulInteraction;
import com.ibm.watsonhealth.fhir.model.type.UnsignedInt;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;
import com.ibm.watsonhealth.fhir.model.format.Format;
import com.ibm.watsonhealth.fhir.model.type.Instant;
import com.ibm.watsonhealth.fhir.model.type.Markdown;
import com.ibm.watsonhealth.fhir.model.type.PublicationStatus;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.ResourceType;
import com.ibm.watsonhealth.fhir.core.MediaType;

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
        Binary binary = Binary.builder(Code.of("text/plain")).data(Base64Binary.builder().value(value).build()).build();

        Entity<Binary> entity = Entity.entity(binary, MediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Binary").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        if (DEBUG_JSON) {
            FHIRUtil.write(binary, Format.JSON, System.out);
        }

        String binaryId = getLocationLogicalId(response);

        response = target.path("Binary/" + binaryId).request(MediaType.APPLICATION_FHIR_JSON).get();
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
        listDocRef.add(DocumentReference.Content.builder(attachment).build());

        DocumentReference docRef = DocumentReference
                .builder(DocumentReferenceStatus.CURRENT, listDocRef).type(CodeableConcept.builder()
                        .coding(Coding.builder().code(Code.of("attachmentTest")).build()).build())
                .date(Instant.of(ZonedDateTime.now())).build();

        // Persist the DocumentReference
        Entity<DocumentReference> entity = Entity.entity(docRef, MediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("DocumentReference").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        if (DEBUG_JSON) {
            FHIRUtil.write(docRef, Format.JSON, System.out);
        }

        // Retrieve the DocumentReference
        String docRefId = getLocationLogicalId(response);
        response = target.path("DocumentReference/" + docRefId).request(MediaType.APPLICATION_FHIR_JSON).get();
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
        listAgents.add(AuditEvent.Agent.builder(com.ibm.watsonhealth.fhir.model.type.Boolean.TRUE).build());
        AuditEvent auditEvent = AuditEvent
                .builder(Coding.builder().code(Code.of("99")).build(), Instant.of(ZonedDateTime.now()), listAgents,
                        AuditEvent.Source
                                .builder(Reference.builder()
                                        .identifier(Identifier.builder().value(string("Device/1")).build()).build())
                                .build())
                .entity(AuditEvent.Entity.builder().detail(AuditEvent.Entity.Detail
                        .builder(string("Base64Binary test"), Base64Binary.builder().value(value).build()).build())
                        .build())
                .build();

        // Persist the AuditEvent
        Entity<AuditEvent> entity = Entity.entity(auditEvent, MediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("AuditEvent").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        if (DEBUG_JSON) {
            FHIRUtil.write(auditEvent, Format.JSON, System.out);
        }

        // Retrieve the AuditEvent
        String auditEventId = getLocationLogicalId(response);
        response = target.path("AuditEvent/" + auditEventId).request(MediaType.APPLICATION_FHIR_JSON).get();
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
        listCoding.add(Coding.builder().code(Code.of(MediaType.APPLICATION_FHIR_JSON)).build());
        Parameters parameters = Parameters.builder()
                .parameter(Parameters.Parameter.builder(string("base64BinaryParameterTest"))
                        .value(Base64Binary.builder().value(valueParameter).build()).build())
                .parameter(Parameters.Parameter.builder(string("resourceParameterTest"))
                        .resource(Person.builder().active(com.ibm.watsonhealth.fhir.model.type.Boolean.FALSE)
                                .build())
                        .build())
                .parameter(Parameters.Parameter.builder(string("signatureParameterTest"))
                        .value(Signature
                                .builder(listCoding, Instant.of(ZonedDateTime.now()),
                                        Reference.builder().type(Uri.of("1.2.840.10065.1.12.1.1"))
                                                .reference(string("Patient/777")).build())
                                .data(Base64Binary.builder().value(valueSignature).build()).build())
                        .build())
                .build();

        if (DEBUG_JSON) {
            FHIRUtil.write(parameters, Format.JSON, System.out);
        }

        // Persist the Parameters resource
        Entity<Parameters> entity = Entity.entity(parameters, MediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Parameters").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Retrieve the Parameters
        String parametersId = getLocationLogicalId(response);
        response = target.path("Parameters/" + parametersId).request(MediaType.APPLICATION_FHIR_JSON).get();
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
        listAgents.add(AuditEvent.Agent.builder(com.ibm.watsonhealth.fhir.model.type.Boolean.FALSE).build());
        AuditEvent auditEvent = AuditEvent
                .builder(Coding.builder().code(Code.of("99")).build(), Instant.of(ZonedDateTime.now()), listAgents,
                        AuditEvent.Source
                                .builder(Reference.builder()
                                        .identifier(Identifier.builder().value(string("Device/1")).build()).build())
                                .build())
                .extension(Extension.builder("http://ibm.com/watsonhealth/fhir/AuditEvent/testExtension")
                        .value(Base64Binary.builder().value(value).build()).build())
                .build();

        // Persist the AuditEvent
        Entity<AuditEvent> entity = Entity.entity(auditEvent, MediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("AuditEvent").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        if (DEBUG_JSON) {
            FHIRUtil.write(auditEvent, Format.JSON, System.out);
        }

        // Retrieve the AuditEvent
        String auditEventId = getLocationLogicalId(response);
        response = target.path("AuditEvent/" + auditEventId).request(MediaType.APPLICATION_FHIR_JSON).get();
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
