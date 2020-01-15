/*
 * (C) Copyright IBM Corp. 2016, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.cli;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import com.ibm.fhir.cli.invoker.BatchInvoker;
import com.ibm.fhir.cli.invoker.ConditionalCreateInvoker;
import com.ibm.fhir.cli.invoker.ConditionalDeleteInvoker;
import com.ibm.fhir.cli.invoker.ConditionalUpdateInvoker;
import com.ibm.fhir.cli.invoker.CreateInvoker;
import com.ibm.fhir.cli.invoker.DeleteInvoker;
import com.ibm.fhir.cli.invoker.HistoryInvoker;
import com.ibm.fhir.cli.invoker.InvocationContext;
import com.ibm.fhir.cli.invoker.MetadataInvoker;
import com.ibm.fhir.cli.invoker.OperationInvoker;
import com.ibm.fhir.cli.invoker.ReadInvoker;
import com.ibm.fhir.cli.invoker.SearchAllInvoker;
import com.ibm.fhir.cli.invoker.SearchInvoker;
import com.ibm.fhir.cli.invoker.SearchPostInvoker;
import com.ibm.fhir.cli.invoker.TransactionInvoker;
import com.ibm.fhir.cli.invoker.UpdateInvoker;
import com.ibm.fhir.cli.invoker.ValidateInvoker;
import com.ibm.fhir.cli.invoker.VreadInvoker;
import com.ibm.fhir.client.FHIRResponse;
import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.resource.Resource;

/**
 * This class provides a command-line interface (CLI) to the FHIR Client API, allowing
 * the user to invoke commands on the command-line that invoke the FHIR Client API to
 * carry out various FHIR-related operations.
 * 
 * @author padams
 */
public class FHIRCLI {
    
    public static void main(String[] args) {
        FHIRCLI fhircli = null;
        try {
            println(copyright);
            fhircli = new FHIRCLI(args);
            fhircli.processCommand();
            System.exit(0);
        } catch (Throwable t) {
            println("\nUnexpected exception: " + t);
            println(getStackTrace(t));
            System.exit(1);
        }
    }
    
    private static final String copyright = "\nFHIR Client Command Line Interface (fhir-cli)    (c) Copyright IBM Corporation, 2018, 2020.\n";
    private static final String header = "\nProvides access to the FHIR Client API via the command line.\n\nOptions:\n";
    private static final String syntax = "fhir-cli [options]";
    private static final String DEFAULT_MIMETYPE = FHIRMediaType.APPLICATION_FHIR_JSON;
    
    private static PrintStream console = System.err;
    
    private String[] args;
    
    private Map<Operations, OperationInvoker> invokers = null;

    public FHIRCLI(String[] args) {
        this.args = args;
        initInvokersMap();
    }
    
    public static void setConsoleStream(PrintStream stream) {
        console = stream;
    }
    
    private static void println(String s) {
        console.println(s);
    }
    
    private static void print(String s) {
        console.print(s);
    }
    
    public static String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    /**
     * Instantiates an OperationInvoker for each supported operation and adds it to a Map.
     */
    private void initInvokersMap() {
        invokers = new HashMap<Operations, OperationInvoker>();
        invokers.put(Operations.BATCH, new BatchInvoker());
        invokers.put(Operations.CREATE, new CreateInvoker());
        invokers.put(Operations.COND_CREATE, new ConditionalCreateInvoker());
        invokers.put(Operations.DELETE, new DeleteInvoker());
        invokers.put(Operations.COND_DELETE, new ConditionalDeleteInvoker());
        invokers.put(Operations.HISTORY, new HistoryInvoker());
        invokers.put(Operations.METADATA, new MetadataInvoker());
        invokers.put(Operations.READ, new ReadInvoker());
        invokers.put(Operations.SEARCH, new SearchInvoker());
        invokers.put(Operations.SEARCHALL, new SearchAllInvoker());
        invokers.put(Operations.SEARCH_POST, new SearchPostInvoker());
        invokers.put(Operations.TRANSACTION, new TransactionInvoker());
        invokers.put(Operations.UPDATE, new UpdateInvoker());
        invokers.put(Operations.COND_UPDATE, new ConditionalUpdateInvoker());
        invokers.put(Operations.VALIDATE, new ValidateInvoker());
        invokers.put(Operations.VREAD, new VreadInvoker());
    }

