package example

import org.scalatra.test.scalatest.ScalatraFlatSpec
import skinny.jackson.JSONStringOps
import skinny.micro.SkinnyMicroFilter
import skinny.micro.contrib.jackson.JSONSupport

import scala.util.Success

case class Person(firstName: String, lastName: Option[String])

class JSONOperationSpec extends ScalatraFlatSpec with JSONStringOps {

  val persons = Seq(
    Person("Alice", Some("Cooper")),
    Person("Bob", Some("Marley")),
    Person("Chris", None))

  object App extends SkinnyMicroFilter with JSONSupport {
    def name = params.getAs[String]("name").getOrElse("Anonymous")

    get("/hello") {
      responseAsJSON(Map("message" -> s"Hello, $name"))
    }
    get("/persons") {
      responseAsJSON(entity = persons, underscoreKeys = false)
    }
    get("/snake-cased-persons") {
      responseAsJSON(entity = persons, underscoreKeys = true)
    }
    get("/persons-prettify") {
      responseAsJSON(entity = persons, prettify = true, underscoreKeys = false)
    }
    get("/snake-cased-persons-prettify") {
      responseAsJSON(entity = persons, prettify = true, underscoreKeys = true)
    }
  }
  addFilter(App, "/*")

  object AngularApp extends SkinnyMicroFilter with JSONSupport {
    override protected def useJSONVulnerabilityProtection: Boolean = true
    override protected def useUnderscoreKeysForJSON: Boolean = false

    get("/persons.json") {
      responseAsJSON(persons)
    }
  }
  addFilter(AngularApp, "/*")

  it should "fetch hello message" in {
    get("/hello") {
      status should equal(200)
      header("Content-Type") should equal("application/json;charset=utf-8")
      body should equal("""{"message":"Hello, Anonymous"}""")
    }
  }

  it should "fetch hello message for Martin" in {
    get("/hello?name=Martin") {
      status should equal(200)
      header("Content-Type") should equal("application/json;charset=utf-8")
      body should equal("""{"message":"Hello, Martin"}""")
    }
  }

  it should "fetch persons" in {
    get("/persons") {
      status should equal(200)
      header("Content-Type") should equal("application/json;charset=utf-8")
      body should equal(
        """[{"firstName":"Alice","lastName":"Cooper"},{"firstName":"Bob","lastName":"Marley"},{"firstName":"Chris","lastName":null}]""")
    }
  }

  it should "fetch snake cased persons" in {
    get("/snake-cased-persons") {
      status should equal(200)
      header("Content-Type") should equal("application/json;charset=utf-8")
      body should equal(
        """[{"first_name":"Alice","last_name":"Cooper"},{"first_name":"Bob","last_name":"Marley"},{"first_name":"Chris","last_name":null}]""")
    }
  }

  it should "fetch prettified persons" in {
    get("/persons-prettify") {
      status should equal(200)
      header("Content-Type") should equal("application/json;charset=utf-8")
      body should equal(
        """[ {
          |  "firstName" : "Alice",
          |  "lastName" : "Cooper"
          |}, {
          |  "firstName" : "Bob",
          |  "lastName" : "Marley"
          |}, {
          |  "firstName" : "Chris",
          |  "lastName" : null
          |} ]""".stripMargin)
      val responsePersons = fromJSONString[Seq[Person]](body, false)
      responsePersons should equal(Success(persons))
    }
  }

  it should "fetch prettified snake cased persons" in {
    get("/snake-cased-persons-prettify") {
      status should equal(200)
      header("Content-Type") should equal("application/json;charset=utf-8")
      body should equal(
        """[ {
          |  "first_name" : "Alice",
          |  "last_name" : "Cooper"
          |}, {
          |  "first_name" : "Bob",
          |  "last_name" : "Marley"
          |}, {
          |  "first_name" : "Chris",
          |  "last_name" : null
          |} ]""".stripMargin)
      val responsePersons = fromJSONString[Seq[Person]](body, true)
      responsePersons should equal(Success(persons))
    }
  }

  it should "fetch persons for Angular apps" in {
    get("/persons.json") {
      status should equal(200)
      header("Content-Type") should equal("application/json;charset=utf-8")
      body should equal(
        """)]}',
          |[{"firstName":"Alice","lastName":"Cooper"},{"firstName":"Bob","lastName":"Marley"},{"firstName":"Chris","lastName":null}]""".stripMargin)
    }
  }

}
