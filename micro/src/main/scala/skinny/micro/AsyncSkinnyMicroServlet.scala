package skinny.micro

import javax.servlet.http.HttpServlet

/**
 * Async skinny.micro servlet.
 */
trait AsyncSkinnyMicroServlet
    extends HttpServlet
    with SkinnyMicroServletBase
    with AsyncFeatures {

}
