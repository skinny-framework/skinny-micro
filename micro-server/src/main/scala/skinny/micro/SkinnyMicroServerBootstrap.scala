package skinny.micro

import javax.servlet.ServletContext

class SkinnyMicroServerBootstrap extends LifeCycle {

  override def init(context: ServletContext): Unit = {
    WebServer.singleton.mountableHandlers.foreach(_.mount(context))
  }

}
