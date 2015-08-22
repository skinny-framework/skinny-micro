package example.scalate

import org.specs2.mutable._
import skinny.micro.SkinnyMicroServlet
import skinny.micro.scalate.{ ScalateSupport, ScalateUrlGeneratorSupport }

import scala.language.reflectiveCalls

class ScalateUrlGeneratorSupportTest extends Specification {

  val servlet = new SkinnyMicroServlet with ScalateSupport with ScalateUrlGeneratorSupport {

    val cat: String = "meea"

    val simpleString = get("/foo") {}

    val singleNamed = get("/foo/:bar") {}
  }

  "Routes extracted from the servlet" should {
    "exist" in {
      servlet.reflectRoutes must haveValue(servlet.simpleString)
      servlet.reflectRoutes must haveValue(servlet.singleNamed)
    }
    "be indexed by their names" in {
      servlet.reflectRoutes must havePair("simpleString" -> servlet.simpleString)
      servlet.reflectRoutes must havePair("singleNamed" -> servlet.singleNamed)
    }
  }
}
