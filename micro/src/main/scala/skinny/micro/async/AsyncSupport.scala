package skinny.micro.async

import skinny.micro.SkinnyMicroBase
import scala.concurrent.Future

/**
 * Async operations provider.
 */
trait AsyncSupport
    extends AsyncOperations { self: SkinnyMicroBase =>

  /**
   * true if async supported
   */
  override protected def isAsyncExecutable(result: Any): Boolean = {
    classOf[Future[_]].isAssignableFrom(result.getClass) ||
      classOf[AsyncResult].isAssignableFrom(result.getClass)
  }

}
