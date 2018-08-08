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

  def iterator: Iterator[(String, String)] =
    for (name <- res.getHeaderNames.asScala.iterator)
      yield (name, res.getHeaders(name).asScala mkString ", ")

  override def subtractOne(elem: String): this.type = {
    res.setHeader(elem, null)
    this
  }

  override def addOne(elem: (String, String)): this.type = {
    res.setHeader(elem._1, elem._2)
    this
  }

  override def clear(): Unit = {
    res.getHeaderNames.asScala.foreach(res.setHeader(_, null))
  }

}
