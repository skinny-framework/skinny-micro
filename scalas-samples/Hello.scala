#!/usr/bin/env scalas
// or ./scalas Hello.scala
/***
scalaVersion := "2.11.7"
resolvers += "sonatype releases" at "https://oss.sonatype.org/content/repositories/releases"
libraryDependencies += "org.skinny-framework" %% "skinny-micro-server" % "1.0.0"
*/
import skinny.micro._
object HelloApp extends WebApp {
  get("/say-hello") {
    s"Hello, ${params.getOrElse("name", "Anonymous")}!\n"
  }
}
WebServer.mount(HelloApp).port(4567).start()

println
println("Try: curl -v 'localhost:4567/say-hello?name=Martin'")
println
