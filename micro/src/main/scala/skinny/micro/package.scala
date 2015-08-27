package skinny

import skinny.micro.control.HaltPassControl
import skinny.micro.data.{ MapWithIndifferentAccess, MultiMapHeadView, MultiMap }
import skinny.micro.routing.Route
import skinny.util.LoanPattern.Closable

import scala.language.implicitConversions

package object micro
    extends HaltPassControl // make halt and pass visible to helpers outside the DSL
    //  with DefaultValues // make defaults visible
    {

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

  type AsyncAction = (Context) => Any

  type Action = () => Any

  type ErrorHandler = PartialFunction[Throwable, Any]

  type ContentTypeInferrer = PartialFunction[Any, String]

  type RenderPipeline = PartialFunction[Any, Any]

  val EnvironmentKey = "skinny.micro.environment"

  val MultiParamsKey = "skinny.micro.MultiParams"

  type Context = skinny.micro.context.SkinnyContext

  type AppBase = skinny.micro.SkinnyMicroBase

  type SingleApp = skinny.micro.SkinnyMicroServlet

  type WebApp = skinny.micro.SkinnyMicroFilter

  type AsyncSingleApp = skinny.micro.AsyncSkinnyMicroServlet

  type AsyncWebApp = skinny.micro.AsyncSkinnyMicroFilter

  def using[R <: Closable, A](resource: R)(f: R => A): A = {
    skinny.util.LoanPattern.using[R, A](resource)(f)
  }

}
