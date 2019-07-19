/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.server.test;

import static com.ibm.watsonhealth.fhir.model.type.String.string;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;
import static org.testng.AssertJUnit.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.ClientEndpointConfig.Configurator;
import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.glassfish.tyrus.client.SslContextConfigurator;
import org.glassfish.tyrus.client.SslEngineConfigurator;
import org.testng.annotations.BeforeClass;

import com.ibm.watsonhealth.fhir.client.FHIRClient;
import com.ibm.watsonhealth.fhir.client.FHIRClientFactory;
import com.ibm.watsonhealth.fhir.client.FHIRResponse;
import com.ibm.watsonhealth.fhir.core.FHIRUtilities;
import com.ibm.watsonhealth.fhir.model.resource.CapabilityStatement;
import com.ibm.watsonhealth.fhir.model.resource.CapabilityStatement.Rest.Resource.Interaction;
import com.ibm.watsonhealth.fhir.model.resource.CapabilityStatement.Rest;

import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.HumanName;
import com.ibm.watsonhealth.fhir.model.type.IssueSeverity;
import com.ibm.watsonhealth.fhir.model.type.IssueType;

import com.ibm.watsonhealth.fhir.model.resource.OperationOutcome;
import com.ibm.watsonhealth.fhir.model.resource.Patient;
import com.ibm.watsonhealth.fhir.model.type.RestfulCapabilityMode;
import com.ibm.watsonhealth.fhir.model.type.SystemRestfulInteraction;
import com.ibm.watsonhealth.fhir.model.type.TypeRestfulInteraction;
import com.ibm.watsonhealth.fhir.persistence.test.common.FHIRModelTestBase;
import com.ibm.watsonhealth.fhir.server.test.FHIRNotificationServiceClientEndpoint;

/**
 * Base class for fhir-server unit tests.
 */
public abstract class FHIRServerTestBase extends FHIRModelTestBase {

    protected FHIRClient client = null;

    // Default values for test properties.
    private static final String DEFAULT_WEBSOCKET_URL = "wss://localhost:9443/fhir-server/api/v1/notification";
    private static final String DEFAULT_KAFKA_CONNINFO = "localhost:9092";
    private static final String DEFAULT_KAFKA_TOPICNAME = "fhirNotifications";

    // Default values for FHIRClient properties that we use here.
    private static final String DEFAULT_TRUSTSTORE_LOCATION = "fhirClientTruststore.jks";
    private static final String DEFAULT_TRUSTSTORE_PASSWORD = "password";
    private static final String DEFAULT_KEYSTORE_LOCATION = "fhirClientKeystore.jks";
    private static final String DEFAULT_KEYSTORE_PASSWORD = "password";
    private static final String DEFAULT_USERNAME = "fhiruser";
    private static final String DEFAULT_PASSWORD = "fhiruser";

    // Constants that define test property names.
    private static final String PROPNAME_WEBSOCKET_URL = "test.websocket.url";
    private static final String PROPNAME_KAFKA_CONNINFO = "test.kafka.connectionInfo";
    private static final String PROPNAME_KAFKA_TOPICNAME = "test.kafka.topicName";

    // for secure WebSocket Client
    private static final String PROPNAME_SSL_ENGINE_CONFIGURATOR = "org.glassfish.tyrus.client.sslEngineConfigurator";

    // These are values of test-specific properties.
    private String websocketUrl = null;
    private String kafkaConnectionInfo = null;
    private String kafkaTopicName = null;

    // These are values of FHIRClient properties that we need here also.
    private String fhirUser = null;
    private String fhirPassword = null;
    private String tsLocation = null;
    private String tsPassword = null;
    private String ksLocation = null;
    private String ksPassword = null;

    protected static final String MEDIATYPE_JSON = "application/json";
    protected static final String MEDIATYPE_JSON_FHIR = "application/json+fhir";
    protected static final String MEDIATYPE_XML = "application/xml";
    protected static final String MEDIATYPE_XML_FHIR = "application/xml+fhir";

