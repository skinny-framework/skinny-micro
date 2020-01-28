package skinny

import org.scalatest._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class SkinnyEnvSpec extends AnyFlatSpec with Matchers {

  behavior of "SkinnyEnv"

  // affects other tests

  //  it should "work with prop value" in {
  //    System.setProperty(SkinnyEnv.PropertyKey, "foo1")
  //    System.clearProperty(SkinnyEnv.EnvKey)
  //    SkinnyEnv.get() should equal(Some("foo1"))
  //
  //    System.clearProperty(SkinnyEnv.PropertyKey)
  //    System.setProperty(SkinnyEnv.EnvKey, "foo2")
  //    SkinnyEnv.get() should equal(Some("foo2"))
  //  }
  //
  //  it should "work with env value" in {
  //    System.clearProperty(SkinnyEnv.PropertyKey)
  //    System.clearProperty(SkinnyEnv.EnvKey)
  //    val env = SkinnyEnv.get()
  //    println("SKINNY_ENV: " + env)
  //  }

}
