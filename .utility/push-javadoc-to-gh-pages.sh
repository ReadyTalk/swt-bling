#!/bin/bash

if [ "$TRAVIS_REPO_SLUG" == "ReadyTalk/swt-bling" ] && [ "$TRAVIS_JDK_VERSION" == "oraclejdk7" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ] && [ "$TRAVIS_BRANCH" == "master" ]; then

  echo "Publishing javadoc..."

  cp -R build/docs/javadoc $HOME/javadoc-latest

  cd $HOME
  git config --global user.email "travis@travis-ci.org"
  git config --global user.name "travis-ci"
  git clone --quiet --branch=gh-pages https://${GH_TOKEN}@github.com/ReadyTalk/swt-bling gh-pages > /dev/null

  cd gh-pages
  git rm -rf ./javadoc
  cp -Rf $HOME/javadoc-latest ./javadoc
  git add -f .
  git commit -m "Lastest javadoc on successful travis build $TRAVIS_BUILD_NUMBER auto-pushed to gh-pages"
  if ! git push -fq origin gh-pages &> /dev/null; then
    echo "Error pushing gh-pages to origin. Bad GH_TOKEN? GitHub down?"
  else
    echo "Published Javadoc to gh-pages."
  fi
fi
