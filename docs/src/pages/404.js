/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

import React from 'react';
import { FourOhFour } from 'gatsby-theme-carbon';

const links = [
  { href: '/', text: 'IBM FHIR Server Homepage' },
];

const Custom404 = () => <FourOhFour links={links} />;

export default Custom404;
