/*
 * (C) Copyright IBM Corp. 2019,2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

import React from 'react';
import Header from 'gatsby-theme-carbon/src/components/Header';

const CustomHeader = (props) => (
  <Header {...props} >
    LinuxForHealth&nbsp;FHIR&reg;&nbsp;Server
  </Header>
);

export default CustomHeader;
