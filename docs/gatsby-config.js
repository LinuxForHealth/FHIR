module.exports = {
  siteMetadata: {
    title: 'LinuxForHealth FHIR Server',
    description: 'The LinuxForHealth FHIR Server is a modular Java implementation of version 4 of the HL7 FHIR specification with a focus on performance and configurability.',
    keywords: 'ibm,fhir,server',
    siteUrl: 'https://linuxforhealth.github.io'
  },
  pathPrefix: '/FHIR',
  plugins: [
    "gatsby-plugin-slug",
    {
      resolve : 'gatsby-theme-carbon',
      options : {
        mdxExtensions: ['.mdx', '.md'],
        titleType: 'append',
        isSearchEnabled: false,
        repository: {
          baseUrl: 'https://github.com/LinuxForHealth/FHIR',
          subDirectory: '/docs',
          branch: 'main',
        },
        withWebp: true,
        iconPath: 'src/images/fhir-transparent.png',
        display: 'browser',
      }
    },
    {
      resolve: 'gatsby-plugin-manifest',
      options: {
        name: 'IBM FHIR Server',
        short_name: 'IBM FHIR Server',
        start_url: '/FHIR',
        background_color: '#ffffff',
        theme_color: '#0062ff',
        display: 'browser',
        icon: "src/images/fhir-transparent.png",
      },
    },
    {
      resolve: `gatsby-source-filesystem`,
      options: {
        path: `${__dirname}/src/pages`,
        name: `pages`,
      },
    },
    {
      resolve: `gatsby-transformer-remark`,
      options: {
        // CommonMark mode (default: true)
        commonmark: true,
        // Footnotes mode (default: true)
        footnotes: true,
        // Pedantic mode (default: true)
        pedantic: true,
        // GitHub Flavored Markdown mode (default: true)
        gfm: true,
        // Plugins configs
        plugins: [],
      },
    },
    {
    resolve: 'gatsby-plugin-sitemap',
    options: {
      output: '/sitemap.xml',
      exclude: ["/build/", ],
      query: `
        {
          site {
            siteMetadata {
              siteUrl
            }
          }

          allSitePage {
            edges {
              node {
                path
              }
            }
          }
      }`,
      serialize: ({ site, allSitePage }) =>
        allSitePage.edges.map(edge => {
          return {
            url: site.siteMetadata.siteUrl + edge.node.path,
            changefreq: 'daily',
            priority: 0.7,
          }
        })
    }
  },
  ],
  mapping: {
    "MarkdownRemark.frontmatter.author": `AuthorYaml`,
  },
};
