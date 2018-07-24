package skinny.micro.context

import javax.servlet.ServletContext

trait ThinServletBaseConfig {

  def getServletContext(): ServletContext

  def getInitParameter(name: String): String

  def getInitParameterNames(): java.util.Enumeration[String]

}
