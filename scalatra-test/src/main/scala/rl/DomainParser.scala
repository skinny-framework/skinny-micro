// The MIT License (MIT)　Copyright (c) 2011 Mojolly Ltd.
package rl

import scala.io.{ Codec, Source }
import scala.util.Try
import scala.language.reflectiveCalls

trait UriHostDomains { self: UriHost =>

  protected def parsed: (String, String, String)

  def publicSuffix = parsed._1
  def domain: String = parsed._2
  def subdomain: String = parsed._3

}

object DomainParser {

  type Closable = { def close() }

  /**
   * Closes the resource finally.
   */
  def using[R <: Closable, A](resource: R)(f: R => A): A = {
    try {
      f(resource)
    } finally {
      try {
        resource.close()
      } catch {
        case scala.util.control.NonFatal(_) =>
      }
    }
  }

  lazy val publicSuffixes: PublicSuffixList = {
    using(Source.fromInputStream(getClass.getResourceAsStream("/skinny/micro/rl/tld_names.dat"))(Codec.UTF8)) { src =>
      Try(src.getLines).toOption.map { lines =>
        lines.foldLeft(PublicSuffixList.empty) { (buff, line) =>
          line.blankOption.filter(l => !l.startsWith("//")).map { l =>
            val parts = l.split("\\.").reverse
            parts.foldLeft(buff)(_ :+ _)
            buff
          }.getOrElse(buff)
        }
      }.getOrElse(PublicSuffixList.empty)
    }
  }

  object PublicSuffixList {
    def empty = new PublicSuffixList(Vector.empty[PublicSuffix])
  }

  class PublicSuffixList(protected var suffixes: Vector[PublicSuffix]) {

    def get(key: String) = suffixes find (_.key == key) map (_.values)
    def apply(key: String) = get(key).get
    def contains(key: String) = get(key).isDefined
    def notContains(key: String) = get(key).isEmpty
    def isEmpty = suffixes.isEmpty

    private[DomainParser] def :+(key: String) = get(key) getOrElse {
      val suff = PublicSuffix(key)
      suffixes = suffixes :+ suff
      suff.values
    }

    override def toString = suffixes.toString()
  }
  case class PublicSuffix(key: String, values: PublicSuffixList = PublicSuffixList.empty)

  def apply(uri: String) = {
    val parts = (uri split "\\.").reverse
    val part = parts.head
    val subParts = publicSuffixes get part getOrElse PublicSuffixList.empty
    if (subParts contains "*") {
      (part + "." + parts(1), parts(2), parts.slice(3, parts.size) mkString ".")
    } else if (subParts.isEmpty || subParts.notContains(parts(1))) {

      (part, if (parts.size > 1) parts(1) else "", if (parts.size > 2) parts.slice(2, parts.size) mkString "." else "")
    } else {
      (part, "", "")
    }
  }
}
