resolvers += Classpaths.sbtPluginReleases
resolvers += "sonatype staging" at "https://oss.sonatype.org/content/repositories/staging"

addSbtPlugin("com.typesafe"         % "sbt-mima-plugin"         % "0.6.1")
addSbtPlugin("org.skinny-framework" % "sbt-servlet-plugin"      % "3.0.9")
addSbtPlugin("org.scalariform"      % "sbt-scalariform"         % "1.8.3")
addSbtPlugin("com.jsuereth"         % "sbt-pgp"                 % "2.0.1")
addSbtPlugin("org.xerial.sbt"       % "sbt-sonatype"            % "3.8.1")
addSbtPlugin("net.virtual-void"     % "sbt-dependency-graph"    % "0.9.2")
addSbtPlugin("com.timushev.sbt"     % "sbt-updates"             % "0.5.0")

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")
