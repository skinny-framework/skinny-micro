package sample.async_native

import skinny.micro._
import skinny.micro.contrib.json4s.JSONSupport

import scala.concurrent.Future

class Json4sEchoApp extends AsyncWebApp with JSONSupport {

  get("/json4s/echo.json") { implicit ctx =>
    contentType = "application/json"
    Future {
      toJSONString(params)
    }
  }

}
