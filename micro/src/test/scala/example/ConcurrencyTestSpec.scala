package example

import org.scalatra.test.scalatest.ScalatraFlatSpec
import skinny.micro._

import scala.concurrent._
import scala.concurrent.duration._

class ConcurrencyTestSpec extends ScalatraFlatSpec {

  implicit val context = SkinnyMicroBase.defaultExecutionContext

  addFilter(new AsyncWebApp {
    get("/async") { implicit ctx =>
      Future {
        "OK"
      }
    }
    get("/sync") { implicit ctx =>
      "OK"
    }
    get("/content-type-1") { implicit ctx =>
      Thread.sleep(50)
      Future {
        Thread.sleep(50)
        contentType = "text/csv"
        Thread.sleep(20)
        "foo,bar,baz"
      }
    }
    get("/content-type-2") { implicit ctx =>
      Thread.sleep(50)
      contentType = "text/csv"
      Future {
        Thread.sleep(20)
        "foo,bar,baz"
      }
    }

    get("/content-type-3") { implicit ctx =>
      Thread.sleep(50)
      Future {
        Thread.sleep(50)
        Ok(body = "foo,bar,baz", contentType = Some("text/csv"))
      }
    }
  }, "/*")

  it should "work fine in sync mode for concurrent requests" in {
    val listOfFutureBodies = (1 to 3).map(_ => Future { get("/sync") { body } })
    val fListOfBodies = Future.sequence(listOfFutureBodies)
    Await.result(fListOfBodies, 3.seconds).foreach(_ should equal("OK"))
  }

  it should "work fine in async mode for sequential requests" in {
    (1 to 10).foreach { _ =>
      get("/async") { body should equal("OK") }
    }
  }

  it should "work fine in async mode for concurrent requests" in {
    val listOfFutureBodies = (1 to 3).map(_ => Future { get("/async") { body } })
    val fListOfBodies = Future.sequence(listOfFutureBodies)
    Await.result(fListOfBodies, 3.seconds).foreach(_ should equal("OK"))
  }

  it should "work fine in sync mode for concurrent requests 2" in {
    val listOfFutureBodies = (1 to 3).map(_ => Future { get("/sync") { body } })
    val fListOfBodies = Future.sequence(listOfFutureBodies)
    Await.result(fListOfBodies, 3.seconds).foreach(_ should equal("OK"))
  }

  it should "work fine in async mode for sequential requests 2" in {
    (1 to 10).foreach { _ =>
      get("/async") { body should equal("OK") }
    }
  }

  it should "set content-type in async mode 1" in {
    (1 to 5).foreach { _ =>
      Await.result(Future.sequence((1 to 10).map { _ =>
        Future {
          get("/content-type-1") {
            status should equal(200)
            header("Content-Type") should equal("text/csv; charset=UTF-8")
            body should equal("foo,bar,baz")
          }
        }
      }), 3.seconds)
    }
  }

  it should "set content-type in async mode 2" in {
    (1 to 5).foreach { _ =>
      Await.result(Future.sequence((1 to 10).map { _ =>
        Future {
          get("/content-type-2") {
            status should equal(200)
            header("Content-Type") should equal("text/csv; charset=UTF-8")
            body should equal("foo,bar,baz")
          }
        }
      }), 3.seconds)
    }
  }

  it should "set content-type in async mode 3" in {
    (1 to 5).foreach { _ =>
      Await.result(Future.sequence((1 to 10).map { _ =>
        Future {
          get("/content-type-3") {
            status should equal(200)
            header("Content-Type") should equal("text/csv; charset=UTF-8")
            body should equal("foo,bar,baz")
          }
        }
      }), 3.seconds)
    }
  }

}
