name := "scala-ssh-shell"

organization := "com.peak6"

version := "0.0.1-SNAPSHOT"

//scalaVersion := "2.10.6"
scalaVersion := "2.11.7"

crossScalaVersions := Seq("2.10.6", "2.11.7")

scalacOptions ++= Vector("-unchecked", "-deprecation")

javacOptions ++= Vector("-encoding", "UTF-8")

retrieveManaged := true

libraryDependencies <++= (scalaVersion) {
	(scala) => Seq(
	"org.scala-lang" % "scala-compiler" % scala,
//	"org.scala-lang" % "jline" % scala,
	"org.scala-lang.modules" % "scala-jline" % "2.12.1",
	"jline" % "jline" % "2.14.1",
	"org.clapper" %% "grizzled-slf4j" % "1.0.2",
	"org.slf4j" % "slf4j-simple" % "1.6.4",
	"org.bouncycastle" % "bcprov-jdk16" % "1.46",
	"org.apache.sshd" % "sshd-core" % "1.1.0"
	)}

unmanagedSourceDirectories in Compile <+= (sourceDirectory in Compile, scalaBinaryVersion){
	(s, v) => s / ("scala_"+v)
}

enablePlugins(JavaAppPackaging)
