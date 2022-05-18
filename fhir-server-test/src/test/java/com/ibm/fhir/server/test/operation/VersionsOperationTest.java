/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test.operation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.server.test.FHIRServerTestBase;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;

/**
 * This class tests the $versions operation, which returns the list of supported FHIR versions,
 * along with the default FHIR version.
 */
public class VersionsOperationTest extends FHIRServerTestBase {
    private static final String PARAM_DEFAULT = "default";
    private static final String PARAM_VERSION = "version";
    private static final String PARAM_VERSIONS = "versions";

    @Test
    public void testVersions() {
        WebTarget target = getWebTarget();
        target = target.path("/$versions");

        Response r = target.request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .get(Response.class);

        assertEquals(r.getStatus(), Status.OK.getStatusCode());
        Parameters parameters = r.readEntity(Parameters.class);
        assertNotNull(parameters);

        List<String> versions = getParameterValues(parameters, PARAM_VERSION);
        assertEquals(versions.size(), 2);
        assertTrue(versions.contains("4.0"));
        assertTrue(versions.contains("4.3"));

        List<String> defaults = getParameterValues(parameters, PARAM_DEFAULT);
        assertEquals(defaults.size(), 1);
        assertTrue(defaults.contains("4.0"));
    }

    @Test
    public void testVersions_with_default_configured() {
        WebTarget target = getWebTarget();
        target = target.path("/$versions");

        Response r = target.request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "tenant1")
                .header("X-FHIR-DSID", "study1")
                .get(Response.class);

        assertEquals(r.getStatus(), Status.OK.getStatusCode());
        Parameters parameters = r.readEntity(Parameters.class);
        assertNotNull(parameters);

        List<String> versions = getParameterValues(parameters, PARAM_VERSION);
        assertEquals(versions.size(), 2);
        assertTrue(versions.contains("4.0"));
        assertTrue(versions.contains("4.3"));

