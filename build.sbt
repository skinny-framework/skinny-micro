import skinny.servlet._, ServletPlugin._, ServletKeys._
import MimaSettings.mimaSettings
import scala.language.postfixOps

lazy val currentVersion = "1.1.1-SNAPSHOT"

lazy val json4SVersion = "3.4.0"
lazy val mockitoVersion = "1.10.19"
// Jetty 9.3 dropped Java 7
lazy val jettyVersion = "9.2.19.v20160908"
lazy val logbackVersion = "1.1.7"
lazy val slf4jApiVersion = "1.7.21"
// TODO: 2.7
lazy val jacksonVersion = "2.6.7"
// Akka 2.4 dropped Scala 2.10 support
lazy val akkaVersion = "2.3.15"
lazy val scalaTestVersion = "3.0.0"

lazy val baseSettings = Seq(
  organization := "org.skinny-framework",
  version := currentVersion,
  resolvers ++= Seq(
    "sonatype releases"  at "https://oss.sonatype.org/content/repositories/releases"
    //, "sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
  ),
  // https://github.com/sbt/sbt/issues/2217
  fullResolvers ~= { _.filterNot(_.name == "jcenter") },
  dependencyOverrides := Set(
    "org.scala-lang" %  "scala-library"  % scalaVersion.value,
    "org.scala-lang" %  "scala-reflect"  % scalaVersion.value,
    "org.scala-lang" %  "scala-compiler" % scalaVersion.value
  ),
  publishMavenStyle := true,
  sbtPlugin := false,
  scalaVersion := "2.11.8",
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
  // https://github.com/sbt/sbt/issues/653
  // https://github.com/travis-ci/travis-ci/issues/3775
  javaOptions += "-Xmx256M",
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (version.value.trim.endsWith("SNAPSHOT")) Some("snapshots" at nexus + "content/repositories/snapshots")
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

lazy val microCommon = (project in file("micro-common")).settings(baseSettings ++ mimaSettings ++ Seq(
  name := "skinny-micro-common",
  libraryDependencies ++= slf4jApiDependencies ++ Seq(
    "org.scalatest"  %% "scalatest"       % scalaTestVersion % Test,
    "org.mockito"    %  "mockito-core"    % mockitoVersion   % Test,
    "ch.qos.logback" %  "logback-classic" % logbackVersion   % Test
  )
))

lazy val micro = (project in file("micro")).settings(baseSettings ++ mimaSettings ++ Seq(
  name := "skinny-micro",
  libraryDependencies ++= {
    servletApiDependencies ++ slf4jApiDependencies ++ Seq(
      "com.googlecode.juniversalchardet" %  "juniversalchardet" % "1.0.3"     % Compile,
      "com.typesafe.akka" %% "akka-actor"               % akkaVersion         % Test,
      "ch.qos.logback"    %  "logback-classic"          % logbackVersion      % Test
    ) ++ (scalaVersion.value match {
      case v if v.startsWith("2.11.") => Seq("org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4" % Compile)
      case _ => Nil
    })
  }
)).dependsOn(microCommon, scalatraTest % Test)

lazy val microJackson = (project in file("micro-jackson")).settings(baseSettings ++ mimaSettings ++ Seq(
  name := "skinny-micro-jackson",
  libraryDependencies ++= servletApiDependencies ++ jacksonDependencies ++ Seq(
    "com.typesafe.akka" %% "akka-actor"         % akkaVersion         % Test,
    "ch.qos.logback"    %  "logback-classic"    % logbackVersion      % Test
  )
)).dependsOn(micro, scalatraTest % Test)

lazy val microJacksonXml = (project in file("micro-jackson-xml")).settings(baseSettings ++ mimaSettings ++ Seq(
  name := "skinny-micro-jackson-xml",
  libraryDependencies ++= servletApiDependencies ++ jacksonDependencies ++ Seq(
    "com.fasterxml.jackson.dataformat" %  "jackson-dataformat-xml" % jacksonVersion      % Compile,
    "org.codehaus.woodstox"            %  "woodstox-core-asl"      % "4.4.1"             % Compile,
    "com.typesafe.akka"                %% "akka-actor"             % akkaVersion         % Test,
    "ch.qos.logback"                   %  "logback-classic"        % logbackVersion      % Test
  )
)).dependsOn(micro, microJackson, scalatraTest % Test)

lazy val microJson4s = (project in file("micro-json4s")).settings(baseSettings ++ mimaSettings ++ Seq(
  name := "skinny-micro-json4s",
  libraryDependencies ++= servletApiDependencies ++ json4sDependencies ++ Seq(
    "joda-time"         %  "joda-time"          % "2.9.4"             % Compile,
    "org.joda"          %  "joda-convert"       % "1.8.1"             % Compile,
    "com.typesafe.akka" %% "akka-actor"         % akkaVersion         % Test,
    "ch.qos.logback"    %  "logback-classic"    % logbackVersion      % Test
  )
)).dependsOn(micro, scalatraTest % Test)

lazy val microScalate = (project in file("micro-scalate")).settings(baseSettings ++ mimaSettings ++ Seq(
  name := "skinny-micro-scalate",
  libraryDependencies ++= slf4jApiDependencies ++ servletApiDependencies ++ Seq(
    "org.scalatra.scalate"  %% "scalate-core"       % "1.7.1"             % Compile excludeAll(fullExclusionRules: _*),
    "com.typesafe.akka"     %% "akka-actor"         % akkaVersion         % Test,
    "ch.qos.logback"        %  "logback-classic"    % logbackVersion      % Test
  )
)).dependsOn(micro, scalatraTest % Test)

lazy val microServer = (project in file("micro-server")).settings(baseSettings ++ mimaSettings ++ Seq(
  name := "skinny-micro-server",
  libraryDependencies ++= jettyDependencies ++ Seq(
    "org.skinny-framework" %% "skinny-http-client" % "2.2.+"          % Test,
    "org.scalatest"        %% "scalatest"          % scalaTestVersion % Test,
    "org.mockito"          %  "mockito-core"       % mockitoVersion   % Test,
    "ch.qos.logback"       %  "logback-classic"    % logbackVersion   % Test
  )
)).dependsOn(micro, microJackson % Test, scalatraTest % Test)

lazy val scalatraTest = (project in file("scalatra-test")).settings(baseSettings ++ Seq(
  name := "scalatra-test",
  libraryDependencies ++= servletApiDependencies ++ slf4jApiDependencies ++ Seq(
    "com.googlecode.juniversalchardet" % "juniversalchardet" % "1.0.3" % Compile,
    "junit"              %  "junit"            % "4.12"           % Compile,
    "org.testng"         %  "testng"           % "6.8.21"         % Compile,
    "org.mockito"        %  "mockito-core"     % mockitoVersion   % Compile,
    "org.apache.commons" %  "commons-lang3"    % "3.5"            % Compile,
    "org.eclipse.jetty"  %  "jetty-webapp"     % jettyVersion     % Compile,
    "org.apache.httpcomponents" % "httpclient" % "4.5.2"          % Compile,
    "org.apache.httpcomponents" % "httpmime"   % "4.5.2"          % Compile,
    "org.scalatest"      %% "scalatest"        % scalaTestVersion % Compile,
    "org.specs2"         %% "specs2-core"      % "2.3.13"         % Compile
  )
))

lazy val microTest = (project in file("micro-test")).settings(baseSettings ++ mimaSettings ++ Seq(
  name := "skinny-micro-test",
  libraryDependencies ++= servletApiDependencies ++ Seq(
    "junit"              %  "junit"            % "4.12"           % Compile,
    "org.apache.commons" %  "commons-lang3"    % "3.5"            % Compile,
    "org.eclipse.jetty"  %  "jetty-webapp"     % jettyVersion     % Compile,
    "org.apache.httpcomponents" % "httpclient" % "4.5.2"          % Compile,
    "org.apache.httpcomponents" % "httpmime"   % "4.5.2"          % Compile,
    "org.scalatest"      %% "scalatest"        % scalaTestVersion % Compile,
    "ch.qos.logback"     %  "logback-classic"  % logbackVersion   % Test
  )
)).dependsOn(micro)

lazy val samples = (project in file("samples")).settings(baseSettings ++ Seq(
  libraryDependencies ++= Seq(
    "com.typesafe.slick" %% "slick"            % "3.1.1",
    "org.slf4j"          %  "slf4j-nop"        % slf4jApiVersion,
    "com.h2database"     %  "h2"               % "1.4.192",
    "ch.qos.logback"     %  "logback-classic"  % logbackVersion
  )
)).dependsOn(micro, microJackson, microJacksonXml, microJson4s, microScalate, microServer, microTest % Test)

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
  "javax.servlet" % "javax.servlet-api" % "3.1.0"  % Provided
)
lazy val slf4jApiDependencies   = Seq(
  "org.slf4j"     % "slf4j-api"         % slf4jApiVersion % Compile
)
lazy val jacksonDependencies   = Seq(
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.6.7" % Compile
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
