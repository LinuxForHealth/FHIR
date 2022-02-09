/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.cliffc.high_scale_lib;

import java.util.concurrent.ConcurrentHashMap;

public class NonBlockingHashMapLong<TypeV> extends ConcurrentHashMap<Long,TypeV>  {
    public NonBlockingHashMapLong( final int initial_sz ) { 
        super(initial_sz);
    }
}
