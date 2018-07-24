package skinny.micro

import skinny.standalone.JettyServer

import scala.collection.mutable.ListBuffer

trait WebServer extends JettyServer {

  private[this] val registeredWebAppHandlers = new ListBuffer[Handler]

  def mountableHandlers: Seq[Handler] = registeredWebAppHandlers.toSeq

  def init(): WebServer = {
    registeredWebAppHandlers.clear()
    this
  }

  def mount(handler: Handler): WebServer = {
    registeredWebAppHandlers += handler
    this
  }

  override def start(): Unit = {
    WebServer.singleton = this
    listener(new SkinnyMicroServerListener)
    super.start()
  }

}

object WebServer extends WebServer {

  var singleton: WebServer = this

}
