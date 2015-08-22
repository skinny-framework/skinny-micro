package skinny.micro

import skinny.micro.base.{ MainThreadLocalEverywhere, BeforeAfterDsl }
import skinny.micro.routing.RoutingDsl

/**
 * Built-in features in SkinnyMicroFilter/SkinnyMicroServlet.
 * These traits should not be mixed in SkinnyMicroBase.
 */
trait ThreadLocalFeatures
    extends MainThreadLocalEverywhere
    with RoutingDsl
    with BeforeAfterDsl { self: SkinnyMicroBase =>

}
