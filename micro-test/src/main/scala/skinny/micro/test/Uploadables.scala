package skinny.micro.test

import java.io.{ ByteArrayOutputStream, File, FileInputStream }

import scala.annotation.tailrec

/**
 * File part entity.
 */
case class FilePart(
  file: File,
  contentType: String = "application/octet-stream"
)
    extends Uploadable {

  lazy val content = {
    val fin = new FileInputStream(file)
    val bos = new ByteArrayOutputStream()
    val buf = new Array[Byte](4096)

    @tailrec
    def copyStream() {
      val bytesRead = fin.read(buf)
      if (bytesRead > 0) {
        bos.write(buf, 0, bytesRead)
        copyStream()
      }
    }

    try {
      copyStream()
      bos.toByteArray
    } finally {
      fin.close()
    }
  }

  def contentLength = file.length()

  def fileName = file.getName

}

/**
 * Bytes entity.
 */
case class BytesPart(
  fileName: String,
  content: Array[Byte],
  contentType: String = "application/octet-stream"
)
    extends Uploadable {

  def contentLength = content.length.toLong

}
