package org

package object scalatra {

  @deprecated("Use skinny.micro.SkinnyMicroBase instead", since = "2.0.0")
  type ScalatraBase = skinny.micro.SkinnyMicroBase

  @deprecated("Use skinny.micro.SkinnyMicroServlet instead", since = "2.0.0")
  type ScalatraServlet = skinny.micro.SkinnyMicroServlet

  @deprecated("Use skinny.micro.SkinnyMicroFilter instead", since = "2.0.0")
  type ScalatraFilter = skinny.micro.SkinnyMicroFilter

  @deprecated("Use skinny.micro.SkinnyMicroException instead", since = "2.0.0")
  type ScalatraException = skinny.micro.SkinnyMicroException

}
