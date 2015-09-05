package skinny.json

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.{ PropertyNamingStrategy, ObjectMapper }
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper

/**
 * Easy-to-use JSON String Operation.
 */
trait JSONStringOps {

  /**
   * Use the prefix for JSON Vulnerability Protection.
   * see: "https://docs.angularjs.org/api/ng/service/$http"
   */
  protected def useJSONVulnerabilityProtection: Boolean = false

  /**
   * the prefix for JSON Vulnerability Protection.
   * see: "https://docs.angularjs.org/api/ng/service/$http"
   */
  protected def prefixForJSONVulnerabilityProtection: String = ")]}',\n"

  /**
   * Default key policy.
   */
  protected def useUnderscoreKeysForJSON: Boolean = true

  // -------------------------------
  // Avoid extending org.json4s.jackson.JsonMethods due to #render method conflict
  // -------------------------------

  private[this] lazy val _defaultMapper = {
    val m = new ObjectMapper with ScalaObjectMapper
    m.registerModule(DefaultScalaModule)
    m.setDefaultPrettyPrinter(new DefaultPrettyPrinter)
    m
  }

  private[this] lazy val _snakeCaseMapper = {
    val m = new ObjectMapper with ScalaObjectMapper
    m.registerModule(DefaultScalaModule)
    m.setDefaultPrettyPrinter(new DefaultPrettyPrinter)
    m.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES)
    m
  }

  def plainJSONObjectMapper: ObjectMapper = _defaultMapper

  def snakeCasedKeyJSONObjectMapper: ObjectMapper = _snakeCaseMapper

  /**
   * Converts a value to JSON string.
   *
   * @param value value
   * @param underscoreKeys apply #underscoreKeys keys if true
   * @return json string
   */
  def toJSONString(value: Any, underscoreKeys: Boolean = useUnderscoreKeysForJSON, prettify: Boolean = false): String = {
    val json = if (underscoreKeys) {
      if (prettify) snakeCasedKeyJSONObjectMapper.writerWithDefaultPrettyPrinter.writeValueAsString(value)
      else snakeCasedKeyJSONObjectMapper.writeValueAsString(value)
    } else {
      if (prettify) plainJSONObjectMapper.writerWithDefaultPrettyPrinter.writeValueAsString(value)
      else plainJSONObjectMapper.writeValueAsString(value)
    }
    if (useJSONVulnerabilityProtection) prefixForJSONVulnerabilityProtection + json
    else json
  }

  def toJSONStringAsIs(value: Any, prettify: Boolean = false): String = toJSONString(value, false, prettify)

  /**
   * Converts a value to prettified JSON string.
   *
   * @param value value
   * @param underscoreKeys apply #underscoreKeys keys if true
   * @return json string
   */
  def toPrettyJSONString(value: Any, underscoreKeys: Boolean = useUnderscoreKeysForJSON): String = {
    toJSONString(value, underscoreKeys, true)
  }

  def toPrettyJSONStringAsIs(value: Any): String = toPrettyJSONString(value, false)

  /**
   * Extracts a value from JSON string.
   * NOTE: When you convert to Map objects, be aware that underscoreKeys is false by default.
   *
   * @param json json string
   * @param underscoreKeys apply #underscoreKeys keys if true
   * @tparam A return type
   * @return value
   */
  def fromJSONString[A](json: String, underscoreKeys: Boolean = false)(implicit mf: Manifest[A]): Option[A] = {
    val pureJson = if (useJSONVulnerabilityProtection &&
      json.startsWith(prefixForJSONVulnerabilityProtection)) {
      json.replace(prefixForJSONVulnerabilityProtection, "")
    } else {
      json
    }
    val clazz = mf.runtimeClass.asInstanceOf[Class[A]]
    Option {
      if (underscoreKeys) snakeCasedKeyJSONObjectMapper.readValue[A](pureJson, clazz)
      else plainJSONObjectMapper.readValue[A](pureJson, clazz)
    }
  }

}

object JSONStringOps
  extends JSONStringOps
