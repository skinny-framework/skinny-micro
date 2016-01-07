package example

import skinny.micro._
import org.scalatra.test.scalatest._

// skinny-micro's before/after in filter apps don't effect unmatched routes
class BeforeAfterFilterSpec extends ScalatraFlatSpec {

  addFilter(new WebApp {
    before() {
      response.setHeader("x-first", "1")
    }
    error { case e => e.printStackTrace() }
    get("/first") {
      "first one"
    }
  }, "/*")

  addFilter(new WebApp {
    before() {
      response.setHeader("x-second", "1")
    }
    get("/second") {
      "second one"
    }
  }, "/*")

  it should "not effect other web apps" in {
    get("/first") {
      status should equal(200)
      body should equal("first one")
      header.get("x-first") should equal(Some("1"))
      header.get("x-second") should equal(None)
    }

    get("/second") {
      status should equal(200)
      body should equal("second one")
      header.get("x-first") should equal(None)
      header.get("x-second") should equal(Some("1"))
    }
  }

}
