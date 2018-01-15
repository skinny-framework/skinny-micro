package skinny.micro

import javax.servlet.http.HttpServlet

/**
 * Async skinny.micro servlet.
 */
trait TypedAsyncSkinnyMicroServlet
  extends HttpServlet
  with SkinnyMicroServletBase
  with TypedAsyncFeatures {

}
