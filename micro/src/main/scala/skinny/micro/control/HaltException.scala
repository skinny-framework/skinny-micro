package skinny.micro.control

import scala.util.control.NoStackTrace

/**
 * Represents halt operations at skinny.micro code.
 */
case class HaltException(
  status: Option[Int],
  reason: Option[String],
  headers: Map[String, String],
  body: Any)
    extends Throwable
    with NoStackTrace
