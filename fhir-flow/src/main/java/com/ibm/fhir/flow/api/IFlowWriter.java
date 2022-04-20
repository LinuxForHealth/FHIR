/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.flow.api;


/**
 * Interface representing downstream writers to which we submit {@link FlowInteraction}
 * requests.
 */
public interface IFlowWriter {
    /**
     * Submit this interaction to the writer queue. Interactions are
     * processed in the order they are received, but in order to be
     * processed, any async operations they depend on must be completed
     * first.
     * 
     * @param interaction the interaction to write
     */
    void submit(FlowInteraction interaction);

    /**
     * Wait here until all the submitted interactions have been processed
     */
    void waitForShutdown();
}
