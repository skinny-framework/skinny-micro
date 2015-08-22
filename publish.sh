#!/bin/sh

sbt ++2.11.7 \
  clean \
  microCommon/publishSigned \
  micro/publishSigned \
  microJson/publishSigned \
  microScalate/publishSigned \
  microServer/publishSigned \
  microTest/publishSigned \
  ++2.10.5 \
  clean \
  microCommon/publishSigned \
  micro/publishSigned \
  microJson/publishSigned \
  microScalate/publishSigned \
  microServer/publishSigned \
  microTest/publishSigned
