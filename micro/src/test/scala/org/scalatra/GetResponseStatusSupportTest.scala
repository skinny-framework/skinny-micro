package org.scalatra

import org.scalatest.exceptions.TestFailedException
import org.scalatra.test.scalatest.ScalatraFunSuite
import skinny.micro.SkinnyMicroServlet

class GetResponseStatusSupportTestServlet extends SkinnyMicroServlet {
  before() {
    request.getSession(true) // Establish a session before we commit the response
  }

  after() {
    session.setAttribute("status", status.toString)
  }

  get("/status/:status") {
    response.setStatus(params("status").toInt)
    status.toString
  }

  get("/redirect") {
    response.sendRedirect("/")
  }

  get("/session-status") {
    session.getOrElse("status", "none")
  }

  get("/send-error/:status") {
    response.sendError(params("status").toInt)
  }

  get("/send-error/:status/:msg") {
    response.sendError(params("status").toInt, params("msg"))
  }
}

class GetResponseStatusSupportTest extends ScalatraFunSuite {
  addServlet(classOf[GetResponseStatusSupportTestServlet], "/*")

  test("remember status after setStatus") {
    get("/status/404") {
      body should equal("404")
    }
  }

  def verifySession(path: String, status: Int): Unit = {
    def _verifySession(path: String, status: Int, count: Int): Unit = try {
      session {
        get(path) { status should equal(status) }
        get("/session-status") { body should equal(status.toString) }
      }
    } catch {
      case _: TestFailedException if count < 5 => _verifySession(path, status, count + 1)
      case e: TestFailedException => throw e
    }
    _verifySession(path, status, 1)
  }

  test("remembers status after sendRedirect") {
    verifySession("/redirect", 302)
  }

  test("remembers status after sendError without a message") {
    verifySession("/send-error/500", 500)
  }

  test("remembers status after sendError with a message") {
    verifySession("/send-error/504/Gateway%20Timeout", 504)
  }
}
