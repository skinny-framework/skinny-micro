package org.scalatra

import skinny.micro.SkinnyScalateRenderContext

package object scalate {

  @deprecated(s"Use ${classOf[SkinnyScalateRenderContext].getName} instead", since = "1.0.0")
  type ScalatraRenderContext = SkinnyScalateRenderContext

}
