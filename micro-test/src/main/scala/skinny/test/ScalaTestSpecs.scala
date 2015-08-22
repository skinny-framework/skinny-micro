package skinny.test

import org.scalatest._
import org.scalatest.junit.{ JUnit3Suite, JUnitSuite }
import skinny.micro.test.scalatest.SkinnyMicroSuite

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
  extends FeatureSpecLike
  with SkinnyMicroSuite

/**
 * Convenience trait to add Skinny test support to Spec.
 */
trait SkinnySpec
  extends FunSpecLike
  with SkinnyMicroSuite

/**
 * Convenience trait to add Skinny test support to FlatSpec.
 */
trait SkinnyFlatSpec
  extends FlatSpecLike
  with SkinnyMicroSuite

/**
 * Convenience trait to add Skinny test support to FunSpec.
 */
trait SkinnyFunSpec
  extends FunSpecLike
  with SkinnyMicroSuite

/**
 * Convenience trait to add Skinny test support to FreeSpec.
 */
trait SkinnyFreeSpec
  extends FreeSpecLike
  with SkinnyMicroSuite

/**
 * Convenience trait to add Skinny test support to WordSpec.
 */
trait SkinnyWordSpec
  extends WordSpecLike
  with SkinnyMicroSuite

/**
 * Convenience trait to add Skinny test support to FunSuite.
 */
trait SkinnyFunSuite
  extends FunSuite
  with SkinnyMicroSuite
