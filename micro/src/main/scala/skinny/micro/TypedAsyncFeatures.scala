package skinny.micro

import skinny.micro.async.{ AsyncBeforeAfterDsl, AsyncSupport }
import skinny.micro.routing.TypedAsyncRoutingDsl

/**
 * Built-in features in SkinnyMicroFilter/SkinnyMicroServlet.
 * These traits should not be mixed in SkinnyMicroBase.
 */
trait TypedAsyncFeatures
    extends AsyncSupport
    with TypedAsyncRoutingDsl
    with AsyncBeforeAfterDsl { self: SkinnyMicroBase =>

}
