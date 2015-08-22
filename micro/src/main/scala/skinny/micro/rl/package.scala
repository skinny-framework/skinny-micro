package skinny.micro

import scala.language.implicitConversions

import scala.io.Codec

package object rl {

  private[rl] class UriStringExtensions(source: String) {
    def isNotBlank = Option(source).foldLeft(false) { (_, v) => v.trim.nonEmpty }
    def blankOption = if (isNotBlank) Some(source) else None
  }

  private[rl] class RicherUriString(source: String) {
    def urlEncode = if (source != null && source.trim().nonEmpty) UrlCodingUtils.urlEncode(source) else ""
    def urlDecode = if (source != null && source.trim().nonEmpty) UrlCodingUtils.urlDecode(source) else ""
  }

  private[rl] implicit def string2UriStringExtension(source: String) = new UriStringExtensions(source)
  private[rl] implicit val Utf8Codec = Codec.UTF8

  trait Imports {
    implicit def string2RicherUriString(s: String) = new RicherUriString(s)
  }

  object Imports extends Imports
}
