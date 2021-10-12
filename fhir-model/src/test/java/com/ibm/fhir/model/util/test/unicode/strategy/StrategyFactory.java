/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.util.test.unicode.strategy;

import com.ibm.fhir.model.util.test.unicode.UnicodeChar;

/**
 * Controls the instantiation of the Control Strategy
 */
public class StrategyFactory {
    private StrategyFactory() {
        // Nop
    }

    /**
     * The various types of Chacter Control Strategies.
     */
    public enum Strategy {
        UTF8,
        UTF16
    }

    /**
     * Location defines where the Unicode data is injected into the given channel.
     */
    public enum Location {
        START,
        END,
        MIDDLE,
        ATTRIBUTE,
        BODY
    }

    /**
     * selects the character control strategy
     * @param strategy
     * @param unicodeChar
     * @return
     */
    public static CharacterControlStrategy getStrategy(Strategy strategy, Location location, UnicodeChar unicodeChar) {
        switch(strategy) {
            case UTF8:
            case UTF16:
                return new UnicodeCharacterControlStrategy(location, unicodeChar);
            default:
                throw new IllegalArgumentException("Invalid Entry in the Enum");
        }
    }
}