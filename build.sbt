organization := "com.github.xuwei-k"

name := "sbt-conflict-classes"

// https://github.com/sbt/sbt/issues/6090
scriptedBatchExecution := false

scalacOptions ++= Seq("-deprecation", "-unchecked")

val tagName = Def.setting {
  s"v${if (releaseUseGlobalVersion.value) (ThisBuild / version).value else version.value}"
}
val tagOrHash = Def.setting {
  if (isSnapshot.value)
    sys.process.Process("git rev-parse HEAD").lineStream_!.head
  else
    tagName.value
}

releaseTagName := tagName.value

Compile / doc / scalacOptions ++= {
  val tag = tagOrHash.value
  Seq(
    "-sourcepath",
    (LocalRootProject / baseDirectory).value.getAbsolutePath,
    "-doc-source-url",
    s"https://github.com/xuwei-k/sbt-conflict-classes/tree/${tag}€{FILE_PATH}.scala"
  )
}

licenses := Seq("MIT" -> url("https://github.com/xuwei-k/sbt-conflict-classes/blob/master/LICENSE"))

homepage := Some(url("https://github.com/xuwei-k/sbt-conflict-classes"))

pomExtra := (
  <developers>
  <developer>
    <id>xuwei-k</id>
    <name>Kenji Yoshida</name>
    <url>https://github.com/xuwei-k</url>
  </developer>
</developers>
<scm>
  <url>git@github.com:xuwei-k/sbt-conflict-classes.git</url>
  <connection>scm:git:git@github.com:xuwei-k/sbt-conflict-classes.git</connection>
  <tag>{tagOrHash.value}</tag>
</scm>
)

import ReleaseTransformations._

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  releaseStepCommandAndRemaining("+ test"),
  releaseStepCommandAndRemaining("+ scripted"),
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  releaseStepCommandAndRemaining("+ publishSigned"),
  releaseStepCommand("sonaRelease"),
  setNextVersion,
  commitNextVersion,
  pushChanges
)

publishTo := (if (isSnapshot.value) None else localStaging.value)

pluginCrossBuild / sbtVersion := {
  scalaBinaryVersion.value match {
    case "2.12" =>
      (pluginCrossBuild / sbtVersion).value
    case _ =>
      "2.0.0-RC8"
  }
}

crossScalaVersions += "3.8.1"
