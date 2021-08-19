/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.tool;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Date;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.parser.exception.FHIRParserException;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.path.FHIRPathBooleanValue;
import com.ibm.fhir.path.FHIRPathDateTimeValue;
import com.ibm.fhir.path.FHIRPathDateValue;
import com.ibm.fhir.path.FHIRPathDecimalValue;
import com.ibm.fhir.path.FHIRPathElementNode;
import com.ibm.fhir.path.FHIRPathIntegerValue;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.FHIRPathQuantityNode;
import com.ibm.fhir.path.FHIRPathQuantityValue;
import com.ibm.fhir.path.FHIRPathResourceNode;
import com.ibm.fhir.path.FHIRPathStringValue;
import com.ibm.fhir.path.FHIRPathTimeValue;
import com.ibm.fhir.path.FHIRPathTypeInfoNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;
import com.ibm.fhir.path.exception.FHIRPathException;
import com.ibm.fhir.path.visitor.FHIRPathNodeVisitor;

/**
 * Processes the resource from one of the entry points, into an in memory resource
 * a FHIR Path is executed over it, and the output is put out to the screen.
 *
 * The types of entry points are:
 * <ul>
 * <li>a file
 * <li>a string (raw command line arg); or
 * <li>stdin (so we can pipe to it)
 * </ul>
 *
 * @implNote the manifest.mf is specifically designed to pickup lib/
 *           if implementation guides need to be picked up, please use that local folder.
 *           in combination with the cli (uber) jar.
 */
public final class Main {
    // Properties Types
    private static final String PROP_PATH = "PATH";
    private static final String PROP_TYPE = "TYPE";
    private static final String PROP_RESOURCE = "RESOURCE";
    private static final String PROP_FILE = "FILE";
    private static final String PROP_FORMAT = "FORMAT";

    private Boolean pretty = Boolean.FALSE;
    private Boolean help = Boolean.FALSE;
    private Boolean error = Boolean.FALSE;

    private Resource r = null;

    private Properties props = new Properties();

    private Main() {
        // No Operation
    }

