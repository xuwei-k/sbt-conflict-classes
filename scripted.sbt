enablePlugins(SbtPlugin)

val javaVmArgs: List[String] = {
  import scala.jdk.CollectionConverters._
  java.lang.management.ManagementFactory.getRuntimeMXBean.getInputArguments.asScala.toList
}

scriptedLaunchOpts ++= javaVmArgs.filter(a => Seq("-Xmx", "-Xms", "-XX", "-Dsbt.log.noformat").exists(a.startsWith))

scriptedLaunchOpts += ("-Dplugin.version=" + version.value)

scriptedBufferLog := false
