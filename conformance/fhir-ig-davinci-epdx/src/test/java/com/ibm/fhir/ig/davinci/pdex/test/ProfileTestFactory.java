/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.ig.davinci.pdex.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Factory;

import com.ibm.fhir.examples.ExamplesUtil;
import com.ibm.fhir.examples.Index;

/**
 * Dynamically builds the test list based on the Profiles XMl and JSON index
 */
public class ProfileTestFactory {
    @Factory
    public Object[] createInstances() {
        List<Object> result = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(ExamplesUtil.indexReader(Index.PROFILES_EPDX_JSON))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split("\\s+");
                if (tokens.length == 2) {
                    String expectation = tokens[0];
                    String example = tokens[1];
                    result.add(new ProfileTest(expectation, example, true));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toArray();
    }
}