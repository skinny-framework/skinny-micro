package skinny.micro.test

/**
 * Provides a framework-agnostic way to test your Skinny Micro app.
 * You probably want to extend this with either
 * <code>skinny.micro.test.scalatest._</code> or <code>skinny.micro.test.specs._</code>.
 *
 * Cookies are crudely supported within session blocks.
 * No attempt is made to match domains, paths, or max-ages;
 * the request sends a Cookie header to match whatever Set-Cookie call it received on the previous response.
 */
trait SkinnyMicroTests
  extends EmbeddedJettyContainer
  with HttpComponentsClient