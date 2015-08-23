package example

import org.scalatra.test.scalatest.ScalatraFlatSpec
import skinny.micro.{ AsyncSkinnyMicroServlet, ServletConcurrencyException }

import scala.concurrent.Future
import scala.concurrent.duration._

class FutureSpec extends ScalatraFlatSpec {

  addServlet(new AsyncSkinnyMicroServlet {

    before() { implicit ctx =>
      Future {
        Thread.sleep(100)
        request.setAttribute("before", "done")
      }
    }

    after() { implicit ctx =>
      Future {
        Thread.sleep(100)
        response.setHeader("foo", "bar")
      }
    }

    get("/") { implicit ctx =>
      params.keys.mkString(",")
    }

    get("/future") { implicit ctx =>
      Future {
        params.keys.mkString(",")
      }
    }

    get("/no-future-error") { implicit ctx =>
      awaitFutures(3.seconds) {
        Future {
          try {
            params.keys.mkString(",")
          } catch {
            case e: ServletConcurrencyException =>
              Map("message" -> e.getMessage)
          }
        }
      }
    }

    get("/before") { implicit ctx =>
      Future {
        request.getAttribute("before")
      }
    }
  }, "/*")

  it should "simply work" in {
    get("/?foo=bar") {
      status should equal(200)
      body should equal("foo")
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
    get("/future?foo=bar&baz=zzz") {
      status should equal(200)
      body should equal("baz,foo")
      header("foo") should equal("bar")
    }
  }

  it should "work with async before filters" in {
    get("/before") {
      status should equal(200)
      body should equal("done")
      header("foo") should equal("bar")
    }
  }

}
