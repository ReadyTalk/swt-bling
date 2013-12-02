#!/bin/bash
# This script initiates the Gradle Publish task when pushes to master occur.

if [ "$TRAVIS_PULL_REQUEST" == "false" ] && [ "$TRAVIS_BRANCH" == "master" ]; then
  echo -e "Starting publish to Sonatype...\n"

  # Uncomment when publish functionality is implemented
  #./gradle publish

  echo -e "Completed publish!\n"
fi