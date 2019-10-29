import React, { useLayoutEffect } from 'react';

import LeftNav from 'gatsby-theme-carbon/src/components/LeftNav';
import Meta from 'gatsby-theme-carbon/src/components/Meta';
import Header from 'gatsby-theme-carbon/src/components/Header';
import Footer from 'gatsby-theme-carbon/src/components/Footer';
import Container from 'gatsby-theme-carbon/src/components/Container';

import { AnchorLink , AnchorLinks } from 'gatsby-theme-carbon/src/components/AnchorLinks';
import { PageDescription } from 'gatsby-theme-carbon/src/components/PageDescription';
import MDXProvider from 'gatsby-theme-carbon/src/components/MDXProvider';

const Layout = ({ children, homepage, shouldHideHeader, ...rest }) => {
  const is404 = children.key === null;

  useLayoutEffect(() => {
    // eslint-disable-next-line global-require
    require('smooth-scroll')('a[href*="#"]', {
      speed: 400,
      durationMin: 250,
      durationMax: 700,
      easing: 'easeInOutCubic',
      clip: true,
      offset: 48,
    });
  }, []);

  return (
    <>
      <Meta />
      <Header shouldHideHeader={shouldHideHeader} />
      <LeftNav
        shouldHideHeader={shouldHideHeader}
        homepage={homepage}
        is404Page={is404}
      />
      <Container homepage={homepage}>
        <MDXProvider>{children}</MDXProvider>
        <Footer />
      </Container>
    </>
  );
};

export default Layout;
