scalacOptions ++= Seq("-deprecation", "-unchecked")

addSbtPlugin("com.github.sbt" % "sbt-pgp" % "2.4.0")
addSbtPlugin("com.github.sbt" % "sbt-release" % "1.6.0")
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.6.2")

libraryDependencies += "com.github.xuwei-k" %% "scala-version-from-sbt-version" % "0.2.0"
