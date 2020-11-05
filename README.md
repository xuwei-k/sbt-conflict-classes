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
$ sbt compile:conflictClasses # show compile-time conflicts
$ sbt test:conflictClasses    # show test-time conflicts
$ sbt runtime:conflictClasses # show runtime conflicts
```
