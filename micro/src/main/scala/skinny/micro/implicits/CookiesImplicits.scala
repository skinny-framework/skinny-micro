package skinny.micro.implicits

import scala.language.implicitConversions

import skinny.micro.context.SkinnyContext
import skinny.micro.cookie.{ Cookie, CookieOptions, SweetCookies }

/**
 * Implicit conversion for Cookie values.
 */
trait CookiesImplicits extends ServletApiImplicits {

  implicit def cookieOptions(implicit ctx: SkinnyContext): CookieOptions = {
    ctx.servletContext.get(Cookie.CookieOptionsKey).orNull.asInstanceOf[CookieOptions]
  }

  def cookies(implicit ctx: SkinnyContext): SweetCookies = {
    ctx.request.get(Cookie.SweetCookiesKey).orNull.asInstanceOf[SweetCookies]
  }

}
