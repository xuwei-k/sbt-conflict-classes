# sbt-conflict-classes: List conflict classes in classpath


## Usage

```scala
// project/plugins.sbt
addSbtPlugin("com.github.xuwei-k" % "sbt-conflict-classes" % "0.1.1")
```

```scala
// build.sbt

enablePlugins(ConflictClassesPlugin)

// Exclude from conflict detection(match with startsWith)(Optional)
conflictClassExcludes ++= Seq(
  "com/example/DuplicateClass.class",
  "com/example/dups/"
)
```

```
$ sbt Compile/conflictClasses # show compile-time conflicts
$ sbt Test/conflictClasses    # show test-time conflicts
$ sbt Runtime/conflictClasses # show runtime conflicts
```
