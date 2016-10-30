package org.scalatra.test

import skinny.test._
import skinny.test.scalatest._

package object scalatest {

  @deprecated("Use skinny.test.scalatest.SkinnyFeatureSpec instead.", "Skinny Framework 2.0.0, Skinny Micro 1.0.0")
  type ScalatraFeatureSpec = SkinnyFeatureSpec

  @deprecated("Use skinny.test.scalatest.SkinnyFlatSpec instead.", "Skinny Framework 2.0.0, Skinny Micro 1.0.0")
  type ScalatraFlatSpec = SkinnyFlatSpec

  @deprecated("Use skinny.test.scalatest.SkinnyFreeSpec instead.", "Skinny Framework 2.0.0, Skinny Micro 1.0.0")
  type ScalatraFreeSpec = SkinnyFreeSpec

  @deprecated("Use skinny.test.scalatest.SkinnyFunSpec instead.", "Skinny Framework 2.0.0, Skinny Micro 1.0.0")
  type ScalatraFunSpec = SkinnyFunSpec

  @deprecated("Use skinny.test.scalatest.SkinnyFunSuite instead.", "Skinny Framework 2.0.0, Skinny Micro 1.0.0")
  type ScalatraFunSuite = SkinnyFunSuite

  @deprecated("Use skinny.test.scalatest.SkinnyJUnit3Suite instead.", "Skinny Framework 2.0.0, Skinny Micro 1.0.0")
  type ScalatraJUnit3Suite = SkinnyJUnit3Suite

  @deprecated("Use skinny.test.scalatest.SkinnyJUnitSuite instead.", "Skinny Framework 2.0.0, Skinny Micro 1.0.0")
  type ScalatraJUnitSuite = SkinnyJUnitSuite

  @deprecated("Use skinny.test.scalatest.SkinnySpec instead.", "Skinny Framework 2.0.0, Skinny Micro 1.0.0")
  type ScalatraSpec = SkinnySpec

  @deprecated("Use skinny.test.scalatest.SkinnyWordSpec instead.", "Skinny Framework 2.0.0, Skinny Micro 1.0.0")
  type ScalatraWordSpec = SkinnyWordSpec

}
