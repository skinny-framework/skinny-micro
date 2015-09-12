package skinny.jackson

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.`type`.CollectionType
import com.fasterxml.jackson.databind.{ DeserializationFeature, PropertyNamingStrategy, ObjectMapper }
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper

import scala.util.Try
import scala.reflect._
import scala.reflect.runtime.{ universe => ru }
import ru._
import scala.collection.JavaConverters._

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

  private[this] def withBasicConfiguration(m: ObjectMapper): ObjectMapper = {
    m.registerModule(DefaultScalaModule)
    m.setDefaultPrettyPrinter(new DefaultPrettyPrinter)
    // To avoid deserializing absent fields with null
    // https://github.com/FasterXML/jackson-module-scala/issues/87
    m.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, true)
    m
  }

  private[this] lazy val _defaultMapper = {
    withBasicConfiguration(new ObjectMapper with ScalaObjectMapper)
  }

  private[this] lazy val _snakeCaseMapper = {
    val m = withBasicConfiguration(new ObjectMapper with ScalaObjectMapper)
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
  def fromJSONString[A: TypeTag](json: String, underscoreKeys: Boolean = useUnderscoreKeysForJSON)(
    implicit tag: ClassTag[A]): Try[A] = {

    val pureJson = if (useJSONVulnerabilityProtection &&
      json.startsWith(prefixForJSONVulnerabilityProtection)) {
      json.replace(prefixForJSONVulnerabilityProtection, "")
    } else {
      json
    }

    Try {
      if (collectionClassTags.exists(_ == classTag[A])) {
        val mirror = ru.runtimeMirror(Thread.currentThread.getContextClassLoader)
        // NOTE: Scala 2.10 doesn't have typeArgs
        // val typeArgClass = mirror.runtimeClass(typeOf[A].typeArgs.head)
        val typeArgClass = mirror.runtimeClass(typeOf[A].asInstanceOf[TypeRefApi].args.head)
        val colType: CollectionType = plainJSONObjectMapper.getTypeFactory.constructCollectionType(
          classOf[java.util.List[_]], typeArgClass)

        val arrayValue = {
          if (underscoreKeys) snakeCasedKeyJSONObjectMapper.readValue[A](pureJson, colType)
          else plainJSONObjectMapper.readValue[A](pureJson, colType)
        }
        arrayValue.asInstanceOf[java.util.List[_]].asScala.asInstanceOf[A]

      } else {
        val clazz = classTag[A].runtimeClass.asInstanceOf[Class[A]]
        if (underscoreKeys) snakeCasedKeyJSONObjectMapper.readValue[A](pureJson, clazz)
        else plainJSONObjectMapper.readValue[A](pureJson, clazz)
      }
    }
  }

  private[this] lazy val collectionClassTags = Seq(
    ClassTag(classOf[TraversableOnce[_]]),
    ClassTag(classOf[Traversable[_]]),
    ClassTag(classOf[Iterable[_]]),
    ClassTag(classOf[Seq[_]]),
    ClassTag(classOf[IndexedSeq[_]]),
    ClassTag(classOf[Iterator[_]]),
    ClassTag(classOf[BufferedIterator[_]]),
    ClassTag(classOf[List[_]]),
    ClassTag(classOf[Stream[_]]),
    ClassTag(classOf[Vector[_]])
  )

}

object JSONStringOps
  extends JSONStringOps
