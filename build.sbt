name := "scala-ssh-shell"

organization := "co.actioniq"

version := "0.2-572a853553e0381ea56dd026f0ea563bacf16169"

scalaVersion := "2.10.4"

scalacOptions ++= Vector("-unchecked", "-deprecation", "-Ywarn-all")

javacOptions ++= Vector("-encoding", "UTF-8")

//retrieveManaged := true

libraryDependencies <++= (scalaVersion) {
	(scala) => Seq(
	"org.scala-lang" % "scala-compiler" % scala,
	"org.scala-lang" % "jline" % scala,
	"org.clapper" %% "grizzled-slf4j" % "1.0.2",
	"org.slf4j" % "slf4j-simple" % "1.6.4",
	"org.bouncycastle" % "bcprov-jdk16" % "1.46",
	"org.apache.sshd" % "sshd-core" % "0.14.0"
	)}

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra := (
  <url>https://github.com/ActionIQ/scala-ssh-shell</url>
  <licenses>
    <license>
      <name>Apache</name>
      <url>https://raw.githubusercontent.com/ActionIQ/scala-ssh-shell/master/LICENSE</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:ActionIQ/scala-ssh-shell.git</url>
    <connection>scm:git:git@github.com:ActionIQ/scala-ssh-shell.git</connection>
  </scm>
  <developers>
    <developer>
      <id>you</id>
      <name>Your Name</name>
      <url>http://your.url</url>
    </developer>
  </developers>
)
