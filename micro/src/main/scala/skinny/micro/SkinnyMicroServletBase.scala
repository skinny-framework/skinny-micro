package skinny.micro

import javax.servlet.http.{ HttpServlet, HttpServletRequest, HttpServletResponse }
import javax.servlet._
import skinny.micro.context.{ SkinnyContext, ThinServletBaseConfig }
import skinny.micro.implicits.{ RicherStringImplicits, ServletApiImplicits }
import skinny.micro.util.UriDecoder

import scala.util.control.Exception._

/**
 * Base trait for SkinnyMicroServlet implementations.
 */
trait SkinnyMicroServletBase extends HttpServlet with SkinnyMicroBase {

  override def service(request: HttpServletRequest, response: HttpServletResponse): Unit = {
    // NOTICE: Keep this method invocation here
    // When doing this in SKinnyMicroBase, www-form-urlencoded body request makes its included multi-byte chars garbled at least on Tomcat 7.
    setRequestCharacterEncodingAsDefaultIfAbsent(request)
    handle(request, response)
  }

  /**
   * Defines the request path to be matched by routers.  The default
   * definition is optimized for `path mapped` servlets (i.e., servlet
   * mapping ends in `&#47;*`).  The route should match everything matched by
   * the `&#47;*`.  In the event that the request URI equals the servlet path
   * with no trailing slash (e.g., mapping = `/admin&#47;*`, request URI =
   * '/admin'), a '/' is returned.
   *
   * All other servlet mappings likely want to return request.getServletPath.
   * Custom implementations are allowed for unusual cases.
   */
  override def requestPath(implicit ctx: SkinnyContext): String = {
    SkinnyMicroServletBase.requestPath(ctx.request)
  }

  override protected def routeBasePath(implicit ctx: SkinnyContext): String = {
    require(config != null, "routeBasePath requires the servlet to be initialized")
    require(ctx.request != null, "routeBasePath requires an active request to determine the servlet path")

    if (ctx.servletContext != null) {
      ctx.servletContext.getContextPath + ctx.request.getServletPath
    } else {
      // servletContext can be null when test environment
      ctx.request.getServletPath
    }
  }

  /**
   * Invoked when no route matches.  By default, calls `serveStaticResource()`,
   * and if that fails, calls `resourceNotFound()`.
   *
   * This action can be overridden by a notFound block.
   */
  protected var doNotFound: Action = () => {
    serveStaticResource()(skinnyContext)
      .getOrElse(resourceNotFound()(skinnyContext))
  }

  /**
   * Attempts to find a static resource matching the request path.
   * Override to return None to stop this.
   */
  protected def serveStaticResource()(
    implicit
    ctx: SkinnyContext): Option[Any] = {
    servletContext.resource(ctx.request) map { _ =>
      servletContext.getNamedDispatcher("default").forward(ctx.request, ctx.response)
    }
  }

  /**
   * Called by default notFound if no routes matched and no static resource could be found.
   */
  protected def resourceNotFound()(
    implicit
    ctx: SkinnyContext): Any = {
    ctx.response.setStatus(404)
    if (isDevelopment()) {
      val error = "Requesting \"%s %s\" on servlet \"%s\" but only have: %s"
      ctx.response.getWriter println error.format(
        ctx.request.getMethod,
        Option(ctx.request.getPathInfo) getOrElse "/",
        ctx.request.getServletPath,
        routes.entryPoints.mkString("<ul><li>", "</li><li>", "</li></ul>"))
    }
  }

  override def init(config: ServletConfig): Unit = {
    super.init(config)
    initialize(new ThinServletBaseConfig {
      override def getServletContext(): ServletContext = config.getServletContext
      override def getInitParameter(name: String): String = config.getInitParameter(name)
      override def getInitParameterNames(): java.util.Enumeration[String] = config.getInitParameterNames
      override def getBaseConfigType: BaseConfigType = BaseConfigType.ServletConfig
      override def getServletConfig: Option[ServletConfig] = Some(config)
    }) // see Initializable.initialize for why
  }

  override def initialize(config: ThinServletBaseConfig): Unit = {
    super.initialize(config)
  }

  override def destroy(): Unit = {
    shutdown()
    super.destroy()
  }

}

object SkinnyMicroServletBase {

  import ServletApiImplicits._
  import RicherStringImplicits._

  val RequestPathKey = "skinny.micro.SkinnyMicroServlet.requestPath"

  def requestPath(request: HttpServletRequest): String = {
    require(request != null, "The request can't be null for getting the request path")
    def startIndex(r: HttpServletRequest) =
      r.getContextPath.blankOption.map(_.length).getOrElse(0) + r.getServletPath.blankOption.map(_.length).getOrElse(0)
    def getRequestPath(r: HttpServletRequest) = {
      val u = (catching(classOf[NullPointerException]) opt { r.getRequestURI } getOrElse "/")
      requestPath(u, startIndex(r))
    }

    request.get(RequestPathKey) map (_.toString) getOrElse {
      val rp = getRequestPath(request)
      request(RequestPathKey) = rp
      rp
    }
  }

  def requestPath(uri: String, idx: Int): String = {
    val u1 = UriDecoder.firstStep(uri)
    val u2 = (u1.blankOption map { _.substring(idx) } flatMap (_.blankOption) getOrElse "/")
    val pos = u2.indexOf(';')
    if (pos > -1) u2.substring(0, pos) else u2
  }

}
