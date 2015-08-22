# Skinny Micro 

Skinny Micro is at once a micro Web framework to build Servlet applications in Scala and the core part of [Skinny Framework](http://skinny-framework.org/) 2.

Skinny Micro started as a fork of [Scalatra](http://scalatra.org/). After that, many improvements have been made to be safer and more efficient when working with Scala Future values upon it.

Basically, Skinny Micro's DSLs are source compatible with Scalatra 2.3's ones. But names of base traits and packages are mostly renamed and the structure of internal modules are re-designed.

## Getting Started

```scala
lazy val skinnyMicroVersion = "0.9.1"

libraryDependencies ++= Seq(
  // micro Web framework
  "org.skinny-framework" %% "skinny-micro"         % skinnyMicroVersion,
  // json4s integration for skinny-micro
  "org.skinny-framework" %% "skinny-micro-json"    % skinnyMicroVersion,
  // Scalate integration for skinny-micro
  "org.skinny-framework" %% "skinny-micro-scalate" % skinnyMicroVersion,
  // Standalone Web server (Jetty 9.2)
  "org.skinny-framework" %% "skinny-micro-server"  % skinnyMicroVersion
)
```

## Minimum Example

```scala
#!/usr/bin/env scalas
// or ./scalas app.scala
/***
scalaVersion := "2.11.7"
resolvers += "sonatype releases" at "https://oss.sonatype.org/content/repositories/releases"
libraryDependencies += "org.skinny-framework" %% "skinny-micro-server" % "0.9.1"
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
```

See the examples under [samples](https://github.com/skinny-framework/skinny-micro/tree/master/samples).

## Under The MIT License

Others are under The MIT License.

(The MIT License)

Copyright (c) skinny-framework.org



