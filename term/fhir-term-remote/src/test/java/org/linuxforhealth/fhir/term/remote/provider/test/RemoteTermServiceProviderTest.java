/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.term.remote.provider.test;

import java.util.Set;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;

import org.linuxforhealth.fhir.cache.CachingProxy;
import org.linuxforhealth.fhir.model.resource.CodeSystem;
import org.linuxforhealth.fhir.model.resource.CodeSystem.Concept;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.type.code.CodeSystemContentMode;
import org.linuxforhealth.fhir.model.type.code.PublicationStatus;
import org.linuxforhealth.fhir.term.remote.provider.RemoteTermServiceProvider;
import org.linuxforhealth.fhir.term.remote.provider.RemoteTermServiceProvider.Configuration;
import org.linuxforhealth.fhir.term.remote.provider.RemoteTermServiceProvider.Configuration.BasicAuth;
import org.linuxforhealth.fhir.term.remote.provider.RemoteTermServiceProvider.Configuration.Supports;
import org.linuxforhealth.fhir.term.remote.provider.RemoteTermServiceProvider.Configuration.TrustStore;
import org.linuxforhealth.fhir.term.spi.FHIRTermServiceProvider;
import org.linuxforhealth.fhir.term.util.CodeSystemSupport;

public class RemoteTermServiceProviderTest {
    public static void main(String[] args) {
        Logger logger = Logger.getLogger(RemoteTermServiceProvider.class.getName());
        logger.setLevel(Level.FINEST);
        Handler handler = new ConsoleHandler();
        handler.setLevel(Level.FINEST);
        logger.addHandler(handler);

        Options options = null;
        RemoteTermServiceProvider snomedProvider = null;
        RemoteTermServiceProvider loincProvider = null;
        RemoteTermServiceProvider provider = null;
        try {
            options = new Options()
                .addRequiredOption("loincUsername", null, true, "LOINC username")
                .addRequiredOption("loincPassword", null, true, "LOINC password");

            CommandLineParser parser = new DefaultParser();

            CommandLine commandLine = parser.parse(options, args);

            Configuration snomedConfig = Configuration.builder()
                .base("https://snowstorm-fhir.snomedtools.org/fhir")
                .supports(Supports.builder()
                    .system("http://snomed.info/sct")
                    .build())
                .build();

            snomedProvider = new RemoteTermServiceProvider(snomedConfig);

            FHIRTermServiceProvider cachingSnomedProvider = CachingProxy.newInstance(FHIRTermServiceProvider.class, snomedProvider);

            CodeSystem snomed = CodeSystem.builder()
                .url(Uri.of("http://snomed.info/sct"))
                .status(PublicationStatus.ACTIVE)
                .content(CodeSystemContentMode.NOT_PRESENT)
                .build();

            Concept snomedConcept = snomedProvider.getConcept(snomed, Code.of("403190006"));

            System.out.println(snomedConcept);

            Set<Concept> concepts = cachingSnomedProvider.closure(snomed, Code.of("403190006"));
            concepts.forEach(System.out::println);

            concepts = cachingSnomedProvider.closure(snomed, Code.of("403190006"));
            concepts.forEach(System.out::println);

            System.out.println("hasConcept: " + snomedProvider.hasConcept(snomed, Code.of("403190006")));

            System.out.println("subsumes: " + snomedProvider.subsumes(snomed, Code.of("403190006"), Code.of("52425001")));

            Configuration loincConfig = Configuration.builder()
                .base("https://fhir.loinc.org")
                .basicAuth(BasicAuth.builder()
                    .username(commandLine.getOptionValue("loincUsername"))
                    .password(commandLine.getOptionValue("loincPassword"))
                    .build())
                .supports(Supports.builder()
                    .system("http://loinc.org")
                    .build())
                .build();

            loincProvider = new RemoteTermServiceProvider(loincConfig);

            CodeSystem loinc = CodeSystem.builder()
                .url(Uri.of("http://loinc.org"))
                .status(PublicationStatus.ACTIVE)
                .content(CodeSystemContentMode.NOT_PRESENT)
                .build();

            Concept loincConcept = loincProvider.getConcept(loinc, Code.of("789-8"));

            System.out.println(loincConcept);

            Configuration configuration = Configuration.builder()
                .base("https://localhost:9443/fhir-server/api/v4")
                .trustStore(TrustStore.builder()
                    .location("fhirClientTrustStore.p12")
                    .password("change-password")
                    .build())
                .basicAuth(BasicAuth.builder()
                    .username("fhiruser")
                    .password("change-password")
                    .build())
                .supports(Supports.builder()
                    .system("http://ibm.com/fhir/CodeSystem/test")
                    .version("1.0.0")
                    .build())
                .build();

            provider = new RemoteTermServiceProvider(configuration);

            CodeSystem codeSystem = CodeSystemSupport.getCodeSystem("http://ibm.com/fhir/CodeSystem/test");

            Concept concept = provider.getConcept(codeSystem, Code.of("a"));

            System.out.println(concept);
        } catch (MissingOptionException e) {
            System.out.println("MissingOptionException: " + e.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("RemoteTermServiceProviderTest", options);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (snomedProvider != null) {
                snomedProvider.close();
            }
            if (loincProvider != null) {
                loincProvider.close();
            }
            if (provider != null) {
                provider.close();
            }
        }
    }
}