    public void processCommand() throws Exception {
        Options options = createOptions();

        // Parse the command-line args into a CommandLine.
        CommandLineParser parser = new DefaultParser();
        CommandLine cmdline = null;
        try {
            cmdline = parser.parse(options, args);
        } catch (Throwable t) {
            throw new IllegalArgumentException("Error while parsing command line options.", t);
        }
        
        // If "help" was requested, then let's display that and we're done.
        if (cmdline.hasOption(OptionNames.HELP.getShortName())) {
            displayHelp(options);
            return;
        } 
        
        try {
            // Next, build an InvocationContext object.
            InvocationContext context = buildInvocationContext(cmdline);
            
            // Invoke the operation.
            invokeOperation(context);
            
            // Process the response.
            processResponse(context);

        } catch (Throwable t) {
            throw new IllegalStateException("Unexpected exception occurred while invoking the operation.", t);
        }
    }

    /**
     * Builds an InvocationContext that holds all the important pieces of context for the
     * requested API invocation.
     * @param cmdline a CommandLine object that holds all the command-line options specified by the user
     */
    private InvocationContext buildInvocationContext(CommandLine cmdline) {
        InvocationContext ic = new InvocationContext();
        
        // Set the operation in the IC.
        if (cmdline.hasOption(OptionNames.OPERATION.getShortName())) {
            String opString = cmdline.getOptionValue(OptionNames.OPERATION.getShortName());
            Operations operation = Operations.fromString(opString);
            if (operation == null) {
                throw new IllegalArgumentException("Unrecognized operation name: " + opString);
            }
            ic.setOperation(operation);
        } else {
            throw new IllegalArgumentException("The 'operation' option was missing.");
        }
        
        // Set the mime type.
        ic.setMimeType(getMimetype(cmdline));
        
        // Set the properties filename in the IC.
        if (cmdline.hasOption(OptionNames.PROPERTIES.getShortName())) {
            ic.setPropertiesFile(cmdline.getOptionValue(OptionNames.PROPERTIES.getShortName()));
        } else {
            throw new IllegalArgumentException("The 'properties' option was missing.");
        }
        
        // Set the resource filename in the IC.
        if (cmdline.hasOption(OptionNames.RESOURCE.getShortName())) {
            ic.setResourceFile(cmdline.getOptionValue(OptionNames.RESOURCE.getShortName()));
        }
        
        // Set the resource type in the IC.
        if (cmdline.hasOption(OptionNames.RESOURCETYPE.getShortName())) {
            ic.setResourceType(cmdline.getOptionValue(OptionNames.RESOURCETYPE.getShortName()));
        }
        
        // Set the object id in the IC.
        if (cmdline.hasOption(OptionNames.ID.getShortName())) {
            ic.setResourceId(cmdline.getOptionValue(OptionNames.ID.getShortName()));
        }
        
        // Set the version # in the IC.
        if (cmdline.hasOption(OptionNames.VERSIONID.getShortName())) {
            ic.setVersionId(cmdline.getOptionValue(OptionNames.VERSIONID.getShortName()));
        }
        
        // Set the output filename in the IC.
        if (cmdline.hasOption(OptionNames.OUTPUT.getShortName())) {
            ic.setOutputFile(cmdline.getOptionValue(OptionNames.OUTPUT.getShortName()));
        }
        
        // Set the verbose flag in the IC.
        if (cmdline.hasOption(OptionNames.VERBOSE.getShortName())) {
            ic.setVerbose(true);
        }
        
        collectQueryParameters(cmdline, ic);
        collectHeaders(cmdline, ic);
        
        return ic;
    }

