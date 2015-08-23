package sample.async_native

import skinny.micro.AsyncWebApp
import skinny.micro.response.Ok

import scala.concurrent.Future

class MinimumApp extends AsyncWebApp {

  before() { implicit ctx =>
    Future {
      logger.info("before filter")
    }
  }

  after() { implicit ctx =>
    Future {
      logger.info("after filter")
    }
  }

  get("/hello") { implicit ctx =>
    Future {
      "Hello, World!"
    }
  }

  post("/hello") { implicit ctx =>
    Future {
      s"Hello, ${params.getOrElse("name", "Anonymous")}!"
    }
  }

  get("/hello-with-cookie-1") { implicit ctx =>
    Future {
      cookies += "theme" -> "light"
      "Hello, World!"
    }
  }

  get("/hello-with-cookie-2") { implicit ctx =>
    Future {
      Ok(
        body = "Hello, World!",
        headers = Map("Set-Cookie" -> "theme=light")
      )
    }
  }

}
