package example

import org.scalatra.test.scalatest.ScalatraFlatSpec
import skinny.micro.SkinnyMicroFilter
import skinny.micro.contrib.jackson.XMLSupport

class XMLOperationSpec extends ScalatraFlatSpec {

  case class Group(persons: Seq[Person])
  case class Person(firstName: String, lastName: Option[String])

  val persons = Seq(
    Person("Alice", Some("Cooper")),
    Person("Bob", Some("Marley")),
    Person("Chris", None))

  object App extends SkinnyMicroFilter with XMLSupport {
    def name = params.getAs[String]("name").getOrElse("Anonymous")

    get("/hello") {
      responseAsXML(Map("message" -> s"Hello, $name"))
    }
    get("/persons") {
      responseAsXML(entity = persons, underscoreKeys = false)
    }
    get("/group") {
      responseAsXML(entity = Group(persons), underscoreKeys = false)
    }
    get("/snake-cased-persons") {
      responseAsXML(entity = persons, underscoreKeys = true)
    }
    get("/persons-prettify") {
      responseAsXML(entity = persons, prettify = true, underscoreKeys = false)
    }
    get("/snake-cased-persons-prettify") {
      responseAsXML(entity = persons, prettify = true, underscoreKeys = true)
    }
  }
  addFilter(App, "/*")

  it should "fetch hello message" in {
    get("/hello") {
      status should equal(200)
      header("Content-Type") should equal("application/xml;charset=utf-8")
      body should equal(
        """<?xml version="1.0" encoding="UTF-8"?>
          |<response>
          |  <message>Hello, Anonymous</message>
          |</response>""".stripMargin.replace("\n", "").replaceAll("\\s{2}", ""))
    }
  }

  it should "fetch hello message for Martin" in {
    get("/hello?name=Martin") {
      status should equal(200)
      header("Content-Type") should equal("application/xml;charset=utf-8")
      body should equal(
        """<?xml version="1.0" encoding="UTF-8"?>
          |<response>
          |  <message>Hello, Martin</message>
          |</response>""".stripMargin.replace("\n", "").replaceAll("\\s{2}", ""))
    }
  }

  it should "fetch hello message for tag value" in {
    get("/hello?name=%3CMap1%3Efoo%3C/Map1%3E") {
      status should equal(200)
      header("Content-Type") should equal("application/xml;charset=utf-8")
      body should equal(
        """<?xml version="1.0" encoding="UTF-8"?>
          |<response>
          |  <message>Hello, &lt;Map1>foo&lt;/Map1></message>
          |</response>""".stripMargin.replace("\n", "").replaceAll("\\s{2}", ""))
    }
  }

  it should "fetch persons" in {
    get("/persons") {
      status should equal(200)
      header("Content-Type") should equal("application/xml;charset=utf-8")
      body should (
        equal(
          """<?xml version="1.0" encoding="UTF-8"?>
            |<response>
            |<item>
            |  <firstName>Alice</firstName>
            |  <lastName>Cooper</lastName>
            |</item>
            |<item>
            |  <firstName>Bob</firstName>
            |  <lastName>Marley</lastName>
            |</item>
            |<item>
            |  <firstName>Chris</firstName>
            |  <lastName/>
            |</item>
            |</response>""".stripMargin.replace("\n", "").replaceAll("\\s{2}", ""))
          or
          equal(
            """<?xml version="1.0" encoding="UTF-8"?>
            |<response>
            |<item>
            |  <lastName>Cooper</lastName>
            |  <firstName>Alice</firstName>
            |</item>
            |<item>
            |  <lastName>Marley</lastName>
            |  <firstName>Bob</firstName>
            |</item>
            |<item>
            |  <lastName/>
            |  <firstName>Chris</firstName>
            |</item>
            |</response>""".stripMargin.replace("\n", "").replaceAll("\\s{2}", "")))
    }
  }

