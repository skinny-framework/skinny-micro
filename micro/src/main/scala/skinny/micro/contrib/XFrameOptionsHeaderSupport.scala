package skinny.micro.contrib

import skinny.micro.SkinnyMicroBase
import skinny.micro.base.BeforeAfterDsl

/**
 * X-Frame-Options header support
 *
 * - https://www.owasp.org/index.php/List_of_useful_HTTP_headers
 */
trait XFrameOptionsHeaderSupport { self: SkinnyMicroBase with BeforeAfterDsl =>

  // NOTE: There are three possible values for the X-Frame-Options headers.
  // - "deny" which prevents any domain from framing the content.
  // - "sameorigin" which only allows the current site to frame the content.
  // - "allow-from uri" which permits the specified 'uri' to frame this page.
  lazy val xFrameOptionsPolicy = "sameorigin"

  // NOTE: for all HTML responses defined as Skinny routes
  before() {
    response.setHeader("X-Frame-Options", xFrameOptionsPolicy)
  }

}
