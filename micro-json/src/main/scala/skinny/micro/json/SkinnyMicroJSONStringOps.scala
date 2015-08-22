package skinny.micro.json

import skinny.micro.{ Format, SkinnyMicroBase }
import skinny.micro.context.SkinnyContext
import skinny.json._

/**
 * JSON response support.
 */
trait SkinnyMicroJSONStringOps extends JSONStringOps with JSONStringOpsConfig { self: SkinnyMicroBase =>

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
