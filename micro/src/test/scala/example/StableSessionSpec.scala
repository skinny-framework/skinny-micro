package example

import org.scalatra.test.scalatest.ScalatraFlatSpec
import skinny.micro.AsyncSkinnyMicroServlet

import scala.concurrent.Future

class StableSessionSpec extends ScalatraFlatSpec {

  addServlet(new AsyncSkinnyMicroServlet {
    override def useMostlyStableHttpSession = true

    get("/foo") { implicit ctx =>
      session.setAttribute("foo", "bar")
      Future {
        session.getAttribute("foo")
      }
    }

    get("/bar") { implicit ctx =>
      Future {
        session.getAttribute("bar")
      }
    }
    put("/bar") { implicit ctx =>
      session.setAttribute("bar", params.get("value").orNull[String])
      session.getAttribute("bar")
    }
    put("/bar/async") { implicit ctx =>
      Future {
        session.setAttribute("bar", params.get("value").orNull[String])
        session.getAttribute("bar")
      }
    }

  }, "/app/*")

  it should "handle session attributes" in {
    get("/app/foo") {
      status should equal(200)
      body should equal("bar")
    }
  }

  it should "set/get session attributes" in {
    session {
      get("/app/bar") {
        status should equal(200)
        body should equal("")
      }
    }

    session {
      put("/app/bar", Map("value" -> "skinny-micro")) {
        status should equal(200)
        body should equal("skinny-micro")
      }
      get("/app/bar") {
        status should equal(200)
        body should equal("skinny-micro")
      }
    }

    session {
      put("/app/bar/async", Map("value" -> "12345")) {
        status should equal(200)
        body should equal("12345")
      }
      get("/app/bar") {
        status should equal(200)
        body should equal("12345")
      }
    }
  }

}
