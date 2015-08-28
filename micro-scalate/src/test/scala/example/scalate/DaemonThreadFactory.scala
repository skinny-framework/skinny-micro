package example.scalate

import java.util.concurrent.{ Executors, ThreadFactory }

object DaemonThreadFactory {

  def newPool() = Executors.newCachedThreadPool(new DaemonThreadFactory)

}

class DaemonThreadFactory extends ThreadFactory {

  def newThread(r: Runnable): Thread = {
    val thread = new Thread(r)
    thread setDaemon true
    thread
  }

}
