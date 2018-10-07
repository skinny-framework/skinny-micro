package skinny.micro.contrib

import java.io.{ PrintWriter, StringWriter }

import javax.servlet.{ FilterConfig, ServletConfig }
import javax.servlet.http.{ HttpServletRequest, HttpServletResponse }
import org.fusesource.scalate.layout.DefaultLayoutStrategy
import org.fusesource.scalate.servlet.ServletTemplateEngine
import org.fusesource.scalate.support.TemplateFinder
import org.fusesource.scalate.{ Binding, TemplateEngine }
import skinny.micro._
import skinny.micro.context.{ SkinnyContext, ThinServletBaseConfig }
import skinny.micro.contrib.scalate.SkinnyScalateRenderContext

import scala.collection.concurrent.{ TrieMap, Map => CMap }
import scala.collection.mutable

/**
 * ScalateSupport creates and configures a template engine and provides
 * helper methods and bindings to integrate with the ServletBase.
 */
trait ScalateSupport extends SkinnyMicroBase {

  /**
   * The template engine used by the methods in this support class.  It
   * provides a lower-level interface to Scalate and may be used directly
   * to circumvent the conventions imposed by the helpers in this class.
   * For instance, paths passed directly to the template engine are not
   * run through `findTemplate`.
   */
  protected[this] var templateEngine: TemplateEngine = _

  abstract override def initialize(config: ThinServletBaseConfig): Unit = {
    super.initialize(config)
    templateEngine = createTemplateEngine(config)
  }

  abstract override def shutdown(): Unit = {
    if (templateEngine != null) {
      templateEngine.compiler.shutdown()
    }
    super.shutdown()
  }

  private[this] lazy val nonServletTemplateEngine: TemplateEngine = {
    new TemplateEngine with SkinnyMicroTemplateEngine
  }

  /**
   * Creates the templateMicro from the config.  There is little reason to
   * override this unless you have created a ServletBase extension outside
   * an HttpServlet or Filter.
   */
  protected def createTemplateEngine(config: ThinServletBaseConfig): TemplateEngine = {
    val contextPath = config.getServletContext.getContextPath match {
      case "" => "ROOT"
      case path => path.substring(1)
    }
    config.getBaseConfigType match {
      case ThinServletBaseConfig.BaseConfigType.FilterConfig =>
        config.getFilterConfig match {
          case Some(filterConfig: FilterConfig) =>
            val templateEngine = new ServletTemplateEngine(filterConfig) with SkinnyMicroTemplateEngine
            ScalateSupport.scalateTemplateEngine(contextPath, templateEngine)
          case _ => throw new IllegalStateException(
            "ThinServletBaseConfig#getFilterConfig should return a Some value when BaseConfigType is FilterConfig")
        }
      case ThinServletBaseConfig.BaseConfigType.ServletConfig =>
        config.getServletConfig match {
          case Some(servletConfig: ServletConfig) =>
            val templateEngine = new ServletTemplateEngine(servletConfig) with SkinnyMicroTemplateEngine
            ScalateSupport.scalateTemplateEngine(contextPath, templateEngine)
          case _ => throw new IllegalStateException(
            "ThinServletBaseConfig#getServletConfig should return a Some value when BaseConfigType is ServletConfig")
        }
      case _ =>
        // Don't know how to convert your Config to something that
        // ServletTemplateEngine can accept, so fall back to a TemplateEngine
        ScalateSupport.scalateTemplateEngine(contextPath, nonServletTemplateEngine)
    }
  }

  /**
   * A TemplateEngine integrated with SkinnyMicro.
   *
   * A SkinnyMicroTemplateEngine looks for layouts in `/WEB-INF/templates/layouts` before
   * searching in `/WEB-INF/layouts` and `/WEB-INF/scalate/layouts`.
   */
  trait SkinnyMicroTemplateEngine { self: TemplateEngine =>

    /**
     * Delegates to the ServletBase's isDevelopmentMode flag.
     */
    override def isDevelopmentMode: Boolean = ScalateSupport.this.isDevelopment() || ScalateSupport.this.isTest()

    ScalateSupport.setLayoutStrategy(self)

