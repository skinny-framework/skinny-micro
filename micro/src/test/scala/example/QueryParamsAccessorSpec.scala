package example

import org.scalatra.test.scalatest.ScalatraFlatSpec
import skinny.micro.WebApp

class QueryParamsAccessorSpec extends ScalatraFlatSpec {
  behavior of "QueryParamsAccessor"

  class Controller extends WebApp {
    def single = queryParams.getAs[String]("foo").getOrElse("<empty>")
    def multi = queryMultiParams.getAs[String]("foo").map(_.mkString(",")).getOrElse("<empty>")
  }
  object ctrl extends Controller {
    get("/get")(single)
    post("/post")(single)
    get("/multi/get")(multi)
    post("/multi/post")(multi)
  }
  addFilter(ctrl, "/*")

  "queryMultiParams" should "be available" in {
    get("/multi/get?foo=bar&foo=baz") {
      status should equal(200)
      body should equal("bar,baz")
    }
    post("/multi/post", "foo" -> "bar", "foo" -> "baz") {
      status should equal(200)
      body should equal("<empty>")
    }
    post("/multi/post?foo=bar&foo=baz", "foo" -> "xxx", "foo" -> "yyy") {
      status should equal(200)
      body should equal("bar,baz")
    }
  }

  "queryParams" should "be available" in {
    get("/get", "foo" -> "bar") {
      status should equal(200)
      body should equal("bar")
    }
    post("/post", "foo" -> "bar") {
      status should equal(200)
      body should equal("<empty>")
    }
    post("/post?foo=bar", "foo" -> "baz") {
      status should equal(200)
      body should equal("bar")
    }
  }

}