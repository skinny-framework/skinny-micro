package skinny.micro.response

/**
 * skinny.micro's action result.
 */
case class ActionResult(
  status: ResponseStatus,
  body: Any,
  headers: Map[String, String])
