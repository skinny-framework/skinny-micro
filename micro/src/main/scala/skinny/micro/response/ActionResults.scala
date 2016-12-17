package skinny.micro.response

private object ActionResultHelpers {

  def responseStatus(status: Int): ResponseStatus = ResponseStatus(status)

}

import ActionResultHelpers._
import skinny.micro.cookie.Cookie

object Ok extends ActionResult(responseStatus(200), Unit, Map.empty) {
  def apply(
    body: Any = Unit,
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(200),
    body,
    headers,
    contentType,
    charset,
    cookies
  )
}

object Created extends ActionResult(responseStatus(201), Unit, Map.empty) {
  def apply(
    body: Any = Unit,
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(201),
    body,
    headers,
    contentType,
    charset,
    cookies
  )
}

object Accepted extends ActionResult(responseStatus(202), Unit, Map.empty) {
  def apply(
    body: Any = Unit,
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(202),
    body,
    headers,
    contentType,
    charset,
    cookies
  )
}

object NonAuthoritativeInformation extends ActionResult(responseStatus(203), Unit, Map.empty) {
  def apply(
    body: Any = Unit,
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(203),
    body,
    headers,
    contentType,
    charset,
    cookies
  )
}

object NoContent extends ActionResult(responseStatus(204), Unit, Map.empty) {
  def apply(
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(204),
    Unit,
    headers,
    contentType,
    charset,
    cookies
  )
}

object ResetContent extends ActionResult(responseStatus(205), Unit, Map.empty) {
  def apply(
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(205),
    Unit,
    headers,
    contentType,
    charset,
    cookies
  )
}

object PartialContent extends ActionResult(responseStatus(206), Unit, Map.empty) {
  def apply(
    body: Any = Unit,
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(206),
    body,
    headers,
    contentType,
    charset,
    cookies
  )
}

object MultiStatus extends ActionResult(responseStatus(207), Unit, Map.empty) {
  def apply(
    body: Any = Unit,
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(207),
    body,
    headers,
    contentType,
    charset,
    cookies
  )
}

object AlreadyReported extends ActionResult(responseStatus(208), Unit, Map.empty) {
  def apply(
    body: Any = Unit,
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(208),
    body,
    headers,
    contentType,
    charset,
    cookies
  )
}

object IMUsed extends ActionResult(responseStatus(226), Unit, Map.empty) {
  def apply(
    body: Any = Unit,
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(226),
    body,
    headers,
    contentType,
    charset,
    cookies
  )
}

object MultipleChoices extends ActionResult(responseStatus(300), Unit, Map.empty) {
  def apply(
    body: Any = Unit,
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(300),
    body,
    headers,
    contentType,
    charset,
    cookies
  )
}

object MovedPermanently {
  def apply(
    location: String,
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(301),
    Unit,
    Map("Location" -> location) ++ headers,
    contentType,
    charset,
    cookies
  )
}

object Found {
  def apply(
    location: String,
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(302),
    Unit,
    Map("Location" -> location) ++ headers,
    contentType,
    charset,
    cookies
  )
}

object SeeOther {
  def apply(
    location: String,
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(303),
    Unit,
    Map("Location" -> location) ++ headers,
    contentType,
    charset,
    cookies
  )
}

object NotModified extends ActionResult(responseStatus(304), Unit, Map.empty) {
  def apply(
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(304),
    Unit,
    headers,
    contentType,
    charset,
    cookies
  )
}

object UseProxy {
  def apply(
    location: String,
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(305),
    Unit,
    Map("Location" -> location) ++ headers,
    contentType,
    charset,
    cookies
  )
}

object TemporaryRedirect {
  def apply(
    location: String,
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(307),
    Unit,
    Map("Location" -> location) ++ headers,
    contentType,
    charset,
    cookies
  )
}

object PermanentRedirect {
  def apply(
    location: String,
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(308),
    Unit,
    Map("Location" -> location) ++ headers,
    contentType,
    charset,
    cookies
  )
}

