package sample.scalatra_compatible

import skinny.micro._
import skinny.micro.contrib.json4s.JSONSupport

class Json4sEchoApp extends WebApp with JSONSupport {

  get("/json4s/echo.json") {
    contentType = "application/json"
    toJSONString(params)
  }

}
