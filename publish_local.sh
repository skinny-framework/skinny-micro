#!/bin/sh

sbt ++2.12.4 \
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
