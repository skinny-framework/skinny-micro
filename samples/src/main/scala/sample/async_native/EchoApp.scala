package sample.async_native

import skinny.micro._
import skinny.micro.contrib.{ ScalateSupport, JSONSupport }

import scala.concurrent.Future

class EchoApp extends AsyncWebApp with JSONSupport with ScalateSupport {

  before() { implicit ctx =>
    Future {
      contentType = "text/html"
    }
  }

  get("/echo.json") { implicit ctx =>
    contentType = "application/json"
    Future {
      toJSONString(params)
    }
  }

  post("/hello/:name") { implicit ctx =>
    Future {
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
  }

  get("/html") { implicit ctx =>
    Future {
      ssp("/html.ssp", "name" -> "Martin")
    }
  }

}
