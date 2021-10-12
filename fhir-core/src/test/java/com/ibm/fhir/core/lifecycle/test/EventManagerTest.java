/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.core.lifecycle.test;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.mockito.Mockito;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.core.lifecycle.EventCallback;
import com.ibm.fhir.core.lifecycle.EventManager;

/**
 * Unit tests for {@link EventManager} lifecycle event distribution
 */
public class EventManagerTest {
    private final Object serviceManagerId = new Object();

    /**
     * Register the serviceManagerId with the EventManagerImpl singleton. All future
     * lifecycle requests (in this JVM) must pass this exact same serviceManagerId
     * to indicate the caller is authorized in the code to make the call.
     */
    @BeforeClass
    public void setup() {
        EventManager.registerServiceManagerId(serviceManagerId);
    }

    /**
     * Test that we don't get an exception when providing the correct serviceManagerId
     */
    @Test
    public void testGoodId() {
        EventManager.serverReady(serviceManagerId);
    }
    
    /**
     * Test that an exception is generated if the wrong serviceManagerId is provided
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testBadId() {
        final Object badServiceManagerId = new Object();
        EventManager.serverReady(badServiceManagerId);
    }

    /**
     * Test that the lifecycle callbacks are invoked on all registered
     * EventCallback instances.
     */
    @Test
    public void testCallback() {
        
        EventCallback cb1 = Mockito.mock(EventCallback.class);
        EventCallback cb2 = Mockito.mock(EventCallback.class);
        EventManager.register(cb1);
        EventManager.register(cb2);

        // Set up the mock to capture the callback
        EventManager.serverReady(serviceManagerId);
        verify(cb1).serverReady();
        verify(cb2).serverReady();
        verify(cb1, never()).startShutdown();
        verify(cb2, never()).startShutdown();
        verify(cb1, never()).finalShutdown();
        verify(cb2, never()).finalShutdown();
        
        EventManager.startShutdown(serviceManagerId);
        verify(cb1).startShutdown();
        verify(cb2).startShutdown();
        verify(cb1, never()).finalShutdown();
        verify(cb2, never()).finalShutdown();
        
        EventManager.finalShutdown(serviceManagerId);
        verify(cb1).finalShutdown();
        verify(cb2).finalShutdown();
    }
}