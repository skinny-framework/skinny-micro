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

  def -=(key: String): this.type = {
    logger.warn("Map#-= operation is unsupported for request headers. Skipped.")
    this
  }

  def +=(kv: (String, String)): this.type = {
    logger.warn("Map#+= operation is unsupported for request headers. Skipped.")
    this
  }

}
