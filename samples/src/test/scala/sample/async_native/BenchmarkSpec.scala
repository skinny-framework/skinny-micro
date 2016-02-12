package sample.async_native

import skinny.test.SkinnyFunSpec

import java.util.concurrent.Executors
import scala.concurrent._
import scala.concurrent.duration._

/*
Mostly same performance

jackson: 2.59 ms / request
json4s:  2.57 ms / request

jackson: 2.58 ms / request
json4s:  2.61 ms / request

jackson: 2.45 ms / request
json4s:  2.58 ms / request

jackson: 2.51 ms / request
json4s:  2.81 ms / request
 */
class BenchmarkSpec extends SkinnyFunSpec {

  implicit val ec: ExecutionContextExecutor = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(30))

  addFilter(classOf[EchoApp], "/*")
  addFilter(classOf[Json4sEchoApp], "/*")

  describe("JSON serialization performance") {

    it("warm up") {
      (1 to 50).foreach { _ =>
        get("/echo.json", "name" -> "Alice", "age" -> "18") {
          if (status != 200) println(body)
          status should equal(200)
        }
        get("/json4s/echo.json", "name" -> "Alice", "age" -> "18") {
          if (status != 200) println(body)
          status should equal(200)
        }
      }
    }

    it("should work with jackson-scala-module") {
      val before = System.currentTimeMillis
      val requests = 300
      val futures: Seq[Future[Unit]] = (1 to requests).map { i =>
        Future {
          get("/echo.json", "name" -> "Alice", "age" -> i.toString) {
            if (status != 200) println(body)
            status should equal(200)
          }
        }
      }
      Await.result(Future.sequence(futures), 5.seconds)

      val time = "%1.02f".format((System.currentTimeMillis - before).toDouble / requests)
      println(s"jackson: ${time} ms / request")
    }

    it("should work with json4s") {
      val before = System.currentTimeMillis
      val requests = 300
      val futures: Seq[Future[Unit]] = (1 to requests).map { i =>
        Future {
          get("/json4s/echo.json", "name" -> "Alice", "age" -> i.toString) {
            if (status != 200) println(body)
            status should equal(200)
          }
        }
      }
      Await.result(Future.sequence(futures), 5.seconds)

      val time = "%1.02f".format((System.currentTimeMillis - before).toDouble / requests)
      println(s"json4s:  ${time} ms / request")
    }

  }

}
