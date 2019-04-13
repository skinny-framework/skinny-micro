package scalatra_compatible

import org.scalatra.test.specs2.MutableScalatraSpec
import skinny.micro.{ SkinnyMicroServlet, RouteTransformer }
import skinny.micro.routing.Route

class RouteMetadataSpec extends MutableScalatraSpec {
  addServlet(RouteMetadataSpec.servlet, "/*")

  "A route without metadata transformers" should {
    "not have any metadata" in {
      get("/zero/size") { body must_== "0" }
    }
  }

  "A route with a metadata transformer" should {
    "record the metadata" in {
      get("/one/foo") { body must_== "bar" }
    }
  }

  "A route with two metadata transformers" should {
    "apply left to right" in {
      get("/two/foo") { body must_== "baz" }
    }
  }
}

object RouteMetadataSpec {
  def meta(key: Symbol, value: String): RouteTransformer = { (route: Route) =>
    route.copy(metadata = route.metadata + (key -> value))
  }

  def servlet = new SkinnyMicroServlet {
    val zero: Route = get("/zero/:key") {
      // TODO: Since Scala 2.13.0-RC1 need to use view.filterKeys
      zero.metadata.filterKeys(_ != skinny.micro.Handler.RouteMetadataHttpMethodCacheKey).size.toString
    }

    val one: Route = get("/one/:key", meta(Symbol("foo"), "bar")) {
      renderMeta(one, Symbol(params("key")))
    }

    val two: Route = get("/two/:key", meta(Symbol("foo"), "bar"), meta(Symbol("foo"), "baz")) {
      renderMeta(two, Symbol(params("key")))
    }

    def renderMeta(route: Route, key: Symbol) =
      route.metadata.getOrElse(key, "None")
  }
}
