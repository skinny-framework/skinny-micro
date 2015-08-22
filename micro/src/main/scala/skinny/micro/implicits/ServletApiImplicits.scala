package skinny.micro.implicits

import scala.language.implicitConversions

import javax.servlet.ServletContext
import javax.servlet.http.{ HttpServletRequest, HttpServletResponse, HttpSession }

import skinny.micro.context.RichServletContext
import skinny.micro.request.{ RichHttpServletSession, RichRequest }
import skinny.micro.response.RichResponse

/**
 * Implicit conversions to enrich Servlet APIs.
 */
trait ServletApiImplicits {

  implicit def enrichRequest(request: HttpServletRequest): RichRequest =
    RichRequest(request)

  implicit def enrichResponse(response: HttpServletResponse): RichResponse =
    RichResponse(response)

  implicit def enrichSession(session: HttpSession): RichHttpServletSession =
    RichHttpServletSession(session)

  implicit def enrichServletContext(servletContext: ServletContext): RichServletContext =
    RichServletContext(servletContext)

}

object ServletApiImplicits
  extends ServletApiImplicits
