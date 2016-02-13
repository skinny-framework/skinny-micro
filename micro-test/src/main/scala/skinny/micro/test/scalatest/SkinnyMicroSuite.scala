package skinny.micro.test.scalatest

import org.junit.runner.RunWith
import org.scalatest.exceptions.TestFailedException
import org.scalatest.junit.JUnitRunner
import org.scalatest.{ BeforeAndAfterAll, Matchers, Suite }
import skinny.micro.test.SkinnyMicroTests

/**
 * Provides Skinny Micro test support to ScalaTest suites.
 * The servlet tester is started before the first test in the suite and stopped after the last.
 */
@RunWith(classOf[JUnitRunner])
trait SkinnyMicroSuite
    extends Suite
    with SkinnyMicroTests
    with BeforeAndAfterAll
    with Matchers {

  override protected def beforeAll(): Unit = start()

  override protected def afterAll(): Unit = stop()

  /**
   * Tries the same test code block until times.
   */
  protected def withRetries[A](times: Int)(block: => A): A = {
    def _retry(block: => A, count: Int): A = {
      try {
        block
      } catch {
        case _: TestFailedException if count < times => _retry(block, count + 1)
        case e: TestFailedException => throw e
      }
    }
    _retry(block, 1)
  }

}
