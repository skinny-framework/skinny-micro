package org.scalatra

package object servlet {

  @deprecated("Use skinny.micro.SkinnyMicroListener instead", since = "2.0.0")
  type ScalatraListener = skinny.micro.SkinnyMicroListener

  @deprecated("Use skinny.micro.SkinnyMicroBase instead", since = "2.0.0")
  type ServletBase = skinny.micro.SkinnyMicroBase

}
