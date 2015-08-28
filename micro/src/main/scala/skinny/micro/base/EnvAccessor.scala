package skinny.micro.base

import skinny.SkinnyEnv

/**
 * SkinnyEnv support.
 */
trait EnvAccessor {

  /**
   * Env string value from "skinny.env" or "org.scalatra.environment".
   *
   * @return env string such as "production"
   */
  protected def skinnyEnv: Option[String] = SkinnyEnv.get()

  /**
   * Predicates current env is "development" or "dev".
   *
   * @return true/false
   */
  protected def isDevelopment(): Boolean = SkinnyEnv.isDevelopment(skinnyEnv)

  /**
   * Predicates current env is "test".
   *
   * @return true/false
   */
  protected def isTest(): Boolean = SkinnyEnv.isTest(skinnyEnv)

  /**
   * Predicates current env is "staging" or "qa".
   *
   * @return true/false
   */
  protected def isStaging(): Boolean = SkinnyEnv.isStaging(skinnyEnv)

  /**
   * Predicates current env is "production" or "prod".
   *
   * @return true/false
   */
  protected def isProduction(): Boolean = SkinnyEnv.isProduction(skinnyEnv)

}
