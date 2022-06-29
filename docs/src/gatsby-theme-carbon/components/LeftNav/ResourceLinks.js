/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

import React from 'react';
import ResourceLinks from 'gatsby-theme-carbon/src/components/LeftNav/ResourceLinks';
const links = [
  {
    title: 'Javadoc: Latest',
    href: "https://linuxforhealth.github.io/FHIR/javadocs/latest/"
  },
  {
    title: 'Javadoc: All Versions',
    href: "https://linuxforhealth.github.io/FHIR/javadocs/index.html"
  },
  {
    title: 'GitHub: Source',
    href: 'https://github.com/IBM/FHIR/',
  },
  {
    title: 'GitHub: Issues',
    href: 'https://github.com/IBM/FHIR/issues',
  },
  {
    title: 'GitHub: Releases',
    href: 'https://github.com/ibm/fhir/releases',
  },
  {
    title: 'HL7 FHIR',
    href: 'https://www.hl7.org/fhir/index.html',
  }
];

// shouldOpenNewTabs: true if outbound links should open in a new tab
const CustomResources = () => <ResourceLinks shouldOpenNewTabs links={links} />;

export default CustomResources;
