#!/bin/sh

sbt ++2.13.1 \
  clean \
  microCommon/publishLocal \
  micro/publishLocal \
  microJson4s/publishLocal \
  microJackson/publishLocal \
  microJacksonXml/publishLocal \
  microScalate/publishLocal \
  microServer/publishLocal \
  microTest/publishLocal