    /**
     * Invokes the operation that is described by the InvocationContext.
     * @param ic the InvocationContext that holds all the relevant information
     * @throws Exception
     */
    private void invokeOperation(InvocationContext ic) throws Exception {
        OperationInvoker invoker = invokers.get(ic.getOperation());
        if (invoker == null) {
            throw new UnsupportedOperationException("Operation '" + ic.getOperation().getOpName() + "' is not yet implemented");
        }
        
        long startTime = 0;
        
        print("Invoking operation '" + ic.getOperation().getOpName() + "'...");
        startTime = System.currentTimeMillis();
        
        invoker.invoke(ic);
        
        long elapsedTime = System.currentTimeMillis() - startTime;
        print(" done! (" + elapsedTime + "ms)\n");
    }

    /**
     * Processes the results of the operation invocation by retrieving the
     * response information from the InvocationContext and displaying it
     * according to the specified options.
     * 
     * @param context the InvocationContext that holds the response information
     * @throws Exception 
     */
    private void processResponse(InvocationContext ic) throws Exception {
        FHIRResponse response = ic.getResponse();
        if (response != null) {
            Response jaxrsResponse = response.getResponse();
            
            println("Status code: " + response.getStatus());

            String locationURI = response.getLocation();
            if (locationURI != null) {
                println("Location URI: " + locationURI);
            }

            String etag = response.getETag();
            if (etag != null) {
                println("ETag: " + etag);
            }
            
            String lastModified = (response.getLastModified() != null ? response.getLastModified().toString() : null);
            if (lastModified != null) {
                println("Last modified: " + lastModified);
            }
            
            // If verbose is requested, then display the headers.
            if (ic.isVerbose()) {
                println("Response headers:");
                MultivaluedMap<String, String> headers = jaxrsResponse.getStringHeaders();
                for (Map.Entry<String, List<String>> headerEntry : headers.entrySet()) {
                    for (String value : headerEntry.getValue()) {
                        println("   " + headerEntry.getKey() + ": " + value);
                    }
                }
                println("");
            }
            
            if (!response.isEmpty()) {
                Resource responseObj = response.getResource(Resource.class);
                if (responseObj != null) {
                    Writer writer = null;
                    String outputFile = ic.getOutputFile();
                    if (outputFile != null && !outputFile.isEmpty()) {
                        FileOutputStream os = new FileOutputStream(outputFile);
                        writer = new OutputStreamWriter(os);
                        println("Response resource written to file: " + outputFile);
                    } else {
                        writer = new OutputStreamWriter(console);
                        println("Response resource:\n");
                    }

                    FHIRGenerator.generator( Format.JSON, false).generate(responseObj, writer);
                }
            }
        }
    }

    /**
     * Visits each "header" option and adds it to the InvocationContext.
     */
    private void collectHeaders(CommandLine cmdline, InvocationContext ic) {
        for (Option option : cmdline.getOptions()) {
            if (option.getOpt().equals(OptionNames.HEADER.getShortName())) {
                List<String> values = option.getValuesList();
                if (values != null && values.size() >= 2) {
                    ic.addHeader(values.get(0), values.get(1));
                }
            }
        }
    }

    /**
     * Visits each "queryParameter" option and adds it to the InvocationContext.
     */
    private void collectQueryParameters(CommandLine cmdline, InvocationContext ic) {
        for (Option option : cmdline.getOptions()) {
            if (option.getOpt().equals(OptionNames.QUERYPARAMETER.getShortName())) {
                List<String> values = option.getValuesList();
                if (values != null && values.size() >= 2) {
                    ic.addQueryParameter(values.get(0), values.get(1));
                }
            }
        }
    }

