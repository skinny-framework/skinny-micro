package skinny.micro.test

/**
 * Simple Http response.
 */
case class SimpleResponse(
  status: Int,
  headers: Map[String, Seq[String]],
  body: String
)
