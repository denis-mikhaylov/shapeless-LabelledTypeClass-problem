name := "problem"

scalaVersion := "2.11.4"

resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies ++= Seq(
	"com.chuusai" %% "shapeless" % "2.1.0-SNAPSHOT",
	"com.typesafe.play" %% "play-json" % "2.3.5"
)