package scalatra_compatible

import javax.servlet.http.{ Cookie => ServletCookie }

import org.scalatra.test.scalatest.ScalatraFunSuite
import skinny.micro.SkinnyMicroServlet

class RequestCookiesTest extends ScalatraFunSuite {
  addServlet(new SkinnyMicroServlet {
    get("/multi-cookies") {
      Seq("one", "two", "three") map { key =>
        response.setHeader(key, request.multiCookies(key).mkString(":"))
      }
    }

    get("/cookies") {
      Seq("one", "two", "three") map { key =>
        response.setHeader(key, request.cookies.getOrElse(key, "NONE"))
      }
    }
  }, "/*")

  test("multiCookies is a multi-map of names to values") {
    get("/multi-cookies", headers = Map("Cookie" -> "one=uno; one=eins; two=zwei")) {
      header("one") should be("uno:eins")
      header("two") should be("zwei")
      header("three") should be("")
    }
  }

  test("cookies is a map of names to values") {
    get("/cookies", headers = Map("Cookie" -> "one=uno; one=eins; two=zwei")) {
      header("one") should be("uno")
      header("two") should be("zwei")
      header("three") should be("NONE")
    }
  }
}