        List<String> defaults = getParameterValues(parameters, PARAM_DEFAULT);
        assertEquals(defaults.size(), 1);
        assertTrue(defaults.contains("4.3"));
    }

    @Test
    public void testVersions_with_parameters() {
        WebTarget target = getWebTarget();
        target = target.path("/$versions");

        Response r = target.request(FHIRMediaType.APPLICATION_FHIR_JSON + ";test=1")
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .get(Response.class);

        assertEquals(r.getStatus(), Status.OK.getStatusCode());
        Parameters parameters = r.readEntity(Parameters.class);
        assertNotNull(parameters);

        List<String> versions = getParameterValues(parameters, PARAM_VERSION);
        assertEquals(versions.size(), 2);
        assertTrue(versions.contains("4.0"));
        assertTrue(versions.contains("4.3"));

        List<String> defaults = getParameterValues(parameters, PARAM_DEFAULT);
        assertEquals(defaults.size(), 1);
        assertTrue(defaults.contains("4.0"));
    }

    @Test
    public void testVersions_JSON() {
        WebTarget target = getWebTarget();
        target = target.path("/$versions");

        Response r = target.request(MediaType.APPLICATION_JSON)
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .get(Response.class);

        assertEquals(r.getStatus(), Status.OK.getStatusCode());
        JsonObject jsonObject = r.readEntity(JsonObject.class);
        assertNotNull(jsonObject);

        List<String> versions = getJsonArrayStringValues(jsonObject.getJsonArray(PARAM_VERSIONS));
        assertEquals(versions.size(), 2);
        assertTrue(versions.contains("4.0"));
        assertTrue(versions.contains("4.3"));

        String defaultVersion = jsonObject.getString(PARAM_DEFAULT);
        assertEquals(defaultVersion, "4.0");
    }

    @Test
    public void testVersions_JSON_with_parameters() {
        WebTarget target = getWebTarget();
        target = target.path("/$versions");

        Response r = target.request(MediaType.APPLICATION_JSON + ";test=1")
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .get(Response.class);

        assertEquals(r.getStatus(), Status.OK.getStatusCode());
        JsonObject jsonObject = r.readEntity(JsonObject.class);
        assertNotNull(jsonObject);

        List<String> versions = getJsonArrayStringValues(jsonObject.getJsonArray(PARAM_VERSIONS));
        assertEquals(versions.size(), 2);
        assertTrue(versions.contains("4.0"));
        assertTrue(versions.contains("4.3"));

        String defaultVersion = jsonObject.getString(PARAM_DEFAULT);
        assertEquals(defaultVersion, "4.0");
    }

    @Test
    public void testVersions_XML() {
        WebTarget target = getWebTarget();
        target = target.path("/$versions");

        Response r = target.request(MediaType.APPLICATION_XML)
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .get(Response.class);

        assertEquals(r.getStatus(), Status.OK.getStatusCode());
        Document doc = r.readEntity(Document.class);
        assertNotNull(doc);
        List<Node> versionsNodes = getChildNodesByName(doc, PARAM_VERSIONS);
        assertEquals(versionsNodes.size(), 1);

        List<String> versions = getNodeStringValues(getChildNodesByName(versionsNodes.get(0), PARAM_VERSION));
        assertEquals(versions.size(), 2);
        assertTrue(versions.contains("4.0"));
        assertTrue(versions.contains("4.3"));

        List<String> defaults = getNodeStringValues(getChildNodesByName(versionsNodes.get(0), PARAM_DEFAULT));
        assertEquals(defaults.size(), 1);
        assertTrue(defaults.contains("4.0"));
    }

    @Test
    public void testVersions_XML_with_parameters() {
        WebTarget target = getWebTarget();
        target = target.path("/$versions");

        Response r = target.request(MediaType.APPLICATION_XML + ";test=1")
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .get(Response.class);

        assertEquals(r.getStatus(), Status.OK.getStatusCode());
        Document doc = r.readEntity(Document.class);
        assertNotNull(doc);
        List<Node> versionsNodes = getChildNodesByName(doc, PARAM_VERSIONS);
        assertEquals(versionsNodes.size(), 1);

        List<String> versions = getNodeStringValues(getChildNodesByName(versionsNodes.get(0), PARAM_VERSION));
        assertEquals(versions.size(), 2);
        assertTrue(versions.contains("4.0"));
        assertTrue(versions.contains("4.3"));

        List<String> defaults = getNodeStringValues(getChildNodesByName(versionsNodes.get(0), PARAM_DEFAULT));
        assertEquals(defaults.size(), 1);
        assertTrue(defaults.contains("4.0"));
    }

    private List<String> getParameterValues(Parameters parameters, String name) {
        List<String> valueStrings = new ArrayList<>();
        for (Parameter parameter : parameters.getParameter()) {
            if (name.equals(parameter.getName().getValue())) {
                valueStrings.add(parameter.getValue().as(Code.class).getValue());
            }
        }
        return valueStrings;
    }

    private List<String> getJsonArrayStringValues(JsonArray array) {
        List<String> valueStrings = new ArrayList<>();
        for (int i=0; i<array.size(); i++) {
            valueStrings.add(array.getString(i));
        }
        return valueStrings;
    }

    private List<Node> getChildNodesByName(Node node, String name) {
        List<Node> childNodes = new ArrayList<>();
        NodeList nodeList = node.getChildNodes();
        for (int i=0; i<nodeList.getLength(); i++) {
            Node childNode = nodeList.item(i);
            if (name.equals(childNode.getNodeName())) {
                childNodes.add(childNode);
            }
        }
        return childNodes;
    }

    private List<String> getNodeStringValues(List<Node> nodes) {
        List<String> valueStrings = new ArrayList<>();
        for (Node node : nodes) {
            valueStrings.add(node.getTextContent());
        }
        return valueStrings;
    }
}