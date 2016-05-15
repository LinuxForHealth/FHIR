/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.test;

import com.ibm.watsonhealth.fhir.model.Patient;
import com.ibm.watsonhealth.fhir.persistence.interceptor.FHIRPersistenceEvent;
import com.ibm.watsonhealth.fhir.persistence.interceptor.FHIRPersistenceInterceptor;
import com.ibm.watsonhealth.fhir.persistence.interceptor.FHIRPersistenceInterceptorException;

/**
 * This class servces as a test implementation of the FHIRPersistenceInterceptor interface.
 */
public class MyInterceptor implements FHIRPersistenceInterceptor {
    private static int beforeCreateCount = 0;
    private static int afterCreateCount = 0;
    private static int beforeUpdateCount = 0;
    private static int afterUpdateCount = 0;

    public MyInterceptor() {
    }

    @Override
    public void beforeCreate(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        beforeCreateCount++;
        if (event.getFhirResource() instanceof Patient) {
            Patient patient = (Patient) event.getFhirResource();
            if (patient.getName().get(0).getFamily().get(0).getValue().equals("Exception")) {
                throw new FHIRPersistenceInterceptorException("Detected invalid patient");
            }
        }
    }

    @Override
    public void afterCreate(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        afterCreateCount++;
        if (event.getFhirResource() instanceof Patient) {
            Patient patient = (Patient) event.getFhirResource();
            if (patient.getName().get(0).getFamily().get(0).getValue().equals("Exception")) {
                throw new FHIRPersistenceInterceptorException("Detected invalid patient");
            }
        }
    }

    @Override
    public void beforeUpdate(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        beforeUpdateCount++;
        if (event.getFhirResource() instanceof Patient) {
            Patient patient = (Patient) event.getFhirResource();
            if (patient.getName().get(0).getFamily().get(0).getValue().equals("Exception")) {
                throw new FHIRPersistenceInterceptorException("Detected invalid patient");
            }
        }
    }

    @Override
    public void afterUpdate(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        afterUpdateCount++;
        if (event.getFhirResource() instanceof Patient) {
            Patient patient = (Patient) event.getFhirResource();
            if (patient.getName().get(0).getFamily().get(0).getValue().equals("Exception")) {
                throw new FHIRPersistenceInterceptorException("Detected invalid patient");
            }
        }
    }

    public static int getBeforeCreateCount() {
        return beforeCreateCount;
    }

    public static int getAfterCreateCount() {
        return afterCreateCount;
    }

    public static int getBeforeUpdateCount() {
        return beforeUpdateCount;
    }

    public static int getAfterUpdateCount() {
        return afterUpdateCount;
    }
}
