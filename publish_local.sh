#!/bin/sh

sbt ++2.13.0-RC1 \
  clean \
  microCommon/publishLocal \
  micro/publishLocal \
  microJson4s/publishLocal \
  microJackson/publishLocal \
  microJacksonXml/publishLocal \
  microScalate/publishLocal \
  microServer/publishLocal \
  microTest/publishLocal \
  ++2.12.8 \
  clean \
  microCommon/publishLocal \
  micro/publishLocal \
  microJson4s/publishLocal \
  microJackson/publishLocal \
  microJacksonXml/publishLocal \
  microScalate/publishLocal \
  microServer/publishLocal \
  microTest/publishLocal \
  ++2.11.12 \
  clean \
  microCommon/publishLocal \
  micro/publishLocal \
  microJson4s/publishLocal \
  microJackson/publishLocal \
  microJacksonXml/publishLocal \
  microScalate/publishLocal \
  microServer/publishLocal \
  microTest/publishLocal
