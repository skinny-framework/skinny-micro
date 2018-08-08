package org.scalatra.servlet

import javax.servlet.ServletContext

import scala.collection.mutable
import scala.collection.JavaConverters._

class InitParameters(sc: ServletContext) extends mutable.Map[String, String] {

  def get(key: String): Option[String] = Option(sc.getInitParameter(key))

  def iterator: Iterator[(String, String)] = {
    val theInitParams = sc.getInitParameterNames

    new Iterator[(String, String)] {

      def hasNext: Boolean = theInitParams.hasMoreElements

      def next(): (String, String) = {
        val nm = theInitParams.nextElement()
        (nm, sc.getInitParameter(nm))
      }
    }
  }

  override def subtractOne(elem: String): this.type = {
    sc.setInitParameter(elem, null)
    this
  }

  override def addOne(elem: (String, String)): this.type = {
    sc.setInitParameter(elem._1, elem._2)
    this
  }

  override def clear(): Unit = {
    sc.getInitParameterNames.asScala.foreach { name =>
      sc.setInitParameter(name, null)
    }
  }

}
