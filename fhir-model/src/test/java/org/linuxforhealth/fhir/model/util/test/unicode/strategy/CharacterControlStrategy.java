/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.util.test.unicode.strategy;

import java.nio.ByteBuffer;

/**
 * The implementation of the CharacterControlStrategy
 * manages the creation of a left or right padding of special unicode characters
 * where the padding is applicable.
 */
public interface CharacterControlStrategy {
    /**
     * applying the strategy is applicable.
     * @param len
     * @param dst
     * @return
     */
    boolean isApplicable(int len, ByteBuffer dst);

    /**
     * the previous padding that makes sense for the given strategy.
     * @return
     */
    byte[] pre();

    /**
     * the post padding that makes sense for the given strategy.
     * @return
     */
    byte[] post();
}
