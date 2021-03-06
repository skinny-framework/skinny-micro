package sample.async_native

import skinny.test.SkinnyFunSpec

class MinimumAppSpec extends SkinnyFunSpec {
  addFilter(classOf[MinimumApp], "/*")

  describe("Async native MinimumApp") {

    it("accepts GET requests") {
      get("/hello") {
        status should equal(200)
        body should equal("Hello, World!")
        header("Content-Type") should equal("text/plain;charset=utf-8")
      }
    }

    it("accepts POST requests") {
      post("/hello", "name" -> "Martin") {
        status should equal(200)
        body should equal("Hello, Martin!")
        header("Content-Type") should equal("text/plain;charset=utf-8")
      }
    }

    it("returns response headers") {
      get("/hello-with-cookie-1") {
        status should equal(200)
        body should equal("Hello, World!")
        header("Content-Type") should equal("text/plain;charset=utf-8")
        header("Set-Cookie") should equal("theme=light; Path=/")
      }

      get("/hello-with-cookie-2") {
        status should equal(200)
        body should equal("Hello, World!")
        header("Content-Type") should equal("text/plain;charset=utf-8")
        header("Set-Cookie") should equal("theme=light")
      }
    }

  }

}
