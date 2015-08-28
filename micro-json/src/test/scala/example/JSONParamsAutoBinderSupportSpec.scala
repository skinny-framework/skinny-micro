package example

import org.scalatra.test.scalatest.ScalatraFlatSpec
import skinny.json.JSONStringOps
import skinny.micro.WebApp
import skinny.micro.contrib.JSONParamsAutoBinderSupport

class JSONParamsAutoBinderSupportSpec extends ScalatraFlatSpec {

  behavior of "JSONParamsAutoBinderFeature"

  object Controller extends WebApp with JSONParamsAutoBinderSupport {
    post("/") {
      params.getAs[String]("name") should equal(Some("Alice"))
    }
  }
  addFilter(Controller, "/*")

  it should "accepts json body as params" in {
    val body = JSONStringOps.toJSONString(Map("name" -> "Alice"))
    val headers = Map("Content-Type" -> "application/json")
    post("/", body, headers) {
      status should equal(200)
    }
  }

}
