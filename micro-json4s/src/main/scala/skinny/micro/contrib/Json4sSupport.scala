package skinny.micro.contrib

import skinny.json4s._
import skinny.micro.context.SkinnyContext
import skinny.micro.{ Format, SkinnyMicroBase }

/**
 * JSON response support.
 */
trait Json4sSupport extends Json4sJSONStringOps { self: SkinnyMicroBase =>

  /**
   * Returns JSON response.
   *
   * @param entity entity
   * @param charset charset
   * @param prettify prettify if true
   * @return body
   */
  def responseAsJSON(
    entity: Any,
    charset: Option[String] = Some("utf-8"),
    prettify: Boolean = false,
    underscoreKeys: Boolean = self.useUnderscoreKeysForJSON)(implicit ctx: SkinnyContext): String = {

    // If Content-Type is already set, never overwrite it.
    if (contentType(ctx) == null) {
      (contentType = Format.JSON.contentType + charset.map(c => s"; charset=${c}").getOrElse(""))(ctx)
    }

    if (prettify) toPrettyJSONString(entity)
    else toJSONString(entity, underscoreKeys)
  }

}

object Json4sSupport {

  val ParsedBodyKey = "skinny.micro.json4s.ParsedBody"

}
