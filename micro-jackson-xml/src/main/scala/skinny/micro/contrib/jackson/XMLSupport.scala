package skinny.micro.contrib.jackson

import skinny.jackson.XMLStringOps
import skinny.micro.context.SkinnyContext
import skinny.micro.{ Format, SkinnyMicroBase }

/**
 * XML response support.
 */
trait XMLSupport extends XMLStringOps { self: SkinnyMicroBase =>

  /**
   * Returns XML response.
   *
   * @param entity entity
   * @param charset charset
   * @param prettify prettify if true
   * @return body
   */
  protected def responseAsXML(
    entity: Any,
    charset: Option[String] = Some("utf-8"),
    prettify: Boolean = false,
    underscoreKeys: Boolean = self.useUnderscoreKeysForXML)(implicit ctx: SkinnyContext): String = {

    // If Content-Type is already set, never overwrite it.
    if (contentType(ctx) == null) {
      (contentType = Format.XML.contentType + charset.map(c => s";charset=${c}").getOrElse(""))(ctx)
    }

    if (prettify) toPrettyXMLString(entity, underscoreKeys = underscoreKeys)
    else toXMLString(value = entity, underscoreKeys = underscoreKeys)
  }

}

object XMLSupport {

  val ParsedBodyKey = "skinny.micro.json.ParsedBody"

}
