package sbt_conflict_classes

import sbt_conflict_classes.ConflictClassesPluginCompat.*

object ConflictClassesPlugin extends sbt.AutoPlugin {
  import sbt._

  object autoImport {
    val conflictClasses = TaskKey[Seq[Conflict]]("conflict-classes", "Show conflict classes in classpath")
    val conflictClassExcludes = TaskKey[Seq[String]](
      "conflict-class-excludes",
      "Exclude pattern for conflict checking. Check is done by `path startWith pattern`."
    )
  }

  import autoImport._

  override lazy val projectSettings =
    forConfig(Compile) ++ forConfig(Test) ++ forConfig(Runtime) ++ Seq(
      conflictClassExcludes := Seq("META-INF/")
    )

  def forConfig(config: Configuration) =
    inConfig(config)(
      Seq(
        conflictClasses := Def.uncached {
          val conflicts = buildConflicts(
            (config / Keys.dependencyClasspath).value
              .map(cp => Classpath(ConflictClassesPluginCompat.toFile(cp.data, Keys.fileConverter.value))),
            conflictClassExcludes.value
          )
          printConflicts(Keys.streams.value.log, conflicts)
          conflicts
        }
      )
    )

  def printConflicts(log: Logger, conflicts: Seq[Conflict]): Unit = {
    log.info("Listing conflict classes:")
    conflicts.foreach { (conflict: Conflict) =>
      log.info("Found conflict classes in:")
      conflict.classpathes.toSeq.sortBy(_.asFile.name).foreach { jar => log.info("    " + jar.asFile.getPath) }
      log.info("  with classes:")
      conflict.resources.toSeq.sortBy(_.name).foreach { entry => log.info("    " + entry.name) }
    }
  }

  def buildConflicts(cps: Seq[Classpath], excludes: Seq[String]): Seq[Conflict] = {
    val resourceToCps: Map[Resource, Seq[Classpath]] =
      cps.foldLeft(Map[Resource, Seq[Classpath]]()) { (map, cp) =>
        cp.listResources()
          .filter { res => !res.name.endsWith("/") && !excludes.exists(ex => res.name.startsWith(ex)) }
          .foldLeft(map) { (map, res) => map + (res -> (map.getOrElse(res, Seq()) :+ cp)) }
      }

    val cpsToResources: Map[Set[Classpath], Set[Resource]] =
      resourceToCps.foldLeft(Map[Set[Classpath], Set[Resource]]()) { case (map, (res, cps)) =>
        val cpSet = cps.toSet
        map + (cpSet -> (map.getOrElse(cpSet, Set()) + res))
      }

    cpsToResources.collect { case (cps, resources) if cps.size > 1 => Conflict(resources, cps) }.toSeq
  }
}
