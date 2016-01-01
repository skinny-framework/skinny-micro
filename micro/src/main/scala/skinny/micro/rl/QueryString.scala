// The MIT License (MIT)　Copyright (c) 2011 Mojolly Ltd.
package skinny.micro.rl

import scala.language.postfixOps

import scala.collection.SortedMap
import Imports._

object QueryString {

  val DEFAULT_EXCLUSIONS = List("utm_source", "utm_medium", "utm_term", "utm_content", "utm_campaign", "sms_ss", "awesm")

  def apply(rawValue: String) = {
    rawValue.blankOption map { v =>
      (v.indexOf('&') > -1, v.indexOf('=') > -1) match {
        case (true, true) | (false, true) => MapQueryString(v)
        case (true, false) => StringSeqQueryString(v)
        case (false, false) => StringQueryString(v)
      }
    } getOrElse EmptyQueryString
  }
}
trait QueryString extends UriNode {
  type Value
  def rawValue: String
  def value: Value
  def empty: Value

  def normalize: QueryString

  def apply() = rawValue.urlEncode
}
case object EmptyQueryString extends QueryString {

  def empty = ""

  type Value = String
  val value = empty
  val uriPart = empty
  val rawValue = empty

  val normalize = this
}
case class StringQueryString(rawValue: String) extends QueryString {

  val uriPart = "?" + rawValue.blankOption.map(_.urlEncode).getOrElse("")
  val value = rawValue.blankOption.map(_.urlEncode).getOrElse("")

  val empty = ""

  type Value = String

  def normalize = this
}
case class StringSeqQueryString(rawValue: String) extends QueryString {
  val value: Value = rawValue.blankOption.map(_.split("&").map(_.urlDecode).toList).getOrElse(Nil)

  val uriPart = "?" + value.map(_.urlEncode).mkString("?", "&", "")

  val empty = Nil

  type Value = List[String]

  def normalize = StringSeqQueryString(value.sortWith(_ >= _).map(_.urlEncode).mkString("?", "&", ""))
}

object MapQueryString {
  def parseString(rw: String) = {
    // this is probably an accident waiting to happen when people do actually mix stuff
    val semiColon = if (rw.indexOf(';') > -1) {
      rw.split(';').foldRight(Map[String, List[String]]()) { readQsPair _ }
    } else readQsPair(rw)
    val ampersand = if (rw.indexOf('&') > -1) {
      rw.split('&').foldRight(Map[String, List[String]]()) { readQsPair _ }
    } else {
      readQsPair(rw)
    }
    semiColon ++ ampersand
  }

  private def readQsPair(pair: String, current: Map[String, List[String]] = Map.empty) = {
    (pair split '=' toList) map { _.urlDecode } match {
      case item :: Nil => current + (item -> List[String]())
      case item :: rest =>
        if (!current.contains(item)) current + (item -> rest) else (current + (item -> (rest ::: current(item)).distinct))
      case _ => current
    }
  }

  def apply(rawValue: String): MapQueryString = new MapQueryString(parseString(rawValue).toSeq, rawValue)
}
case class MapQueryString(initialValues: Seq[(String, Seq[String])], rawValue: String) extends QueryString {

  val uriPart = {
    "?" + mkString()
  }

  val empty = Map.empty[String, List[String]]

  def value: Value = Map(initialValues: _*)

  def normalize = copy(SortedMap(initialValues filter (k => !QueryString.DEFAULT_EXCLUSIONS.contains(k._1)): _*) toSeq)

  private def mkString(values: Value = value) = values map {
    case (k, v) => v.map(s => "%s=%s".format(k.urlEncode, s.urlEncode)).mkString("&")
  } mkString "&"

  type Value = Map[String, Seq[String]]

}
