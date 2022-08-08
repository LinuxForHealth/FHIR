/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.operation.versions;

import static org.linuxforhealth.fhir.core.FHIRVersionParam.VERSION_40;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.linuxforhealth.fhir.config.FHIRConfigHelper;
import org.linuxforhealth.fhir.config.FHIRConfiguration;
import org.linuxforhealth.fhir.core.FHIRVersionParam;
import org.linuxforhealth.fhir.exception.FHIROperationException;
import org.linuxforhealth.fhir.model.resource.OperationDefinition;
import org.linuxforhealth.fhir.model.resource.Parameters;
import org.linuxforhealth.fhir.model.resource.Parameters.Parameter;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.registry.FHIRRegistry;
import org.linuxforhealth.fhir.search.util.SearchHelper;
import org.linuxforhealth.fhir.server.spi.operation.AbstractOperation;
import org.linuxforhealth.fhir.server.spi.operation.FHIROperationContext;
import org.linuxforhealth.fhir.server.spi.operation.FHIROperationUtil;
import org.linuxforhealth.fhir.server.spi.operation.FHIRResourceHelpers;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonBuilderFactory;
import jakarta.json.JsonObject;

/**
 * Implementation of the $versions operation, which returns the list of supported FHIR versions,
 * along with the default FHIR version.
 */
public class VersionsOperation extends AbstractOperation {

    private static final JsonBuilderFactory JSON_BUILDER_FACTORY = Json.createBuilderFactory(null);
    private static final String ACCEPT_HEADER = "Accept";
    private static final String PARAM_DEFAULT = "default";
    private static final String PARAM_VERSION = "version";
    private static final String PARAM_VERSIONS = "versions";
    private static final int STATUS_OK = 200;

    public VersionsOperation() {
        super();
    }

    @Override
    protected OperationDefinition buildOperationDefinition() {
        return FHIRRegistry.getInstance().getResource("http://hl7.org/fhir/OperationDefinition/CapabilityStatement-versions",
                OperationDefinition.class);
    }

    @Override
    protected Parameters doInvoke(FHIROperationContext operationContext, Class<? extends Resource> resourceType,
            String logicalId, String versionId, Parameters parameters, FHIRResourceHelpers resourceHelper, SearchHelper searchHelper)
            throws FHIROperationException {
        try {
            List<String> versions = getFhirVersions();
            String defaultVersion = getDefaultFhirVersion();

            // Build and return response based on the Accept header
            // This allows responses to be in any of the following formats:
            //   - application/fhir+json
            //   - application/fhir+xml
            //   - application/json (for convenience of non-FHIR clients)
            //   - application/xml (for convenience of non-FHIR clients)
            String outputFormat = getNonFhirOutputFormat(operationContext);
            if (MediaType.APPLICATION_JSON.equals(outputFormat)) {
                return buildJsonResponse(operationContext, versions, defaultVersion);
            } else if (MediaType.APPLICATION_XML.equals(outputFormat)) {
                return buildXmlResponse(operationContext, versions, defaultVersion);
            } else {
                return buildFhirResponse(versions, defaultVersion);
            }
        } catch (Throwable t) {
            throw new FHIROperationException("Unexpected error occurred while processing request for operation '"
                    + getName() + "': " + getCausedByMessage(t), t);
        }
    }

    /**
     * Gets all FHIR versions supported by the server.
     * @return the list of FHIR versions
     */
    private List<String> getFhirVersions() {
        return Stream.of(FHIRVersionParam.values()).map(k -> k.value()).collect(Collectors.toList());
    }

    /**
     * Gets the default FHIR version based on the server configuration.
     * @return the default FHIR version
     */
    private String getDefaultFhirVersion() {
        String fhirVersionString = FHIRConfigHelper.getStringProperty(FHIRConfiguration.PROPERTY_DEFAULT_FHIR_VERSION, VERSION_40.value());
        return FHIRVersionParam.from(fhirVersionString).value();
    }

