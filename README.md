# Fork information

This is a Scala.js enabled fork of Paul Butcher's Scala Mock. I'm planning to maintain this until this pull request is merged: https://github.com/paulbutcher/ScalaMock/pull/152 

Use may use forked snapshot artefact from sonatype:

```scala
"com.hypertino" %%% "scalamock-scalatest-support" % "3.4-SNAPSHOT" % "test"
```

# ScalaMock [![Build Status](https://travis-ci.org/hypertino/ScalaMock.svg?branch=master)](https://travis-ci.org/hypertino/ScalaMock)

Native Scala mocking. For Scala 2.10, 2.11 and 2.12.

Official website: [http://scalamock.org/](http://scalamock.org/)

## Examples

### Expectations-First Style

```scala
def testTurtle {
  val m = mock[Turtle]                              // Create mock Turtle object

  (m.setPosition _).expects(10.0, 10.0)             //
  (m.forward _).expects(5.0)                        // Set expectations
  (m.getPosition _).expects().returning(15.0, 10.0) // 

  drawLine(m, (10.0, 10.0), (15.0, 10.0))           // Exercise System Under Test
}
```

### Record-then-Verify (Mockito) Style

```scala
def testTurtle {
  val m = stub[Turtle]                              // Create stub Turtle
  
  (m.getPosition _).when().returns(15.0, 10.0)      // Setup return values

  drawLine(m, (10.0, 10.0), (15.0, 10.0))           // Exercise System Under Test

  (m.setPosition _).verify(10.0, 10.0)              // Verify expectations met
  (m.forward _).verify(5.0)                         //
}
```

[Full worked example](http://scalamock.org/quick-start/)

## Features

* Fully typesafe
* Full support for Scala features such as:
  * Polymorphic (type parameterised) methods
  * Operators (methods with symbolic names)
  * Overloaded methods
  * Type constraints
* ScalaTest and Specs2 integration

## Downloading

Download from Maven Central or JCenter (synced via [Bintray](https://bintray.com/scalamock/maven))

To use ScalaMock in your Tests  add the following to your project file:

### For [ScalaTest](http://www.scalatest.org/)

- [sbt](http://www.scala-sbt.org/):
```scala
libraryDependencies += "org.scalamock" %% "scalamock-scalatest-support" % "3.4.2" % Test
```

- [gradle](https://gradle.org/):
```groovy
testCompile 'org.scalamock:scalamock-scalatest-support_2.12:3.4.2'
```

### For [Specs2](http://etorreborre.github.com/specs2/):

- [sbt](http://www.scala-sbt.org/):
```scala
libraryDependencies += "org.scalamock" %% "scalamock-specs2-support" % "3.4.2" % Test
```

- [gradle](https://gradle.org/):
```groovy
testCompile 'org.scalamock:scalamock-specs2-support_2.12:3.4.2'
```

## Documentation

* [Quick Start](http://scalamock.org/quick-start/)
* [User Guide](http://scalamock.org/user-guide/)
* [Scaladoc](http://scalamock.org/api/index.html#org.scalamock.package)

## Future Plans

Check our [roadmap](http://scalamock.org/roadmap/).

### Acknowledgements

YourKit is kindly supporting open source projects with its full-featured Java Profiler.
YourKit, LLC is the creator of innovative and intelligent tools for profiling
Java and .NET applications. Take a look at YourKit's leading software products:
[YourKit Java Profiler](http://www.yourkit.com/java/profiler/index.jsp) and
[YourKit .NET Profiler](http://www.yourkit.com/.net/profiler/index.jsp).
