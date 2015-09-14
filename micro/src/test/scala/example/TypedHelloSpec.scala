package example

import org.scalatra.test.scalatest.ScalatraFlatSpec
import skinny.micro._

import scala.concurrent.Future

object TypedHello extends TypedWebApp {

  def message(implicit ctx: Context) = {
    s"Hello, ${params(ctx).getOrElse("name", "Anonymous")}"
  }

  // synchronous action
  get("/hello")(Ok(message))
  post("/hello")(Ok(message))

  get("/cookie") {
    Ok(body = "ok", cookies = Seq(Cookie("foo", "bar")))
  }

  // asynchronous action
  asyncGet("/hello/async") {
    implicit val ctx = context
    Future { Ok(message(ctx)) }
  }

  asyncGet("/dynamic") {
    Future {
      Ok(request)
    }
  }
}

class TypedHelloSpec extends ScalatraFlatSpec {
  addFilter(TypedHello, "/*")

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

  it should "detect dynamic value access when the first access" in {
    get("/dynamic") {
      status should equal(500)
    }
  }

  it should "return cookies in the ActionResult" in {
    get("/cookie") {
      header("Set-Cookie") should equal("foo=bar;Path=/")
      status should equal(200)
    }
  }
}
