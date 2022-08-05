/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.util.test.unicode;

import static org.testng.Assert.fail;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.exception.FHIRException;
import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.parser.FHIRParser;
import org.linuxforhealth.fhir.model.parser.exception.FHIRParserException;
import org.linuxforhealth.fhir.model.util.test.unicode.strategy.StrategyFactory;
import org.linuxforhealth.fhir.model.util.test.unicode.strategy.StrategyFactory.Location;
import org.linuxforhealth.fhir.model.util.test.unicode.strategy.StrategyFactory.Strategy;

import jakarta.json.stream.JsonParsingException;

/**
 * Tests injecting invalid characters into the JSON/XML bodies for the Parser and ValidationSupport
 */
public class UnicodeParsingTest {

    @Test
    public void testChannelJson() throws IOException, FHIRException {
        for (UnicodeChar ch : UnicodeChar.forbidden()) {
            ByteArrayInputStream bais = new ByteArrayInputStream("{\"resourceType\": \"ActivityDefinition\"}".getBytes());
            try (ReadableByteChannel channel = Channels.newChannel(bais);
                    InjectCharacterChannel icc =
                            new InjectCharacterChannel(channel, StrategyFactory.getStrategy(Strategy.UTF8, Location.START, ch));
                    BufferedInputStream nin = new BufferedInputStream(Channels.newInputStream(icc))) {
                FHIRParser.parser(Format.JSON).parse(nin);
            } catch (Exception e) {
                if (e.getCause() instanceof JsonParsingException) {
                    JsonParsingException jpe = (JsonParsingException) e.getCause();
                    return;
                }
            }
            fail("Unexpected");
        }
    }

    @Test
    public void testChannelXml() throws IOException, FHIRException {
        for (UnicodeChar ch : UnicodeChar.forbidden()) {
            ByteArrayInputStream bais = new ByteArrayInputStream("<ActivityDefinition xmlns=\"http://hl7.org/fhir\"></ActivityDefinition>".getBytes());
            try (ReadableByteChannel channel = Channels.newChannel(bais);
                    InjectCharacterChannel icc =
                            new InjectCharacterChannel(channel, StrategyFactory.getStrategy(Strategy.UTF8, Location.START, ch));
                    BufferedInputStream nin = new BufferedInputStream(Channels.newInputStream(icc))) {
                FHIRParser.parser(Format.XML).parse(nin);
            } catch (Exception e) {
                if (e.getCause() instanceof javax.xml.stream.XMLStreamException) {
                    continue;
                }
            }
            fail("Unexpected");
        }
    }

    @Test
    public void testParsingJson() {
        for (UnicodeChar ch : UnicodeChar.forbidden()) {
            String chEncoded = ch.getEscapedValue();

            String s = "{\n"
                    + "    \"resourceType\": \"Basic\",\n"
                    + "    \"code\": {\n"
                    + "        \"coding\": [\n"
                    + "            {\n"
                    + "                \"extension\": [\n"
                    + "                    {\n"
                    + "                        \"url\": \"http://hl7.org/fhir/StructureDefinition/data-absent-reason\",\n"
                    + "                        \"valueCode\": \"Test" + chEncoded + "\"\n"
                    + "                    }\n"
                    + "                ]\n"
                    + "            }\n"
                    + "        ]\n"
                    + "    }\n"
                    + "}";

            try (ByteArrayInputStream bais = new ByteArrayInputStream(s.getBytes());
                    BufferedReader brx = new BufferedReader(new InputStreamReader(bais, "UTF-8"));) {
                System.out.println(s);
                FHIRParser.parser(Format.JSON).parse(brx);
                fail();
            } catch (Exception e) {
                continue;
            }
        }
    }

    @Test
    public void testParsingXml() {
        for (UnicodeChar ch : UnicodeChar.forbidden()) {
            String chEncoded = ch.getEscapedValue();

            String s = "<Basic xmlns=\"http://hl7.org/fhir\">\n"
                    + "  <meta>\n"
                    + "    <tag>\n"
                    + "      <code value=\"ibm/minimal\"/>\n"
                    + "    </tag>\n"
                    + "  </meta>\n"
                    + "  <text>"
                    + "    <div xmlns=\"http://www.w3.org/1999/xhtml\">\n"
                    + "      TEST " + chEncoded
                    + "    </div> \n"
                    + "  </text> "
                    + "  <code>\n"
                    + "    <coding>\n"
                    + "      <extension url=\"http://hl7.org/fhir/StructureDefinition/data-absent-reason\">\n"
                    + "        <valueCode value=\"unknown " + chEncoded + "\"/>\n"
                    + "      </extension>\n"
                    + "    </coding>\n"
                    + "  </code>\n"
                    + "</Basic>";

            try (ByteArrayInputStream bais = new ByteArrayInputStream(s.getBytes());
                    BufferedReader brx = new BufferedReader(new InputStreamReader(bais, "UTF-8"));) {
                System.out.println(s);
                FHIRParser.parser(Format.XML).parse(brx);
                fail("Unexpected pass");
            } catch (Exception e) {
                continue;
            }
        }
    }

    @Test(expectedExceptions = { FHIRParserException.class })
    public void testSampleJson() throws IOException, FHIRException {
        try (InputStream in = UnicodeParsingTest.class.getResourceAsStream("JSON/condition-bad-unicode.json");) {
            FHIRParser.parser(Format.JSON).parse(in);
        }
    }
}