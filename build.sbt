// Scalate template engine config for Xitrum
// "import" must be at top of build.sbt, or SBT will complain

import ScalateKeys._

// Precompile Scalate
seq(scalateSettings: _*)

scalateTemplateConfig in Compile := Seq(TemplateConfig(
  file("src") / "main" / "scalate",
  Seq(),
  Seq(Binding("helper", "xitrum.Action", true))
))

libraryDependencies += "tv.cntt" %% "xitrum-scalate" % "1.4"

//------------------------------------------------------------------------------

organization := "com.liberty"

name := "gs-server"

version := "1.0-SNAPSHOT"

scalaVersion := "2.10.3"

scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked")

// Most Scala projects are published to Sonatype, but Sonatype is not default
// and it takes several hours to sync from Sonatype to Maven Central
resolvers += "SonatypeReleases" at "http://oss.sonatype.org/content/repositories/releases/"

//resolvers += "MavenCentral" at "http://central.maven.org/maven2/com/cloudphysics/jerkson_2.10/"


libraryDependencies += "tv.cntt" %% "xitrum" % "2.15"

// Xitrum uses SLF4J, an implementation of SLF4J is needed
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.0.13"

libraryDependencies += "com.novus" %% "salat" % "1.9.2"

libraryDependencies += "com.cloudphysics" %% "jerkson" % "0.6.1"

// xgettext i18n translation key string extractor is a compiler plugin ---------

autoCompilerPlugins := true

addCompilerPlugin("tv.cntt" %% "xgettext" % "1.0")

scalacOptions += "-P:xgettext:xitrum.I18n"

// xitrum.imperatively uses Scala continuation, also a compiler plugin ---------

libraryDependencies <+= scalaVersion {
  sv =>
    compilerPlugin("org.scala-lang.plugins" % "continuations" % sv)
}

scalacOptions += "-P:continuations:enable"

// Put config directory in classpath for easier development --------------------

// For "sbt console"
unmanagedClasspath in Compile <+= (baseDirectory) map {
  bd => Attributed.blank(bd / "config")
}

// For "sbt run"
unmanagedClasspath in Runtime <+= (baseDirectory) map {
  bd => Attributed.blank(bd / "config")
}

// Copy these to target/xitrum when sbt xitrum-package is run
XitrumPackage.copy("config", "public", "script")
