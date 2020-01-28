package example

import org.scalatest._
import skinny.jackson.JSONStringOps
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

case class SomeGroup(name: String, groupMembers: Seq[Member])
case class Member(id: Long, name: Option[String])

class JSONStringOpsSpec extends AnyFunSpec with Matchers with JSONStringOps {

  val members = Seq(
    Member(1, Some("Alice")),
    Member(2, Some("Bob")),
    Member(3, None),
    Member(4, Some("Denis")))

  describe("JSONStringOps") {
    it("should serialize normal data") {
      val group = SomeGroup("Igo", members)
      val expected = """{"name":"Igo","group_members":[{"id":1,"name":"Alice"},{"id":2,"name":"Bob"},{"id":3,"name":null},{"id":4,"name":"Denis"}]}"""
      toJSONString(group) should equal(expected)
    }

    it("should work with non-existing fields") {
      val json = """{"name":"Igo"}"""
      val expected = SomeGroup("Igo", Seq.empty)
      fromJSONString[SomeGroup](json).isFailure should equal(true)
    }
  }

}
