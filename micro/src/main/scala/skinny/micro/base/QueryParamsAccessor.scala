package skinny.micro.base

import skinny.micro.context.SkinnyContext
import skinny.micro.{ SkinnyMicroBase, MultiParams, Params, SkinnyMicroParams }

/**
 * Provides queryParams/queryMultiParams.
 */
trait QueryParamsAccessor { self: SkinnyMicroBase =>

  /**
   * Returns query string multi parameters as a Map value.
   */
  def queryMultiParams(implicit ctx: SkinnyContext): MultiParams = {
    new MultiParams(skinny.micro.rl.MapQueryString.parseString(ctx.request.queryString))
  }

  /**
   * Returns query string parameters as a Map value.
   */
  def queryParams(implicit ctx: SkinnyContext): Params = new SkinnyMicroParams(queryMultiParams(ctx))

}
