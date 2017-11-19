package skinny.micro

/**
 * Represents unstable access to servlet objects managed by containers from unmanaged threads.
 */
class UnstableAccessException(attribute: String)
  extends RuntimeException(UnstableAccessException.message(attribute)) {

}

object UnstableAccessException {

  def message(attribute: String): String = {
    val workaround = {
      if (attribute == "getSession") "Or, if you accept the risk, set the web controller's #useMostlyStableHttpSession as false (default: true)."
      else "Or, if you accept the risk, set the web controller's #unstableAccessValidationEnabled as false (default: true)."
    }
    s"""
      |
      |------------------------------------------------------
      |
      |  !!! Concurrency Issue Detected !!!
      |
      |  Accessing $attribute from unmanaged threads, inside Future blocks in most cases, is too dangerous.
      |
      |  Objects managed by Servlet containers should be accessed on main threads.
      |
      |  Fix your code to copy needed values from $attribute as read-only ones before entering Future blocks.
      |
      |  $workaround
      |
      |------------------------------------------------------
      |""".stripMargin
  }

}
