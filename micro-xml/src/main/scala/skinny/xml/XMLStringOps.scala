package skinny.xml

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.{ DeserializationFeature, PropertyNamingStrategy }
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.dataformat.xml.util.DefaultXmlPrettyPrinter
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import skinny.json.JSONStringOps._
import scala.util.{ Failure, Success, Try }

/**
 * Easy-to-use XML String Operation.
 */
trait XMLStringOps {

  /**
   * Default key policy.
   */
  protected def useUnderscoreKeysForXML: Boolean = false

  protected def xmlRootName: String = "response"

  // -------------------------------
  // Avoid extending org.xml4s.jackson.JsonMethods due to #render method conflict
  // -------------------------------

  private[this] def withBasicConfiguration(m: XmlMapper): XmlMapper = {
    m.registerModule(DefaultScalaModule)
    // To avoid deserializing absent fields with null
    // https://github.com/FasterXML/jackson-module-scala/issues/87
    m.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, true)
    m.setDefaultPrettyPrinter(new DefaultXmlPrettyPrinter)
    m
  }

  private[this] lazy val _defaultMapper: XmlMapper = {
    withBasicConfiguration(new XmlMapper with ScalaObjectMapper)
  }

  private[this] lazy val _snakeCaseMapper: XmlMapper = {
    val m = withBasicConfiguration(new XmlMapper with ScalaObjectMapper)
    m.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES)
    m
  }

  def plainXMLObjectMapper: XmlMapper = _defaultMapper

  def snakeCasedKeyXMLObjectMapper: XmlMapper = _snakeCaseMapper

  /**
   * Converts a value to XML string.
   *
   * @param value value
   * @param underscoreKeys apply #underscoreKeys keys if true
   * @return xml string
   */
  def toXMLString(value: Any, charset: String = "UTF-8", underscoreKeys: Boolean = useUnderscoreKeysForXML, prettify: Boolean = false): String = {
    val mapValueOpt: Option[Any] = {
      fromJSONString[Map[String, Any]](toJSONString(value, underscoreKeys, prettify)) match {
        case Success(mapValue) => Some(mapValue)
        case Failure(e) =>
          // try to extract top level array value
          fromJSONString[Array[Map[String, Any]]](toJSONString(value, underscoreKeys, prettify)).toOption
      }
    }
    val xml = mapValueOpt match {
      case Some(mapValue) =>
        if (prettify) plainXMLObjectMapper.writerWithDefaultPrettyPrinter.writeValueAsString(mapValue)
        else plainXMLObjectMapper.writeValueAsString(mapValue)
      case _ =>
        ""
    }
    val entityXml = {
      // a bit ugly way to remove noisy elements
      xml
        .replaceAll("<Map\\d+>", "").replaceAll("</Map\\d+>", "")
        .replaceAll("<Maps>", "").replaceAll("</Maps>", "")
    }
    s"""<?xml version="1.0" encoding="${charset}"?><${xmlRootName}>${entityXml}</${xmlRootName}>"""
  }

  /**
   * Converts a value to prettified XML string.
   *
   * @param value value
   * @param underscoreKeys apply #underscoreKeys keys if true
   * @return xml string
   */
  def toPrettyXMLString(value: Any, charset: String = "UTF-8", underscoreKeys: Boolean = useUnderscoreKeysForXML): String = {
    toXMLString(value, charset, underscoreKeys, true)
  }

  /**
   * Extracts a value from XML string.
   * NOTE: When you convert to Map objects, be aware that underscoreKeys is false by default.
   *
   * @param xml xml string
   * @param underscoreKeys apply #underscoreKeys keys if true
   * @tparam A return type
   * @return value
   */
  def fromXMLString[A](xml: String, underscoreKeys: Boolean = false)(implicit mf: Manifest[A]): Try[A] = {
    val clazz = mf.runtimeClass.asInstanceOf[Class[A]]
    Try {
      if (underscoreKeys) snakeCasedKeyXMLObjectMapper.readValue[A](xml, clazz)
      else plainXMLObjectMapper.readValue[A](xml, clazz)
    }
  }

}

object XMLStringOps
  extends XMLStringOps
