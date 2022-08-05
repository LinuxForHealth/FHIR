package org.linuxforhealth.fhir.server.test.smart;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.fail;

import java.util.UUID;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;
import com.beust.jcommander.Strings;
import org.linuxforhealth.fhir.client.impl.FHIROAuth2Authenticator;
import org.linuxforhealth.fhir.core.FHIRMediaType;
import org.linuxforhealth.fhir.model.resource.OperationOutcome;
import org.linuxforhealth.fhir.model.resource.Patient;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.type.code.IssueType;
import org.linuxforhealth.fhir.server.test.FHIRServerTestBase;

public class SMARTTest extends FHIRServerTestBase {
    boolean smartEnabled = false;

    @BeforeClass
    public void testGetSmartConfiguration() {
        Response response = getWebTarget().path("/.well-known/smart-configuration").request().get();
        switch(response.getStatus()) {
        case 200:
            smartEnabled = true;
            return;
        case 401:
        case 404:
            // no smart-configuration endpoint, so SMART isn't enabled
            return;
        default:
            fail("Unexpected " + response.getStatus() + " response from /.well-known/smart-configuration");
        }
    }

    @Test
    public void testPatientEverything_invalid() throws Exception {
        if (!smartEnabled) {
            throw new SkipException("The server is not configured for SMART-on-FHIR");
        }

        String patientId = UUID.randomUUID().toString();
        String jwt = jwt(patientId, "patient/Patient.*");
        Entity<Resource> entity = Entity.entity(Patient.builder().id(patientId).build(), FHIRMediaType.APPLICATION_FHIR_JSON);

        WebTarget target = getWebTarget();
        target.register(new FHIROAuth2Authenticator(jwt));
        Response response = target.path("Patient/" + patientId).request().put(entity);
        assertEquals(response.getStatus(), 201);

        response = target.path("Patient/" + patientId + "/$everything").request().get();
        assertEquals(response.getStatus(), 403);
        OperationOutcome oo = response.readEntity(OperationOutcome.class);
        assertFalse(oo.getIssue().isEmpty());
        assertEquals(oo.getIssue().get(0).getCode(), IssueType.FORBIDDEN);
    }

    /**
     * Factory method to create JWTs that are compatible with the FHIR Server SMART support
     */
    private static String jwt(String patientId, String... scope) {
        Builder jwt = JWT.create()
                .withClaim("patient_id", patientId)
                .withClaim("scope", Strings.join(" ", scope));

        return jwt.sign(Algorithm.none());
    }
}
