package rl
package tests

import org.specs2.Specification
import skinny.micro.rl._
import Uri._
import java.net.{ URI, URISyntaxException, IDN }

class NotImplementedException(msg: String) extends RuntimeException(msg)
object TestParser {
  def parseFragment(possible: String): Uri = {
    return notImplemented
  }

  def parseQuery(possible: String): Uri = {
    return notImplemented
  }

  def parseIPv4(possible: String): Uri = {
    return notImplemented
  }

  def parseIPv6(possible: String): Uri = {
    return notImplemented
  }

  def parseIPvFuture(possible: String): Uri = {
    return notImplemented
  }

  def parseIPLiteral(possible: String): Uri = {
    return notImplemented
  }

  def parsePath(possible: String): Uri = {
    return notImplemented
  }

  def parseAuthority(possible: String): Uri = {
    return notImplemented
  }

  def apply(toParse: String, originalUri: String): Uri = {
    return notImplemented
  }

  private def notImplemented: Uri = {
    return new FailedUri(new NotImplementedException("This implementation is not complete"), "http://kang.jazz.net")
  }
}

class AboutAbsoluteUris extends Specification {
  def is =
    "when parsing a full uri, a UriParser" ^
      "returns a failure when hostname contains a space" ! {
        val res = Uri("http://www.exa mple.org")
        res must beAnInstanceOf[FailedUri]
        res.originalUri must_== "http://www.exa mple.org"
      } ^
      "returns the port" ! {
        Uri("http://www.example.org:8080") must_== AbsoluteUri(
          Scheme("http"),
          Some(Authority(None, HostName("www.example.org"), Some(8080))),
          EmptyPath,
          EmptyQueryString,
          EmptyFragment,
          "http://www.example.org:8080")
      } ^
      "returns the hostname" ! {
        Uri("http://www.example.org/") must_== AbsoluteUri(
          Scheme("http"),
          Some(Authority(None, HostName("www.example.org"), None)),
          EmptyPath,
          EmptyQueryString,
          EmptyFragment,
          "http://www.example.org/")
      } ^
      "can handle internationalized domain names" ! {
        Uri("http://www.詹姆斯.org/") must_== AbsoluteUri(
          Scheme("http"),
          Some(Authority(None, HostName("www.xn--8ws00zhy3a.org"), None)),
          EmptyPath,
          EmptyQueryString,
          EmptyFragment,
          "http://www.詹姆斯.org/")
      } ^
      "returns the path split into fragments" ! {
        Uri("http://www.example.org/hello/world.txt") must_== AbsoluteUri(
          Scheme("http"),
          Some(Authority(None, HostName("www.example.org"), None)),
          AbsolutePath("hello" :: "world.txt" :: Nil),
          EmptyQueryString,
          EmptyFragment,
          "http://www.example.org/hello/world.txt")
      } ^
      "returns query string" ! {
        Uri("http://www.example.org/hello/world.txt/?id=5&part=three") must_== AbsoluteUri(
          Scheme("http"),
          Some(Authority(None, HostName("www.example.org"), None)),
          AbsolutePath("hello" :: "world.txt" :: Nil),
          MapQueryString("id=5&part=three"),
          EmptyFragment,
          "http://www.example.org/hello/world.txt/?id=5&part=three")
      } ^
      "returns query string and fragment" ! {
        Uri("http://www.example.org/hello/world.txt/?id=5&part=three#there-you-go") must_== AbsoluteUri(
          Scheme("http"),
          Some(Authority(None, HostName("www.example.org"), None)),
          AbsolutePath("hello" :: "world.txt" :: Nil),
          MapQueryString("id=5&part=three"),
          StringFragment("there-you-go"),
          "http://www.example.org/hello/world.txt/?id=5&part=three#there-you-go")
      } ^
      "returns fragment when URI has no query string" ! {
        Uri("http://www.example.org/hello/world.txt/#here-we-are") must_== AbsoluteUri(
          Scheme("http"),
          Some(Authority(None, HostName("www.example.org"), None)),
          AbsolutePath("hello" :: "world.txt" :: Nil),
          EmptyQueryString,
          StringFragment("here-we-are"),
          "http://www.example.org/hello/world.txt/#here-we-are")
      } ^
      end
}

