/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.bucket.interop;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 */
public interface IPatientScenario {
    
    /**
     * Execute the scenario for the given patient, incrementing the atomic counters
     * to report the statistics
     * @param patientId
     * @param fhirRequest
     * @param fhirRequestTime
     * @param resourceCount
     */
    void process(String patientId, AtomicInteger fhirRequest, AtomicLong fhirRequestTime, AtomicInteger resourceCount);
}
