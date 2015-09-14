package sample.scalatra_compatible

import skinny.micro._
import skinny.micro.cookie.Cookie

class TypedMinimumApp extends TypedWebApp {

  before() {
    logger.info("before filter")
  }

  after() {
    logger.info("after filter")
  }

  get("/hello") {
    Ok("Hello, World!")
  }

  post("/hello") {
    Ok(s"Hello, ${params.getOrElse("name", "Anonymous")}!")
  }

  get("/hello-with-cookie-1") {
    cookies += "theme" -> "light"
    Ok(
      body = "Hello, World!",
      cookies = Seq(Cookie("theme", "light"))
    )
  }

  get("/hello-with-cookie-2") {
    Ok(
      body = "Hello, World!",
      headers = Map("Set-Cookie" -> "theme=light")
    )
  }

}
