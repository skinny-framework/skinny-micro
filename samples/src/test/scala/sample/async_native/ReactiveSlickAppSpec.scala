package sample.async_native

import skinny.test.SkinnyFunSpec
import skinny.json.DefaultJSONStringOps

class ReactiveSlickAppSpec extends SkinnyFunSpec with DefaultJSONStringOps {
  addFilter(classOf[ReactiveSlickApp], "/*")

  describe("Reactive Slick Demo") {

    it("shows coffees") {
      get("/slick-demo/coffees") {
        status should equal(200)
        header("Content-Type") should equal("application/json; charset=UTF-8")
      }
    }

    it("shows suppliers") {
      get("/slick-demo/suppliers") {
        status should equal(200)
        header("Content-Type") should equal("application/json; charset=UTF-8")
      }
    }
  }

}
