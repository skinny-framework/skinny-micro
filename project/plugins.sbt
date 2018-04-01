resolvers += Classpaths.sbtPluginReleases
resolvers += "sonatype releases"  at "https://oss.sonatype.org/content/repositories/releases"

addSbtPlugin("io.get-coursier"      % "sbt-coursier"            % "1.0.3")
addSbtPlugin("com.typesafe"         % "sbt-mima-plugin"         % "0.1.18")
addSbtPlugin("org.skinny-framework" % "sbt-servlet-plugin"      % "2.2.5")
addSbtPlugin("org.scalariform"      % "sbt-scalariform"         % "1.8.2")
addSbtPlugin("com.jsuereth"         % "sbt-pgp"                 % "1.1.1")
addSbtPlugin("org.xerial.sbt"       % "sbt-sonatype"            % "2.0")
addSbtPlugin("net.virtual-void"     % "sbt-dependency-graph"    % "0.9.0")
addSbtPlugin("com.timushev.sbt"     % "sbt-updates"             % "0.3.4")

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")
