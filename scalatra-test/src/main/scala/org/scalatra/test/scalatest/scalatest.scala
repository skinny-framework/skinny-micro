package org.scalatra.test
package scalatest

import org.junit.runner.RunWith
import org.scalatest._
import org.scalatestplus.junit.{ JUnitSuite, JUnit3Suite, JUnitRunner }
import org.scalatestplus.testng.TestNGSuite
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

@RunWith(classOf[JUnitRunner]) /**
 * Provides Scalatra test support to ScalaTest suites.  The servlet tester
 * is started before the first test in the suite and stopped after the last.
 */
trait ScalatraSuite extends Suite with ScalatraTests with BeforeAndAfterAll with Matchers {
  override protected def beforeAll(): Unit = start()
  override protected def afterAll(): Unit = stop()
}

/**
 * Convenience trait to add Scalatra test support to JUnit3Suite.
 */
trait ScalatraJUnit3Suite extends JUnit3Suite with ScalatraSuite

/**
 * Convenience trait to add Scalatra test support to JUnitSuite.
 */
trait ScalatraJUnitSuite extends JUnitSuite with ScalatraSuite

/**
 * Convenience trait to add Scalatra test support to TestNGSuite.
 */
trait ScalatraTestNGSuite extends TestNGSuite with ScalatraSuite

/**
 * Convenience trait to add Scalatra test support to FeatureSpec.
 */
trait ScalatraFeatureSpec extends AnyFeatureSpec with ScalatraSuite

/**
 * Convenience trait to add Scalatra test support to Spec.
 */
trait ScalatraSpec extends AnyFunSpec with ScalatraSuite

/**
 * Convenience trait to add Scalatra test support to FlatSpec.
 */
trait ScalatraFlatSpec extends AnyFlatSpec with ScalatraSuite

/**
 * Convenience trait to add Scalatra test support to FreeSpec.
 */
trait ScalatraFreeSpec extends AnyFreeSpec with ScalatraSuite

/**
 * Convenience trait to add Scalatra test support to WordSpec.
 */
trait ScalatraWordSpec extends AnyWordSpec with ScalatraSuite

/**
 * Convenience trait to add Scalatra test support to FunSuite.
 */
trait ScalatraFunSuite extends AnyFunSuite with ScalatraSuite
