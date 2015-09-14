package sample.scalatra_compatible

import skinny.micro._

class TypedMinimumApp extends WebApp {

  before() {
    logger.info("before filter")
  }

  after() {
    logger.info("after filter")
  }

  get("/hello") {
    "Hello, World!"
  }

  post("/hello") {
    s"Hello, ${params.getOrElse("name", "Anonymous")}!"
  }

  get("/hello-with-cookie-1") {
    cookies += "theme" -> "light"
    "Hello, World!"
  }

  get("/hello-with-cookie-2") {
    Ok(
      body = "Hello, World!",
      headers = Map("Set-Cookie" -> "theme=light")
    )
  }

}
