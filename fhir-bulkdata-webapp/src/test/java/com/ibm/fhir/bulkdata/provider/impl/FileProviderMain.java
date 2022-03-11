/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.provider.impl;

import com.ibm.fhir.bulkdata.jbatch.load.data.ImportTransientUserData;
import com.ibm.fhir.exception.FHIRException;

/**
 * Run the logic directly against a File instance
 * example: r4_AllergyIntolerance.ndjson
 * example: patient-large.ndjson
 */
public class FileProviderMain {

    /**
     * Runs a test with FileProvider
     * @param args
     * @throws FHIRException
     */
    public static void main(String[] args) throws FHIRException {
        String file = "/patient-large.ndjson";

        FileProvider provider = new FileProvider("default");
        long size = provider.getSizeWithAbsolute(args[0] + file, file);
        System.out.println("Size: " + size);

        ImportTransientUserData transientUserData = ImportTransientUserData.Builder.builder()
                .build();
        transientUserData.setImportFileSize(size);
        provider.registerTransient(transientUserData);
        int total = 0;
        while (transientUserData.getCurrentBytes() < (size - 1)) {
            System.out.println("- PREREAD - bytes " + transientUserData.getCurrentBytes());
            provider.readResourcesWithAbsolute(size, args[0] + file);
            System.out.println(provider.getResources().size());
            total += provider.getResources().size();
            System.out.println("- POSTREAD - bytes " + transientUserData.getCurrentBytes());
        }
        System.out.println("Total: " + total);
    }
}