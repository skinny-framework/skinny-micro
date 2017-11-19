package skinny.micro.routing

import javax.servlet.http.HttpServletRequest

import skinny.micro._
import skinny.micro.constant._
import skinny.micro.context.SkinnyContext

/**
 * The core SkinnyMicro DSL.
 *
 * The SkinnyMicro DSL core methods take a list of [[skinny.micro.routing.RouteMatcher]] and a block as the action body.
 * The return value of the block is rendered through the pipeline and sent to the client as the response body.
 */
trait RoutingDsl extends RoutingDslBase {

  def get(transformers: RouteTransformer*)(action: => Any): Route = addRoute(Get, transformers, action)

  def post(transformers: RouteTransformer*)(action: => Any): Route = addRoute(Post, transformers, action)

  def put(transformers: RouteTransformer*)(action: => Any): Route = addRoute(Put, transformers, action)

  def delete(transformers: RouteTransformer*)(action: => Any): Route = addRoute(Delete, transformers, action)

  def options(transformers: RouteTransformer*)(action: => Any): Route = addRoute(Options, transformers, action)

  def head(transformers: RouteTransformer*)(action: => Any): Route = addRoute(Head, transformers, action)

  def patch(transformers: RouteTransformer*)(action: => Any): Route = addRoute(Patch, transformers, action)

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
  protected def addRoute(method: HttpMethod, transformers: Seq[RouteTransformer], action: => Any): Route = {
    val route: Route = {
      val r = Route(transformers, () => action, (req: HttpServletRequest) => routeBasePath(
        SkinnyContext.buildWithoutResponse(req, servletContext,
          UnstableAccessValidation(unstableAccessValidationEnabled, useMostlyStableHttpSession))))
      r.copy(metadata = r.metadata.updated(Handler.RouteMetadataHttpMethodCacheKey, method))
    }
    routes.prependRoute(method, route)
    route
  }

  private[this] def addStatusRoute(codes: Range, action: => Any): Unit = {
    val route = Route(Seq.empty, () => action, (req: HttpServletRequest) => routeBasePath(skinnyContext(servletContext)))
    routes.addStatusRoute(codes, route)
  }

  /**
   * Error handler for HTTP response status code range. You can intercept every response code previously
   * specified with #status or even generic 404 error.
   * {{{
   *   trap(403) {
   *    "You are not authorized"
   *   }
   * }* }}}
   * }}
   */
  def trap(codes: Range)(block: => Any): Unit = {
    addStatusRoute(codes, block)
  }

  /**
   * @see trap
   */
  def trap(code: Int)(block: => Any): Unit = {
    trap(Range(code, code + 1))(block)
  }

}
