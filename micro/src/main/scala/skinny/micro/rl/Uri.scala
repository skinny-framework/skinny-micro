// The MIT License (MIT)　Copyright (c) 2011 Mojolly Ltd.
package skinny.micro.rl

import java.net.{ URI, URISyntaxException, IDN }

trait UriNode {
  def uriPart: String
  def normalize: UriNode

  def apply(): String
}

trait UriOperations {
  def +(other: Uri): Uri
  def normalize: Uri
  def /(other: Uri): Uri
}

trait Uri {
  def scheme: UriScheme
  def authority: Option[Authority]
  def segments: UriPath
  def query: QueryString
  def fragment: UriFragment

  lazy val user = authority flatMap (_.userInfo map (_.user))
  lazy val secret = authority flatMap (_.userInfo map (_.secret))
  lazy val host = authority map (_.host.value)
  lazy val port = authority map (_.port)
  lazy val path = segments()
  lazy val queryString = query()
  lazy val rawQuery = query.rawValue

  def originalUri: String
  def isAbsolute: Boolean
  def isRelative: Boolean
  def normalize: Uri = normalize(false)
  def normalize(stripCommonPrefixFromHost: Boolean = false): Uri
  def withPath(path: String): this.type

  def asciiStringWithoutTrailingSlash = {
    scheme.uriPart + authority.map(_.uriPart).getOrElse("") + segments.uriPartWithoutTrailingSlash + query.uriPart + fragment.uriPart
  }

  def asciiString = {
    scheme.uriPart + authority.map(_.uriPart).getOrElse("") + segments.uriPart + query.uriPart + fragment.uriPart
  }

  private[this] def ensureTrailingSlash(part: String) = if (part endsWith "/") part else part + "/"
}

case class AbsoluteUri(scheme: Scheme, authority: Option[Authority], segments: UriPath, query: QueryString, fragment: UriFragment, originalUri: String = "") extends Uri {
  val isAbsolute: Boolean = true
  val isRelative: Boolean = false

  def normalize(stripCommonPrefixFromHost: Boolean = false) =
    copy(scheme.normalize, authority.map(_.normalize(stripCommonPrefixFromHost)), segments.normalize, query.normalize, fragment.normalize)
  def withPath(path: String): this.type =
    copy(segments = UriPath.parsePath(path.blankOption map UrlCodingUtils.ensureUrlEncoding).normalize).asInstanceOf[this.type]
}

case class RelativeUri(authority: Option[Authority], segments: UriPath, query: QueryString, fragment: UriFragment, originalUri: String = "") extends Uri {
  val scheme = NoScheme

  val isAbsolute: Boolean = false
  val isRelative: Boolean = true

  def normalize(stripCommonPrefixFromHost: Boolean = false) =
    copy(authority.map(_.normalize(stripCommonPrefixFromHost)), segments.normalize, query.normalize, fragment.normalize)

  def withPath(path: String): this.type =
    copy(segments = UriPath.parsePath(path.blankOption map UrlCodingUtils.ensureUrlEncoding).normalize).asInstanceOf[this.type]
}

case class FailedUri(throwable: Throwable, originalUri: String = "") extends Uri {

  private def noop = {
    val u = originalUri.blankOption getOrElse "not set"
    throw new UnsupportedOperationException("Parsing the uri '%s' failed." format u, throwable)
  }

  def fragment = noop

  def query = noop

  def segments = noop

  def authority = noop

  def scheme = noop

  val isRelative: Boolean = false

  val isAbsolute: Boolean = false

  def normalize(stripCommonPrefixFromHost: Boolean = false) = this

  def withPath(path: String): this.type = this
}

object Uri {

  /*
   * The regex to split a URI up into its parts for further processing
   * Source: http://tools.ietf.org/html/rfc3986#appendix-B
   */
  val UriParts = """^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\?([^#]*))?(#(.*))?""".r

  def apply(uriString: String): Uri = {
    try {
      apply(URI.create(uriString))
    } catch {
      case e: URISyntaxException => {
        FailedUri(e, uriString)
      }
      case e: IllegalArgumentException => {
        FailedUri(e, uriString)
      }
    }
  }

  def apply(u: URI, originalUri: Option[String] = None): Uri = {
    try {
      val pth = UriPath.parsePath(u.getRawPath.blankOption map UrlCodingUtils.ensureUrlEncoding)

      if (u.isAbsolute) {
        AbsoluteUri(
          Scheme(u.getScheme),
          u.getRawAuthority.blankOption.map(a => Authority(IDN.toASCII(a))),
          pth,
          QueryString(u.getRawQuery),
          UriFragment(u.getRawFragment),
          originalUri getOrElse u.toString)
      } else {
        RelativeUri(
          u.getRawAuthority.blankOption.map(a => Authority(IDN.toASCII(a))),
          pth,
          QueryString(u.getRawQuery),
          UriFragment(u.getRawFragment),
          originalUri getOrElse u.toString)
      }
    } catch {
      case e: NullPointerException => {
        FailedUri(e, originalUri getOrElse u.toString)
      }
      case e: Throwable => {
        FailedUri(e, originalUri getOrElse u.toString)
      }
    }
  }

}
