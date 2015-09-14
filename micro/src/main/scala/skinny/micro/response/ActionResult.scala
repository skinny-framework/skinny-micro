package skinny.micro.response

import skinny.micro.cookie.Cookie

/**
 * skinny.micro's action result.
 */
case class ActionResult(
  status: ResponseStatus,
  body: Any,
  headers: Map[String, String],
  cookies: Seq[Cookie] = Seq.empty)
