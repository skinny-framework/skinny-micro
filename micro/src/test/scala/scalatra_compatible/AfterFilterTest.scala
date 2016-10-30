package scalatra_compatible

import org.scalatra.test.scalatest.ScalatraFunSuite
import skinny.micro.SkinnyMicroFilter

class AfterTestFilter extends SkinnyMicroFilter {

  after() {
    response.setStatus(204)
  }

  after("/filtered/some/path") {
    response.setStatus(202)
  }

  after("/filtered/other/path") {
    response.setStatus(206)
  }

  get("/filtered/some/path") {}

  get("/filtered/other/path") {}

  get("/filtered/third/path") {}

}
class YetAnotherFilter extends SkinnyMicroFilter {
  get("/yet-another/path") {
    response.setStatus(200)
  }
}

class AfterFilterTest extends ScalatraFunSuite {

  mount(classOf[AfterTestFilter], "/*")
  mount(classOf[YetAnotherFilter], "/*")

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
