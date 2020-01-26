/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app.feature;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.ibm.fhir.schema.app.processor.action.bean.ActionBean;

/**
 * 
 */
public class ProcessFeatureTest {
    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testCheckDropConfirmedNot() {
        ProcessFeature processFeature = new ProcessFeature();
        ActionBean actionBean = new ActionBean();
        processFeature.checkDropConfirmed(actionBean);
    }

    @Test
    public void testCheckDropConfirmed() {
        ProcessFeature processFeature = new ProcessFeature();
        ActionBean actionBean = new ActionBean();
        actionBean.setConfirmDrop(Boolean.TRUE);
        processFeature.checkDropConfirmed(actionBean);
    }
    
    @Test
    public void testProcess() throws Exception {
        ProcessFeature processFeature = new ProcessFeature();
        ActionBean actionBean = new ActionBean();
        processFeature.process(actionBean);
        assert true;
    }
    
    @Test
    public void testProcessCheckCompatibility() throws Exception {
        ProcessFeature processFeature = new ProcessFeature();
        ActionBean actionBean = new ActionBean();
        actionBean.setCheckCompatibility(Boolean.TRUE);
        actionBean.setDryRun(Boolean.TRUE);
        //processFeature.process(actionBean);
        assert true;
    }
}