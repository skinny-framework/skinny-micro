package org.scalatra

import org.scalatest._
import skinny.micro.SkinnyMicroServletBase

class ScalatraServletRequestPathSpec extends WordSpec with MustMatchers {

  "a ScalatraServlet requestPath" should {

    "be extracted properly from encoded url" in {
      SkinnyMicroServletBase.requestPath("/%D1%82%D0%B5%D1%81%D1%82/", 5) must equal("/")
      SkinnyMicroServletBase.requestPath("/%D1%82%D0%B5%D1%81%D1%82/%D1%82%D0%B5%D1%81%D1%82/", 5) must equal("/тест/")
    }

    "be extracted properly from decoded url" in {
      SkinnyMicroServletBase.requestPath("/тест/", 5) must equal("/")
      SkinnyMicroServletBase.requestPath("/тест/тест/", 5) must equal("/тест/")
    }
  }
}
