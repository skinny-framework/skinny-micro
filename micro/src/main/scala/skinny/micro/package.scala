package skinny

import scala.language.implicitConversions

import skinny.micro.control.HaltPassControl
import skinny.micro.data.{ MapWithIndifferentAccess, MultiMapHeadView, MultiMap }
import skinny.micro.routing.Route
import skinny.util.LoanPattern.Closable

package object micro
    extends HaltPassControl // make halt and pass visible to helpers outside the DSL
    //  with DefaultValues // make defaults visible
    {

  def using[R <: Closable, A](resource: R)(f: R => A): A = {
    skinny.util.LoanPattern.using[R, A](resource)(f)
  }

  object RouteTransformer {

    implicit def fn2transformer(fn: Route => Route): RouteTransformer = new RouteTransformer {
      override def apply(route: Route): Route = fn(route)
    }
  }

  trait RouteTransformer {
    def apply(route: Route): Route
  }

  type MultiParams = MultiMap

  type Params = MultiMapHeadView[String, String] with MapWithIndifferentAccess[String]

  type Cookie = skinny.micro.cookie.Cookie
  val Cookie = skinny.micro.cookie.Cookie

  type CookieOptions = skinny.micro.cookie.CookieOptions
  val CookieOptions = skinny.micro.cookie.CookieOptions

  type AsyncAction = (Context) => Any

  type Action = () => Any

  type ErrorHandler = PartialFunction[Throwable, Any]

  type ContentTypeInferrer = PartialFunction[Any, String]

  type RenderPipeline = PartialFunction[Any, Any]

  val EnvironmentKey = "skinny.micro.environment"

  val MultiParamsKey = "skinny.micro.MultiParams"

  type Context = skinny.micro.context.SkinnyContext

  // ------------------
  // base traits

  type AppBase = skinny.micro.SkinnyMicroBase

  type SingleApp = skinny.micro.SkinnyMicroServlet
  type TypedSingleApp = skinny.micro.TypedSkinnyMicroServlet

  type WebApp = skinny.micro.SkinnyMicroFilter
  type TypedWebApp = skinny.micro.TypedSkinnyMicroFilter

  type AsyncSingleApp = skinny.micro.AsyncSkinnyMicroServlet
  type TypedAsyncSingleApp = skinny.micro.TypedAsyncSkinnyMicroServlet

  type AsyncWebApp = skinny.micro.AsyncSkinnyMicroFilter
  type TypedAsyncWebApp = skinny.micro.TypedAsyncSkinnyMicroFilter

  // ------------------
  // action results

  type ActionResult = skinny.micro.response.ActionResult
  val ActionResult = skinny.micro.response.ActionResult

  type ResponseStatus = skinny.micro.response.ResponseStatus
  val ResponseStatus = skinny.micro.response.ResponseStatus

  type AsyncResult = skinny.micro.async.AsyncResult
  val AsyncResult = skinny.micro.async.AsyncResult

  val Ok = skinny.micro.response.Ok
  val Created = skinny.micro.response.Created
  val Accepted = skinny.micro.response.Accepted
  val NonAuthoritativeInformation = skinny.micro.response.NonAuthoritativeInformation
  val NoContent = skinny.micro.response.NoContent
  val ResetContent = skinny.micro.response.ResetContent
  val PartialContent = skinny.micro.response.PartialContent
  val MultiStatus = skinny.micro.response.MultiStatus
  val AlreadyReported = skinny.micro.response.AlreadyReported
  val IMUsed = skinny.micro.response.IMUsed
  val MultipleChoices = skinny.micro.response.MultipleChoices
  val MovedPermanently = skinny.micro.response.MovedPermanently
  val Found = skinny.micro.response.Found
  val SeeOther = skinny.micro.response.SeeOther
  val NotModified = skinny.micro.response.NotModified
  val UseProxy = skinny.micro.response.UseProxy
  val TemporaryRedirect = skinny.micro.response.TemporaryRedirect
  val PermanentRedirect = skinny.micro.response.PermanentRedirect
  val BadRequest = skinny.micro.response.BadRequest
  val Unauthorized = skinny.micro.response.Unauthorized
  val PaymentRequired = skinny.micro.response.PaymentRequired
  val Forbidden = skinny.micro.response.Forbidden
  val NotFound = skinny.micro.response.NotFound
  val MethodNotAllowed = skinny.micro.response.MethodNotAllowed
  val NotAcceptable = skinny.micro.response.NotAcceptable
  val ProxyAuthenticationRequired = skinny.micro.response.ProxyAuthenticationRequired
  val RequestTimeout = skinny.micro.response.RequestTimeout
  val Conflict = skinny.micro.response.Conflict
  val Gone = skinny.micro.response.Gone
  val LengthRequired = skinny.micro.response.LengthRequired
  val PreconditionFailed = skinny.micro.response.PreconditionFailed
  val RequestEntityTooLarge = skinny.micro.response.RequestEntityTooLarge
  val RequestURITooLong = skinny.micro.response.RequestURITooLong
  val UnsupportedMediaType = skinny.micro.response.UnsupportedMediaType
  val RequestedRangeNotSatisfiable = skinny.micro.response.RequestedRangeNotSatisfiable
  val ExpectationFailed = skinny.micro.response.ExpectationFailed
  val UnprocessableEntity = skinny.micro.response.UnprocessableEntity
  val Locked = skinny.micro.response.Locked
  val FailedDependency = skinny.micro.response.FailedDependency
  val UpgradeRequired = skinny.micro.response.UpgradeRequired
  val PreconditionRequired = skinny.micro.response.PreconditionRequired
  val TooManyRequests = skinny.micro.response.TooManyRequests
  val RequestHeaderFieldsTooLarge = skinny.micro.response.RequestHeaderFieldsTooLarge
  val InternalServerError = skinny.micro.response.InternalServerError
  val NotImplemented = skinny.micro.response.NotImplemented
  val BadGateway = skinny.micro.response.BadGateway
  val ServiceUnavailable = skinny.micro.response.ServiceUnavailable
  val GatewayTimeout = skinny.micro.response.GatewayTimeout
  val HTTPVersionNotSupported = skinny.micro.response.HTTPVersionNotSupported
  val VariantAlsoNegotiates = skinny.micro.response.VariantAlsoNegotiates
  val InsufficientStorage = skinny.micro.response.InsufficientStorage
  val LoopDetected = skinny.micro.response.LoopDetected
  val NotExtended = skinny.micro.response.NotExtended
  val NetworkAuthenticationRequired = skinny.micro.response.NetworkAuthenticationRequired

}
