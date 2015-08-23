package example.scalate

import java.util.concurrent._

import org.fusesource.scalate.layout.DefaultLayoutStrategy
import skinny.micro._
import skinny.micro.async.AsyncResult
import skinny.micro.base.FlashMapSupport
import skinny.micro.routing.Route
import skinny.micro.scalate.{ ScalateSupport, ScalateUrlGeneratorSupport }

import scala.concurrent.{ ExecutionContext, Future }

class ScalateFuturesSupportServlet(exec: ExecutorService)
    extends SkinnyMicroServlet
    with ScalateSupport
    with ScalateUrlGeneratorSupport
    with FlashMapSupport {

  protected override implicit val executionContext = ExecutionContext.fromExecutorService(exec)

  protected override def unstableAccessValidationEnabled = false

  get("/barf") {
    new AsyncResult { val is = Future { throw new RuntimeException } }
  }

  get("/happy-happy") {
    new AsyncResult { val is = Future { "puppy dogs" } }
  }

  get("/simple-template") {
    new AsyncResult { val is = Future { layoutTemplate("/simple.jade") } }
  }

  get("/params") {
    new AsyncResult { val is = Future { layoutTemplate("/params.jade", "foo" -> "Configurable") } }
  }

  get("/jade-template") {
    new AsyncResult { val is = Future { jade("simple") } }
  }

  get("/jade-params") {
    new AsyncResult { val is = Future { jade("params", "foo" -> "Configurable") } }
  }

  get("/scaml-template") {
    new AsyncResult { val is = Future { scaml("simple") } }
  }

  get("/scaml-params") {
    new AsyncResult { val is = Future { scaml("params", "foo" -> "Configurable") } }
  }

  get("/ssp-template") {
    new AsyncResult { val is = Future { ssp("simple") } }
  }

  get("/ssp-params") {
    new AsyncResult { val is = Future { ssp("params", "foo" -> "Configurable") } }
  }

  get("/mustache-template") {
    new AsyncResult { val is = Future { mustache("simple") } }
  }

  get("/mustache-params") {
    new AsyncResult { val is = Future { mustache("params", "foo" -> "Configurable") } }
  }

  get("/layout-strategy") {
    new AsyncResult {
      val is = Future {
        templateEngine.layoutStrategy.asInstanceOf[DefaultLayoutStrategy].defaultLayouts mkString ";"
      }
    }
  }

  val urlGeneration: Route = get("/url-generation") {
    new AsyncResult { val is = Future { layoutTemplate("/urlGeneration.jade") } }
  }

  val urlGenerationWithParams: Route = get("/url-generation-with-params/:a/vs/:b") {
    new AsyncResult {
      val is = Future {
        layoutTemplate("/urlGenerationWithParams.jade", ("a" -> params("a")), ("b" -> params("b")))
      }
    }
  }

  get("/legacy-view-path") {
    new AsyncResult { val is = Future { jade("legacy") } }
  }

  get("/directory") {
    new AsyncResult { val is = Future { jade("directory/index") } }
  }

  get("/bindings/*") {
    new AsyncResult {
      val is =
        Future {
          flash.now("message") = "flash works"
          session("message") = "session works"
          jade(requestPath)
        }
    }
  }

  get("/bindings/params/:foo") {
    new AsyncResult { val is = Future { jade("/bindings/params") } }
  }

  get("/bindings/multiParams/*/*") {
    new AsyncResult { val is = Future { jade("/bindings/multiParams") } }
  }

  get("/template-attributes") {
    new AsyncResult {
      val is =
        Future {
          templateAttributes("foo") = "from attributes"
          scaml("params")
        }
    }
  }

  get("/render-to-string") {
    new AsyncResult {
      val is = Future {
        response.setHeader("X-Template-Output", layoutTemplate("simple"))
      }
    }
  }
}

object ScalateFuturesSupportServlet
  extends ScalateFuturesSupportServlet(DaemonThreadFactory.newPool())