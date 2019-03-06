import sbt._, Keys._
import com.typesafe.tools.mima.plugin.MimaPlugin
import com.typesafe.tools.mima.plugin.MimaKeys.{mimaPreviousArtifacts, mimaReportBinaryIssues, mimaBinaryIssueFilters}

/*
 * MiMa settings of Skinny-Micro libs.
 */
object MimaSettings {
  val previousVersions = Seq(0, 1).map(patch => s"2.0.$patch").toSet
  //val previousVersions = Set[String]()

  val mimaSettings = MimaPlugin.mimaDefaultSettings ++ Seq(
    mimaPreviousArtifacts := {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, scalaMajor)) if scalaMajor <= 12 => previousVersions.map { organization.value % s"${name.value}_${scalaBinaryVersion.value}" % _ }
        case _ => Set.empty
      }
    },
    test in Test := {
      mimaReportBinaryIssues.value
      (test in Test).value
    },
    mimaBinaryIssueFilters ++= {
      import com.typesafe.tools.mima.core._
      Seq()
    }
  )
}
