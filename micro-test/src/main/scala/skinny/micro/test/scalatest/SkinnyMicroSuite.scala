package skinny.micro.test.scalatest

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{ BeforeAndAfterAll, Matchers, Suite }
import skinny.micro.test.SkinnyMicroTests

/**
 * Provides Scalatra test support to ScalaTest suites.  The servlet tester
 * is started before the first test in the suite and stopped after the last.
 */
@RunWith(classOf[JUnitRunner])
trait SkinnyMicroSuite
    extends Suite
    with SkinnyMicroTests
    with BeforeAndAfterAll
    with Matchers {

  override protected def beforeAll(): Unit = start()

  override protected def afterAll(): Unit = stop()

}
