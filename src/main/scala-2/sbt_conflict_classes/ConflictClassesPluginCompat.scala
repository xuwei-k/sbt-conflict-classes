package sbt_conflict_classes

import xsbti.FileConverter
import java.io.File

private[sbt_conflict_classes] object ConflictClassesPluginCompat {
  def toFile(file: File, converter: FileConverter): File = file

  implicit class DefOps(private val self: sbt.Def.type) extends AnyVal {
    def uncached[A](a: A): A = a
  }
}
