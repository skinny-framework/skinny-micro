package rl.tests

import org.specs2.Specification
import skinny.micro.rl.DomainParser

class DomainParserSpec extends Specification {
  def is =
    "When a domain parser" ^
      "reads the dat file, it should" ^
      "create the first level of the tree" ! { DomainParser.publicSuffixes.contains("com") must beTrue } ^
      "create the first level of the tree even when the first doesn't appear on a line on its own" ! {
        DomainParser.publicSuffixes.contains("uk") must beTrue
      } ^
      "create the lower levels of the tree" ! {
        (DomainParser.publicSuffixes("jp") contains "ac" must beTrue) and
          (DomainParser.publicSuffixes("jp")("kawasaki") contains "*" must beTrue)
      } ^ p ^
      "parses a host string, the result" ^
      "includes the tld" ! { DomainParser("backchat.io") must_== ("io", "backchat", "") } ^
      "includes the domain" ! { DomainParser("backchat.io") must_== ("io", "backchat", "") } ^
      "includes the subdomain" ! { DomainParser("builds.mojolly.com") must_== ("com", "mojolly", "builds") } ^ end

}