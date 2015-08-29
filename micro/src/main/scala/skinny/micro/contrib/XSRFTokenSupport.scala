package skinny.micro.contrib

import skinny.micro.base.BeforeAfterDsl
import skinny.micro.context.SkinnyContext
import skinny.micro.contrib.csrf.CSRFTokenGenerator
import skinny.micro.{ RouteTransformer, SkinnyMicroBase }

/**
 * Provides cross-site request forgery protection.
 *
 * If a request is determined to be forged, the `handleForgery()` hook is invoked.
 * Otherwise, a token for the next request is prepared with `prepareCsrfToken`.
 */
trait XSRFTokenSupport { this: SkinnyMicroBase with BeforeAfterDsl =>

  import XSRFTokenSupport._

  /**
   * The key used to store the token on the session, as well as the parameter
   * of the request.
   */
  def xsrfKey: String = DefaultKey

  /**
   * Returns the token from the session.
   */
  def xsrfToken(implicit ctx: SkinnyContext): String =
    ctx.request.getSession.getAttribute(xsrfKey).asInstanceOf[String]

  def xsrfGuard(only: RouteTransformer*): Unit = {
    before((only.toSeq ++ Seq[RouteTransformer](isForged)): _*) { handleForgery() }
  }

  before() { prepareXsrfToken() }

  /**
   * Tests whether a request with a unsafe method is a potential cross-site
   * forgery.
   *
   * @return true if the request is an unsafe method (POST, PUT, DELETE, TRACE,
   * CONNECT, PATCH) and the request parameter at `xsrfKey` does not match
   * the session key of the same name.
   */
  protected def isForged: Boolean = {
    implicit val ctx = context
    !request.requestMethod.isSafe &&
      session.get(xsrfKey) != params.get(xsrfKey) &&
      !HeaderNames.map(request.headers.get).contains(session.get(xsrfKey))
  }

  /**
   * Take an action when a forgery is detected. The default action
   * halts further request processing and returns a 403 HTTP status code.
   */
  protected def handleForgery(): Unit = {
    halt(403, "Request tampering detected!")
  }

  /**
   * Prepares a XSRF token.  The default implementation uses `GenerateId`
   * and stores it on the session.
   */
  protected def prepareXsrfToken(): Unit = {
    implicit val ctx = context
    session.getOrElseUpdate(xsrfKey, CSRFTokenGenerator.apply())
    val cookieOpt = cookies.get(CookieKey)
    if (cookieOpt.isEmpty || cookieOpt != session.get(xsrfKey)) {
      cookies += CookieKey -> xsrfToken
    }
  }
}

object XSRFTokenSupport {

  val DefaultKey = "skinny.micro.XSRFTokenSupport.key"

  val HeaderNames = Vector("X-XSRF-TOKEN")

  val CookieKey = "XSRF-TOKEN"

}
