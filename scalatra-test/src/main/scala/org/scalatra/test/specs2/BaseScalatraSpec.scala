package org.scalatra
package test
package specs2

import org.specs2.specification.core.SpecificationStructure
import org.specs2.specification.BeforeAfterAll

/**
 * A base specification structure that starts the tester before the
 * specification and stops it afterward.  Clients probably want to extend
 * ScalatraSpec or MutableScalatraSpec.
 */
trait BaseScalatraSpec
  extends BeforeAfterAll
  with ScalatraTests {

  override def beforeAll = start()
  override def afterAll = stop()

}
