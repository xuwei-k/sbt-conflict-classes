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

  private def listFromDirectory(root: File, prefix: Seq[String] = Seq()): Seq[Resource] = {
    import scala.collection.JavaConverters._
    root.listFiles().flatMap {
      case f if f.isFile      => Seq(Resource((prefix :+ f.getName).mkString("/")))
      case f if f.isDirectory => listFromDirectory(f, prefix :+ f.getName)
    }
  }

  private def listFromJar(file: File) = {
    import scala.collection.JavaConverters._
    new java.util.jar.JarFile(file).entries.asScala.map { e => Resource(e.getName) }.toSeq
  }
}
