package skinny.micro.cookie

import javax.servlet.http.{ HttpServletRequest, HttpServletResponse }

import skinny.micro.implicits.ServletApiImplicits

import scala.collection.mutable

/**
 * Extended cookie object.
 */
class SweetCookies(
  private[this] val request: HttpServletRequest,
  private[this] val response: HttpServletResponse) extends ServletApiImplicits {

  private[this] lazy val cookies = mutable.HashMap[String, String]() ++ request.cookies

  def get(key: String): Option[String] = cookies.get(key)

  def apply(key: String): String = {
    cookies.get(key) getOrElse (throw new Exception("No cookie could be found for the specified key"))
  }

  def update(name: String, value: String)(
    implicit
    cookieOptions: CookieOptions = CookieOptions.default): Cookie = {
    cookies += name -> value
    addCookie(name, value, cookieOptions)
  }

  def set(name: String, value: String)(implicit cookieOptions: CookieOptions = CookieOptions.default): Cookie = {
    this.update(name, value)(cookieOptions)
  }

  def delete(name: String)(implicit cookieOptions: CookieOptions = CookieOptions.default): Unit = {
    cookies -= name
    addCookie(name, "", cookieOptions.copy(maxAge = 0))
  }

  def +=(keyValuePair: (String, String))(implicit cookieOptions: CookieOptions = CookieOptions.default): Unit = {
    this.update(keyValuePair._1, keyValuePair._2)(cookieOptions)
  }

  def +=(cookie: Cookie): Unit = {
    this.update(cookie.name, cookie.value)(cookie.options)
  }

  def ++=(cookies: Seq[Cookie]): Unit = {
    cookies.foreach { cookie =>
      this.update(cookie.name, cookie.value)(cookie.options)
    }
  }

  def -=(key: String)(implicit cookieOptions: CookieOptions = CookieOptions.default): Unit = {
    delete(key)(cookieOptions)
  }

  private def addCookie(name: String, value: String, options: CookieOptions): Cookie = {
    val cookie = new Cookie(name, value)(options)
    response.addCookie(cookie)
    cookie
  }

}
