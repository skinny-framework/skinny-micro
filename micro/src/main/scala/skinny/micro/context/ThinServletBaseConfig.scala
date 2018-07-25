package skinny.micro.context

import javax.servlet.{ FilterConfig, ServletConfig, ServletContext }

object ThinServletBaseConfig {

  sealed trait BaseConfigType

  object BaseConfigType {
    case object Unknown extends BaseConfigType
    case object ServletConfig extends BaseConfigType
    case object FilterConfig extends BaseConfigType
  }

}

trait ThinServletBaseConfig {

  def getServletContext(): ServletContext

  def getInitParameter(name: String): String

  def getInitParameterNames(): java.util.Enumeration[String]

  def getBaseConfigType: ThinServletBaseConfig.BaseConfigType

  def getServletConfig: Option[ServletConfig] = None

  def getFilterConfig: Option[FilterConfig] = None

}
