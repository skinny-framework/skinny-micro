package skinny.micro.scalate

import java.io.PrintWriter

import org.fusesource.scalate.Binding
import skinny.micro.base.BeforeAfterDsl
import skinny.micro.context.SkinnyContext
import skinny.micro.i18n.{ Messages, I18nSupport }

trait ScalateI18nSupport
    extends ScalateSupport
    with I18nSupport { self: BeforeAfterDsl =>

  /*
   * Binding done here seems to work all the time.
   *
   * If it were placed in createRenderContext, it wouldn't work for "view" templates
   * on first access. However, on subsequent accesses, it worked fine.
   */
  before() {
    templateEngine.bindings ::= Binding("messages", classOf[Messages].getName, true, isImplicit = true)
  }

  /**
   * Added "messages" into the template context so it can be accessed like:
   * #{messages("hello")}
   */
  override protected def createRenderContext(out: PrintWriter)(
    implicit ctx: SkinnyContext): SkinnyScalateRenderContext = {
    val context = super.createRenderContext(out)(ctx)
    context.attributes("messages") = messages(ctx)
    context
  }

}
