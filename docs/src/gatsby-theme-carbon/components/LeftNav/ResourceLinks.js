/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

import React from 'react';
import ResourceLinks from 'gatsby-theme-carbon/src/components/LeftNav/ResourceLinks';
const links = [
  {
    title: 'Javadoc',
    href: "https://ibm.github.io/FHIR/javadocs/latest/"
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
  },
  
  
  
];

// shouldOpenNewTabs: true if outbound links should open in a new tab
const CustomResources = () => <ResourceLinks shouldOpenNewTabs links={links} />;

export default CustomResources;
