package example

import org.scalatra.test.scalatest.ScalatraFlatSpec
import skinny.micro._
import skinny.micro.async.AsyncResult
import skinny.micro.contrib.jackson.XMLSupport

import scala.concurrent.Future

object Hello extends WebApp with XMLSupport {

  def message(implicit ctx: Context) = {
    s"Hello, ${params(ctx).getOrElse("name", "Anonymous")}"
  }

  // synchronous action
  get("/hello")(message)
  post("/hello")(message)

  // asynchronous action
  get("/hello/async") {
    implicit val ctx = context
    Future { message(ctx) }
  }

  // returns XML response
  get("/hello/xml") {
    responseAsXML(Map("message" -> message))
  }
  get("/hello/xml/async") {
    AsyncResult {
      responseAsXML(Map("message" -> s"Hello, ${params.getOrElse("name", "Anonymous")}"))
    }
  }

  get("/dynamic") {
    Future {
      request
    }
  }
}

class HelloSpec extends ScalatraFlatSpec {
  addFilter(Hello, "/*")

  it should "work fine with GET Requests" in {
    get("/hello") {
      status should equal(200)
      body should equal("Hello, Anonymous")
    }
    get("/hello?name=Martin") {
      status should equal(200)
      body should equal("Hello, Martin")
    }
  }

  it should "work fine with POST Requests" in {
    post("/hello", Map()) {
      status should equal(200)
      body should equal("Hello, Anonymous")
    }
    post("/hello", Map("name" -> "Martin")) {
      status should equal(200)
      body should equal("Hello, Martin")
    }
  }

  it should "work fine with AsyncResult" in {
    get("/hello/async") {
      status should equal(200)
      body should equal("Hello, Anonymous")
    }
    get("/hello/async?name=Martin") {
      status should equal(200)
      body should equal("Hello, Martin")
    }
  }

  it should "return XML response" in {
    get("/hello/xml") {
      status should equal(200)
      header("Content-Type") should equal("application/xml; charset=utf-8")
      body should equal(
        """<?xml version="1.0" encoding="UTF-8"?><response><message>Hello, Anonymous</message></response>""")
    }
    get("/hello/xml/async?name=Martin") {
      status should equal(200)
      header("Content-Type") should equal("application/xml; charset=utf-8")
      body should equal(
        """<?xml version="1.0" encoding="UTF-8"?><response><message>Hello, Martin</message></response>""")
    }
  }

  it should "detect dynamic value access when the first access" in {
    get("/dynamic") {
      status should equal(500)
    }
  }
}
