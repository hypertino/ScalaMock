import sbt.Keys._
import sbt.inc.Analysis

crossScalaVersions := Seq("2.11.8", "2.10.6")

organization in Global := "com.hypertino"

val buildVersion = "3.4-SNAPSHOT"

val buildSettings = Defaults.coreDefaultSettings ++ Seq(
  version := buildVersion,
  scalaVersion := "2.11.8",
  scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature"),
  scalacOptions in (Compile, doc) ++= Opts.doc.title("ScalaMock") ++ Opts.doc.version(buildVersion) ++ Seq("-doc-root-content", "rootdoc.txt", "-version"),
  resolvers += Resolver.sonatypeRepo("releases"),
  resolvers += Resolver.sonatypeRepo("snapshots"),
  resolvers += "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases",
  pomIncludeRepository := { _ => false },
  publishArtifact := false,
  publishArtifact in Test := false,
  pomExtra := <url>http://scalamock.org/</url>
    <licenses>
      <license>
        <name>BSD-style</name>
        <url>http://www.opensource.org/licenses/bsd-license.php</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
    <scm>
      <url>git@github.com:paulbutcher/ScalaMock.git</url>
      <connection>scm:git:git@github.com:paulbutcher/ScalaMock.git</connection>
    </scm>
    <developers>
      <developer>
        <id>paulbutcher</id>
        <name>Paul Butcher</name>
        <url>http://paulbutcher.com/</url>
      </developer>
    </developers>,
  pgpSecretRing := file("./travis/ht-oss-private.asc"),
  pgpPublicRing := file("./travis/ht-oss-public.asc"),
  usePgpKeyHex("F8CDEF49B0EDEDCC"),
  pgpPassphrase := Option(System.getenv().get("oss_gpg_passphrase")).map(_.toCharArray),
  publishMavenStyle := true,
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases" at nexus + "service/local/staging/deploy/maven2")
  },
  shellPrompt := ShellPrompt.buildShellPrompt
)

val specs2 = "org.specs2" %% "specs2" % "2.4.16"

// Specs2 and ScalaTest use different scala-xml versions
// and this caused problems with referencing class org.scalatest.events.Event
// val scalaXml = "org.scala-lang.modules" %% "scala-xml" % "1.0.3" % "test"

lazy val core = crossProject.settings(buildSettings:_*)
  .in(file("core"))
  .settings(
    name := "ScalaMock Core",
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % scalaVersion.value
    ) ++ {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, 10)) =>
          Seq(compilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
            "org.scalamacros" %% "quasiquotes" % "2.1.0" cross CrossVersion.binary)
        case _ ⇒ Seq.empty
      }
    },
    publishArtifact := true
  )

lazy val jsCore = core.js

lazy val jvmCore = core.jvm  

lazy val scalatestSupport = crossProject.settings(buildSettings:_*)
  .in(file("frameworks/scalatest"))
  .settings(
    name := "ScalaMock ScalaTest Support",
    libraryDependencies += "org.scalatest" %%% "scalatest" % "3.0.0",
    publishArtifact := true
  )
  .dependsOn(core)

lazy val jsScalatestSupport = scalatestSupport.js

lazy val jvmScalatestSupport = scalatestSupport.jvm

lazy val specs2Support = crossProject.settings(buildSettings:_*)
  .in(file("frameworks/specs2"))
  .settings(
    name := "ScalaMock Specs2 Support",
    libraryDependencies += specs2
  )
  .dependsOn(core)

lazy val jsSpecs2Support = specs2Support.js

lazy val jvmSpecs2Support = specs2Support.jvm

lazy val core_tests = crossProject.settings(buildSettings:_*)
  .in(file("core_tests"))
  .settings(
    name := "ScalaMock Core Tests"
  )
  .dependsOn(scalatestSupport)

lazy val jscore_tests = core_tests.js

lazy val jvmcore_tests = core_tests.jvm  
  
lazy val examples = crossProject.settings(buildSettings:_*)
  .in(file("examples"))
  .settings(
    name := "ScalaMock Examples"
  )
  .dependsOn(scalatestSupport, specs2Support)

lazy val jsExamples = examples.js

lazy val jvmExamples = examples.jvm

lazy val root = crossProject
  .in(file("."))
  .settings(
    name := "ScalaMock"
  )
  .dependsOn(scalatestSupport, specs2Support)

credentials in Global ++= (for {
  username <- Option(System.getenv().get("sonatype_username"))
  password <- Option(System.getenv().get("sonatype_password"))
} yield Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", username, password)).toSeq

publishArtifact in Global := false

publish in Global := ()

publishLocal in Global := ()

packagedArtifacts in Global := Map.empty
