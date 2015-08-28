package example

import org.scalatest._
import skinny.micro.WebApp

class EnvAccessorSpec extends FlatSpec with Matchers {

  behavior of "EnvFeature"

  it should "have predicate methods" in {
    class Controller extends WebApp {
      isDevelopment() should equal(false)
      isTest() should equal(true)
      isStaging() should equal(false)
      isProduction() should equal(false)
    }
  }

  it should "convert the default value of Scalatra's environment to lower cased one" in {
    class Controller extends WebApp {
      skinnyEnv should equal(None)
    }
  }

}
