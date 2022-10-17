/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.model.string.util.strategy;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.util.Objects;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.model.string.util.StringSizeControlStrategyFactory.Strategy;

/**
 * Truncate the input String value to fit the input maxBytes(maximum bytes) size and return the truncated string. 
 */
public class MaxBytesStringSizeControlStrategy implements StringSizeControlStrategy {

    private static final Logger LOG = Logger.getLogger(MaxBytesStringSizeControlStrategy.class.getName());
    
    @Override
    public String truncateString(String value, int maximumBytes) {
        
        LOG.fine("truncate input string " +value + ", to " + maximumBytes);
        Objects.requireNonNull(value);
        Charset charset = Charset.forName("UTF-8");
        CharsetDecoder charsetDecoder = charset.newDecoder();
        byte[] byteValue = value.getBytes(charset);
        if (byteValue.length <= maximumBytes) {
            return value;
        }
        // Ensure truncation by having byteBuffer = maximumBytes
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteValue, 0, maximumBytes);
        CharBuffer charBuffer = CharBuffer.allocate(maximumBytes);
        // Ignore an incomplete character
        charsetDecoder.onMalformedInput(CodingErrorAction.IGNORE);
        charsetDecoder.decode(byteBuffer, charBuffer, true);
        charsetDecoder.flush(charBuffer);
        return new String(charBuffer.array(), 0, charBuffer.position());
    }

    @Override
    public String getStrategyIdentifier() {
        return Strategy.MAX_BYTES.getValue();
    }

}
