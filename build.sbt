import skinny.servlet._
import ServletPlugin._
import ServletKeys._
import MimaSettings.mimaSettings
import sbt.Keys._

import scala.language.postfixOps

lazy val currentVersion = "2.2.0"

lazy val json4SVersion = "3.6.7"
lazy val jettyVersion = "9.4.26.v20200117"
lazy val logbackVersion = "1.2.3"
lazy val slf4jApiVersion = "1.7.30"
lazy val jacksonVersion = "2.10.2"
lazy val jacksonScalaVersion = "2.10.2"
lazy val scalatestV = SettingKey[String]("scalatestVersion")

lazy val baseSettings = Seq(
  organization := "org.skinny-framework",
  version := currentVersion,
  resolvers ++= Seq(
    "sonatype staging" at "https://oss.sonatype.org/content/repositories/staging",
    //, "sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
  ),
  dependencyOverrides := Seq(
    "org.scala-lang" %  "scala-library"  % scalaVersion.value,
    "org.scala-lang" %  "scala-reflect"  % scalaVersion.value,
    "org.scala-lang" %  "scala-compiler" % scalaVersion.value
  ),
  scalatestV := "3.1.0",
  unmanagedSourceDirectories in Compile += {
    val base = (sourceDirectory in Compile).value.getParentFile / Defaults.nameForSrc(Compile.name)
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, v)) if v >= 13 => base / s"scala-2.13+"
      case _ =>                       base / s"scala-2.13-"
    }
  },
  publishMavenStyle := true,
  sbtPlugin := false,
  scalaVersion := "2.13.1",
  crossScalaVersions := Seq("2.13.1", "2.12.10", "2.11.12"),
  scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature"),
  scalacOptions += {
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, 11)) =>
        "-Ywarn-unused-import"
      case _ =>
        "-Ywarn-unused:imports"
    }
  },
  scalacOptions in (Compile, doc) ++= {
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, v)) if v <= 11 => Nil
      case _ => Seq("-no-java-comments") // https://issues.scala-lang.org/browse/SI-10020
    }
  },
  publishArtifact in Test := false,
  pomIncludeRepository := { x => false },
  transitiveClassifiers in Global := Seq(Artifact.SourceClassifier),
  logBuffered in Test := false,
  javaOptions in Test ++= Seq("-Dskinny.env=test"),
  fork in Test := true,
  updateOptions := updateOptions.value.withCachedResolution(true),
  javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-encoding", "UTF-8", "-Xlint:-options"),
  javacOptions in doc := Seq("-source", "1.8"),
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
    "org.scalatest"  %% "scalatest"       % scalatestV.value % Test,
    "org.scalatestplus" %% "mockito-3-2"  % "3.1.0.0"        % Test,
    "ch.qos.logback" %  "logback-classic" % logbackVersion   % Test
  )
))

lazy val micro = (project in file("micro")).settings(baseSettings ++ mimaSettings ++ Seq(
  name := "skinny-micro",
  libraryDependencies ++= {
    servletApiDependencies ++ slf4jApiDependencies ++ Seq(
      "org.scala-lang.modules" %% "scala-parser-combinators"    % "1.1.2" % Compile,
      "com.googlecode.juniversalchardet" %  "juniversalchardet" % "1.0.3" % Compile,
      "ch.qos.logback"    %  "logback-classic" % logbackVersion           % Test
    )
  }
)).dependsOn(microCommon, scalatraTest % Test)

lazy val microJackson = (project in file("micro-jackson")).settings(baseSettings ++ mimaSettings ++ Seq(
  name := "skinny-micro-jackson",
  libraryDependencies ++= servletApiDependencies ++ (if (scalaVersion.value.startsWith("2.13")) jacksonDependencies213 else jacksonDependencies) ++ Seq(
    "ch.qos.logback"    %  "logback-classic"    % logbackVersion % Test
  ),
  libraryDependencies += "org.scala-lang" %  "scala-reflect"  % scalaVersion.value
)).dependsOn(micro, scalatraTest % Test)

lazy val microJacksonXml = (project in file("micro-jackson-xml")).settings(baseSettings ++ mimaSettings ++ Seq(
  name := "skinny-micro-jackson-xml",
  libraryDependencies ++= servletApiDependencies ++  (if (scalaVersion.value.startsWith("2.13")) jacksonDependencies213 else jacksonDependencies) ++ Seq(
    "com.fasterxml.jackson.dataformat" %  "jackson-dataformat-xml" % jacksonVersion % Compile,
    "org.codehaus.woodstox"            %  "woodstox-core-asl"      % "4.4.1"        % Compile,
    "ch.qos.logback"                   %  "logback-classic"        % logbackVersion % Test
  )
)).dependsOn(micro, microJackson, scalatraTest % Test)

lazy val microJson4s = (project in file("micro-json4s")).settings(baseSettings ++ mimaSettings ++ Seq(
  name := "skinny-micro-json4s",
  libraryDependencies ++= servletApiDependencies ++ json4sDependencies ++ Seq(
    "joda-time"         %  "joda-time"          % "2.10.2"         % Compile,
    "org.joda"          %  "joda-convert"       % "2.2.1"        % Compile,
    "ch.qos.logback"    %  "logback-classic"    % logbackVersion % Test
  )
)).dependsOn(micro, scalatraTest % Test)

