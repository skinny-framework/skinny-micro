package org.scalatra

import javax.servlet.http.{ HttpServletResponse, HttpServletRequest }
import servlet.ServletApiImplicits
import util.{ MapWithIndifferentAccess, MultiMapHeadView }
import javax.servlet.ServletContext
import annotation.implicitNotFound

class ScalatraParams(protected val multiMap: Map[String, Seq[String]]) extends MultiMapHeadView[String, String] with MapWithIndifferentAccess[String]

object ScalatraContext {
  private class StableValuesContext(implicit val request: HttpServletRequest, val response: HttpServletResponse, val servletContext: ServletContext) extends ScalatraContext
}

trait ScalatraContext extends ServletApiImplicits with SessionSupport with CookieContext {
  import ScalatraContext.StableValuesContext
  implicit def request: HttpServletRequest
  implicit def response: HttpServletResponse
  def servletContext: ServletContext

  /**
   * Gets the content type of the current response.
   */
  def contentType: String = response.contentType getOrElse null

  /**
   * Gets the status code of the current response.
   */
  def status: Int = response.status.code

  /**
   * Sets the content type of the current response.
   */
  def contentType_=(contentType: String): Unit = {
    response.contentType = Option(contentType)
  }

  @deprecated("Use status_=(Int) instead", "2.1.0")
  def status(code: Int): Unit = { status_=(code) }

  /**
   * Sets the status code of the current response.
   */
  def status_=(code: Int): Unit = { response.status = ResponseStatus(code) }

  /**
   * Explicitly sets the request-scoped format.  This takes precedence over
   * whatever was inferred from the request.
   */
  def format_=(formatValue: Symbol): Unit = {
    request(ApiFormats.FormatKey) = formatValue.name
  }

  /**
   * Explicitly sets the request-scoped format.  This takes precedence over
   * whatever was inferred from the request.
   */
  def format_=(formatValue: String): Unit = {
    request(ApiFormats.FormatKey) = formatValue
  }

  protected[this] implicit def scalatraContext: ScalatraContext = new StableValuesContext()(request, response, servletContext)
}
