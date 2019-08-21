/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.util.test;

import java.io.StringReader;

import javax.xml.stream.XMLStreamReader;

import com.ibm.watsonhealth.fhir.model.util.XMLSupport;

public class XMLSupportTest {
    public static void main(String[] args) throws Exception {
        String div = "<div xmlns=\"http://www.w3.org/1999/xhtml\"><p><b>Generated Narrative</b></p></div>";
        XMLStreamReader reader = XMLSupport.createXMLStreamReader(new StringReader(div));
        reader.next();
        System.out.println(XMLSupport.parseDiv(reader));
        
        div = "<h:div xmlns:h=\"http://www.w3.org/1999/xhtml\"><h:p><h:b>Generated Narrative</h:b></h:p></h:div>";
        reader = XMLSupport.createXMLStreamReader(new StringReader(div));
        reader.next();
        System.out.println(XMLSupport.parseDiv(reader));
        
        div = "<div xmlns=\"http://www.w3.org/1999/xhtml\"><div><p>Anything</p></div></div>";
        reader = XMLSupport.createXMLStreamReader(new StringReader(div));
        reader.next();
        System.out.println(XMLSupport.parseDiv(reader));
    }
}
