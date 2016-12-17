package skinny.micro.context

import javax.servlet.ServletContext
import javax.servlet.http.{ HttpServletRequest, HttpServletResponse }

import skinny.micro.UnstableAccessValidation
import skinny.micro.implicits.{ CookiesImplicits, ServletApiImplicits, SessionImplicits }
import skinny.micro.request.StableHttpServletRequest

/**
 * SkinnyMicro's context for each request.
 */
trait SkinnyContext
    extends ServletApiImplicits
    with SessionImplicits
    with CookiesImplicits {

  val request: HttpServletRequest

  val response: HttpServletResponse

  val servletContext: ServletContext

  val unstableAccessValidation: UnstableAccessValidation

  def surelyStable(validation: UnstableAccessValidation): SkinnyContext = {
    SkinnyContext.surelyStable(this, validation)
  }

}

object SkinnyContext {

  private class StableSkinnyContext(implicit
    val request: HttpServletRequest,
    val response: HttpServletResponse,
    val servletContext: ServletContext,
    val unstableAccessValidation: UnstableAccessValidation) extends SkinnyContext {}

  def surelyStable(
    ctx: SkinnyContext,
    validation: UnstableAccessValidation
  ): SkinnyContext = {
    new StableSkinnyContext()(
      StableHttpServletRequest(ctx.request, validation),
      ctx.response,
      ctx.servletContext,
      validation
    )
  }

  def build(
    ctx: ServletContext,
    req: HttpServletRequest,
    resp: HttpServletResponse,
    validation: UnstableAccessValidation
  ): SkinnyContext = {
    new StableSkinnyContext()(
      StableHttpServletRequest(req, validation),
      resp,
      ctx,
      validation
    )
  }

  def buildWithRequest(
    req: HttpServletRequest,
    validation: UnstableAccessValidation
  ): SkinnyContext = {
    new StableSkinnyContext()(
      StableHttpServletRequest(req, validation),
      null,
      null,
      validation
    )
  }

  def buildWithoutResponse(
    req: HttpServletRequest,
    ctx: ServletContext,
    validation: UnstableAccessValidation
  ): SkinnyContext = {
    new StableSkinnyContext()(
      StableHttpServletRequest(req, validation),
      null,
      ctx,
      validation
    )
  }

}
