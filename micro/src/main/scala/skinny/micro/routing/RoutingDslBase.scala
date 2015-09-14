package skinny.micro.routing

import skinny.micro._
import skinny.micro.base.{ ServletContextAccessor, UnstableAccessValidationConfig, SkinnyContextInitializer, RouteRegistryAccessor }
import skinny.micro.constant._
import skinny.micro.context.SkinnyContext
import skinny.micro.control.HaltPassControl
import skinny.micro.implicits.ServletApiImplicits

/**
 * The core SkinnyMicro DSL.
 */
trait RoutingDslBase
    extends HaltPassControl
    with RouteRegistryAccessor
    with SkinnyContextInitializer
    with UnstableAccessValidationConfig
    with ServletContextAccessor
    with ServletApiImplicits {

  /**
   * The base path for URL generation
   */
  protected def routeBasePath(implicit ctx: SkinnyContext): String

  /**
   * Defines a block to run if no matching routes are found, or if all
   * matching routes pass.
   */
  def notFound(block: => Any): Unit

  /**
   * Defines a block to run if matching routes are found only for other
   * methods.  The set of matching methods is passed to the block.
   */
  def methodNotAllowed(block: Set[HttpMethod] => Any): Unit

  /**
   * Defines an error handler for exceptions thrown in either the before
   * block or a route action.
   *
   * If the error handler does not match, the result falls through to the
   * previously defined error handler.  The default error handler simply
   * rethrows the exception.
   *
   * The error handler is run before the after filters, and the result is
   * rendered like a standard response.  It is the error handler's
   * responsibility to set any appropriate status code.
   */
  def error(handler: ErrorHandler): Unit

}
