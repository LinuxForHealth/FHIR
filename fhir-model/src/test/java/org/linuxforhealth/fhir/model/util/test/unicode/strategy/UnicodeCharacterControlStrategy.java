/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.util.test.unicode.strategy;

import java.nio.ByteBuffer;

import org.linuxforhealth.fhir.model.util.test.unicode.UnicodeChar;
import org.linuxforhealth.fhir.model.util.test.unicode.strategy.StrategyFactory.Location;

/**
 *
 */
public class UnicodeCharacterControlStrategy implements CharacterControlStrategy {
    private Location location;
    private UnicodeChar unicodeChar;

    // The current location in the stream (it cloud be very long...).
    private long len = 0;

    public UnicodeCharacterControlStrategy(Location location, UnicodeChar unicodeChar) {
        this.location = location;
        this.unicodeChar = unicodeChar;
    }

    @Override
    public boolean isApplicable(int len, ByteBuffer dst) {
        this.len += len;
        return true;
    }

    @Override
    public byte[] pre() {
        switch (location) {
        case START:
            return getBytes();
        default:
            return new byte[0];
        }
    }

    @Override
    public byte[] post() {
        switch (location) {
        case END:
            return getBytes();
        default:
            return new byte[0];
        }
    }

    public long getCount() {
        return len;
    }

    /**
     * gets the bytes related to this padding/overload.
     *
     * @return
     */
    public byte[] getBytes() {
        return unicodeChar.getBytes();
    }
}