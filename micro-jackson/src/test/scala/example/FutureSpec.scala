package example

import org.scalatra.test.scalatest.ScalatraFlatSpec
import skinny.micro.contrib.jackson.JSONSupport
import skinny.micro.{ AsyncSkinnyMicroServlet, ServletConcurrencyException }

import scala.concurrent.Future

class FutureSpec extends ScalatraFlatSpec {

  addServlet(new AsyncSkinnyMicroServlet with JSONSupport {

    get("/") { implicit ctx =>
      responseAsJSON(params)
    }

    get("/future") { implicit ctx =>
      Future {
        responseAsJSON(params)
      }
    }

    get("/no-future-error") { implicit ctx =>
      Future {
        try {
          responseAsJSON(params)
        } catch {
          case e: ServletConcurrencyException =>
            Map("message" -> e.getMessage)
        }
      }
    }
  }, "/*")

  it should "simply work" in {
    get("/?foo=bar") {
      status should equal(200)
      body should equal("""{"foo":"bar"}""")
    }
  }
  it should "fail with simple Future" in {
    get("/no-future-error?foo=bar") {
      status should equal(200)
      var found = false
      var count = 0
      while (!found && count < 10) {
        if (body.contains("Concurrency Issue Detected")) {
          found = true
        } else {
          count += 1
        }
      }
      found should be(false)
    }
  }
  it should "work with futureWithContext" in {
    get("/future?foo=bar") {
      status should equal(200)
      body should equal("""{"foo":"bar"}""")
    }
  }

}
