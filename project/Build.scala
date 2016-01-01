import sbt._, Keys._
import skinny.servlet._, ServletPlugin._, ServletKeys._

import scala.language.postfixOps

object SkinnyMicroBuild extends Build {

  lazy val currentVersion = "1.0.1-SNAPSHOT"

  lazy val json4SVersion = "3.3.0"
  // TODO: specs2 breaking changes
  lazy val scalatraTestVersion = "2.3.1"
  lazy val mockitoVersion = "1.10.19"
  // Jetty 9.3 dropped Java 7
  lazy val jettyVersion = "9.2.14.v20151106"
  lazy val logbackVersion = "1.1.3"
  lazy val slf4jApiVersion = "1.7.13"
  lazy val jacksonVersion = "2.6.4"
  // Akka 2.4 dropped Scala 2.10 support
  lazy val akkaVersion = "2.3.14"
  lazy val scalaTestVersion = "2.2.5"

  lazy val baseSettings = Seq(
    organization := "org.skinny-framework",
    version := currentVersion,
    resolvers ++= Seq(
      "sonatype releases"  at "https://oss.sonatype.org/content/repositories/releases"
      //, "sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
    ),
    publishMavenStyle := true,
    sbtPlugin := false,
    scalaVersion := "2.11.7",
    ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) },
    scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature"),
    publishArtifact in Test := false,
    pomIncludeRepository := { x => false },
    transitiveClassifiers in Global := Seq(Artifact.SourceClassifier),
    incOptions := incOptions.value.withNameHashing(true),
    logBuffered in Test := false,
    javaOptions in Test ++= Seq("-Dskinny.env=test"),
    fork in Test := true,
    updateOptions := updateOptions.value.withCachedResolution(true),
    javacOptions ++= Seq("-source", "1.7", "-target", "1.7", "-encoding", "UTF-8", "-Xlint:-options"),
    javacOptions in doc := Seq("-source", "1.7"),
    publishTo <<= version { (v: String) =>
      val nexus = "https://oss.sonatype.org/"
      if (v.trim.endsWith("SNAPSHOT")) Some("snapshots" at nexus + "content/repositories/snapshots")
      else Some("releases" at nexus + "service/local/staging/deploy/maven2")
    },
    pomExtra := {
      <url>http://skinny-framework.org/</url>
        <licenses>
          <license>
            <name>The BSD 2-Clause License</name>
            <url>http://opensource.org/licenses/BSD-2-Clause</url>
            <distribution>repo</distribution>
          </license>
        </licenses>
        <scm>
          <url>git@github.com:skinny-framework/skinny-micro.git</url>
          <connection>scm:git:git@github.com:skinny-framework/skinny-micro.git</connection>
        </scm>
        <developers>
          <developer>
            <id>seratch</id>
            <name>Kazuhiro Sera</name>
            <url>http://git.io/sera</url>
          </developer>
        </developers>
    }
  )

  // -----------------------------
  // skinny libraries

  lazy val microCommon = Project(id = "microCommon", base = file("micro-common"),
    settings = baseSettings ++ Seq(
      name := "skinny-micro-common",
      libraryDependencies ++= slf4jApiDependencies ++ Seq(
        "org.scalatest"  %% "scalatest"       % scalaTestVersion % Test,
        "org.mockito"    %  "mockito-core"    % mockitoVersion   % Test,
        "ch.qos.logback" %  "logback-classic" % logbackVersion   % Test
      )
    )
  )

  lazy val micro = Project(id = "micro", base = file("micro"),
    settings = baseSettings ++ Seq(
      name := "skinny-micro",
      libraryDependencies <++= (scalaVersion) { (sv) =>
        servletApiDependencies ++ slf4jApiDependencies ++ Seq(
          "com.googlecode.juniversalchardet" %  "juniversalchardet" % "1.0.3"     % Compile,
          "org.scalatra"      %% "scalatra-specs2"          % scalatraTestVersion % Test,
          "org.scalatra"      %% "scalatra-scalatest"       % scalatraTestVersion % Test,
          "com.typesafe.akka" %% "akka-actor"               % akkaVersion         % Test,
          "ch.qos.logback"    %  "logback-classic"          % logbackVersion      % Test
        ) ++ (sv match {
          case v if v.startsWith("2.11.") => Seq("org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4" % Compile)
          case _ => Nil
        })
      }
    )
  ).dependsOn(microCommon)

  lazy val microJackson = Project(id = "microJackson", base = file("micro-jackson"),
    settings = baseSettings ++ Seq(
      name := "skinny-micro-jackson",
      libraryDependencies ++= servletApiDependencies ++ jacksonDependencies ++ Seq(
        "org.scalatra"      %% "scalatra-scalatest" % scalatraTestVersion % Test,
        "com.typesafe.akka" %% "akka-actor"         % akkaVersion         % Test,
        "ch.qos.logback"    %  "logback-classic"    % logbackVersion      % Test
      )
    )
  ).dependsOn(micro)

  lazy val microJacksonXml = Project(id = "microJacksonXml", base = file("micro-jackson-xml"),
    settings = baseSettings ++ Seq(
      name := "skinny-micro-jackson-xml",
      libraryDependencies ++= servletApiDependencies ++ jacksonDependencies ++ Seq(
        "com.fasterxml.jackson.dataformat" %  "jackson-dataformat-xml" % jacksonVersion      % Compile,
        "org.codehaus.woodstox"            %  "woodstox-core-asl"      % "4.4.1"             % Compile,
        "org.scalatra"                     %% "scalatra-scalatest"     % scalatraTestVersion % Test,
        "com.typesafe.akka"                %% "akka-actor"             % akkaVersion         % Test,
        "ch.qos.logback"                   %  "logback-classic"        % logbackVersion      % Test
      )
    )
  ).dependsOn(micro, microJackson)

  lazy val microJson4s = Project(id = "microJson4s", base = file("micro-json4s"),
    settings = baseSettings ++ Seq(
      name := "skinny-micro-json4s",
      libraryDependencies ++= servletApiDependencies ++ json4sDependencies ++ Seq(
        "joda-time"         %  "joda-time"          % "2.9.1"             % Compile,
        "org.joda"          %  "joda-convert"       % "1.8.1"             % Compile,
        "org.scalatra"      %% "scalatra-scalatest" % scalatraTestVersion % Test,
        "com.typesafe.akka" %% "akka-actor"         % akkaVersion         % Test,
        "ch.qos.logback"    %  "logback-classic"    % logbackVersion      % Test
      )
    )
  ).dependsOn(micro)

  lazy val microScalate = Project(id = "microScalate", base = file("micro-scalate"),
    settings = baseSettings ++ Seq(
      name := "skinny-micro-scalate",
      libraryDependencies ++= slf4jApiDependencies ++ servletApiDependencies ++ Seq(
        "org.scalatra.scalate"  %% "scalate-core"       % "1.7.1"             % Compile excludeAll(fullExclusionRules: _*),
        "org.scalatra"          %% "scalatra-specs2"    % scalatraTestVersion % Test,
        "org.scalatra"          %% "scalatra-scalatest" % scalatraTestVersion % Test,
        "com.typesafe.akka"     %% "akka-actor"         % akkaVersion         % Test,
        "ch.qos.logback"        %  "logback-classic"    % logbackVersion      % Test
      )
    )
  ).dependsOn(micro)

  lazy val microServer = Project(id = "microServer", base = file("micro-server"),
    settings = baseSettings ++ Seq(
      name := "skinny-micro-server",
      libraryDependencies ++= jettyDependencies ++ Seq(
        "org.skinny-framework" %% "skinny-http-client" % "2.0.+"          % Test,
        "org.scalatest"        %% "scalatest"          % scalaTestVersion % Test,
        "org.mockito"          %  "mockito-core"       % mockitoVersion   % Test,
        "ch.qos.logback"       %  "logback-classic"    % logbackVersion   % Test
      )
    )
  ).dependsOn(
    micro,
    microJackson % Test
  )

  lazy val microTest = Project(id = "microTest", base = file("micro-test"),
    settings = baseSettings ++ Seq(
      name := "skinny-micro-test",
      libraryDependencies ++= servletApiDependencies ++ Seq(
        "junit"              %  "junit"            % "4.12"           % Compile,
        "org.apache.commons" %  "commons-lang3"    % "3.4"            % Compile,
        "org.eclipse.jetty"  %  "jetty-webapp"     % jettyVersion     % Compile,
        "org.apache.httpcomponents" % "httpclient" % "4.5.1"          % Compile,
        "org.apache.httpcomponents" % "httpmime"   % "4.5.1"          % Compile,
        "org.scalatest"      %% "scalatest"        % scalaTestVersion % Compile,
        "ch.qos.logback"     %  "logback-classic"  % logbackVersion   % Test
      )
    )
  ).dependsOn(micro)

  lazy val samples = Project(id = "samples", base = file("samples"),
    settings = baseSettings ++ Seq(
      libraryDependencies ++= Seq(
        "com.typesafe.slick" %% "slick"            % "3.1.1",
        "org.slf4j"          %  "slf4j-nop"        % slf4jApiVersion,
        "com.h2database"     %  "h2"               % "1.4.190",
        "ch.qos.logback"     %  "logback-classic"  % logbackVersion
      )
    )
  ).dependsOn(micro, microJackson, microJacksonXml, microJson4s, microScalate, microServer, microTest % Test)

  // -----------------------------
  // common dependencies

  lazy val fullExclusionRules = Seq(
    ExclusionRule("org.slf4j", "slf4j-api"),
    ExclusionRule("joda-time", "joda-time"),
    ExclusionRule("org.joda",  "joda-convert"),
    ExclusionRule("log4j",     "log4j"),
    ExclusionRule("org.slf4j", "slf4j-log4j12")
  )
  lazy val servletApiDependencies = Seq(
    "javax.servlet" % "javax.servlet-api" % "3.0.1"  % Provided
  )
  lazy val slf4jApiDependencies   = Seq(
    "org.slf4j"     % "slf4j-api"         % slf4jApiVersion % Compile
  )
  lazy val jacksonDependencies   = Seq(
    "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.6.3" % Compile
  )
  lazy val json4sDependencies = Seq(
    "org.json4s"    %% "json4s-jackson"     % json4SVersion    % Compile  excludeAll(fullExclusionRules: _*),
    "org.json4s"    %% "json4s-native"      % json4SVersion    % Provided excludeAll(fullExclusionRules: _*),
    "org.json4s"    %% "json4s-ext"         % json4SVersion    % Compile  excludeAll(fullExclusionRules: _*)
  )
  lazy val jettyDependencies = Seq(
    // NOTICE: Jetty 9.2 support 3.1 or higher
    "javax.servlet"     %  "javax.servlet-api" % "3.1.0"       % Compile,
    "org.eclipse.jetty" %  "jetty-webapp"      % jettyVersion  % Compile,
    "org.eclipse.jetty" %  "jetty-servlet"     % jettyVersion  % Compile,
    "org.eclipse.jetty" %  "jetty-server"      % jettyVersion  % Compile
  )
}
