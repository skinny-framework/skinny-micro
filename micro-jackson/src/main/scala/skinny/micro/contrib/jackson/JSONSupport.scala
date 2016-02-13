package skinny.micro.contrib.jackson

import skinny.jackson.JSONStringOps
import skinny.micro.context.SkinnyContext
import skinny.micro.{ Format, SkinnyMicroBase }

/**
 * JSON response support.
 */
trait JSONSupport extends JSONStringOps { self: SkinnyMicroBase =>

  /**
   * Returns JSON response.
   *
   * @param entity entity
   * @param charset charset
   * @param prettify prettify if true
   * @return body
   */
  protected def responseAsJSON(
    entity: Any,
    charset: Option[String] = Some("utf-8"),
    prettify: Boolean = false,
    underscoreKeys: Boolean = self.useUnderscoreKeysForJSON
  )(implicit ctx: SkinnyContext): String = {

    // If Content-Type is already set, never overwrite it.
    if (contentType(ctx) == null) {
      (contentType = Format.JSON.contentType + charset.map(c => s"; charset=${c}").getOrElse(""))(ctx)
    }

    if (prettify) toPrettyJSONString(entity, underscoreKeys = underscoreKeys)
    else toJSONString(value = entity, underscoreKeys = underscoreKeys)
  }

}

object JSONSupport {

  val ParsedBodyKey = "skinny.micro.json.ParsedBody"

}
