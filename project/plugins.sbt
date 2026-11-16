scalacOptions ++= Seq("-deprecation", "-unchecked")

addSbtPlugin("com.github.sbt" % "sbt-pgp" % "2.3.2")
addSbtPlugin("com.github.sbt" % "sbt-release" % "1.6.0")
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.6.2")

libraryDependencies += "com.github.xuwei-k" %% "scala-version-from-sbt-version" % "0.1.0"
