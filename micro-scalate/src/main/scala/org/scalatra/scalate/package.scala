package org.scalatra

package object scalate {

  @deprecated(s"Use ${classOf[skinny.micro.scalate.SkinnyScalateRenderContext].getName} instead", since = "1.0.0")
  type ScalatraRenderContext = skinny.micro.scalate.SkinnyScalateRenderContext

}
