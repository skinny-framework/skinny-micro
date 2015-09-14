package sample.async_native

import skinny.micro._
import skinny.micro.contrib.ScalateSupport
import skinny.micro.contrib.jackson.JSONSupport

import scala.concurrent.Future

class TypedEchoApp extends AsyncWebApp with JSONSupport with ScalateSupport {

  get("/echo.json") { implicit ctx =>
    contentType = "application/json"
    Future {
      toJSONString(params)
    }
  }

  post("/hello/:name") { implicit ctx =>
    contentType = "text/plain"
    Future {
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
  }

  get("/html") { implicit ctx =>
    Future {
      contentType = "text/html"
      ssp("/html.ssp", "name" -> "Martin")
    }
  }

}
