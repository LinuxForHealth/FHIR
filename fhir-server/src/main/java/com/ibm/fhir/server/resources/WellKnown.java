/*
 * (C) Copyright IBM Corp. 2016, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.resources;

import static com.ibm.fhir.config.FHIRConfiguration.PROPERTY_SECURITY_OAUTH_AUTHURL;
import static com.ibm.fhir.config.FHIRConfiguration.PROPERTY_SECURITY_OAUTH_REGURL;
import static com.ibm.fhir.config.FHIRConfiguration.PROPERTY_SECURITY_OAUTH_SMART_SCOPES;
import static com.ibm.fhir.config.FHIRConfiguration.PROPERTY_SECURITY_OAUTH_TOKENURL;
import static com.ibm.fhir.server.util.IssueTypeToHttpStatusMapper.issueListToStatus;

import java.net.URI;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.server.util.RestAuditLogger;

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

        String regURLTemplate = null;
        String authURLTemplate = null;
        String tokenURLTemplate = null;
        List<String> supportedScopes = null;
        try {
            regURLTemplate = fhirConfig.getStringProperty(PROPERTY_SECURITY_OAUTH_REGURL, "");
            authURLTemplate = fhirConfig.getStringProperty(PROPERTY_SECURITY_OAUTH_AUTHURL, "");
            tokenURLTemplate = fhirConfig.getStringProperty(PROPERTY_SECURITY_OAUTH_TOKENURL, "");
            Object[] smartScopeObjs = fhirConfig.getArrayProperty(PROPERTY_SECURITY_OAUTH_SMART_SCOPES);
            if (smartScopeObjs != null) {
                supportedScopes = Arrays.asList(Arrays.copyOf(smartScopeObjs, smartScopeObjs.length, String[].class));
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "An error occurred while adding OAuth URLs to the response", e);
        }
        String regURL = regURLTemplate.replaceAll("<host>", actualHost);
        String authURL = authURLTemplate.replaceAll("<host>", actualHost);
        String tokenURL = tokenURLTemplate.replaceAll("<host>", actualHost);

        JsonObjectBuilder responseBuilder = Json.createObjectBuilder();

        if (regURL != null && !regURL.isEmpty()) {
            responseBuilder.add("registration_endpoint", regURL); // recommended
        }

        responseBuilder.add("authorization_endpoint", authURL); // required
        responseBuilder.add("token_endpoint", tokenURL); // required

        if (supportedScopes != null && !supportedScopes.isEmpty()) {
            responseBuilder.add("scopes_supported", Json.createArrayBuilder(supportedScopes).build()); // recommended
        }

        responseBuilder.add("response_types", Json.createArrayBuilder() // recommended
                .add("code")
                .add("token")
                .add("id_token token")
                .build());
        responseBuilder.add("capabilities", Json.createArrayBuilder() // required
                .add("launch-standalone")
                .add("client-public")
                .add("client-confidential-symmetric")
                .add("permission-offline")
                // TODO: make this configurable
                .add("context-standalone-patient")
                .add("permission-patient")
//                .add("context-standalone-encounter")
//                .add("permission-user")
                .build());
// management_endpoint: RECOMMENDED, URL where an end-user can view which applications currently have access to data and can make adjustments to these access rights.
// introspection_endpoint : RECOMMENDED, URL to a server’s introspection endpoint that can be used to validate a token.
// revocation_endpoint : RECOMMENDED, URL to a server’s revoke endpoint that can be used to revoke a token.

        return responseBuilder.build();
    }
}