object BadRequest extends ActionResult(responseStatus(400), Unit, Map.empty) {
  def apply(
    body: Any = Unit,
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(400),
    body,
    headers,
    contentType,
    charset,
    cookies
  )
}

object Unauthorized extends ActionResult(responseStatus(401), Unit, Map.empty) {
  def apply(
    body: Any = Unit,
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(401),
    body,
    headers,
    contentType,
    charset,
    cookies
  )
}

object PaymentRequired extends ActionResult(responseStatus(402), Unit, Map.empty) {
  def apply(
    body: Any = Unit,
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(402),
    body,
    headers,
    contentType,
    charset,
    cookies
  )
}

object Forbidden extends ActionResult(responseStatus(403), Unit, Map.empty) {
  def apply(
    body: Any = Unit,
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(403),
    body,
    headers,
    contentType,
    charset,
    cookies
  )
}

object NotFound extends ActionResult(responseStatus(404), Unit, Map.empty) {
  def apply(
    body: Any = Unit,
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(404),
    body,
    headers,
    contentType,
    charset,
    cookies
  )
}

object MethodNotAllowed extends ActionResult(responseStatus(405), Unit, Map.empty) {
  def apply(
    body: Any = Unit,
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(405),
    body,
    headers,
    contentType,
    charset,
    cookies
  )
}

object NotAcceptable extends ActionResult(responseStatus(406), Unit, Map.empty) {
  def apply(
    body: Any = Unit,
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(406),
    body,
    headers,
    contentType,
    charset,
    cookies
  )
}

object ProxyAuthenticationRequired extends ActionResult(responseStatus(407), Unit, Map.empty) {
  def apply(
    body: Any = Unit,
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(407),
    body,
    headers,
    contentType,
    charset,
    cookies
  )
}

object RequestTimeout extends ActionResult(responseStatus(408), Unit, Map.empty) {
  def apply(
    body: Any = Unit,
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(408),
    body,
    headers,
    contentType,
    charset,
    cookies
  )
}

object Conflict extends ActionResult(responseStatus(409), Unit, Map.empty) {
  def apply(
    body: Any = Unit,
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(409),
    body,
    headers,
    contentType,
    charset,
    cookies
  )
}

object Gone extends ActionResult(responseStatus(410), Unit, Map.empty) {
  def apply(
    body: Any = Unit,
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(410),
    body,
    headers,
    contentType,
    charset,
    cookies
  )
}

object LengthRequired extends ActionResult(responseStatus(411), Unit, Map.empty) {
  def apply(
    body: Any = Unit,
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(411),
    body,
    headers,
    contentType,
    charset,
    cookies
  )
}

object PreconditionFailed extends ActionResult(responseStatus(412), Unit, Map.empty) {
  def apply(
    body: Any = Unit,
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(412),
    body,
    headers,
    contentType,
    charset,
    cookies
  )
}

object RequestEntityTooLarge extends ActionResult(responseStatus(413), Unit, Map.empty) {
  def apply(
    body: Any = Unit,
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(413),
    body,
    headers,
    contentType,
    charset,
    cookies
  )
}

object RequestURITooLong extends ActionResult(responseStatus(414), Unit, Map.empty) {
  def apply(
    body: Any = Unit,
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(414),
    body,
    headers,
    contentType,
    charset,
    cookies
  )
}

object UnsupportedMediaType extends ActionResult(responseStatus(415), Unit, Map.empty) {
  def apply(
    body: Any = Unit,
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(415),
    body,
    headers,
    contentType,
    charset,
    cookies
  )
}

object RequestedRangeNotSatisfiable extends ActionResult(responseStatus(416), Unit, Map.empty) {
  def apply(
    body: Any = Unit,
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(416),
    body,
    headers,
    contentType,
    charset,
    cookies
  )
}

object ExpectationFailed extends ActionResult(responseStatus(417), Unit, Map.empty) {
  def apply(
    body: Any = Unit,
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(417),
    body,
    headers,
    contentType,
    charset,
    cookies
  )
}

object UnprocessableEntity extends ActionResult(responseStatus(422), Unit, Map.empty) {
  def apply(
    body: Any = Unit,
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(422),
    body,
    headers,
    contentType,
    charset,
    cookies
  )
}

