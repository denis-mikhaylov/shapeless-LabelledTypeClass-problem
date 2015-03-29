name := "problem"

scalaVersion := "2.11.6"

resolvers += Resolver.sonatypeRepo("snapshots")

resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
	"com.chuusai" %% "shapeless" % "2.1.0"
)