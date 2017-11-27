/** to build the first time you need to execute
  * jooq:codegen which depends on flywayMigrate to generate the jooq db code
  */
scalaVersion  := "2.11.8"
scalaVersion in ThisBuild := "2.11.8"
// # libs

// ## input configuration
val configLib     = "com.typesafe"        % "config"         % "1.2.1"

val zxingLibs = Seq("com.google.zxing" % "core" % "3.3.1",
  "com.google.zxing" % "javase" % "3.3.1")

lazy val qrIdent = (project in file(".")).
  settings(
    libraryDependencies ++= zxingLibs,
    name := "qrIdent"
  )
