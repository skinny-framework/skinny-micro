import sbt._, Keys._
import com.typesafe.tools.mima.plugin.MimaPlugin
import com.typesafe.tools.mima.plugin.MimaKeys.{previousArtifacts, reportBinaryIssues}

/*
 * MiMa settings of Skinny-Micro libs.
 */
object MimaSettings {
  val previousVersions = (0 to 2).map(patch => s"1.0.$patch").toSet

  val mimaSettings = MimaPlugin.mimaDefaultSettings ++ Seq(
    previousArtifacts := {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, scalaMajor)) if scalaMajor <= 11 => previousVersions.map { organization.value % s"${name.value}_${scalaBinaryVersion.value}" % _ }
        case _ => Set.empty
      }
    },
    test in Test := {
      reportBinaryIssues.value
      (test in Test).value
    }
  )
}
