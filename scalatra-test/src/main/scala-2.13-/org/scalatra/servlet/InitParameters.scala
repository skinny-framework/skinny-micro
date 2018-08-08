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

  def +=(kv: (String, String)): this.type = {
    sc.setInitParameter(kv._1, kv._2)
    this
  }

  def -=(key: String): this.type = {
    sc.setInitParameter(key, null)
    this
  }

}