    /**
     * processes the command line parameters into objects.
     *
     * @param argsInc
     */
    protected void determineTypeAndSetProperties(String[] argsInc) {
        String type = "stdin";
        // We check before processing anything else.
        String[] args = new String[argsInc.length];
        int idx = 0;
        for (int i = 0; i < argsInc.length; i++) {
            if ("--throw-error".equals(argsInc[i])) {
                error = Boolean.TRUE;
            } else {
                args[idx]= argsInc[i];
                idx++;
            }
        }

        for (int i = 0; i < idx; i++) {
            if ("--help".equals(args[i]) || "-?".equals(args[i])) {
                help();
                help = Boolean.TRUE;
                return;
            } else if ("--pretty".equals(args[i])) {
                this.pretty = Boolean.TRUE;
            } else if ("--path".equals(args[i])) {
                checkIsThereMore(i, idx, "path");
                i++;
                String path = args[i];
                props.put(PROP_PATH, path);

                if (path == null || path.isEmpty()) {
                    throw new IllegalArgumentException("path must not be empty");
                }
            } else if ("--format".equals(args[i])) {
                checkIsThereMore(i, idx, "format");
                i++;
                String format = args[i].toLowerCase();
                props.put(PROP_FORMAT, format);

                if (format == null || format.isEmpty()
                        || (!"xml".equals(format)
                                && !"json".equals(format))) {
                    throw new IllegalArgumentException("format must be 'json' or 'xml'");
                }
            } else if ("--resource".equals(args[i])) {
                type = "string";
                checkIsThereMore(i, idx, "resource");
                i++;
                String resource = args[i];
                if (resource == null || resource.isEmpty()) {
                    throw new IllegalArgumentException("Resource must not be empty");
                }
                props.put(PROP_RESOURCE, resource);
            } else if ("--file".equals(args[i])) {
                type = "file";
                checkIsThereMore(i, idx, "resource");
                i++;
                String file = args[i];
                props.put(PROP_FILE, file);

                if (file == null || file.isEmpty()) {
                    throw new IllegalArgumentException("file property value must not be empty");
                }

                if (!Files.exists(Paths.get(file))) {
                    throw new IllegalArgumentException("File not found");
                }

                // File Exists, so let's serialize it.
                try (Stream<String> lines = Files.lines(Paths.get(file))) {
                    String body = lines.collect(Collectors.joining("\n"));
                    props.put(PROP_RESOURCE, body);
                } catch (IOException e) {
                    throw new IllegalArgumentException("Unable to read the file", e);
                }
            } else if (!"--throw-error".equals(args[i])) {
                throw new IllegalArgumentException("Unable to recognize the parameter name");
            }
        }


        if (props.size() == 0) {
            throw new IllegalArgumentException("Invalid parameters were set for the fhir path client");
        } else if (props.size() == 1 && !"stdin".equals(type)) {
            throw new IllegalArgumentException("Not enough parameters were set for the fhir path client");
        }

        if (!props.containsKey(PROP_PATH)) {
            throw new IllegalArgumentException("No path set");
        }

        // Check the type
        props.put(PROP_TYPE, type);

        // Only for the stdin do we need process into a String
        if ("stdin".equals(type)) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                    Stream<String> lines = reader.lines()) {
                String body = lines.collect(Collectors.joining("\n"));
                props.put(PROP_RESOURCE, body);
            } catch (Exception e) {
                throw new IllegalArgumentException("Unable to read the resource from stdin");
            }
        }
    }

    /**
     * verifies that the property exists before allowing the menu to move onward.
     *
     * @param i
     * @param len
     * @param property
     */
    private void checkIsThereMore(int i, int len, String property) {
        if (i + 1 >= len) {
            throw new IllegalArgumentException("Missing a property value for '" + property + "'");
        }
    }

    /**
     * print the error message.
     * @return
     */
    public Boolean shouldPrintError() {
        return error;
    }

    /**
     * verifies we have a fhir resource
     */
    public void verifyResource() {
        if (!help) {
            String fmt = props.getProperty(PROP_FORMAT);
            Format format = Format.JSON;
            if ("xml".equals(fmt)) {
                format = Format.XML;
            }

            String body = props.getProperty(PROP_RESOURCE);
            try (InputStream in = new ByteArrayInputStream(body.getBytes())) {
                r = FHIRParser.parser(format).parse(in);
            } catch (FHIRParserException e) {
                throw new IllegalArgumentException("Unable to parse the resource input", e);
            } catch (IOException e1) {
                throw new IllegalArgumentException("Unable to read the resource", e1);
            }
        }
    }

    /**
     * process the fhir path and output the nodes with each node separated by a newline
     *
     * <p>if {@code pretty} is set to true, each node is additionally prefixed by its index in the
     *     collected result of the FHIRPath evaluation.
     */
    protected void processFhirPath() {
        if (!help) {
            String fhirPath = props.getProperty(PROP_PATH);

            try {
                Collection<FHIRPathNode> nodes = FHIRPathEvaluator.evaluator().evaluate(r, fhirPath);
                int i = 0;
                if (pretty) {
                    header(fhirPath, nodes.size());
                }
                for (FHIRPathNode node : nodes) {
                    i++;
                    OutputPrinter printer = new OutputPrinter();
                    node.accept(printer);
                    String prefix = "";
                    if (pretty) {
                        prefix = "[" + i + "] ";
                    }
                    System.out.println(prefix + printer.getValue());
                }
                if (pretty) {
                    footer();
                }
            } catch (FHIRPathException e) {
                Throwable cause = e.getCause();
                if (cause != null && cause instanceof org.antlr.v4.runtime.misc.ParseCancellationException) {
                    throw new RuntimeException("Check fhirpath expression [" + fhirPath + "]\n" + e.getMessage());
                }
                throw new RuntimeException("Exception with FHIR Path Node" , e);
            }
        }
    }

    /**
     * output the header
     *
     * @param fhirPath
     * @param count
     */
    public void header(String fhirPath, int count) {
        System.out.println("FHIR Path Executed is: [" + fhirPath + "]");
        System.out.println("Start Time [" + new Date() + "]");
        System.out.println("-------------------------------------------");
        System.out.println("Count: [" + count + "]");
        System.out.println("-------------------------------------------");
        System.out.println("- Result (#) - Value                      -");
        System.out.println("-------------------------------------------");
    }

    /**
     * print the footer.
     */
    public void footer() {
        System.out.println("-------------------------------------------");
        System.out.println("End Time [" + new Date() + "]");
    }

    /**
     * print the help information.
     */
    public void help() {
        System.err.println("--path 'fhir-path'");
        System.err.println("--format ['json'|'xml'] . Default is 'json'");
        // --file only
        System.err.println("--file path-to-file . The file that is accessible and read.");
        // --resource only
        System.err.println("--resource 'resource-payload'. The FHIR resource as a well formed string.");
        System.err.println("--pretty adds columns and start time and end time of the fhir path request");
        System.err.println("--throw-error print the stacktrace");
        System.err.println("--help");
    }

    /**
     * executes a fhir path expression over a given resource.
     *
     * @param args
     */
    public static void main(String[] args) {
        Main main = new Main();
        try {
            main.determineTypeAndSetProperties(args);
            main.verifyResource();
            main.processFhirPath();
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            main.help();

            if (main.shouldPrintError()) {
                System.err.println();
                throw e;
            }
        }
    }

    /**
     * facilitate the printing of various fhir path elements.
     *
     * <p>FHIR Resource and Element nodes are serialized using the fhir-model's toString implementation
     * <p>FHIRPath value nodes are serialized using their raw values, with no quotation marks around strings
     */
    private static class OutputPrinter implements FHIRPathNodeVisitor {

        private String value;

        public String getValue() {
            return value;
        }

        /**
         * Standard serialization of the resource contained in the FHIRPathResourceNode
         * via the {@link com.ibm.fhir.model.resource.Resource} toString() implementation.
         */
        @Override
        public void visit(FHIRPathResourceNode node) {
            this.value = node.resource().toString();
        }

        /**
         * Custom JSON serialization of the element contained in the FHIRPathElementNode
         * via the {@link com.ibm.fhir.model.type.Element} toString() implementation.
         */
        @Override
        public void visit(FHIRPathElementNode node) {
            this.value = node.element().toString();
        }


        @Override
        public void visit(FHIRPathBooleanValue value) {
            // Convert to String
            this.value = "" + value._boolean();
        }

        @Override
        public void visit(FHIRPathDateValue value) {
            this.value = "" + value.date();
        }

        @Override
        public void visit(FHIRPathDateTimeValue value) {
            this.value = "" + value.dateTime();
        }

        @Override
        public void visit(FHIRPathDecimalValue value) {
            // Convert to String
            this.value = "" + value.decimal();
        }

        @Override
        public void visit(FHIRPathIntegerValue value) {
            // Convert to String
            this.value = "" + value.number().intValue();
        }

        @Override
        public void visit(FHIRPathQuantityNode node) {
            this.value = node.getQuantitySystem() + "|" + node.getQuantityCode() + "=" + node.getQuantityValue() + node.getQuantityUnit();
        }

        @Override
        public void visit(FHIRPathQuantityValue value) {
            this.value = value.value() + value.unit();
        }

        @Override
        public void visit(FHIRPathStringValue value) {
            this.value = value.string();
        }

        @Override
        public void visit(FHIRPathTimeValue value) {
            this.value = "" + value.time();
        }

        @Override
        public void visit(FHIRPathTypeInfoNode node) {
            this.value = node.typeInfo().getNamespace() + "." + node.typeInfo().getName();
        }
    }
}
