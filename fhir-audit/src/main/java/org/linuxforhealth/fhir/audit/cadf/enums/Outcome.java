/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.audit.cadf.enums;

/**
 * "Root" outcome classification.
 *
 * "success" -- The attempted action completed successfully with the expected
 * results.
 *
 * "failure" -- The attempted action failed due to some form of operational
 * system failure or because the action was denied, blocked, or refused in some
 * way.
 *
 * "unknown" -- The outcome of the attempted action is unknown and it is not
 * expected that it will ever be known. Example: data sent to a third party via
 * an unreliable protocol without acknowledgment.
 *
 * "pending" -- The outcome of the attempted action is unknown, but it is
 * expected that it will be known at some point in the future. A separate,
 * future, correlated event may provide additional detail. Example: a long-
 * running activity has started; the Observer will follow up with a nearly
 * identical event that includes the final outcome.
 */
public enum Outcome {
    success, failure, unknown, pending
}