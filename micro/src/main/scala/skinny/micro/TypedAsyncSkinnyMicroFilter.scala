package skinny.micro

import javax.servlet.Filter

/**
 * Async skinny.micro filter.
 */
trait TypedAsyncSkinnyMicroFilter
    extends Filter
    with SkinnyMicroFilterBase
    with TypedAsyncFeatures {

}
