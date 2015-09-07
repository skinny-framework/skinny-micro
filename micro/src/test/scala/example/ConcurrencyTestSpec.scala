package example

import org.scalatra.test.scalatest.ScalatraFlatSpec
import skinny.micro._

import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class ConcurrencyTestSpec extends ScalatraFlatSpec {

  addFilter(new AsyncWebApp {
    get("/async") { implicit ctx =>
      Future {
        "OK"
      }
    }
    get("/sync") { implicit ctx =>
      "OK"
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

}
