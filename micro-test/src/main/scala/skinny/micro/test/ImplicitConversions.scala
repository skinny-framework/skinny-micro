package skinny.micro.test

import scala.language.implicitConversions

/**
 * Contains implicit conversions for making test DSL easier to use.
 * This is included by all `Client` implementations.
 */
private[skinny] trait ImplicitConversions {

  /**
   * Converts String value to byte array on demand.
   */
  implicit def stringToByteArray(str: String): Array[Byte] = str.getBytes("UTF-8")

}
