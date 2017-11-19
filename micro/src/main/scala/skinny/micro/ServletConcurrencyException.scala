package skinny.micro

/**
 * Represents concurrency issue access multiple threads on the Servlet.
 */
class ServletConcurrencyException(message: Option[String] = None)
  extends RuntimeException(message.getOrElse(ServletConcurrencyException.message)) {

}

object ServletConcurrencyException {

  val message: String = {
    """
      |
      |------------------------------------------------------
      |
      |  !!! Concurrency Issue Detected !!!
      |
      |  Your code inside Future values or running on other thread is directly accessing Skinny Micro's APIs that depend on Servlet's main thread's state.
      |  To make this code safer, you need to explicitly pass SkinnyMicroContext to them like this:
      |
      |  get("/somewhere") {
      |    implicit val ctx = context // fix context outside of Future blocks
      |    Future {
      |      // Fix detected `ambiguous implicit values` errors here
      |      // (e.g.) params(ctx), ctx.request instead of request
      |    }
      |  }
      |
      |------------------------------------------------------
      |""".stripMargin
  }

}
