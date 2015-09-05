package org.scalatra

import org.scalatra.test.scalatest.ScalatraFunSuite
import skinny.micro.SkinnyMicroServlet

class AfterTestServlet extends SkinnyMicroServlet {

  after() {
    response.setStatus(204)
  }

  after("/some/path") {
    response.setStatus(202)
  }

  after("/other/path") {
    response.setStatus(206)
  }

  get("/some/path") {}

  get("/other/path") {}

  get("/third/path") {}

}
class YetAnotherServlet extends SkinnyMicroServlet {
  get("/path") {
    response.setStatus(200)
  }
}

class AfterServletTest extends ScalatraFunSuite {
  mount(classOf[AfterTestServlet], "/filtered/*")
  mount(classOf[YetAnotherServlet], "/yet-another/*")

  test("afterAll is applied to all paths") {
    get("/filtered/third/path") {
      status should equal(204)
    }
  }

  test("after only applies to a given path") {
    get("/filtered/some/path") {
      status should equal(202)
    }
    get("/filtered/other/path") {
      status should equal(206)
    }
  }

  test("after won't be applied to another servlets") {
    get("/yet-another/path") {
      status should equal(200)
    }
  }

}
