package skinny.micro.test

/**
 * Servlet Container interface.
 */
trait Container {

  protected def ensureSessionIsSerializable()

  /**
   * Starts this Servlet container.
   */
  protected def start(): Unit

  /**
   * Stops this Servlet container.
   */
  protected def stop(): Unit

  /**
   * Resource base path.
   */
  // BITE: This var member here should be kept for backward compatibility...
  var resourceBasePath: String = "src/main/webapp"

}
