package skinny.micro.data

import scala.language.reflectiveCalls
import skinny.micro.SkinnyMicroException
import skinny.micro.implicits.TypeConverter

import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.collection.mutable.Map

/**
 * Adapts attributes from servlet objects (e.g., ServletRequest, HttpSession,
 * ServletContext) to a mutable map.
 */
trait AttributesMap extends Map[String, Any] with MutableMapWithIndifferentAccess[Any] {

  protected def attributes: Attributes

  override protected def mutableMap: mutable.Map[String, Any] = new mutable.Map[String, Any] {

    override def get(key: String): Option[Any] = Option(attributes.getAttribute(key))

    override def iterator: Iterator[(String, Any)] = {
      attributes.getAttributeNames.asScala.toSeq
        .map { name => name -> attributes.getAttribute(name) }.iterator
    }

    override def addOne(elem: (String, Any)): this.type = {
      try {
        attributes.setAttribute(elem._1, elem._2.asInstanceOf[AnyRef])
      } catch { case _: ClassCastException => }
      this
    }

    override def clear(): Unit = {
      attributes.getAttributeNames.asScala.foreach(attributes.removeAttribute)
    }

    override def subtractOne(elem: String): this.type = {
      attributes.removeAttribute(elem)
      this
    }

  }

  /**
   * Optionally returns the attribute associated with the key
   *
   * @return an option value containing the attribute associated with the key
   * in the underlying servlet object, or None if none exists.
   */
  def get(key: String): Option[Any] = {
    if (attributes == null) None
    else {
      attributes.getAttribute(key) match {
        case null => None
        case v => Some(v)
      }
    }
  }

  /**
   * Optionally return and type cast the attribute associated with the key
   *
   * @param key The key to find
   * @tparam T The type of the value
   * @return an option value containing the attributed associated with the key in the underlying servlet object,
   *         or None if none exists
   */
  def getAs[T](key: String)(implicit
    mf: Manifest[T],
    converter: TypeConverter[Any, T]): Option[T] = {
    get(key) flatMap (converter(_))
  }

  /**
   * Return the attribute associated with the key or throw an exception when nothing found
   *
   * @param key The key to find
   * @tparam T The type of the value
   * @return an value for the attributed associated with the key in the underlying servlet object,
   *         or throw an exception if the key doesn't exist
   */
  def as[T](key: String)(implicit
    mf: Manifest[T],
    converter: TypeConverter[Any, T]): T = {
    getAs[T](key) getOrElse (throw new SkinnyMicroException("Key " + key + " not found"))
  }

  /**
   * Return the attribute associated with the key or throw an exception when nothing found
   *
   * @param key The key to find
   * @tparam T The type of the value
   * @return an value for the attributed associated with the key in the underlying servlet object,
   *         or throw an exception if the key doesn't exist
   */
  def getAsOrElse[T](key: String, default: => T)(implicit
    mf: Manifest[T],
    converter: TypeConverter[Any, T]): T = {
    getAs[T](key) getOrElse default
  }

  /**
   * Creates a new iterator over all attributes in the underlying servlet object.
   *
   * @return the new iterator
   */
  def iterator: Iterator[(String, Any)] = {
    attributes.getAttributeNames().asScala map { key =>
      (key, attributes.getAttribute(key))
    }
  }

}
