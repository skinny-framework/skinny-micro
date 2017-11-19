package skinny.micro.routing

import javax.servlet.http.HttpServletRequest

import skinny.micro._
import skinny.micro.constant._
import skinny.micro.context.SkinnyContext

import scala.concurrent.Future

/**
 * The core SkinnyMicro DSL.
 *
 * The SkinnyMicro DSL core methods take a list of [[skinny.micro.routing.RouteMatcher]] and a block as the action body.
 * The return value of the block is rendered through the pipeline and sent to the client as the response body.
 */
trait TypedAsyncRoutingDsl extends RoutingDslBase {

  def get(transformers: RouteTransformer*)(action: (Context) => Future[ActionResult]): Route = {
    addRoute(Get, transformers, (ctx) => AsyncResult.withFuture(action(ctx))(ctx))
  }

  def post(transformers: RouteTransformer*)(action: (Context) => Future[ActionResult]): Route = {
    addRoute(Post, transformers, (ctx) => AsyncResult.withFuture(action(ctx))(ctx))
  }

  def put(transformers: RouteTransformer*)(action: (Context) => Future[ActionResult]): Route = {
    addRoute(Put, transformers, (ctx) => AsyncResult.withFuture(action(ctx))(ctx))
  }

  def delete(transformers: RouteTransformer*)(action: (Context) => Future[ActionResult]): Route = {
    addRoute(Delete, transformers, (ctx) => AsyncResult.withFuture(action(ctx))(ctx))
  }

  def options(transformers: RouteTransformer*)(action: (Context) => Future[ActionResult]): Route = {
    addRoute(Options, transformers, (ctx) => AsyncResult.withFuture(action(ctx))(ctx))
  }

  def head(transformers: RouteTransformer*)(action: (Context) => Future[ActionResult]): Route = {
    addRoute(Head, transformers, (ctx) => AsyncResult.withFuture(action(ctx))(ctx))
  }

  def patch(transformers: RouteTransformer*)(action: (Context) => Future[ActionResult]): Route = {
    addRoute(Patch, transformers, (ctx) => AsyncResult.withFuture(action(ctx))(ctx))
  }

  /**
   * Prepends a new route for the given HTTP method.
   *
   * Can be overriden so that subtraits can use their own logic.
   * Possible examples:
   * $ - restricting protocols
   * $ - namespace routes based on class name
   * $ - raising errors on overlapping entries.
   *
   * This is the method invoked by get(), post() etc.
   *
   * @see skinny.micro.SkinnyMicroKernel#removeRoute
   */
  protected def addRoute(method: HttpMethod, transformers: Seq[RouteTransformer], action: (Context) => AsyncResult): Route = {
    val route: Route = {
      val r = Route(transformers, () => action.apply(context), (req: HttpServletRequest) => routeBasePath(
        SkinnyContext.buildWithoutResponse(req, servletContext,
          UnstableAccessValidation(unstableAccessValidationEnabled, useMostlyStableHttpSession))))
      r.copy(metadata = r.metadata.updated(Handler.RouteMetadataHttpMethodCacheKey, method))
    }
    routes.prependRoute(method, route)
    route
  }

  private[this] def addStatusRoute(codes: Range, action: (Context) => Future[ActionResult]): Unit = {
    val route = Route(Seq.empty, () => action, (req: HttpServletRequest) => routeBasePath(skinnyContext(servletContext)))
    routes.addStatusRoute(codes, route)
  }

  /**
   * Error handler for HTTP response status code range. You can intercept every response code previously
   * specified with #status or even generic 404 error.
   * {{{
   *   trap(403) { implicit ctx =>
   *    "You are not authorized"
   *   }
   * }* }}}
   * }}
   */
  def trap(codes: Range)(block: (Context) => Future[ActionResult]): Unit = {
    addStatusRoute(codes, block)
  }

  /**
   * @see error
   */
  def trap(code: Int)(block: (Context) => Future[ActionResult]): Unit = {
    trap(Range(code, code + 1))(block)
  }

}
