package sbt_conflict_classes

case class Conflict(resources: Set[Resource], classpathes: Set[Classpath])
