package sample.scalatra_compatible

import skinny.micro._
import skinny.micro.contrib.Json4sSupport

class Json4sEchoApp extends WebApp with Json4sSupport {

  get("/json4s/echo.json") {
    contentType = "application/json"
    toJSONString(params)
  }

}
