/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.spec.test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.ibm.watsonhealth.fhir.model.visitor.PathAwareVisitorAdapter;

/**
 * Simple visitor to flatten the structure of a resource and compare
 * it with another instance
 * @author rarnold
 *
 */
public class ResourceComparatorVisitor extends PathAwareVisitorAdapter {
    public boolean compare = false;

    public Map<String, Object> originalValues = new HashMap<>();
    
    /**
     * Flip the mode of this visitor so it compares incoming paths with
     * values stashed in the map
     */
    public void setCompare() {
        this.compare = true;
    }

    @Override
    public void visit(java.lang.String elementName, byte[] value) {
        
        
        if (this.compare) {
            String key = getPath();
            Object orig = this.originalValues.get(key);
            if (orig == null) {
                System.out.println(key + " not found in original");
            }
            else if (!(orig instanceof byte[])) {
                System.out.println(key + " is a " + orig.getClass().getName() + " not byte[]");
            }
            else if (!Arrays.equals(value, (byte[])orig)) {
                //    ^^^^^^ ^^^^^^ not Objects.equals!
                System.out.println(key + ": " 
                        + Base64.getEncoder().encodeToString((byte[])orig) + " != " 
                        + Base64.getEncoder().encodeToString(value));
                
            }
        }
        else {
            // stash the value for later comparison
            this.originalValues.put(getPath(), value);
        }
    }
    
    @Override
    public void visit(java.lang.String elementName, BigDecimal value) {
        if (this.compare) {
            String key = getPath();
            Object orig = this.originalValues.get(key);
            if (orig == null) {
                System.out.println(key + " not found in original");
            }
            else if (!Objects.equals(value, orig)) {
                System.out.println(key + ": " + orig.toString() + " != " + value.toString());
            }
        }
        else {
            // stash the value for later comparison
            this.originalValues.put(getPath(), value);
        }
    }

    @Override
    public void visit(java.lang.String elementName, java.lang.Boolean value) {
        if (this.compare) {
            String key = getPath();
            Object orig = this.originalValues.get(key);
            if (orig == null) {
                System.out.println(key + " not found in original");
            }
            else if (!Objects.equals(value, orig)) {
                System.out.println(key + ": " + orig.toString() + " != " + value.toString());
            }
        }
        else {
            // stash the value for later comparison
            this.originalValues.put(getPath(), value);
        }
    }
    
    @Override
    public void visit(java.lang.String elementName, java.lang.Integer value) {
        if (this.compare) {
            String key = getPath();
            Object orig = this.originalValues.get(key);
            if (orig == null) {
                System.out.println(key + " not found in original");
            }
            else if (!Objects.equals(value, orig)) {
                System.out.println(key + ": " + orig.toString() + " != " + value.toString());
            }
        }
        else {
            // stash the value for later comparison
            this.originalValues.put(getPath(), value);
        }
    }

    @Override
    public void visit(java.lang.String elementName, LocalDate value) {
        if (this.compare) {
            String key = getPath();
            Object orig = this.originalValues.get(key);
            if (orig == null) {
                System.out.println(key + " not found in original");
            }
            else if (!Objects.equals(value, orig)) {
                System.out.println(key + ": " + orig.toString() + " != " + value.toString());
            }
        }
        else {
            // stash the value for later comparison
            this.originalValues.put(getPath(), value);
        }
    }
    
    @Override
    public void visit(java.lang.String elementName, LocalTime value) {
        if (this.compare) {
            String key = getPath();
            Object orig = this.originalValues.get(key);
            if (orig == null) {
                System.out.println(key + " not found in original");
            }
            else if (!Objects.equals(value, orig)) {
                System.out.println(key + ": " + orig.toString() + " != " + value.toString());
            }
        }
        else {
            // stash the value for later comparison
            this.originalValues.put(getPath(), value);
        }
    }
    
    @Override
    public void visit(java.lang.String elementName, java.lang.String value) {
        if (this.compare) {
            String key = getPath();
            Object orig = this.originalValues.get(key);
            if (orig == null) {
                System.out.println(key + " not found in original");
            }
            else if (!Objects.equals(value, orig)) {
                System.out.println(key + ": " + orig.toString() + " != " + value.toString());
            }
        }
        else {
            // stash the value for later comparison
            this.originalValues.put(getPath(), value);
        }
    }
    
    @Override
    public void visit(java.lang.String elementName, Year value) {
        if (this.compare) {
            String key = getPath();
            Object orig = this.originalValues.get(key);
            if (orig == null) {
                System.out.println(key + " not found in original");
            }
            else if (!Objects.equals(value, orig)) {
                System.out.println(key + ": " + orig.toString() + " != " + value.toString());
            }
        }
        else {
            // stash the value for later comparison
            this.originalValues.put(getPath(), value);
        }
    }
    
    @Override
    public void visit(java.lang.String elementName, YearMonth value) {
        if (this.compare) {
            String key = getPath();
            Object orig = this.originalValues.get(key);
            if (orig == null) {
                System.out.println(key + " not found in original");
            }
            else if (!Objects.equals(value, orig)) {
                System.out.println(key + ": " + orig.toString() + " != " + value.toString());
            }
        }
        else {
            // stash the value for later comparison
            this.originalValues.put(getPath(), value);
        }
    }
    
    @Override
    public void visit(java.lang.String elementName, ZonedDateTime value) {
        if (this.compare) {
            String key = getPath();
            Object orig = this.originalValues.get(key);
            if (orig == null) {
                System.out.println(key + " not found in original");
            }
            else if (!Objects.equals(value, orig)) {
                System.out.println(key + ": " + orig.toString() + " != " + value.toString());
            }
        }
        else {
            // stash the value for later comparison
            this.originalValues.put(getPath(), value);
        }
    }
}
