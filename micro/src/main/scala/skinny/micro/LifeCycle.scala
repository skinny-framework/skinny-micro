package skinny.micro

import javax.servlet.ServletContext

import skinny.micro.implicits.ServletApiImplicits

trait LifeCycle extends ServletApiImplicits {

  def init(context: ServletContext): Unit = {}

  def destroy(context: ServletContext): Unit = {}

}
