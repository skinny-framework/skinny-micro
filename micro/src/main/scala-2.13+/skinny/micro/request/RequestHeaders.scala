package skinny.micro.request

import javax.servlet.http.HttpServletRequest
import skinny.logging.LoggerProvider

import scala.collection.JavaConverters._
import scala.collection.mutable.Map

class RequestHeaders(req: HttpServletRequest) extends Map[String, String] with LoggerProvider {

  def get(key: String): Option[String] = {
    req.getHeaders(key).asScala match {
      case xs if xs.isEmpty => None
      case xs => Some(xs.mkString(","))
    }
  }

  def iterator: Iterator[(String, String)] = {
    for (name <- req.getHeaderNames.asScala)
      yield (name, req.getHeaders(name).asScala mkString ", ")
  }

  override def subtractOne(elem: String): this.type = {
    // NOOP
    logger.warn("Map#subtractOne operation is unsupported for request headers. Skipped.")
    this
  }

  override def addOne(elem: (String, String)): this.type = {
    // NOOP
    logger.warn("Map#addOne operation is unsupported for request headers. Skipped.")
    this
  }

  override def clear(): Unit = {
  }

}
