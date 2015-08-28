package example

import org.scalatra.test.scalatest._
import skinny.micro.WebApp
import skinny.micro.contrib.CORSSupport

class CORSSupportSpec extends ScalatraFlatSpec {

  behavior of "CORSFeature"

  class SampleController extends WebApp with CORSSupport {
    before() {
      contentType = "application/json"
    }

    def showOk = "ok"
  }
  addFilter(new SampleController {
    get("/ok")(showOk)
  }, "/*")

  it should "have CORS headers" in {
    get("/ok") {
      status should equal(200)
      header("Access-Control-Allow-Origin") should equal("*")
    }
  }

}
