package skinny.micro.cookie

/**
 * Cookie options.
 */
case class CookieOptions(
  domain: String = "",
  path: String = "",
  maxAge: Int = -1,
  secure: Boolean = false,
  comment: String = "",
  httpOnly: Boolean = false,
  version: Int = 0,
  encoding: String = "UTF-8")

object CookieOptions {

  val default = CookieOptions()

}
