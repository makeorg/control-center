import java.time.{ZoneOffset, ZonedDateTime}

import com.typesafe.sbt.GitVersioning
import com.typesafe.sbt.SbtGit.GitKeys
import sbt.enablePlugins

organization := "org.make.backoffice"
name := "make-backoffice"
scalaVersion := "2.12.5"

/* scala libraries version */
val scalaJsReactVersion = "0.14.0"
val circeVersion = "0.8.0"
val scalajsDomVersion = "0.9.1"
val scalaCssCoreVersion = "0.5.3"

/* Npm versions */
val npmReactVersion = "15.6.2"
val npmWebpackVersion = "4.12.0"
val npmReactRouterVersion = "4.1.2"
val npmSassLoaderVersion = "7.0.3"
val npmNodeSassVersion = "4.9.0"
val npmExtractTextWebpackPluginVersion = "v4.0.0-beta.0"
val npmCssLoaderVersion = "0.28.11"
val npmStyleLoaderVersion = "0.21.0"
val npmCleanWebpackPluginVersion = "0.1.19"
val npmHtmlWebpackPluginVersion = "3.2.0"
val npmWebpackMd5HashVersion = "0.0.6"
val npmFontAwesomeVersion = "4.7.0"
val npmFileLoaderVersion = "1.1.11"
val npmBootstrapVersion = "3.3.7"
val npmAdminOnRestVersion = "1.4.1"
val npmAorDependentInput = "1.2.0"
val npmMaterialUi = "0.20.0"
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

Compile / npmDependencies ++= Seq(
  "react" -> npmReactVersion,
  "react-dom" -> npmReactVersion,
  "react-router" -> npmReactRouterVersion,
  "react-router-dom" -> npmReactRouterVersion,
  "react-google-login" -> npmReactGoogleLogin,
  "admin-on-rest" -> npmAdminOnRestVersion,
  "material-ui" -> npmMaterialUi,
  "font-awesome" -> npmFontAwesomeVersion,
  "bootstrap" -> npmBootstrapVersion,
  "aor-dependent-input" -> npmAorDependentInput
)

Compile / npmDevDependencies ++= Seq(
  "sass-loader" -> npmSassLoaderVersion,
  "node-sass" -> npmNodeSassVersion,
  "extract-text-webpack-plugin" -> npmExtractTextWebpackPluginVersion,
  "css-loader" -> npmCssLoaderVersion,
  "style-loader" -> npmStyleLoaderVersion,
  "clean-webpack-plugin" -> npmCleanWebpackPluginVersion,
  "html-webpack-plugin" -> npmHtmlWebpackPluginVersion,
  "webpack-md5-hash" -> npmWebpackMd5HashVersion,
  "file-loader" -> npmFileLoaderVersion,
  "webpack-dev-server" -> "3.1.4",
  "uglifyjs-webpack-plugin" -> "1.2.5",
  "webpack" -> npmWebpackVersion
)

Compile / npmResolutions := {
  (Compile / npmDependencies).value.toMap ++ (Compile / npmDevDependencies).value.toMap
}

webpack / version := npmWebpackVersion

webpackResources := {
  baseDirectory.value / "src" / "main" / "static" / "sass" ** "*.sass"
}

webpackDevServerPort := 4242

fastOptJS / webpackConfigFile := Some(baseDirectory.value / "make-webpack-library.config.js")
fullOptJS / webpackConfigFile := Some(baseDirectory.value / "make-webpack-prod.config.js")

webpackDevServerExtraArgs := Seq("--lazy", "--inline")

fastOptJS / webpackBundlingMode := BundlingMode.LibraryOnly("makeBackoffice")
fullOptJS / webpackBundlingMode := BundlingMode.Application

scalacOptions ++= {
  if (System.getenv("CI_BUILD") == "true") {
    Seq("-Xelide-below", "OFF")
  } else {
    Seq(
      "-deprecation",
      "-feature"
    )
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
ThisBuild / prepareAssets := {
  val npmDirectory = (Compile / npmUpdate).value
  IO.copyDirectory(baseDirectory.value / "src" / "main" / "static", npmDirectory, overwrite = true)

  val buildVersionFile: File = npmDirectory / "dist" / "version"
  val buildVersionLocalFile: File = npmDirectory / "version"
  val contents: String = buildVersion.value

  IO.write(buildVersionFile, contents)
  IO.write(buildVersionLocalFile, contents)

  streams.value.log.info("Copy assets to working directory")
}

Compile / fastOptJS := {
  prepareAssets.value
  (Compile / fastOptJS).value
}

Compile / fullOptJS := {
  prepareAssets.value
  (Compile / fullOptJS).value
}

gitCommitMessageHook := Some(baseDirectory.value / "bin" / "commit-msg.hook")

enablePlugins(GitHooks)

useYarn := true

git.formattedShaVersion := git.gitHeadCommit.value map { sha => sha.take(10) }

ThisBuild / version := {
  git.formattedShaVersion.value.get
}

enablePlugins(GitVersioning)