package skinny.json4s

/**
 * JSON String operations for Angular application's server side.
 *
 * - camelCase keys
 * - JSON vulnerability protection enabled by default
 */
trait Json4sAngularJSONStringOps extends Json4sJSONStringOps {

  // JSON vulnerability protection enabled by default
  override protected def useJSONVulnerabilityProtection: Boolean = true

  // camelCase keys by default
  override protected def useUnderscoreKeysForJSON: Boolean = false

}

object Json4sAngularJSONStringOps extends Json4sAngularJSONStringOps
