package example

import org.scalatra.test.scalatest.ScalatraFlatSpec
import skinny.micro.WebApp
import skinny.micro.contrib.ChunkedResponseSupport

class ChunkedResponseSupportSpec extends ScalatraFlatSpec {

  behavior of "ChunkedResponseFeature"

  class Controller extends WebApp with ChunkedResponseSupport {
    def index = {
      writeChunk("abc".getBytes)
    }
  }
  addFilter(new Controller {
    get("/")(index)
  }, "/*")

  it should "return chunked response" in {
    get("/") {
      status should equal(200)
      body should equal("abc")
    }
  }

}
