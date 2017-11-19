package skinny.micro

import javax.servlet._
import javax.servlet.http.{ HttpServletRequest, HttpServletResponse }

import skinny.SkinnyEnv
import skinny.micro.async.AsyncSupported
import skinny.micro.base.{ UnstableAccessValidationConfig, ServletContextAccessor, RouteRegistryAccessor, SkinnyContextInitializer }
import skinny.logging.LoggerProvider

/**
 * A `Handler` is the SkinnyMicro abstraction for an object that operates on a request/response pair.
 */
trait Handler
  extends Initializable
  with SkinnyContextInitializer
  with UnstableAccessValidationConfig
  with RouteRegistryAccessor
  with ServletContextAccessor
  with LoggerProvider {

  /**
   * Handles a request and writes to the response.
   */
  def handle(request: HttpServletRequest, res: HttpServletResponse): Unit

  def mount(ctx: ServletContext): Unit = {
    this match {
      case filter: Filter =>
        allRoutePaths.foreach { path =>
          val name = this.getClass.getName
          val registration: FilterRegistration = {
            val reg = Option(ctx.getFilterRegistration(name)).getOrElse(ctx.addFilter(name, filter))
            // mocked object can be null
            if (reg != null && filter.isInstanceOf[AsyncSupported]) {
              reg.asInstanceOf[FilterRegistration.Dynamic].setAsyncSupported(true)
            }
            reg
          }
          if (registration != null) {
            registration.addMappingForUrlPatterns(
              java.util.EnumSet.of(DispatcherType.REQUEST, DispatcherType.ASYNC), true, toNormalizedRoutePath(path))
          } else {
            logger.info("FilterRegistration is empty. Skipped.")
          }
        }
      case servlet: Servlet =>
        try {
          ctx.mount(servlet, "/")
        } catch {
          case e: NullPointerException if SkinnyEnv.isTest() =>
            logger.info("Skipped NPE when mocking servlet APIs.")
        }
    }
  }

  /**
   * Defines formats to be respond. By default, HTML, JSON, XML are available.
   *
   * @return formats
   */
  protected def respondTo: Seq[Format] = Seq(Format.HTML, Format.JSON, Format.XML)

  private[this] def toNormalizedRoutePath(path: String): String = path match {
    case "/" => "/"
    case p if p.endsWith("/*") => p
    case p if p.endsWith("/") => p + "*"
    case _ => path + "/*"
  }

  private[this] def allRoutePaths: Seq[String] = {
    routes.entryPoints.map(_.toString)
      .flatMap(_.split("\\t").tail.headOption.map(_.split("[:\\?]+").head))
      .distinct
      .flatMap { path =>
        if (path.endsWith(".")) respondTo.map(format => path + format.name)
        else Seq(path)
      }
  }

}

object Handler {

  /**
   * To save the HTTP method into route's metadata.
   */
  val RouteMetadataHttpMethodCacheKey: Symbol = 'HttpMethodSavedBySkinnyFramework

}
