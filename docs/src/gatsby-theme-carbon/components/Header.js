import React from 'react';
import Header from 'gatsby-theme-carbon/src/components/Header';

const CustomHeader = props => (
  <Header {...props} href="/FHIR">
    <a href="/FHIR" class="bx--header__name"><span>IBM</span>&nbsp;FHIR&reg;&nbsp;Server</a>
  </Header>
);

export default CustomHeader;
