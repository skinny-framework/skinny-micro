package skinny.micro.context

import scala.collection.mutable
import scala.collection.JavaConverters._

class InitParameters(config: ThinServletBaseConfig) extends mutable.Map[String, String] {

  def get(key: String): Option[String] = {
    Option(config.getInitParameter(key))
  }

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

  override def subtractOne(elem: String): this.type = {
    config.getServletContext.setInitParameter(elem, null)
    this
  }

  override def addOne(elem: (String, String)): this.type = {
    config.getServletContext.setInitParameter(elem._1, elem._2)
    this
  }

  override def clear(): Unit = {
    config.getInitParameterNames.asScala.foreach { name =>
      config.getServletContext.setInitParameter(name, null)
    }
  }

}
