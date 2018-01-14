package skinny.micro.base

import scala.language.reflectiveCalls
import scala.language.implicitConversions
import javax.servlet.{ FilterConfig, ServletConfig, ServletContext }

import skinny.micro.implicits.{ RicherStringImplicits, ServletApiImplicits }
import skinny.micro.{ Initializable, SkinnyMicroBase }
import skinny.micro.context.SkinnyContext
import skinny.micro.cookie.{ Cookie, CookieOptions }

import scala.collection.immutable.DefaultMap
import scala.collection.JavaConverters._

/**
 * Provides accessor for ServletContext.
 */
trait ServletContextAccessor
  extends Initializable
  with ServletApiImplicits
  with RicherStringImplicits {

  import SkinnyMicroBase._

  type ConfigT <: {

    def getServletContext(): ServletContext

    def getInitParameter(name: String): String

    def getInitParameterNames(): java.util.Enumeration[String]

  }

  private trait NullConfig extends ServletConfig with FilterConfig {
    override def getServletName: String = null
    override def getFilterName: String = null
    override def getServletContext(): ServletContext = null
    override def getInitParameter(name: String): String = null
    override def getInitParameterNames(): java.util.Enumeration[String] = null
  }

  protected implicit def configWrapper(config: ConfigT) = new Config {

    override def context: ServletContext = config.getServletContext

    object initParameters extends DefaultMap[String, String] {

      override def get(key: String): Option[String] = Option(config.getInitParameter(key))

      override def iterator: Iterator[(String, String)] = {
        for (name <- config.getInitParameterNames.asScala)
          yield (name, config.getInitParameter(name))
      }
    }

  }

  /**
   * The configuration, typically a ServletConfig or FilterConfig.
   */
  var config: ConfigT = (new NullConfig {}).asInstanceOf[ConfigT]

  /**
   * Initializes the kernel.  Used to provide context that is unavailable
   * when the instance is constructed, for example the servlet lifecycle.
   * Should set the `config` variable to the parameter.
   *
   * @param config the configuration.
   */
  def initialize(config: ConfigT): Unit = {
    this.config = config
    val path = contextPath match {
      case "" => "/" // The root servlet is "", but the root cookie path is "/"
      case p => p
    }
    servletContext(Cookie.CookieOptionsKey) = CookieOptions(path = path)
  }

  /**
   * The servlet context in which this kernel runs.
   */
  implicit def servletContext: ServletContext = config.context

  protected def serverAuthority(implicit ctx: SkinnyContext): String = {
    val p = serverPort(ctx)
    val h = serverHost(ctx)
    if (p == 80 || p == 443) h else h + ":" + p.toString
  }

  def serverHost(implicit ctx: SkinnyContext): String = {
    initParameter(HostNameKey).flatMap(_.blankOption) getOrElse ctx.request.getServerName
  }

  def serverPort(implicit ctx: SkinnyContext): Int = {
    initParameter(PortKey).flatMap(_.blankOption).map(_.toInt) getOrElse ctx.request.getServerPort
  }

  def contextPath: String = servletContext.contextPath

  /**
   * Gets an init parameter from the config.
   *
   * @param name the name of the key
   * @return an option containing the value of the parameter if defined, or
   *         `None` if the parameter is not set.
   */
  def initParameter(name: String): Option[String] = {
    config.initParameters.get(name) orElse {
      servletContext.initParameters.get(name)
    }
  }

}
