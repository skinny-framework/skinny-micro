// The MIT License (MIT)　Copyright (c) 2011 Mojolly Ltd.
package skinny.micro.rl

import scala.collection.GenSeq
import scala.collection.immutable.Vector

trait UriPath extends UriNode {

  def segments: GenSeq[String]
  def isRelative: Boolean
  def isAbsolute: Boolean

  def collapseDots(): GenSeq[String] = {
    segments.foldLeft(Vector.empty[String]) { (lb, seg) =>
      seg match {
        case "." => lb
        case "src/main" => if (!lb.isEmpty) lb.dropRight(1) else lb
        case a => lb :+ a
      }
    }
  }

  def normalize: UriPath

  def apply() = uriPart

  protected[this] def startSeparator = UriPath.unixSeparator

  protected[this] def toUriPart(endSeparator: String = UriPath.unixSeparator) = {
    val l = segments.size
    if (l == 0) ""
    else {
      val sb = new StringBuilder
      var i = 0
      while (i < l) {
        if (i > 0) sb.append(UriPath.unixSeparator)
        else sb.append(startSeparator)
        sb.append(segments(i))
        i += 1
      }
      sb.append(endSeparator)
      sb.toString()
    }
  }

  def uriPartWithoutTrailingSlash = toUriPart("")

}

trait EmptyUriPath extends UriPath {

  val segments = Nil

  def normalize = this

}

case object EmptyPath extends EmptyUriPath {

  val isAbsolute: Boolean = false

  val isRelative: Boolean = true

  val uriPart = "/"

}

case class RelativePath(segments: GenSeq[String]) extends UriPath {

  val isAbsolute: Boolean = false

  val isRelative: Boolean = true

  override protected[this] def startSeparator: String = ""

  val uriPart = toUriPart()

  def normalize = RelativePath(collapseDots())

}

case class AbsolutePath(segments: GenSeq[String]) extends UriPath {

  val isAbsolute: Boolean = true

  val isRelative: Boolean = false

  val uriPart = toUriPart()

  def normalize = AbsolutePath(collapseDots())

}

trait PathOps {

  private val wlpExpr = """^[A-Za-z]:\\""".r
  private val wuncpExpr = """^\\\\""".r

  val windowsSeparator = "\\"
  val unixSeparator = "/"

  def windowsToUnixPath(path: String) = {
    if (wlpExpr.findFirstIn(path).isDefined) {
      "file:///" + convertWindowsToUnixPath(path)
    } else if (wuncpExpr.findFirstIn(path).isDefined) {
      "file:" + convertWindowsToUnixPath(path)
    } else convertWindowsToUnixPath(path)
  }

  private def convertWindowsToUnixPath(path: String) = {
    path.replace(windowsSeparator, unixSeparator).replace(" ", "%20")
  }

  def parsePath(text: Option[String]): UriPath = {
    text match {
      case None => EmptyPath
      case Some(pt) if pt.trim == "/" => EmptyPath
      case Some(pt) if pt.startsWith("/") => AbsolutePath(pt.split("/").drop(1).toList)
      case Some(pt) => RelativePath(pt.split("/"))
    }
  }

}

object UriPath extends PathOps
