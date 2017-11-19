package skinny.micro.request

import java.io.{ BufferedReader, InputStreamReader }
import javax.servlet.http.{ HttpServletRequest, HttpServletRequestWrapper }

import skinny.micro.ContentEncoding

/**
 * Decoded servlet request.
 */
private[skinny] class DecodedServletRequest(
  req: HttpServletRequest,
  enc: ContentEncoding) extends HttpServletRequestWrapper(req) {

  override lazy val getInputStream: EncodedInputStream = {
    val raw = req.getInputStream
    new EncodedInputStream(enc.decode(raw), raw)
  }

  override lazy val getReader: BufferedReader = {
    new BufferedReader(new InputStreamReader(getInputStream, getCharacterEncoding))
  }

  override def getContentLength: Int = -1

}
