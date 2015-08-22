package skinny.micro.routing

import skinny.micro.{ MultiParams, Action }

/**
 * An action and the multi-map of route parameters to invoke it with.
 */
case class MatchedRoute(
  action: Action,
  multiParams: MultiParams)
