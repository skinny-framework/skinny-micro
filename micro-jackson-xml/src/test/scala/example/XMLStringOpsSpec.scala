package example

import org.scalatest._
import skinny.jackson.XMLStringOps

case class SomeGroup(name: String, groupMembers: Seq[Member])
case class Member(id: Long, name: Option[String])

class XMLStringOpsSpec extends FunSpec with Matchers with XMLStringOps {

  val members = Seq(
    Member(1, Some("Alice")),
    Member(2, Some("Bob")),
    Member(3, None),
    Member(4, Some("Denis")))

  describe("JSONStringOps") {
    it("should serialize normal data") {
      val group = SomeGroup("Igo", members)
      toXMLString(group) should (
        equal(
          """<?xml version="1.0" encoding="UTF-8"?>
            |<response>
            |  <name>Igo</name>
            |  <groupMembers>
            |    <id>1</id>
            |    <name>Alice</name>
            |  </groupMembers>
            |  <groupMembers>
            |    <id>2</id>
            |    <name>Bob</name>
            |  </groupMembers>
            |  <groupMembers>
            |    <id>3</id>
            |    <name/>
            |  </groupMembers>
            |  <groupMembers>
            |    <id>4</id>
            |    <name>Denis</name>
            |  </groupMembers>
            |</response>""".stripMargin.replace("\n", "").replaceAll("\\s{2}", ""))
          or
          equal(
            """<?xml version="1.0" encoding="UTF-8"?>
              |<response>
              |  <name>Igo</name>
              |  <groupMembers>
              |    <name>Alice</name>
              |    <id>1</id>
              |  </groupMembers>
              |  <groupMembers>
              |    <name>Bob</name>
              |    <id>2</id>
              |  </groupMembers>
              |  <groupMembers>
              |    <name/>
              |    <id>3</id>
              |  </groupMembers>
              |  <groupMembers>
              |    <name>Denis</name>
              |    <id>4</id>
              |  </groupMembers>
              |</response>""".stripMargin.replace("\n", "").replaceAll("\\s{2}", "")))
    }

    it("should work with non-existing fields") {
      val xml =
        """<?xml version="1.0" encoding="UTF-8"?>
          |<response>
          |  <name>Igo</name>
          |</response>""".stripMargin.replace("\n", "").replaceAll("\\s{2}", "")
      fromXMLString[SomeGroup](xml).isFailure should equal(true)
    }
  }

}
