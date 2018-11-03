name := "http4s"

version := "1.0"

scalaVersion in ThisBuild := "2.11.12"

cancelable in Global := true

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-unchecked",
  "-language:higherKinds",
  "-Yno-adapted-args",
  "-Ypartial-unification",
  "-Ywarn-numeric-widen",
  "-Xfuture",
  "-Xlint"
)

enablePlugins(SbtTwirl)

TwirlKeys.templateImports += "http4s.techempower.benchmark._"

libraryDependencies ++= {
  object V {
    val http4s = "0.20.0-M1"
    val doobie = "0.6.0"
  }

  Seq(
    "org.http4s" %% "http4s-blaze-server" % V.http4s,
    "org.http4s" %% "http4s-dsl" % V.http4s,
    "org.http4s" %% "http4s-twirl" % V.http4s,
    "com.github.plokhotnyuk.jsoniter-scala" %% "macros" % "0.21.6",
    "org.tpolecat" %% "doobie-core" % V.doobie,
    "org.tpolecat" %% "doobie-hikari" % V.doobie,
    "org.postgresql" % "postgresql" % "42.2.5",
    "ch.qos.logback" % "logback-classic" % "1.2.3"
  )
}

resolvers += Resolver.sonatypeRepo("releases")

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.8")
