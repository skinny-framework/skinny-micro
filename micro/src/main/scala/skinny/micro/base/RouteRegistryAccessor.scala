package skinny.micro.base

import skinny.micro.routing.RouteRegistry

/**
 * RouteRegistry.
 */
trait RouteRegistryAccessor {

  /**
   * The routes registered in this kernel.
   */
  lazy val routes: RouteRegistry = RouteRegistry.getInstance(this)

}
