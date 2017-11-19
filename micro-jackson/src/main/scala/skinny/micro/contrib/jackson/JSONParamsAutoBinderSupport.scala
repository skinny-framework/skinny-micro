package skinny.micro.contrib.jackson

import skinny.jackson.JSONStringOps
import skinny.logging.LoggerProvider
import skinny.micro.context.SkinnyContext
import skinny.micro.routing.MatchedRoute
import skinny.micro.{ ApiFormats, Params, SkinnyMicroBase, SkinnyMicroParams }

/**
 * Merging JSON request body into skinny-micro params.
 *
 * When you'd like to avoid merging JSON request body into params in some actions, please separate controllers.
 */
trait JSONParamsAutoBinderSupport
  extends SkinnyMicroBase
  with JSONStringOps
  with ApiFormats
  with LoggerProvider {

  /**
   * Merge parsedBody (JValue) into params if possible.
   */
  override def params(implicit ctx: SkinnyContext): Params = {
    if (request(ctx).get(JSONSupport.ParsedBodyKey).isDefined) {
      try {
        val jsonParams: Map[String, Seq[String]] = parsedBody(ctx).mapValues(v => Seq(v))
        val mergedParams: Map[String, Seq[String]] = getMergedMultiParams(multiParams(ctx), jsonParams)
        new SkinnyMicroParams(mergedParams)
      } catch {
        case scala.util.control.NonFatal(e) =>
          e.printStackTrace()
          logger.debug(s"Failed to parse JSON body because ${e.getMessage}")
          super.params(ctx)
      }
    } else {
      super.params(ctx)
    }
  }

  protected def getMergedMultiParams(
    params1: Map[String, Seq[String]],
    params2: Map[String, Seq[String]]): Map[String, Seq[String]] = {
    (params1.toSeq ++ params2.toSeq).groupBy(_._1).mapValues(_.flatMap(_._2))
  }

  private[this] val _defaultCacheRequestBody = true

  protected def cacheRequestBodyAsString: Boolean = _defaultCacheRequestBody

  override protected def invoke(matchedRoute: MatchedRoute) = {
    implicit val ctx = context
    withRouteMultiParams(Some(matchedRoute)) {
      implicit val ctx = context
      val mt = request(context).contentType.fold("application/x-www-form-urlencoded")(_.split(";").head)
      val fmt = mimeTypes get mt getOrElse "html"
      if (shouldParseBody(fmt)(context)) {
        request(ctx)(JSONSupport.ParsedBodyKey) = parsedBody
      }
      super.invoke(matchedRoute)
    }
  }

  protected def shouldParseBody(fmt: String)(implicit ctx: SkinnyContext) = {
    fmt == "json" && !ctx.request.requestMethod.isSafe
  }

  protected def parsedBody(implicit ctx: SkinnyContext): Map[String, String] = {
    ctx.request
      .getAs[Map[String, String]](JSONSupport.ParsedBodyKey)
      .orElse(fromJSONString[Map[String, String]](request.body).toOption)
      .getOrElse(Map.empty)
  }

}
