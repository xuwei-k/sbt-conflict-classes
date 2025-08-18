package sbt_conflict_classes

import xsbti.FileConverter
import xsbti.VirtualFileRef
import java.io.File

private[sbt_conflict_classes] object ConflictClassesPluginCompat {
  def toFile(file: VirtualFileRef, converter: FileConverter): File =
    converter.toPath(file).toFile
}
