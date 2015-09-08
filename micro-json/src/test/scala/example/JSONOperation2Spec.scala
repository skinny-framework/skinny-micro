package example

import org.scalatest._
import skinny.json.JSONStringOps

case class Samples(samples: Seq[Sample])
case class Sample(id: Long, firstName: String)
case class SamplePerson(name: Option[String] = None, parent: SamplePerson, children: Seq[SamplePerson] = Nil)

class JSONOperation2Spec extends FlatSpec with JSONStringOps with Matchers {

  def toJSONString1 = toJSONString(Sample(1, "Alice"))
  def toJSONString2 = toJSONString(List(Sample(1, "Alice"), Sample(2, "Bob")))
  def toJSONString3 = toPrettyJSONString(List(Sample(1, "Alice"), Sample(2, "Bob")))

  def toJSONString4 = toJSONString(Sample(1, "Alice"), false)
  def toJSONString5 = toJSONString(List(Sample(1, "Alice"), Sample(2, "Bob")), false)
  def toJSONString6 = toPrettyJSONString(List(Sample(1, "Alice"), Sample(2, "Bob")), false)

  val alice = SamplePerson(Some("Alice"), null)
  val bob = SamplePerson(Some("Bob"), alice, Nil)
  val chris = SamplePerson(Some("Chris"), alice, Seq(bob))
  val dennis = SamplePerson(Some("Dennis"), alice, Seq(bob, chris))

  def toJSONString7 = toJSONString(dennis)

  def fromJSON1: Option[Sample] = fromJSONString[Sample]("""{"id":1,"first_name":"Alice"}""").toOption
  def fromJSON2: Option[Samples] = fromJSONString[Samples]("""{"samples":[{"id":1,"first_name":"Alice"},{"id":2,"first_name":"Bob"}]}""").toOption

  def fromJSON3: Option[Sample] = fromJSONString[Sample]("""{"id":1,"firstName":"Alice"}""", false).toOption
  def fromJSON4: Option[Seq[Sample]] = fromJSONString[Seq[Sample]]("""[{"id":1,"firstName":"Alice"},{"id":2,"firstName":"Bob"}]""", false).toOption

  def fromJSON5: Option[SamplePerson] = fromJSONString[SamplePerson](
    """{"name":"Dennis","parent":{"name":"Alice","parent":null,"children":[]},"children":[{"name":"Bob","parent":{"name":"Alice","parent":null,"children":[]},"children":[]},{"name":"Chris","parent":{"name":"Alice","parent":null,"children":[]},"children":[{"name":"Bob","parent":{"name":"Alice","parent":null,"children":[]},"children":[]}]}]}"""
  ).toOption

  it should "have toJSONString 1" in {
    toJSONString1 should equal("""{"id":1,"first_name":"Alice"}""")
  }
  it should "have toJSONString 2" in {
    toJSONString2 should equal("""[{"id":1,"first_name":"Alice"},{"id":2,"first_name":"Bob"}]""")
  }
  it should "have toJSONString 3" in {
    toJSONString3 should equal(
      """[ {
        |  "id" : 1,
        |  "first_name" : "Alice"
        |}, {
        |  "id" : 2,
        |  "first_name" : "Bob"
        |} ]""".stripMargin)

  }
  it should "have toJSONString 4" in {
    toJSONString4 should equal("""{"id":1,"firstName":"Alice"}""")
  }
  it should "have toJSONString 5" in {
    toJSONString5 should equal("""[{"id":1,"firstName":"Alice"},{"id":2,"firstName":"Bob"}]""")
  }
  it should "have toJSONString 6" in {
    toJSONString6 should equal(
      """[ {
        |  "id" : 1,
        |  "firstName" : "Alice"
        |}, {
        |  "id" : 2,
        |  "firstName" : "Bob"
        |} ]""".stripMargin)
  }
  it should "have toJSONString 7" in {
    toJSONString7 should equal(
      """{"name":"Dennis","parent":{"name":"Alice","parent":null,"children":[]},"children":[{"name":"Bob","parent":{"name":"Alice","parent":null,"children":[]},"children":[]},{"name":"Chris","parent":{"name":"Alice","parent":null,"children":[]},"children":[{"name":"Bob","parent":{"name":"Alice","parent":null,"children":[]},"children":[]}]}]}""")
  }

  it should "have fromJSONString 1" in {
    fromJSON1.get should equal(Sample(1, "Alice"))
  }
  it should "have fromJSONString 2" in {
    fromJSON2.get should equal(Samples(Seq(Sample(1, "Alice"), Sample(2, "Bob"))))
  }
  it should "have fromJSONString 3" in {
    fromJSON3.get should equal(Sample(1, "Alice"))
  }
  it should "have fromJSONString 4" in {
    fromJSON4.get should equal(Seq(Sample(1, "Alice"), Sample(2, "Bob")))
  }
  it should "have fromJSONString 5" in {
    fromJSON5.get should equal(dennis)
  }

}
