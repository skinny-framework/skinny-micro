#!/bin/sh

sbt ++2.12.4 \
  clean \
  microCommon/publishSigned \
  micro/publishSigned \
  microJson4s/publishSigned \
  microJackson/publishSigned \
  microJacksonXml/publishSigned \
  microScalate/publishSigned \
  microServer/publishSigned \
  microTest/publishSigned \
  scalatraTest/publishSigned \
  ++2.11.11 \
  clean \
  microCommon/publishSigned \
  micro/publishSigned \
  microJson4s/publishSigned \
  microJackson/publishSigned \
  microJacksonXml/publishSigned \
  microScalate/publishSigned \
  microServer/publishSigned \
  microTest/publishSigned \
  scalatraTest/publishSigned \
  sonatypeRelease
