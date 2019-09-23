/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.cli;

/**
 * This enum defines each of the command-line options, including the long name, short name, description and an optional
 * "argName" field for options which take an additional argument.
 * 
 * @author padams
 */
public enum OptionNames {
    
    OPERATION("operation", "op", "The operation to be invoked", "OPERATION"),
    RESOURCE("resource", "r", "Use FHIR resource contained in <FILE> for operation invocation (e.g. patient.json)", "FILE"),
    RESOURCETYPE("type", "t", "Use resource type <TYPE> for the operation invocation (e.g. \"Patient\")", "TYPE"),
    ID("resourceId", "id", "Use resource identifier <ID> for the operation invocation", "ID"),
    VERSIONID("versionId", "vid", "Use version # <VID> for the operation invocation", "VID"),
    PROPERTIES("properties", "p", "Use FHIR Client properties contained in <FILE> (e.g. fhir-cli.properties)", "FILE"),
    QUERYPARAMETER("queryParameter", "qp", "Include query parameter NAME=VALUE with the operation invocation (e.g. _count=100).", "NAME=VALUE"),
    HEADER("header", "H", "Include the specified request header NAME=VALUE with the operation invocation (e.g. If-Match=W/\"3\").", "NAME=VALUE"),
    OUTPUT("output", "o", "Write output resource to <FILE> (e.g. searchresults.json)", "FILE"),
    HELP("help", "h", "Display this help text"),
    VERBOSE("verbose", "v", "Display detailed output");
    // JSON("json", "j", "Use JSON format for requests and responses"),
    // XML("xml", "x", "Use XML format for requests and responses");
    // HEADER("header", "H", "Define a request header with the specified name and value", "HEADER=VALUE");
    
    private String longName;
    private String shortName;
    private String desc;
    private String argName;
    
    private OptionNames(String longName, String shortName, String desc) {
        this.longName = longName;
        this.shortName = shortName;
        this.desc = desc;
    }
    private OptionNames(String longName, String shortName, String desc, String argName) {
        this(longName, shortName, desc);
        this.argName = argName;
    }
    
    public String getLongName() {
        return this.longName;
    }
    
    public String getShortName() {
        return this.shortName;
    }
    
    public String getDesc() {
        return this.desc;
    }
    
    public String getArgName() {
        return this.argName;
    }

}