lazy val microScalate = (project in file("micro-scalate")).settings(baseSettings ++ mimaSettings ++ Seq(
  name := "skinny-micro-scalate",
  libraryDependencies ++= slf4jApiDependencies ++ servletApiDependencies ++ Seq(
    "org.scalatra.scalate"  %% "scalate-core"       % "1.9.5"    % Compile excludeAll(fullExclusionRules: _*),
    "ch.qos.logback"        %  "logback-classic"    % logbackVersion % Test
  )
)).dependsOn(micro, scalatraTest % Test)

lazy val microServer = (project in file("micro-server")).settings(baseSettings ++ mimaSettings ++ Seq(
  name := "skinny-micro-server",
  libraryDependencies ++= jettyDependencies ++ Seq(
    "org.scalatest"        %% "scalatest"          % scalatestV.value % Test,
    "org.scalatestplus"    %% "mockito-3-2"        % "3.1.0.0"        % Test,
    "ch.qos.logback"       %  "logback-classic"    % logbackVersion   % Test
  )
)).dependsOn(micro, microJackson % Test, scalatraTest % Test)

lazy val scalatraTest = (project in file("scalatra-test")).settings(baseSettings ++ Seq(
  name := "scalatra-test",
  libraryDependencies ++= servletApiDependencies ++ slf4jApiDependencies ++ Seq(
    "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.3",
    "com.googlecode.juniversalchardet" % "juniversalchardet" % "1.0.3" % Compile,
    "org.scalatestplus"  %% "testng-6-7"       % "3.1.0.0"        % Compile,
    "org.scalatestplus"  %% "junit-4-12"       % "3.1.0.0"        % Compile,
    "org.scalatestplus"  %% "mockito-3-2"      % "3.1.0.0"        % Compile,
    "org.apache.commons" %  "commons-lang3"    % "3.9"            % Compile,
    "org.eclipse.jetty"  %  "jetty-webapp"     % jettyVersion     % Compile,
    "org.apache.httpcomponents" % "httpclient" % "4.5.6"          % Compile, // TODO: 4.5.7 behaves differently
    "org.apache.httpcomponents" % "httpmime"   % "4.5.6"          % Compile,
    "org.scalatest"      %% "scalatest"        % scalatestV.value % Compile,
    "org.specs2"         %% "specs2-core"      % "4.8.3"          % Compile
  )
))

lazy val microTest = (project in file("micro-test")).settings(baseSettings ++ mimaSettings ++ Seq(
  name := "skinny-micro-test",
  libraryDependencies ++= servletApiDependencies ++ Seq(
    "org.scalatestplus"  %% "junit-4-12"       % "3.1.0.0"        % Compile,
    "org.apache.commons" %  "commons-lang3"    % "3.9"            % Compile,
    "org.eclipse.jetty"  %  "jetty-webapp"     % jettyVersion     % Compile,
    "org.apache.httpcomponents" % "httpclient" % "4.5.6"          % Compile, // TODO: 4.5.7 behaves differently
    "org.apache.httpcomponents" % "httpmime"   % "4.5.6"          % Compile,
    "org.scalatest"      %% "scalatest"        % scalatestV.value % Compile,
    "ch.qos.logback"     %  "logback-classic"  % logbackVersion   % Test
  )
)).dependsOn(micro)

lazy val samples = (project in file("samples")).settings(baseSettings ++ Seq(
  libraryDependencies ++= Seq(
    // Slick dropped Scala 2.10 support
    //"com.typesafe.slick" %% "slick"            % "3.2.0-M2",
    "org.slf4j"          %  "slf4j-nop"        % slf4jApiVersion,
    "com.h2database"     %  "h2"               % "1.4.200",
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
lazy val jacksonDependencies = Seq(
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % jacksonScalaVersion % Compile
)
lazy val jacksonDependencies213 = Seq(
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % jacksonScalaVersion % Compile
)
lazy val json4sDependencies = Seq(
  "org.json4s"    %% "json4s-jackson"     % json4SVersion    % Compile  excludeAll(fullExclusionRules: _*),
  "org.json4s"    %% "json4s-native"      % json4SVersion    % Provided excludeAll(fullExclusionRules: _*),
  "org.json4s"    %% "json4s-xml"         % json4SVersion    % Provided excludeAll(fullExclusionRules: _*),
  "org.json4s"    %% "json4s-ext"         % json4SVersion    % Compile  excludeAll(fullExclusionRules: _*)
)
lazy val jettyDependencies = Seq(
  // NOTICE: Jetty 9.2 support 3.1 or higher
  "javax.servlet"     %  "javax.servlet-api" % "3.1.0"       % Compile,
  "org.eclipse.jetty" %  "jetty-webapp"      % jettyVersion  % Compile,
  "org.eclipse.jetty" %  "jetty-servlet"     % jettyVersion  % Compile,
  "org.eclipse.jetty" %  "jetty-server"      % jettyVersion  % Compile
)
