/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.util.group;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import com.ibm.fhir.model.parser.exception.FHIRParserException;
import com.ibm.fhir.path.exception.FHIRPathException;

/**
 * Location
 */
public class LocationGeneratorMain {
    public static void main(String... args)
            throws FHIRParserException, FHIRPathException, IOException, URISyntaxException {
        List<String> lines =
                Files.lines(Paths.get(LocationGeneratorMain.class.getResource("/groups/location-data.csv").toURI()))
                        .collect(Collectors.toList());
        for (String line : lines) {
            System.out.println(line);
        }
    }
}