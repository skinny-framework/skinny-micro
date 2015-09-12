package skinny.micro.contrib.json4s

import java.io.{ InputStream, InputStreamReader }

import org.json4s.Xml._
import org.json4s._
import org.slf4j.LoggerFactory
import skinny.json4s.JSONStringOps
import skinny.logging.LoggerProvider
import skinny.micro.context.SkinnyContext
import skinny.micro.routing.MatchedRoute
import skinny.micro.{ ApiFormats, Params, SkinnyMicroBase, SkinnyMicroParams }

/**
 * Merging JSON request body into skinny-micro params.
 *
 * When you'd like to avoid merging JSON request body into params in some actions, please separate controllers.
 *
 * NOTICE: Avoid extending JacksonJsonSupport due to render method conflict
 */
trait JSONParamsAutoBinderSupport
    extends SkinnyMicroBase
    with JSONStringOps
    with ApiFormats
    with LoggerProvider {

  /**
   * Merge parsedBody (JValue) into params if possible.
   */
  override def params(implicit ctx: SkinnyContext): Params = {
    if (request(ctx).get(JSONSupport.ParsedBodyKey).isDefined) {
      try {
        val jsonParams: Map[String, Seq[String]] = parsedBody(ctx).extract[Map[String, String]].mapValues(v => Seq(v))
        val mergedParams: Map[String, Seq[String]] = getMergedMultiParams(multiParams(ctx), jsonParams)
        new SkinnyMicroParams(mergedParams)
      } catch {
        case scala.util.control.NonFatal(e) =>
          logger.debug(s"Failed to parse JSON body because ${e.getMessage}")
          super.params(ctx)
      }
    } else {
      super.params(ctx)
    }
  }

  protected def cacheRequestBodyAsString: Boolean = _defaultCacheRequestBody

  protected def transformRequestBody(body: JValue) = body

  override protected def invoke(matchedRoute: MatchedRoute) = {
    withRouteMultiParams(Some(matchedRoute)) {
      implicit val ctx = context
      val mt = request(context).contentType.fold("application/x-www-form-urlencoded")(_.split(";").head)
      val fmt = mimeTypes get mt getOrElse "html"
      if (shouldParseBody(fmt)(context)) {
        request(ctx)(JSONSupport.ParsedBodyKey) = parseRequestBody(fmt)(ctx).asInstanceOf[AnyRef]
      }
      super.invoke(matchedRoute)
    }
  }

  private[this] val logger = LoggerFactory.getLogger(getClass)

  private[this] val _defaultCacheRequestBody = true

  private[this] def parseRequestBody(format: String)(implicit ctx: SkinnyContext): JValue = try {
    val ct = ctx.request.contentType getOrElse ""
    if (format == "json") {
      val bd = {
        if (ct == "application/x-www-form-urlencoded") {
          multiParams(ctx).keys.headOption
            .map(readJsonFromBody)
            .getOrElse(JNothing)
        } else if (cacheRequestBodyAsString) {
          readJsonFromBody(ctx.request.body)
        } else {
          readJsonFromStreamWithCharset(
            ctx.request.inputStream,
            ctx.request.characterEncoding.getOrElse(defaultCharacterEncoding)
          )
        }
      }
      transformRequestBody(bd)
    } else if (format == "xml") {
      val bd = {
        if (ct == "application/x-www-form-urlencoded") {
          multiParams(ctx).keys.headOption
            .map(readXmlFromBody)
            .getOrElse(JNothing)
        } else if (cacheRequestBodyAsString) readXmlFromBody(request(ctx).body)
        else readXmlFromStream(request(ctx).inputStream)
      }
      transformRequestBody(bd)
    } else JNothing
  } catch {
    case scala.util.control.NonFatal(t) => {
      logger.error(s"Parsing the request body failed, because:", t)
      JNothing
    }
  }

  private[this] def getMergedMultiParams(
    params1: Map[String, Seq[String]],
    params2: Map[String, Seq[String]]): Map[String, Seq[String]] = {
    (params1.toSeq ++ params2.toSeq).groupBy(_._1).mapValues(_.flatMap(_._2))
  }

  private[this] def readJsonFromStreamWithCharset(stream: InputStream, charset: String): JValue = {
    val rdr = new InputStreamReader(stream, charset)
    if (rdr.ready()) defaultObjectMapper.readValue(rdr, classOf[JValue])
    else {
      rdr.close()
      JNothing
    }
  }

  private[this] def readJsonFromBody(bd: String): JValue = {
    if (Option(bd).exists(_.trim.length > 0)) defaultObjectMapper.readValue(bd, classOf[JValue])
    else JNothing
  }

  private[this] def readXmlFromBody(bd: String): JValue = {
    if (Option(bd).exists(_.trim.length > 0)) {
      val JObject(JField(_, jv) :: Nil) = toJson(scala.xml.XML.loadString(bd))
      jv
    } else JNothing
  }

  private[this] def readXmlFromStream(stream: InputStream): JValue = {
    val rdr = new InputStreamReader(stream)
    if (rdr.ready()) {
      val JObject(JField(_, jv) :: Nil) = toJson(scala.xml.XML.load(rdr))
      jv
    } else JNothing
  }

  private[this] def shouldParseBody(fmt: String)(implicit ctx: SkinnyContext) = {
    (fmt == "json" || fmt == "xml") && !ctx.request.requestMethod.isSafe && parsedBody(ctx) == JNothing
  }

  private[this] def parsedBody(implicit ctx: SkinnyContext): JValue = {
    ctx.request.get(JSONSupport.ParsedBodyKey).fold({
      val fmt = requestFormat(ctx)
      var bd: JValue = JNothing
      if (fmt == "json" || fmt == "xml") {
        bd = parseRequestBody(fmt)(ctx)
        ctx.request(JSONSupport.ParsedBodyKey) = bd.asInstanceOf[AnyRef]
      }
      bd
    })(_.asInstanceOf[JValue])
  }

}
