package skinny.micro

import skinny.micro.data.{ MapWithIndifferentAccess, MultiMapHeadView }

/**
 * Params.
 */
class SkinnyMicroParams(
  protected val multiMap: Map[String, Seq[String]])
    extends MultiMapHeadView[String, String]
    with MapWithIndifferentAccess[String]
