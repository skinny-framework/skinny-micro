import sbt._, Keys._
import com.typesafe.tools.mima.plugin.MimaPlugin
import com.typesafe.tools.mima.plugin.MimaKeys.{mimaPreviousArtifacts, mimaReportBinaryIssues, mimaBinaryIssueFilters}

/*
 * MiMa settings of Skinny-Micro libs.
 */
object MimaSettings {
  // 1.2.0 -> 1.2.1 is not bin-compatible, it's not critical though
  // error]  * class skinny.micro.rl.Benchmark#delayedInit#body does not have a correspondent in current version
  // [error]    filter with: ProblemFilters.exclude[MissingClassProblem]("skinny.micro.rl.Benchmark$delayedInit$body")
  // [error]    filter with: ProblemFilters.exclude[MissingClassProblem]("skinny.micro.rl.Benchmark")
  // [error]    filter with: ProblemFilters.exclude[MissingClassProblem]("skinny.micro.rl.Benchmark$")
  //val previousVersions = (0 to 4).map(patch => s"1.2.$patch").toSet
  val previousVersions = (1 to 4).map(patch => s"1.2.$patch").toSet

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
