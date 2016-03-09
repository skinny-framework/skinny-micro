#!/bin/sh

sbt ++2.11.8 \
  clean \
  microCommon/publishSigned \
  micro/publishSigned \
  microJson4s/publishSigned \
  microJackson/publishSigned \
  microJacksonXml/publishSigned \
  microScalate/publishSigned \
  microServer/publishSigned \
  microTest/publishSigned \
  ++2.10.6 \
  clean \
  microCommon/publishSigned \
  micro/publishSigned \
  microJson4s/publishSigned \
  microJackson/publishSigned \
  microJacksonXml/publishSigned \
  microScalate/publishSigned \
  microServer/publishSigned \
  microTest/publishSigned