    templateDirectories = defaultTemplatePath

    bindings ::= Binding(
      name = "context",
      className = s"_root_.${classOf[SkinnyScalateRenderContext].getName}",
      importMembers = true,
      isImplicit = true)

    importStatements ::= s"import ${classOf[skinny.micro.implicits.ServletApiImplicits].getName}._"

  }

  /**
   * Creates a render context to be used by default in the template engine.
   *
   * Returns a SkinnyMicroRenderContext by default in order to bind some other
   * framework variables (e.g., multiParams, flash).  SkinnyMicroTemplateEngine
   * assumes this returns SkinnyMicroRenderContext in its binding of "context".
   * If you return something other than a SkinnyMicroRenderContext, you will
   * also want to redefine that binding.
   */
  protected def createRenderContext(out: PrintWriter)(implicit ctx: SkinnyContext): SkinnyScalateRenderContext = {
    new SkinnyScalateRenderContext(this, ctx, templateEngine, out)
  }

  /**
   * Flag whether the Scalate error page is enabled.  If true, uncaught
   * exceptions will be caught and rendered by the Scalate error page.
   *
   * The default is true.
   */
  protected def isScalateErrorPageEnabled = true

  abstract override def handle(req: HttpServletRequest, res: HttpServletResponse): Unit = {
    super.handle(req, res)
  }

  override protected def renderUncaughtException(e: Throwable)(implicit ctx: SkinnyContext): Unit = {
    if (isScalateErrorPageEnabled) renderScalateErrorPage(e)(ctx)
    else super.renderUncaughtException(e)(ctx)
  }

  private[this] def renderScalateErrorPage(e: Throwable)(implicit context: SkinnyContext) = {
    context.response.setStatus(500)
    context.response.setContentType("text/html")
    val errorPage = templateEngine.load("/WEB-INF/scalate/errors/500.scaml")
    val ctx = createRenderContext(context.response.getWriter)(context)
    ctx.setAttribute("javax.servlet.error.exception", Some(e))
    templateEngine.layout(errorPage, ctx)
  }

  /**
   * The default index page when the path is a directory.
   */
  protected def defaultIndexName: String = "index"

  /**
   * The default template format.
   */
  protected def defaultTemplateFormat: String = "jade"

  /**
   * The default path to search for templates.  Left as a def so it can be
   * read from the servletContext in initialize, but you probably want a
   * constant.
   *
   * Defaults to:
   * - `/WEB-INF/templates/views` (recommended)
   * - `/WEB-INF/views` (used by previous SkinnyMicro quickstarts)
   * - `/WEB-INF/scalate/templates` (used by previous SkinnyMicro quickstarts)
   */
  protected def defaultTemplatePath: List[String] =
    List(
      "/WEB-INF/templates/views",
      "/WEB-INF/views",
      "/WEB-INF/scalate/templates")

  /**
   * The default path to search for templates.  Left as a def so it can be
   * read from the servletContext in initialize, but you probably want a
   * constant.
   *
   * Defaults to:
   * - `/WEB-INF/templates/views` (recommended)
   * - `/WEB-INF/views` (used by previous SkinnyMicro quickstarts)
   * - `/WEB-INF/scalate/templates` (used by previous SkinnyMicro quickstarts)
   */
  protected def defaultLayoutPath: Option[String] = None

  /**
   * Convenience method for `layoutTemplateAs("jade")`.
   */
  protected def jade(path: String, attributes: (String, Any)*)(implicit ctx: SkinnyContext): String = {
    layoutTemplateAs(Set("jade"))(path, attributes: _*)(ctx)
  }

  /**
   * Convenience method for `layoutTemplateAs("scaml")`.
   */
  protected def scaml(path: String, attributes: (String, Any)*)(implicit ctx: SkinnyContext): String = {
    layoutTemplateAs(Set("scaml"))(path, attributes: _*)(ctx)
  }

  /**
   * Convenience method for `layoutTemplateAs("ssp")`.
   */
  protected def ssp(path: String, attributes: (String, Any)*)(implicit ctx: SkinnyContext): String = {
    layoutTemplateAs(Set("ssp"))(path, attributes: _*)(ctx)
  }

