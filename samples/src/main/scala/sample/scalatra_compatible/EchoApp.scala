package sample.scalatra_compatible

import skinny.micro._
import skinny.micro.response._
import skinny.json.DefaultJSONStringOps
import skinny.micro.scalate.ScalateSupport

class EchoApp extends WebApp with DefaultJSONStringOps with ScalateSupport {

  before() {
    contentType = "text/html"
  }

  get("/echo.json") {
    contentType = "application/json"
    toJSONString(params)
  }

  post("/hello/:name") {
    contentType = "text/plain"
    (for {
      name <- params.get("name")
    } yield {
      Ok {
        params.get("with")
          .map(something => s"Hello, ${name} with ${something}")
          .getOrElse(s"Hello, ${name}")
      }
    }).getOrElse(NotFound)
  }

  get("/html") {
    ssp("/html.ssp", "name" -> "Martin")
  }

}