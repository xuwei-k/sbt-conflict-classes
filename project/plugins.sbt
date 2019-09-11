scalacOptions ++= Seq("-deprecation", "-unchecked", "-language:_")

addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.1.2")
addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.11")
addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "3.7")
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.0.4")
