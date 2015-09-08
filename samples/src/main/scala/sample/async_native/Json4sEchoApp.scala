package sample.async_native

import skinny.micro._
import skinny.micro.contrib._

import scala.concurrent.Future

class Json4sEchoApp extends AsyncWebApp with Json4sSupport {

  get("/json4s/echo.json") { implicit ctx =>
    contentType = "application/json"
    Future {
      toJSONString(params)
    }
  }

}
