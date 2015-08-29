package skinny.micro.contrib

import skinny.micro.SkinnyMicroBase
import skinny.micro.base.BeforeAfterDsl

/**
 * X-XSS-Protection header support
 *
 * - https://www.owasp.org/index.php/List_of_useful_HTTP_headers
 */
trait XXSSProtectionHeaderSupport { self: SkinnyMicroBase with BeforeAfterDsl =>

  // NOTE: for all HTML responses defined as Skinny routes
  before() {
    response(context).setHeader("X-XSS-Protection", "1; mode=block")
  }

}
