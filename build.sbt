name := "plane-simulator"

version := "1.0"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq("com.typesafe.akka" %% "akka-actor" % "2.5.3",
                            "com.typesafe.akka" %% "akka-testkit" % "2.5.3" % "test",
                            "org.scalatest" %% "scalatest" % "3.0.1" % "test")

        