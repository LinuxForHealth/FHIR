/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

import React from 'react';
import Footer from 'gatsby-theme-carbon/src/components/Footer';

const Content = () => (
  <>
    <p>
      The LinuxForHealth FHIR Server is licensed under the Apache 2.0 license. Full license text is available at <a href="https://linuxforhealth.github.io/FHIR/LICENSE">LICENSE</a>.
    </p>
    <p>
      <a href="https://www.hl7.org/fhir/index.html">FHIR&reg;</a> is the registered trademark of HL7 and is used with the permission of HL7. Use of the FHIR trademark does not constitute endorsement of this product by HL7.
      <br/>
      IBM and the IBM logo are trademarks of International Business Machines Corporation, registered in many jurisdictions worldwide. Other product and service names might be trademarks of IBM or other companies. A current list of IBM trademarks is available on <a href="https://ibm.com/trademark">ibm.com/trademark</a>.
    </p>
  </>
);

const links = {
  firstCol: [
    { href: 'https://github.com/LinuxForHealth/fhir', linkText: 'IBM FHIR Server on GitHub' },
    { href: 'https://www.hl7.org/fhir/index.html', linkText: 'HL7 FHIR' },
  ],
  
};

const CustomFooter = () => <Footer links={links} Content={Content} />;

export default CustomFooter;