class UriParserSpec extends Specification {
  def is =
    "A UriParser should" ^
      "when parsing a fragment" ^
      "get the fragment value" ! { TestParser.parseFragment("#i-m-a-fragment") must_== StringFragment("i-m-a-fragment") }.pendingUntilFixed ^
      "get none when empty fragment" ! { TestParser.parseFragment("#") must_== EmptyFragment }.pendingUntilFixed ^
      "get none when no fragment found" ! { TestParser.parseFragment("") must_== EmptyFragment }.pendingUntilFixed ^ p ^
      "when parsing a query string" ^
      "get the query string value" ! { TestParser.parseQuery("?id=6") must_== MapQueryString("id=6") }.pendingUntilFixed ^
      "get none when empty query string" ! { TestParser.parseQuery("?") must_== EmptyQueryString }.pendingUntilFixed ^
      "get none when no querystring found" ! { TestParser.parseQuery("") must_== EmptyQueryString }.pendingUntilFixed ^ p ^
      "when parsing ip addresses" ^
      "parse an ipv4 address" ! { TestParser.parseIPv4("123.23.34.56") must_== IPv4Address("123.23.34.56") }.pendingUntilFixed ^
      "parse an ipv6 address" ! {
        TestParser.parseIPv6("2001:0000:1234:0000:0000:C1C0:ABCD:0876") must_== IPv6Address("2001:0000:1234:0000:0000:C1C0:ABCD:0876")
      }.pendingUntilFixed ^
      "parse an ipvFuture address" ! {
        TestParser.parseIPvFuture("v2A.dd") must_== IPvFutureAddress("v2A.dd")
      }.pendingUntilFixed ^
      "parse an ip Future literal" ! {
        TestParser.parseIPLiteral("[v2A.dd]") must_== IPvFutureAddress("v2A.dd")
      }.pendingUntilFixed ^
      "parse an ip v6 literal" ! {
        TestParser.parseIPLiteral("[2001:0000:1234:0000:0000:C1C0:ABCD:0876]") must_== IPv6Address("2001:0000:1234:0000:0000:C1C0:ABCD:0876")
      }.pendingUntilFixed ^ p ^
      "when parsing paths" ^
      "parse a relative path" ! {
        val seg = ".." :: ".." :: "hello" :: "world.txt" :: Nil
        TestParser.parsePath("../../hello/world.txt") must_== RelativePath(seg)
      }.pendingUntilFixed ^
      "parse an absolute path" ! {
        val seg = "hello" :: "world.txt" :: Nil
        TestParser.parsePath("/hello/world.txt") must_== AbsolutePath(seg)
      }.pendingUntilFixed ^ p ^
      "when parsing the authority" ^
      "parse www.example.org" ! {
        TestParser.parseAuthority("www.example.org") must_== Authority(None, HostName("www.example.org"), None)
      }.pendingUntilFixed ^
      "parse www.example.org:8080" ! {
        TestParser.parseAuthority("www.example.org:8080") must_== Authority(None, HostName("www.example.org"), Some(8080))
      }.pendingUntilFixed ^
      "parse tom:tim@www.example.org:8080" ! {
        TestParser.parseAuthority("tom:tim@www.example.org:8080") must_== Authority(Some(new UserInfo("tom", "tim")), HostName("www.example.org"), Some(8080))
      }.pendingUntilFixed ^
      "parse tom@www.example.org:8080" ! {
        TestParser.parseAuthority("tom@www.example.org:8080") must_== Authority(Some(new UserInfo("tom", "")), HostName("www.example.org"), Some(8080))
      }.pendingUntilFixed ^
      "parse tom:tim@www.example.org" ! {
        TestParser.parseAuthority("tom:tim@www.example.org") must_== Authority(Some(new UserInfo("tom", "tim")), HostName("www.example.org"), None)
      }.pendingUntilFixed ^
      "parse tom@www.example.org" ! {
        TestParser.parseAuthority("tom@www.example.org") must_== Authority(Some(new UserInfo("tom", "")), HostName("www.example.org"), None)
      }.pendingUntilFixed ^ p ^
      end
}
