/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.query;

/**
 * An adapter which represents just a fragment of the where clause
 * so we can build up just a portion of the predicate without having
 * access to the whole clause
 */
public class WhereFragment extends BaseWhereAdapter<WhereFragment> {

    @Override
    protected WhereFragment getThis() {
        return this;
    }
}