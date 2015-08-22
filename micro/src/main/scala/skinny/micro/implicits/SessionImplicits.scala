package skinny.micro.implicits

import scala.language.implicitConversions

import javax.servlet.http.HttpSession
import skinny.micro.context.SkinnyContext

/**
 * This trait provides session support for stateful applications.
 */
trait SessionImplicits { self: ServletApiImplicits =>

  /**
   * The current session.  Creates a session if none exists.
   */
  implicit def session(implicit ctx: SkinnyContext): HttpSession = ctx.request.getSession

  def session(key: String)(implicit ctx: SkinnyContext): Any = session(ctx)(key)

  def session(key: Symbol)(implicit ctx: SkinnyContext): Any = session(ctx)(key)

  /**
   * The current session.  If none exists, None is returned.
   */
  def sessionOption(implicit ctx: SkinnyContext): Option[HttpSession] = Option(ctx.request.getSession(false))

}
