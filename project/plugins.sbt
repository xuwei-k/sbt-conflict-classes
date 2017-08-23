scalacOptions ++= Seq("-deprecation", "-Xlint", "-unchecked", "-language:_")

addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.0.1")
addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.6")
addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "2.0")
