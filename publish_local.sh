#!/bin/sh

sbt ++2.11.7 \
  clean \
  microCommon/publishLocal \
  micro/publishLocal \
  microJson4s/publishLocal \
  microJackson/publishLocal \
  microJacksonXml/publishLocal \
  microScalate/publishLocal \
  microServer/publishLocal \
  microTest/publishLocal \
  ++2.10.5 \
  clean \
  microCommon/publishLocal \
  micro/publishLocal \
  microJson4s/publishLocal \
  microJackson/publishLocal \
  microJacksonXml/publishLocal \
  microScalate/publishLocal \
  microServer/publishLocal \
  microTest/publishLocal
