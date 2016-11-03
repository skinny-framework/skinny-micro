package example

import org.scalatra.test.scalatest.ScalatraFlatSpec
import skinny.micro._
import skinny.micro.contrib.jackson.XMLSupport

import scala.concurrent.Future

object HelloServlet extends SingleApp with XMLSupport {

  def message(implicit ctx: Context) = {
    s"Hello, ${params(ctx).getOrElse("name", "Anonymous")}"
  }

  // returns XML response
  get("/hello/xml") {
    responseAsXML(Map("message" -> message))
  }
  get("/hello/xml/async") {
    implicit val ctx = context
    Future {
      responseAsXML(Map("message" -> s"Hello, ${params(ctx).getOrElse("name", "Anonymous")}"))(ctx)
    }
  }
}

class HelloServletSpec extends ScalatraFlatSpec {
  addServlet(HelloServlet, "/*")

  it should "return XML response" in {
    get("/hello/xml") {
      status should equal(200)
      header("Content-Type") should equal("application/xml;charset=utf-8")
      body should equal(
        """<?xml version="1.0" encoding="UTF-8"?><response><message>Hello, Anonymous</message></response>"""
      )
    }
    get("/hello/xml/async?name=Martin") {
      status should equal(200)
      header("Content-Type") should equal("application/xml;charset=utf-8")
      body should equal(
        """<?xml version="1.0" encoding="UTF-8"?><response><message>Hello, Martin</message></response>"""
      )
    }
  }
}
