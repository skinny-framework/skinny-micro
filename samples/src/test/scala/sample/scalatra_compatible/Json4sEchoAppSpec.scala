package sample.scalatra_compatible

import skinny.json4s.JSONStringOps
import skinny.test.SkinnyFunSpec

import scala.util._

class Json4sEchoAppSpec extends SkinnyFunSpec with JSONStringOps {
  addFilter(classOf[Json4sEchoApp], "/*")

  describe("Scalatra compatible EchoApp") {

    it("shows params as JSON") {
      get("/json4s/echo.json", "name" -> "Alice", "age" -> "18") {
        status should equal(200)
        fromJSONString[Map[String, String]](body) should equal(Success(Map("name" -> "Alice", "age" -> "18")))
        header("Content-Type") should equal("application/json;charset=utf-8")
      }
    }

  }

}
