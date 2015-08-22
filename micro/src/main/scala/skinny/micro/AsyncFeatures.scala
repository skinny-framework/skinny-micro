package skinny.micro

import skinny.micro.async.{ AsyncBeforeAfterDsl, AsyncSupport }
import skinny.micro.routing.AsyncRoutingDsl

/**
 * Built-in features in SkinnyMicroFilter/SkinnyMicroServlet.
 * These traits should not be mixed in SkinnyMicroBase.
 */
trait AsyncFeatures
    extends AsyncSupport
    with AsyncRoutingDsl
    with AsyncBeforeAfterDsl { self: SkinnyMicroBase =>

}
