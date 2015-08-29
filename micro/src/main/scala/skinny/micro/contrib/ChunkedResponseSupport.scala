package skinny.micro.contrib

import javax.servlet.ServletOutputStream

import skinny.logging.LoggerProvider
import skinny.micro.{ Context, SkinnyMicroBase }

/**
 * Chunked Response (Transfer-Encoding: chunked).
 */
trait ChunkedResponseSupport { self: SkinnyMicroBase with LoggerProvider =>

  def withOutputStream(f: ServletOutputStream => Unit)(implicit ctx: Context): Unit = {
    val stream = response.getOutputStream
    try {
      f.apply(stream)
    } catch {
      case e: Exception if Option(e.getCause).isDefined =>
        val message = Option(e.getMessage).getOrElse(Option(e.getCause).map(_.getMessage).getOrElse(""))
        if (logger.isDebugEnabled) {
          logger.debug(s"Chunked response error (message: ${message})", e)
        } else {
          logger.info(s"Chunked response error (message: ${message})")
        }
        try stream.close() catch {
          case scala.util.control.NonFatal(e) =>
            logger.debug(s"Failed to close output stream because ${e.getMessage})", e)
        }
    }
  }

  def writeChunk(chunk: Array[Byte])(implicit ctx: Context): Unit = {
    val stream = response.getOutputStream
    Option(chunk).foreach { c =>
      stream.write(c)
      stream.flush()
    }
  }

}
