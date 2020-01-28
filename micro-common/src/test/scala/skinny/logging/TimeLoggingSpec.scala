package skinny.logging

import org.scalatest._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TimeLoggingSpec extends AnyFlatSpec with Matchers with TimeLogging {

  behavior of "TimeLogging"

  it should "work" in {
    val result = warnElapsedTime(10) {
      Thread.sleep(100)
      "AAA"
    }
    result should equal("AAA")
  }

}
