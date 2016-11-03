package sample.scalatra_compatible

import skinny.jackson.JSONStringOps
import skinny.test.SkinnyFunSpec
import scala.util._

class EchoAppSpec extends SkinnyFunSpec with JSONStringOps {
  addFilter(classOf[EchoApp], "/*")

  describe("Scalatra compatible EchoApp") {

    it("shows params as JSON") {
      get("/echo.json", "name" -> "Alice", "age" -> "18") {
        status should equal(200)
        fromJSONString[Map[String, String]](body) should equal(Success(Map("name" -> "Alice", "age" -> "18")))
        header("Content-Type") should equal("application/json;charset=utf-8")
      }
    }

    it("shows greeting") {
      post("/hello/Martin") {
        status should equal(200)
        body should equal("Hello, Martin")
        header("Content-Type") should equal("text/plain;charset=utf-8")
      }

      post("/hello/Martin", "with" -> "Love") {
        status should equal(200)
        body should equal("Hello, Martin with Love")
        header("Content-Type") should equal("text/plain;charset=utf-8")
      }
    }

    it("shows html") {
      get("/html") {
        status should equal(200)
        body should equal("""<html><body>Hello, Martin</body></html>""")
        header("Content-Type") should equal("text/html;charset=utf-8")
      }
    }

  }

}
