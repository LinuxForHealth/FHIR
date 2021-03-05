/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.graph.test;

import java.io.InputStream;

import org.apache.commons.configuration.PropertiesConfiguration;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.CodeSystem;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.term.graph.provider.GraphTermServiceProvider;

public class CodeSystemTermGraphLoaderTest {
    public static void main(String[] args) throws Exception {
        InputStream in = CodeSystemTermGraphLoaderTest.class.getClassLoader().getResourceAsStream("JSON/CodeSystem-cs5.json");
        CodeSystem codeSystem = FHIRParser.parser(Format.JSON).parse(in);
        GraphTermServiceProvider provider = new GraphTermServiceProvider(new PropertiesConfiguration("conf/janusgraph-berkeleyje-lucene.properties"));
        System.out.println(provider.subsumes(codeSystem, Code.of("m"), Code.of("p")));
        System.out.println(provider.getConcept(codeSystem, Code.of("o")));
        provider.getGraph().close();
    }
}
