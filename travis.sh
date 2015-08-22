#!/bin/bash
export SKINNY_ENV=test
export APP_ENV=test
if [[ "$TRAVIS_SCALA_VERSION" == 2.11* ]]; then
  gem install sass &&
  sbt clean ++$TRAVIS_SCALA_VERSION coverage test
else
  sbt clean ++$TRAVIS_SCALA_VERSION test
fi
