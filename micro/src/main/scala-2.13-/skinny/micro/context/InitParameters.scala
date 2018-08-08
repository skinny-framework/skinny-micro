package skinny.micro.context

import scala.collection.mutable
import scala.collection.JavaConverters._

class InitParameters(config: ThinServletBaseConfig) extends mutable.Map[String, String] {

  def get(key: String): Option[String] = Option(config.getInitParameter(key))

  def iterator: Iterator[(String, String)] = {
    val theInitParams = config.getInitParameterNames

    new Iterator[(String, String)] {

      def hasNext: Boolean = theInitParams.hasMoreElements

      def next(): (String, String) = {
        val nm = theInitParams.nextElement()
        (nm, config.getInitParameter(nm))
      }
    }
  }

  def -=(key: String): this.type = {
    config.getServletContext.setInitParameter(key, null)
    this
  }

  def +=(kv: (String, String)): this.type = {
    config.getServletContext.setInitParameter(kv._1, kv._2)
    this
  }

}
