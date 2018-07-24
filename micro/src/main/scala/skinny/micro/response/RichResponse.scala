package skinny.micro.response

import java.io.{ OutputStream, PrintWriter }

import javax.servlet.http.{ HttpServletResponse, Cookie => ServletCookie }
import skinny.micro.cookie.Cookie
import skinny.micro.implicits.RicherStringImplicits

/**
 * Rich Servlet response.
 */
case class RichResponse(res: HttpServletResponse) {

  import RicherStringImplicits._

  /**
   * Note: the servlet API doesn't remember the reason.  If a custom
   * reason was set, it will be returned incorrectly here,
   */
  def status: ResponseStatus = ResponseStatus(res.getStatus)

  def status_=(statusLine: ResponseStatus): Unit = {
    //  Deprecated. As of version 2.1, due to ambiguous meaning of the message parameter.
    //  To set a status code use setStatus(int), to send an error with a description use sendError(int, String).
    // res.setStatus(statusLine.code, statusLine.message)
    res.setStatus(statusLine.code)
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

  def characterEncoding: Option[String] = Option(res.getCharacterEncoding)

  def characterEncoding_=(encoding: Option[String]): Unit = {
    res.setCharacterEncoding(encoding getOrElse null)
  }

  def contentType: Option[String] = Option(res.getContentType)

  def contentType_=(contentType: Option[String]): Unit = {
    res.setContentType(contentType.orNull[String])
  }

  def redirect(uri: String): Unit = {
    res.sendRedirect(uri)
  }

  def outputStream: OutputStream = res.getOutputStream

  def writer: PrintWriter = res.getWriter

  def end(): Unit = {
    res.flushBuffer()
    res.getOutputStream.close()
  }

}
