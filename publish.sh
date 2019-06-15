#!/bin/sh

sbt ++2.13.0 \
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
  ++2.12.8 \
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
  ++2.11.12 \
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
