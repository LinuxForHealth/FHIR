/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.examples.tracker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.generator.exception.FHIRGeneratorException;
import com.ibm.fhir.model.resource.Resource;

/**
 * Tracks the Resource Creation and verifies the unique elements are written.
 */
public class ResourceTracker {
    private Map<String, List<String>> DIGESTMAP = new HashMap<>();

    public ResourceTracker() {
        // No Operation
    }

    /**
     * should write the resource?
     * @param generator
     * @param path
     * @param r
     * @return
     * @throws FHIRGeneratorException
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public boolean shouldWriteResourceExample(FHIRGenerator generator, Path path, Resource r) throws FHIRGeneratorException, NoSuchAlgorithmException, IOException {
        final MessageDigest sha256 = MessageDigest.getInstance("SHA-256");

        byte[] digest = null;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()){
            generator.generate(r, out);
            digest = sha256.digest(out.toByteArray());
        }

        String hash = translate(digest);
        boolean result = false;
        if (!DIGESTMAP.containsKey(hash)) {
            DIGESTMAP.put(hash, Arrays.asList(path.toFile().getAbsoluteFile().toString()));
            result = true;
        } else {
            List<String> files = DIGESTMAP.get(hash);
            List<String> fs = new ArrayList<>();
            fs.add(path.toFile().getAbsoluteFile().toString());
            fs.addAll(files);
            DIGESTMAP.put(hash, fs);
        }
        return result;
    }

    private static String translate(byte[] md) {
        // bytes to hex
        StringBuilder result = new StringBuilder();
        for (byte b : md) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    /**
     * prints out the tracking details
     * @param out
     * @param base
     */
    public void print(PrintStream out, String base) {
        out.println("Total number of generated unique resources " + DIGESTMAP.size());
        DIGESTMAP.entrySet().stream().filter(entry -> entry.getValue().size() > 1).forEach(e -> {
            Optional<String> first = e.getValue().stream().map(m -> m.replace(base, "")).sorted().findFirst();
            out.println("[" + first.get() + "] occured [" + e.getValue().size() + "] times ");
        });
    }
}