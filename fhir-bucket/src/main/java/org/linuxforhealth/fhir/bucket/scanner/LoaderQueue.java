/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.bucket.scanner;

public interface LoaderQueue {

    void processEntry(String cosBucketName, String cosPath);
}
