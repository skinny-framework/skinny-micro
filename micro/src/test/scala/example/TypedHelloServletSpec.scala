package example

import org.scalatra.test.scalatest.ScalatraFlatSpec
import skinny.micro._

import scala.concurrent.Future

object TypedHelloServlet extends TypedSingleApp {

  def message(implicit ctx: Context) = {
    s"Hello, ${params(ctx).getOrElse("name", "Anonymous")}"
  }

  // synchronous action
  get("/hello")(Ok(message))
  post("/hello")(Ok(message))

  // asynchronous action
  asyncGet("/hello/async") {
    implicit val ctx = context
    Future { Ok(message(ctx)) }
  }
}

class TypedHelloServletSpec extends ScalatraFlatSpec {
  addServlet(TypedHelloServlet, "/*")

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
}
