package skinny.micro.contrib

import skinny.micro.SkinnyMicroBase
import skinny.micro.base.BeforeAfterDsl

/**
 * CORS(Cross-Origin Resource Sharing) Support.
 */
trait CORSSupport { self: SkinnyMicroBase with BeforeAfterDsl =>

  protected def accessControlAllowOriginHeaderValue = "*"

  before() {
    implicit val ctx = context
    response.setHeader(
      "Access-Control-Allow-Origin",
      accessControlAllowOriginHeaderValue
    )
  }

}
