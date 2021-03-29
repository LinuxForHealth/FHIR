/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.examples.tools;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Verifies the content in the examples directories are unique.
 */
public class Main {

    private static Map<String, List<String>> DIGESTMAP = new HashMap<>();

    private Main() {
        // No Operation
    }

    public void process(String folder) throws IOException, NoSuchAlgorithmException {
        process(Paths.get(folder));
    }

    public static String translate(byte[] md) {
        // bytes to hex
        StringBuilder result = new StringBuilder();
        for (byte b : md) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    public void process(Path folder) throws IOException, NoSuchAlgorithmException {
        final MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        Stream<Path> paths = Files.list(folder);
        paths.forEach(p -> {
            File f = p.toFile();
            if (f.isDirectory()) {
                try {
                    process(f.getAbsoluteFile().getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                String path = f.getAbsoluteFile().getAbsolutePath();
                byte[] arr;
                try {
                    arr = Files.readAllBytes(p);
                    byte[] digest = sha256.digest(arr);

                    String hash = translate(digest);
                    if (!DIGESTMAP.containsKey(hash)) {
                        DIGESTMAP.put(hash, Arrays.asList(path));
                    } else {
                        List<String> files = DIGESTMAP.get(hash);
                        List<String> fs = new ArrayList<>();
                        fs.add(path);
                        fs.addAll(files);
                        DIGESTMAP.put(hash, fs);
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        });
        paths.close();
    }

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws Exception {
        Main main = new Main();
        String dir = System.getProperty("user.dir");
        main.process(dir + "/../fhir-examples/src/main/resources/json");
        main.process(dir + "/../fhir-examples/src/main/resources/xml");
        System.out.println(DIGESTMAP.size());
        DIGESTMAP.entrySet().stream().filter(entry -> entry.getValue().size() > 1).forEach(e -> {
            System.out.println(e);
        });
    }

}
