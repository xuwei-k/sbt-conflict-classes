scalacOptions ++= Seq("-deprecation", "-Xlint", "-unchecked", "-language:_")

addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.1.0")
addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.7")
addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "2.2")
addSbtPlugin("com.lucidchart" % "sbt-scalafmt" % "1.14")
