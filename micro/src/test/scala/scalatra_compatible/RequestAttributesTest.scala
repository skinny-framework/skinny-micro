package scalatra_compatible

import org.scalatra.test.scalatest.ScalatraFunSuite

class RequestAttributesTest extends ScalatraFunSuite with AttributesTest {
  addServlet(new AttributesServlet {
    def attributesMap = request
  }, "/*")
}

