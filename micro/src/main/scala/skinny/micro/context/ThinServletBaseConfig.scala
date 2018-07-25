package skinny.micro.context

import javax.servlet.{ FilterConfig, ServletConfig, ServletContext }

trait ThinServletBaseConfig {

  sealed trait BaseConfigType

  object BaseConfigType {
    case object Unknown extends BaseConfigType
    case object ServletConfig extends BaseConfigType
    case object FilterConfig extends BaseConfigType
  }

  def getServletContext(): ServletContext

  def getInitParameter(name: String): String

  def getInitParameterNames(): java.util.Enumeration[String]

  def getBaseConfigType: BaseConfigType

  def getServletConfig: Option[ServletConfig] = None

  def getFilterConfig: Option[FilterConfig] = None

}
