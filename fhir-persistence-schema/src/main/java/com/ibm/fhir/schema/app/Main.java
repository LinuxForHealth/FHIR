/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.dryrun.DryRunContainer;
import com.ibm.fhir.schema.app.feature.ArgumentFeature;
import com.ibm.fhir.schema.app.feature.ExitFeature;
import com.ibm.fhir.schema.app.feature.ProcessFeature;
import com.ibm.fhir.schema.app.processor.SchemaUtil;
import com.ibm.fhir.schema.app.processor.action.bean.ActionBean;
import com.ibm.fhir.schema.app.processor.action.exceptions.SchemaActionException;

/**
 * Utility app to connect to a DB2 database and create/update the FHIR schema.
 * The DDL processing is idempotent, with only the necessary changes applied.
 * <br>
 * This utility also includes an option to exercise the tenant partitioning
 * code.
 */
public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    /**
     * The main entry point is designed to be on the commandline. 
     * <pre>Main.main args[0]...args[n]</pre>
     * @param args are string name-value pairs or values in general prefixed with --
     */
    public static void main(String[] args) {
        SchemaUtil.logClasspath();

        ExitFeature exitFeature = new ExitFeature();
        ArgumentFeature argumentFeature = new ArgumentFeature();
        ProcessFeature processFeature = new ProcessFeature();
        try {
            SchemaUtil.configureLogger();
            ActionBean actionBean = argumentFeature.parseArgs(args);
            processFeature.process(actionBean);

            // Print before closing out.
            DryRunContainer.getSingleInstance().print(";", System.out);
            exitFeature.setExitStatus(actionBean.getExitStatus());
        } catch (IllegalArgumentException x) {
            logger.log(Level.SEVERE, "bad argument", x);
            argumentFeature.printUsage(System.err);
            exitFeature.setExitStatus(ExitFeature.EXIT_BAD_ARGS);
        } catch (SchemaActionException sae) {
            // Signals an issue with a schema action. 
            logger.log(Level.SEVERE, "schema tool failed");
            exitFeature.setExitStatus(ExitFeature.EXIT_RUNTIME_ERROR);
        } catch (Exception x) {
            logger.log(Level.SEVERE, "schema tool failed", x);
            exitFeature.setExitStatus(ExitFeature.EXIT_RUNTIME_ERROR);
        }

        // Write out a final status message to make it easy to see validation success/failure
        exitFeature.logStatusMessage();
        
        // TODO: Output on Success. 

        // almost certainly will get flagged during code-scan, but this is intentional,
        // as we genuinely want to exit with the correct status here. The code-scan tool
        // really ought to be able to see that this is a main function in a J2SE environment
        System.exit(exitFeature.getExitStatus());
    }
}