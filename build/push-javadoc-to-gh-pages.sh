#!/bin/bash

if [ "$TRAVIS_PULL_REQUEST" == "false" ] && [ "$TRAVIS_BRANCH" ]; then

  git clone --quiet --branch=gh-pages https://${GITHUB_OAUTH_TOKEN}@github.com/IBM/java-sdk-core.git gh-pages > /dev/null

  pushd gh-pages
    # on tagged builds, $TRAVIS_BRANCH is the tag (e.g. v1.2.3), otherwise it's the branch name (e.g. master)
    rm -rf docs/$TRAVIS_BRANCH
    mkdir -p docs/$TRAVIS_BRANCH
    cp -rf ../target/site/apidocs/* docs/$TRAVIS_BRANCH
    ./../build/generate-index-html.sh > index.html

    git add -f .
    git commit -m "Latest javadoc for $TRAVIS_BRANCH ($TRAVIS_COMMIT)"
    git push -fq origin gh-pages > /dev/null

  popd

  echo -e "Published Javadoc for build $TRAVIS_BUILD_NUMBER ($TRAVIS_JOB_NUMBER) on branch $TRAVIS_BRANCH.\n"

else

  echo -e "Not publishing docs for build $TRAVIS_BUILD_NUMBER ($TRAVIS_JOB_NUMBER) on branch $TRAVIS_BRANCH of repo $TRAVIS_REPO_SLUG"

fi
