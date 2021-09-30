/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.graph.loader.main;

import static com.ibm.fhir.term.graph.loader.util.FHIRTermGraphLoaderUtil.toMap;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;

import com.ibm.fhir.term.graph.loader.FHIRTermGraphLoader;
import com.ibm.fhir.term.graph.loader.factory.FHIRTermGraphLoaderFactory;

/*
 * Main command line entry point for term graph loaders
 */
public class FHIRTermGraphLoaderMain {
    public static void main(String[] args) throws Exception {
        Options options = null;
        FHIRTermGraphLoader.Type type = null;
        FHIRTermGraphLoader loader = null;
        try {
            long start = System.currentTimeMillis();

            type = getType(args);
            options = type.options();

            // parse command line arguments using loader type specific options
            CommandLineParser parser = new DefaultParser();
            CommandLine commandLine = parser.parse(options, getArguments(args, 1));

            loader = FHIRTermGraphLoaderFactory.create(type, toMap(commandLine));
            loader.load();

            long end = System.currentTimeMillis();

            System.out.println("Loading time (milliseconds): " + (end - start));
        } catch (MissingOptionException e) {
            System.out.println("MissingOptionException: " + e.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(String.format("FHIRTermGraphLoaderMain %s <loader-specific-options>", type.toString()), options);
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        } finally {
            if (loader != null) {
                loader.close();
            }
        }
    }

    private static FHIRTermGraphLoader.Type getType(String[] args) {
        if (args.length > 0) {
            return FHIRTermGraphLoader.Type.valueOf(args[0]);
        }
        throw new IllegalArgumentException("loader type not found");
    }

    private static String[] getArguments(String[] args, int beginIndex) {
        List<String> arguments = new ArrayList<>();
        for (int i = beginIndex; i < args.length; i++) {
            arguments.add(args[i]);
        }
        return arguments.toArray(new String[arguments.size()]);
    }
}