    private CapabilityStatement conformanceStmt = null;

    public FHIRServerTestBase() {
    }

    protected String getWebSocketURL() {
        return websocketUrl;
    }

    public String getKafkaConnectionInfo() {
        return kafkaConnectionInfo;
    }

    public String getKafkaTopicName() {
        return kafkaTopicName;
    }

    private String getFhirUser() {
        return fhirUser;
    }

    private String getFhirPassword() {
        return fhirPassword;
    }

    private String getTsLocation() {
        return tsLocation;
    }

    private String getTsPassword() {
        return tsPassword;
    }

    private String getKsLocation() {
        return ksLocation;
    }

    private String getKsPassword() {
        return ksPassword;
    }

    /**
     * We'll resolve all the supported test properties. We have two ways of setting
     * properties: 1) store them in a file called "test.properties" which is in the
     * classpath. 2) set each individual property as a JVM system property.
     *
     * Supported property names: test.host test.port test.urlprefix
     */
    @BeforeClass
    public void setUp() throws Exception {
        Properties properties = new Properties();
        try {
            // Read test.properties file if present
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("test.properties");
            if (is != null) {
                properties.load(is);
                is.close();
            }
        } catch (Exception e) {
            // Ignore any errors while trying to load the file.
        }

        // Create our FHIRClient instance based on the properties we just read in.
        client = FHIRClientFactory.getClient(properties);

        websocketUrl = getProperty(properties, PROPNAME_WEBSOCKET_URL, DEFAULT_WEBSOCKET_URL);

        // Retrieve the info needed by a Kafka consumer.
        kafkaConnectionInfo = getProperty(properties, PROPNAME_KAFKA_CONNINFO, DEFAULT_KAFKA_CONNINFO);
        kafkaTopicName = getProperty(properties, PROPNAME_KAFKA_TOPICNAME, DEFAULT_KAFKA_TOPICNAME);

        fhirUser = getProperty(properties, FHIRClient.PROPNAME_CLIENT_USERNAME, DEFAULT_USERNAME);
        fhirPassword = FHIRUtilities
                .decode(getProperty(properties, FHIRClient.PROPNAME_CLIENT_PASSWORD, DEFAULT_PASSWORD));
        tsLocation = getProperty(properties, FHIRClient.PROPNAME_TRUSTSTORE_LOCATION, DEFAULT_TRUSTSTORE_LOCATION);
        tsPassword = FHIRUtilities
                .decode(getProperty(properties, FHIRClient.PROPNAME_TRUSTSTORE_PASSWORD, DEFAULT_TRUSTSTORE_PASSWORD));
        ksLocation = getProperty(properties, FHIRClient.PROPNAME_KEYSTORE_LOCATION, DEFAULT_KEYSTORE_LOCATION);
        ksPassword = FHIRUtilities
                .decode(getProperty(properties, FHIRClient.PROPNAME_KEYSTORE_LOCATION, DEFAULT_KEYSTORE_PASSWORD));
    }

    /**
     * Tries to find the property named <propertyName> first as a System property,
     * then as a property within the <properties> object. If neither are found, then
     * the <defaultValue> is returned.
     *
     * @param properties   Properties object containing the properties loaded from
     *                     our properties file.
     * @param propertyName the name of the property to retrieve
     * @param defaultValue the default value to use if the property can't be found
     */
    protected String getProperty(Properties properties, String propertyName, String defaultValue) {
        return System.getProperty(propertyName, properties.getProperty(propertyName, defaultValue));
    }

    //
    // JAX-RS 2.0 utility functions.
    //

    /**
     * Returns a WebTarget to be used for a REST API invocation.
     */
    protected WebTarget getWebTarget() {
        try {
            return client.getWebTarget();
        } catch (Throwable t) {
            fail("Unexpected exception while retrieving WebTarget: " + t);

            // This is here to appease the java compiler so we don't need to declare a
            // throws clause.
            throw new RuntimeException("Shouldn't get here as the fail() will throw an exception...");
        }
    }

