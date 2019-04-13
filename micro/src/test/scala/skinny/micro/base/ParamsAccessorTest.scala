package skinny.micro.base

import javax.servlet.ServletContext
import javax.servlet.http.{ HttpServletRequest, HttpServletResponse }
import org.mockito.Mockito
import org.mockito.stubbing.Stubber
import org.scalatest.{ FunSuite, Matchers }
import org.scalatestplus.mockito.MockitoSugar
import skinny.micro.UnstableAccessValidation
import skinny.micro.context.SkinnyContext
import skinny.micro.implicits.ServletApiImplicits

class ParamsAccessorTest extends FunSuite with Matchers with MockitoSugar with ServletApiImplicits {

  test("Compatibility with Scala 2.13.0-RC1") {
    val paramsAccessor = new ParamsAccessor {}
    implicit val ctx = new SkinnyContext {
      override val request: HttpServletRequest = mock[HttpServletRequest]
      override val response: HttpServletResponse = mock[HttpServletResponse]
      override val servletContext: ServletContext = mock[ServletContext]
      override val unstableAccessValidation: UnstableAccessValidation =
        UnstableAccessValidation(enabled = true, useMostlyStableHttpSession = true)
    }
    val emptyMap = Map.empty[String, Seq[String]]
    doReturn(emptyMap).when(ctx.request).get(skinny.micro.MultiParamsKey)

    val multiParams = paramsAccessor.multiParams
    multiParams shouldNot be(null)
  }

  // This workaround is necessary for Scala 2.12-
  // https://newfivefour.com/scala-ambiguous-reference-to-overloaded-defintion.html
  private def doReturn(obj: Any): Stubber = {
    classOf[Mockito]
      .getMethod("doReturn", classOf[Object])
      .invoke(null, Seq(obj.asInstanceOf[Object]): _*)
      .asInstanceOf[Stubber]
  }

}