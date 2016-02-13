package skinny.concurrent

import java.util.concurrent.{ ExecutorService, Executors }
import skinny.logging.LoggerProvider

import scala.concurrent.{ ExecutionContextExecutor, ExecutionContext }

/**
 * A factory which creates scala.concurrent.ExecutionContext instances.
 */
object ExecutionContextFactory extends LoggerProvider {

  /**
   * Returns sized thread pool EC instance.
   *
   * @param numberOfThreads number of threads
   * @return ExecutionContext
   */
  def create(numberOfThreads: Int): ExecutionContextExecutor = {
    logger.debug(s"An ExecutionContext ($numberOfThreads threads) is newly created.")
    val executorService: ExecutorService = Executors.newFixedThreadPool(numberOfThreads)
    ExecutionContext.fromExecutor(executorService)
  }

}
