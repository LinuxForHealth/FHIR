/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.client.app;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.client.FHIRClient;
import org.linuxforhealth.fhir.client.FHIRClientFactory;
import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.parser.FHIRParser;
import org.linuxforhealth.fhir.model.resource.OperationOutcome;
import org.linuxforhealth.fhir.model.resource.Resource;

/**
 * Main application for running simple FHIR client commands
 */
public class Main implements IClientInteractionContext {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    // The properties used to configure the client
    private final Properties clientProperties = new Properties();

    // The client we use to communicate with the FHIR server
    private FHIRClient client;

    // The list of interactions to perform
    private List<ClientInteraction> interactions = new ArrayList<>();

    // The most recent value of resource set from the command line or read by the client
    private Resource resource;

    // The status returned from the last interaction that was invoked
    private int lastInteractionStatus;

    // The OperationOutcome from the last interaction that was invoked
    private OperationOutcome lastOperationOutcome;

    // The location response header value from the last interaction that was invoked
    private String lastLocation;

    private boolean pretty;

    /**
     * Parse the command line arguments
     * @param args
     */
    private void parseArgs(String[] args) throws Exception {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            switch (arg) {
            case "--client-properties":
                if (++i < args.length) {
                    loadPropertyFile(args[i]);
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--property-value":
                if (++i < args.length) {
                    addPropertyValue(args[i]);
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--tenant-id":
                if (++i < args.length) {
                    final String tenantId = args[i];
                    clientProperties.put(FHIRClient.PROPNAME_TENANT_ID, tenantId);
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--resource":
                if (++i < args.length) {
                    parseResource(args[i]);
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--resource-file":
                if (++i < args.length) {
                    parseResourceFromFile(args[i]);
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--resource-stdin":
                parseResourceFromStdin();
                break;
            case "--read":
                if (++i < args.length) {
                    addReadInteraction(args[i]);
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--vread":
                if (++i < args.length) {
                    addVReadInteraction(args[i]);
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--create":
                addCreateInteraction();
                break;
            case "--update":
                if (++i < args.length) {
                    addUpdateInteraction(args[i]);
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--print":
                addPrintInteraction();
                break;
            case "--print-operation-outcome":
                addPrintOperationOutcome();
                break;
            case "--print-location":
                addPrintLocation();
                break;
            case "--set-pretty":
                if (++i < args.length) {
                    this.pretty = Boolean.parseBoolean(args[i]);
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            default:
                throw new IllegalArgumentException("Bad argument: '" + arg + "'");
            }
        }
    }

    /**
     * Read the configuration properties from the given filename
     * @param filename
     * @throws IOException
     */
    private void loadPropertyFile(String filename) throws IOException {
        try (InputStream is = new FileInputStream(filename)) {
            clientProperties.load(is);
        } catch (IOException x) {
            throw new IllegalArgumentException(x);
        }
        
    }

    /**
     * Add the property value described by the input string which should be
     * of the form "property-name = value". The String is split using the first
     * "=" character.
     * @param input
     */
    private void addPropertyValue(String input) {
        int posn = input.indexOf('=');
        if (posn > 0) {
            final String propertyName = input.substring(0, posn).trim();
            final String propertyValue = input.substring(posn + 1).trim();
            if (propertyName.isEmpty()) {
                throw new IllegalArgumentException("Property value does not specify a valid property name");
            }
            logger.info("Overriding property value: " + propertyName + " = " + propertyValue);
            clientProperties.put(propertyName, propertyValue);
        } else {
            throw new IllegalArgumentException("Invalid property value: '" + input + "'");
        }
    }

    /**
     * Configure the client based on the current properties
     */
    private void configure() throws Exception {
        client = FHIRClientFactory.getClient(this.clientProperties);
    }

    /**
     * Add a FHIR READ interaction
     * @param typeAndId for example "Patient/123abc"
     */
    private void addReadInteraction(String typeAndId) {
        String[] parts = typeAndId.split("/");
        if (parts.length == 2) {
            this.interactions.add(new ClientReadInteraction(parts[0], parts[1]));
        } else {
            throw new IllegalArgumentException("Invalid READ value: '" + typeAndId + "'");
        }
    }

    /**
     * Add a FHIR VREAD interaction
     * @param typeAndId for example "Patient/123abc/1"
     */
    private void addVReadInteraction(String typeIdVersion) {
        String[] parts = typeIdVersion.split("/");
        if (parts.length == 3) {
            this.interactions.add(new ClientVReadInteraction(parts[0], parts[1], parts[2]));
        } else {
            throw new IllegalArgumentException("Invalid VREAD value: '" + typeIdVersion + "'");
        }
    }

    /**
     * Add a FHIR CREATE interaction
     */
    private void addCreateInteraction() {
        this.interactions.add(new ClientCreateInteraction());
    }

    /**
     * Add a print interaction so we can render the current resource value to standard out
     */
    private void addPrintInteraction() {
        this.interactions.add(new ClientPrintInteraction());
    }

    /**
     * Add a print OperationOutcome interaction so that we can see details of the last interaction
     */
    private void addPrintOperationOutcome() {
        this.interactions.add(new ClientPrintOperationOutcomeInteraction());
    }

    /**
     * Add a print location interaction so we can see the location returned by the last interaction
     */
    private void addPrintLocation() {
        this.interactions.add(new ClientPrintLocationInteraction());
    }

    /**
     * Add a FHIR UPDATE interaction
     * @param typeAndId for example "Patient/123abc"
     */
    private void addUpdateInteraction(String typeAndId) {
        String[] parts = typeAndId.split("/");
        if (parts.length == 2) {
            this.interactions.add(new ClientUpdateInteraction(parts[0], parts[1]));
        } else {
            throw new IllegalArgumentException("Invalid READ value: '" + typeAndId + "'");
        }
    }
    /**
     * Invoke each of the interactions that have been requested
     * @throws Exception
     */
    private void run() throws Exception {
        for (ClientInteraction interaction: interactions) {
            interaction.invoke(this, this.client);
        }
    }

    /**
     * Parse the given string value as a FHIR resource
     * @param resourceStr
     * @throws Exception
     */
    private void parseResource(String resourceStr) throws Exception {
        final ByteArrayInputStream in = new ByteArrayInputStream(resourceStr.getBytes(StandardCharsets.UTF_8));
        setResource(FHIRParser.parser(Format.JSON).parse(in));
    }

    /**
     * Read a FHIR resource from the given file
     * @param file the filename containing the resource in JSON format
     * @throws Exception
     */
    private void parseResourceFromFile(String file) throws Exception {
        try (InputStream in = new FileInputStream(file)) {
            setResource(FHIRParser.parser(Format.JSON).parse(in));
        }
    }

    /**
     * Read a FHIR resource from stdin
     * @throws Exception
     */
    private void parseResourceFromStdin() throws Exception {
        setResource(FHIRParser.parser(Format.JSON).parse(System.in));
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        int systemExitStatus = 0;
        Main m = new Main();
        try {
            m.parseArgs(args);
            m.configure();
            m.run();
        } catch (IllegalArgumentException x) {
            logger.log(Level.SEVERE, "bad argument", x);
            systemExitStatus = 1;
        } catch (Exception x) {
            logger.log(Level.SEVERE, "request failed", x);
            systemExitStatus = 2;
        }
        System.exit(systemExitStatus);
    }

    @Override
    public Resource getResource() {
        return this.resource;
    }

    @Override
    public void setResource(Resource r) {
        this.resource = r;
    }

    @Override
    public void setStatus(int status) {
        this.lastInteractionStatus = status;
    }

    @Override
    public int getStatus() {
        return this.lastInteractionStatus;
    }

    @Override
    public boolean isPretty() {
        return this.pretty;
    }

    @Override
    public OperationOutcome getOperationOutcome() {
        return this.lastOperationOutcome;
    }

    @Override
    public void setOperationOutcome(OperationOutcome opo) {
        this.lastOperationOutcome = opo;
    }

    @Override
    public String getLocation() {
        return this.lastLocation;
    }

    @Override
    public void setLocation(String location) {
        this.lastLocation = location;
    }
}
