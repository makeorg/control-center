import java.time.{LocalDate, ZoneOffset, ZonedDateTime}

import com.typesafe.sbt.GitVersioning
import com.typesafe.sbt.SbtGit.GitKeys
import sbt.enablePlugins

organization := "org.make.backoffice"
name := "make-backoffice"
scalaVersion := "2.12.1"

/* scala libraries version */
val scalaJsReactVersion = "0.14.0"
val circeVersion = "0.8.0"
val scalajsDomVersion = "0.9.1"
val scalaCssCoreVersion = "0.5.3"

/* Npm versions */
val npmReactVersion = "15.6.2"
val npmWebpackVersion = "3.6.0"
val npmReactRouterVersion = "4.1.2"
val npmBulmaVersion = "0.4.4"
val npmSassLoaderVersion = "6.0.6"
val npmNodeSassVersion = "4.5.3"
val npmExtractTextWebpackPluginVersion = "3.0.0"
val npmCssLoaderVersion = "0.28.4"
val npmStyleLoaderVersion = "0.18.2"
val npmCleanWebpackPluginVersion = "0.1.16"
val npmHtmlWebpackPluginVersion = "2.29.0"
val npmWebpackMd5HashVersion = "0.0.5"
val npmFontAwesomeVersion = "4.7.0"
val npmFileLoaderVersion = "0.11.2"
val npmNormalizeVersion = "7.0.0"
val npmBootstrapVersion = "3.3.7"
val npmAdminOnRestVersion = "1.3.4"
val npmAorDependentInput = "1.2.0"
val npmMaterialUi = "0.19.2"
val npmReactGoogleLogin = "2.9.2"

enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)

libraryDependencies ++= Seq(
  "org.scala-js"                 %%% "scalajs-dom"                    % scalajsDomVersion,
  "io.github.shogowada"          %%% "scalajs-reactjs"                % scalaJsReactVersion, // For react facade
  "io.github.shogowada"          %%% "scalajs-reactjs-router-dom"     % scalaJsReactVersion, // Optional. For react-router-dom facade
  "com.github.japgolly.scalacss" %%% "core"                           % scalaCssCoreVersion,
  "io.circe"                     %%% "circe-core"                     % circeVersion,
  "io.circe"                     %%% "circe-java8"                    % circeVersion,
  "io.circe"                     %%% "circe-generic"                  % circeVersion,
  "io.circe"                     %%% "circe-parser"                   % circeVersion,
  "io.circe"                     %%% "circe-scalajs"                  % circeVersion
)

npmDependencies in Compile ++= Seq(
  "react" -> npmReactVersion,
  "react-dom" -> npmReactVersion,
  "react-router" -> npmReactRouterVersion,
  "react-router-dom" -> npmReactRouterVersion,
  "react-google-login" -> npmReactGoogleLogin,
  "admin-on-rest" -> npmAdminOnRestVersion,
  "material-ui" -> npmMaterialUi,
  "bulma" -> npmBulmaVersion,
  "font-awesome" -> npmFontAwesomeVersion,
  "normalize-scss" -> npmNormalizeVersion,
  "bootstrap" -> npmBootstrapVersion,
  "aor-dependent-input" -> npmAorDependentInput
)

npmDevDependencies in Compile ++= Seq(
  "ajv" -> "5.2.2",
  "sass-loader" -> npmSassLoaderVersion,
  "node-sass" -> npmNodeSassVersion,
  "extract-text-webpack-plugin" -> npmExtractTextWebpackPluginVersion,
  "css-loader" -> npmCssLoaderVersion,
  "style-loader" -> npmStyleLoaderVersion,
  "clean-webpack-plugin" -> npmCleanWebpackPluginVersion,
  "html-webpack-plugin" -> npmHtmlWebpackPluginVersion,
  "webpack-md5-hash" -> npmWebpackMd5HashVersion,
  "file-loader" -> npmFileLoaderVersion,
  "webpack-dev-server" -> "2.8.2",
  "webpack" -> npmWebpackVersion
)

npmResolutions in Compile := {
  (npmDependencies in Compile).value.toMap ++ (npmDevDependencies in Compile).value.toMap
}

version in webpack := npmWebpackVersion
webpackResources := {
  baseDirectory.value / "src" / "main" / "static" / "sass" ** "*.sass"
}

webpackDevServerPort := 4242

webpackConfigFile in fastOptJS := Some(baseDirectory.value / "make-webpack-library.config.js")
webpackConfigFile in fullOptJS := Some(baseDirectory.value / "make-webpack-prod.config.js")

webpackDevServerExtraArgs := Seq("--lazy", "--inline")

webpackBundlingMode in fastOptJS := BundlingMode.LibraryOnly("makeBackoffice")
webpackBundlingMode in fullOptJS := BundlingMode.Application

scalacOptions ++= {
  if (System.getenv("CI_BUILD") == "true") {
    Seq("-Xelide-below", "OFF")
  } else {
    Seq.empty
  }
}

emitSourceMaps := System.getenv("CI_BUILD") != "true"

scalaJSUseMainModuleInitializer := true

lazy val buildTime: SettingKey[String] = SettingKey[String]("buildTime", "time of build")

buildTime := ZonedDateTime.now(ZoneOffset.UTC).toString

lazy val buildVersion: TaskKey[String] = taskKey[String]("build version")

buildVersion := {
  val buildVersion: String = s"""{
                                |    "name": "${name.value}",
                                |    "version": "${version.value}",
                                |    "gitHeadCommit": "${GitKeys.gitHeadCommit.value.getOrElse("DETACHED")}",
                                |    "gitBranch": "${GitKeys.gitCurrentBranch.value}",
                                |    "buildTime": "${buildTime.value}"
                                |}""".stripMargin

  streams.value.log.info("Building version")
  streams.value.log.info(buildVersion)

  buildVersion
}


// Custome task to manage assets
val prepareAssets = taskKey[Unit]("prepareAssets")
prepareAssets in ThisBuild := {
  val npmDirectory = (npmUpdate in Compile).value
  IO.copyDirectory(baseDirectory.value / "src" / "main" / "static", npmDirectory, overwrite = true)

  val buildVersionFile: File = npmDirectory / "dist" / "version"
  val buildVersionLocalFile: File = npmDirectory / "version"
  val contents: String = buildVersion.value

  IO.write(buildVersionFile, contents)
  IO.write(buildVersionLocalFile, contents)

  streams.value.log.info("Copy assets to working directory")
}
fastOptJS in Compile := {
  prepareAssets.value
  (fastOptJS in Compile).value
}
fullOptJS in Compile := {
  prepareAssets.value
  (fullOptJS in Compile).value
}

gitCommitMessageHook := Some(baseDirectory.value / "bin" / "commit-msg.hook")

enablePlugins(GitHooks)

useYarn := true

git.formattedShaVersion := git.gitHeadCommit.value map { sha => sha.take(7) }

version in ThisBuild := {
  git.formattedShaVersion.value.get
}

enablePlugins(GitVersioning)