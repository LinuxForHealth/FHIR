/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.bulkdata.jbatch.control;

import java.security.Principal;

import javax.annotation.PostConstruct;
import javax.annotation.security.RunAs;
import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.ejb.Startup;
import javax.inject.Inject;

/**
 *
 */

@Startup
@RunAs("JOBSTARTER")
public class JobControllerBean {

    @Inject
    private Principal user;

    @PostConstruct
    public void initialize() {
        System.out.println("Initializing");
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        long execID = jobOperator.start("testjob", null);
    }
}