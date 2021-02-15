#!/bin/sh

sbt ++2.13.1 \
  clean \
  microCommon/publishSigned \
  micro/publishSigned \
  microJson4s/publishSigned \
  microJackson/publishSigned \
  microJacksonXml/publishSigned \
  microScalate/publishSigned \
  microServer/publishSigned \
  microTest/publishSigned \
  scalatraTest/publishSigned