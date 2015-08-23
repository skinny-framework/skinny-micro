package skinny.micro

import javax.servlet.{ ServletContext, ServletContextEvent, ServletContextListener }

import skinny.micro.implicits.RicherStringImplicits
import skinny.logging.LoggerProvider
import SkinnyListener._

class SkinnyListener extends ServletContextListener with LoggerProvider {

  import RicherStringImplicits._

  private[this] var servletContext: ServletContext = _

  private[this] var cycle: LifeCycle = _

  protected def loadCycleClassName: String = {
    Option(servletContext.getInitParameter(LifeCycleKey))
      .flatMap(_.blankOption)
      .getOrElse(DefaultLifeCycle)
  }

  override def contextInitialized(sce: ServletContextEvent): Unit = {
    try {
      configureServletContext(sce)
      configureCycleClass(Thread.currentThread.getContextClassLoader)
    } catch {
      case scala.util.control.NonFatal(e) =>
        logger.error("Failed to initialize skinny application at " + sce.getServletContext.getContextPath, e)
        throw e
    }
  }

  override def contextDestroyed(sce: ServletContextEvent): Unit = {
    if (cycle != null) {
      logger.info("Destroying life cycle class: %s".format(cycle.getClass.getName))
      cycle.destroy(servletContext)
    }
  }

  protected def configureServletContext(sce: ServletContextEvent): Unit = {
    servletContext = sce.getServletContext
  }

  protected def configureCycleClass(classLoader: ClassLoader): Unit = {
    val cycleClassName = loadCycleClassName
    logger.info("The cycle class name from the config: " + (if (cycleClassName == null) "null" else cycleClassName))

    val cycleClass: Class[_] = {
      val lifeCycleClass: Option[Class[_]] = {
        try { Some(Class.forName(cycleClassName, true, classLoader)) }
        catch {
          case e: ClassNotFoundException =>
            logger.debug(s"Failed to load cycle class name: ${cycleClassName}")
            None
          case t: Throwable => throw t
        }
      }
      lifeCycleClass match {
        case Some(clazz) => clazz
        case _ => {
          try { Class.forName(OldDefaultLifeCycle, true, classLoader) }
          catch {
            case _: ClassNotFoundException => null
            case t: Throwable => throw t
          }
        }
      }
    }

    assert(cycleClass != null,
      """----------------
        |
        | *** No skinny.micro.LifeCycle class found! ****
        |
        | To fix this issue:
        |
        | echo 'import skinny._
        |import _root_.controller._
        |
        |class Bootstrap extends SkinnyLifeCycle {
        |  override def initSkinnyApp(ctx: ServletContext) {
        |    Controllers.mount(ctx)
        |  }
        |}
        |' > src/main/scala/Bootstrap.scala
        |
        | NOTE: If you're using only skinny.micro, inherit skinny.micro.LifeCycle instead.
        |
        |----------------
        |""".stripMargin)
    logger.debug(s"Loaded lifecycle class: ${cycleClass}")

    if (cycleClass.getName == OldDefaultLifeCycle) {
      logger.warn(s"${OldDefaultLifeCycle} for a boot class will be removed eventually. " +
        s"Please use ${DefaultLifeCycle} instead as class name.")
    }
    cycle = cycleClass.newInstance.asInstanceOf[LifeCycle]
    logger.info("Initializing life cycle class: %s".format(cycleClassName))
    cycle.init(servletContext)
  }
}

object SkinnyListener {

  val DefaultLifeCycle: String = "Bootstrap"

  // DO NOT RENAME THIS CLASS NAME AS IT BREAKS THE ENTIRE WORLD
  // TOGETHER WITH THE WORLD IT WILL BREAK ALL EXISTING SCALATRA APPS
  // RENAMING THIS CLASS WILL RESULT IN GETTING SHOT, IF YOU SURVIVE YOU WILL BE SHOT AGAIN
  val OldDefaultLifeCycle: String = "ScalatraBootstrap"
  val LifeCycleKey: String = "skinny.micro.LifeCycle"

}
