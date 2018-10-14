package skinny.micro.test

import java.io.InputStream

import scala.collection.DefaultMap
import scala.collection.JavaConverters._

/**
 * Http Client's response.
 */
abstract class ClientResponse {

  def bodyBytes: Array[Byte]
  def inputStream: InputStream
  def statusLine: ResponseStatus
  def headers: Map[String, Seq[String]]

  def body = new String(bodyBytes, charset.getOrElse("ISO-8859-1"))

  def mediaType: Option[String] = {
    header.get("Content-Type") match {
      case Some(contentType) => contentType.split(";").map(_.trim).headOption
      case _ => None
    }
  }

  def status = statusLine.code

  val header = new DefaultMap[String, String] {
    def get(key: String) = {
      headers.get(key) match {
        case Some(values) => Some(values.head)
        case _ => None
      }
    }

    override def apply(key: String) = {
      get(key) match {
        case Some(value) => value
        case _ => null
      }
    }

    def iterator = {
      headers.keys.map(name => (name -> this(name))).iterator
    }

    // Since Scala 2.13.0-M5
    //// These two methods are not in MapOps so that MapView is not forced to implement them
    //@deprecated("Use - or remove on an immutable Map", "2.13.0")
    //def - (key: K): Map[K, V]
    //@deprecated("Use -- or removeAll on an immutable Map", "2.13.0")
    //def - (key1: K, key2: K, keys: K*): Map[K, V]

    override def -(key: String): collection.Map[String, String] = {
      headers.keys
        .filter(_ != key)
        .map(name => (name -> this(name))).toMap
    }

    override def -(key1: String, key2: String, keys: String*): collection.Map[String, String] = {
      headers.keys
        .filter(name => name != key1 && name != key2 && keys.contains(name) == false)
        .map(name => (name -> this(name))).toMap
    }
  }

  def charset = {
    header.getOrElse("Content-Type", "").split(";").map(_.trim).find(_.startsWith("charset=")) match {
      case Some(attr) => Some(attr.split("=")(1))
      case _ => None
    }
  }

  def getReason() = statusLine.message

  def getHeader(name: String) = header.getOrElse(name, null)

  def getLongHeader(name: String) = header.getOrElse(name, "-1").toLong

  def getHeaderNames(): java.util.Enumeration[String] = headers.keysIterator.asJavaEnumeration

  def getHeaderValues(name: String): java.util.Enumeration[String] = headers.getOrElse(name, Seq()).iterator.asJavaEnumeration

  def getContentBytes() = bodyBytes

  def getContent() = body

  def getContentType() = header.getOrElse("Content-Type", null)

}
