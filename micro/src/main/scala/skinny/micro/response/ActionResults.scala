package skinny.micro.response

private object ActionResultHelpers {

  def responseStatus(status: Int): ResponseStatus = ResponseStatus(status)

}

import ActionResultHelpers._

object Ok {
  def apply(body: Any = Unit, headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(200), body, headers)
}

object Created {
  def apply(body: Any = Unit, headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(201), body, headers)
}

object Accepted {
  def apply(body: Any = Unit, headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(202), body, headers)
}

object NonAuthoritativeInformation {
  def apply(body: Any = Unit, headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(203), body, headers)
}

object NoContent {
  def apply(headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(204), Unit, headers)
}

object ResetContent {
  def apply(headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(205), Unit, headers)
}

object PartialContent {
  def apply(body: Any = Unit, headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(206), body, headers)
}

object MultiStatus {
  def apply(body: Any = Unit, headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(207), body, headers)
}

object AlreadyReported {
  def apply(body: Any = Unit, headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(208), body, headers)
}

object IMUsed {
  def apply(body: Any = Unit, headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(226), body, headers)
}

object MultipleChoices {
  def apply(body: Any = Unit, headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(300), body, headers)
}

object MovedPermanently {
  def apply(location: String, headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(301), Unit, Map("Location" -> location) ++ headers)
}

object Found {
  def apply(location: String, headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(302), Unit, Map("Location" -> location) ++ headers)
}

object SeeOther {
  def apply(location: String, headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(303), Unit, Map("Location" -> location) ++ headers)
}

object NotModified {
  def apply(headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(304), Unit, headers)
}

object UseProxy {
  def apply(location: String, headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(305), Unit, Map("Location" -> location) ++ headers)
}

object TemporaryRedirect {
  def apply(location: String, headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(307), Unit, Map("Location" -> location) ++ headers)
}

object PermanentRedirect {
  def apply(location: String, headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(308), Unit, Map("Location" -> location) ++ headers)
}

object BadRequest {
  def apply(body: Any = Unit, headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(400), body, headers)
}

object Unauthorized {
  def apply(body: Any = Unit, headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(401), body, headers)
}

object PaymentRequired {
  def apply(body: Any = Unit, headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(402), body, headers)
}

object Forbidden {
  def apply(body: Any = Unit, headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(403), body, headers)
}

object NotFound {
  def apply(body: Any = Unit, headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(404), body, headers)
}

object MethodNotAllowed {
  def apply(body: Any = Unit, headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(405), body, headers)
}

object NotAcceptable {
  def apply(body: Any = Unit, headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(406), body, headers)
}

object ProxyAuthenticationRequired {
  def apply(body: Any = Unit, headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(407), body, headers)
}

object RequestTimeout {
  def apply(body: Any = Unit, headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(408), body, headers)
}

object Conflict {
  def apply(body: Any = Unit, headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(409), body, headers)
}

object Gone {
  def apply(body: Any = Unit, headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(410), body, headers)
}

object LengthRequired {
  def apply(body: Any = Unit, headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(411), body, headers)
}

object PreconditionFailed {
  def apply(body: Any = Unit, headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(412), body, headers)
}

object RequestEntityTooLarge {
  def apply(body: Any = Unit, headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(413), body, headers)
}

object RequestURITooLong {
  def apply(body: Any = Unit, headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(414), body, headers)
}

object UnsupportedMediaType {
  def apply(body: Any = Unit, headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(415), body, headers)
}

object RequestedRangeNotSatisfiable {
  def apply(body: Any = Unit, headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(416), body, headers)
}

object ExpectationFailed {
  def apply(body: Any = Unit, headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(417), body, headers)
}

object UnprocessableEntity {
  def apply(body: Any = Unit, headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(422), body, headers)
}

object Locked {
  def apply(body: Any = Unit, headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(423), body, headers)
}

object FailedDependency {
  def apply(body: Any = Unit, headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(424), body, headers)
}

object UpgradeRequired {
  def apply(body: Any = Unit, headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(426), body, headers)
}

object PreconditionRequired {
  def apply(body: Any = Unit, headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(428), body, headers)
}

object TooManyRequests {
  def apply(body: Any = Unit, headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(429), body, headers)
}

object RequestHeaderFieldsTooLarge {
  def apply(body: Any = Unit, headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(431), body, headers)
}

object InternalServerError {
  def apply(body: Any = Unit, headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(500), body, headers)
}

object NotImplemented {
  def apply(body: Any = Unit, headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(501), body, headers)
}

object BadGateway {
  def apply(body: Any = Unit, headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(502), body, headers)
}

object ServiceUnavailable {
  def apply(body: Any = Unit, headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(503), body, headers)
}

object GatewayTimeout {
  def apply(body: Any = Unit, headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(504), body, headers)
}

object HTTPVersionNotSupported {
  def apply(body: Any = Unit, headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(505), body, headers)
}

object VariantAlsoNegotiates {
  def apply(body: Any = Unit, headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(506), body, headers)
}

object InsufficientStorage {
  def apply(body: Any = Unit, headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(507), body, headers)
}

object LoopDetected {
  def apply(body: Any = Unit, headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(508), body, headers)
}

object NotExtended {
  def apply(body: Any = Unit, headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(510), body, headers)
}

object NetworkAuthenticationRequired {
  def apply(body: Any = Unit, headers: Map[String, String] = Map.empty) =
    ActionResult(responseStatus(511), body, headers)
}
