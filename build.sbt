version := "0.0.3-SNAPSHOT"

organization := "com.github.xuwei-k"

name := "sbt-conflict-classes"

sbtPlugin := true

scalacOptions ++= Seq("-deprecation", "-unchecked")

crossSbtVersions := Seq("0.13.16", "1.0.0")
