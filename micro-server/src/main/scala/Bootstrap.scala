import javax.servlet.ServletContext

import skinny.micro._

class Bootstrap extends LifeCycle {

  override def init(context: ServletContext): Unit = {
    WebServer.singleton.mountableHandlers.foreach(_.mount(context))
  }

}
