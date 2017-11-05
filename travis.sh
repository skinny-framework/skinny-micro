#!/bin/bash -eu
export SKINNY_ENV=test
sbt clean ++$TRAVIS_SCALA_VERSION test doc
git diff --exit-code
