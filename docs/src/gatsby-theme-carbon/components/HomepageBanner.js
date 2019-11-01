/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

import React from 'react';
import styled from '@emotion/styled';
import { Grid, Row, Column } from 'gatsby-theme-carbon';

import { column, row, grid } from './Banner.module.scss';

const StyledGrid = styled(Grid)`
  max-width: 100%;
  background-position: ${props => (props.position ? props.position : 'center')};
`;

const HomepageBanner = ({ image, position, renderText }) => (
  <StyledGrid className={grid} position={position}>
    <Row className={row}>
      <Column className={column}>{renderText()}</Column>
    </Row>
  </StyledGrid>
);

export default HomepageBanner;