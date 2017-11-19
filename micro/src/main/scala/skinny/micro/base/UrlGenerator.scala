package skinny.micro.base

import skinny.SkinnyEnv
import skinny.micro.SkinnyMicroBase
import skinny.micro.context.SkinnyContext
import skinny.micro.implicits.RicherStringImplicits

import scala.util.control.Exception._

/**
 * Provides url generator DSL.
 */
trait UrlGenerator extends RicherStringImplicits { self: SkinnyMicroBase =>

  private[this] def needHttps: Boolean = {
    allCatch.withApply(_ => false) {
      servletContext.getInitParameter(SkinnyMicroBase.ForceHttpsKey).blankOption
        .map(_.toBoolean) getOrElse false
    }
  }

  def relativeUrl(
    path: String,
    params: Iterable[(String, Any)] = Iterable.empty,
    includeContextPath: Boolean = true,
    includeServletPath: Boolean = true)(implicit ctx: SkinnyContext): String = {
    url(path, params, includeContextPath, includeServletPath, absolutize = false)(ctx)
  }

  /**
   * Returns a context-relative, session-aware URL for a path and specified parameters.
   * Finally, the result is run through `response.encodeURL` for a session
   * ID, if necessary.
   *
   * @param path the base path.  If a path begins with '/', then the context
   *             path will be prepended to the result
   *
   * @param params params, to be appended in the form of a query string
   *
   * @return the path plus the query string, if any.  The path is run through
   *         `response.encodeURL` to add any necessary session tracking parameters.
   */
  def url(
    path: String,
    params: Iterable[(String, Any)] = Iterable.empty,
    includeContextPath: Boolean = true,
    includeServletPath: Boolean = true,
    absolutize: Boolean = true)(implicit ctx: SkinnyContext): String = {
    try {
      val newPath = path match {
        case x if x.startsWith("/") && includeContextPath && includeServletPath =>
          ensureSlash(routeBasePath(ctx)) + ensureContextPathsStripped(ensureSlash(path))(ctx)
        case x if x.startsWith("/") && includeContextPath =>
          ensureSlash(contextPath) + ensureContextPathStripped(ensureSlash(path))
        case x if x.startsWith("/") && includeServletPath => ctx.request.getServletPath.blankOption map {
          ensureSlash(_) + ensureServletPathStripped(ensureSlash(path))(ctx)
        } getOrElse "/"
        case _ if absolutize => ensureContextPathsStripped(ensureSlash(path))(ctx)
        case _ => path
      }

      val pairs = params map {
        case (key, None) => key.urlEncode + "="
        case (key, Some(value)) => key.urlEncode + "=" + value.toString.urlEncode
        case (key, value) => key.urlEncode + "=" + value.toString.urlEncode
      }
      val queryString = if (pairs.isEmpty) "" else pairs.mkString("?", "&", "")
      newPath + queryString
    } catch {
      case e: NullPointerException =>
        // FIXME: 2.0.0 still has this issue.
        if (SkinnyEnv.isTest()) "[work around] see https://github.com/scalatra/scalatra/issues/368"
        else throw e
    }
  }

  /**
   * Builds a full URL from the given relative path. Takes into account the port configuration, https, ...
   *
   * @param path a relative path
   *
   * @return the full URL
   */
  def fullUrl(
    path: String,
    params: Iterable[(String, Any)] = Iterable.empty,
    includeContextPath: Boolean = true,
    includeServletPath: Boolean = true,
    withSessionId: Boolean = true)(implicit ctx: SkinnyContext): String = {
    if (path.startsWith("http")) path
    else {
      val p = url(path, params, includeContextPath, includeServletPath, withSessionId)(ctx)
      if (p.startsWith("http")) p else buildBaseUrl(ctx) + ensureSlash(p)
    }
  }

  private[this] def buildBaseUrl(implicit ctx: SkinnyContext): String = {
    "%s://%s".format(
      if (needHttps || ctx.request.isHttps) "https" else "http",
      serverAuthority(ctx))
  }

  private[this] def ensureContextPathsStripped(path: String)(implicit ctx: SkinnyContext): String = {
    ((ensureContextPathStripped _) andThen (p => ensureServletPathStripped(p)(ctx)))(path)
  }

  private[this] def ensureServletPathStripped(path: String)(implicit ctx: SkinnyContext): String = {
    val sp = ensureSlash(Option(ctx.request.getServletPath).flatMap(_.blankOption).getOrElse(""))
    val np = if (path.startsWith(sp + "/")) path.substring(sp.length) else path
    ensureSlash(np)
  }

  private[this] def ensureContextPathStripped(path: String): String = {
    val cp = ensureSlash(contextPath)
    val np = if (path.startsWith(cp + "/")) path.substring(cp.length) else path
    ensureSlash(np)
  }

  private[this] def ensureSlash(candidate: String): String = {
    if (candidate == null) {
      ""
    } else {
      val p = if (candidate.startsWith("/")) candidate else "/" + candidate
      if (p.endsWith("/")) p.dropRight(1) else p
    }
  }

}