  it should "fetch a group with persons" in {
    get("/group") {
      status should equal(200)
      header("Content-Type") should equal("application/xml;charset=utf-8")
      body should (
        equal(
          """<?xml version="1.0" encoding="UTF-8"?>
            |<response>
            |<persons>
            |  <firstName>Alice</firstName>
            |  <lastName>Cooper</lastName>
            |</persons>
            |<persons>
            |  <firstName>Bob</firstName>
            |  <lastName>Marley</lastName>
            |</persons>
            |<persons>
            |  <firstName>Chris</firstName>
            |  <lastName/>
            |</persons>
            |</response>""".stripMargin.replace("\n", "").replaceAll("\\s{2}", ""))
          or
          equal(
            """<?xml version="1.0" encoding="UTF-8"?>
            |<response>
            |<persons>
            |  <lastName>Cooper</lastName>
            |  <firstName>Alice</firstName>
            |</persons>
            |<persons>
            |  <lastName>Marley</lastName>
            |  <firstName>Bob</firstName>
            |</persons>
            |<persons>
            |  <lastName/>
            |  <firstName>Chris</firstName>
            |</persons>
            |</response>""".stripMargin.replace("\n", "").replaceAll("\\s{2}", "")))
    }
  }

  it should "fetch snake cased persons" in {
    get("/snake-cased-persons") {
      status should equal(200)
      header("Content-Type") should equal("application/xml;charset=utf-8")
      body should (
        equal(
          """<?xml version="1.0" encoding="UTF-8"?>
            |<response>
            |<item>
            |  <first_name>Alice</first_name>
            |  <last_name>Cooper</last_name>
            |</item>
            |<item>
            |  <first_name>Bob</first_name>
            |  <last_name>Marley</last_name>
            |</item>
            |<item>
            |  <first_name>Chris</first_name>
            |  <last_name/>
            |</item>
            |</response>""".stripMargin.replace("\n", "").replaceAll("\\s{2}", ""))
          or
          equal(
            """<?xml version="1.0" encoding="UTF-8"?>
            |<response>
            |<item>
            |  <last_name>Cooper</last_name>
            |  <first_name>Alice</first_name>
            |</item>
            |<item>
            |  <last_name>Marley</last_name>
            |  <first_name>Bob</first_name>
            |</item>
            |<item>
            |  <last_name/>
            |  <first_name>Chris</first_name>
            |</item>
            |</response>""".stripMargin.replace("\n", "").replaceAll("\\s{2}", "")))
    }
  }

  it should "fetch prettified persons" in {
    get("/persons-prettify") {
      status should equal(200)
      header("Content-Type") should equal("application/xml;charset=utf-8")
      body should (
        equal(
          """<?xml version="1.0" encoding="UTF-8"?><response>
            |  <item>
            |    <firstName>Alice</firstName>
            |    <lastName>Cooper</lastName>
            |  </item>
            |  <item>
            |    <firstName>Bob</firstName>
            |    <lastName>Marley</lastName>
            |  </item>
            |  <item>
            |    <firstName>Chris</firstName>
            |    <lastName/>
            |  </item>
            |
            |</response>""".stripMargin)
          or
          equal(
            """<?xml version="1.0" encoding="UTF-8"?><response>
              |  <item>
              |    <lastName>Cooper</lastName>
              |    <firstName>Alice</firstName>
              |  </item>
              |  <item>
              |    <lastName>Marley</lastName>
              |    <firstName>Bob</firstName>
              |  </item>
              |  <item>
              |    <lastName/>
              |    <firstName>Chris</firstName>
              |  </item>
              |
              |</response>""".stripMargin))
    }
  }

  it should "fetch prettified snake cased persons" in {
    get("/snake-cased-persons-prettify") {
      status should equal(200)
      header("Content-Type") should equal("application/xml;charset=utf-8")
      body should (
        equal(
          """<?xml version="1.0" encoding="UTF-8"?><response>
            |  <item>
            |    <first_name>Alice</first_name>
            |    <last_name>Cooper</last_name>
            |  </item>
            |  <item>
            |    <first_name>Bob</first_name>
            |    <last_name>Marley</last_name>
            |  </item>
            |  <item>
            |    <first_name>Chris</first_name>
            |    <last_name/>
            |  </item>
            |
            |</response>""".stripMargin)
          or
          equal(
            """<?xml version="1.0" encoding="UTF-8"?><response>
              |  <item>
              |    <last_name>Cooper</last_name>
              |    <first_name>Alice</first_name>
              |  </item>
              |  <item>
              |    <last_name>Marley</last_name>
              |    <first_name>Bob</first_name>
              |  </item>
              |  <item>
              |    <last_name/>
              |    <first_name>Chris</first_name>
              |  </item>
              |
              |</response>""".stripMargin))
    }
  }

}