  /**
   * Convenience method for `layoutTemplateAs("mustache")`.
   */
  protected def mustache(path: String, attributes: (String, Any)*)(implicit ctx: SkinnyContext): String = {
    layoutTemplateAs(Set("mustache"))(path, attributes: _*)(ctx)
  }

  /**
   * Finds and renders a template with the current layout strategy,
   * returning the result.
   *
   * @param ext The extensions to look for a template.
   * @param path The path of the template, passed to `findTemplate`.
   * @param attributes Attributes to path to the render context.  Disable
   * layouts by passing `layout -> ""`.
   */
  protected def layoutTemplateAs(ext: Set[String])(path: String, attributes: (String, Any)*)(
    implicit
    ctx: SkinnyContext): String = {
    val uri = findTemplate(path, ext).getOrElse(path)
    using(new StringWriter()) { buffer =>
      using(new PrintWriter(buffer)) { out =>
        val attrs: mutable.Map[String, Any] = {
          templateAttributes(ctx) ++
            defaultLayoutPath.map(p => Map("layout" -> p) ++
              Map(attributes: _*)).getOrElse(Map(attributes: _*))
        }
        val context = createRenderContext(out)(ctx)
        attrs.foreach { case (k, v) => context.attributes(k) = v }
        templateEngine.layout(uri, context)
        buffer.toString
      }
    }
  }

  /**
   * Finds and renders a template with the current layout strategy,
   * looking for all known extensions, returning the result.
   *
   * @param path The path of the template, passed to `findTemplate`.
   * @param attributes Attributes to path to the render context.  Disable
   * layouts by passing `layout -> ""`.
   */
  protected def layoutTemplate(path: String, attributes: (String, Any)*)(implicit ctx: SkinnyContext): String = {
    layoutTemplateAs(templateEngine.extensions)(path, attributes: _*)(ctx)
  }

  /**
   * Finds a template for a path.  Delegates to a TemplateFinder, and if
   * that fails, tries again with `/defaultIndexName` appended.
   */
  protected def findTemplate(path: String, extensionSet: Set[String] = templateEngine.extensions): Option[String] = {
    val finder = new TemplateFinder(templateEngine) {
      override lazy val extensions = extensionSet
    }
    finder.findTemplate(("/" + path).replaceAll("//", "/"))
      .orElse(finder.findTemplate("/%s/%s".format(path, defaultIndexName)))
  }

  /**
   * A request-scoped map of attributes to pass to the template.  This map
   * will be set to any render context created with the `createRenderContext`
   * method.
   */
  protected def templateAttributes(implicit ctx: SkinnyContext): mutable.Map[String, Any] = {
    ctx.request.getOrElseUpdate(ScalateSupport.TemplateAttributesKey, mutable.Map.empty)
      .asInstanceOf[mutable.Map[String, Any]]
  }

  protected def templateAttributes(key: String)(implicit ctx: SkinnyContext): Any = {
    templateAttributes(ctx)(key)
  }

}

object ScalateSupport {

  val DefaultLayouts = Seq(
    "/WEB-INF/templates/layouts/default",
    "/WEB-INF/layouts/default",
    "/WEB-INF/scalate/layouts/default")

  private def setLayoutStrategy(engine: TemplateEngine) = {
    val layouts = for {
      base <- ScalateSupport.DefaultLayouts
      extension <- TemplateEngine.templateTypes
    } yield ("%s.%s".format(base, extension))
    engine.layoutStrategy = new DefaultLayoutStrategy(engine, layouts: _*)
  }

  private val TemplateAttributesKey = "skinny.micro.scalate.ScalateSupport.TemplateAttributes"

  private val templateEngineInstances: CMap[String, TemplateEngine] = new TrieMap[String, TemplateEngine]

  def scalateTemplateEngine(contextPath: String, init: => TemplateEngine): TemplateEngine = {
    templateEngineInstances.get(contextPath).getOrElse {
      val engine = init
      engine.workingDirectory = new java.io.File(engine.workingDirectory, contextPath)
      templateEngineInstances.putIfAbsent(contextPath, engine).getOrElse(engine)
    }
  }
}
