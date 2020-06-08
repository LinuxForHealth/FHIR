module.exports = {
  siteMetadata: {
    title: 'IBM FHIR Server',
    description: 'The IBM FHIR Server is a modular Java implementation of version 4 of the HL7 FHIR specification with a focus on performance and configurability.',
    keywords: 'ibm,fhir,server',
    siteUrl: 'https://ibm.github.io'
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
          baseUrl: 'https://github.com/IBM/FHIR',
          subDirectory: '/docs',
          branch: 'master',
        },
      }
    }, 
    {
      resolve: 'gatsby-plugin-manifest',
      options: {
        name: 'IBM FHIR Server',
        short_name: 'IBM FHIR Server',
        start_url: '/',
        background_color: '#ffffff',
        theme_color: '#0062ff',
        display: 'browser',
        icon: "src/images/fhir-transparent.png",
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
