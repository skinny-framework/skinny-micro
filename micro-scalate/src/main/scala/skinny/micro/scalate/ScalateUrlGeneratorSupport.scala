package skinny.micro.scalate

import java.io.PrintWriter

import org.fusesource.scalate.Binding
import skinny.micro.context.SkinnyContext
import skinny.micro.routing.Route

trait ScalateUrlGeneratorSupport extends ScalateSupport {

  lazy val reflectRoutes: Map[String, Route] = {
    this.getClass.getDeclaredMethods
      .filter(_.getParameterTypes.isEmpty)
      .filter(f => classOf[Route].isAssignableFrom(f.getReturnType))
      .map(f => (f.getName, f.invoke(this).asInstanceOf[Route]))
      .toMap
  }

  override protected def createTemplateEngine(config: ConfigT) = {
    val engine = super.createTemplateEngine(config)
    val routeBindings = this.reflectRoutes.keys.map(k => Binding(k, classOf[Route].getName))
    engine.bindings = engine.bindings ::: routeBindings.toList
    engine
  }

  override protected def createRenderContext(out: PrintWriter)(implicit ctx: SkinnyContext) = {
    val context = super.createRenderContext(out)(ctx)
    for ((name, route) <- this.reflectRoutes) {
      context.attributes.update(name, route)
    }
    context
  }
}