    /**
     * Builds a response when output is a FHIR format.
     * @param versions the supported FHIR versions
     * @param defaultVersion the default FHIR version
     * @return the parameters object
     */
    private Parameters buildFhirResponse(List<String> versions, String defaultVersion) {
        Parameters.Builder builder = Parameters.builder();
        for (String version : versions) {
            builder.parameter(Parameter.builder()
                .name(PARAM_VERSION)
                .value(Code.of(version))
                .build());
        }
        builder.parameter(Parameter.builder()
            .name(PARAM_DEFAULT)
            .value(Code.of(defaultVersion))
            .build());
        return builder.build();
    }

    /**
     * Builds a response when output is JSON (non-FHIR).
     * @param operationContext the operation context
     * @param versions the supported FHIR versions
     * @param defaultVersion the default FHIR version
     * @return a parameters object that will not be the response entity
     */
    private Parameters buildJsonResponse(FHIROperationContext operationContext, List<String> versions, String defaultVersion) {
        // Build the response
        JsonArrayBuilder versionsJsonArray = JSON_BUILDER_FACTORY.createArrayBuilder();
        for (String version : versions) {
            versionsJsonArray.add(version);
        }
        JsonObject jsonObject = JSON_BUILDER_FACTORY.createObjectBuilder()
                .add(PARAM_VERSIONS, versionsJsonArray)
                .add(PARAM_DEFAULT, defaultVersion)
                .build();
        Response response = Response.status(STATUS_OK).entity(jsonObject).build();
        // Set properties in the operation context that are used to return this response instead of Parameters
        operationContext.setProperty(FHIROperationContext.PROPNAME_RESPONSE, response);
        operationContext.setProperty(FHIROperationContext.PROPNAME_STATUS_TYPE, STATUS_OK);
        return FHIROperationUtil.getOutputParameters(null);
    }

    /**
     * Builds a response when output is XML (non-FHIR).
     * @param operationContext the operation context
     * @param versions the supported FHIR versions
     * @param defaultVersion the default FHIR version
     * @return a parameters object that will not be the response entity
     * @throws ParserConfigurationException
     */
    private Parameters buildXmlResponse(FHIROperationContext operationContext, List<String> versions, String defaultVersion) throws ParserConfigurationException {
        // Build the response
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element versionsElement = doc.createElement(PARAM_VERSIONS);
        doc.appendChild(versionsElement);
        for (String version : versions) {
            Element versionElement = doc.createElement(PARAM_VERSION);
            versionElement.setTextContent(version);
            versionsElement.appendChild(versionElement);
        }
        Element defaultElement = doc.createElement(PARAM_DEFAULT);
        defaultElement.setTextContent(defaultVersion);
        versionsElement.appendChild(defaultElement);
        Response response = Response.status(STATUS_OK).entity(doc).build();
        // Set properties in the operation context that are used to return this response instead of Parameters
        operationContext.setProperty(FHIROperationContext.PROPNAME_RESPONSE, response);
        operationContext.setProperty(FHIROperationContext.PROPNAME_STATUS_TYPE, STATUS_OK);
        return FHIROperationUtil.getOutputParameters(null);
    }

    /**
     * Gets the non-FHIR format for returning output. If the first Accept header
     * is either 'application/json' or 'application/xml', the output is returned in
     * that non-FHIR format.
     * @param operationContext the operation context
     * @return 'application/json', 'application/xml', or null
     */
    private String getNonFhirOutputFormat(FHIROperationContext operationContext) {
        String headerString = operationContext.getHeaderString(ACCEPT_HEADER);
        if (headerString != null) {
            for (String headerStringElement : headerString.split(",")) {
                String mediaType = headerStringElement.split(";", 2)[0].trim();
                if (MediaType.APPLICATION_JSON.equals(mediaType)) {
                    return MediaType.APPLICATION_JSON;
                }
                if (MediaType.APPLICATION_XML.equals(mediaType)) {
                    return MediaType.APPLICATION_XML;
                }
            }
        }
        return null;
    }

    /**
     * Gets the caused-by message.
     * @param throwable the throwable
     * @return the message
     */
    private String getCausedByMessage(Throwable throwable) {
        return throwable.getClass().getName() + ": " + throwable.getMessage();
    }
}