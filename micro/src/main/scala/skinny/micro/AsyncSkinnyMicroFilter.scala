package skinny.micro

import javax.servlet.Filter

/**
 * Async skinny.micro filter.
 */
trait AsyncSkinnyMicroFilter
    extends Filter
    with SkinnyMicroFilterBase
    with AsyncFeatures {

}
