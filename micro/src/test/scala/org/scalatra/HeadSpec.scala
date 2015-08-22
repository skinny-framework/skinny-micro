package org.scalatra

import org.scalatra.test.specs2.ScalatraSpec
import skinny.micro.SkinnyMicroServlet

class HeadSpec extends ScalatraSpec {
  def is =
    s2"""
A HEAD request should"
  return no body $noBody
  preserve headers $preserveHeaders
"""
  val servletHolder = addServlet(classOf[HeadSpecServlet], "/*")

  def noBody = head("/") { response.body must_== "" }

  def preserveHeaders = head("/") {
    header("X-Powered-By") must_== "caffeine"
  }
}

class HeadSpecServlet extends SkinnyMicroServlet {
  get("/") {
    response.addHeader("X-Powered-By", "caffeine")
    "poof -- watch me disappear"
  }
}
