package sample.scalatra_compatible

import skinny.micro._
import skinny.micro.contrib.ScalateSupport
import skinny.micro.contrib.jackson.JSONSupport

class EchoApp extends WebApp with JSONSupport with ScalateSupport {

  before() {
    contentType = "text/html"
  }

  get("/echo.json") {
    contentType = "application/json"
    toJSONString(params)
  }

  post("/hello/:name") {
    contentType = "text/plain"
    (params.get("name"), params.get("with")) match {
      case (Some(name), Some(something)) => Ok(s"Hello, ${name} with ${something}")
      case (Some(name), None) => Ok(s"Hello, ${name}")
      case _ => NotFound
    }
  }

  get("/html") {
    ssp("/html.ssp", "name" -> "Martin")
  }

}
