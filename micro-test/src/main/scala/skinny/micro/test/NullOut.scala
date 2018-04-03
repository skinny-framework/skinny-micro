package skinny.micro.test

import java.io.OutputStream

/**
 * Null output stream.
 */
object NullOut extends OutputStream {

  def write(b: Int): Unit = {}

}
