organization := "com.github.xuwei-k"

name := "sbt-conflict-classes"

// https://github.com/sbt/sbt/issues/6090
scriptedBatchExecution := false

scalacOptions ++= Seq("-deprecation", "-unchecked")

// Don't update to sbt 1.3.x
// https://github.com/sbt/sbt/issues/5049
crossSbtVersions := Seq("0.13.18", "1.2.8")

val tagName = Def.setting {
  s"v${if (releaseUseGlobalVersion.value) (version in ThisBuild).value else version.value}"
}
val tagOrHash = Def.setting {
  if (isSnapshot.value)
    sys.process.Process("git rev-parse HEAD").lineStream_!.head
  else
    tagName.value
}

releaseTagName := tagName.value

scalacOptions in (Compile, doc) ++= {
  val tag = tagOrHash.value
  Seq(
    "-sourcepath",
    (baseDirectory in LocalRootProject).value.getAbsolutePath,
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
  releaseStepCommandAndRemaining("^ test"),
  releaseStepCommandAndRemaining("^ scripted"),
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  releaseStepCommandAndRemaining("^ publishSigned"),
  setNextVersion,
  commitNextVersion,
  releaseStepCommand("sonatypeReleaseAll"),
  pushChanges
)

publishTo := Some(
  if (isSnapshot.value)
    Opts.resolver.sonatypeSnapshots
  else
    Opts.resolver.sonatypeStaging
)
