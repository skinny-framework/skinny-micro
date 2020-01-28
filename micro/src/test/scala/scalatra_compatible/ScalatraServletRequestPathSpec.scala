package scalatra_compatible

import org.scalatest._
import skinny.micro.SkinnyMicroServletBase
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ScalatraServletRequestPathSpec extends AnyWordSpec with Matchers {

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
