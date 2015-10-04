package skinny.micro.request

import java.util.{ Collections, Date }
import javax.servlet.ServletContext
import javax.servlet.http.{ HttpServletRequest, HttpSessionContext, HttpSession }
import skinny.logging.LoggerProvider

import scala.collection.JavaConverters._

import scala.collection.concurrent.TrieMap
import scala.util.Try
import scala.util.control.NonFatal

/**
 * Mostly stable Servlet HttpSession implementation.
 *
 * Since this implementation never throws exception even when Servlet containers have recycled request/session objects,
 * it works mostly as we expected within Future operations.
 *
 * This implementation doesn't completely guarantee attributes' consistency in the session
 * because #setAttribute after objects recycle cannot save the value in memory managed by Servlet containers.
 *
 * If you expect read-only stable session in Future threads, this implementation works fine as you expected.
 */
case class MostlyStableHttpSession(request: HttpServletRequest) extends HttpSession with LoggerProvider {

  private[this] val attributes: TrieMap[String, Any] = new TrieMap[String, Any]()

  // create session if absent
  private[this] def underlying: HttpSession = request.getSession(true)

  private[this] val _isNew: Boolean = underlying != null && underlying.isNew

  private[this] def isUnderlyingAvailable: Boolean = {
    underlying != null && Try(underlying.getId).isSuccess
  }

  if (underlying != null && !_isNew) {
    underlying.getAttributeNames.asScala.foreach { name =>
      attributes.put(name, underlying.getAttribute(name))
    }
  }

  private[this] val createdTime = new Date

  private[this] val _getMaxInactiveInterval: Int = {
    if (underlying != null) underlying.getMaxInactiveInterval
    else 0
  }

  private[this] var id: String = {
    if (underlying != null && !underlying.isNew) underlying.getId
    else Thread.currentThread.getId + "-" + createdTime.getTime
  }

  private[this] def refreshId(): Unit = {
    id = Thread.currentThread.getId + "-" + createdTime.getTime
  }

  private[this] def unsupportedOperationError(msg: String) = throw new UnsupportedOperationException(msg)

  override def getValue(name: String): AnyRef = {
    val value = attributes.get(name).map(_.asInstanceOf[AnyRef]).orNull[AnyRef]
    if (value == null && isUnderlyingAvailable) {
      try {
        val v = underlying.getAttribute(name)
        if (v != null) {
          attributes.put(name, v)
        }
        v
      } catch { case NonFatal(_) => value }
    } else {
      value
    }
  }

  override def isNew: Boolean = _isNew

  override def getValueNames: Array[String] = {
    val names: Array[String] = Option(attributes.keys).map(_.toArray).getOrElse(Array.empty)
    if (isUnderlyingAvailable) {
      (Option(underlying.getAttributeNames).map(_.asScala ++ names).getOrElse(Seq.empty)).toArray.distinct
    } else {
      names
    }
  }

  override def getLastAccessedTime: Long = createdTime.getTime

  override def putValue(name: String, value: scala.Any): Unit = setAttribute(name, value)

  override def getSessionContext: HttpSessionContext = unsupportedOperationError("#getSessionContext is unsupported")

  override def getAttribute(name: String): AnyRef = getValue(name)

  override def removeAttribute(name: String): Unit = {
    removeAttribute(name)
  }

  override def getId: String = id

  override def setMaxInactiveInterval(interval: Int): Unit = {
    if (isUnderlyingAvailable) {
      try underlying.setMaxInactiveInterval(interval)
      catch {
        case NonFatal(e) =>
          logger.debug(s"Failed to execute underlying session's setMaxInactiveInterval method because ${e.getMessage}", e)
      }
    } else {
      unsupportedOperationError("#setMaxInactiveInterval is unsupported")
    }
  }

  override def getAttributeNames: java.util.Enumeration[String] = {
    Collections.enumeration(getValueNames.toList.asJava)
  }

  override def setAttribute(name: String, value: Any): Unit = {
    attributes.put(name, value)
    if (isUnderlyingAvailable) {
      try underlying.setAttribute(name, value)
      catch {
        case NonFatal(e) =>
          logger.debug(s"Failed to execute underlying session's setAttribute method because ${e.getMessage}", e)
      }
    }
  }

  override def invalidate(): Unit = {
    refreshId()
    attributes.clear()
    if (isUnderlyingAvailable) {
      try underlying.invalidate()
      catch {
        case NonFatal(e) =>
          logger.debug(s"Failed to execute underlying session's invalidate method because ${e.getMessage}", e)
      }
    }
  }

  override def getCreationTime: Long = createdTime.getTime

  override def removeValue(name: String): Unit = {
    attributes.remove(name)
    if (isUnderlyingAvailable) {
      try underlying.removeAttribute(name)
      catch {
        case NonFatal(e) =>
          logger.debug(s"Failed to execute underlying session's removeAttribute(${name}) method because ${e.getMessage}", e)
      }
    }
  }

  override def getServletContext: ServletContext = unsupportedOperationError("#getServletContext is unsupported")

  override def getMaxInactiveInterval: Int = _getMaxInactiveInterval

}