object Locked extends ActionResult(responseStatus(423), Unit, Map.empty) {
  def apply(
    body: Any = Unit,
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(423),
    body,
    headers,
    contentType,
    charset,
    cookies
  )
}

object FailedDependency extends ActionResult(responseStatus(424), Unit, Map.empty) {
  def apply(
    body: Any = Unit,
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(424),
    body,
    headers,
    contentType,
    charset,
    cookies
  )
}

object UpgradeRequired extends ActionResult(responseStatus(426), Unit, Map.empty) {
  def apply(
    body: Any = Unit,
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(426),
    body,
    headers,
    contentType,
    charset,
    cookies
  )
}

object PreconditionRequired extends ActionResult(responseStatus(428), Unit, Map.empty) {
  def apply(
    body: Any = Unit,
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(428),
    body,
    headers,
    contentType,
    charset,
    cookies
  )
}

object TooManyRequests extends ActionResult(responseStatus(429), Unit, Map.empty) {
  def apply(
    body: Any = Unit,
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(429),
    body,
    headers,
    contentType,
    charset,
    cookies
  )
}

object RequestHeaderFieldsTooLarge extends ActionResult(responseStatus(431), Unit, Map.empty) {
  def apply(
    body: Any = Unit,
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(431),
    body,
    headers,
    contentType,
    charset,
    cookies
  )
}

object InternalServerError extends ActionResult(responseStatus(500), Unit, Map.empty) {
  def apply(
    body: Any = Unit,
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(500),
    body,
    headers,
    contentType,
    charset,
    cookies
  )
}

object NotImplemented extends ActionResult(responseStatus(501), Unit, Map.empty) {
  def apply(
    body: Any = Unit,
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(501),
    body,
    headers,
    contentType,
    charset,
    cookies
  )
}

object BadGateway extends ActionResult(responseStatus(502), Unit, Map.empty) {
  def apply(
    body: Any = Unit,
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(502),
    body,
    headers,
    contentType,
    charset,
    cookies
  )
}

object ServiceUnavailable extends ActionResult(responseStatus(503), Unit, Map.empty) {
  def apply(
    body: Any = Unit,
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(503),
    body,
    headers,
    contentType,
    charset,
    cookies
  )
}

object GatewayTimeout extends ActionResult(responseStatus(504), Unit, Map.empty) {
  def apply(
    body: Any = Unit,
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(504),
    body,
    headers,
    contentType,
    charset,
    cookies
  )
}

object HTTPVersionNotSupported extends ActionResult(responseStatus(505), Unit, Map.empty) {
  def apply(
    body: Any = Unit,
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(505),
    body,
    headers,
    contentType,
    charset,
    cookies
  )
}

object VariantAlsoNegotiates extends ActionResult(responseStatus(506), Unit, Map.empty) {
  def apply(
    body: Any = Unit,
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(506),
    body,
    headers,
    contentType,
    charset,
    cookies
  )
}

object InsufficientStorage extends ActionResult(responseStatus(507), Unit, Map.empty) {
  def apply(
    body: Any = Unit,
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(507),
    body,
    headers,
    contentType,
    charset,
    cookies
  )
}

object LoopDetected extends ActionResult(responseStatus(508), Unit, Map.empty) {
  def apply(
    body: Any = Unit,
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(508),
    body,
    headers,
    contentType,
    charset,
    cookies
  )
}

object NotExtended extends ActionResult(responseStatus(510), Unit, Map.empty) {
  def apply(
    body: Any = Unit,
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(510),
    body,
    headers,
    contentType,
    charset,
    cookies
  )
}

object NetworkAuthenticationRequired extends ActionResult(responseStatus(511), Unit, Map.empty) {
  def apply(
    body: Any = Unit,
    headers: Map[String, String] = Map.empty,
    contentType: Option[String] = None,
    charset: Option[String] = None,
    cookies: Seq[Cookie] = Seq.empty
  ) = ActionResult(
    responseStatus(511),
    body,
    headers,
    contentType,
    charset,
    cookies
  )
}
