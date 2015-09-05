package skinny.xml

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.{ JsonMappingException, PropertyNamingStrategy, ObjectMapper }
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.dataformat.xml.util.DefaultXmlPrettyPrinter
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import skinny.json.JSONStringOps._

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

  private[this] lazy val _defaultMapper: XmlMapper = {
    val m = new XmlMapper with ScalaObjectMapper
    m.registerModule(DefaultScalaModule)
    m.setDefaultPrettyPrinter(new DefaultXmlPrettyPrinter)
    m
  }

  private[this] lazy val _snakeCaseMapper: XmlMapper = {
    val m = new XmlMapper with ScalaObjectMapper
    m.registerModule(DefaultScalaModule)
    m.setDefaultPrettyPrinter(new DefaultPrettyPrinter)
    m.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES)
    m
  }

  def plainObjectMapper: XmlMapper = _defaultMapper

  def snakeCasedKeyObjectMapper: XmlMapper = _snakeCaseMapper

  /**
   * Converts a value to XML string.
   *
   * @param value value
   * @param underscoreKeys apply #underscoreKeys keys if true
   * @return xml string
   */
  def toXMLString(value: Any, charset: String = "UTF-8", underscoreKeys: Boolean = useUnderscoreKeysForXML, prettify: Boolean = false): String = {
    val mapValueOpt: Option[Any] = {
      try { fromJSONString[Map[String, Any]](toJSONString(value, underscoreKeys, prettify)) }
      catch {
        case e: JsonMappingException =>
          // try to extract top level array value
          fromJSONString[Array[Map[String, Any]]](toJSONString(value, underscoreKeys, prettify))
      }
    }
    val xml = mapValueOpt match {
      case Some(mapValue) =>
        if (prettify) plainObjectMapper.writerWithDefaultPrettyPrinter.writeValueAsString(mapValue)
        else plainObjectMapper.writeValueAsString(mapValue)
      case _ =>
        ""
    }
    val entityXml = {
      xml.replaceFirst("""<Map1>""", "").replaceFirst("</Map1>", "")
        .replaceFirst("""<Maps>""", "").replaceFirst("</Maps>", "")
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
  def fromXMLString[A](xml: String, underscoreKeys: Boolean = false)(implicit mf: Manifest[A]): Option[A] = {
    val clazz = mf.runtimeClass.asInstanceOf[Class[A]]
    Option {
      if (underscoreKeys) snakeCasedKeyObjectMapper.readValue[A](xml, clazz)
      else plainObjectMapper.readValue[A](xml, clazz)
    }
  }

}

object XMLStringOps
  extends XMLStringOps
