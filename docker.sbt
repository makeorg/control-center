import com.typesafe.sbt.packager.docker.{Cmd, ExecCmd}

enablePlugins(DockerPlugin)

dockerRepository := Some("nexus.prod.makeorg.tech")
packageName in Docker := "repository/docker-dev/make-backoffice"

val nginxContentDirectory = "/usr/share/nginx/html/make"

dockerCommands := Seq(
  Cmd("FROM", "nginx:1.13.3"),
  Cmd("MAINTAINER", "technical2@make.org"),
  ExecCmd("RUN", "rm", "/etc/nginx/conf.d/default.conf"),
  Cmd("COPY", "dist", nginxContentDirectory),
  Cmd("COPY", "conf/nginx.conf", "/etc/nginx/conf.d/make.conf"),
  ExecCmd("RUN", "chmod", "-R", "+rw", nginxContentDirectory)
)

val copyDockerResources: TaskKey[String] = taskKey[String]("copy directories")

copyDockerResources := {
  val files = (webpack in (Compile, fullOptJS)).value
  streams.value.log.info("Copying resources to the docker directory")
  val base = baseDirectory.value
  val dockerDirectory = base / "target" / "docker" / "stage"
  dockerDirectory.mkdirs()
  IO.copyDirectory(base / "src" / "main" / "universal", dockerDirectory, overwrite = true)
  IO.copyDirectory(
    base / "target" / s"scala-${scalaBinaryVersion.value}" / "scalajs-bundler" / "main" / "dist",
    dockerDirectory / "dist",
    overwrite = true
  )
  "Done"
}

publishLocal := {
  Def.sequential(copyDockerResources, publishLocal in Docker).value
}

publish := {
  Def.sequential(copyDockerResources, publish in Docker).value
}