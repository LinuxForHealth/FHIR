/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.jbatch.bulkdata.control;

import java.util.logging.Logger;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;


/**
 *
 */
@WebListener("IBM FHIR Server Bulk Data Context Listener")
public class JobControlContextListener implements ServletContextListener {
    private static final Logger log = Logger.getLogger(JobControlContextListener.class.getName());
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("Starting up the Bulk Data Web App");
        try {

            JobOperator operator = BatchRuntime.getJobOperator();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // No Operation
    }
}
