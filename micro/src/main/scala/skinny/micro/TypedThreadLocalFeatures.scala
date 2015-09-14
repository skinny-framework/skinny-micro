package skinny.micro

import skinny.micro.base.{ BeforeAfterDsl, MainThreadLocalEverywhere }
import skinny.micro.routing.TypedRoutingDsl

/**
 * Built-in features in SkinnyMicroFilter/SkinnyMicroServlet.
 * These traits should not be mixed in SkinnyMicroBase.
 */
trait TypedThreadLocalFeatures
    extends MainThreadLocalEverywhere
    with TypedRoutingDsl
    with BeforeAfterDsl { self: SkinnyMicroBase =>

}
