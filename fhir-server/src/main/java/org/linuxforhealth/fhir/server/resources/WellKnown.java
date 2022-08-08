/*
 * (C) Copyright IBM Corp. 2016, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.resources;

import static org.linuxforhealth.fhir.config.FHIRConfiguration.PROPERTY_SECURITY_OAUTH_AUTH_URL;
import static org.linuxforhealth.fhir.config.FHIRConfiguration.PROPERTY_SECURITY_OAUTH_INTROSPECT_URL;
import static org.linuxforhealth.fhir.config.FHIRConfiguration.PROPERTY_SECURITY_OAUTH_MANAGE_URL;
import static org.linuxforhealth.fhir.config.FHIRConfiguration.PROPERTY_SECURITY_OAUTH_REG_URL;
import static org.linuxforhealth.fhir.config.FHIRConfiguration.PROPERTY_SECURITY_OAUTH_REVOKE_URL;
import static org.linuxforhealth.fhir.config.FHIRConfiguration.PROPERTY_SECURITY_OAUTH_SMART_CAPABILITIES;
import static org.linuxforhealth.fhir.config.FHIRConfiguration.PROPERTY_SECURITY_OAUTH_SMART_SCOPES;
import static org.linuxforhealth.fhir.config.FHIRConfiguration.PROPERTY_SECURITY_OAUTH_TOKEN_URL;
import static org.linuxforhealth.fhir.server.util.IssueTypeToHttpStatusMapper.issueListToStatus;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.linuxforhealth.fhir.config.FHIRConfigHelper;
import org.linuxforhealth.fhir.exception.FHIROperationException;
import org.linuxforhealth.fhir.model.type.code.IssueType;
import org.linuxforhealth.fhir.server.util.RestAuditLogger;

@Path("/.well-known")
@Produces(MediaType.APPLICATION_JSON)
public class WellKnown extends FHIRResource {
    private static final Logger log =
            java.util.logging.Logger.getLogger(WellKnown.class.getName());

    public WellKnown() throws Exception {
        super();
    }

    @GET
    @Path("smart-configuration")
    public Response smartConfig() throws ClassNotFoundException {
        log.entering(this.getClass().getName(), "smartConfig()");
        Date startTime = new Date();
        String errMsg = "Caught exception while processing 'metadata' request.";

        try {
            checkInitComplete();

            JsonObject smartConfig = getSmartConfig();
            RestAuditLogger.logMetadata(httpServletRequest, startTime, new Date(), Response.Status.OK);

            return Response.ok().entity(smartConfig).build();
        } catch (FHIROperationException e) {
            log.log(Level.SEVERE, errMsg, e);
            return exceptionResponse(e, issueListToStatus(e.getIssues()));
        } catch (Exception e) {
            log.log(Level.SEVERE, errMsg, e);
            return exceptionResponse(e, Response.Status.INTERNAL_SERVER_ERROR);
        } finally {
            log.exiting(this.getClass().getName(), "smartConfig()");
        }
    }

    private JsonObject getSmartConfig() throws FHIROperationException {
        try {
            return buildSmartConfig();
        } catch (Throwable t) {
            String msg = "An error occurred while constructing the Conformance statement.";
            log.log(Level.SEVERE, msg, t);
            throw buildRestException(msg, IssueType.EXCEPTION);
        }
    }

    /**
     * Builds a CapabilityStatement resource instance which describes this server.
     *
     * @throws Exception
     */
    private JsonObject buildSmartConfig() throws Exception {
        String actualHost = new URI(getRequestUri()).getHost();

        String authURLTemplate = null;
        String tokenURLTemplate = null;
        String regURLTemplate = null;
        String manageURLTemplate = null;
        String introspectURLTemplate = null;
        String revokeURLTemplate = null;
        List<String> supportedScopes = null;
        List<String> capabilities = null;

        try {
            authURLTemplate = FHIRConfigHelper.getStringProperty(PROPERTY_SECURITY_OAUTH_AUTH_URL, "");
            tokenURLTemplate = FHIRConfigHelper.getStringProperty(PROPERTY_SECURITY_OAUTH_TOKEN_URL, "");
            regURLTemplate = FHIRConfigHelper.getStringProperty(PROPERTY_SECURITY_OAUTH_REG_URL, "");
            manageURLTemplate = FHIRConfigHelper.getStringProperty(PROPERTY_SECURITY_OAUTH_MANAGE_URL, "");
            introspectURLTemplate = FHIRConfigHelper.getStringProperty(PROPERTY_SECURITY_OAUTH_INTROSPECT_URL, "");
            revokeURLTemplate = FHIRConfigHelper.getStringProperty(PROPERTY_SECURITY_OAUTH_REVOKE_URL, "");
            supportedScopes = FHIRConfigHelper.getStringListProperty(PROPERTY_SECURITY_OAUTH_SMART_SCOPES);
            capabilities = FHIRConfigHelper.getStringListProperty(PROPERTY_SECURITY_OAUTH_SMART_CAPABILITIES);
            if (capabilities == null) {
                // Set an empty list since capabilities is a required field
                capabilities = new ArrayList<>();
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "An error occurred while reading the OAuth/SMART properties", e);
        }
        String authURL = authURLTemplate.replaceAll("<host>", actualHost);
        String tokenURL = tokenURLTemplate.replaceAll("<host>", actualHost);
        String regURL = regURLTemplate.replaceAll("<host>", actualHost);
        String manageURL = manageURLTemplate.replaceAll("<host>", actualHost);
        String introspectURL = introspectURLTemplate.replaceAll("<host>", actualHost);
        String revokeURL = revokeURLTemplate.replaceAll("<host>", actualHost);

        JsonObjectBuilder responseBuilder = Json.createObjectBuilder();

        responseBuilder.add("authorization_endpoint", authURL); // required
        responseBuilder.add("token_endpoint", tokenURL); // required

        if (regURL != null && !regURL.isEmpty()) {
            responseBuilder.add("registration_endpoint", regURL); // recommended
        }
        if (manageURL != null && !manageURL.isEmpty()) {
            responseBuilder.add("management_endpoint", manageURL); // recommended
        }
        if (introspectURL != null && !introspectURL.isEmpty()) {
            responseBuilder.add("introspection_endpoint", introspectURL); // recommended
        }
        if (revokeURL != null && !revokeURL.isEmpty()) {
            responseBuilder.add("revocation_endpoint", revokeURL); // recommended
        }

        if (supportedScopes != null && !supportedScopes.isEmpty()) {
            responseBuilder.add("scopes_supported", Json.createArrayBuilder(supportedScopes).build()); // recommended
        }

        responseBuilder.add("response_types", Json.createArrayBuilder() // recommended
                .add("code")
                .add("token")
                .add("id_token token")
                .build());

        responseBuilder.add("capabilities", Json.createArrayBuilder(capabilities).build()); // required

// : RECOMMENDED, URL where an end-user can view which applications currently have access to data and can make adjustments to these access rights.
//  : RECOMMENDED, URL to a server’s introspection endpoint that can be used to validate a token.
//  : RECOMMENDED, URL to a server’s revoke endpoint that can be used to revoke a token.

        return responseBuilder.build();
    }
}
