package sbt_conflict_classes

import java.io.File

case class Classpath(asFile: File) {
  def listResources(): Seq[Resource] = {
    asFile match {
      case f if f.isDirectory => listFromDirectory(f)
      case f if f.isFile      => listFromJar(f)
      case _                  => Seq()
    }
  }

  private[this] def listFromDirectory(root: File, prefix: Seq[String] = Seq()): Seq[Resource] = {
    import scala.collection.JavaConverters._
    root.listFiles().flatMap {
      case f if f.isFile      => Seq(Resource((prefix :+ f.getName).mkString("/")))
      case f if f.isDirectory => listFromDirectory(f, prefix :+ f.getName)
    }
  }

  private[this] def listFromJar(file: File) = {
    import scala.collection.JavaConverters._
    new java.util.jar.JarFile(file).entries.asScala.map { e =>
      Resource(e.getName)
    }.toSeq
  }
}
case class Resource(name: String)
case class Conflict(resources: Set[Resource], classpathes: Set[Classpath])

object ConflictClassesPlugin extends sbt.AutoPlugin {
  import sbt._

  object autoImport {
    val conflictClasses = TaskKey[Seq[Conflict]]("conflict-classes", "Show conflict classes in classpath")
    val conflictClassExcludes = TaskKey[Seq[String]](
      "conflict-class-excludes",
      "Exclude pattern for conflict checking. Check is done by `path startWith pattern`.")
  }

  import autoImport._

  override lazy val projectSettings =
    forConfig(Compile) ++ forConfig(Test) ++ forConfig(Runtime) ++ Seq(
      conflictClassExcludes := Seq("META-INF/")
    )

  def forConfig(config: Configuration) =
    inConfig(config)(
      Seq(
        conflictClasses := {
          val conflicts = buildConflicts(
            (Keys.dependencyClasspath in config).value.map(cp => Classpath(cp.data)),
            conflictClassExcludes.value)
          printConflicts(Keys.streams.value.log, conflicts)
          conflicts
        }
      ))

  def printConflicts(log: Logger, conflicts: Seq[Conflict]): Unit = {
    log.info("Listing conflict classes:")
    conflicts.foreach { conflict: Conflict =>
      log.info("Found conflict classes in:")
      conflict.classpathes.toSeq.sortBy(_.asFile.name).foreach { jar =>
        log.info("    " + jar.asFile.getPath)
      }
      log.info("  with classes:")
      conflict.resources.toSeq.sortBy(_.name).foreach { entry =>
        log.info("    " + entry.name)
      }
    }
  }

  def buildConflicts(cps: Seq[Classpath], excludes: Seq[String]): Seq[Conflict] = {
    val resourceToCps: Map[Resource, Seq[Classpath]] =
      cps.foldLeft(Map[Resource, Seq[Classpath]]()) { (map, cp) =>
        cp.listResources.filter { res =>
          !res.name.endsWith("/") && !excludes.exists(ex => res.name.startsWith(ex))
        }.foldLeft(map) { (map, res) =>
          map + (res -> (map.getOrElse(res, Seq()) :+ cp))
        }
      }

    val cpsToResources: Map[Set[Classpath], Set[Resource]] =
      resourceToCps.foldLeft(Map[Set[Classpath], Set[Resource]]()) {
        case (map, (res, cps)) =>
          val cpSet = cps.toSet
          map + (cpSet -> (map.getOrElse(cpSet, Set()) + res))
      }

    cpsToResources.collect { case (cps, resources) if cps.size > 1 => Conflict(resources, cps) }.toSeq
  }
}
