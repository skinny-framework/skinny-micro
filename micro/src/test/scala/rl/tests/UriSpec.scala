package rl.tests

import org.specs2.Specification
import skinny.micro.rl.Uri

class UriSpec extends Specification {
  def is =
    "A Uri should" ^
      "parse an absolute uri with authority" ! parseAbsoluteAuthority ^
      "parse a relative uri with authority" ! parseRelativeAuthority ^
      "parse a uri without an authority" ! parseWithoutAuthority ^
      end

  def parseAbsoluteAuthority = {
    val u = Uri("http://localhost/blah")

    u.scheme() must_== "http" and
      (u.host must beSome("localhost")) and
      (u.path must_== "/blah/")
  }

  def parseRelativeAuthority = {
    val u = Uri("//localhost/blah")

    u.scheme() must_== "" and
      (u.host must beSome("localhost")) and
      (u.path must_== "/blah/")
  }

  def parseWithoutAuthority = {
    val u = Uri("/blah")

    u.scheme() must_== "" and (u.host must beNone) and (u.path must_== "/blah/")
  }

}