/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.provider.impl.unicode;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

import com.ibm.fhir.model.util.test.unicode.strategy.CharacterControlStrategy;

/**
 * InjectCharacterChannel modifies the Channel based on the CharacterControlStategy.
 *
 * @implNote The injection (today) occurs on the first key. It's designed to be extensible, such that
 * other hooks on the stream are used for injection.
 */
public class InjectCharacterChannel implements ReadableByteChannel {

    private ReadableByteChannel channel;
    private CharacterControlStrategy strategy;
    private boolean first = true;

    public InjectCharacterChannel(ReadableByteChannel channel, CharacterControlStrategy strategy) {
        this.channel = channel;
        this.strategy = strategy;
    }

    @Override
    public void close() throws IOException {
        channel.close();
    }

    @Override
    public boolean isOpen() {
        return channel.isOpen();
    }

    @Override
    public int read(ByteBuffer dst) throws IOException {
        /*
         * Based on the strategy, the ReadableByteChannel is updated.
         */
        ByteBuffer temp = ByteBuffer.allocate(4096);
        int len = channel.read(temp);

        if (strategy.isApplicable(len, temp) && len > 0) {
            byte[] pre = strategy.pre();

            // Translate the body to a smaller byte array.
            if (first) {
                len++;
            }
            byte[] body = new byte[len];

            int pad = 0;
            for (int index = 0; index < len; index++) {
                byte t = temp.get(index - pad);
                int x = t;
                if (x == 34 && first) {
                    body[index] = t;
                    index++;
                    body[index] = strategy.pre()[0];
                    pad = 1;
                    first = false;
                } else {
                    body[index] = t;
                }
            }
            byte[] post = strategy.post();

            // Don't reallocate the dst buffer above.
            dst.put(body);
            //len += post.length;
        } else if (len > 0) {
            byte[] body = new byte[len];
            for (int index = 0; index < len; index++) {
                byte t = temp.get(index);
                body[index] = t;
            }
            dst = dst.put(body);
        }
        return len;
    }
}