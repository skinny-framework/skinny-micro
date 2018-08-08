package skinny.micro.test

import org.eclipse.jetty.server.session.DefaultSessionIdManager
import org.eclipse.jetty.server.{ Server, ServerConnector }
import org.eclipse.jetty.servlet.ServletContextHandler
import org.slf4j.LoggerFactory

/**
 * Embedded Jetty Servlet container.
 */
trait EmbeddedJettyContainer extends JettyContainer {

  private[this] val logger = LoggerFactory.getLogger(classOf[EmbeddedJettyContainer])

  /**
   * Sets the port to listen on.  0 means listen on any available port.
   */
  def port: Int = 0

  /**
   * The port of the currently running Jetty.  May differ from port if port is 0.
   *
   * @return Some port if Jetty is currently listening, or None if it is not.
   */
  def localPort: Option[Int] = server.getConnectors.collectFirst {
    case x: ServerConnector => x.getLocalPort
  }

  def contextPath: String = "/"

  lazy val server: Server = new Server(port)

  lazy val servletContextHandler: ServletContextHandler = {
    val handler = new ServletContextHandler(ServletContextHandler.SESSIONS)
    handler.setContextPath(contextPath)
    handler.setResourceBase(resourceBasePath)
    configureSessionIdManager(handler, server)
    handler
  }

  def start(): Unit = {
    server.setHandler(servletContextHandler)
    server.start()
  }

  def stop(): Unit = server.stop()

  def baseUrl: String = {
    server.getConnectors.collectFirst {
      case conn: ServerConnector =>
        val host = Option(conn.getHost).getOrElse("localhost")
        val port = conn.getLocalPort
        require(port > 0, "The detected local port is < 1, that's not allowed")
        "http://%s:%d".format(host, port)
    }.getOrElse {
      sys.error("can't calculate base URL: no connector")
    }
  }

  protected def configureSessionIdManager(servletContextHandler: ServletContextHandler, server: Server): Unit = {
    val sessionHandler = servletContextHandler.getSessionHandler
    sessionHandler.getSessionIdManager match {
      case null =>
        val sessionIdManger = new DefaultSessionIdManager(server)
        sessionIdManger.setWorkerName(null)
        sessionHandler.setSessionIdManager(sessionIdManger)
      case defaultSessionIdManager: DefaultSessionIdManager =>
        defaultSessionIdManager.setWorkerName(null)
      case manager =>
        val className = manager.getClass.getCanonicalName
        logger.warn(s"Skipped to configure SessionIdManager (unexpected implementation: ${className})")
    }
  }

}
