#!/bin/bash
export SKINNY_ENV=test
sbt clean ++$TRAVIS_SCALA_VERSION test doc
