package skinny.micro

import javax.servlet.http.{ HttpServletRequest, HttpServletResponse }
import javax.servlet._
import skinny.micro.context.{ SkinnyContext, ThinServletBaseConfig }
import skinny.micro.util.UriDecoder

import scala.util.DynamicVariable

/**
 * Base trait for SkinnyMicroFilter implementations.
 */
trait SkinnyMicroFilterBase extends SkinnyMicroBase {

  private[this] val _filterChain: DynamicVariable[FilterChain] = new DynamicVariable[FilterChain](null)

  protected def filterChain: FilterChain = _filterChain.value

  def doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain): Unit = {
    val httpRequest = request.asInstanceOf[HttpServletRequest]
    // NOTICE: Keep this method invocation here
    // When doing this in SKinnyMicroBase, www-form-urlencoded body request makes its included multi-byte chars garbled at least on Tomcat 7.
    setRequestCharacterEncodingAsDefaultIfAbsent(httpRequest)

    val httpResponse = response.asInstanceOf[HttpServletResponse]

    _filterChain.withValue(chain) {
      handle(httpRequest, httpResponse)
    }
  }

  // What goes in servletPath and what goes in pathInfo depends on how the underlying servlet is mapped.
  // Unlike the SkinnyMicro servlet, we'll use both here by default.  Don't like it?  Override it.
  override def requestPath(implicit ctx: SkinnyContext): String = {
    val request = ctx.request
    def getRequestPath: String = request.getRequestURI match {
      case requestURI: String =>
        var uri = requestURI
        if (request.getContextPath.length > 0) uri = uri.substring(request.getContextPath.length)
        if (uri.length == 0) {
          uri = "/"
        } else {
          val pos = uri.indexOf(';')
          if (pos >= 0) uri = uri.substring(0, pos)
        }
        UriDecoder.firstStep(uri)
      case null => "/"
    }

    request.get("skinny.micro.SkinnyMicroFilter.requestPath") match {
      case Some(uri) => uri.toString
      case _ => {
        val requestPath = getRequestPath
        request.setAttribute("skinny.micro.SkinnyMicroFilter.requestPath", requestPath)
        requestPath.toString
      }
    }
  }

  override protected def routeBasePath(implicit ctx: SkinnyContext): String = {
    // servletContext can be null when test environment
    if (ctx.servletContext != null) ctx.servletContext.getContextPath
    else "/"
  }

  protected var doNotFound: Action = () => {
    implicit val ctx = context
    filterChain.doFilter(request, response)
  }

  methodNotAllowed { _ =>
    implicit val ctx = context
    filterChain.doFilter(request, response)
  }

  // see Initializable.initialize for why
  def init(filterConfig: FilterConfig): Unit = {
    initialize(new ThinServletBaseConfig {
      override def getServletContext(): ServletContext = filterConfig.getServletContext
      override def getInitParameter(name: String): String = filterConfig.getInitParameter(name)
      override def getInitParameterNames(): java.util.Enumeration[String] = filterConfig.getInitParameterNames
      override def getBaseConfigType: BaseConfigType = BaseConfigType.FilterConfig
      override def getFilterConfig: Option[FilterConfig] = Some(filterConfig)
    })
  }

  def destroy(): Unit = {
    shutdown()
  }

}
