package skinny.micro.response

import javax.servlet.http.HttpServletResponse

import scala.collection.JavaConverters._
import scala.collection.mutable.Map

class ResponseHeaders(res: HttpServletResponse) extends Map[String, String] {
  def get(key: String): Option[String] =
    res.getHeaders(key) match {
      case xs if xs.isEmpty => None
      case xs => Some(xs.asScala mkString ",")
    }

  def iterator: Iterator[(String, String)] = {
    for (name <- res.getHeaderNames.asScala.iterator)
      yield (name, res.getHeaders(name).asScala mkString ", ")
  }

  def -=(key: String): this.type = {
    res.setHeader(key, null)
    this
  }

  def +=(kv: (String, String)): this.type = {
    res.setHeader(kv._1, kv._2)
    this
  }

}
