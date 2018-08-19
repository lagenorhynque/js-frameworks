scalaVersion := "2.12.6"

libraryDependencies ++= {
  val akkaHttpV = "10.1.3"
  val scalikejdbcV = "3.3.0"
  Seq(
    "com.typesafe.akka" %% "akka-http" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpV,
    "com.typesafe.akka" %% "akka-stream" % "2.5.12",
    "org.scalikejdbc" %% "scalikejdbc" % scalikejdbcV,
    "org.scalikejdbc" %% "scalikejdbc-config" % scalikejdbcV,
    "org.scalikejdbc" %% "scalikejdbc-syntax-support-macro" % scalikejdbcV,
    "mysql" % "mysql-connector-java" % "8.0.12",
    "ch.qos.logback" % "logback-classic" % "1.2.3"
  )
}
