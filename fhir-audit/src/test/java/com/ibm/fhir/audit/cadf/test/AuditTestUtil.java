/*
 * (C) Copyright IBM Corp. 2016, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.audit.cadf.test;

import java.io.IOException;
import java.io.InputStream;

public class AuditTestUtil {
    public static InputStream generateExceptionStream() {
        InputStream is = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("Fail");
            }
        };
        return is;
    }
}
