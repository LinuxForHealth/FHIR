/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.jbatch.control;

import java.security.AccessController;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.JobExecution;
import javax.security.auth.Subject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.model.util.FHIRUtil;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.registry.FHIRRegistry;

/**
 * This Servlet Context Listener provides a hook to the BatchRuntime.
 */
@WebListener("IBM FHIR Server Bulk Data Context Listener")
public class JobControlContextListener implements ServletContextListener {
    private static final Logger log = Logger.getLogger(JobControlContextListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("Starting up the Bulk Data Web App");

        FHIRConfiguration.setConfigHome(System.getenv("FHIR_CONFIG_HOME"));

        log.fine("Initializing ModelSupport...");
        ModelSupport.init();

        log.fine("Initializing FHIRUtil...");
        FHIRUtil.init();

        log.fine("Initializing FHIRRegistry...");
        FHIRRegistry.getInstance();
        FHIRRegistry.init();

        try {
            @SuppressWarnings("unused")
            Subject subject = Subject.getSubject(AccessController.getContext());

            // @implNote The following will fail, I leave it here as a pattern for reporting on startup.
            // The case where we hit an exception, the stacktrace/logs are swallowed and never presented
            // Note, this also adds time to the startup process.
            if (log.isLoggable(Level.FINE)) {
                report("bulkexportchunkjob");
                report("bulkexportfastjob");
                report("bulkpatientexportchunkjob");
                report("bulkgroupexportchunkjob");
                report("bulkimportchunkjob");
            }
        } catch (Exception e) {
            // Intentionally we are not logging anything here.
        }
    }

    private void report(String jobName) {
        JobOperator operator = BatchRuntime.getJobOperator();
        // Get the Running Executions
        List<Long> runnings = operator.getRunningExecutions("jobName");
        for (Long running : runnings) {
            JobExecution jobExecution = operator.getJobExecution(running);
            BatchStatus status = jobExecution.getBatchStatus();
            log.fine(running + "/" + status + "[" + jobExecution.getJobParameters() + "]");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("Bringing down the Bulk Data Web App");
    }
}
