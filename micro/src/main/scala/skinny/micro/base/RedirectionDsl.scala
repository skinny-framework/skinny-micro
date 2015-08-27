package skinny.micro.base

import skinny.SkinnyEnv
import skinny.micro._
import skinny.micro.context.SkinnyContext

trait RedirectionDsl { self: UrlGenerator with SkinnyContextInitializer =>

  protected def skipHaltingWhenRedirection: Boolean = false

  /**
   * Sends a redirect response and immediately halts the current action.
   */
  def redirect(uri: String)(implicit ctx: SkinnyContext): ActionResult = {
    val result = Found(fullUrl(uri, includeServletPath = false)(ctx))
    if (skipHaltingWhenRedirection) result else halt(result)
  }

  /**
   * Responds as "301 Moved Permanently"
   *
   * @return ActionResult
   */
  def redirect301(location: String, headers: Map[String, String] = Map.empty)(
    implicit ctx: SkinnyContext = context): ActionResult = {
    val result = MovedPermanently(fullUrl(location, includeServletPath = false), headers)
    if (skipHaltingWhenRedirection) result else halt(result)
  }

  /**
   * Responds as "302 Found"
   *
   * @return ActionResult
   */
  def redirect302(location: String, headers: Map[String, String] = Map.empty)(
    implicit ctx: SkinnyContext = context): ActionResult = {
    val result = Found(fullUrl(location, includeServletPath = false), headers)
    if (skipHaltingWhenRedirection) result else halt(result)
  }

  /**
   * Responds as "303 See Other"
   *
   * @return ActionResult
   */
  def redirect303(location: String, headers: Map[String, String] = Map.empty)(
    implicit ctx: SkinnyContext = context): ActionResult = {
    val result = SeeOther(fullUrl(location, includeServletPath = false), headers)
    if (skipHaltingWhenRedirection) result else halt(result)
  }

}