    /**
     * Returns the FHIRClient instance being used for this testcase instance.
     */
    protected FHIRClient getFHIRClient() {
        return client;
    }

    /**
     * Creates and returns an "endpoint" to be used to receive notifications via a
     * websocket.
     */
    protected FHIRNotificationServiceClientEndpoint getWebsocketClientEndpoint() {
        try {
            FHIRNotificationServiceClientEndpoint endpoint = new FHIRNotificationServiceClientEndpoint();
            ClientEndpointConfig config = ClientEndpointConfig.Builder.create().configurator(new Configurator() {
                public void beforeRequest(Map<String, List<String>> headers) {
                    String userpw = getFhirUser() + ":" + getFhirPassword();
                    String encoding = Base64.getEncoder().encodeToString(userpw.getBytes());
                    List<String> values = new ArrayList<String>();
                    values.add("Basic " + encoding);
                    headers.put("Authorization", values);
                }
            }).build();

            String webSocketURL = getWebSocketURL();
            if (webSocketURL.startsWith("wss")) {
                String tsLoc = getAbsoluteFilename(getTsLocation());
                if (tsLoc == null) {
                    throw new FileNotFoundException("Truststore file not found: " + getTsLocation());
                }

                String ksLoc = getAbsoluteFilename(getKsLocation());
                if (ksLoc == null) {
                    throw new FileNotFoundException("Keystore file not found: " + getKsLocation());
                }

                System.getProperties().put(SslContextConfigurator.KEY_STORE_FILE, ksLoc);
                System.getProperties().put(SslContextConfigurator.KEY_STORE_PASSWORD, getKsPassword());
                System.getProperties().put(SslContextConfigurator.TRUST_STORE_FILE, tsLoc);
                System.getProperties().put(SslContextConfigurator.TRUST_STORE_PASSWORD, getTsPassword());

                final SslContextConfigurator defaultConfig = new SslContextConfigurator();
                defaultConfig.retrieve(System.getProperties());
                SslEngineConfigurator sslEngineConfigurator = new SslEngineConfigurator(defaultConfig, true, false,
                        false);
                sslEngineConfigurator.setHostVerificationEnabled(false);
                config.getUserProperties().put(PROPNAME_SSL_ENGINE_CONFIGURATOR, sslEngineConfigurator);
            }
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(endpoint, config, new URI(webSocketURL));
            return endpoint;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Common function to invoke the 'metadata' operation and return the
     * CapabilityStatement object.
     */
    protected CapabilityStatement retrieveConformanceStatement() throws Exception {
        if (conformanceStmt == null) {
            FHIRClient client = getFHIRClient();
            FHIRResponse response = client.metadata();
            assertNotNull(response);
            assertEquals(200, response.getStatus());

            conformanceStmt = response.getResource(CapabilityStatement.class);
            assertNotNull(conformanceStmt);
        }

        return conformanceStmt;
    }

    /**
     * Determines whether or not the server supports the "update/create" feature by
     * examining the conformance statement.
     */
    protected boolean isUpdateCreateSupported() throws Exception {
        Boolean updateCreateSupported = Boolean.FALSE;
        CapabilityStatement conf = retrieveConformanceStatement();

        List<CapabilityStatement.Rest> restList = conf.getRest();
        if (restList != null) {
            CapabilityStatement.Rest rest = restList.get(0);
            if (rest != null) {
                assertEquals(RestfulCapabilityMode.SERVER, rest.getMode().getValue());
                List<Rest.Resource> resources = rest.getResource();
                if (resources != null) {
                    Rest.Resource resource = resources.get(0);
                    if (resource != null) {
                        updateCreateSupported = resource.getUpdateCreate().getValue();
                    }
                }
            }
        }

        return (updateCreateSupported != null ? updateCreateSupported.booleanValue() : false);
    }

    /**
     * Determines whether or not the server supports the "delete" operation by
     * examining the conformance statement.
     */
    protected boolean isDeleteSupported() throws Exception {
        CapabilityStatement conf = retrieveConformanceStatement();

        List<CapabilityStatement.Rest> restList = conf.getRest();
        if (restList != null) {
            CapabilityStatement.Rest rest = restList.get(0);
            if (rest != null) {
                assertEquals(RestfulCapabilityMode.SERVER, rest.getMode().getValue());
                List<Rest.Resource> resources = rest.getResource();
                if (resources != null) {
                    Rest.Resource resource = resources.get(0);
                    if (resource != null) {
                        List<Interaction> interactions = resource.getInteraction();
                        for (Interaction interaction : interactions) {
                            if (interaction.getCode().getValue() == TypeRestfulInteraction.DELETE.getValue()) {
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    protected boolean isComparmentSearchSupported() throws Exception {
        boolean compartmentSearchSupported = false;
        CapabilityStatement conf = retrieveConformanceStatement();

        for (Extension e : conf.getExtension()) {
            if (e.getUrl().contains("persistenceType") && (((com.ibm.watsonhealth.fhir.model.type.String) e.getValue())
                    .getValue().contains("FHIRPersistenceJDBCImpl")
                    || ((com.ibm.watsonhealth.fhir.model.type.String) e.getValue()).getValue()
                            .contains("FHIRPersistenceJDBCNormalizedImpl"))) {
                compartmentSearchSupported = true;
                break;
            }
        }
        return compartmentSearchSupported;
    }

    protected boolean isSearchAllSupported() throws Exception {
        return isComparmentSearchSupported();
    }

    protected boolean isSortingSupported() throws Exception {
        return isComparmentSearchSupported();
    }

    /**
     * Determines whether or not the server supports the "update/create" feature by
     * examining the conformance statement.
     */
    protected boolean isTransactionSupported() throws Exception {
        SystemRestfulInteraction transactionMode = null;
        CapabilityStatement conf = retrieveConformanceStatement();

        List<CapabilityStatement.Rest> restList = conf.getRest();
        if (restList != null) {
            CapabilityStatement.Rest rest = restList.get(0);
            if (rest != null) {
                assertEquals(RestfulCapabilityMode.SERVER, rest.getMode().getValue());
                if (rest.getInteraction() != null) {
                    transactionMode = rest.getInteraction().get(0).getCode();
                }
            }
        }

        if (transactionMode == null) {
            transactionMode = SystemRestfulInteraction.BATCH;
        }

        boolean txnSupported = false;

        if (transactionMode.getValue().contentEquals(SystemRestfulInteraction.TRANSACTION.getValue())) {
            txnSupported = true;
        }

        return txnSupported;
    }

    //
    // Some homegrown assert-type common functions.
    //

    /**
     * Verify that the status code in the Response is equal to the expected status
     * code.
     *
     * @param response           the Response to verify
     * @param expectedStatusCode the expected status code value
     */
    protected void assertResponse(Response response, int expectedStatusCode) {
        assertNotNull(response);
        assertEquals(expectedStatusCode, response.getStatus());
    }

    /**
     * Verify that a FHIRClient response has the expected status code.
     *
     * @param response           the FHIRResponse that contains the response for a
     *                           FHIRClient API invocation
     * @param expectedStatusCode the status code that we expect in the response
     */
    protected void assertResponse(FHIRResponse response, int expectedStatusCode) {
        assertNotNull(response);
        assertEquals(expectedStatusCode, response.getStatus());
    }

    /**
     * Validate the specified OperationOutcome object to make sure it contains an
     * exception.
     *
     * @param msgPart a string which should be found in the exception message.
     */
    protected void assertExceptionOperationOutcome(OperationOutcome oo, String msgPart) {
        assertNotNull(oo);

        // Make sure the OperationOutcomeIssue has a message containing 'msgPart'.
        assertNotNull(oo.getIssue());
        assertEquals(1, oo.getIssue().size());
        OperationOutcome.Issue ooi = oo.getIssue().get(0);
        assertNotNull(ooi);
        assertNotNull(ooi.getCode());
        assertNotNull(ooi.getCode().getValue());

        assertNotNull(ooi.getSeverity());
        assertNotNull(ooi.getSeverity().getValue());

        assertNotNull(ooi.getDiagnostics());
        String msg = ooi.getDiagnostics().getValue();
        assertNotNull(msg);
        assertTrue(msg.contains(msgPart));
    }

    protected void assertValidationOperationOutcome(OperationOutcome oo, String msgPart) {
        assertNotNull(oo);

        // Make sure that we can find the 'msgPart' in one of the OperationOutcomeIssue
        // objects.
        boolean foundIt = false;
        assertNotNull(oo.getIssue());
        assertFalse(oo.getIssue().isEmpty());
        for (OperationOutcome.Issue ooi : oo.getIssue()) {
            assertNotNull(ooi.getCode());
            assertNotNull(ooi.getCode().getValue());
            assertEquals(IssueType.INVALID.getValue(), ooi.getCode().getValue());

            assertNotNull(ooi.getSeverity());
            assertNotNull(ooi.getSeverity().getValue());
            assertEquals(IssueSeverity.ERROR.getValue(), ooi.getSeverity().getValue());

            assertNotNull(ooi.getDiagnostics());
            String msg = ooi.getDiagnostics().getValue();
            assertNotNull(msg);

            if (msg.contains(msgPart)) {
                foundIt = true;
            }
        }
        assertTrue("Could not find '" + msgPart + "' in OperationOutcomeIssue list!", foundIt);
    }

    //
    // Misc. common functions used by testcases.
    //

    /**
     * For the specified response, this function will extract the logical id value
     * from the response's Location header. The format of a location header value
     * should be: "[base]/<resource-type>/<id>/_history/<version>"
     *
     * @param response the response object for a REST API invocation
     * @return the logical id value
     */
    protected String getLocationLogicalId(Response response) {
        String location = response.getLocation().toString();
        assertNotNull(location);
        assertFalse(location.isEmpty());
        // System.out.println("Location value: " + location);

        String[] tokens = location.split("/");
        assertNotNull(tokens);
        assertTrue(tokens.length >= 4);

        String logicalId = tokens[tokens.length - 3];
        assertNotNull(logicalId);
        assertFalse(logicalId.isEmpty());

        return logicalId;
    }

    /**
     * Returns the absolute filename associated with 'filename'.
     */
    private String getAbsoluteFilename(String filename) {
        // First, look in the classpath for the file.
        URL url = Thread.currentThread().getContextClassLoader().getResource(filename);
        if (url != null) {
            return url.getFile();
        } else {
            File f = new File(filename);
            if (f.exists()) {
                return f.getAbsolutePath();
            }
        }

        return null;
    }

    protected String[] getLocationURITokens(String locationURI) {
        String[] temp = locationURI.split("/");
        String[] tokens;
        if (temp.length > 4) {
            tokens = new String[4];
            for (int i = 0; i < 4; i++) {
                tokens[i] = temp[temp.length - 4 + i];
            }
        } else {
            tokens = temp;
        }
        return tokens;
    }
    
    protected Patient setUniqueFamilyName(Patient patient, String uniqueName) {
        List<com.ibm.watsonhealth.fhir.model.type.String> familyList = new ArrayList<com.ibm.watsonhealth.fhir.model.type.String>();
        familyList.add(string(uniqueName));
        List <HumanName> nameList = new ArrayList<HumanName>();
        for(HumanName humanName: patient.getName()) {
            nameList.add(HumanName.builder().family(string(uniqueName)).given(humanName.getGiven()).build());           
        }
        
        
        patient = patient.toBuilder().name(nameList).build();
        return patient;
    }
}
