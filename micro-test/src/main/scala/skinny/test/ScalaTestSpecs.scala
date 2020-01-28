package skinny.test

import org.scalatest._
import org.scalatestplus.junit.{ JUnit3Suite, JUnitSuite }
import skinny.micro.test.scalatest.SkinnyMicroSuite
import org.scalatest.featurespec.AnyFeatureSpecLike
import org.scalatest.flatspec.AnyFlatSpecLike
import org.scalatest.freespec.AnyFreeSpecLike
import org.scalatest.funspec.AnyFunSpecLike
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.wordspec.AnyWordSpecLike

/**
 * Convenience trait to add Skinny test support to JUnit3Suite.
 */
trait SkinnyJUnit3Suite
  extends JUnit3Suite
  with SkinnyMicroSuite

/**
 * Convenience trait to add Skinny test support to JUnitSuite.
 */
trait SkinnyJUnitSuite
  extends JUnitSuite
  with SkinnyMicroSuite

/**
 * Convenience trait to add Skinny test support to FeatureSpec.
 */
trait SkinnyFeatureSpec
  extends AnyFeatureSpecLike
  with SkinnyMicroSuite

/**
 * Convenience trait to add Skinny test support to Spec.
 */
trait SkinnySpec
  extends AnyFunSpecLike
  with SkinnyMicroSuite

/**
 * Convenience trait to add Skinny test support to FlatSpec.
 */
trait SkinnyFlatSpec
  extends AnyFlatSpecLike
  with SkinnyMicroSuite

/**
 * Convenience trait to add Skinny test support to FunSpec.
 */
trait SkinnyFunSpec
  extends AnyFunSpecLike
  with SkinnyMicroSuite

/**
 * Convenience trait to add Skinny test support to FreeSpec.
 */
trait SkinnyFreeSpec
  extends AnyFreeSpecLike
  with SkinnyMicroSuite

/**
 * Convenience trait to add Skinny test support to WordSpec.
 */
trait SkinnyWordSpec
  extends AnyWordSpecLike
  with SkinnyMicroSuite

/**
 * Convenience trait to add Skinny test support to FunSuite.
 */
trait SkinnyFunSuite
  extends AnyFunSuite
  with SkinnyMicroSuite
