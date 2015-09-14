package example

import org.scalatra.test.scalatest.ScalatraFlatSpec
import skinny.micro.response._
import skinny.micro.{ TypedAsyncSkinnyMicroServlet, ServletConcurrencyException }

import scala.concurrent.Future

class TypedFutureSpec extends ScalatraFlatSpec {

  addServlet(new TypedAsyncSkinnyMicroServlet {

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
      Future.successful(Ok(params.keys.mkString(",")))
    }

    get("/future") { implicit ctx =>
      Future {
        Ok(params.keys.mkString(","))
      }
    }

    get("/no-future-error") { implicit ctx =>
      Future {
        try {
          Ok(params.keys.mkString(","))
        } catch {
          case e: ServletConcurrencyException =>
            Ok(Map("message" -> e.getMessage))
        }
      }
    }

    get("/before") { implicit ctx =>
      Future {
        Ok(request.getAttribute("before"))
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
