package skinny.micro.contrib

import javax.servlet.http.{ HttpServletRequest, HttpServletResponse }

import skinny.micro.{ ContentEncoding, ContentNegotiation, Handler, SkinnyMicroBase }

/**
 * SkinnyMicro handler for gzipped responses.
 */
trait ContentEncodingSupport extends Handler { self: SkinnyMicroBase =>

  abstract override def handle(
    req: HttpServletRequest,
    res: HttpServletResponse): Unit = {
    withRequestResponse(req, res) {
      super.handle(decodedRequest(req), encodedResponse(req, res))
    }
  }

  /** Encodes the response if necessary. */
  private def encodedResponse(
    req: HttpServletRequest,
    res: HttpServletResponse): HttpServletResponse = {
    ContentNegotiation.preferredEncoding(req).map { encoding =>
      val encoded = encoding(res)
      SkinnyMicroBase.onRenderedCompleted { _ => encoded.end() }(context)
      encoded
    }.getOrElse(res)
  }

  /** Decodes the request if necessary. */
  private def decodedRequest(req: HttpServletRequest): HttpServletRequest = {
    (for {
      name: String <- Option(req.getHeader("Content-Encoding"))
      enc: ContentEncoding <- ContentEncoding.forName(name)
    } yield enc.apply(req)).getOrElse(req)
  }

}
