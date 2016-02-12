package sample.async_native

import skinny.micro._
import skinny.micro.contrib.ScalateSupport
import skinny.micro.contrib.jackson.JSONSupport

import scala.concurrent.Future

class TypedEchoApp extends TypedAsyncWebApp with JSONSupport with ScalateSupport {

  get("/echo.json") { implicit ctx =>
    contentType = "application/json"
    Future {
      Ok(toJSONString(params))
    }
  }

  post("/hello/:name") { implicit ctx =>
    contentType = "text/plain"
    Future {
      (params.get("name"), params.get("with")) match {
        case (Some(name), Some(something)) => Ok(s"Hello, ${name} with ${something}")
        case (Some(name), None) => Ok(s"Hello, ${name}")
        case _ => NotFound
      }
    }
  }

  get("/html") { implicit ctx =>
    Future {
      contentType = "text/html"
      Ok(ssp("/html.ssp", "name" -> "Martin"))
    }
  }

}
