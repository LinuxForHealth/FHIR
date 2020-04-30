/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * manages the writing and reading of the tenant key enforcing the constraints:
 * <li>file exists
 * <li>folder exists
 * <li>one non-empty line in the file
 */
public class TenantKeyFileUtil {
    public TenantKeyFileUtil() {
        // NO Operation
    }

    /**
     * checks if the tenant key at the given location exists
     * 
     * @param tenantKeyFileName
     * @return
     */
    public boolean keyFileExists(String tenantKeyFileName) {
        return (tenantKeyFileName != null) && (new File(tenantKeyFileName)).exists();
    }

    /**
     * reads a tenant key from a file with one line.
     * 
     * @param tenantKeyFileName a relative or absolute path
     * @return the tenant key contained in the file
     */
    public String readTenantFile(String tenantKeyFileName) {
        // tenantKey is returned only if valid. 
        String tenantKey;
        if (tenantKeyFileName == null) {
            throw new IllegalArgumentException("Value for tenant-key-file-name is never set");
        }

        File input = new File(tenantKeyFileName);

        if (!input.exists()) {
            throw new IllegalArgumentException("tenant-key-file-name does not exist [" + tenantKeyFileName + "]");
        }

        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(input.toURI()), StandardCharsets.UTF_8);

            // Make sure lines are not null, indicating an issue with extracting the data.
            // Check if it's empty
            if (lines == null || lines.isEmpty()) {
                throw new IllegalArgumentException("tenant-key-file-name is empty");
            }

            // Only ONE line
            if (lines.size() != 1) {
                throw new IllegalArgumentException("tenant-key-file-name supports only one line in the file");
            }
            tenantKey = lines.get(0);

            // 32 char UUID mapped to 44 chars base64 encode
            if (tenantKey.length() > 44) {
                throw new IllegalArgumentException("included tenant key is greater than 44 characters");
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("problem reading tenant-key-file-name  [" + tenantKeyFileName + "]");
        }
        return tenantKey;
    }

    /**
     * writes the tenant's key to the given file.
     * 
     * @param tenantKeyFileName a valid file and location.
     * @param tenantKey         a generated key
     */
    public void writeTenantFile(String tenantKeyFileName, String tenantKey) {
        if (tenantKeyFileName == null) {
            throw new IllegalArgumentException("Value for tenant-key-file-name is never set");
        }

        File output = new File(tenantKeyFileName);
        if (output.exists()) {
            throw new IllegalArgumentException("tenant-key-file-name must not exist [" + tenantKeyFileName + "]");
        }

        if (tenantKey == null) {
            throw new IllegalArgumentException("Value for tenant-key is never set when writing tenant key");
        }

        Path path = Paths.get(output.toURI());
        boolean writeable = Files.isWritable(path.getParent());
        if (!writeable) {
            throw new IllegalArgumentException("not a writeable directory");
        }

        try {
            Files.write(path, tenantKey.getBytes());
        } catch (IOException e) {
            throw new IllegalArgumentException("IOException writing the bytes");
        }
    }
}