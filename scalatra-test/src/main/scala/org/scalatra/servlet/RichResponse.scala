package org.scalatra
package servlet

import util.RicherString._

import java.io.{ OutputStream, PrintWriter }
import javax.servlet.http.{ HttpServletResponse, HttpServletResponseWrapper, Cookie => ServletCookie }
import scala.collection.JavaConverters._
import scala.collection.mutable.Map

case class RichResponse(res: HttpServletResponse) {
  /**
   * Note: the servlet API doesn't remember the reason.  If a custom
   * reason was set, it will be returned incorrectly here,
   */
  def status: ResponseStatus = ResponseStatus(res.getStatus)

  def status_=(statusLine: ResponseStatus): Unit = {
    res.setStatus(statusLine.code, statusLine.message)
  }

  val headers = new ResponseHeaders(res)

  def addCookie(cookie: Cookie): Unit = {
    import cookie._

    val sCookie = new ServletCookie(name, value)
    if (options.domain.nonBlank) sCookie.setDomain(options.domain)
    if (options.path.nonBlank) sCookie.setPath(options.path)
    sCookie.setMaxAge(options.maxAge)
    sCookie.setSecure(options.secure)
    if (options.comment.nonBlank) sCookie.setComment(options.comment)
    sCookie.setHttpOnly(options.httpOnly)
    sCookie.setVersion(options.version)
    res.addCookie(sCookie)
  }

  def characterEncoding: Option[String] =
    Option(res.getCharacterEncoding)

  def characterEncoding_=(encoding: Option[String]): Unit = {
    res.setCharacterEncoding(encoding getOrElse null)
  }

  def contentType: Option[String] =
    Option(res.getContentType)

  def contentType_=(contentType: Option[String]): Unit = {
    res.setContentType(contentType getOrElse null)
  }

  def redirect(uri: String): Unit = {
    res.sendRedirect(uri)
  }

  def outputStream: OutputStream =
    res.getOutputStream

  def writer: PrintWriter =
    res.getWriter

  def end(): Unit = {
    res.flushBuffer()
    res.getOutputStream.close()
  }
}