    /**
     * Determines the correct mimeType to use for the request, based on the command line options.
     */
    private String getMimetype(CommandLine cmdline) {
        String mimeType = DEFAULT_MIMETYPE;
//        if (cmdline.hasOption(OptionNames.XML.getShortName())) {
//            mimeType = "application/fhir+xml";
//        }
        return mimeType;
    }

    /**
     * Returns an Options object with the set of options supported by this tool.
     */
    private Options createOptions() {
        Options options = new Options();
        options
            .addOption(Option.builder(OptionNames.HELP.getShortName()).longOpt(OptionNames.HELP.getLongName()).desc(OptionNames.HELP.getDesc()).build())
            .addOption(Option.builder(OptionNames.VERBOSE.getShortName()).longOpt(OptionNames.VERBOSE.getLongName()).desc(OptionNames.VERBOSE.getDesc()).build())
            .addOption(Option.builder(OptionNames.OPERATION.getShortName()).longOpt(OptionNames.OPERATION.getLongName()).desc(OptionNames.OPERATION.getDesc()).hasArg().argName(OptionNames.OPERATION.getArgName()).build())
            .addOption(Option.builder(OptionNames.PROPERTIES.getShortName()).longOpt(OptionNames.PROPERTIES.getLongName()).desc(OptionNames.PROPERTIES.getDesc()).hasArg().argName(OptionNames.PROPERTIES.getArgName()).build())
            .addOption(Option.builder(OptionNames.RESOURCE.getShortName()).longOpt(OptionNames.RESOURCE.getLongName()).desc(OptionNames.RESOURCE.getDesc()).hasArg().argName(OptionNames.RESOURCE.getArgName()).build())
            .addOption(Option.builder(OptionNames.RESOURCETYPE.getShortName()).longOpt(OptionNames.RESOURCETYPE.getLongName()).desc(OptionNames.RESOURCETYPE.getDesc()).hasArg().argName(OptionNames.RESOURCETYPE.getArgName()).build())
            .addOption(Option.builder(OptionNames.ID.getShortName()).longOpt(OptionNames.ID.getLongName()).desc(OptionNames.ID.getDesc()).hasArg().argName(OptionNames.ID.getArgName()).build())
            .addOption(Option.builder(OptionNames.VERSIONID.getShortName()).longOpt(OptionNames.VERSIONID.getLongName()).desc(OptionNames.VERSIONID.getDesc()).hasArg().argName(OptionNames.VERSIONID.getArgName()).build())
            .addOption(Option.builder(OptionNames.OUTPUT.getShortName()).longOpt(OptionNames.OUTPUT.getLongName()).desc(OptionNames.OUTPUT.getDesc()).hasArg().argName(OptionNames.OUTPUT.getArgName()).build())
            .addOption(Option.builder(OptionNames.QUERYPARAMETER.getShortName()).longOpt(OptionNames.QUERYPARAMETER.getLongName()).desc(OptionNames.QUERYPARAMETER.getDesc()).numberOfArgs(2).argName(OptionNames.QUERYPARAMETER.getArgName()).valueSeparator().build())
            .addOption(Option.builder(OptionNames.HEADER.getShortName()).longOpt(OptionNames.HEADER.getLongName()).desc(OptionNames.HEADER.getDesc()).numberOfArgs(2).argName(OptionNames.HEADER.getArgName()).valueSeparator().build());
        
        return options;
    }

    /**
     * Displays the help text and usage syntax on the console.
     */
    private void displayHelp(Options options) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        HelpFormatter helpFormatter = new HelpFormatter();
        String footer = "\nOPERATION should be one of: " + Operations.validOperations() 
            + "\n\nEach supported operation requires the following command-line options:\n" + Operations.operationsAndRequiredOptions();
        helpFormatter.printHelp(pw, 100, syntax, header, options, 3, 3, footer);
        println(sw.toString());
    }
}
