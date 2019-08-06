/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.search.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.ibm.watsonhealth.fhir.model.path.FHIRPathDateTimeValue;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathElementNode;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathNode;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathPrimitiveValue;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathResourceNode;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathStringValue;
import com.ibm.watsonhealth.fhir.model.resource.SearchParameter;

/**
 * 
 * @author pbastide
 *
 */
public class ExtractorValidator {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    private Map<String, List<String>> expected = new HashMap<>();
    boolean strict = false;

    /**
     * 
     * @param name
     * @param values
     */
    public void addExpected(String name, List<String> values) {
        expected.put(name, values);
    }

    /**
     * 
     * @param output
     * @param ignoreExtra
     */
    public void validate(Map<SearchParameter, List<FHIRPathNode>> output) {

        printOutput(output);

        //
        for (Entry<SearchParameter, List<FHIRPathNode>> entry : output.entrySet()) {
            String name = entry.getKey().getName().getValue();

            if (strict) {
                assertTrue(expected.containsKey(name));
            }

            List<String> expectedValues = expected.remove(name);
            if (expectedValues != null) {
                for (FHIRPathNode node : entry.getValue()) {
                    String tmp = processOutput(node);
                    System.out.println("NODE: " + name + " " + tmp);
                    if (expectedValues.contains(tmp)) {
                        expectedValues = expectedValues.stream().filter(new Predicate<String>() {

                            @Override
                            public boolean test(String t) {
                                return tmp.compareTo(t) != 0;
                            }

                        }).collect(Collectors.toList());

                        if (expectedValues == null) {
                            expectedValues = Collections.emptyList();
                        }

                        // expectedValues.remove(tmp);
                    }
                }

                System.out.println("Expected Values -> " + expectedValues);
                assertEquals(expectedValues.size(), 0);
            }

        }

        // Dummy Assertion in case of empty expected
        if(expected.size() > 0) {
            System.out.println(expected);
        }
        assertEquals(expected.size(), 0);

    }

    private void printOutput(Map<SearchParameter, List<FHIRPathNode>> output) {
        int i = 0;
        for (Entry<SearchParameter, List<FHIRPathNode>> entry : output.entrySet()) {
            String name = entry.getKey().getName().getValue();

            System.out.print(i++ + "|" + name + "|=[");
            for (FHIRPathNode node : entry.getValue()) {
                System.out.print(processOutput(node) + ",");
            }
            System.out.println("]");

        }

    }

    /**
     * 
     * @param node
     * @return
     */
    public static String processOutput(FHIRPathNode node) {
        // System.out.println("Node is of type " + node);
        String val = "";
        if (node.isPrimitiveValue()) {
            FHIRPathPrimitiveValue nodeConverted = node.asPrimitiveValue();
            if (nodeConverted.isDateTimeValue()) {
                // FHIRPathDateTimeValue v = (FHIRPathDateTimeValue) node;

            } else if (nodeConverted.isStringValue()) {
                FHIRPathStringValue v = nodeConverted.asStringValue();
                val = "" + v.string();
            }

        } else if (node.is(FHIRPathElementNode.class)) {
            //
            FHIRPathElementNode tNode = node.asElementNode();

            FHIRPathPrimitiveValue v = tNode.getValue();
            if (v != null) {

                if (v.isStringValue()) {
                    FHIRPathStringValue sv = v.asStringValue();
                    val = "" + sv.string();
                } else if (v.isDateTimeValue()) {
                    FHIRPathDateTimeValue vv = v.asDateTimeValue();
                    TemporalAccessor acc = vv.dateTime();

                    val = "" + DATE_TIME_FORMATTER.format(acc);
                    System.out.println("Value DateTime: [" + val + "]");
                }
            } else {
                //System.out.println("OTHER -> " + tNode.getClass() + " " + tNode.children().size());
                Collection<FHIRPathNode> children = tNode.children();
                for (FHIRPathNode child : children) {
                    //System.out.println("--- " + child);
                    if (child.isElementNode()) {
                        FHIRPathElementNode nx = child.asElementNode();
                        val = "" + nx.getValue();
                        
                    } else if (child.isPrimitiveValue()) {
                        FHIRPathPrimitiveValue v1 = child.asPrimitiveValue();
                        if (v1.isStringValue()) {
                            FHIRPathStringValue sv = v1.asStringValue();
                            val = "" + sv.string() + ",";
                        }
                    }

                }

            }

        } else {
            FHIRPathResourceNode nodeConverted = node.asResourceNode();
            val = "" + nodeConverted.getValue();
        }
        return val;
    }

    /**
     * Builder
     * 
     * @author pbastide
     *
     */
    public static class Builder {

        private ExtractorValidator validator = new ExtractorValidator();

        public Builder() {

        }

        public Builder add(String name, List<String> values) {
            validator.addExpected(name, values);
            return this;
        }

        public Builder add(String name, String... values) {
            validator.addExpected(name, Arrays.asList(values));
            return this;
        }

        public Builder strict(boolean strict) {
            validator.setStrict(strict);
            return this;
        }

        public ExtractorValidator build() {
            return validator;
        }

        public static ExtractorValidator.Builder builder() {
            return new ExtractorValidator.Builder();
        }

    }

    public void setStrict(boolean strict) {
        this.strict = strict;
    }

}
