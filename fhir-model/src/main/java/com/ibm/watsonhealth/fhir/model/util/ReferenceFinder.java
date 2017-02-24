/**
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.Binder;
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ibm.watsonhealth.fhir.model.Reference;
import com.ibm.watsonhealth.fhir.model.Resource;

/**
 * This class contains utility functions for finding "reference" fields within a FHIR resource.
 */
public class ReferenceFinder {
    
    /**
     * The XPath-related objects are not thread-safe so we'll use a threadlocal to store
     * thread-specific instances of XPathExpression.
     * By overriding the initialValue() method, we can perform the initialize required the first
     * time that a particular thread calls the get() method.
     */
    private static ThreadLocal<XPathExpression> referenceExpressions = new ThreadLocal<XPathExpression>() {
        @Override
        public XPathExpression initialValue() {
            XPathFactory xf = XPathFactory.newInstance();
            XPath xp = xf.newXPath();
            xp.setNamespaceContext(new NamespaceContext() {
                @Override
                public String getNamespaceURI(String prefix) {
                    if ("f".equals(prefix)) {
                        return "http://hl7.org/fhir";
                    }
                    return null;
                }

                @Override
                public String getPrefix(String namespaceURI) {
                    if ("http://hl7.org/fhir".equals(namespaceURI)) {
                        return "f";
                    }
                    return null;
                }

                @Override
                public Iterator<String> getPrefixes(String namespaceURI) {
                    return null;
                }
            });
            
            XPathExpression result = null;
            try {
                result = xp.compile("//*[./f:reference]");
            } catch (Throwable t) {
                throw new RuntimeException("Unexpected error while compiling xpath expression.", t);
            }
            return result;
        }
    };
    
    /**
     * Returns a threadlocal copy of the XPathExpression needed to find the reference fields.
     */
    private static synchronized XPathExpression getReferenceExpression() throws Exception {
        return referenceExpressions.get();
    }
    
    /**
     * Returns a list of Reference fields that are found in the 
     * specified resource instance.  For example, an Observation has a "subject" field of type
     * Reference, so if this method is called on an Observation instance that has the "subject" field
     * set, then we'll return the Reference instance that represents this "subject" field 
     * @param resource the FHIR resource for which we want to find all Reference fields
     * @return list of Reference instances found in the specified FHIR resource
     * @throws Exception
     */
    public static List<Reference> getReferences(Resource resource) throws Exception {
        List<Reference> references = new ArrayList<Reference>();
        
        // Get the xpath expression we'll use to find the Reference instances.
        XPathExpression expr = getReferenceExpression();
        
        // Create the DOM representation of the resource instance.
        Binder<Node> binder = FHIRUtil.createBinder(resource);
        Document document = binder.getXMLNode(resource).getOwnerDocument();

        // Find the DOM Nodes that represent the Reference fields.
        NodeList nodeList = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
        
        // Walk through the Node list and add each "value" (i.e. Reference instance) to
        // our result list.
        for (int i = 0; i < nodeList.getLength(); i++) {
            Object value = binder.getJAXBNode(nodeList.item(i));
            if (value instanceof Reference) {
                references.add((Reference) value);
            }
        }
        
        return references;
    }
}
