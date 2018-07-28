resolvers += Classpaths.sbtPluginReleases
resolvers += "sonatype releases"  at "https://oss.sonatype.org/content/repositories/releases"

addSbtPlugin("io.get-coursier"      % "sbt-coursier"            % "1.0.3")
addSbtPlugin("com.typesafe"         % "sbt-mima-plugin"         % "0.3.0")
addSbtPlugin("org.skinny-framework" % "sbt-servlet-plugin"      % "3.0.4")
addSbtPlugin("org.scalariform"      % "sbt-scalariform"         % "1.8.2")
addSbtPlugin("com.jsuereth"         % "sbt-pgp"                 % "1.1.2")
addSbtPlugin("org.xerial.sbt"       % "sbt-sonatype"            % "2.3")
addSbtPlugin("net.virtual-void"     % "sbt-dependency-graph"    % "0.9.1")
addSbtPlugin("com.timushev.sbt"     % "sbt-updates"             % "0.3.4")

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")
