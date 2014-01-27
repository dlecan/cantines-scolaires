import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "cantines-scolaires"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    anorm,
    "com.itextpdf" % "itextpdf" % "5.4.3",
    "org.mockito" % "mockito-core" % "1.9.5"
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
