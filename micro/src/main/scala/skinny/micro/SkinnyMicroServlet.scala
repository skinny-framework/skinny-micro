package skinny.micro

import javax.servlet.http._

/**
 * An implementation of the SkinnyMicro DSL in a servlet.  This is the recommended
 * base trait for most SkinnyMicro applications.  Use a servlet if:
 *
 * $ - your SkinnyMicro routes run in a subcontext of your web application.
 * $ - you want SkinnyMicro to have complete control of unmatched requests.
 * $ - you think you want a filter just for serving static content with the
 *     default servlet; SkinnyMicroServlet can do this too
 * $ - you don't know the difference
 *
 * @see SkinnyMicroFilter
 */
trait SkinnyMicroServlet
    extends HttpServlet
    with SkinnyMicroServletBase
    with ThreadLocalFeatures {

}
