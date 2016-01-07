package example

import org.scalatra.test.scalatest.ScalatraFlatSpec
import skinny.micro._
import skinny.micro.routing.RouteRegistry

class RouteRegistrySpec extends ScalatraFlatSpec {

  RouteRegistry.init()

  addFilter(new WebApp {
    private def sayHelloTo(name: Option[String]): String = name match {
      case Some(n) => s"Hello, $n!"
      case _ => ""
    }
    private def sayHello(): String = sayHelloTo(params.get("name"))

    get("/echo")(sayHello)
    post("/echo")(sayHello)

    get("/routes") {
      routes.toString
    }
    get("/all-routes") {
      RouteRegistry.toString
    }
  }, "/*")

  addFilter(new WebApp {
    private def sayHelloTo(name: Option[String]): String = name match {
      case Some(n) => s"Hello, $n!"
      case _ => ""
    }
    private def sayHello(): String = sayHelloTo(params.get("name"))

    get("/hello/:name")(sayHello)
    post("/good-bye/:name")(sayHello)
  }, "/foo/*") // context path won't be appended to the routes

  it should "echo via GET requests" in {
    get("echo", "name" -> "Martin") {
      status should equal(200)
    }
  }

  it should "echo via POST requests" in {
    post("/echo", "name" -> "Martin") {
      status should equal(200)
      body should equal("Hello, Martin!")
    }

  }

  it should "show routes" in {
    get("/routes") {
      status should equal(200)
      body should equal(
        """GET	/all-routes
          |GET	/echo
          |GET	/routes
          |POST	/echo
          |""".stripMargin)
    }
  }

  it should "show all routes" in {
    get("/all-routes") {
      status should equal(200)
      body should equal(
        """GET	/all-routes
          |GET	/echo
          |GET	/hello/:name
          |GET	/routes
          |POST	/echo
          |POST	/good-bye/:name
          |""".stripMargin)
    }
  }

}
