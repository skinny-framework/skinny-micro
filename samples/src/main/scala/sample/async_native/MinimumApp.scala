package sample.async_native

import skinny.micro.AsyncWebApp
import skinny.micro.response.Ok

class MinimumApp extends AsyncWebApp {

  before() { implicit ctx =>
    logger.info("before filter")
  }

  after() { implicit ctx =>
    logger.info("after filter")
  }

  get("/hello") { implicit ctx =>
    "Hello, World!"
  }

  post("/hello") { implicit ctx =>
    s"Hello, ${params.getOrElse("name", "Anonymous")}!"
  }

  get("/hello-with-cookie-1") { implicit ctx =>
    cookies += "theme" -> "light"
    "Hello, World!"
  }

  get("/hello-with-cookie-2") { implicit ctx =>
    Ok(
      body = "Hello, World!",
      headers = Map("Set-Cookie" -> "theme=light")
    )
  }

}
