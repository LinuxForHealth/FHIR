# IBM FHIR Server Docs
This is the source for the IBM FHIR Server GitHub Pages site at https://ibm.github.io/FHIR.

We use Gatsby with the Carbon Theme to generate the static site.
You can access the source of the theme at https://github.com/carbon-design-system/gatsby-theme-carbon.

The site uses the following gatsby plugins:
- [gatsby-plugin-slug](https://www.gatsbyjs.org/packages/gatsby-plugin-slug/)
    - resolves inter-page links e.g. conformance to license is resolved to /FHIR/LICENSE
- [gatsby-plugin-manifest](https://www.gatsbyjs.org/packages/gatsby-plugin-manifest/?=gatsby-plugin-manifest)
    - favicon, general theme details, metadata
- [gatsby-plugin-sitemap](https://www.gatsbyjs.org/packages/gatsby-plugin-sitemap/)
    - makes the site searchable

The site supports `mdx` and `md` formatted files.

# Building the Site
Execute the following steps:

1 - npm install
    - if prompted, install these plugins:
        npm install --save gatsby-plugin-manifest
        npm install --save gatsby-plugin-slug
        npm install --save gatsby-plugin-manifest

2 - gatsby build --prefix-paths

The build should output:
```
08:02:18-paulbastide@pauls-mbp:~/git/wffh/oct27/FHIR/docs$ gatsby build --prefix-paths 
success open and validate gatsby-configs - 0.090 s
success load plugins - 1.701 s
success onPreInit - 0.017 s
success delete html and css files from previous builds - 0.019 s
success initialize cache - 0.031 s
success copy gatsby files - 0.080 s
success onPreBootstrap - 1.931 s
success source and transform nodes - 4.337 s
success building schema - 0.272 s
success createPages - 0.014 s
success createPagesStatefully - 1.165 s
success onPreExtractQueries - 0.014 s
success update schema - 0.071 s
success extract queries from components - 0.243 s
success write out requires - 0.017 s
success write out redirect data - 0.015 s
success Build manifest and related icons - 0.110 s
success Build manifest and related icons - 0.129 s
success onPostBootstrap - 0.268 s
⠀
info bootstrap finished - 15.674 s
⠀
success run static queries - 0.049 s — 6/6 187.18 queries/second
success Building production JavaScript and CSS bundles - 105.557 s
success Rewriting compilation hashes - 0.029 s
success run page queries - 0.087 s — 19/19 312.56 queries/second
success Building static HTML for pages - 21.400 s — 19/19 14.81 pages/second
info Done building in 142.923 sec
```

3 - gatsby serve (to check)

4 - on a different repo, copy the files from `public/` over to the gh-pages branch at the root

5 - git add .

6 - git push

If you are editing files, I recommend cloning gatsby-theme-carbon repository, and editing locally.

# Updating Components

- Navigation (on left)
Update the `src/data/nav-items.yaml`

- Footer
Update the `src/gatsby-theme-carbon/components/Footer.js`
Only one column and content area used. (RIGHT column not used)

- Header
The custom header `src/gatsby-theme-carbon/components/Header.js` is in this file.
This is the only one that should be edited.

The other files are theme.

# Links
- [Gatsby Theme: Carbon](https://github.com/carbon-design-system/gatsby-theme-carbon)
- [Gatsby Theme Carbon: Guides](https://gatsby-theme-carbon.now.sh/guides/)
- [Carbon Design](https://www.carbondesignsystem.com/components/link/code/)
- [Carbon Theme Details](http://react.carbondesignsystem.com/?path=/story/link--default)
- [MDX](https://mdxjs.com/)

FHIR® is the registered trademark of HL7 and is used with the permission of HL7.
