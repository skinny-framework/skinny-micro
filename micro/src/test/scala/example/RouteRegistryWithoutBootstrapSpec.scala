package example

import org.scalatest._
import skinny.micro._
import skinny.micro.routing.RouteRegistry
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class RouteRegistryWithoutBootstrapSpec extends AnyFlatSpec with Matchers {

  RouteRegistry.init()

  private def sayHello(): String = ???

  val app1 = new WebApp {
    get("/echo")(sayHello)
    post("/echo")(sayHello)

    get("/routes") {
      routes.toString
    }
    get("/all-routes") {
      RouteRegistry.toString
    }
  }
  val app2 = new WebApp {
    get("/hello/:name")(sayHello)
    post("/good-bye/:name")(sayHello)
  }

  it should "show routes" in {
    app1.routes.toString should equal(
      """GET	/all-routes
        |GET	/echo
        |POST	/echo
        |GET	/routes
        |""".stripMargin)
  }

  it should "show all routes" in {
    RouteRegistry.toString should equal(
      """GET	/all-routes
          |GET	/echo
          |POST	/echo
          |POST	/good-bye/:name
          |GET	/hello/:name
          |GET	/routes
          |""".stripMargin)
  }

}
