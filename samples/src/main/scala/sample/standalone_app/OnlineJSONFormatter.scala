package sample.standalone_app

import skinny.micro._
import skinny.micro.contrib.jackson.JSONSupport
import scala.util._

/**
 * Simple JSON formatter application.
 *
 * How to run:
 *
 * sbt samples/run
 */
object OnlineJSONFormatter extends App {

  WebServer.mount(
    new AsyncWebApp with JSONSupport {
      post("/prettify") { implicit ctx =>
        contentType = "application/json"
        fromJSONString[Map[String, Any]](request.body) match {
          case Success(value) => Ok(toPrettyJSONString(value))
          case _ => BadRequest(toJSONString(Map("error" -> "Failed to parse JSON string")))
        }
      }
    }).port(4567).start()

  println
  println("""curl -XPOST localhost:4567/prettify -H'Content-Type: application/json' -d'{"glossary":{"title":"example glossary","GlossDiv":{"title":"S","GlossList":{"GlossEntry":{"ID":"SGML","SortAs":"SGML","GlossTerm":"Standard Generalized Markup Language","Acronym":"SGML","Abbrev":"ISO 8879:1986","GlossDef":{"para":"A meta-markup language, used to create markup languages such as DocBook.","GlossSeeAlso":["GML","XML"]},"GlossSee":"markup"}}}}}'""")
  println

  /*
{
  "glossary" : {
    "title" : "example glossary",
    "GlossDiv" : {
      "title" : "S",
      "GlossList" : {
        "GlossEntry" : {
          "ID" : "SGML",
          "SortAs" : "SGML",
          "GlossTerm" : "Standard Generalized Markup Language",
          "Acronym" : "SGML",
          "Abbrev" : "ISO 8879:1986",
          "GlossDef" : {
            "para" : "A meta-markup language, used to create markup languages such as DocBook.",
            "GlossSeeAlso" : [ "GML", "XML" ]
          },
          "GlossSee" : "markup"
        }
      }
    }
  }
}
  */

}
