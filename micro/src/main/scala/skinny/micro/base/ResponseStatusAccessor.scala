package skinny.micro.base

import skinny.micro.SkinnyMicroBase
import skinny.micro.context.SkinnyContext
import skinny.micro.implicits.ServletApiImplicits
import skinny.micro.response.ResponseStatus

/**
 * Provides accessor for response status.
 */
trait ResponseStatusAccessor extends ServletApiImplicits { self: SkinnyMicroBase =>

  /**
   * Gets the status code of the current response.
   */
  def status(implicit ctx: SkinnyContext = context): Int = ctx.response.status.code

  /**
   * Sets the status code of the current response.
   */
  def status_=(code: Int)(implicit ctx: SkinnyContext = context): Unit = {
    ctx.response.status = ResponseStatus(code)
  }

}
