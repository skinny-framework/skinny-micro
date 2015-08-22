package skinny.micro.base

import skinny.micro.{ SkinnyMicroBase, ApiFormats }
import skinny.micro.context.SkinnyContext
import skinny.micro.implicits.ServletApiImplicits

/**
 * Provides accessor for request format.
 */
trait RequestFormatAccessor extends ServletApiImplicits { self: SkinnyMicroBase =>

  /**
   * Explicitly sets the request-scoped format.  This takes precedence over
   * whatever was inferred from the request.
   */
  def format_=(formatValue: String)(implicit ctx: SkinnyContext = context): Unit = {
    ctx.request(ApiFormats.FormatKey) = formatValue
  }

}
