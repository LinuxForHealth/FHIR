/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.examples.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.ibm.fhir.examples.ExamplesUtil;
import com.ibm.fhir.examples.Index;

/**
 * Verifies the indices in the Examples have no invalid entries.
 */
public class VerifyMain {

    public static void main(String[] args) throws IOException {
        for(Index index : Index.values()) {
            List<String> lines = new ArrayList<>();
            try(Reader reader = ExamplesUtil.indexReader(index);
                    BufferedReader br = new BufferedReader(reader)){
                String r = br.readLine();
                int error = 0;
                while(r != null) {
                    String[] parts = r.split(" ");
                    String fileName = parts[parts.length -1];
                    File f = new File("src/main/resources/" + fileName);
                    if (f.exists()) {
                        lines.add(r);
                    } else {
                        error++;
                    }
                    r = br.readLine();
                }

                if (error > 0) {
                    System.out.println("Index: " + index.name());
                    System.out.println("-------");
                    System.out.println(lines.stream().filter(m -> m != null).collect(Collectors.joining("\n")));
                    System.out.println("-------");
                    System.out.println("Error: " + error);
                    System.out.println("-------");
                }
            }
        }
    }
}