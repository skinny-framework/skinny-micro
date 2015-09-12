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
    Member(4, Some("Denis"))
  )

  describe("JSONStringOps") {
    it("should serialize normal data") {
      val group = SomeGroup("Igo", members)
      val expected = """<?xml version="1.0" encoding="UTF-8"?><response><name>Igo</name><groupMembers><id>1</id><name>Alice</name></groupMembers><groupMembers><id>2</id><name>Bob</name></groupMembers><groupMembers><id>3</id><name/></groupMembers><groupMembers><id>4</id><name>Denis</name></groupMembers></response>"""
      toXMLString(group) should equal(expected)
    }

    it("should work with non-existing fields") {
      val xml = """<?xml version="1.0" encoding="UTF-8"?><response><name>Igo</name></response>"""
      val expected = SomeGroup("Igo", Seq.empty)
      fromXMLString[SomeGroup](xml).isFailure should equal(true)
    }
  }

}
